package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection; // INTEGRADO PARA LA SESIÓN GLOBAL
import javax.swing.*;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.*;

public class TorneoPrediccionesFrame extends JFrame {

    JPanel panelPrincipal;
    JLabel lblTitulo;
    JLabel lblSubtitulo;
    JPanel panelJornadas;
    JButton btnGuardar;
    JButton btnVolver;

    boolean editable = true;

    // ATRIBUTOS DE PERSISTENCIA INYECTADOS
    private Connection conexion;
    private Usuarios usuarioSesion;

    // CONSTRUCTOR MODIFICADO PARA RECIBIR Y ASIGNAR LA CONEXIÓN Y EL USUARIO ACTIVO
    public TorneoPrediccionesFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        setTitle("Predicciones del Torneo");

        setSize(1600, 950);

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

        panelPrincipal.setLayout(new BorderLayout());

        panelPrincipal.setBackground(
                new Color(226,232,240)
        );

        // =========================
        // PANEL SUPERIOR
        // =========================

        JPanel panelSuperior = new JPanel();

        panelSuperior.setPreferredSize(
                new Dimension(1600, 110)
        );

        panelSuperior.setBackground(
                new Color(15,23,42)
        );

        panelSuperior.setLayout(null);

        lblTitulo = new JLabel(
                "TORNEO COMPLETO - APERTURA 2026"
        );

        lblTitulo.setForeground(Color.WHITE);

        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 34)
        );

        lblTitulo.setBounds(40, 20, 700, 40);

        lblSubtitulo = new JLabel(
                "Captura de predicciones de las 17 jornadas"
        );

        lblSubtitulo.setForeground(
                new Color(148,163,184)
        );

        lblSubtitulo.setFont(
                new Font("Segoe UI", Font.PLAIN, 20)
        );

        lblSubtitulo.setBounds(45, 65, 500, 25);

        panelSuperior.add(lblTitulo);

        panelSuperior.add(lblSubtitulo);

        // =========================
        // PANEL JORNADAS
        // =========================

        panelJornadas = new JPanel();

        panelJornadas.setBackground(
                new Color(226,232,240)
        );

        panelJornadas.setLayout(
                new GridLayout(
                        17,
                        1,
                        18,
                        18
                )
        );

        panelJornadas.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        35,
                        25,
                        35
                )
        );

        // =========================
        // AGREGAR 17 JORNADAS
        // =========================

        for(int i = 1; i <= 17; i++){

            panelJornadas.add(
                    crearCardJornada(i)
            );

        }

        JScrollPane scroll = new JScrollPane(
                panelJornadas
        );

        scroll.setBorder(null);

        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // =========================
        // PANEL INFERIOR
        // =========================

        JPanel panelInferior = new JPanel();

        panelInferior.setPreferredSize(
                new Dimension(1600, 90)
        );

        panelInferior.setBackground(
                new Color(226,232,240)
        );

        btnGuardar = crearBoton(
                "GUARDAR TORNEO",
                new Color(59,130,246)
        );

        btnVolver = crearBoton(
                "VOLVER",
                new Color(71,85,105)
        );

        panelInferior.add(btnGuardar);

        panelInferior.add(Box.createHorizontalStrut(25));

        panelInferior.add(btnVolver);

        // =========================
        // AGREGAR COMPONENTES
        // =========================

        panelPrincipal.add(
                panelSuperior,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                scroll,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelInferior,
                BorderLayout.SOUTH
        );

        add(panelPrincipal);

        // =========================
        // EVENTOS
        // =========================

        btnGuardar.addActionListener(e -> {
            // Aquí podrás integrar la inserción masiva a PostgreSQL en el futuro
            JOptionPane.showMessageDialog(
                    null,
                    "Predicciones del torneo guardadas en PostgreSQL"
            );
        });

        btnVolver.addActionListener(e -> {
            // Regresa al Hub heredando los parámetros de sesión intactos
            new TorneoHubFrame(conexion, usuarioSesion);
            dispose();
        });

    }

    // =========================
    // CREAR CARD JORNADA
    // =========================

    private JPanel crearCardJornada(int jornada) {

        JPanel card = new JPanel();

        card.setLayout(null);

        card.setPreferredSize(
                new Dimension(1450, 130)
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createLineBorder(
                        new Color(203,213,225),
                        2
                )
        );

        JLabel lblJornada = new JLabel(
                "JORNADA " + jornada
        );

        lblJornada.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        lblJornada.setForeground(
                new Color(15,23,42)
        );

        lblJornada.setBounds(40, 25, 300, 35);

        JLabel lblEstado = new JLabel(
                jornada <= 4
                ? "CERRADA"
                : "ABIERTA"
        );

        lblEstado.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        lblEstado.setForeground(
                jornada <= 4
                ? new Color(239,68,68)
                : new Color(16,185,129)
        );

        lblEstado.setBounds(45, 70, 200, 25);

        JLabel lblPartidos = new JLabel(
                "9 partidos disponibles"
        );

        lblPartidos.setFont(
                new Font("Segoe UI", Font.PLAIN, 18)
        );

        lblPartidos.setForeground(
                new Color(71,85,105)
        );

        lblPartidos.setBounds(350, 45, 250, 30);

        JButton btnEntrar = crearBoton(
                jornada <= 4
                ? "CONSULTAR"
                : "CAPTURAR",
                jornada <= 4
                ? new Color(100,116,139)
                : new Color(59,130,246)
        );

        btnEntrar.setBounds(1080, 40, 220, 45);

        // =========================
        // EVENTO: SE PASA LA CONEXIÓN Y USUARIO A QUINIELAFRAME
        // =========================

        btnEntrar.addActionListener(e -> {

            QuinielaFrame frame =
                    new QuinielaFrame(conexion, usuarioSesion, jornada);

            if(jornada <= 4){

                frame.editable = false;

            }

            dispose();

        });

        card.add(lblJornada);

        card.add(lblEstado);

        card.add(lblPartidos);

        card.add(btnEntrar);

        return card;

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
