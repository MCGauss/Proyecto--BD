package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.io.File;
import java.sql.Connection;
import javax.swing.*;

// IMPORTA TUS CLASES DEL BACKEND
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.UsuariosDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Rol;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class LoginFrame extends JFrame {

    JPanel panelIzquierdo, panelDerecho;
    JLabel lblLogo, lblSistema, lblEquipo, lblTitulo, lblUsuario, lblPassword;
    JTextField txtUsuario;
    JPasswordField txtPassword;
    JButton btnLogin, btnRegistro;

    // NUEVO: Atributo para mantener la conexión viva en la ventana
    private Connection conexion;

    // MODIFICADO: El constructor ahora exige recibir la conexión obligatoriamente
    public LoginFrame(Connection conexion) {
        this.conexion = conexion;

        setTitle("FootBets");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        iniciarComponentes();
        
        // NUEVO: Separamos la lógica de los botones para poder meter la BD limpiamente
        configurarEventosBD();

        setVisible(true);
    }

    private void iniciarComponentes() {
        setLayout(new GridLayout(1,2));

        // PANEL IZQUIERDO
        panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(new Color(15,23,42));
        panelIzquierdo.setLayout(null);

        lblLogo = new JLabel();
        // CORRECCIÓN DE RUTA: Al estar la carpeta 'Assets' fuera de src, 
        // usamos File para verificar su existencia en la raíz del proyecto.
        File archivoLogo = new File("Assets/logo.png");
        if (archivoLogo.exists()) {
            ImageIcon iconoOriginal = new ImageIcon(archivoLogo.getAbsolutePath());
            // Escalamos de forma suave (Smooth) para no perder calidad visual
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imagenEscalada));
        } else {
            // Plan de respaldo escalado (por si lee del Classpath interno)
            java.net.URL urlLogo = getClass().getResource("/assets/logo.png");
            if (urlLogo != null) {
                ImageIcon iconoOriginal = new ImageIcon(urlLogo);
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(imagenEscalada));
            }
        }
        lblLogo.setBounds(125, 80, 200, 200);

        lblSistema = new JLabel("FOOTBETS", SwingConstants.CENTER);
        lblSistema.setForeground(Color.WHITE);
        lblSistema.setFont(new Font("Arial", Font.BOLD, 22));
        lblSistema.setBounds(50, 310, 350, 40);

        lblEquipo = new JLabel("THE FOREIGN KEY SQUAD", SwingConstants.CENTER);
        lblEquipo.setForeground(new Color(148,163,184));
        lblEquipo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEquipo.setBounds(50, 360, 350, 30);

        panelIzquierdo.add(lblLogo);
        panelIzquierdo.add(lblSistema);
        panelIzquierdo.add(lblEquipo);

        // PANEL DERECHO
        panelDerecho = new JPanel();
        panelDerecho.setBackground(new Color(30,41,59));
        panelDerecho.setLayout(null);

        lblTitulo = new JLabel("INICIAR SESIÓN");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setBounds(100, 60, 250, 40);

        lblUsuario = new JLabel("CORREO");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        lblUsuario.setBounds(100, 150, 150, 30);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsuario.setBounds(100, 185, 250, 35);

        lblPassword = new JLabel("CONTRASEÑA");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPassword.setBounds(100, 270, 150, 30);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setBounds(100, 305, 250, 35);

        btnLogin = new JButton("INICIAR SESIÓN");
        btnLogin.setBounds(100, 380, 250, 40);
        btnLogin.setOpaque(true);           // <- Obliga a pintar el fondo en Mac
        btnLogin.setBorderPainted(false);   // <- Quita el borde Aqua nativo de Mac
        btnLogin.setBackground(new Color(59,130,246));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));

        btnRegistro = new JButton("REGISTRARSE");
        btnRegistro.setBounds(100, 440, 250, 40);
        btnRegistro.setOpaque(true);         // <- Obliga a pintar el fondo en Mac
        btnRegistro.setBorderPainted(false); // <- Quita el borde Aqua nativo de Mac
        btnRegistro.setBackground(new Color(16,185,129));
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.setFont(new Font("Arial", Font.BOLD, 14));

        panelDerecho.add(lblTitulo);
        panelDerecho.add(lblUsuario);
        panelDerecho.add(txtUsuario);
        panelDerecho.add(lblPassword);
        panelDerecho.add(txtPassword);
        panelDerecho.add(btnLogin);
        panelDerecho.add(btnRegistro);

        add(panelIzquierdo);
        add(panelDerecho);
    }

    /**
     * NUEVO: Lógica de conexión real para los botones del Login
     */
    private void configurarEventosBD() {
        // Redirección a Registro pasando la conexión activa
        btnRegistro.addActionListener(e -> {
            new RegistroFrame(conexion);
            dispose();
        });

        // VALIDACIÓN DE LOGEO CON TU BASE DE DATOS
        btnLogin.addActionListener(e -> {
            String user = txtUsuario.getText().trim();
            String passsword = new String(txtPassword.getPassword());

            if (user.isEmpty() || passsword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Campos incompletos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Instanciamos tu DAO pasándole la conexión inyectada
                UsuariosDAO dao = new UsuariosDAO(conexion);
                
                // Suponiendo que tu método del backend se llama validarLogin o verificarUsuario
                Usuarios usuarioLogueado = dao.validarLogin(user, passsword);

                if (usuarioLogueado != null) {
                	// BIFURCACIÓN DE VISTAS SEGÚN EL ROL DE LA BASE DE DATOS
                    // Revisa cómo se llama el getter de tu modelo (ej. getIdRol() o idRol)
                    if (usuarioLogueado.getRol() == Rol.ADMINISTRADOR) { 
                        JOptionPane.showMessageDialog(this, "¡Bienvenido al Panel de Administración, " + usuarioLogueado.getUsername() + "!");
                        
                        // Abrimos el menú exclusivo para administradores
                        new MenuAdministradorFrame(conexion, usuarioLogueado);
                    } else {
                        JOptionPane.showMessageDialog(this, "¡Bienvenido, " + usuarioLogueado.getUsername() + "!");
                        
                        // Abrimos el Menu Principal heredando la sesión de usuario convencional
                        new MenuPrincipal(conexion, usuarioLogueado);
                    }
             
                    
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error de comunicación con PostgreSQL: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }
}
