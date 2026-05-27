package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.BolsaPremios;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Jornadas;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.StatusBolsa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BolsaPremiosDAO {

	private final Connection conexion;

    // Constructor que recibe la conexión activa
    public BolsaPremiosDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * SELECT: Obtiene el monto acumulado de la bolsa para una jornada específica.
     * @param idJornada El identificador de la jornada consultada.
     * @return El monto total acumulado, o 0.0 si no existe un registro aún.
     */
    public double obtenerMontoAcumulado(int idJornada) {
        String query = "SELECT monto_total FROM bolsa_premios WHERE id_jornada = ?;";
        
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idJornada);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("monto_total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * INSERT / UPDATE (UPSERT): Registra o actualiza el monto total de una bolsa.
     * Diseñado para actualizar el acumulado conforme se aprueban nuevas inscripciones.
     */
    public boolean guardarOActualizarMonto(int idJornada, double montoAAgregar, StatusBolsa estatus) {
        
        String query = "SELECT b.id_bolsa, b.monto_total, c.nombre_status " +
                "FROM bolsa_premios b " +
                "JOIN cat_status_bolsa c ON b.status_bolsa = c.id_status " +
                "WHERE b.id_jornada = ?;";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idJornada);
            ps.setDouble(2, montoAAgregar);
            ps.setString(3, estatus.name()); // Mapeo seguro del ENUM a String

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * SELECT: Obtiene el objeto BolsaPremios completo por Jornada.
     * Útil cuando necesitas validar si la bolsa sigue ABIERTA antes de meter más dinero.
     */
    public BolsaPremios obtenerBolsaPorJornada(int idJornada) {
        String query = "SELECT id_bolsa, monto_total, status_bolsa FROM bolsa_premios WHERE id_jornada = ?;";
        
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idJornada);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BolsaPremios bolsa = new BolsaPremios();
                    bolsa.setIdBolsa(rs.getInt("id_bolsa"));
                    bolsa.setMontoAcumulado(rs.getDouble("monto_total"));
                    
                    // Conversión segura de String de la BD de vuelta al ENUM de Java
                    int statusInt = rs.getInt("status_bolsa");
                    
                 // Restamos 1 si en tu base de datos los IDs empiezan en 1 pero en Java los arreglos empiezan en 0
                    if (!rs.wasNull()) {
                        bolsa.setStatusBolsa(StatusBolsa.values()[statusInt - 1]);
                    }
                    
                    // Cascarón de la jornada relacionando el ID
                    Jornadas j = new Jornadas();
                    j.setIdJornada(idJornada);
                    bolsa.setJornada(j);
                    
                    return bolsa;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no se ha inicializado la bolsa de esa jornada
    }
}