package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class RegistroFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // Componentes visuales
    JLabel lblTitulo, lblNombre, lblApPaterno, lblApMaterno, lblUsuario, lblCorreo, lblPassword, lblTelefono;
    JTextField txtNombre, txtApPaterno, txtApMaterno, txtUsuario, txtCorreo, txtTelefono;
    JPasswordField txtPassword;
    JButton btnRegistrar, btnVolver;

    private Connection conexion;

    public RegistroFrame(Connection conexion) {
        this.conexion = conexion;

        setTitle("Registro de Usuario - The Foreign Key Squad");
        setSize(800, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        iniciarComponentes();
        configurarEventosBD();

        setVisible(true);
    }

    private void iniciarComponentes() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(15, 23, 42)); // Azul oscuro profundo oficial
        panel.setLayout(null);

        // TÍTULO
        lblTitulo = new JLabel("REGISTRO DE NUEVO USUARIO");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBounds(220, 30, 400, 40);
        panel.add(lblTitulo);

        // FUENTE DE ETIQUETAS
        Font labelFont = new Font("Arial", Font.BOLD, 13);
        Color labelColor = new Color(148, 163, 184);

        // COLUMNA 1: DATOS DE CUENTA
        lblUsuario = new JLabel("USERNAME:");
        lblUsuario.setFont(labelFont);
        lblUsuario.setForeground(labelColor);
        lblUsuario.setBounds(80, 100, 150, 25);
        panel.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(80, 130, 280, 35);
        txtUsuario.setBackground(new Color(30, 41, 59));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(txtUsuario);

        lblCorreo = new JLabel("CORREO ELECTRÓNICO:");
        lblCorreo.setFont(labelFont);
        lblCorreo.setForeground(labelColor);
        lblCorreo.setBounds(80, 190, 200, 25);
        panel.add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(80, 220, 280, 35);
        txtCorreo.setBackground(new Color(30, 41, 59));
        txtCorreo.setForeground(Color.WHITE);
        txtCorreo.setCaretColor(Color.WHITE);
        txtCorreo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(txtCorreo);

        lblPassword = new JLabel("CONTRASEÑA:");
        lblPassword.setFont(labelFont);
        lblPassword.setForeground(labelColor);
        lblPassword.setBounds(80, 280, 150, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(80, 310, 280, 35);
        txtPassword.setBackground(new Color(30, 41, 59));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(txtPassword);

        lblTelefono = new JLabel("TELÉFONO (10 DÍGITOS):");
        lblTelefono.setFont(labelFont);
        lblTelefono.setForeground(labelColor);
        lblTelefono.setBounds(80, 370, 200, 25);
        panel.add(lblTelefono);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(80, 400, 280, 35);
        txtTelefono.setBackground(new Color(30, 41, 59));
        txtTelefono.setForeground(Color.WHITE);
        txtTelefono.setCaretColor(Color.WHITE);
        txtTelefono.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(txtTelefono);

        // COLUMNA 2: DATOS PERSONALES
        lblNombre = new JLabel("NOMBRE(S):");
        lblNombre.setFont(labelFont);
        lblNombre.setForeground(labelColor);
        lblNombre.setBounds(440, 100, 150, 25);
        panel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(440, 130, 280, 35);
        txtNombre.setBackground(new Color(30, 41, 59));
        txtNombre.setForeground(Color.WHITE);
        txtNombre.setCaretColor(Color.WHITE);
        txtNombre.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(txtNombre);

        lblApPaterno = new JLabel("APELLIDO PATERNO:");
        lblApPaterno.setFont(labelFont);
        lblApPaterno.setForeground(labelColor);
        lblApPaterno.setBounds(440, 190, 150, 25);
        panel.add(lblApPaterno);

        txtApPaterno = new JTextField();
        txtApPaterno.setBounds(440, 220, 280, 35);
        txtApPaterno.setBackground(new Color(30, 41, 59));
        txtApPaterno.setForeground(Color.WHITE);
        txtApPaterno.setCaretColor(Color.WHITE);
        txtApPaterno.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(txtApPaterno);

        lblApMaterno = new JLabel("APELLIDO MATERNO:");
        lblApMaterno.setFont(labelFont);
        lblApMaterno.setForeground(labelColor);
        lblApMaterno.setBounds(440, 280, 150, 25);
        panel.add(lblApMaterno);

        txtApMaterno = new JTextField();
        txtApMaterno.setBounds(440, 310, 280, 35);
        txtApMaterno.setBackground(new Color(30, 41, 59));
        txtApMaterno.setForeground(Color.WHITE);
        txtApMaterno.setCaretColor(Color.WHITE);
        txtApMaterno.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(txtApMaterno);

        // ==========================================
        // BOTÓN: CONFIRMAR REGISTRO (Verde Éxito)
        // ==========================================
        btnRegistrar = new JButton("CONFIRMAR REGISTRO");
        btnRegistrar.setBounds(440, 395, 280, 42);
        
        // Propiedades críticas para la compatibilidad con macOS
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBorderPainted(false); 
        
        btnRegistrar.setBackground(new Color(16, 185, 129)); // Verde oficial
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnRegistrar);

        // ==========================================
        // BOTÓN: VOLVER AL LOGIN (Gris Slate)
        // ==========================================
        btnVolver = new JButton("VOLVER AL LOGIN");
        btnVolver.setBounds(260, 520, 280, 40);
        
        // Propiedades críticas para la compatibilidad con macOS
        btnVolver.setOpaque(true);
        btnVolver.setBorderPainted(false);
        
        btnVolver.setBackground(new Color(71, 85, 105)); // Gris de control
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 13));
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnVolver);

        add(panel);
    }

    private void configurarEventosBD() {
        btnVolver.addActionListener(e -> {
            new LoginFrame(conexion).setVisible(true);
            dispose();
        });

        btnRegistrar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String correo = txtCorreo.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String telefono = txtTelefono.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apPaterno = txtApPaterno.getText().trim();
            String apMaterno = txtApMaterno.getText().trim();

            // Validación básica de campos
            if (usuario.isEmpty() || correo.isEmpty() || password.isEmpty() || 
                telefono.isEmpty() || nombre.isEmpty() || apPaterno.isEmpty() || apMaterno.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (telefono.length() != 10) {
                JOptionPane.showMessageDialog(this, "El teléfono debe tener exactamente 10 dígitos.", "Error de Formato", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Desactivamos autocommit para manejar la transacción de forma atómica (Ambas tablas o ninguna)
                conexion.setAutoCommit(false);

                // 1. Insertar en la tabla USUARIOS (id_rol = 2 es el rol de apostador/usuario común)
                String sqlUsuario = "INSERT INTO usuarios (username, email, passsword, saldo, id_rol) VALUES (?, ?, ?, 0.0, 2) RETURNING id_usuario";
                PreparedStatement psUser = conexion.prepareStatement(sqlUsuario);
                psUser.setString(1, usuario);
                psUser.setString(2, correo);
                psUser.setString(3, password);

                ResultSet rs = psUser.executeQuery();
                int idUsuarioGenerado = -1;
                if (rs.next()) {
                    idUsuarioGenerado = rs.getInt(1);
                }

                // 2. Insertar en la tabla DATOS_PERSONALES ligada al ID obtenido
                String sqlDatos = "INSERT INTO datos_personales (nombre_usuario, ap_paterno, ap_materno, telefono, id_usuario) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psDatos = conexion.prepareStatement(sqlDatos);
                psDatos.setString(1, nombre);
                psDatos.setString(2, apPaterno);
                psDatos.setString(3, apMaterno);
                psDatos.setString(4, telefono);
                psDatos.setInt(5, idUsuarioGenerado);

                psDatos.executeUpdate();

                // Guardamos los cambios de manera segura en PostgreSQL
                conexion.commit();
                conexion.setAutoCommit(true);

                JOptionPane.showMessageDialog(this, "¡Usuario registrado con éxito en el sistema!", "Registro Completado", JOptionPane.INFORMATION_MESSAGE);
                
                new LoginFrame(conexion).setVisible(true);
                dispose();

            } catch (Exception ex) {
                try {
                    conexion.rollback(); // Cancelamos todo si algo falló
                    conexion.setAutoCommit(true);
                } catch (Exception rollbackEx) {
                    System.out.println("Error en rollback: " + rollbackEx.getMessage());
                }
                JOptionPane.showMessageDialog(this, "Error en la Base de Datos: " + ex.getMessage(), "Error de Registro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
