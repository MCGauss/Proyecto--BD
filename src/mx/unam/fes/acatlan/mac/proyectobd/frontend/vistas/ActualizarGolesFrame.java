package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class ActualizarGolesFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable tablaPartidos;
    private JTextField txtGolesLocal, txtGolesVis;
    private JButton btnGuardar, btnVolver;
    
    private Connection conexion;
    private Usuarios adminSesion;

    public ActualizarGolesFrame(Connection conexion, Usuarios adminSesion) {
        this.conexion = conexion;
        this.adminSesion = adminSesion;

        setTitle("FootBets - Actualizar Marcadores Oficiales");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarPartidosReal();
        setVisible(true);
    }

    private void iniciarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(226, 232, 240));

        // Título Superior
        JPanel panelSup = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSup.setBackground(new Color(15, 23, 42));
        JLabel lblTitulo = new JLabel("REGISTRO DE MARCADORES OFICIALES");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panelSup.add(lblTitulo);

        // Tabla de Partidos
        String[] columnas = {"ID Partido", "Equipo Local", "G. Local", "G. Visitante", "Equipo Visitante", "Jornada"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaPartidos = new JTable(modelo);
        tablaPartidos.setRowHeight(32);
        tablaPartidos.setBackground(new Color(30, 41, 59));
        tablaPartidos.setForeground(Color.WHITE);
        tablaPartidos.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tablaPartidos.getColumnCount(); i++) {
            tablaPartidos.getColumnModel().getColumn(i).setCellRenderer(centro);
        }

        JScrollPane scroll = new JScrollPane(tablaPartidos);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Panel Lateral de Captura (Formulario Rápido)
        JPanel panelCaptura = new JPanel(new GridLayout(6, 1, 10, 10));
        panelCaptura.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(15, 23, 42)), "Capturar Goles"));
        panelCaptura.setBackground(new Color(226, 232, 240));
        panelCaptura.setPreferredSize(new Dimension(240, 300));

        JLabel lblL = new JLabel("Goles Local:", SwingConstants.CENTER);
        lblL.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtGolesLocal = new JTextField();
        txtGolesLocal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtGolesLocal.setHorizontalAlignment(JTextField.CENTER);

        JLabel lblV = new JLabel("Goles Visitante:", SwingConstants.CENTER);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtGolesVis = new JTextField();
        txtGolesVis.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtGolesVis.setHorizontalAlignment(JTextField.CENTER);

        btnGuardar = new JButton("PUBLICAR MARCADOR");
        btnGuardar.setBackground(new Color(59, 130, 246)); // Azul corporativo
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);

        panelCaptura.add(lblL);
        panelCaptura.add(txtGolesLocal);
        panelCaptura.add(lblV);
        panelCaptura.add(txtGolesVis);
        panelCaptura.add(new JLabel()); // Espacio sutil
        panelCaptura.add(btnGuardar);

        // Panel de Cierre Inferior
        JPanel panelInf = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelInf.setBackground(new Color(226, 232, 240));
        btnVolver = new JButton("VOLVER AL PANEL");
        btnVolver.setBackground(new Color(71, 85, 105));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setPreferredSize(new Dimension(180, 35));
        btnVolver.setOpaque(true);
        btnVolver.setBorderPainted(false);
        panelInf.add(btnVolver);

        // Unir todo al Layout principal
        panelPrincipal.add(panelSup, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);
        panelPrincipal.add(panelCaptura, BorderLayout.EAST);
        panelPrincipal.add(panelInf, BorderLayout.SOUTH);
        add(panelPrincipal);

        // Lógica de Selección de Filas automáticas
        tablaPartidos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaPartidos.getSelectedRow();
            if (fila != -1) {
                txtGolesLocal.setText(String.valueOf(tablaPartidos.getValueAt(fila, 2)));
                txtGolesVis.setText(String.valueOf(tablaPartidos.getValueAt(fila, 3)));
            }
        });

        // --- Acciones de Eventos ---
        btnVolver.addActionListener(e -> {
            new MenuAdministradorFrame(conexion, adminSesion);
            dispose();
        });

        btnGuardar.addActionListener(e -> {
            int fila = tablaPartidos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un partido de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idPartido = (int) tablaPartidos.getValueAt(fila, 0);
            
            try {
                int gLocal = Integer.parseInt(txtGolesLocal.getText().trim());
                int gVis = Integer.parseInt(txtGolesVis.getText().trim());

                if (gLocal < 0 || gVis < 0) {
                    JOptionPane.showMessageDialog(this, "Los goles no pueden ser negativos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Query SQL apuntando a la tabla 'partido' de tu LDD
                String sql = "UPDATE partido SET goles_eq_local = ?, goles_eq_vis = ?, id_status_partido = 3 WHERE id_partido = ?";
                PreparedStatement ps = conexion.prepareStatement(sql);
                ps.setInt(1, gLocal);
                ps.setInt(2, gVis);
                ps.setInt(3, idPartido);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "¡Marcador oficial guardado y estatus actualizado!");
                cargarPartidosReal(); // Refrescar los datos inmediatamente
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Escribe valores numéricos válidos en los goles.", "Formato Incorrecto", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error de actualización: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cargarPartidosReal() {
        DefaultTableModel modelo = (DefaultTableModel) tablaPartidos.getModel();
        modelo.setRowCount(0);
        try {
            // Relacionamos las llaves foráneas de tu LDD: id_eq_local, id_eq_vis e id_jornada
            String sql = "SELECT p.id_partido, el.nombre_equipo AS local, p.goles_eq_local, " +
                         "p.goles_eq_vis, ev.nombre_equipo AS visitante, j.nombre_jornada " +
                         "FROM partido p " +
                         "JOIN equipos el ON p.id_eq_local = el.id_equipo " +
                         "JOIN equipos ev ON p.id_eq_vis = ev.id_equipo " +
                         "JOIN jornadas j ON p.id_jornada = j.id_jornada " +
                         "WHERE p.id_status_partido = 3 " + // Solo traea  los partidos con estatus finalizado
                         "ORDER BY p.id_partido ASC";
            
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_partido"),
                    rs.getString("local"),
                    rs.getObject("goles_eq_local") == null ? 0 : rs.getInt("goles_eq_local"),
                    rs.getObject("goles_eq_vis") == null ? 0 : rs.getInt("goles_eq_vis"),
                    rs.getString("visitante"),
                    rs.getString("nombre_jornada")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al mapear partidos: " + e.getMessage());
        }
    }
}