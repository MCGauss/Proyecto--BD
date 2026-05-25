package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection; // CONEXIÓN INTEGRADA A POSTGRESQL
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.*;

public class MiCuentaFrame extends JFrame {

    JPanel panel;

    JLabel lblTitulo;
    JLabel lblSaldo;

    JPanel cardSaldo;
    JPanel cardQuinielas;
    JPanel cardTorneos;

    JTable tablaMovimientos;

    JScrollPane scroll;

    JButton btnRecargar;
    JButton btnInscribirse;
    JButton btnVolver;

    // ATRIBUTOS DE PERSISTENCIA INYECTADOS
    private Connection conexion;
    private Usuarios usuarioSesion;

    // CONSTRUCTOR ADAPTADO PARA MANTENER LA PERSISTENCIA DE LA SESIÓN ACTIVA
    public MiCuentaFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        setTitle("Mi Cuenta");

        setSize(1200, 700);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        iniciarComponentes();

        setVisible(true);
    }

    private void iniciarComponentes() {

        panel = new JPanel();

        panel.setLayout(null);

        panel.setBackground(new Color(226,232,240));

        // =========================================================
        // TITULO DINÁMICO CORREGIDO (.getUsername())
        // =========================================================
        String nombreUsuario = (usuarioSesion != null) ? usuarioSesion.getUsername().toUpperCase() : "USUARIO";
        lblTitulo = new JLabel("BIENVENIDO " + nombreUsuario);

        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 30)
        );

        lblTitulo.setForeground(
                new Color(15,23,42)
        );

        lblTitulo.setBounds(380, 20, 500, 40);

        // =========================================================
        // SALDO DINÁMICO (DESDE LA SESIÓN)
        // =========================================================
        double saldoActual = (usuarioSesion != null) ? usuarioSesion.getSaldo() : 0.0;
        lblSaldo = new JLabel("SALDO DISPONIBLE: $" + saldoActual);

        lblSaldo.setFont(
                new Font("Segoe UI", Font.BOLD, 22)
        );

        lblSaldo.setForeground(
                new Color(16,185,129)
        );

        lblSaldo.setBounds(420, 70, 400, 35);

        // =========================
        // CARD SALDO DINÁMICA
        // =========================

        cardSaldo = crearCard(
                "SALDO ACTUAL",
                "$" + saldoActual,
                70,
                140
        );

        // =========================
        // CARD QUINIELAS
        // =========================

        cardQuinielas = crearCard(
                "QUINIELAS ACTIVAS",
                "3",
                430,
                140
        );

        // =========================
        // CARD TORNEOS
        // =========================

        cardTorneos = crearCard(
                "TORNEOS INSCRITOS",
                "2",
                790,
                140
        );

        // =========================
        // TABLA MOVIMIENTOS
        // =========================

        String columnas[] = {
            "FECHA",
            "TIPO",
            "MONTO"
        };

        String datos[][] = {};

        tablaMovimientos = new JTable(
                new DefaultTableModel(datos, columnas)
        );

        cargarMovimientos();

        tablaMovimientos.setRowHeight(38);

        tablaMovimientos.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        tablaMovimientos.setBackground(
                new Color(30,41,59)
        );

        tablaMovimientos.setForeground(Color.WHITE);

        tablaMovimientos.setGridColor(
                new Color(71,85,105)
        );

        tablaMovimientos.getTableHeader().setBackground(
                new Color(15,23,42)
        );

        tablaMovimientos.getTableHeader().setForeground(
                Color.WHITE
        );

        tablaMovimientos.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        // CENTRAR TEXTO

        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        for(int i = 0; i < tablaMovimientos.getColumnCount(); i++) {

            tablaMovimientos.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(center);

        }

        scroll = new JScrollPane(tablaMovimientos);

        scroll.setBounds(180, 340, 800, 180);

        // =========================
        // BOTONES
        // =========================

        btnRecargar = crearBoton(
                "RECARGAR SALDO",
                new Color(59,130,246),
                180,
                570
        );

        btnInscribirse = crearBoton(
                "INSCRIBIRSE",
                new Color(16,185,129),
                490,
                570
        );

        btnVolver = crearBoton(
                "VOLVER",
                new Color(71,85,105),
                800,
                570
        );

        // =========================
        // AGREGAR COMPONENTES
        // =========================

        panel.add(lblTitulo);

        panel.add(lblSaldo);

        panel.add(cardSaldo);

        panel.add(cardQuinielas);

        panel.add(cardTorneos);

        panel.add(scroll);

        panel.add(btnRecargar);

        panel.add(btnInscribirse);

        panel.add(btnVolver);

        add(panel);

        // =========================================================
        // EVENTOS CON PROPAGACIÓN DE FLUJO Y DATOS DE SESIÓN
        // =========================================================

        btnRecargar.addActionListener(e -> {
            new RecargaSaldoFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        btnInscribirse.addActionListener(e -> {
            new SeleccionQuinielaFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        btnVolver.addActionListener(e -> {
            new MenuPrincipal(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

    }

    // =========================
    // CREAR CARDS
    // =========================

    private JPanel crearCard(
            String titulo,
            String valor,
            int x,
            int y
    ) {

        JPanel card = new JPanel();

        card.setLayout(null);

        card.setBackground(new Color(15,23,42));

        card.setBounds(x, y, 300, 140);

        JLabel lblTitulo = new JLabel(titulo);

        lblTitulo.setForeground(Color.WHITE);

        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        lblTitulo.setBounds(40, 25, 250, 30);

        JLabel lblValor = new JLabel(valor);

        lblValor.setForeground(
                new Color(59,130,246)
        );

        lblValor.setFont(
                new Font("Segoe UI", Font.BOLD, 34)
        );

        lblValor.setBounds(110, 65, 150, 40);

        card.add(lblTitulo);

        card.add(lblValor);

        return card;

    }

    // =========================
    // CREAR BOTONES
    // =========================

    private JButton crearBoton(
            String texto,
            Color color,
            int x,
            int y
    ) {

        JButton boton = new JButton(texto);

        boton.setBounds(x, y, 220, 45);

        boton.setBackground(color);

        boton.setForeground(Color.WHITE);

        boton.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        boton.setFocusPainted(false);

        boton.setBorderPainted(false);

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return boton;

    }

    // =========================================================
    // MOCK DATA (SUSTITUIBLE POSTERIORMENTE POR LLAMADA A DAO)
    // =========================================================

    private void cargarMovimientos() {

        DefaultTableModel modelo =
                (DefaultTableModel)
                        tablaMovimientos.getModel();

        // Posteriormente usarás usuarioSesion.getIdUsuario() con tu DAO
        modelo.addRow(new Object[] {
                "10/05/2026",
                "RECARGA",
                "+$500"
        });

        modelo.addRow(new Object[] {
                "11/05/2026",
                "INSCRIPCIÓN",
                "-$100"
        });

        modelo.addRow(new Object[] {
                "12/05/2026",
                "PREMIO",
                "+$250"
        });

    }

}