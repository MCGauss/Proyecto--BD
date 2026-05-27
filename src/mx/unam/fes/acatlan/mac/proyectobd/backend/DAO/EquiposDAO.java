package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mx.unam.fes.acatlan.mac.proyectobd.backend.Conexion;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Equipos;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Ligas;

public class EquiposDAO {
	// metodo para obtener los equipos filtrados por la liga a la que pertenecen
    public List<Equipos> obtenerEquiposPorLiga(int idLiga) {
        List<Equipos> lista = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            if (conn != null) {
                stmt = conn.createStatement();
                // query 
                String query = "SELECT id_equipo, nombre_equipo, logo, id_liga FROM equipos WHERE id_liga = " + idLiga;
                rs = stmt.executeQuery(query);
                
                while (rs.next()) {
                    // mapeo de datos
                    Ligas ligaTemporal = new Ligas(rs.getInt("id_liga"), null);
                    
                    // Creamos el objeto Equipos respetando el orden exacto de tu constructor:
                    // (int idEquipo, String nombreEquipo, String logoURL, Ligas liga)
                    Equipos equipo = new Equipos(
                        rs.getInt("id_equipo"),
                        rs.getString("nombre_equipo"),
                        rs.getString("logo"),
                        ligaTemporal
                    );
                    
                    lista.add(equipo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }

}
