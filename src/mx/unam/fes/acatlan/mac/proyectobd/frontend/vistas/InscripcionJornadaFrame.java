/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection; // INTEGRADO PARA LA CONEXIÓN A POSTGRESQL
import javax.swing.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios; // IMPORTA TU MODELO

public class InscripcionJornadaFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JPanel panelPrincipal;

    JButton btnVolver;

    // EL SALDO AHORA SE EXTRAE DINÁMICAMENTE DE LA SESIÓN ACTIVADA
    double saldoUsuario;
    double costoJornada = 100.0;

    // ATRIBUTOS DE PERSISTENCIA INYECTADOS
    private Connection conexion;
    private Usuarios usuarioSesion;

    // CONSTRUCTOR ADAPTADO PARA MANTENER LA PERSISTENCIA DE LA SESIÓN ACTIVA
    public InscripcionJornadaFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;
        
        // Asignación dinámica del saldo real del usuario en sesión
        this.saldoUsuario = (usuarioSesion != null) ? usuarioSesion.getSaldo() : 0.0;

        setTitle("Inscripción por Jornada");

        setSize(1000, 750);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(true);

        iniciarComponentes();

        setVisible(true);
    }

    private void iniciarComponentes() {

        panelPrincipal = new JPanel();

        panelPrincipal.setLayout(null);

        panelPrincipal.setBackground(
                new Color(226,232,240)
        );

        // =====================================
        // TITULO
        // =====================================

        JLabel lblTitulo = new JLabel(
                "INSCRIPCIÓN POR JORNADA"
        );

        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 34)
        );

        lblTitulo.setForeground(
                new Color(15,23,42)
        );

        lblTitulo.setBounds(260, 30, 500, 40);

        panelPrincipal.add(lblTitulo);

        // =====================================
        // SALDO DINÁMICO
        // =====================================

        JLabel lblSaldo = new JLabel(
                "Saldo disponible: $" + saldoUsuario
        );

        lblSaldo.setFont(
                new Font("Segoe UI", Font.BOLD, 20)
        );

        lblSaldo.setForeground(
                new Color(16,185,129)
        );

        lblSaldo.setBounds(60, 90, 350, 30);

        panelPrincipal.add(lblSaldo);

        // =====================================
        // PANEL TABLA
        // =====================================

        JPanel tablaPanel = new JPanel();

        tablaPanel.setLayout(
                new GridLayout(18, 4, 10, 10)
        );

        tablaPanel.setBackground(Color.WHITE);

        tablaPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,20,20,20
                )
        );

        // =====================================
        // ENCABEZADOS
        // =====================================

        tablaPanel.add(crearHeader("NÚM."));
        tablaPanel.add(crearHeader("JORNADA"));
        tablaPanel.add(crearHeader("ESTATUS"));
        tablaPanel.add(crearHeader("ACCIÓN"));

        int jornadaActual = 5;

        // =====================================
        // LISTADO JORNADAS (1 A 17)
        // =====================================

        for(int i = 1; i <= 17; i++){

            JLabel lblNumero = new JLabel(
                    String.valueOf(i),
                    SwingConstants.CENTER
            );

            lblNumero.setFont(
                    new Font("Segoe UI", Font.PLAIN, 18)
            );

            JLabel lblJornada = new JLabel(
                    "Jornada " + i,
                    SwingConstants.CENTER
            );

            lblJornada.setFont(
                    new Font("Segoe UI", Font.PLAIN, 18)
            );

            JLabel lblEstado = new JLabel(
                    "",
                    SwingConstants.CENTER
            );

            lblEstado.setFont(
                    new Font("Segoe UI", Font.BOLD, 16)
            );

            JButton btnAccion = new JButton();

            btnAccion.setFont(
                    new Font("Segoe UI", Font.BOLD, 14)
            );

            btnAccion.setFocusPainted(false);
            btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // =====================================
            // MANEJO DE ESTADOS Y EVENTOS PROPAGADOS
            // =====================================

            if(i < jornadaActual){

                lblEstado.setText("FINALIZADA");

                lblEstado.setForeground(
                        new Color(239,68,68)
                );

                btnAccion.setText("CERRADA");

                btnAccion.setEnabled(false);

            }
            else if(i == jornadaActual){

                lblEstado.setText("EN CURSO");

                lblEstado.setForeground(
                        new Color(245,158,11)
                );

                btnAccion.setText("VER");

                btnAccion.setBackground(
                        new Color(245,158,11)
                );

                btnAccion.setForeground(Color.WHITE);

                btnAccion.addActionListener(e -> {
                    // Pasa estado editable en falso y propaga la sesión actual
                    QuinielaFrame frame = new QuinielaFrame(conexion, usuarioSesion);
                    frame.editable = false; // (Suponiendo que el atributo sea público o manejado por Setter)
                    frame.setVisible(true);
                    dispose();
                });

            }
            else{

                lblEstado.setText("DISPONIBLE");

                lblEstado.setForeground(
                        new Color(16,185,129)
                );

                btnAccion.setText("INSCRIBIRSE");

                btnAccion.setBackground(
                        new Color(16,185,129)
                );

                btnAccion.setForeground(Color.WHITE);

                if(saldoUsuario < costoJornada){

                    btnAccion.setEnabled(false);

                    btnAccion.setText(
                            "SIN SALDO"
                    );

                }

                int jornadaSeleccionada = i;

                btnAccion.addActionListener(e -> {

                    JOptionPane.showMessageDialog(
                            null,
                            "Inscripción realizada a Jornada " + jornadaSeleccionada
                    );

                    // Posteriormente aquí invocarás al DAO para restar el saldo en PostgreSQL
                    new QuinielaFrame(conexion, usuarioSesion).setVisible(true);
                    dispose();

                });

            }

            tablaPanel.add(lblNumero);

            tablaPanel.add(lblJornada);

            tablaPanel.add(lblEstado);

            tablaPanel.add(btnAccion);

        }

        JScrollPane scroll = new JScrollPane(tablaPanel);

        scroll.setBounds(60, 140, 850, 450);

        scroll.setBorder(null);

        panelPrincipal.add(scroll);

        // =====================================
        // BOTON VOLVER
        // =====================================

        btnVolver = new JButton("VOLVER");

        btnVolver.setBounds(360, 620, 220, 50);

        btnVolver.setBackground(
                new Color(71,85,105)
        );

        btnVolver.setForeground(Color.WHITE);

        btnVolver.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
                );

        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelPrincipal.add(btnVolver);

        add(panelPrincipal);

        // =====================================
        // EVENTOS DE RETORNO AL HUB DE JORNADAS
        // =====================================

        btnVolver.addActionListener(e -> {
            new JornadaHubFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

    }

    // =====================================
    // HEADERS AUXILIARES
    // =====================================

    private JLabel crearHeader(String texto){

        JLabel label = new JLabel(
                texto,
                SwingConstants.CENTER
        );

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        label.setForeground(Color.WHITE);

        label.setOpaque(true);

        label.setBackground(
                new Color(15,23,42)
        );

        return label;

    }
}