package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement; // NUEVO: Cambiado por PreparedStatement
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
