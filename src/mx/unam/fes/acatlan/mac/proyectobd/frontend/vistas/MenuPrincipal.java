package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.io.File;
import java.sql.Connection; 
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

        setTitle("FootBets");
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
        // PANEL DE MENÚ (BARRA LATERAL - DISEÑO FIEL A TONY)
        // =========================================================
        panelMenu = new JPanel();
        panelMenu.setPreferredSize(new Dimension(260, 700));
        panelMenu.setBackground(new Color(15, 23, 42)); // Azul oscuro profundo original
        panelMenu.setLayout(null);

        // TÍTULO DE LA APP: Regresamos al original de Tony
        lblTitulo = new JLabel("FootBets");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setBounds(40, 40, 220, 40);
        panelMenu.add(lblTitulo);

        // SUBTÍTULO: Sello oficial del equipo "THE FOREIGN KEY SQUAD"
        lblSubtitulo = new JLabel("THE FOREIGN KEY SQUAD");
        lblSubtitulo.setForeground(new Color(148, 163, 184));
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setBounds(25, 80, 220, 30);
        panelMenu.add(lblSubtitulo);

        // BOTONES CON COLORES, FUENTES, COORDENADAS Y TEXTOS EXACTOS DE TONY
        btnQuinielas = new JButton("QUINIELAS");
        btnQuinielas.setBounds(25, 160, 200, 50);
        btnQuinielas.setBackground(new Color(59, 130, 246)); // Azul brillante
        btnQuinielas.setForeground(Color.WHITE);
        btnQuinielas.setFont(new Font("Arial", Font.BOLD, 16));
        btnQuinielas.setFocusPainted(false);
        btnQuinielas.setBorderPainted(false);
        btnQuinielas.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRanking = new JButton("RANKING");
        btnRanking.setBounds(25, 240, 200, 50);
        btnRanking.setBackground(new Color(16, 185, 129)); // Verde éxito
        btnRanking.setForeground(Color.WHITE);
        btnRanking.setFont(new Font("Arial", Font.BOLD, 16));
        btnRanking.setFocusPainted(false);
        btnRanking.setBorderPainted(false);
        btnRanking.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCuenta = new JButton("MI CUENTA");
        btnCuenta.setBounds(25, 320, 200, 50);
        btnCuenta.setBackground(new Color(245, 158, 11)); // Ámbar/Naranja cuenta
        btnCuenta.setForeground(Color.WHITE);
        btnCuenta.setFont(new Font("Arial", Font.BOLD, 16));
        btnCuenta.setFocusPainted(false);
        btnCuenta.setBorderPainted(false);
        btnCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSalir = new JButton("CERRAR SESIÓN");
        btnSalir.setBounds(25, 480, 200, 45); 
        btnSalir.setBackground(new Color(71, 85, 105)); // Gris Slate de salida discreto
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Arial", Font.BOLD, 15));
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelMenu.add(btnQuinielas);
        panelMenu.add(btnRanking);
        panelMenu.add(btnCuenta);
        panelMenu.add(btnSalir);

        // =========================================================
        // PANEL DE CONTENIDO (FONDO ANIMADO CON LEYENDA ORIGINAL)
        // =========================================================
        panelContenido = new PanelFondoGif();
        panelContenido.setLayout(null);

        // Modificamos el mensaje de bienvenida
        String userDisplay = (usuarioSesion != null) ? usuarioSesion.getUsername().toUpperCase() : "USER";
        JLabel bienvenida = new JLabel("BIENVENID@, " + userDisplay);
        bienvenida.setFont(new Font("Arial", Font.BOLD, 34));
        bienvenida.setForeground(new Color(15, 23, 42));
        bienvenida.setBounds(220, 40, 650, 50);

        JLabel descripcion = new JLabel("Administra tus quinielas y predicciones");
        descripcion.setFont(new Font("Arial", Font.PLAIN, 20));
        descripcion.setForeground(new Color(71, 85, 105));
        descripcion.setBounds(220, 95, 500, 40);

        panelContenido.add(bienvenida);
        panelContenido.add(descripcion);

        // INTEGRAR PANELES AL FRAME PRINCIPAL
        add(panelMenu, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);
    }

    /**
     * NAVEGACIÓN COMPLETA PROPAGANDO LA CONEXIÓN Y EL USUARIO A LAS SIGUIENTES PANTALLAS
     */
    private void configurarNavegacionBD() {
        btnSalir.addActionListener(e -> {
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
     * CLASE INTERNA: Administrador del fondo visual con el recurso multimedia (.gif)
     */
    private class PanelFondoGif extends JPanel {
        private static final long serialVersionUID = 1L;
        private Image imagenFondo;

        public PanelFondoGif() {
            // CORRECCIÓN DE DISCO: Validamos la carpeta externa 'Assets' que vimos en el árbol de Eclipse
            File archivoGif = new File("Assets/futbol.gif");
            if (archivoGif.exists()) {
                imagenFondo = new ImageIcon(archivoGif.getAbsolutePath()).getImage();
            } else {
                // Fallback clásico por Classpath interno en src/assets por si acaso
                java.net.URL url = getClass().getResource("/assets/futbol.gif");
                if (url != null) {
                    imagenFondo = new ImageIcon(url).getImage();
                } else {
                    // Fallback alternativo de recursos ordinario
                    java.net.URL urlRecursos = getClass().getResource("/recursos/futbol.gif");
                    if (urlRecursos != null) {
                        imagenFondo = new ImageIcon(urlRecursos).getImage();
                    }
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            // Fondo base grisáceo claro idéntico al de Tony
            g2d.setColor(new Color(226, 232, 240));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            if (imagenFondo != null) {
                g2d.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            }

            // Capa de opacidad traslúcida oficial (180 de Alpha) para mantener legibles los textos oscuros
            g2d.setColor(new Color(226, 232, 240, 180)); 
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.dispose();
        }
    }
}