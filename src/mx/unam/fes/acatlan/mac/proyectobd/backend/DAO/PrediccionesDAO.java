package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Predicciones;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;
//import mx.unam.fes.acatlan.mac.proyectobd.backend.ConexionBD;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Partido;
import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PrediccionesDAO {

	private final Connection conexion;
	
	//Constructor (conexión activa)
	public PrediccionesDAO(Connection conexion) {
		this.conexion = conexion;
	}

	/**** UPSERT (INSERT / UPDATE) *****
	 * Guardar o actualizar un pronóstico por usuario.
	 * Validar si existe apuesta o aún no.
	 */
	
	public boolean guardarActualizarPred(Predicciones prediccion) {
		//sentencia SQL
		String query = "INSERT INTO predicciones (id_usuario, id_partido, pred_goles_local, pred_goles_vis, puntos_obtenidos) "
                + "VALUES (?, ?, ?, ?, ?) "
                + "ON CONFLICT (id_usuario, id_partido) " 
                + "DO UPDATE SET pred_goles_local = EXCLUDED.pred_goles_local, pred_goles_vis = EXCLUDED.pred_goles_vis;";
     
	//Obtener valores directamente de la conexion ccon la BD
     try (PreparedStatement ps = conexion.prepareStatement(query)) {
         ps.setInt(1, prediccion.getUsuario().getIdUsuario());
         ps.setInt(2, prediccion.getPartido().getIdPartido());
         ps.setInt(3, prediccion.getPredGolesLocal());
         ps.setInt(4, prediccion.getPredGolesVis());
         
         //Asignación de puntos obtenidoss
         if (prediccion.getPuntosObtenidos() == null) {
             ps.setNull(5, java.sql.Types.INTEGER);
         } else {
             ps.setInt(5, prediccion.getPuntosObtenidos());
         }

         return ps.executeUpdate() > 0;
     } catch (SQLException e) {
         e.printStackTrace();
         return false;
     }
	}
	
	/**
     ***** SELECT *****
     * Obtiene las predicciones de un usuario específico en una jornada.
     * Útil para pintar la matriz horizontal/vertical de apuestas (Imagen 1000320115.jpg).
     */
    public List<Predicciones> obtenerPorUsuarioYJornada(int idUsuario, int idJornada) {
        List<Predicciones> predicciones = new ArrayList<>();
        
        // Hacer JOIN con Partido para filtrar por la Jornada correcta
        String query = "SELECT pr.* FROM predicciones pr "
                   + "INNER JOIN partido pa ON pr.id_partido = pa.id_partido "
                   + "WHERE pr.id_usuario = ? AND pa.id_jornada = ?;";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idJornada);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    //Instanciar objeto Predicciones
                    Predicciones pred = new Predicciones();
                    pred.setIdPrediccion(rs.getInt("id_prediccion"));
                    pred.setPredGolesLocal(rs.getInt("pred_goles_local"));
                    pred.setPredGolesVis(rs.getInt("pred_goles_vis"));
                    
                    // Manejo de un Integer Nullable de la BD
                    int pts = rs.getInt("puntos_obtenidos");
                    pred.setPuntosObtenidos(rs.wasNull() ? null : pts);
                    
                    // Asignamos cascarones de los objetos para que los servicios los terminen de poblar si es necesario
                    Usuarios u = new Usuarios(); u.setIdUsuario(idUsuario);
                    Partido p = new Partido(); p.setIdPartido(rs.getInt("id_partido"));
                    
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
     ***** UPDATE ***** 
     * Método que invoca el CierreJornadaService para asignarle los puntos derivados 
     * a cada apuesta individual una vez finalizado el encuentro.
     */
    public boolean actualizarPuntosGanados(int idPrediccion, Integer puntos) {
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
            e.printStackTrace();
            return false;
        }
    }
}
