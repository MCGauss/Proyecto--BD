package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection; // Es el puente de comunicación entre Java y PostgreSQL.
import java.sql.PreparedStatement; // NUEVO: Mensajero optimizado y seguro para transportar el código SQL.
import java.sql.ResultSet; // Tabla virtual temporal de resultados que devuelve la base de datos.
import java.sql.SQLException; // Para captar posibles errores.
import java.util.ArrayList; // Estructuras de datos dinámicas para guardar los objetos mapeados.
import java.util.List;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Ligas;

public class LigasDAO {
    
    private final Connection conexion;

    // Constructor unificado que recibe la conexión activa desde el controlador/capa superior
    public LigasDAO(Connection conexion) {
        this.conexion = conexion;
    }
    
    /**
     * SELECT: Obtiene todas las ligas registradas en el sistema.
     * Al ser un catálogo informativo, es 100% compatible con tus scripts de backend.
     * @return Lista completa de objetos Ligas.
     */
    public List<Ligas> obtenerTodas() {
        List<Ligas> lista = new ArrayList<>();
        // CORRECCIÓN: Se eliminó la coma sobrante antes del FROM para evitar errores de sintaxis en Postgres
        String query = "SELECT id_liga, nombre_liga FROM ligas;";
        
        // Usamos try-with-resources para que Eclipse gestione y cierre de forma segura el statement y resultset
        try (PreparedStatement ps = conexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                // Mapeo directo respetando tu modelo de datos
                Ligas liga = new Ligas(
                    rs.getInt("id_liga"),
                    rs.getString("nombre_liga")
                );
                lista.add(liga);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lista;
    }
}
