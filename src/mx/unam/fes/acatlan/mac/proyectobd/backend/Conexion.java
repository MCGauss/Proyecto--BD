package mx.unam.fes.acatlan.mac.proyectobd.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // El objeto Connection que guardará el puente activo
    private static Connection conn = null;
    
    public static Connection getConexion() {
        try {
            // PATRÓN SINGLETON: Si el puente ya existe y está abierto, lo reutilizamos
            if (conn != null && !conn.isClosed()) {
                return conn;
            }

            // 1. Cargamos el Driver de PostgreSQL (el .jar)
            Class.forName("org.postgresql.Driver"); 
            

            // 2. Definimos la dirección del servidor local de tu MacBook
            
            String servidor = "jdbc:postgresql://127.0.0.1:5432/prueba_proyecto"; 
            String usuarioDB = "postgres";
            String passwordDB = "331968"; 

            //Opción 2
            //String servidor = "jdbc:postgresql://127.0.0.1:5432/dbLigaMX"; 
            //String usuarioDB = "postgres";
            //String passwordDB = "Jack09#"; 
            
            // 3. El DriverManager fabrica el puente físico usando las variables de arriba
            conn = DriverManager.getConnection(servidor, usuarioDB, passwordDB);
            
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: ¡No se encontró el Driver de PostgreSQL en el proyecto!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("ERROR: No se pudo conectar a la base de datos. Revisa si Postgres está encendido o las credenciales.");
            e.printStackTrace();
        }
        
        return conn; 
    }
}

