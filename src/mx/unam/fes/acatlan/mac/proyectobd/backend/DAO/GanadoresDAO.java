package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.BolsaPremios;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.UsuariosGanadores;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.RankingsHistoricos;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GanadoresDAO {

	private final Connection conexion;

    // Constructor que recibe la conexión activa a la base de datos
    public GanadoresDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * INSERT: Registra un nuevo ganador en el sistema.
     * Vincula al usuario con la bolsa de donde sale el dinero y el ranking que respaldó su victoria.
     */
    public boolean registrarGanador(UsuariosGanadores ganador) {
        String query = "INSERT INTO ganadores (id_bolsa, id_usuario, id_ranking, monto_premio) "
                   + "VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, ganador.getBolsa().getIdBolsa());
            ps.setInt(2, ganador.getUsuario().getIdUsuario());
            ps.setInt(3, ganador.getRanking().getIdRanking());
            ps.setDouble(4, ganador.getMontoPremio());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * SELECT: Obtiene la lista histórica de todos los ganadores registrados.
     * Perfecto para alimentar una pantalla de "Muro de la Fama" o auditoría financiera en Java Swing.
     */
    public List<UsuariosGanadores> obtenerHistorialGanadores() {
        List<UsuariosGanadores> lista = new ArrayList<>();
        String query = "SELECT id_ganador, id_bolsa, id_usuario, id_ranking, monto_premio FROM ganadores;";

        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UsuariosGanadores g = new UsuariosGanadores();
                g.setIdGanador(rs.getInt("id_ganador"));
                g.setMontoPremio(rs.getDouble("monto_premio"));

                // Cascarón de BolsaPremios
                BolsaPremios b = new BolsaPremios();
                b.setIdBolsa(rs.getInt("id_bolsa"));
                g.setBolsa(b);

                // Cascarón de Usuarios
                Usuarios u = new Usuarios();
                u.setIdUsuario(rs.getInt("id_usuario"));
                g.setUsuario(u);

                // Cascarón de RankingsHistoricos
                RankingsHistoricos r = new RankingsHistoricos();
                r.setIdRanking(rs.getInt("id_ranking"));
                g.setRanking(r);

                lista.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * UPDATE: Permite modificar el monto de un premio asignado si es necesario.
     */
    public boolean actualizarMontoPremio(int idGanador, double nuevoMonto) {
        String query = "UPDATE ganadores SET monto_premio = ? WHERE id_ganador = ?;";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setDouble(1, nuevoMonto);
            ps.setInt(2, idGanador);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
