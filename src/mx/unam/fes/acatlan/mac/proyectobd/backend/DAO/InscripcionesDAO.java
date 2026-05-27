package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Inscripciones;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Jornadas;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.TipoInscripcion;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InscripcionesDAO {

    private final Connection conexion;

    // Constructor que recibe la conexión activa
    public InscripcionesDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * INSERT: Registra una nueva inscripción (pago de cuota).
     * Soporta que el id_jornada sea NULL si es una inscripción global del torneo ($500).
     * * COMPATIBILIDAD: Lanza SQLException hacia la capa superior para que la interfaz pueda 
     * recuperar y mostrar el mensaje de error personalizado lanzado por el TRIGGER en Postgres.
     */
    public boolean registrarInscripcion(Inscripciones ins) throws SQLException {
        String query = "INSERT INTO inscripciones (id_usuario, id_jornada, id_tipo_inscripcion, cuota_pagada) "
                   + "VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, ins.getUsuario().getIdUsuario());
            
            // Regla de negocio: Si la jornada es null (Inscripción Global), guardamos NULL en la BD
            if (ins.getJornada() == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, ins.getJornada().getIdJornada());
            }
            
            ps.setInt(3, ins.getTipoInscripcion().getIdTipoInscripcion());
            ps.setDouble(4, ins.getMontoTrans()); // Usamos el método heredado de Transaccion

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Mandamos el error completo (incluyendo el RAISE EXCEPTION del trigger) al controlador/interfaz
            throw e;
        }
    }
    
    /**
     * SELECT: Cuenta los torneos completos a los que se ha inscrito el usuario.
     */
    public int contarTorneosInscritos(int idUsuario) {
        // Ajusta el id_tipo_inscripcion según tu catálogo (ej. 2 para Torneo Completo) o si id_jornada IS NULL
        String query = "SELECT COUNT(*) FROM inscripciones WHERE id_usuario = ? AND id_jornada IS NULL;";
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * SELECT: Verifica si un usuario ya pagó su inscripción para una jornada específica.
     * Sirve para bloquear las pantallas de apuestas en Swing si no han pagado sus $50.
     */
    public boolean verificarInscripcionJornada(int idUsuario, int idJornada) {
        String query = "SELECT COUNT(*) FROM inscripciones WHERE id_usuario = ? AND id_jornada = ?;";
        
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idJornada);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * SELECT: Verifica si un usuario cuenta con la inscripción global del torneo ($500).
     */
    public boolean verificarInscripcionGlobal(int idUsuario) {
        String query = "SELECT COUNT(*) FROM inscripciones WHERE id_usuario = ? AND id_jornada IS NULL;";
        
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * SELECT: Obtiene el historial de todas las inscripciones de un usuario.
     * Ideal para mostrar el estado de cuenta en un JTable de Java Swing.
     */
    public List<Inscripciones> obtenerPorUsuario(int idUsuario) {
        List<Inscripciones> lista = new ArrayList<>();
        String query = "SELECT id_inscripcion, id_jornada, id_tipo_inscripcion, cuota_pagada "
                   + "FROM inscripciones WHERE id_usuario = ?;";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inscripciones ins = new Inscripciones();
                    ins.setIdTrans(rs.getInt("id_inscripcion"));
                    ins.setMontoTrans(rs.getDouble("cuota_pagada"));
                    
                    // Cascarón del Usuario
                    Usuarios u = new Usuarios();
                    u.setIdUsuario(idUsuario);
                    ins.setUsuario(u);

                    // Cascarón de Jornada (Manejo de campos NULLABLE)
                    int idJornada = rs.getInt("id_jornada");
                    if (!rs.wasNull()) {
                        Jornadas j = new Jornadas();
                        j.setIdJornada(idJornada);
                        ins.setJornada(j);
                    } else {
                        ins.setJornada(null); // Significa que es una inscripción del torneo completo
                    }

                    // Cascarón de TipoInscripcion
                    TipoInscripcion tipo = new TipoInscripcion();
                    tipo.setIdTipoInscripcion(rs.getInt("id_tipo_inscripcion"));
                    ins.setTipoInscripcion(tipo);

                    lista.add(ins);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}