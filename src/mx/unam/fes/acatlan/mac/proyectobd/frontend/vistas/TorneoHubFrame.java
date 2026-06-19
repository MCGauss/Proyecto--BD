package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import java.util.Map;
import javax.swing.*;

import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.TorneosDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class TorneoHubFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JPanel panelPrincipal;
    JLabel lblTitulo;
    JPanel cardPanel;
    JButton btnEntrar;
    JButton btnPagar;
    JButton btnVolver;

    // ATRIBUTOS DE CONEXIÓN Y SESIÓN ACTIVA
    private Connection conexion;
    private Usuarios usuarioSesion;
    private int idTorneoActivo = -1;

    // CONSTRUCTOR MODIFICADO: Recibe la conexión y la sesión del usuario
    public TorneoHubFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        setTitle("Torneo Completo");
        setSize(1000, 750); // Ajustado al estándar de tus ventanas anteriores
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        configurarEventosBD();
    }

    private void iniciarComponentes() {
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(null);
        panelPrincipal.setBackground(new Color(241, 245, 249));

        lblTitulo = new JLabel("TORNEO COMPLETO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBounds(60, 40, 500, 45);
        panelPrincipal.add(lblTitulo);

        // ========================================================
        // CONSULTA DINÁMICA A LA BASE DE DATOS MEDIANTE EL DAO
        // ========================================================
        TorneosDAO torneosDAO = new TorneosDAO(conexion);
        Map<String, String> datosTorneo = torneosDAO.obtenerInformacionTorneoHub();

        // Valores por defecto si la base de datos se encuentra vacía
        String nombreTorneo = "SIN TORNEO ACTIVO";
        String stringJornadaInfo = "Jornada actual: 0 de 0";

        if (datosTorneo != null && !datosTorneo.isEmpty()) {
            nombreTorneo = datosTorneo.get("nombre_torneo");
            idTorneoActivo = Integer.parseInt(datosTorneo.get("id_torneo"));
            stringJornadaInfo = "Jornada actual: " + datosTorneo.get("jornada_actual") + " de " + datosTorneo.get("total_jornadas");
        }

        // ========================================================
        // PANEL CARD (MANTENIENDO TU DISEÑO E INYECTANDO LA BD)
        // ========================================================
        cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
        cardPanel.setBounds(60, 120, 860, 360);

        // TITULO CARD (DINÁMICO)
        JLabel lblTorneo = new JLabel(nombreTorneo);
        lblTorneo.setFont(new Font("Segoe UI", Font.BOLD, 36)); // Ligeramente ajustado para nombres largos
        lblTorneo.setForeground(new Color(16, 185, 129));
        lblTorneo.setBounds(30, 40, 800, 50);
        cardPanel.add(lblTorneo);

        // JORNADA ACTUAL (DINÁMICO)
        JLabel lblJornada = new JLabel(stringJornadaInfo);
        lblJornada.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblJornada.setForeground(new Color(15, 23, 42));
        lblJornada.setBounds(60, 110, 800, 40);
        cardPanel.add(lblJornada);

        // DESCRIPCIÓN FIJA
        JTextArea descripcion = new JTextArea(
                "Pronostica todos los partidos y acumula puntos en la tabla general.\n" +
                "¡El jugador con más puntos al final se lleva la bolsa acumulada!\n" +
                "Costo de entrada: $500.00"
        );
        descripcion.setEditable(false);
        descripcion.setOpaque(false);
        descripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descripcion.setForeground(new Color(71, 85, 105));
     // Forzamos el centrado de líneas en el JTextArea
        descripcion.setAlignmentX(Component.CENTER_ALIGNMENT); 
        // Un truco manual efectivo para centrar texto multilinea plano en Swing:
        descripcion.setBounds(140, 180, 580, 80); // Reducimos el ancho a 580 y lo movemos a X=140
        cardPanel.add(descripcion);

     // BOTONES INTERNOS DE LA CARD CENTRADOS EN CONJUNTO
        btnEntrar = crearBoton("PRONOSTICAR", new Color(16, 185, 129));
        btnEntrar.setBounds(210, 280, 200, 45); // Se posiciona en la mitad izquierda del bloque central
        btnEntrar.setOpaque(true);           // <- Obliga a pintar el fondo en Mac
        btnEntrar.setBorderPainted(false);   // <- Quita el borde Aqua nativo de Mac
        cardPanel.add(btnEntrar);

        btnPagar = crearBoton("PAGAR INSCRIPCIÓN", new Color(15, 23, 42));
        btnPagar.setBounds(450, 280, 200, 45); // Se posiciona en la mitad derecha del bloque central
        btnPagar.setOpaque(true);           // <- Obliga a pintar el fondo en Mac
        btnPagar.setBorderPainted(false);   // <- Quita el borde Aqua nativo de Mac
        cardPanel.add(btnPagar);

        panelPrincipal.add(cardPanel);

        // BOTÓN VOLVER GENERAL
        btnVolver = crearBoton("VOLVER", new Color(71, 85, 105));
        btnVolver.setBounds(380, 530, 220, 50);
        btnVolver.setOpaque(true);           // <- Obliga a pintar el fondo en Mac
        btnVolver.setBorderPainted(false);   // <- Quita el borde Aqua nativo de Mac
        panelPrincipal.add(btnVolver);

        // Si no se encontró ningún torneo, deshabilitamos las acciones de juego
        if (idTorneoActivo == -1) {
            btnEntrar.setEnabled(false);
            btnPagar.setEnabled(false);
        }

        add(panelPrincipal);
    }

    private void configurarEventosBD() {
        btnEntrar.addActionListener(e -> {
        	new TorneoPrediccionesFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        btnPagar.addActionListener(e -> { // Llamada al metodo pagarInscripcionPorProcedimiento
            int opcion = JOptionPane.showConfirmDialog(
                this, 
                "¿Deseas pagar la inscripción para el Torneo? \n" + "\nCosto: $500.00", 
                "Confirmar Pago", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (opcion == JOptionPane.YES_OPTION) {
                try {
                    mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.InscripcionesDAO inscripcionesDAO = 
                        new mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.InscripcionesDAO(conexion);
                    
                    // LLAMADA AL PROCEDIMIENTO: idUsuario, idJornada = null (porque es torneo), tipoInscripcion = 2, monto = 500.00
                    boolean exito = inscripcionesDAO.pagarInscripcionPorProcedimiento(
                        usuarioSesion.getIdUsuario(), 
                        null, 
                        2, 
                        500.00
                    );
                    
                    if (exito) {
                        // Sincronizar el estado del objeto en memoria local de la sesión de Java
                        usuarioSesion.setSaldo(usuarioSesion.getSaldo() - 500.00);
                        
                        JOptionPane.showMessageDialog(
                            this, 
                            "¡Inscripción al Torneo generada con éxito!\nSe han descontado $500.00 de tu saldo.", 
                            "Pago Exitoso", 
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        
                        // Ventana al flujo de Torneo Completo
                         new TorneoPrediccionesFrame(conexion, usuarioSesion).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo procesar la inscripción.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    
                } catch (java.sql.SQLException ex) {
                    String mensajeError = ex.getMessage();
                    if (mensajeError.contains("ERROR:")) {
                        int inicio = mensajeError.indexOf("ERROR:") + 6;
                        int fin = mensajeError.indexOf("\n", inicio);
                        mensajeError = (fin != -1) ? mensajeError.substring(inicio, fin).trim() : mensajeError.substring(inicio).trim();
                    }
                    JOptionPane.showMessageDialog(this, "Transacción Rechazada:\n" + mensajeError, "Validación de la BD", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        btnVolver.addActionListener(e -> {
        	new SeleccionQuinielaFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}