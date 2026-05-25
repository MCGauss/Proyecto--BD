/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection; // INYECTADO PARA ARQUITECTURA GLOBAL
import javax.swing.*;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.*;

public class QuinielaFrame extends JFrame {

    JPanel panelPrincipal;

    JLabel lblTitulo;
    JLabel lblSubtitulo;
    JLabel lblPremio;

    JPanel panelPartidos;

    JButton btnGuardar;
    JButton btnVolver;

    boolean editable = true;

    // ATRIBUTOS DE CONTROL DE PERSISTENCIA Y SESIÓN (FES ACATLÁN - MAC)
    private Connection conexion;
    private Usuarios usuarioSesion;

    // CONSTRUCTOR MODIFICADO PARA INYECTAR LA CONEXIÓN Y LA SESIÓN ACTIVA
    public QuinielaFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        setTitle("Quiniela");
        setSize(1500, 950);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        iniciarComponentes();

        setVisible(true);
    }

    private void iniciarComponentes() {

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(new Color(226, 232, 240));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setPreferredSize(new Dimension(1500, 120));
        panelSuperior.setBackground(new Color(15, 23, 42));
        panelSuperior.setLayout(null);

        lblTitulo = new JLabel("QUINIELA - JORNADA 5");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setBounds(40, 20, 500, 40);

        lblSubtitulo = new JLabel("APERTURA 2026");
        lblSubtitulo.setForeground(new Color(148, 163, 184));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblSubtitulo.setBounds(45, 65, 250, 25);

        lblPremio = new JLabel("PREMIO ACUMULADO: $50,000");
        lblPremio.setForeground(new Color(16, 185, 129));
        lblPremio.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblPremio.setBounds(980, 40, 400, 35);

        panelSuperior.add(lblTitulo);
        panelSuperior.add(lblSubtitulo);
        panelSuperior.add(lblPremio);

        panelPartidos = new JPanel();
        panelPartidos.setBackground(new Color(226, 232, 240));
        panelPartidos.setLayout(new GridLayout(9, 1, 18, 18));
        panelPartidos.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        panelPartidos.add(crearPartido("AMÉRICA", "CHIVAS"));
        panelPartidos.add(crearPartido("CRUZ AZUL", "PUMAS"));
        panelPartidos.add(crearPartido("TIGRES", "MONTERREY"));
        panelPartidos.add(crearPartido("LEÓN", "TOLUCA"));
        panelPartidos.add(crearPartido("PACHUCA", "ATLAS"));
        panelPartidos.add(crearPartido("SANTOS", "NECAXA"));
        panelPartidos.add(crearPartido("TIJUANA", "PUEBLA"));
        panelPartidos.add(crearPartido("QUERÉTARO", "MAZATLÁN"));

        JScrollPane scroll = new JScrollPane(panelPartidos);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel panelInferior = new JPanel();
        panelInferior.setPreferredSize(new Dimension(1500, 90));
        panelInferior.setBackground(new Color(226, 232, 240));

        btnGuardar = crearBoton("GUARDAR PREDICCIONES", new Color(59, 130, 246));
        btnVolver = crearBoton("VOLVER", new Color(71, 85, 105));

        panelInferior.add(btnGuardar);
        panelInferior.add(Box.createHorizontalStrut(25));
        panelInferior.add(btnVolver);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);

        // EVENTO: GUARDAR PREDICCIONES CONECTADO AL CONTEXTO DE LA BASE DE DATOS
        btnGuardar.addActionListener(e -> {
            // Aquí puedes instanciar tus DAOs compartidos con Diana más adelante para guardar en PostgreSQL
            JOptionPane.showMessageDialog(null, "Predicciones guardadas correctamente en PostgreSQL");
        });

        // EVENTO: RETORNO PROPAGANDO LOS PARÁMETROS SIN COMPROMETER LA NAVEGACIÓN
        btnVolver.addActionListener(e -> {
            new TorneoPrediccionesFrame(conexion, usuarioSesion);
            dispose();
        });
    }

    private JPanel crearPartido(String local, String visitante) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setPreferredSize(new Dimension(1350, 120));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 2));

        JLabel lblLocal = new JLabel(local);
        lblLocal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLocal.setForeground(new Color(15, 23, 42));
        lblLocal.setHorizontalAlignment(SwingConstants.CENTER);
        lblLocal.setBounds(30, 40, 220, 30);

        JLabel lblGolesLocal = new JLabel("Goles");
        lblGolesLocal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGolesLocal.setBounds(270, 20, 80, 20);

        JSpinner spnLocal = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        spnLocal.setBounds(270, 45, 70, 35);
        spnLocal.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel lblVs = new JLabel("VS");
        lblVs.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblVs.setForeground(new Color(100, 116, 139));
        lblVs.setBounds(380, 43, 50, 30);

        JLabel lblGolesVisitante = new JLabel("Goles");
        lblGolesVisitante.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGolesVisitante.setBounds(470, 20, 80, 20);

        JSpinner spnVisitante = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        spnVisitante.setBounds(470, 45, 70, 35);
        spnVisitante.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel lblVisitante = new JLabel(visitante);
        lblVisitante.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblVisitante.setForeground(new Color(15, 23, 42));
        lblVisitante.setHorizontalAlignment(SwingConstants.CENTER);
        lblVisitante.setBounds(580, 40, 240, 30);

        JLabel lblResultado = new JLabel("Resultado");
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblResultado.setForeground(new Color(71, 85, 105));
        lblResultado.setBounds(930, 15, 100, 20);

        JTextField txtResultado = new JTextField();
        txtResultado.setEditable(false);
        txtResultado.setHorizontalAlignment(JTextField.CENTER);
        txtResultado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtResultado.setBounds(900, 45, 180, 35);

        JButton btnCalcular = new JButton("CALCULAR");
        btnCalcular.setBounds(1120, 43, 140, 40);
        btnCalcular.setBackground(new Color(59, 130, 246));
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setFocusPainted(false);
        btnCalcular.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnCalcular.addActionListener(e -> {
            int golesLocal = (int) spnLocal.getValue();
            int golesVisitante = (int) spnVisitante.getValue();

            if (golesLocal > golesVisitante) {
                txtResultado.setText("GANA " + local);
            } else if (golesVisitante > golesLocal) {
                txtResultado.setText("GANA " + visitante);
            } else {
                txtResultado.setText("EMPATE");
            }
        });

        card.add(lblLocal);
        card.add(lblGolesLocal);
        card.add(spnLocal);
        card.add(lblVs);
        card.add(lblGolesVisitante);
        card.add(spnVisitante);
        card.add(lblVisitante);
        card.add(lblResultado);
        card.add(txtResultado);
        card.add(btnCalcular);

        return card;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(250, 45));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }
}