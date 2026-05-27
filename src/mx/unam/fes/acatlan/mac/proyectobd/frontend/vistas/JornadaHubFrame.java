package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection; // INTEGRADO PARA LA CONEXIÓN A POSTGRESQL
import javax.swing.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios; // IMPORTA TU MODELO

public class JornadaHubFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JPanel panelPrincipal;

    JLabel lblTitulo;

    JPanel cardActual;
    JPanel cardProxima;

    JButton btnVerActual;
    JButton btnInscribirse;
    JButton btnVolver;

    // ATRIBUTOS DE PERSISTENCIA INYECTADOS
    private Connection conexion;
    private Usuarios usuarioSesion;
    int jornada;

    // CONSTRUCTOR MODIFICADO PARA RECIBIR Y MANTENER LA SESIÓN ACTIVA
    public JornadaHubFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        setTitle("Quiniela por Jornada");

        setSize(1400, 850);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(true);

        iniciarComponentes();

        setVisible(true);
    }

    private void iniciarComponentes() {

        panelPrincipal = new JPanel();

        panelPrincipal.setLayout(null);

        panelPrincipal.setBackground(
                new Color(226,232,240)
        );

        // =========================
        // TITULO
        // =========================

        lblTitulo = new JLabel(
                "QUINIELA POR JORNADA"
        );

        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 34)
        );

        lblTitulo.setForeground(
                new Color(15,23,42)
        );

        lblTitulo.setBounds(430, 40, 600, 50);

        // =========================
        // CARD JORNADA ACTUAL
        // =========================

        cardActual = crearCard(
                "JORNADA ACTUAL",
                "Jornada 5 - Apertura 2026",
                "La jornada ya inició.\nSolo disponible para consulta.",
                new Color(239,68,68)
        );

        cardActual.setBounds(120, 160, 500, 350);

        btnVerActual = crearBoton(
                "VER QUINIELA",
                new Color(59,130,246)
        );

        btnVerActual.setBounds(140, 250, 220, 45);

        cardActual.add(btnVerActual);

        // =========================
        // CARD PROXIMA JORNADA
        // =========================

        cardProxima = crearCard(
                "PRÓXIMA JORNADA",
                "Jornada 6 - Apertura 2026",
                "Inscripciones abiertas.\nCosto de entrada: $100",
                new Color(16,185,129)
        );

        cardProxima.setBounds(740, 160, 500, 350);

        btnInscribirse = crearBoton(
                "INSCRIBIRSE",
                new Color(16,185,129)
        );

        btnInscribirse.setBounds(140, 250, 220, 45);

        cardProxima.add(btnInscribirse);

        // =========================
        // BOTON VOLVER
        // =========================

        btnVolver = crearBoton(
                "VOLVER",
                new Color(71,85,105)
        );

        btnVolver.setBounds(560, 650, 250, 45);

        // =========================
        // AGREGAR COMPONENTES
        // =========================

        panelPrincipal.add(lblTitulo);

        panelPrincipal.add(cardActual);

        panelPrincipal.add(cardProxima);

        panelPrincipal.add(btnVolver);

        add(panelPrincipal);

        // =========================================================
        // EVENTOS CONFIGURADOS CON MANEJO DE SESIÓN Y VISIBILIDAD
        // =========================================================

        btnVerActual.addActionListener(e -> {
            new QuinielaFrame(conexion, usuarioSesion, jornada).setVisible(true);
            dispose();
        });

        btnInscribirse.addActionListener(e -> {
            new InscripcionJornadaFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        btnVolver.addActionListener(e -> {
            new SeleccionQuinielaFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

    }

    // =========================
    // CREAR CARD
    // =========================

    private JPanel crearCard(
            String titulo,
            String subtitulo,
            String descripcion,
            Color color
    ) {

        JPanel panel = new JPanel();

        panel.setLayout(null);

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createLineBorder(
                        color,
                        3
                )
        );

        JLabel lblTituloCard = new JLabel(titulo);

        lblTituloCard.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        lblTituloCard.setForeground(color);

        lblTituloCard.setBounds(60, 40, 350, 40);

        JLabel lblSubtitulo = new JLabel(subtitulo);

        lblSubtitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 20)
        );

        lblSubtitulo.setForeground(
                new Color(15,23,42)
        );

        lblSubtitulo.setBounds(60, 110, 350, 35);

        JTextArea txtDescripcion = new JTextArea(descripcion);

        txtDescripcion.setEditable(false);

        txtDescripcion.setOpaque(false);

        txtDescripcion.setFont(
                new Font("Segoe UI", Font.PLAIN, 18)
        );

        txtDescripcion.setForeground(
                new Color(71,85,105)
        );

        txtDescripcion.setBounds(60, 170, 350, 70);

        panel.add(lblTituloCard);

        panel.add(lblSubtitulo);

        panel.add(txtDescripcion);

        return panel;

    }

    // =========================
    // CREAR BOTON
    // =========================

    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton boton = new JButton(texto);

        boton.setBackground(color);

        boton.setForeground(Color.WHITE);

        boton.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        boton.setFocusPainted(false);

        boton.setBorderPainted(false);

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return boton;

    }
}