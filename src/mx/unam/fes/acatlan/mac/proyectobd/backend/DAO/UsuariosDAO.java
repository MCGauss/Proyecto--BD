package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.unam.fes.acatlan.mac.proyectobd.backend.Conexion;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Rol;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class UsuariosDAO {

    // metodo para el inicio de sesion
    public Usuarios validarLogin(String email, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Usuarios userLogueado = null;
        
        try {
            conn = Conexion.getConexion();
            if (conn != null) {
                // los signos "?" actuan como comodines seguros contra inyeccion SQL
                String query = "SELECT u.id_usuario, u.username, u.email, u.password, u.saldo, r.descripcion_rol " +
                               "FROM usuarios u " +
                               "JOIN roles r ON u.id_rol = r.id_rol " +
                               "WHERE u.email = ? AND u.password = ?";
                               
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, email);
                pstmt.setString(2, password);
                
                rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    // mapeamos la descripcion de la BD al Enum 
                    String descRol = rs.getString("descripcion_rol").toUpperCase();
                    Rol rolEnum = Rol.valueOf(descRol);
                    
                    // construimos el usuario 
                    userLogueado = new Usuarios(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDouble("saldo"),
                        rolEnum
                    );
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
        return userLogueado;
    }

    // metodo para registrar nuevos apostadores en el sistema
    public boolean registrarUsuario(Usuarios usuario) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConexion();
            if (conn != null) {
                String query = "INSERT INTO usuarios (username, email, password, saldo, id_rol) VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(query);
                
                // mapeo del Enum de Java a los IDs numericos de la tabla 'roles' en PostgreSQL
                // (asumimos 1 para ADMINISTRADOR y 2 para USUARIO)
                int idRolDb = (usuario.getRol() == Rol.ADMINISTRADOR) ? 1 : 2;
                
                pstmt.setString(1, usuario.getUsername());
                pstmt.setString(2, usuario.getEmail());
                pstmt.setString(3, usuario.getPassword());
                pstmt.setDouble(4, usuario.getSaldo());
                pstmt.setInt(5, idRolDb);
                
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

    // metodo operacional para modificar el monedero virtual (saldo)
    public boolean actualizarSaldo(int idUsuario, double nuevoSaldo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConexion();
            if (conn != null) {
                String query = "UPDATE usuarios SET saldo = ? WHERE id_usuario = ?";
                
                pstmt = conn.prepareStatement(query);
                pstmt.setDouble(1, nuevoSaldo);
                pstmt.setInt(2, idUsuario);
                
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