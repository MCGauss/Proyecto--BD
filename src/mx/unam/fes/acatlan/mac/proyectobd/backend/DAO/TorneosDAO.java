package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import mx.unam.fes.acatlan.mac.proyectobd.backend.Conexion;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Ligas;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Torneos;

public class TorneosDAO {

    public List<Torneos> listarTorneos() {
        List<Torneos> lista = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            if (conn != null) {
                stmt = conn.createStatement();
                // query 
                String query = "SELECT id_torneo, id_liga, nombre_torneo, fecha_inicio, fecha_fin FROM torneos";
                rs = stmt.executeQuery(query);
                
                while (rs.next()) {
                    
                	//mapeo de datos
                    Ligas ligaTemporal = new Ligas(rs.getInt("id_liga"), null);
                    
                    //Convertimos de java.sql.Date a java.time.LocalDate 
                    Date sqlFechaInicio = rs.getDate("fecha_inicio");
                    Date sqlFechaFin = rs.getDate("fecha_fin");
                    
                    LocalDate fechaInicio = (sqlFechaInicio != null) ? sqlFechaInicio.toLocalDate() : null;
                    LocalDate fechaFin = (sqlFechaFin != null) ? sqlFechaFin.toLocalDate() : null;
                    
                    // Creamos el objeto Torneos respetando el orden del constructor:
                    Torneos torneo = new Torneos(
                        rs.getInt("id_torneo"),
                        ligaTemporal,
                        rs.getString("nombre_torneo"),
                        fechaInicio,
                        fechaFin
                    );
                    
                    lista.add(torneo);
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
