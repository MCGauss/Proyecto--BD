package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
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
     */
    public Usuarios validarLogin(String email, String passsword) {
        Usuarios userLogueado = null;
        
        // Query del LDD
        String query = "SELECT u.id_usuario, u.username, u.email, u.passsword, u.saldo, r.nombre_rol AS descripcion_rol " +
                       "FROM usuarios u " +
                       "JOIN rol r ON u.id_rol = r.id_rol " +
                       "WHERE u.email = ? AND u.passsword = ?;";
                               
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, passsword);
                
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String descRol = rs.getString("descripcion_rol").toUpperCase();
                    Rol rol = Rol.USUARIO;
                    
                    if (descRol != null && descRol.equalsIgnoreCase("ADMINISTRADOR")) {
                        rol = Rol.ADMINISTRADOR;
                    }
                    
                    //Construcción del modelo
                    userLogueado = new Usuarios(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("passsword"),
                        rs.getDouble("saldo"),
                        rol
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
     */
    public boolean registrarUsuario(Usuarios usuario) throws SQLException {
        String query = "INSERT INTO usuarios (username, email, passsword, saldo, id_rol) VALUES (?, ?, ?, ?, ?);";
        
        int idRolDb = (usuario.getRol() == Rol.ADMINISTRADOR) ? 1 : 2;
        
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            
            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getPasssword());
            pstmt.setDouble(4, usuario.getSaldo());
            pstmt.setInt(5, idRolDb);
    
            return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
            throw e; // Lanza el error para que Swing avise si el email ya existe
        }
    }

    /**
     * UPDATE: Modifica el saldo del usuario
     * Lanza excepción para validar la restricción 'chk_saldo_positivo'.
     */
    public boolean actualizarSaldo(int idUsuario, double nuevoSaldo) throws SQLException {
        String query = "UPDATE usuarios SET saldo = ? WHERE id_usuario = ?;";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setInt(2, idUsuario);
                
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Se valida un saldo positivo,si se intenta retirar más de lo debido
            throw e;
        }
    }
    
    /**
     * CALLABLE STATEMENT: Invoca el procedimiento almacenado 'sp_registrar_deposito' 
     */
    public boolean abonarSaldoPorDeposito(int idUsuario, double monto) throws SQLException {
        String query = "CALL sp_registrar_deposito(?, ?);";

        try (CallableStatement cstmt = conexion.prepareCall(query)) {
            cstmt.setInt(1, idUsuario);
            cstmt.setBigDecimal(2, java.math.BigDecimal.valueOf(monto));

            cstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en CallableStatement sp_registrar_deposito: " + e.getMessage());
            throw e;
        }
    }
    
}