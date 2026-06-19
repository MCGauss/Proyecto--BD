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

    public RankingsHistoricosDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public boolean registrarRanking(RankingsHistoricos ranking) throws SQLException {
        String query = "INSERT INTO rankings_historicos (id_usuario, id_prediccion, puntos_totales, aciertos, errores) "
                     + "VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, ranking.getUsuario().getIdUsuario());
            ps.setInt(2, ranking.getPrediccion().getIdPrediccion());
            ps.setInt(3, ranking.getPuntos());
            ps.setInt(4, ranking.getAciertos()); 
            ps.setInt(5, ranking.getErrores());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * SELECT CORREGIDO: Obtiene la tabla de posiciones filtrada por una jornada específica.
     * Se añadieron los espacios faltantes en las uniones de las cadenas de texto.
     */
    public List<RankingsHistoricos> obtenerRankingPorJornada(int idJornada) {
        List<RankingsHistoricos> lista = new ArrayList<>();
        
        String query = "SELECT rh.id_ranking, rh.puntos_totales, rh.aciertos, rh.errores, rh.id_usuario, u.username, rh.id_prediccion "
                     + "FROM rankings_historicos rh "
                     + "JOIN usuarios u ON rh.id_usuario = u.id_usuario "
                     + "JOIN predicciones p ON rh.id_prediccion = p.id_prediccion "
                     + "JOIN partido pa ON p.id_partido = pa.id_partido "
                     + "WHERE pa.id_jornada = ? "
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

                    Usuarios u = new Usuarios();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setUsername(rs.getString("username")); 
                    rh.setUsuario(u);

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
     * REQUERIMIENTO EXTENDIDO: Obtiene la matriz horizontal de puntos por jornada
     * e incluye la sumatoria total de aciertos y errores del usuario en el torneo.
     */
    public List<Object[]> obtenerMatrizPuntosPorTorneo(String nombreTorneo) {
        List<Object[]> resultados = new ArrayList<>();
        
        String query = "SELECT u.username, j.nombre_jornada, "
                     + "SUM(rh.puntos_totales) as puntos_jornada, "
                     + "SUM(rh.aciertos) as total_aciertos, "
                     + "SUM(rh.errores) as total_errores "
                     + "FROM rankings_historicos rh "
                     + "JOIN usuarios u ON rh.id_usuario = u.id_usuario "
                     + "JOIN predicciones p ON rh.id_prediccion = p.id_prediccion "
                     + "JOIN partido pa ON p.id_partido = pa.id_partido "
                     + "JOIN jornadas j ON pa.id_jornada = j.id_jornada "
                     + "JOIN torneos t ON j.id_torneo = t.id_torneo "
                     + "WHERE UPPER(t.nombre_torneo) = ? "
                     + "GROUP BY u.username, j.nombre_jornada "
                     + "ORDER BY u.username, j.nombre_jornada;";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, nombreTorneo.toUpperCase().trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultados.add(new Object[]{
                        rs.getString("username"),
                        rs.getString("nombre_jornada"),
                        rs.getInt("puntos_jornada"),
                        rs.getInt("total_aciertos"),
                        rs.getInt("total_errores")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al procesar matriz de posiciones extendida: " + e.getMessage());
        }
        return resultados;
    }

    public boolean actualizarRanking(RankingsHistoricos ranking) throws SQLException {
        String query = "UPDATE rankings_historicos SET puntos_totales = ?, aciertos = ?, errores = ? "
                     + "WHERE id_ranking = ?;";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, ranking.getPuntos());
            ps.setInt(2, ranking.getAciertos());
            ps.setInt(3, ranking.getErrores());
            ps.setInt(4, ranking.getIdRanking());

            return ps.executeUpdate() > 0;
        }
    }

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
