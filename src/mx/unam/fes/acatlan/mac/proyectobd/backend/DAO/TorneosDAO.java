package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Ligas;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Torneos;

public class TorneosDAO {

    private final Connection conexion;

    /**
     * Constructor unificado que recibe la conexión activa (Arquitectura MCGauss & Diana).
     */
    public TorneosDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * SELECT: Obtiene la lista completa de torneos registrados.
     * CORREGIDO: Mapeo de fechas adaptado al tipo TIMESTAMP del LDD usando getTimestamp().
     * @return Lista de objetos Torneos.
     */
    public List<Torneos> listarTorneos() {
        List<Torneos> lista = new ArrayList<>();
        String query = "SELECT id_torneo, id_liga, nombre_torneo, fecha_inicio, fecha_fin FROM torneos;";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
                
            while (rs.next()) {
                // Mapeo relacional con la entidad Ligas (Tabla 1 del LDD)
                Ligas ligaTemporal = new Ligas(rs.getInt("id_liga"), null);
                
                // CORRECCIÓN LDD: Leemos como Timestamp porque en la BD son de tipo TIMESTAMP
                Timestamp tsInicio = rs.getTimestamp("fecha_inicio");
                Timestamp tsFin = rs.getTimestamp("fecha_fin");
                
                // Conversión segura de LocalDateTime a LocalDate para tu modelo
                LocalDate fechaInicio = (tsInicio != null) ? tsInicio.toLocalDateTime().toLocalDate() : null;
                LocalDate fechaFin = (tsFin != null) ? tsFin.toLocalDateTime().toLocalDate() : null;
                
                // Construcción de la entidad de negocio respetando tu constructor
                Torneos torneo = new Torneos(
                    rs.getInt("id_torneo"),
                    ligaTemporal,
                    rs.getString("nombre_torneo"),
                    fechaInicio,
                    fechaFin
                );
                
                lista.add(torneo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    /**
     * SELECT: Obtiene el torneo activo, el número de su jornada actual (calculado)
     * y el total de jornadas que componen dicho torneo.
     */
    public java.util.Map<String, String> obtenerInformacionTorneoHub() {
        java.util.Map<String, String> datos = new java.util.HashMap<>();
        
        // Query principal: Busca el torneo que tenga la jornada con partidos activos/recientes
        String query = 
            "SELECT t.id_torneo, t.nombre_torneo, j.id_jornada, j.nombre_jornada, " +
            "       (SELECT COUNT(*) FROM jornadas j2 WHERE j2.id_torneo = t.id_torneo) as total_jornadas " +
            "FROM torneos t " +
            "INNER JOIN jornadas j ON t.id_torneo = j.id_torneo " +
            "INNER JOIN partido p ON j.id_jornada = p.id_jornada " +
            "WHERE p.id_status_partido IN (2, 3) OR p.fecha_hora_prog <= NOW() " +
            "ORDER BY p.fecha_hora_prog DESC LIMIT 1;";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                datos.put("id_torneo", String.valueOf(rs.getInt("id_torneo")));
                datos.put("nombre_torneo", rs.getString("nombre_torneo"));
                datos.put("total_jornadas", String.valueOf(rs.getInt("total_jornadas")));
                
                // Extraemos solo el número de la jornada (Ej: De "Jornada 5" extrae "5")
                String nombreJornada = rs.getString("nombre_jornada");
                String numeroJornada = nombreJornada.replaceAll("[^0-9]", "");
                datos.put("jornada_actual", numeroJornada.isEmpty() ? "1" : numeroJornada);
            } else {
                // FALLBACK DE SEGURIDAD: Si no hay partidos en curso o las fechas están desfasadas,
                // tomamos el último torneo registrado y su primera jornada.
                String fallbackQuery = 
                    "SELECT t.id_torneo, t.nombre_torneo, " +
                    "       (SELECT COUNT(*) FROM jornadas j2 WHERE j2.id_torneo = t.id_torneo) as total_jornadas " +
                    "FROM torneos t ORDER BY t.fecha_inicio DESC LIMIT 1;";
                
                try (PreparedStatement ps2 = conexion.prepareStatement(fallbackQuery);
                     ResultSet rs2 = ps2.executeQuery()) {
                    if (rs2.next()) {
                        datos.put("id_torneo", String.valueOf(rs2.getInt("id_torneo")));
                        datos.put("nombre_torneo", rs2.getString("nombre_torneo"));
                        datos.put("total_jornadas", String.valueOf(rs2.getInt("total_jornadas")));
                        datos.put("jornada_actual", "1"); // Por defecto la primera
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener información del Torneo Hub: " + e.getMessage());
        }
        return datos;
    }
}
