package mx.unam.fes.acatlan.mac.proyectobd.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
	// El objeto Connection que guardará el puente activo
    private static Connection conn = null;
    
    public static Connection getConexion() {
        try {
            // 1. Cargamos el Driver de PostgreSQL (el .jar )
            Class.forName("org.postgresql.Driver"); 
            
            // 2. Definimos la dirección del servidor local de tu MacBook
            // Nota: El puerto por defecto es 5432. Si tu pgcli usa otro (como 5435), cámbialo aquí.
            String servidor = "jdbc:postgresql://127.0.0.1:5432/quiniela_db"; 
            String usuarioDB = "postgres";
            String passwordDB = "postgres"; // Pon aquí tu contraseña real de Postgres
            
            // 3. El DriverManager fabrica el puente físico y nos lo regresa
            conn = DriverManager.getConnection(servidor, usuarioDB, passwordDB);
            
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: ¡No se encontró el Driver de PostgreSQL en el proyecto!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("ERROR: No se pudo conectar a la base de datos. Revisa si Postgres está encendido.");
            e.printStackTrace();
        }
        
        return conn; // Regresa el puente (o null si falló algo)
    }

}
