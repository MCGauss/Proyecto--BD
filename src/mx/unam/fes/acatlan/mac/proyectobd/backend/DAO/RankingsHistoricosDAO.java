package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Predicciones;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.RankingsHistoricos;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RankingsHistoricosDAO {

	private final Connection conexion;

    // Constructor que recibe la conexión activa a la base de datos
    public RankingsHistoricosDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * INSERT: Guarda un registro de ranking calculado.
     * Se ejecuta al "cerrar" la jornada para congelar el puntaje del usuario.
     */
    public boolean registrarRanking(RankingsHistoricos ranking) {
        String query = "INSERT INTO rankings_historicos (id_usuario, id_prediccion, puntos, aciertos, errores) "
                   + "VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, ranking.getUsuario().getIdUsuario());
            ps.setInt(2, ranking.getPrediccion().getIdPrediccion());
            ps.setInt(3, ranking.getPuntos());
            ps.setInt(4, ranking.getAciertos()); 
            ps.setInt(5, ranking.getErrores());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * SELECT: Obtiene la tabla de posiciones ordenada de mayor a menor puntaje.
     * Este método es el que usará tu JTable de Java Swing para mostrar quién va ganando.
     */
    public List<RankingsHistoricos> obtenerTablaPosiciones() {
        List<RankingsHistoricos> lista = new ArrayList<>();
        
        // Ordenamos por puntos (descendente), luego por aciertos (descendente) y errores (ascendente) para desempatar
        String query = "SELECT id_ranking, id_usuario, id_prediccion, puntos, aciertos, errores "
                   + "FROM rankings_historicos "
                   + "ORDER BY puntos DESC, aciertos DESC, errores ASC;";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RankingsHistoricos rh = new RankingsHistoricos();
                rh.setIdRanking(rs.getInt("id_ranking"));
                rh.setPuntos(rs.getInt("puntos"));
                rh.setAciertos(rs.getInt("aciertos"));
                rh.setErrores(rs.getInt("errores"));

                // Armamos el cascarón de Usuarios
                Usuarios u = new Usuarios();
                u.setIdUsuario(rs.getInt("id_usuario"));
                rh.setUsuario(u);

                // Armamos el cascarón de Predicciones
                Predicciones p = new Predicciones();
                p.setIdPrediccion(rs.getInt("id_prediccion"));
                rh.setPrediccion(p);

                lista.add(rh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * UPDATE: Permite recalcular o corregir el ranking de un usuario si hubo una modificación administrativa.
     */
    public boolean actualizarRanking(RankingsHistoricos ranking) {
        String query = "UPDATE rankings_historicos SET puntos = ?, aciertos = ?, errores = ? "
                   + "WHERE id_ranking = ?;";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, ranking.getPuntos());
            ps.setInt(2, ranking.getAciertos());
            ps.setInt(3, ranking.getErrores());
            ps.setInt(4, ranking.getIdRanking());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
