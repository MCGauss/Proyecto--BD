package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import javax.swing.*;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.*;

public class SeleccionQuinielaFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JPanel panelPrincipal;

    JLabel lblTitulo;

    JPanel cardJornada;
    JPanel cardTorneo;

    JButton btnJornada;
    JButton btnTorneo;

    JButton btnVolver;

    // ATRIBUTOS DE PERSISTENCIA INYECTADOS
    private Connection conexion;
    private Usuarios usuarioSesion;

    // CONSTRUCTOR MODIFICADO PARA MANEJAR LA PERSISTENCIA DE LA SESIÓN
    public SeleccionQuinielaFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        setTitle("Seleccionar Tipo de Quiniela");

        setSize(1200, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(true);

        iniciarComponentes();

        setVisible(true);
    }

    private void iniciarComponentes() {

        // =========================
        // PANEL PRINCIPAL
        // =========================

        panelPrincipal = new JPanel();

        panelPrincipal.setLayout(null);

        panelPrincipal.setBackground(
                new Color(226,232,240)
        );

        // =========================
        // TITULO
        // =========================

        lblTitulo = new JLabel(
                "SELECCIONA TU TIPO DE QUINIELA"
        );

        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 34)
        );

        lblTitulo.setForeground(
                new Color(15,23,42)
        );

        lblTitulo.setBounds(250, 40, 700, 50);

        // =========================
        // CARD JORNADA
        // =========================

        cardJornada = new JPanel();

        cardJornada.setLayout(null);

        cardJornada.setBackground(
                new Color(15,23,42)
        );

        cardJornada.setBounds(120, 160, 400, 330);

        cardJornada.setBorder(
                BorderFactory.createLineBorder(
                        new Color(59,130,246),
                        3
                )
        );

        JLabel lblJornadaTitulo = new JLabel(
                "POR JORNADA"
        );

        lblJornadaTitulo.setForeground(Color.WHITE);

        lblJornadaTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        lblJornadaTitulo.setBounds(90, 40, 250, 40);

        JLabel lblJornadaDesc = new JLabel(
                "<html>"
                + "<center>"
                + "Participa semana a semana<br><br>"
                + "Inscripción individual<br><br>"
                + "Premios por jornada<br><br>"
                + "Mayor flexibilidad"
                + "</center>"
                + "</html>"
        );

        lblJornadaDesc.setForeground(Color.WHITE);

        lblJornadaDesc.setFont(
                new Font("Segoe UI", Font.PLAIN, 18)
        );

        lblJornadaDesc.setBounds(60, 100, 280, 120);

        btnJornada = new JButton("ENTRAR");

        btnJornada.setBounds(110, 250, 180, 45);

        btnJornada.setBackground(
                new Color(59,130,246)
        );

        btnJornada.setForeground(Color.WHITE);

        btnJornada.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        btnJornada.setFocusPainted(false);

        btnJornada.setBorderPainted(false);

        btnJornada.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        cardJornada.add(lblJornadaTitulo);

        cardJornada.add(lblJornadaDesc);

        cardJornada.add(btnJornada);

        // =========================
        // CARD TORNEO
        // =========================

        cardTorneo = new JPanel();

        cardTorneo.setLayout(null);

        cardTorneo.setBackground(
                new Color(15,23,42)
        );

        cardTorneo.setBounds(650, 160, 400, 330);

        cardTorneo.setBorder(
                BorderFactory.createLineBorder(
                        new Color(16,185,129),
                        3
                )
        );

        JLabel lblTorneoTitulo = new JLabel(
                "TORNEO COMPLETO"
        );

        lblTorneoTitulo.setForeground(Color.WHITE);

        lblTorneoTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 26)
        );

        lblTorneoTitulo.setBounds(55, 40, 320, 40);

        JLabel lblTorneoDesc = new JLabel(
                "<html>"
                + "<center>"
                + "Participa las 17 jornadas<br><br>"
                + "Ranking general<br><br>"
                + "Premios acumulados<br><br>"
                + "Competencia completa"
                + "</center>"
                + "</html>"
        );

        lblTorneoDesc.setForeground(Color.WHITE);

        lblTorneoDesc.setFont(
                new Font("Segoe UI", Font.PLAIN, 18)
        );

        lblTorneoDesc.setBounds(60, 100, 280, 120);

        btnTorneo = new JButton("ENTRAR");

        btnTorneo.setBounds(110, 250, 180, 45);

        btnTorneo.setBackground(
                new Color(16,185,129)
        );

        btnTorneo.setForeground(Color.WHITE);

        btnTorneo.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        btnTorneo.setFocusPainted(false);

        btnTorneo.setBorderPainted(false);

        btnTorneo.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        cardTorneo.add(lblTorneoTitulo);

        cardTorneo.add(lblTorneoDesc);

        cardTorneo.add(btnTorneo);

        // =========================
        // BOTON VOLVER
        // =========================

        btnVolver = new JButton("VOLVER");

        btnVolver.setBounds(470, 560, 220, 45);

        btnVolver.setBackground(
                new Color(71,85,105)
        );

        btnVolver.setForeground(Color.WHITE);

        btnVolver.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        btnVolver.setFocusPainted(false);

        btnVolver.setBorderPainted(false);

        btnVolver.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        // =========================
        // AGREGAR COMPONENTES
        // =========================

        panelPrincipal.add(lblTitulo);

        panelPrincipal.add(cardJornada);

        panelPrincipal.add(cardTorneo);

        panelPrincipal.add(btnVolver);

        add(panelPrincipal);

        // =========================================================
        // EVENTOS CORREGIDOS CON PROPAGACIÓN DE FLUJO DE DATOS
        // =========================================================

        // POR JORNADA
        btnJornada.addActionListener(e -> {
            // CORREGIDO: Ahora propaga la sesión actual de forma segura
            new JornadaHubFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        // TORNEO COMPLETO
        btnTorneo.addActionListener(e -> {
            // Abre el Hub del Torneo pasando los parámetros de sesión
            new TorneoHubFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        // VOLVER
        btnVolver.addActionListener(e -> {
            // Regresa al menú principal heredando la sesión activa intacta
            new MenuPrincipal(conexion, usuarioSesion).setVisible(true);
            dispose();
        });
    }
}