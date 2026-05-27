package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import java.util.Map;
import javax.swing.*;

import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.JornadasDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class JornadaHubFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    JPanel panelPrincipal;
    JLabel lblTitulo;

    JPanel cardActual;
    JPanel cardProxima;

    JButton btnVerActual;
    JButton btnInscribirse;
    JButton btnVolver;

    // ATRIBUTOS DE PERSISTENCIA INYECTADOS
    private Connection conexion;
    private Usuarios usuarioSesion;
    
    // Identificadores para control de navegación dinámica
    private int idJornadaActual = -1;
    private int idJornadaProxima = -1;

    public JornadaHubFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        setTitle("Quiniela por Jornada");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
    }

    private void iniciarComponentes() {
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(null);
        panelPrincipal.setBackground(new Color(241, 245, 249));

        lblTitulo = new JLabel("QUINIELA POR JORNADA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBounds(60, 40, 500, 45);
        panelPrincipal.add(lblTitulo);

        // ========================================================
        // CONSULTA DINÁMICA A LA BASE DE DATOS MEDIANTE EL DAO
        // ========================================================
        JornadasDAO jornadasDAO = new JornadasDAO(conexion);
        Map<String, String> datosActual = jornadasDAO.obtenerJornadaActual();
        Map<String, String> datosProxima = jornadasDAO.obtenerProximaJornada();

     // Variables temporales para el análisis lógico
        String tempActual = "No Disponible";
        String tempProxima = "No Disponible";

        if (datosActual != null && !datosActual.isEmpty()) {
            tempActual = datosActual.get("nombre");
            idJornadaActual = Integer.parseInt(datosActual.get("id_jornada"));
        }
        if (datosProxima != null && !datosProxima.isEmpty()) {
            tempProxima = datosProxima.get("nombre");
            idJornadaProxima = Integer.parseInt(datosProxima.get("id_jornada"));
        }

        // SOLUCCIÓN AL ERROR: Creamos copias "final" que nunca cambiarán de valor
        // para que las expresiones Lambda de los botones las lean sin restricciones.
        final String textoActualFinal = tempActual;
        final String textoProximaFinal = tempProxima;

        // ========================================================
        // CONSTRUCCIÓN DE LAS CARDS CON LOS DATOS REALES DE LA BD
        // ========================================================
        cardActual = crearCard(
                "JORNADA ACTUAL",
                textoActualFinal, // Usamos la variable final segura
                "La jornada ya inició.\nSolo disponible para consulta de resultados.",
                new Color(239, 68, 68)
        );
        cardActual.setBounds(60, 120, 410, 340);
        panelPrincipal.add(cardActual);

        cardProxima = crearCard(
                "PRÓXIMA JORNADA",
                textoProximaFinal, // Usamos la variable final segura
                "Inscripciones abiertas.\nCosto de entrada: $100.00 MXN",
                new Color(16, 185, 129)
        );
        cardProxima.setBounds(510, 120, 410, 340);
        panelPrincipal.add(cardProxima);

        // ========================================================
        // BOTONES DE ACCIÓN (MANTENIENDO TUS COORDENADAS)
        // ========================================================
        btnVerActual = crearBoton("CONSULTAR", new Color(15, 23, 42));
        btnVerActual.setBounds(155, 490, 220, 50);
        panelPrincipal.add(btnVerActual);

        btnInscribirse = crearBoton("INSCRIBIRSE", new Color(16, 185, 129));
        btnInscribirse.setBounds(605, 490, 220, 50);
        panelPrincipal.add(btnInscribirse);

        btnVolver = crearBoton("VOLVER", new Color(71, 85, 105));
        btnVolver.setBounds(360, 620, 220, 50);
        panelPrincipal.add(btnVolver);

        // Si no se encontró jornada activa o próxima, deshabilitamos la acción del botón correspondiente
        if (idJornadaActual == -1) btnVerActual.setEnabled(false);
        if (idJornadaProxima == -1) btnInscribirse.setEnabled(false);

        add(panelPrincipal);

        // ========================================================
        // DISPARADORES DE EVENTOS CON PROPAGACIÓN DE DATOS
        // ========================================================
        btnVerActual.addActionListener(e -> {
            // Se puede abrir QuinielaFrame pasando el ID obtenido en modo lectura
            JOptionPane.showMessageDialog(this, "Abriendo consulta de la " + textoActualFinal);
        });

        btnInscribirse.addActionListener(e -> {
            // Abre tu frame de inscripción heredando la persistencia y la sesión
            new InscripcionJornadaFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        btnVolver.addActionListener(e -> {
            new SeleccionQuinielaFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });
    }

    private JPanel crearCard(String titulo, String subtitulo, String descripcion, Color colorBorde) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(colorBorde, 2));

        JLabel lblTituloCard = new JLabel(titulo);
        lblTituloCard.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloCard.setForeground(colorBorde);
        lblTituloCard.setBounds(30, 25, 350, 20);
        panel.add(lblTituloCard);

        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSubtitulo.setForeground(new Color(15, 23, 42));
        lblSubtitulo.setBounds(30, 65, 350, 35);
        panel.add(lblSubtitulo);

        JTextArea txtDescripcion = new JTextArea(descripcion);
        txtDescripcion.setEditable(false);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDescripcion.setForeground(new Color(71, 85, 105));
        txtDescripcion.setBounds(30, 120, 350, 150);
        panel.add(txtDescripcion);

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}