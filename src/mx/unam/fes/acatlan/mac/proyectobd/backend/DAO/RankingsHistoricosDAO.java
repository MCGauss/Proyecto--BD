package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Predicciones;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.RankingsHistoricos;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RankingsHistoricosDAO {

    private final Connection conexion;

    /**
     * Constructor que recibe la conexión activa (Arquitectura MCGauss & Diana).
     */
    public RankingsHistoricosDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * INSERT: Guarda un registro de ranking calculado.
     * @throws SQLException Propaga el error para control transaccional en el Cierre de Jornada.
     */
    public boolean registrarRanking(RankingsHistoricos ranking) throws SQLException {
        String query = "INSERT INTO rankings_historicos (id_usuario, id_prediccion, puntos_totales, aciertos, errores)"
                     + "VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, ranking.getUsuario().getIdUsuario());
            ps.setInt(2, ranking.getPrediccion().getIdPrediccion());
            ps.setInt(3, ranking.getPuntos());
            ps.setInt(4, ranking.getAciertos()); 
            ps.setInt(5, ranking.getErrores());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * SELECT: Obtiene la tabla de posiciones ordenada de mayor a menor
     * con un JOIN con la tabla 'usuarios' para traer el 'username' 
     * y mostrar en un jTable.
     */
    public List<RankingsHistoricos> obtenerRankingPorJornada(int idJornada) {
        List<RankingsHistoricos> lista = new ArrayList<>();
        
        String query = "SELECT rh.id_ranking, rh.puntos_totales, rh.aciertos, rh.errores, rh.id_usuario, u.username, rh.id_prediccion"
                     + "FROM rankings_historicos rh"
                     + "JOIN usuarios u ON rh.id_usuario = u.id_usuario"
                     + "JOIN predicciones p ON rh.id_prediccion = p.id_prediccion "
                     + "JOIN partido pa ON p.id_partido = pa.id_partido "
                     + "WHERE pa.id_jornada = ?"
                     + "ORDER BY rh.puntos_totales DESC, rh.aciertos DESC;";             
                     		
        try (PreparedStatement ps = conexion.prepareStatement(query)){
        	ps.setInt(1, idJornada);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RankingsHistoricos rh = new RankingsHistoricos();
                    rh.setIdRanking(rs.getInt("id_ranking"));
                    rh.setPuntos(rs.getInt("puntos_totales"));
                    rh.setAciertos(rs.getInt("aciertos"));
                    rh.setErrores(rs.getInt("errores"));

                    // Poblamos el objeto de negocio Usuario incluyendo su nombre en texto para la UI
                    Usuarios u = new Usuarios();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setUsername(rs.getString("username")); 
                    rh.setUsuario(u);

                    // Armamos el cascarón de Predicciones
                    Predicciones p = new Predicciones();
                    p.setIdPrediccion(rs.getInt("id_prediccion"));
                    rh.setPrediccion(p);

                    lista.add(rh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * UPDATE: Modifica o recalcula los puntajes históricos de una predicción.
     */
    public boolean actualizarRanking(RankingsHistoricos ranking) throws SQLException {
        String query = "UPDATE rankings_historicos SET puntos_totales = ?, aciertos = ?, errores = ? "
                     + "WHERE id_ranking = ?;";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, ranking.getPuntos());
            ps.setInt(2, ranking.getAciertos());
            ps.setInt(3, ranking.getErrores());
            ps.setInt(4, ranking.getIdRanking());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * CALLABLE STATEMENT: 
     * Este método ejecuta el procedimiento almacenado de SQL.
     * @param idJornada Identificador único de la jornada a cerrar.
     * @return true si se procesó de forma correcta el cierre en el motor de la base de datos.
     * @throws SQLException Se propaga el error para que la ventana de administración en Swing pueda alertar al usuario si falla algo.
     */
    public boolean procesarCierreDeJornadaOficial(int idJornada) throws SQLException {
        String query = "{CALL sp_cerrar_jornada_oficial(?)}";

        try (CallableStatement cstmt = conexion.prepareCall(query)) {
            cstmt.setInt(1, idJornada);
            
            cstmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error crítico en CallableStatement sp_cerrar_jornada_oficial: " + e.getMessage());
            throw e;
        }
    }
}
