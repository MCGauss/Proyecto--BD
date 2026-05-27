package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class AuditoriaPremiosFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable tablaAuditoria;
    private JButton btnRefrescar, btnVolver;
    private Connection conexion;
    private Usuarios adminSesion;

    public AuditoriaPremiosFrame(Connection conexion, Usuarios adminSesion) {
        this.conexion = conexion;
        this.adminSesion = adminSesion;

        setTitle("FootBets - Panel de Auditoría Financiera");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        consultarAuditoriaReal();
        setVisible(true);
    }

    private void iniciarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(226, 232, 240));

        // Encabezado institucional
        JPanel panelSup = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSup.setBackground(new Color(15, 23, 42)); // Slate oscuro
        panelSup.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        JLabel lblTitulo = new JLabel("AUDITORÍA GENERAL Y HISTORIAL DE PAGOS DE PREMIOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panelSup.add(lblTitulo);

        // Configuración de la Tabla de Solo Lectura
        String[] columnas = {"FECHA DE PAGO", "MONTO DISTRIBUIDO", "NÚMERO DE JORNADA / BOLSA", "USERNAME GANADOR"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Asegura que sea puramente de auditoría (sin modificaciones manuales)
            }
        };

        tablaAuditoria = new JTable(modelo);
        tablaAuditoria.setRowHeight(35);
        tablaAuditoria.setBackground(new Color(30, 41, 59)); // Slate intermedio
        tablaAuditoria.setForeground(Color.WHITE);
        tablaAuditoria.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Estilo del Header de la tabla
        tablaAuditoria.getTableHeader().setBackground(new Color(15, 23, 42));
        tablaAuditoria.getTableHeader().setForeground(Color.WHITE);
        tablaAuditoria.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Renderizador para centrar el texto de las celdas
        DefaultTableCellRenderer centroRenderer = new DefaultTableCellRenderer();
        centroRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tablaAuditoria.getColumnCount(); i++) {
            tablaAuditoria.getColumnModel().getColumn(i).setCellRenderer(centroRenderer);
        }

        JScrollPane scroll = new JScrollPane(tablaAuditoria);
        scroll.getViewport().setBackground(new Color(30, 41, 59));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(59, 130, 246), 2)); // Borde azul sutil

        // Contenedor intermedio para padding
        JPanel panelTablaContenedor = new JPanel(new BorderLayout());
        panelTablaContenedor.setBackground(new Color(226, 232, 240));
        panelTablaContenedor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelTablaContenedor.add(scroll, BorderLayout.CENTER);

        // Barra de acciones inferior
        JPanel panelInf = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        panelInf.setBackground(new Color(226, 232, 240));

        btnRefrescar = new JButton("REFRESCAR BITÁCORA");
        btnRefrescar.setBackground(new Color(16, 185, 129)); // Verde éxito
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefrescar.setPreferredSize(new Dimension(220, 40));
        btnRefrescar.setOpaque(true);
        btnRefrescar.setBorderPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVolver = new JButton("VOLVER AL PANEL");
        btnVolver.setBackground(new Color(71, 85, 105)); // Gris neutro
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.setPreferredSize(new Dimension(180, 40));
        btnVolver.setOpaque(true);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelInf.add(btnRefrescar);
        panelInf.add(btnVolver);

        // Construir panel de la ventana
        panelPrincipal.add(panelSup, BorderLayout.NORTH);
        panelPrincipal.add(panelTablaContenedor, BorderLayout.CENTER);
        panelPrincipal.add(panelInf, BorderLayout.SOUTH);
        add(panelPrincipal);

        // --- CONTROLADORES DE EVENTOS DE BASE DE DATOS ---
        
        btnRefrescar.addActionListener(e -> {
            consultarAuditoriaReal();
            JOptionPane.showMessageDialog(this, "Historial actualizado desde la Base de Datos.", "Sincronización", JOptionPane.INFORMATION_MESSAGE);
        });

        btnVolver.addActionListener(e -> {
            new MenuAdministradorFrame(conexion, adminSesion);
            dispose();
        });
    }

    /**
     * Realiza el SELECT JOIN seguro contra PostgreSQL recopilando los datos transaccionales
     */
    private void consultarAuditoriaReal() {
        DefaultTableModel modelo = (DefaultTableModel) tablaAuditoria.getModel();
        modelo.setRowCount(0); // Limpiar ejecuciones anteriores

        // Query estructurado adaptado a tus relaciones físicas del LDD
        String sql = "SELECT p.fecha_pago, p.monto_pagado, j.nombre_jornada, u.username " +
                "FROM pagos_premios p " +
                "JOIN usuarios_ganadores ug ON p.id_ganador = ug.id_ganador " +
                "JOIN usuarios u ON ug.id_usuario = u.id_usuario " +
                "JOIN bolsa_premios b ON ug.id_bolsa = b.id_bolsa " + // <- Paso intermedio necesario
                "JOIN jornadas j ON b.id_jornada = j.id_jornada " +    // <- Ahora sí unimos jornadas con b
                "ORDER BY p.fecha_pago DESC";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getTimestamp("fecha_pago"),
                    "$" + rs.getBigDecimal("monto_pagado"),
                    rs.getString("nombre_jornada"),
                    rs.getString("username").toUpperCase()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar el reporte de auditoría: " + e.getMessage(), 
                    "Error de PostgreSQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
