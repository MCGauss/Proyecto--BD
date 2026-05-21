package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection; // es el puente de comunicación  entre Java y PostgreSQL.
import java.sql.ResultSet; // tabla virtual temporal de resultados que devuelve la base de datos.
import java.sql.SQLException; // para captar posibles errores
import java.sql.Statement; // mensajero que transporta el código SQL hacia la base de datos através de Connection
//estructuras de datos dinamicas para guardar los objetos mapeados 
import java.util.ArrayList;  
import java.util.List;

import mx.unam.fes.acatlan.mac.proyectobd.backend.Conexion;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Ligas;

public class LigasDAO {
	
	public List<Ligas> obtenerTodas() {
        List<Ligas> lista = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            if (conn != null) {
                stmt = conn.createStatement();
                // query 
                String query = "SELECT id_liga, nombre_liga, FROM ligas";
                rs = stmt.executeQuery(query);
                
                while (rs.next()) {
                    Ligas liga = new Ligas(rs.getInt("id_liga"),rs.getString("nombre_liga"));
                    lista.add(liga);
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
