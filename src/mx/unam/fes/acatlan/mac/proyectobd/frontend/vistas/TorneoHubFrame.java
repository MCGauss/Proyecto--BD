package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import javax.swing.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class TorneoHubFrame extends JFrame {

    JPanel panelPrincipal;
    JLabel lblTitulo;
    JPanel cardPanel;
    JButton btnEntrar;
    JButton btnRanking;
    JButton btnVolver;

    // ATRIBUTOS DE CONEXIÓN Y SESIÓN
    private Connection conexion;
    private Usuarios usuarioSesion;

    // CONSTRUCTOR MODIFICADO: Ahora recibe obligatoriamente la conexión y el usuario activo
    public TorneoHubFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        setTitle("Torneo Completo");
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        iniciarComponentes();
        configurarEventosBD(); // Lógica de botones centralizada

        setVisible(true);
    }

    private void iniciarComponentes() {

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(null);
        panelPrincipal.setBackground(new Color(226, 232, 240));

        // =========================
        // TITULO
        // =========================
        lblTitulo = new JLabel("TORNEO COMPLETO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBounds(430, 50, 600, 50);

        // =========================
        // CARD CENTRAL
        // =========================
        cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(16, 185, 129), 2));
        cardPanel.setBounds(280, 160, 800, 480);

        // =========================
        // TITULO CARD
        // =========================
        JLabel lblTorneo = new JLabel("APERTURA 2026");
        lblTorneo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblTorneo.setForeground(new Color(16, 185, 129));
        lblTorneo.setBounds(220, 40, 400, 50);

        // =========================
        // JORNADA ACTUAL
        // =========================
        JLabel lblJornada = new JLabel("Jornada actual: 5 de 17");
        lblJornada.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblJornada.setForeground(new Color(15, 23, 42));
        lblJornada.setBounds(240, 120, 350, 40);

        // =========================
        // DESCRIPCION
        // =========================
        JTextArea descripcion = new JTextArea();
        descripcion.setText("• Participa en el torneo completo.\n\n"
                          + "• Compite durante las 17 jornadas.\n\n"
                          + "• Acumula puntos y escala posiciones.\n\n"
                          + "• Si el torneo inicia ya no podrás modificar tus predicciones.");
        descripcion.setEditable(false);
        descripcion.setFocusable(false);
        descripcion.setOpaque(false);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        descripcion.setForeground(new Color(71, 85, 105));
        descripcion.setBounds(120, 200, 580, 160);

        // =========================
        // BOTONES
        // =========================
        btnEntrar = new JButton("ENTRAR AL TORNEO");
        btnEntrar.setBounds(120, 380, 250, 55);
        btnEntrar.setBackground(new Color(16, 185, 129));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // =========================
        btnRanking = new JButton("VER RANKING");
        btnRanking.setBounds(430, 380, 250, 55);
        btnRanking.setBackground(new Color(59, 130, 246));
        btnRanking.setForeground(Color.WHITE);
        btnRanking.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnRanking.setFocusPainted(false);
        btnRanking.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // =========================
        // BOTON VOLVER
        // =========================
        btnVolver = new JButton("VOLVER");
        btnVolver.setBounds(560, 700, 250, 55);
        btnVolver.setBackground(new Color(71, 85, 105));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // =========================
        // AGREGAR A CARD
        // =========================
        cardPanel.add(lblTorneo);
        cardPanel.add(lblJornada);
        cardPanel.add(descripcion);
        cardPanel.add(btnEntrar);
        cardPanel.add(btnRanking);

        // =========================
        // AGREGAR A FRAME
        // =========================
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(cardPanel);
        panelPrincipal.add(btnVolver);

        add(panelPrincipal);
    }

    /**
     * Centraliza la navegación del Frontend pasando la sesión activa y la conexión a PostgreSQL.
     */
    private void configurarEventosBD() {
        
        // Al entrar a las predicciones del torneo, le heredamos la sesión
        btnEntrar.addActionListener(e -> {
            new TorneoPrediccionesFrame(conexion, usuarioSesion);
            dispose();
        });

        // Si tienes una vista de ranking conectada a la BD
        btnRanking.addActionListener(e -> {
            new RankingFrame(conexion, usuarioSesion);
            dispose();
        });

        // Al volver, regresamos al frame de Selección de Quinielas manteniendo el estado
        btnVolver.addActionListener(e -> {
            new SeleccionQuinielaFrame(conexion, usuarioSesion);
            dispose();
        });
    }
}