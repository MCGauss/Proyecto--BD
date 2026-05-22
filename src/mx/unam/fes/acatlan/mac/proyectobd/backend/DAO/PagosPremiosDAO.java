package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.UsuariosGanadores;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.MetodosPagos;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.PagosPremios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PagosPremiosDAO {

	private final Connection conexion;

    // Constructor que recibe la conexión activa
    public PagosPremiosDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * INSERT: Registra un pago de premio efectuado por el administrador.
     * Guarda la fecha y hora exacta del movimiento y el monto transferido.
     */
    public boolean registrarPagoPremio(PagosPremios pago) {
        String query = "INSERT INTO pagos_premios (id_ganador, id_metodo, fecha_pago, monto_pagado) "
                   + "VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, pago.getGanador().getIdGanador());
            ps.setInt(2, pago.getMetodoPago().getIdMetodo()); // Objeto de la clase MetodosPagos
            
            // Convertimos el LocalDateTime de Java a Timestamp de la Base de Datos
            ps.setTimestamp(3, Timestamp.valueOf(pago.getFechaTrans())); // getFecha() heredado de Transaccion
            ps.setDouble(4, pago.getMontoTrans()); // getMonto() heredado de Transaccion

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * SELECT: Obtiene el historial completo de egresos (premios pagados).
     * Ideal para cargar reportes de contabilidad en un JTable de Java Swing.
     */
    public List<PagosPremios> obtenerHistorialPagos() {
        List<PagosPremios> lista = new ArrayList<>();
        String query = "SELECT id_pago, id_ganador, id_metodo, fecha_pago, monto_pagado FROM pagos_premios;";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PagosPremios pago = new PagosPremios();
                pago.setIdTrans(rs.getInt("id_pago")); // setId() heredado de Transaccion
                pago.setMontoTrans(rs.getDouble("monto_pagado")); // setMonto() heredado de Transaccion
                
                // Convertimos el Timestamp de la BD de vuelta a LocalDateTime de Java
                Timestamp timestamp = rs.getTimestamp("fecha_pago");
                if (timestamp != null) {
                    pago.setFechaTrans(timestamp.toLocalDateTime());
                }

                // Cascarón de Ganador
                UsuariosGanadores g = new UsuariosGanadores();
                g.setIdGanador(rs.getInt("id_ganador"));
                pago.setGanador(g);

                // Cascarón de MetodosPagos (Clase)
                MetodosPagos mp = new MetodosPagos();
                mp.setIdMetodo(rs.getInt("id_metodo"));
                pago.setMetodoPago(mp);

                lista.add(pago);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
