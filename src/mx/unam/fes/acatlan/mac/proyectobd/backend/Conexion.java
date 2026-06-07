package mx.unam.fes.acatlan.mac.proyectobd.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

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
            
            // Base de datos estándar para el equipo
            String servidor = "jdbc:postgresql://127.0.0.1:5432/prueba_proyecto"; 
            String usuarioDB = "postgres";
            
            // Pedimos la contraseña dinámicamente con una ventana emergente estética de Swing
            JPasswordField pf = new JPasswordField();
            int okClic = JOptionPane.showConfirmDialog(null, pf, "contraseña de tu PostgreSQL local:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            String passwordDB = "";
            if (okClic == JOptionPane.OK_OPTION) {
                passwordDB = new String(pf.getPassword());
            } else {
                System.exit(0); // Si cancela, cerramos limpio
            }
            
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

