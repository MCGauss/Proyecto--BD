package mx.unam.fes.acatlan.mac.proyectobd; 

import java.sql.Connection;
import java.sql.SQLException;

import mx.unam.fes.acatlan.mac.proyectobd.backend.Conexion; 
import mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas.LoginFrame;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando el sistema oficial de 'The Foreign Key Squad'...");
            

            Connection conexion = Conexion.getConexion();
            
            if (conexion != null && !conexion.isClosed()) {
                System.out.println("Conexión exitosa a PostgreSQL establecida.");
                
                java.awt.EventQueue.invokeLater(() -> {
                    LoginFrame login = new LoginFrame(conexion);
                    login.setVisible(true);
                });
            } else {
                System.err.println("Error: La conexión se obtuvo pero está cerrada.");
            }

        } catch (SQLException e) {
            System.err.println("Error crítico de SQL al conectar a la Base de Datos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}