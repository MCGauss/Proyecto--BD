package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;

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
    private Predicciones pred;

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
        lblTitulo = new JLabel("BIENVENID@ " + nombreUsuario);

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
        /*SELECT COUNT(*)
        FROM predicciones
        JOIN partido USING (id_partido)
        WHERE id_status_partido = 1
        AND id_usuario = ?;*/
        
     // =========================================================
        // RECUPERACIÓN DE DATOS DINÁMICOS DESDE DAOs
        // =========================================================
        int inscripcionesActivas = 0;
        int torneosInscritos = 0;

        if (usuarioSesion != null && conexion != null) {
            // Instanciamos los DAOs pasándoles la conexión activa del Frame
            PrediccionesDAO prediccionesDAO = new PrediccionesDAO(conexion);
            InscripcionesDAO inscripcionesDAO = new InscripcionesDAO(conexion);
            
            // Consultamos los datos reales del usuario
            inscripcionesActivas = prediccionesDAO.contarPrediccionesActivas(usuarioSesion.getIdUsuario());
            torneosInscritos = inscripcionesDAO.contarTorneosInscritos(usuarioSesion.getIdUsuario());
        }

        // =========================================================
        // CARD QUINIELAS / INSCRIPCIONES
        // =========================================================
        cardQuinielas = crearCard(
                "INSCRIPCIONES ACTIVAS",
                String.valueOf(inscripcionesActivas), // Convertimos a String para el método crearCard
                430,
                140
        );

        // =========================================================
        // CARD TORNEOS
        // =========================================================
        cardTorneos = crearCard(
                "TORNEOS INSCRITOS",
                String.valueOf(torneosInscritos),
                790,
                140
        );

        // =========================
        // TABLA MOVIMIENTOS
        // =========================

        String[] columnas = {"FECHA", "TIPO DE MOVIMIENTO", "MONTO"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaMovimientos = new JTable(modelo);
        tablaMovimientos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaMovimientos.setRowHeight(35);
        tablaMovimientos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaMovimientos.getTableHeader().setBackground(new Color(15, 23, 42));
        tablaMovimientos.getTableHeader().setForeground(Color.WHITE);
        tablaMovimientos.setGridColor(new Color(226, 232, 240));

        // Alineación central del contenido de las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tablaMovimientos.getColumnCount(); i++) {
            tablaMovimientos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scroll = new JScrollPane(tablaMovimientos);
        scroll.setBounds(50, 280, 1100, 280);
        panel.add(scroll);

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


        // =========================================================
        // EVENTOS CON PROPAGACIÓN DE FLUJO Y DATOS DE SESIÓN
        // =========================================================

        btnRecargar.addActionListener(e -> {
            new RecargaSaldoFrame(conexion, usuarioSesion).setVisible(true); //CHECAR COMPATIBILIDAD CON TRANSACCIONES ????
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
        
        add(panel);
        
        //Método dinámico para conectar la BD
        cargarMovimientos();

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
        DefaultTableModel modelo = (DefaultTableModel) tablaMovimientos.getModel();
        modelo.setRowCount(0); // Limpiamos las filas por defecto del Mock Data anterior

        if (usuarioSesion == null) {
            return;
        }

        try {
            // Instanciar el DAO pasándole el objeto Connection inyectado
            TransaccionesDAO transDAO = new TransaccionesDAO(conexion);
            
            // Consultar la tabla unificada 'transaccion' filtrada por el usuario activo
            List<Map<String, Object>> listaTransacciones = transDAO.obtenerHistorialPorUsuario(usuarioSesion.getIdUsuario());

            // Agregar cada registro mapeado directamente en el JTable
            for (Map<String, Object> mov : listaTransacciones) {
                modelo.addRow(new Object[] {
                    mov.get("fecha"),
                    mov.get("tipo"),
                    mov.get("monto")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al recuperar el historial unificado desde PostgreSQL: " + ex.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}