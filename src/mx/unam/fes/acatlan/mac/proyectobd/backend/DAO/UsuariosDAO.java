package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Rol;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class UsuariosDAO {

    private final Connection conexion;

    /**
     * Constructor unificado que recibe la conexión activa (Arquitectura MCGauss & Diana).
     */
    public UsuariosDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * SELECT: Valida las credenciales en el inicio de sesión.
     * CORREGIDO: Se adapta a la tabla 'rol', columna 'nombre_rol' y al campo 'passsword' (con 3 's').
     */
    public Usuarios validarLogin(String email, String password) {
        Usuarios userLogueado = null;
        
        // Query corregido con los nombres físicos reales del LDD
        String query = "SELECT u.id_usuario, u.username, u.email, u.passsword, u.saldo, r.nombre_rol AS descripcion_rol " +
                       "FROM usuarios u " +
                       "JOIN rol r ON u.id_rol = r.id_rol " +
                       "WHERE u.email = ? AND u.passsword = ?;";
                               
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
                
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String descRol = rs.getString("descripcion_rol").toUpperCase();
                    Rol rolEnum = Rol.valueOf(descRol);
                    
                    userLogueado = new Usuarios(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("passsword"), // Sincronizado con las 3 's'
                        rs.getDouble("saldo"),
                        rolEnum
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userLogueado;
    }

    /**
     * INSERT: Registra nuevos apostadores en el sistema.
     * CORREGIDO: Mapeo de columnas y propagación de errores para detectar duplicados en email/username.
     */
    public boolean registrarUsuario(Usuarios usuario) throws SQLException {
        // Query adaptado a la columna 'passsword' del LDD
        String query = "INSERT INTO usuarios (username, email, passsword, saldo, id_rol) VALUES (?, ?, ?, ?, ?);";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            // Mapeo del Enum a los IDs del catálogo 'rol' (1: ADMINISTRADOR, 2: USUARIO)
            int idRolDb = (usuario.getRol() == Rol.ADMINISTRADOR) ? 1 : 2;
                
            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getPassword()); // Pasa el String al campo 'passsword'
            pstmt.setDouble(4, usuario.getSaldo());
            pstmt.setInt(5, idRolDb);
                
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw e; // Lanza el error para que Swing avise si el email ya existe
        }
    }

    /**
     * UPDATE: Modifica el monedero virtual (saldo).
     * CORREGIDO: Lanza excepción para validar la restricción 'chk_saldo_positivo'.
     */
    public boolean actualizarSaldo(int idUsuario, double nuevoSaldo) throws SQLException {
        String query = "UPDATE usuarios SET saldo = ? WHERE id_usuario = ?;";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setInt(2, idUsuario);
                
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Al arrojar el error, protegemos el CHECK (saldo >= 0) si se intenta retirar más de lo debido
            throw e;
        }
    }
}