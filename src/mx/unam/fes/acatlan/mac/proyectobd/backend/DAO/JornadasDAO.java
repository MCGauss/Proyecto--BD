package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mx.unam.fes.acatlan.mac.proyectobd.backend.Conexion;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Jornadas;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Torneos;

public class JornadasDAO {
	// metodo para obtener las jornadas de un torneo específico 
    public List<Jornadas> obtenerJornadasPorTorneo(int idTorneo) {
        List<Jornadas> lista = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            if (conn != null) {
                stmt = conn.createStatement();
                // query
                String query = "SELECT id_jornada, nombre_jornada, id_torneo FROM jornadas WHERE id_torneo = " + idTorneo;
                rs = stmt.executeQuery(query);
                
                while (rs.next()) {
                    // mapeo de datos 
                    // le pasamos null a los atributos que no tenemos en esta consulta.
                    Torneos torneoTemporal = new Torneos(rs.getInt("id_torneo"), null, null, null, null);
                    
                    // creamos el objeto Jornadas 
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
