package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import mx.unam.fes.acatlan.mac.proyectobd.backend.Conexion;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Equipos;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Jornadas;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Partido;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.StatusPartido;

public class PartidosDAO {

    // metodo para traer la cartelera de partidos de una jornada especifica
    public List<Partido> obtenerPartidosPorJornada(int idJornada) {
        List<Partido> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConexion();
            if (conn != null) {
                // usamos PreparedStatement por orden y limpieza con tipos complejos como las fechas
                String query = "SELECT p.id_partido, p.id_jornada, p.goles_local, p.goles_visitante, p.fecha_hora_prog, " +
                               "sp.descripcion_status " +
                               "FROM partido p " +
                               "JOIN status_partido sp ON p.id_status_partido = sp.id_status_partido " +
                               "WHERE p.id_jornada = ?";
                               
                pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, idJornada);
                rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    // mapeo de datos
                    Jornadas jornadaTemporal = new Jornadas(rs.getInt("id_jornada"), null, null);
                    
                    // como en el query plano no traemos los IDs de los equipos, los inicializamos en 0 temporales.
                    Equipos localTemporal = new Equipos(0, "Local", null, null);
                    Equipos visitanteTemporal = new Equipos(0, "Visitante", null, null);
                    
                    // convertimos el Timestamp de sql a LocalDateTime de java 
                    Timestamp ts = rs.getTimestamp("fecha_hora_prog");
                    LocalDateTime fechaHora = (ts != null) ? ts.toLocalDateTime() : null;
                    
                    // convertimos la cadena de texto de la BD al Enum 
                    // reemplazamos espacios por guiones bajos y pasamos a mayusculas para empatar con EN_CURSO, etc.
                    String descStatusDb = rs.getString("descripcion_status").replace(" ", "_").toUpperCase();
                    StatusPartido statusEnum = StatusPartido.valueOf(descStatusDb);
                    
                    // creamos el partido 
                    Partido partido = new Partido(
                        rs.getInt("id_partido"),
                        jornadaTemporal,
                        localTemporal,
                        visitanteTemporal,
                        null, 
                        null,
                        fechaHora,
                        statusEnum
                    );
                    
                    // forzamos los goles reales de la BD usando los setters del objeto, saltandonos el reseteo a null que tiene el constructor adentro.
                    if (rs.getObject("goles_local") != null) {
                        partido.setGolesLocal(rs.getInt("goles_local"));
                    }
                    if (rs.getObject("goles_visitante") != null) {
                        partido.setGolesVisitante(rs.getInt("goles_visitante"));
                    }
                    
                    lista.add(partido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }

    // metodo  para que el administrador guarde los resultados reales desde java swing
    public boolean actualizarMarcadorYEstatus(int idPartido, int golesL, int golesV, StatusPartido nuevoEstatus) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConexion();
            if (conn != null) {
                // mapeo inverso de Enum a los IDs numericos de tu tabla status_partido en la BD
                int idStatusDb = 1; // por defecto PROGRAMADO
                if (nuevoEstatus == StatusPartido.EN_CURSO) idStatusDb = 2;
                if (nuevoEstatus == StatusPartido.FINALIZADO) idStatusDb = 3;
                if (nuevoEstatus == StatusPartido.POSPUESTO) idStatusDb = 4;
                
                String query = "UPDATE partido SET goles_local = ?, goles_visitante = ?, id_status_partido = ? WHERE id_partido = ?";
                
                pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, golesL);
                pstmt.setInt(2, golesV);
                pstmt.setInt(3, idStatusDb);
                pstmt.setInt(4, idPartido);
                
                pstmt.executeUpdate();
                exito = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return exito;
    }
}