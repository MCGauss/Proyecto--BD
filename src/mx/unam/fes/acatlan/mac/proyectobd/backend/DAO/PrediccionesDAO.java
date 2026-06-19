package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Predicciones;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Partido;

import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrediccionesDAO {

    private final Connection conexion;
    
    // Constructor unificado que recibe la conexión activa (Arquitectura MCGauss & Diana)
    public PrediccionesDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * UPSERT (INSERT / UPDATE): Guarda o actualiza un pronóstico por usuario.
     * Sincronizado con la restricción UNIQUE (id_usuario, id_partido) del LDD.
     * @param prediccion Objeto con los goles pronosticados.
     * @return true si la operación se realizó con éxito.
     * @throws SQLException Lanza la excepción íntegra para que Java Swing capture las excepciones de los triggers 
     * (apuestas fuera de tiempo, partido iniciado o usuario no inscrito en la jornada).
     */
    public boolean guardarActualizarPred(Predicciones prediccion) throws SQLException {
        // Sentencia SQL que aprovecha el índice único definido en tu LDD
        String query = "INSERT INTO predicciones (id_usuario, id_partido, pred_goles_local, pred_goles_vis, puntos_obtenidos) "
                     + "VALUES (?, ?, ?, ?, ?) "
                     + "ON CONFLICT (id_usuario, id_partido) " 
                     + "DO UPDATE SET pred_goles_local = EXCLUDED.pred_goles_local, "
                     + "              pred_goles_vis = EXCLUDED.pred_goles_vis;";
         
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, prediccion.getUsuario().getIdUsuario());
            ps.setInt(2, prediccion.getPartido().getIdPartido());
            ps.setInt(3, prediccion.getPredGolesLocal());
            ps.setInt(4, prediccion.getPredGolesVis());
             
            // Asignación de puntos obtenidos (inicialmente NULL hasta que jueguen el partido)
            if (prediccion.getPuntosObtenidos() == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, prediccion.getPuntosObtenidos());
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Se propaga el error para congelar transacciones inválidas por tiempo o estatus en la BD
            throw e;
        }
    }
    
    /**
     * SELECT: Obtiene las predicciones de un usuario específico en una jornada.
     * Útil para pintar la matriz horizontal/vertical de apuestas en un JTable de Java Swing.
     */
    public List<Predicciones> obtenerPorUsuarioYJornada(int idUsuario, int idJornada) {
        List<Predicciones> predicciones = new ArrayList<>();
        
        // Mapeo fiel a las llaves foráneas y nombres del LDD físico
        String query = "SELECT pr.id_prediccion, pr.id_usuario, pr.id_partido, pr.pred_goles_local, "
                     + "pr.pred_goles_vis, pr.puntos_obtenidos "
                     + "FROM predicciones pr "
                     + "INNER JOIN partido pa ON pr.id_partido = pa.id_partido "
                     + "WHERE pr.id_usuario = ? AND pa.id_jornada = ?;";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idJornada);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Predicciones pred = new Predicciones();
                    pred.setIdPrediccion(rs.getInt("id_prediccion"));
                    pred.setPredGolesLocal(rs.getInt("pred_goles_local"));
                    pred.setPredGolesVis(rs.getInt("pred_goles_vis"));
                    
                    // Manejo del Integer Nullable de la columna 'puntos_obtenidos'
                    int pts = rs.getInt("puntos_obtenidos");
                    pred.setPuntosObtenidos(rs.wasNull() ? null : pts);
                    
                    // Construcción de los cascarones relacionales
                    Usuarios u = new Usuarios(); 
                    u.setIdUsuario(idUsuario);
                    
                    Partido p = new Partido(); 
                    p.setIdPartido(rs.getInt("id_partido"));
                    
                    pred.setUsuario(u);
                    pred.setPartido(p);
                    
                    predicciones.add(pred);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return predicciones;
    }

    /**
     * UPDATE: Método de respaldo para asignarle o corregir los puntos de forma manual.
     * Nota: Recuerda que de esto ya se encarga de forma automatizada tu trigger 'tg_calcular_puntos_prediccion'.
     */
    public boolean actualizarPuntosGanados(int idPrediccion, Integer puntos) throws SQLException {
        String query = "UPDATE predicciones SET puntos_obtenidos = ? WHERE id_prediccion = ?;";
        
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            if (puntos == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, puntos);
            }
            ps.setInt(2, idPrediccion);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw e;
        }
    }
    
    /**
     * SELECT: Cuenta cuántas predicciones tiene activas (partidos programados) el usuario.
     */
    public int contarPrediccionesActivas(int idUsuario) {
        String query = "SELECT COUNT(*) FROM predicciones JOIN partido USING (id_partido) WHERE id_status_partido = 1 AND id_usuario = ?;";
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
}
