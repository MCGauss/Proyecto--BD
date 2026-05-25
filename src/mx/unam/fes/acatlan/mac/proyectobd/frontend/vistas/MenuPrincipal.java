package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection; // CONEXIÓN INTEGRADA A POSTGRESQL
import javax.swing.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios; // IMPORTA TU MODELO

public class MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    
    JPanel panelMenu;
    PanelFondoGif panelContenido; 

    JLabel lblTitulo;
    JLabel lblSubtitulo;

    JButton btnQuinielas;
    JButton btnRanking;
    JButton btnCuenta;
    JButton btnSalir;

    // ATRIBUTOS DE CONTROL DE ESTADO GLOBAL INYECTADOS
    private Connection conexion;
    private Usuarios usuarioSesion;

    // CONSTRUCTOR ADAPTADO PARA MANTENER LA PERSISTENCIA DE LA SESIÓN ACTIVA
    public MenuPrincipal(Connection conexion, Usuarios usuarioLogueado) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioLogueado;

        setTitle("Sistema de Quinielas");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        iniciarComponentes();
        configurarNavegacionBD(); // CONTROL DE FLUJOS CON PARÁMETROS

        setVisible(true);
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout());

        // =========================================================
        // PANEL DE MENÚ (BARRA LATERAL)
        // =========================================================
        panelMenu = new JPanel();
        panelMenu.setPreferredSize(new Dimension(260, 700));
        panelMenu.setBackground(new Color(15, 23, 42));
        panelMenu.setLayout(null);

        // TÍTULO DE LA APP
        lblTitulo = new JLabel("PRO-QUINIELAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(248, 250, 252));
        lblTitulo.setBounds(30, 40, 200, 30);
        panelMenu.add(lblTitulo);

        // SUBTÍTULO CON NOMBRE DINÁMICO DESDE EL MODELO
        String userDisplay = (usuarioSesion != null) ? usuarioSesion.getUsername() : "Usuario";
        lblSubtitulo = new JLabel("Hola, " + userDisplay);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(148, 163, 184));
        lblSubtitulo.setBounds(30, 75, 200, 20);
        panelMenu.add(lblSubtitulo);

        // REUTILIZACIÓN DE ESTILOS PARA LOS BOTONES DEL MENÚ LATERAL
        btnQuinielas = crearBotonMenu("JUGAR QUINIELAS", 150);
        btnRanking = crearBotonMenu("RANKING GLOBAL", 210);
        btnCuenta = crearBotonMenu("MI CUENTA", 270);
        btnSalir = crearBotonMenu("CERRAR SESIÓN", 560);
        
        // Estilo especial de salida (Rojo/Gris Slate oscuro)
        btnSalir.setBackground(new Color(30, 41, 59));
        btnSalir.setForeground(new Color(239, 68, 68));

        panelMenu.add(btnQuinielas);
        panelMenu.add(btnRanking);
        panelMenu.add(btnCuenta);
        panelMenu.add(btnSalir);

        // =========================================================
        // PANEL DE CONTENIDO (FONDO ANIMADO DEL ESTADIO)
        // =========================================================
        panelContenido = new PanelFondoGif();

        // INTEGRAR PANELES AL FRAME PRINCIPAL
        add(panelMenu, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);
    }

    /**
     * MÉTODOS AUXILIARES: Creación de botones estilizados para el menú
     */
    private JButton crearBotonMenu(String texto, int posY) {
        JButton boton = new JButton(texto);
        boton.setBounds(20, posY, 220, 45);
        boton.setBackground(new Color(30, 41, 59));
        boton.setForeground(new Color(241, 245, 249));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /**
     * NAVEGACIÓN COMPLETA PROPAGANDO LA CONEXIÓN Y EL USUARIO A LAS SIGUIENTES PANTALLAS
     */
    private void configurarNavegacionBD() {
        btnSalir.addActionListener(e -> {
            // Regresa al login pasándole únicamente la conexión limpia
            new LoginFrame(conexion).setVisible(true);
            dispose();
        });

        btnRanking.addActionListener(e -> {
            new RankingFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        btnQuinielas.addActionListener(e -> {
            new SeleccionQuinielaFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        btnCuenta.addActionListener(e -> {
            new MiCuentaFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });
    }

    /**
     * CLASE INTERNA: Administrador del fondo visual con el recurso multimedia
     */
    private class PanelFondoGif extends JPanel {
        private static final long serialVersionUID = 1L;
        private Image imagenFondo;

        public PanelFondoGif() {
            // Nota de Eclipse: Asegúrate de colocar el archivo en tu carpeta de recursos del proyecto
            String rutaGif = "/mx/unam/fes/acatlan/mac/proyectobd/frontend/recursos/estadio.gif";
            java.net.URL url = getClass().getResource(rutaGif);
            if (url != null) {
                imagenFondo = new ImageIcon(url).getImage();
            } else {
                // Alternativa por si el recurso no carga inmediatamente o cambia de ruta en el build path
                imagenFondo = new ImageIcon("src/mx/unam.fes.acatlan.mac.proyectobd.frontend.recursos/estadio.gif").getImage();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagenFondo != null) {
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Color fallback por si no se encuentra el GIF
                g.setColor(new Color(30, 41, 59));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}