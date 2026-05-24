/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection; // INTEGRADO PARA LA PERSISTENCIA DE LA CONEXIÓN

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.*;

public class RecargaSaldoFrame extends JFrame {

    private JTextField txtTarjeta;
    private JTextField txtNombre;
    private JTextField txtVencimiento; 
    private JPasswordField txtCVV;
    private JTextField txtMonto;

    // ATRIBUTOS DE PERSISTENCIA INYECTADOS
    private Connection conexion;
    private Usuarios usuarioSesion;

    // CONSTRUCTOR MODIFICADO PARA RECIBIR Y MANTENER LA SESIÓN ACTIVA
    public RecargaSaldoFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;
        
        iniciarComponentes();
    }

    private void iniciarComponentes() {

        setTitle("Recargar saldo");
        setSize(700, 580); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true); 

        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(new Color(18, 18, 18));

        //---------------- HEADER ----------------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 25, 25));
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("RECARGAR SALDO");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        JButton btnVolver = new JButton("Volver");
        btnVolver.setFocusPainted(false);

        // SE ENVÍA LA CONEXIÓN Y LA SESIÓN DE REGRESO A MI CUENTA
        btnVolver.addActionListener(e -> {
            new MiCuentaFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        header.add(titulo, BorderLayout.WEST);
        header.add(btnVolver, BorderLayout.EAST);

        
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(new Color(18, 18, 18));
        panelCentral.setBorder(new EmptyBorder(20, 60, 20, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0; 

        // 1. Número de Tarjeta
        gbc.gridx = 0; gbc.gridy = 0;
        panelCentral.add(crearLabel("Número de tarjeta"), gbc);
        gbc.gridy = 1;
        txtTarjeta = crearTextField();
        panelCentral.add(txtTarjeta, gbc);

        // 2. Nombre del Titular
        gbc.gridy = 2;
        panelCentral.add(crearLabel("Nombre del titular"), gbc);
        gbc.gridy = 3;
        txtNombre = crearTextField();
        panelCentral.add(txtNombre, gbc);

        // --- FILA COMPARTIDA: Vencimiento y CVV ---
        
        JPanel panelFilaCorta = new JPanel(new GridLayout(1, 2, 20, 0));
        panelFilaCorta.setBackground(new Color(18, 18, 18));

        // Subpanel Vencimiento
        JPanel subPanelVenc = new JPanel(new BorderLayout(0, 5));
        subPanelVenc.setBackground(new Color(18, 18, 18));
        txtVencimiento = crearTextField();
        txtVencimiento.setToolTipText("MM/AA");
        subPanelVenc.add(crearLabel("Vencimiento (MM/AA)"), BorderLayout.NORTH);
        subPanelVenc.add(txtVencimiento, BorderLayout.CENTER);

        // Subpanel CVV
        JPanel subPanelCVV = new JPanel(new BorderLayout(0, 5));
        subPanelCVV.setBackground(new Color(18, 18, 18));
        txtCVV = new JPasswordField();
        estilizarCampo(txtCVV);
        subPanelCVV.add(crearLabel("CVV"), BorderLayout.NORTH);
        subPanelCVV.add(txtCVV, BorderLayout.CENTER);

        panelFilaCorta.add(subPanelVenc);
        panelFilaCorta.add(subPanelCVV);

        gbc.gridy = 4;
        panelCentral.add(panelFilaCorta, gbc);
        // -------------------------------------------

        // 4. Monto a Recargar
        gbc.gridy = 5;
        panelCentral.add(crearLabel("Monto a recargar"), gbc);
        gbc.gridy = 6;
        txtMonto = crearTextField();
        panelCentral.add(txtMonto, gbc);

        //---------------- FOOTER ----------------
        JPanel footer = new JPanel();
        footer.setBackground(new Color(18, 18, 18));
        footer.setBorder(new EmptyBorder(10, 30, 25, 30));

        JButton btnRecargar = new JButton("CONFIRMAR RECARGA");
        btnRecargar.setPreferredSize(new Dimension(260, 45));
        btnRecargar.setBackground(new Color(0, 153, 76));
        btnRecargar.setForeground(Color.WHITE);
        btnRecargar.setFont(new Font("Arial", Font.BOLD, 16));
        btnRecargar.setFocusPainted(false);

        btnRecargar.addActionListener(e -> realizarRecarga());

        footer.add(btnRecargar);

        //---------------- AGREGAR ----------------
        fondo.add(header, BorderLayout.NORTH);
        fondo.add(panelCentral, BorderLayout.CENTER);
        fondo.add(footer, BorderLayout.SOUTH);

        add(fondo);
    }

    //---------------- MÉTODO RECARGA ----------------
    private void realizarRecarga() {
        String tarjeta = txtTarjeta.getText();
        String nombre = txtNombre.getText();
        String vencimiento = txtVencimiento.getText(); 
        String monto = txtMonto.getText();
        String cvv = new String(txtCVV.getPassword());

        if (tarjeta.isEmpty() || nombre.isEmpty() || vencimiento.isEmpty()
                || monto.isEmpty() || cvv.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Completa todos los campos"
            );
            return;
        }

        // NOTA DE BACKEND: Aquí posteriormente realizarás el UPDATE del saldo en PostgreSQL 
        // usando un DAO e incrementando el saldo del objeto 'usuarioSesion'.
        
        JOptionPane.showMessageDialog(
                this,
                "Saldo recargado correctamente"
        );

        // REENVÍO DE DATOS DE SESIÓN TRAS LA OPERACIÓN
        new MiCuentaFrame(conexion, usuarioSesion).setVisible(true); 
        dispose();
    }

    //---------------- ESTILOS ----------------
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14)); 
        return label;
    }

    private JTextField crearTextField() {
        JTextField txt = new JTextField();
        estilizarCampo(txt);
        return txt;
    }

    private void estilizarCampo(JTextField txt) {
        
        txt.setPreferredSize(new Dimension(txt.getPreferredSize().width, 45));
        txt.setFont(new Font("Arial", Font.PLAIN, 18));
        txt.setBackground(new Color(35, 35, 35));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);

        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
    }
}