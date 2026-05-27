package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Equipos;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Jornadas;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Partido;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.StatusPartido;

public class PartidosDAO {

    private final Connection conexion;

    /**
     * Constructor unificado. Recibe la conexión activa desde la capa superior
     * para permitir control transaccional global.
     */
    public PartidosDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * SELECT: Trae la cartelera de partidos de una jornada específica.
     * Sincronizado con los nombres exactos de columnas del LDD (goles_eq_local, goles_eq_vis).
     * @param idJornada Identificador único de la jornada.
     * @return Lista de objetos Partido mapeados desde PostgreSQL.
     */
    public List<Partido> obtenerPartidosPorJornada(int idJornada) {
        List<Partido> lista = new ArrayList<>();
        
        // Query adaptado fielmente al LDD de las tablas 'partido' y 'status_partido'
        String query = "SELECT p.id_partido, p.id_jornada, p.goles_eq_local, p.goles_eq_vis, p.fecha_hora_prog, " +
                       "sp.status_partido AS descripcion_status " +
                       "FROM partido p " +
                       "JOIN status_partido sp ON p.id_status_partido = sp.id_status_partido " +
                       "WHERE p.id_jornada = ?;";
                       
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setInt(1, idJornada);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Mapeo relacional básico
                    Jornadas jornadaTemporal = new Jornadas(rs.getInt("id_jornada"), null, null);
                    
                    // Objetos auxiliares para evitar valores nulos en la vista Swing
                    Equipos localTemporal = new Equipos(0, "Local", null, null);
                    Equipos visitanteTemporal = new Equipos(0, "Visitante", null, null);
                    
                    // Conversión segura de TIMESTAMP a LocalDateTime
                    Timestamp ts = rs.getTimestamp("fecha_hora_prog");
                    LocalDateTime fechaHora = (ts != null) ? ts.toLocalDateTime() : null;
                    
                    // Conversión de la cadena física al Enum de Java (ej. "FINALIZADO" -> StatusPartido.FINALIZADO)
                    String descStatusDb = rs.getString("descripcion_status").replace(" ", "_").toUpperCase();
                    StatusPartido statusEnum = StatusPartido.valueOf(descStatusDb);
                    
                    Partido partido = new Partido(
                        rs.getInt("id_partido"),
                        jornadaTemporal,
                        localTemporal,
                        visitanteTemporal,
                        null, 
                        null,
                        fechaHora,
                        statusEnum
                    );
                    
                    // COMPATIBILIDAD LDD: Mapeo exacto de las columnas 'goles_eq_local' y 'goles_eq_vis'
                    if (rs.getObject("goles_eq_local") != null) {
                        partido.setGolesLocal(rs.getInt("goles_eq_local"));
                    }
                    if (rs.getObject("goles_eq_vis") != null) {
                        partido.setGolesVisitante(rs.getInt("goles_eq_vis"));
                    }
                    
                    lista.add(partido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * UPDATE: Modifica el marcador y estatus de un encuentro desde el panel de administración.
     * @param idPartido Identificador del partido a actualizar.
     * @param golesL Goles del equipo local (mapeado a goles_eq_local).
     * @param golesV Goles del equipo visitante (mapeado a goles_eq_vis).
     * @param nuevoEstatus Estado de juego proveniente del combo de Swing.
     * @return true si el registro fue afectado exitosamente.
     * @throws SQLException Propaga la excepción íntegra. Es crucial para que Swing capture los fallos de
     * los candados CHECK (valores negativos) o los errores del trigger 'tg_calcular_puntos_prediccion'.
     */
    public boolean actualizarMarcadorYEstatus(int idPartido, int golesL, int golesV, StatusPartido nuevoEstatus) throws SQLException {
        // Mapeo inverso del Enum hacia las llaves primarias de la tabla 'status_partido'
        int idStatusDb = 1; // PROGRAMADO por defecto
        if (nuevoEstatus == StatusPartido.EN_CURSO) idStatusDb = 2;
        if (nuevoEstatus == StatusPartido.FINALIZADO) idStatusDb = 3;
        if (nuevoEstatus == StatusPartido.POSPUESTO) idStatusDb = 4;
        
        // COMPATIBILIDAD LDD: Sentencia UPDATE que impacta directamente las columnas físicas reales
        String query = "UPDATE partido SET goles_eq_local = ?, goles_eq_vis = ?, id_status_partido = ? WHERE id_partido = ?;";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setInt(1, golesL);
            pstmt.setInt(2, golesV);
            pstmt.setInt(3, idStatusDb);
            pstmt.setInt(4, idPartido);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // No silenciamos el error con un printStackTrace pasivo; lo lanzamos para congelar transacciones erróneas
            throw e;
        }
    }
    
    /**
     * SELECT: Trae la cartelera completa de partidos de TODOS los campos 
     * asociados al torneo activo (id_torneo).
     */
    public List<Partido> obtenerPartidosPorTorneo(int idTorneo) {
        List<Partido> lista = new ArrayList<>();
        String query = 
            "SELECT p.id_partido, p.id_jornada, p.id_eq_local, p.id_eq_vis, " +
            "       p.goles_eq_local, p.goles_eq_vis, p.fecha_hora_prog, p.id_status_partido, " +
            "       el.nombre_equipo AS local_nombre, ev.nombre_equipo AS vis_nombre " +
            "FROM partido p " +
            "INNER JOIN jornadas j ON p.id_jornada = j.id_jornada " +
            "INNER JOIN equipos el ON p.id_eq_local = el.id_equipo " +
            "INNER JOIN equipos ev ON p.id_eq_vis = ev.id_equipo " +
            "WHERE j.id_torneo = ? " +
            "ORDER BY j.id_jornada ASC, p.fecha_hora_prog ASC;";

        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setInt(1, idTorneo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Partido partido = new Partido();
                    partido.setIdPartido(rs.getInt("id_partido"));
                    partido.setGolesLocal(rs.getInt("goles_eq_local"));
                    partido.setGolesVisitante(rs.getInt("goles_eq_vis"));
                    
                    // Mapeo de equipos de manera local para las etiquetas
                    Equipos local = new Equipos(rs.getInt("id_eq_local"), rs.getString("local_nombre"), null, null);
                    Equipos visitante = new Equipos(rs.getInt("id_eq_vis"), rs.getString("vis_nombre"), null, null);
                    partido.setEquipoLocal(local);
                    partido.setEquipoVisitante(visitante);
                    
                    lista.add(partido);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar partidos del torneo: " + e.getMessage());
        }
        return lista;
    }
}