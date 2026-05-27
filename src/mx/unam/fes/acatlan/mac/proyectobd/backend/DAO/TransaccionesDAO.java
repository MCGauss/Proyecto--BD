package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TransaccionesDAO {

    private Connection conexion;

    public TransaccionesDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Consulta el historial financiero unificando las tablas del LDD mediante UNION ALL.
     * Mapea dinámicamente el tipo de movimiento sin necesidad de que exista en las tablas.
     */
    public List<Map<String, Object>> obtenerHistorialPorUsuario(int idUsuario) throws SQLException {
        List<Map<String, Object>> historial = new ArrayList<>();

        // 1. RECARGAS: Leemos directo de la tabla transaccion
        // 2. INSCRIPCIONES: Como no tiene fecha, usamos 'NOW()' formateado o un texto descriptivo
        // 3. PREMIOS: Unimos pagos_premios con ganadores usando id_ganador
        String sql = 
        		"SELECT CAST(fecha_trans AS VARCHAR) AS fecha_mov, monto_trans AS monto_mov, 'RECARGA' AS tipo_mov " +
        			    "FROM transaccion " +
        			    "WHERE id_usuario = ? " +
        			    "UNION ALL " +
        			    "SELECT 'Sin fecha' AS fecha_mov, cuota_pagada AS monto_mov, 'INSCRIPCIÓN' AS tipo_mov " +
        			    "FROM inscripciones " +
        			    "WHERE id_usuario = ? " +
        			    "UNION ALL " +
        			    "SELECT CAST(p.fecha_pago AS VARCHAR) AS fecha_mov, p.monto_pagado AS monto_mov, 'PREMIO' AS tipo_mov " +
        			    "FROM pagos_premios p " +
        			    "INNER JOIN usuarios_ganadores g ON p.id_ganador = g.id_ganador " +
        			    "WHERE g.id_usuario = ? " +
        			    "ORDER BY 1 DESC";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Pasamos el ID del usuario logueado a los 3 bloques del UNION
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            ps.setInt(3, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new LinkedHashMap<>();
                    
                    fila.put("fecha", rs.getString("fecha_mov"));
                    
                    String tipo = rs.getString("tipo_mov");
                    fila.put("tipo", tipo);

                    double monto = rs.getDouble("monto_mov");
                    // Agregamos el formato visual (+ / -) dependiendo del flujo de caja
                    if (tipo.equals("INSCRIPCIÓN")) {
                        fila.put("monto", "-$" + monto);
                    } else {
                        fila.put("monto", "+$" + monto);
                    }

                    historial.add(fila);
                }
            }
        }
        return historial;
    }
}