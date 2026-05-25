package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection; // INTEGRADO PARA LA CONEXIÓN A POSTGRESQL
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.TorneosDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.RankingsHistoricosDAO;
//torneo, Rankings historicos, jornadas

public class RankingFrame extends JFrame {

    JPanel panel;

    JLabel lblTitulo;

    JComboBox<String> comboTorneo;

    JTable tablaRanking;

    JScrollPane scroll;

    JButton btnActualizar;
    JButton btnVolver;

    JPanel cardTabla;

    // ATRIBUTOS DE PERSISTENCIA INYECTADOS
    private Connection conexion;
    private Usuarios usuarioSesion;

    // CONSTRUCTOR MODIFICADO PARA RECIBIR Y MANTENER LA SESIÓN ACTIVA
    public RankingFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        setTitle("Ranking General");

        // TAMAÑO MÁS EQUILIBRADO
        setSize(1280, 720);

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

        panel = new JPanel();

        panel.setLayout(new BorderLayout());

        panel.setBackground(new Color(226,232,240));

        // =========================
        // TITULO
        // =========================

        lblTitulo = new JLabel(
                "TABLA GENERAL DE POSICIONES"
        );

        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        lblTitulo.setForeground(
                new Color(15,23,42)
        );

        // =========================
        // COMBO TORNEO
        // =========================

        comboTorneo = new JComboBox<>();

        comboTorneo.addItem("APERTURA 2026");

        comboTorneo.addItem("CLAUSURA 2026");

        comboTorneo.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        comboTorneo.setPreferredSize(
                new Dimension(220, 38)
        );

        // =========================
        // PANEL SUPERIOR
        // =========================

        JPanel panelSuperior = new JPanel();

        panelSuperior.setBackground(
                new Color(226,232,240)
        );

        panelSuperior.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        25,
                        20
                )
        );

        panelSuperior.add(lblTitulo);

        panelSuperior.add(comboTorneo);

        // =========================
        // COLUMNAS
        // =========================

        String columnas[] = {

            "USUARIO",

            "J1",
            "J2",
            "J3",
            "J4",
            "J5",
            "J6",
            "J7",
            "J8",
            "J9",
            "J10",
            "J11",
            "J12",
            "J13",
            "J14",
            "J15",
            "J16",
            "J17",

            "TOTAL"
        };

        // =========================
        // DATOS VACIOS
        // =========================

        String datos[][] = {};

        // =========================
        // TABLA
        // =========================

        tablaRanking = new JTable(
                new DefaultTableModel(datos, columnas)
        );

        cargarRanking();

        tablaRanking.setRowHeight(38);

        tablaRanking.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        tablaRanking.setBackground(
                new Color(30,41,59)
        );

        tablaRanking.setForeground(Color.WHITE);

        tablaRanking.setGridColor(
                new Color(71,85,105)
        );

        tablaRanking.setSelectionBackground(
                new Color(59,130,246)
        );

        tablaRanking.setSelectionForeground(
                Color.WHITE
        );

        tablaRanking.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        // =========================
        // HEADER
        // =========================

        tablaRanking.getTableHeader().setBackground(
                new Color(15,23,42)
        );

        tablaRanking.getTableHeader().setForeground(
                Color.WHITE
        );

        tablaRanking.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        tablaRanking.getTableHeader().setReorderingAllowed(false);

        // =========================
        // CENTRAR TEXTO
        // =========================

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        for (int i = 0; i < tablaRanking.getColumnCount(); i++) {

            tablaRanking.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(centerRenderer);

            tablaRanking.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(60);
        }

        // USUARIO MÁS GRANDE

        tablaRanking.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(160);

        // TOTAL MÁS GRANDE

        tablaRanking.getColumnModel()
                .getColumn(18)
                .setPreferredWidth(90);

        // =========================
        // SCROLL
        // =========================

        scroll = new JScrollPane(tablaRanking);

        scroll.getViewport().setBackground(
                new Color(30,41,59)
        );

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(59,130,246),
                        2
                )
        );

        // =========================
        // CARD TABLA
        // =========================

        cardTabla = new JPanel();

        cardTabla.setLayout(new BorderLayout());

        cardTabla.setBackground(
                new Color(15,23,42)
        );

        cardTabla.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        cardTabla.add(scroll, BorderLayout.CENTER);

        // =========================
        // BOTON ACTUALIZAR
        // =========================

        btnActualizar = new JButton("ACTUALIZAR");

        btnActualizar.setPreferredSize(
                new Dimension(200, 42)
        );

        btnActualizar.setBackground(
                new Color(59,130,246)
        );
        
     // Propiedades de renderizado crítico para macOS
        btnActualizar.setOpaque(true);
        btnActualizar.setBorderPainted(false);
        
        btnActualizar.setForeground(Color.WHITE);

        btnActualizar.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        btnActualizar.setFocusPainted(false);

        btnActualizar.setBorderPainted(false);

        btnActualizar.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        // =========================
        // BOTON VOLVER
        // =========================

        btnVolver = new JButton("VOLVER");

        btnVolver.setPreferredSize(
                new Dimension(200, 42)
        );

        btnVolver.setBackground(
                new Color(71,85,105)
        );
        // Propiedades de renderizado crítico para macOS
        btnVolver.setOpaque(true);
        btnVolver.setBorderPainted(false);

        btnVolver.setForeground(Color.WHITE);

        btnVolver.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        btnVolver.setFocusPainted(false);

        btnVolver.setBorderPainted(false);

        btnVolver.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        // =========================
        // PANEL BOTONES
        // =========================

        JPanel panelBotones = new JPanel();

        panelBotones.setBackground(
                new Color(226,232,240)
        );

        panelBotones.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        30,
                        15
                )
        );

        panelBotones.add(btnActualizar);

        panelBotones.add(btnVolver);

        // =========================
        // AGREGAR COMPONENTES
        // =========================

        panel.add(panelSuperior, BorderLayout.NORTH);

        panel.add(cardTabla, BorderLayout.CENTER);

        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);

        // =========================
        // EVENTO ACTUALIZAR
        // =========================

        btnActualizar.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    null,
                    "Ranking actualizado correctamente"
            );

        });

        // =========================
        // EVENTO VOLVER MODIFICADO CON SESIÓN
        // =========================

        btnVolver.addActionListener(e -> {
            // Regresa al menú principal heredando la sesión activa intacta
            new MenuPrincipal(conexion, usuarioSesion);
            dispose();

        });

    }

    // =========================
    // METODO PARA CARGAR DATOS
    // =========================

    private void cargarRanking() {

        DefaultTableModel modelo =
                (DefaultTableModel) tablaRanking.getModel();

        // DATOS TEMPORALES
        // DESPUES SE CAMBIARAN POR POSTGRESQL

        modelo.addRow(new Object[] {
            "Antonio",
            8,7,6,5,9,7,8,6,7,5,8,9,6,7,8,7,9,
            122
        });

        modelo.addRow(new Object[] {
            "Juan",
            7,8,5,6,7,8,6,7,8,6,7,8,5,7,6,8,7,
            116
        });

        modelo.addRow(new Object[] {
            "Ana",
            6,7,8,5,6,7,8,6,7,8,5,6,7,8,5,7,6,
            112
        });

        modelo.addRow(new Object[] {
            "Carlos",
            5,6,7,8,5,6,7,8,5,6,7,8,5,6,7,8,5,
            109
        });

        modelo.addRow(new Object[] {
            "Luis",
            8,5,6,7,8,5,6,7,8,5,6,7,8,5,6,7,8,
            118
        });

    }

}
