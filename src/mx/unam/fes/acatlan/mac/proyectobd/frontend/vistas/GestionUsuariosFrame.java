package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class GestionUsuariosFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private JTable tablaUsuarios;
    private JButton btnEliminar, btnVolver;
    private Connection conexion;
    private Usuarios adminSesion;

    public GestionUsuariosFrame(Connection conexion, Usuarios adminSesion) {
        this.conexion = conexion;
        this.adminSesion = adminSesion;

        setTitle("FootBets - Control de Usuarios");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarUsuariosReal();
        setVisible(true);
    }

    private void iniciarComponentes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(226, 232, 240));

        // Encabezado
        JPanel panelSup = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSup.setBackground(new Color(15, 23, 42));
        JLabel lblTitulo = new JLabel("BAJA DE USUARIOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panelSup.add(lblTitulo);

        // Tabla
        String[] columnas = {"ID USUARIO", "USERNAME", "EMAIL", "SALDO ACTUAL"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaUsuarios = new JTable(modelo);
        tablaUsuarios.setRowHeight(30);
        tablaUsuarios.setBackground(new Color(30, 41, 59));
        tablaUsuarios.setForeground(Color.WHITE);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Centrar datos
        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        for(int i=0; i<tablaUsuarios.getColumnCount(); i++) {
            tablaUsuarios.getColumnModel().getColumn(i).setCellRenderer(centro);
        }

        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        scroll.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Botones
        JPanel panelInf = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        panelInf.setBackground(new Color(226, 232, 240));

        btnEliminar = new JButton("ELIMINAR USUARIO");
        btnEliminar.setBackground(new Color(239, 68, 68)); // Rojo peligro
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEliminar.setPreferredSize(new Dimension(280, 40));
        btnEliminar.setOpaque(true);
        btnEliminar.setBorderPainted(false);

        btnVolver = new JButton("VOLVER");
        btnVolver.setBackground(new Color(71, 85, 105));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.setPreferredSize(new Dimension(150, 40));
        btnVolver.setOpaque(true);
        btnVolver.setBorderPainted(false);

        panelInf.add(btnEliminar);
        panelInf.add(btnVolver);

        panel.add(panelSup, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelInf, BorderLayout.SOUTH);
        add(panel);

        // --- Eventos de Base de Datos ---
        btnVolver.addActionListener(e -> {
            new MenuAdministradorFrame(conexion, adminSesion);
            dispose();
        });

        btnEliminar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idUsuario = (int) tablaUsuarios.getValueAt(fila, 0);
            String username = (String) tablaUsuarios.getValueAt(fila, 1);

            int opc = JOptionPane.showConfirmDialog(this, 
                    "¿Seguro que deseas eliminar a " + username + "? Esto borrará sus datos en cascada.", 
                    "Confirmar Baja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (opc == JOptionPane.YES_OPTION) {
                try {
                    // Eliminación directa de la tabla usuarios
                    String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
                    PreparedStatement ps = conexion.prepareStatement(sql);
                    ps.setInt(1, idUsuario);
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito.");
                    cargarUsuariosReal(); // Refrescar tabla
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar en Postgres: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void cargarUsuariosReal() {
        DefaultTableModel modelo = (DefaultTableModel) tablaUsuarios.getModel();
        modelo.setRowCount(0);
        try {
            // El id_rol = 2 pertenece a los USUARIOS regulares
            String sql = "SELECT id_usuario, username, email, saldo FROM usuarios WHERE id_rol = 2 ORDER BY id_usuario ASC";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_usuario"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getBigDecimal("saldo")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al leer usuarios: " + e.getMessage());
        }
    }
}