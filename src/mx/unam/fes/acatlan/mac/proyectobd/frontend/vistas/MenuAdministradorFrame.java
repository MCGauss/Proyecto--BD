package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import javax.swing.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class MenuAdministradorFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private JPanel panelPrincipal;
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    
    // Botones de acciones administrativas
    private JButton btnActualizarGoles;
    private JButton btnGestionUsuarios;
    private JButton btnControlBolsas;
    private JButton btnCerrarSesion;

    private Connection conexion;
    private Usuarios adminSesion;

    public MenuAdministradorFrame(Connection conexion, Usuarios adminSesion) {
        this.conexion = conexion;
        this.adminSesion = adminSesion;
        
        setTitle("Panel de Administración - Sistema de Quinielas");
        setSize(1024, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        iniciarComponentes();
        setVisible(true);
    }

    private void iniciarComponentes() {
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(new Color(226, 232, 240)); // Gris claro de fondo

        // Encabezado del Panel
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(15, 23, 42)); // Slate oscuro institucional
        panelSuperior.setLayout(new GridLayout(2, 1, 5, 5));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        lblTitulo = new JLabel("PANEL DE CONTROL ADMINISTRATIVO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblSubtitulo = new JLabel("Administrador activo: " + adminSesion.getUsername().toUpperCase());
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(148, 163, 184)); // Texto gris atenuado
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        panelSuperior.add(lblTitulo);
        panelSuperior.add(lblSubtitulo);

        // Contenedor de las Tarjetas/Botones de Acción (Grid elegante de 3 columnas)
        JPanel panelAcciones = new JPanel();
        panelAcciones.setBackground(new Color(226, 232, 240));
        panelAcciones.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 60));
        panelAcciones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Estilización de botones usando tu paleta visual corporativa
        btnActualizarGoles = crearBotonAdministrativo("ACTUALIZAR GOLES", new Color(59, 130, 246)); // Azul
        btnGestionUsuarios = crearBotonAdministrativo("ELIMINAR USUARIOS", new Color(239, 68, 68)); // Rojo peligro
        btnControlBolsas = crearBotonAdministrativo("AUDITORIA DE PREMIOS", new Color(16, 185, 129)); // Verde éxito

        panelAcciones.add(btnActualizarGoles);
        panelAcciones.add(btnGestionUsuarios);
        panelAcciones.add(btnControlBolsas);

        // Barra inferior para salir de forma segura
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(226, 232, 240));
        panelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT, 30, 15));

        btnCerrarSesion = new JButton("CERRAR SESIÓN");
        btnCerrarSesion.setPreferredSize(new Dimension(220, 40));
        btnCerrarSesion.setBackground(new Color(71, 85, 105));
        btnCerrarSesion.setOpaque(true);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setFocusPainted(false);
        panelInferior.add(btnCerrarSesion);

        // Ensamblar la ventana
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelAcciones, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        add(panelPrincipal);

        // --- MANEJO DE EVENTOS ---

        // 1. Botón para actualizar resultados de partidos reales
        btnActualizarGoles.addActionListener(e -> {
        	new ActualizarGolesFrame(conexion, adminSesion);
            dispose(); // Cierra el menú para dar foco al módulo

        });

        // 2. Botón para dar de baja usuarios o limpiar infractores
        btnGestionUsuarios.addActionListener(e -> {
        	new GestionUsuariosFrame(conexion, adminSesion);
            dispose(); // Cierra el menú para dar foco al módulo
        });

        // 3. Botón para interactuar con la tabla bolsa_premios
        btnControlBolsas.addActionListener(e -> {
        	new AuditoriaPremiosFrame(conexion, adminSesion);
            dispose(); // Cierra el hub para delegar el foco
        });

        // 4. Salida e invalidación de ventana
        btnCerrarSesion.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿Estás seguro de que deseas salir del módulo de administración?", 
                    "Cerrar Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                new LoginFrame(conexion); // Retorna a la pantalla de acceso universal
                dispose();
            }
        });
    }

    /**
     * Helper metodológico para generar botones grandes tipo "Card" consistentes
     */
    private JButton crearBotonAdministrativo(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(280, 160));
        boton.setBackground(colorFondo);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        
        // Efecto sutil de borde blanco para resaltar el botón como un panel interactivo
        boton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return boton;
    }
}