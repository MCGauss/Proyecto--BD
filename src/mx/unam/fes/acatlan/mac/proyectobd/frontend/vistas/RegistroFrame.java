package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection; // NUEVO
import javax.swing.*;
// Importa tus DAOs si vas a registrar usuarios en la BD desde aquí
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.UsuariosDAO; 

public class RegistroFrame extends JFrame {

    JLabel lblTitulo;
    JLabel lblNombre;
    JLabel lblUsuario;
    JLabel lblCorreo;
    JLabel lblPassword;

    JTextField txtNombre;
    JTextField txtUsuario;
    JTextField txtCorreo;
    JPasswordField txtPassword;

    JButton btnRegistrar;
    JButton btnVolver;

    // NUEVO: Atributo para la persistencia
    private Connection conexion;

    // MODIFICADO: Ahora el constructor recibe la conexión de la arquitectura
    public RegistroFrame(Connection conexion) {
        this.conexion = conexion;

        setTitle("Registro");
        setSize(800, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700, 550));
        setResizable(true);

        iniciarComponentes();
        configurarEventosBD(); // NUEVO: Separamos los eventos para limpieza

        setVisible(true);
    }

    private void iniciarComponentes() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 41, 59));
        panel.setLayout(null);

        lblTitulo = new JLabel("REGISTRO DE USUARIO");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setBounds(250, 40, 400, 40); // Ajustado basado en el código de Tony

        // ... (Todo el código de posicionamiento de Tony: lblNombre, txtNombre, etc.) ...
        // NOTA: Asegúrate de mantener los panel.add(...) de Tony intactos aquí.
        
        // Al final del método de Tony original estaban los actionListeners nativos. 
        // Los quitamos de aquí y los movemos abajo a configurarEventosBD().
        panel.add(btnRegistrar);
        panel.add(btnVolver);
        add(panel);
    }

    /**
     * NUEVO: Vinculación con tu base de datos y retorno seguro
     */
    private void configurarEventosBD() {
        // CORREGIDO: Al volver, le regresamos la conexión al LoginFrame
        btnVolver.addActionListener(e -> {
            new LoginFrame(conexion);
            dispose();
        });

        // Evento para insertar en PostgreSQL
        btnRegistrar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String correo = txtCorreo.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (nombre.isEmpty() || usuario.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor completa todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Aquí mandará a llamar tu método del backend para registrar
                // Ejemplo:
                // UsuariosDAO dao = new UsuariosDAO(conexion);
                // boolean exito = dao.registrarUsuario(new Usuarios(usuario, password, correo, 0.0)); // Ajusta a tu modelo
                
                JOptionPane.showMessageDialog(this, "¡Usuario registrado con éxito!");
                new LoginFrame(conexion);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar en la BD: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
