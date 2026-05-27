package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement; // NUEVO: Cambiado por PreparedStatement
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Jornadas;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Torneos;

public class JornadasDAO {
    
    private final Connection conexion;

    // Constructor unificado que recibe la conexión activa desde el controlador superior
    public JornadasDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * SELECT: Obtiene las jornadas de un torneo específico.
     * Al ser un catálogo de consulta visual, es 100% compatible con tus scripts de backend.
     * @param idTorneo Identificador único del torneo.
     * @return Lista de jornadas asociadas.
     */
    public List<Jornadas> obtenerJornadasPorTorneo(int idTorneo) {
        List<Jornadas> lista = new ArrayList<>();
        String query = "SELECT id_jornada, nombre_jornada, id_torneo FROM jornadas WHERE id_torneo = ?;";
        
        // Usamos try-with-resources para un cierre automático de recursos seguro
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idTorneo);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Mapeo de datos respetando tu constructor de Torneos
                    Torneos torneoTemporal = new Torneos(rs.getInt("id_torneo"), null, null, null, null);
                    
                    // Creamos el objeto Jornadas respetando tu modelo
                    Jornadas jornada = new Jornadas(
                        rs.getInt("id_jornada"),
                        rs.getString("nombre_jornada"),
                        torneoTemporal
                    );
                    
                    lista.add(jornada);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lista;
    }
    
    /**
     * SELECT: Obtiene la jornada ACTUAL mapeando el nombre_jornada y nombre_torneo.
     * Se define como ACTUAL si tiene partidos en estado 2 (EN_CURSO) o 3 (FINALIZADO),
     * o por su fecha/hora de programación con respecto al momento actual.
     */
    public Map<String, String> obtenerJornadaActual() {
        Map<String, String> datos = new HashMap<>();
        String query = 
            "SELECT j.id_jornada, j.nombre_jornada, t.nombre_torneo " +
            "FROM jornadas j " +
            "INNER JOIN torneos t ON j.id_torneo = t.id_torneo " +
            "INNER JOIN partido p ON j.id_jornada = p.id_jornada " +
            "WHERE p.id_status_partido = 2 OR p.fecha_hora_prog <= NOW() " +
            "ORDER BY p.fecha_hora_prog DESC LIMIT 1;";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                datos.put("id_jornada", String.valueOf(rs.getInt("id_jornada")));
                datos.put("nombre", rs.getString("nombre_jornada") + " - " + rs.getString("nombre_torneo"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener Jornada Actual: " + e.getMessage());
        }
        return datos;
    }

    /**
     * SELECT: Obtiene la PRÓXIMA jornada disponible en el sistema.
     * Se define como la jornada inmediata superior cuyos partidos están programados (id_status = 1) 
     * y cuya fecha de inicio es posterior a la actual.
     */
    public Map<String, String> obtenerProximaJornada() {
        Map<String, String> datos = new HashMap<>();
        String query = 
            "SELECT j.id_jornada, j.nombre_jornada, t.nombre_torneo " +
            "FROM jornadas j " +
            "INNER JOIN torneos t ON j.id_torneo = t.id_torneo " +
            "INNER JOIN partido p ON j.id_jornada = p.id_jornada " +
            "WHERE p.id_status_partido = 1 AND p.fecha_hora_prog > NOW() " +
            "ORDER BY p.fecha_hora_prog ASC LIMIT 1;";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                datos.put("id_jornada", String.valueOf(rs.getInt("id_jornada")));
                datos.put("nombre", rs.getString("nombre_jornada") + " - " + rs.getString("nombre_torneo"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener Próxima Jornada: " + e.getMessage());
        }
        return datos;
    }
}
