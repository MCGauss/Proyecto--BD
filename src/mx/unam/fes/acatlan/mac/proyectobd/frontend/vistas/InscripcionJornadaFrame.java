package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.JornadasDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.TorneosDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.InscripcionesDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class InscripcionJornadaFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // Paneles Estructurales Contenedores
    private JPanel panelCabecera;
    private JPanel panelColumnas; 
    private JPanel panelContenedorCards;
    private JPanel panelInferior;

    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JLabel lblUsuarioInfo; 
    private JScrollPane scrollPane;
    private JButton btnVolver;

    // Persistencia e Inyección de Dependencias
    private Connection conexion;
    private Usuarios usuarioSesion;
    private int idTorneoActivo = -1;

    public InscripcionJornadaFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        // 1. Consultar metadatos del Torneo Activo al inicializar
        TorneosDAO torneosDAO = new TorneosDAO(conexion);
        Map<String, String> infoTorneo = torneosDAO.obtenerInformacionTorneoHub();
        
        String nombreTorneo = "Sin Torneo Activo";
        if (infoTorneo != null && !infoTorneo.isEmpty()) {
            this.idTorneoActivo = Integer.parseInt(infoTorneo.get("id_torneo"));
            nombreTorneo = infoTorneo.get("nombre_torneo");
        }

        setTitle("Inscripción por Jornada");
        setSize(1600, 950);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        
        setLayout(new BorderLayout());

        iniciarComponentes(nombreTorneo);
    }

    private void iniciarComponentes(String nombreTorneo) {
        // ========================================================
        // 1. CABECERA SUPERIOR (NORTE)
        // ========================================================
        panelCabecera = new JPanel();
        panelCabecera.setLayout(null);
        panelCabecera.setPreferredSize(new Dimension(1600, 150));
        panelCabecera.setBackground(new Color(241, 245, 249));

        lblTitulo = new JLabel("INSCRIPCIÓN POR JORNADA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBounds(60, 25, 800, 55);
        panelCabecera.add(lblTitulo);

        lblSubtitulo = new JLabel("Torneo: " + nombreTorneo + "  |  Selecciona una jornada disponible para participar.");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSubtitulo.setForeground(new Color(71, 85, 105));
        lblSubtitulo.setBounds(60, 90, 800, 30);
        panelCabecera.add(lblSubtitulo);

        String saldoFormateado = String.format("$%.2f", usuarioSesion.getSaldo());
        lblUsuarioInfo = new JLabel("<html><font color='#475569'>Usuario: </font><b>" + usuarioSesion.getUsername() + "</b>"
                + " &nbsp;&nbsp;|&nbsp;&nbsp; <font color='#475569'>Saldo disponible: </font><font color='#10B981'><b>" + saldoFormateado + "</b></font></html>");
        lblUsuarioInfo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblUsuarioInfo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUsuarioInfo.setBounds(750, 40, 700, 40);
        panelCabecera.add(lblUsuarioInfo);

        // ========================================================
        // PANEL DE NOMBRES DE COLUMNAS
        // ========================================================
        panelColumnas = new JPanel();
        panelColumnas.setLayout(null);
        panelColumnas.setPreferredSize(new Dimension(1600, 40));
        panelColumnas.setBackground(new Color(241, 245, 249));

        JLabel colJornada = new JLabel("JORNADA");
        colJornada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        colJornada.setForeground(new Color(148, 163, 184)); 
        colJornada.setBounds(105, 10, 200, 25); 
        panelColumnas.add(colJornada);

        JLabel colEstatus = new JLabel("ESTATUS");
        colEstatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        colEstatus.setForeground(new Color(148, 163, 184));
        colEstatus.setBounds(445, 10, 200, 25); 
        panelColumnas.add(colEstatus);

        JLabel colDescripcion = new JLabel("DETALLE");
        colDescripcion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        colDescripcion.setForeground(new Color(148, 163, 184));
        colDescripcion.setBounds(680, 40, 250, 40); 
        panelColumnas.add(colDescripcion);
        
        JLabel colAccion = new JLabel("ACCION");
        colAccion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        colAccion.setForeground(new Color(148, 163, 184));
        colAccion.setHorizontalAlignment(SwingConstants.CENTER);
        colAccion.setBounds(1220, 10, 220, 25); 
        panelColumnas.add(colAccion);

        JPanel contenedorNorte = new JPanel(new BorderLayout());
        contenedorNorte.add(panelCabecera, BorderLayout.NORTH);
        contenedorNorte.add(panelColumnas, BorderLayout.SOUTH);
        add(contenedorNorte, BorderLayout.NORTH);

        // ========================================================
        // 2. CONTENEDOR DE JORNADAS (CENTRO CON SCROLL)
        // ========================================================
        panelContenedorCards = new JPanel();
        panelContenedorCards.setLayout(new BoxLayout(panelContenedorCards, BoxLayout.Y_AXIS));
        panelContenedorCards.setBackground(new Color(241, 245, 249));
        panelContenedorCards.setBorder(new EmptyBorder(10, 60, 10, 60));

        JornadasDAO jornadasDAO = new JornadasDAO(conexion);
        List<Map<String, String>> listaJornadas = jornadasDAO.obtenerJornadasConEstatusCalculado(idTorneoActivo);

        if (listaJornadas.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay jornadas registradas para este torneo.", SwingConstants.CENTER);
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            lblVacio.setForeground(new Color(100, 116, 139));
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelContenedorCards.add(lblVacio);
        } else {
            for (Map<String, String> j : listaJornadas) {
                panelContenedorCards.add(crearCardJornada(j));
                panelContenedorCards.add(Box.createRigidArea(new Dimension(0, 20))); 
            }
        }

        scrollPane = new JScrollPane(panelContenedorCards);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);

        // ========================================================
        // 3. PANEL INFERIOR FIJO (SUR)
        // ========================================================
        panelInferior = new JPanel();
        panelInferior.setLayout(null);
        panelInferior.setPreferredSize(new Dimension(1300, 75));
        panelInferior.setBackground(new Color(241, 245, 249));

        btnVolver = crearBoton("VOLVER", new Color(15, 23, 42));
        btnVolver.setBounds(690, 15, 220, 50); 
        btnVolver.setOpaque(true);           
        btnVolver.setBorderPainted(false);   
        panelInferior.add(btnVolver);

        btnVolver.addActionListener(e -> {
            new JornadaHubFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        add(panelInferior, BorderLayout.AFTER_LAST_LINE);
    }

    private JPanel crearCardJornada(Map<String, String> datosJornada) {
        int idJornada = Integer.parseInt(datosJornada.get("id_jornada"));
        String nombreJornada = datosJornada.get("nombre_jornada").toUpperCase();
        String estatus = datosJornada.get("estatus");

        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        
        card.setMinimumSize(new Dimension(1430, 120));
        card.setMaximumSize(new Dimension(1430, 120));
        card.setPreferredSize(new Dimension(1430, 120));

        JLabel lblNombre = new JLabel(nombreJornada);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblNombre.setForeground(new Color(15, 23, 42));
        lblNombre.setBounds(45, 40, 300, 40); 
        card.add(lblNombre);

        JLabel lblEstado = new JLabel();
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEstado.setBounds(385, 40, 250, 40); 
        card.add(lblEstado);

        JLabel lblDetalle = new JLabel("Fase regular de grupos");
        lblDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDetalle.setForeground(new Color(100, 116, 139));
        lblDetalle.setBounds(680, 40, 250, 40);
        card.add(lblDetalle);

        JButton btnAccion = new JButton();
        btnAccion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAccion.setForeground(Color.WHITE);
        btnAccion.setFocusPainted(false);
        btnAccion.setOpaque(true);           
        btnAccion.setBorderPainted(false);   
        btnAccion.setBounds(1160, 35, 220, 45); 
        card.add(btnAccion);

        switch (estatus) {
        
            case "DISPONIBLE":
                InscripcionesDAO inscripcionesDAO = new InscripcionesDAO(conexion);
                boolean yaInscrito = inscripcionesDAO.verificarInscripcionJornada(usuarioSesion.getIdUsuario(), idJornada);

                if (yaInscrito) {
                    lblEstado.setText("INSCRITO");
                    lblEstado.setForeground(new Color(37, 99, 235)); 

                    boolean tienePredicciones = verificarSiTienePredicciones(usuarioSesion.getIdUsuario(), idJornada);

                    if (!tienePredicciones) {
                        btnAccion.setText("REGISTRAR PREDICCIÓN");
                        btnAccion.setBackground(new Color(37, 99, 235)); 
                        btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        btnAccion.addActionListener(e -> {
                            new QuinielaFrame(conexion, usuarioSesion, idJornada).setVisible(true);
                            dispose();
                        });
                    } else {
                        btnAccion.setText("VER PARTIDOS");
                        btnAccion.setBackground(new Color(59, 130, 246)); 
                        btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        btnAccion.addActionListener(e -> {
                            new VerPartidosFrame(conexion, usuarioSesion, idJornada).setVisible(true);
                        });
                    }
                } else {
                    lblEstado.setText("DISPONIBLE");
                    lblEstado.setForeground(new Color(16, 185, 129)); 
                    
                    btnAccion.setText("PAGAR INSCRIPCIÓN");
                    btnAccion.setBackground(new Color(16, 185, 129));
                    btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    btnAccion.addActionListener(e -> { 
                        int opcion = JOptionPane.showConfirmDialog(
                            this, 
                            "¿Deseas pagar la inscripción para la Jornada " + idJornada + "?\nCosto: $50.00", 
                            "Confirmar Pago", 
                            JOptionPane.YES_NO_OPTION, 
                            JOptionPane.QUESTION_MESSAGE
                        );
                        
                        if (opcion == JOptionPane.YES_OPTION) {
                            try {
                                boolean exito = inscripcionesDAO.pagarInscripcionPorProcedimiento(
                                    usuarioSesion.getIdUsuario(), 
                                    idJornada, 
                                    1, 
                                    50.00
                                );
                                
                                if (exito) {
                                    usuarioSesion.setSaldo(usuarioSesion.getSaldo() - 50.00);
                                    
                                    JOptionPane.showMessageDialog(
                                        this, 
                                        "¡Inscripción generada con éxito!\nSe han descontado $50.00 de tu saldo.", 
                                        "Pago Exitoso", 
                                        JOptionPane.INFORMATION_MESSAGE
                                    );
                                    
                                    new QuinielaFrame(conexion, usuarioSesion, idJornada).setVisible(true);
                                    dispose();
                                }
                                
                            } catch (java.sql.SQLException ex) {
                                String mensajeError = ex.getMessage();
                                if (mensajeError.contains("ERROR:")) {
                                    int inicio = mensajeError.indexOf("ERROR:") + 6;
                                    int fin = mensajeError.indexOf("\n", inicio);
                                    mensajeError = (fin != -1) ? mensajeError.substring(inicio, fin).trim() : mensajeError.substring(inicio).trim();
                                }
                                JOptionPane.showMessageDialog(this, "Transacción Personalizada:\n" + mensajeError, "Validación de la Base de Datos", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    });
                }
                break;
                
            case "EN CURSO":
                lblEstado.setText("EN CURSO / EN JUEGO");
                lblEstado.setForeground(new Color(245, 158, 11)); 
                
                btnAccion.setText("VER PARTIDOS");
                btnAccion.setBackground(new Color(59, 130, 246)); 
                btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btnAccion.addActionListener(e -> {
                    new VerPartidosFrame(conexion, usuarioSesion, idJornada).setVisible(true);
                });
                break;

            case "FINALIZADA":
                lblEstado.setText("FINALIZADA");
                lblEstado.setForeground(new Color(239, 68, 68)); 
                
                btnAccion.setText("CERRADA");
                btnAccion.setBackground(new Color(100, 116, 139)); 
                btnAccion.setEnabled(false);
                break;

            default:
                lblEstado.setText("SIN CARTELERA");
                lblEstado.setForeground(new Color(148, 163, 184));
                btnAccion.setText("NO DISPONIBLE");
                btnAccion.setBackground(new Color(203, 213, 225));
                btnAccion.setEnabled(false);
                break;
        }

        return card;
    }

    private boolean verificarSiTienePredicciones(int idUsuario, int idJornada) {
        // CORRECCIÓN FINAL: Apuntando exactamente a "predicciones" con validación estricta de marcadores asentados
        String query = "SELECT COUNT(*) FROM predicciones p " +
                       "JOIN partido part ON p.id_partido = part.id_partido " +
                       "WHERE p.id_usuario = ? AND part.id_jornada = ? " +
                       "AND p.pred_goles_local IS NOT NULL AND p.pred_goles_vis IS NOT NULL;";
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idJornada);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}
