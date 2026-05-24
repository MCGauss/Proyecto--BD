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

    /**
     * Constructor que recibe la conexión activa (Arquitectura MCGauss & Diana).
     */
    public RankingsHistoricosDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * INSERT: Guarda un registro de ranking calculado.
     * CORREGIDO: Se cambia 'puntos' por 'puntos_totales' en estricto apego al LDD.
     * @throws SQLException Propaga el error para control transaccional en el Cierre de Jornada.
     */
    public boolean registrarRanking(RankingsHistoricos ranking) throws SQLException {
        String query = "INSERT INTO rankings_historicos (id_usuario, id_prediccion, puntos_totales, aciertos, errores) "
                     + "VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, ranking.getUsuario().getIdUsuario());
            ps.setInt(2, ranking.getPrediccion().getIdPrediccion());
            ps.setInt(3, ranking.getPuntos()); // Mapea a puntos_totales
            ps.setInt(4, ranking.getAciertos()); 
            ps.setInt(5, ranking.getErrores());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * SELECT: Obtiene la tabla de posiciones ordenada de mayor a menor puntaje.
     * ENRIQUECIDO: Se añade INNER JOIN con la tabla 'usuarios' para traer el 'username' 
     * directo de la BD y poblar el JTable de forma profesional.
     */
    public List<RankingsHistoricos> obtenerTablaPosiciones() {
        List<RankingsHistoricos> lista = new ArrayList<>();
        
        // Query adaptado a la columna 'puntos_totales' y desempatado por aciertos/errores
        String query = "SELECT rh.id_ranking, rh.id_usuario, rh.id_prediccion, rh.puntos_totales, rh.aciertos, rh.errores, "
                     + "       u.username "
                     + "FROM rankings_historicos rh "
                     + "INNER JOIN usuarios u ON rh.id_usuario = u.id_usuario "
                     + "ORDER BY rh.puntos_totales DESC, rh.aciertos DESC, rh.errores ASC;";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RankingsHistoricos rh = new RankingsHistoricos();
                rh.setIdRanking(rs.getInt("id_ranking"));
                rh.setPuntos(rs.getInt("puntos_totales")); // Set de puntos leyendo la columna oficial
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * UPDATE: Modifica o recalcula los puntajes históricos de una predicción.
     * CORREGIDO: Adaptado a la columna 'puntos_totales'.
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
}
