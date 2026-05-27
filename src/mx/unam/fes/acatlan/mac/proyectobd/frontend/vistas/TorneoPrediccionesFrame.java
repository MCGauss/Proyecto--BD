package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.JornadasDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.TorneosDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Jornadas;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class TorneoPrediccionesFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // Paneles Estructurales Modernos (Redimensionables)
    JPanel panelCabecera;
    JPanel panelContenedorJornadas;
    JPanel panelInferior;
    
    JLabel lblTitulo;
    JLabel lblSubtitulo;
    JScrollPane scrollPane;
    JButton btnVolver;

    // Atributos de Persistencia e Inyección
    private Connection conexion;
    private Usuarios usuarioSesion;
    private int idTorneoActivo = -1;
    private int idJornadaActual = -1;

    public TorneoPrediccionesFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        // Carga de metadatos analíticos de la Base de Datos
        TorneosDAO torneosDAO = new TorneosDAO(conexion);
        Map<String, String> infoTorneo = torneosDAO.obtenerInformacionTorneoHub();
        
        JornadasDAO jornadasDAO = new JornadasDAO(conexion);
        Map<String, String> infoJornada = jornadasDAO.obtenerJornadaActual();

        String nombreTorneo = "Sin Torneo Activo";
        if (infoTorneo != null && !infoTorneo.isEmpty()) {
            this.idTorneoActivo = Integer.parseInt(infoTorneo.get("id_torneo"));
            nombreTorneo = infoTorneo.get("nombre_torneo");
        }
        if (infoJornada != null && !infoJornada.isEmpty()) {
            this.idJornadaActual = Integer.parseInt(infoJornada.get("id_jornada"));
        }

        setTitle("Predicciones del Torneo");
        setSize(1600, 950);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        // Usamos un BorderLayout global para garantizar que las secciones nunca colisionen
        setLayout(new BorderLayout());

        iniciarComponentes(nombreTorneo);

        setVisible(true);
    }

    private void iniciarComponentes(String nombreTorneo) {
        // ========================================================
        // 1. PANEL CABECERA (NORTE) - Fijo a 150px de alto
        // ========================================================
        panelCabecera = new JPanel();
        panelCabecera.setLayout(null);
        panelCabecera.setPreferredSize(new Dimension(1600, 150));
        panelCabecera.setBackground(new Color(241, 245, 249));

        lblTitulo = new JLabel("PRONÓSTICOS DEL TORNEO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBounds(60, 30, 800, 55);
        panelCabecera.add(lblTitulo);

        lblSubtitulo = new JLabel("Torneo Activo: " + nombreTorneo + "  |  Usuario: " + usuarioSesion.getUsername());
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblSubtitulo.setForeground(new Color(71, 85, 105));
        lblSubtitulo.setBounds(60, 95, 1000, 30);
        panelCabecera.add(lblSubtitulo);

        add(panelCabecera, BorderLayout.NORTH);

        // ========================================================
        // 2. PANEL CONTENEDOR DE FILAS (CENTRO CON SCROLL)
        // ========================================================
        panelContenedorJornadas = new JPanel();
        panelContenedorJornadas.setLayout(new BoxLayout(panelContenedorJornadas, BoxLayout.Y_AXIS));
        panelContenedorJornadas.setBackground(new Color(241, 245, 249));
        // EmptyBorder añade márgenes simulando tus posiciones X originales de las tarjetas
        panelContenedorJornadas.setBorder(new EmptyBorder(10, 60, 10, 60));

        JornadasDAO jornadasDAO = new JornadasDAO(conexion);
        List<Jornadas> listaJornadas = jornadasDAO.obtenerJornadasPorTorneo(idTorneoActivo);

        if (listaJornadas.isEmpty()) {
            JLabel lblVacio = new JLabel("No se encontraron jornadas registradas para este torneo.", SwingConstants.CENTER);
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            lblVacio.setForeground(new Color(100, 116, 139));
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelContenedorJornadas.add(lblVacio);
        } else {
            for (Jornadas j : listaJornadas) {
                panelContenedorJornadas.add(crearCardJornada(j));
                panelContenedorJornadas.add(Box.createRigidArea(new Dimension(0, 20))); // Separación controlada
            }
        }

        scrollPane = new JScrollPane(panelContenedorJornadas);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20); // Scroll súper fluido
        add(scrollPane, BorderLayout.CENTER);

        // ========================================================
        // 3. PANEL INFERIOR FIJO (SUR) - Aloja el Botón Volver de forma segura
        // ========================================================
        panelInferior = new JPanel();
        panelInferior.setLayout(null);
        panelInferior.setPreferredSize(new Dimension(1600, 120));
        panelInferior.setBackground(new Color(241, 245, 249));

        btnVolver = crearBoton("VOLVER AL MENÚ", new Color(15, 23, 42));
        // Centrado matemático perfecto dentro del ancho estándar del frame (1600)
        btnVolver.setBounds(690, 35, 220, 50); 
        btnVolver.setOpaque(true);           // <- Obliga a pintar el fondo en Mac
        btnVolver.setBorderPainted(false);   // <- Quita el borde Aqua nativo de Mac
        panelInferior.add(btnVolver);

        btnVolver.addActionListener(e -> {
            new TorneoHubFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        add(panelInferior, BorderLayout.SOUTH);
    }

    // ========================================================
    // CONSTRUCCIÓN ESTRUCTURAL DE LAS CARDS DE JORNADA
    // ========================================================
    private JPanel crearCardJornada(Jornadas jornadaObjeto) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        
        // Bloqueo estricto de dimensiones para el BoxLayout del Centro
        card.setMinimumSize(new Dimension(1430, 120));
        card.setMaximumSize(new Dimension(1430, 120));
        card.setPreferredSize(new Dimension(1430, 120));

        JLabel lblJornada = new JLabel(jornadaObjeto.getNombreJornada().toUpperCase());
        lblJornada.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblJornada.setForeground(new Color(15, 23, 42));
        lblJornada.setBounds(40, 40, 300, 40);

        boolean esConsulta = (jornadaObjeto.getIdJornada() <= idJornadaActual);

        JLabel lblEstado = new JLabel(esConsulta ? "EN CURSO / CONCLUIDA" : "INSCRIPCIONES ABIERTAS");
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEstado.setForeground(esConsulta ? new Color(239, 68, 68) : new Color(16, 185, 129));
        lblEstado.setBounds(380, 40, 250, 40);

        JLabel lblPartidos = new JLabel("Fase regular de liga");
        lblPartidos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPartidos.setForeground(new Color(100, 116, 139));
        lblPartidos.setBounds(680, 40, 250, 40);

        JButton btnEntrar = crearBoton(
                esConsulta ? "CONSULTAR" : "CAPTURAR",
                esConsulta ? new Color(100, 116, 139) : new Color(59, 130, 246)
        );
        btnEntrar.setBounds(1160, 35, 220, 45);
        btnEntrar.setOpaque(true);
        btnEntrar.setBorderPainted(false);

        btnEntrar.addActionListener(e -> {
            QuinielaFrame frame = new QuinielaFrame(conexion, usuarioSesion, jornadaObjeto.getIdJornada());
            if (esConsulta) {
                frame.editable = false;
            }
            frame.setVisible(true);
            dispose();
        });

        card.add(lblJornada);
        card.add(lblEstado);
        card.add(lblPartidos);
        card.add(btnEntrar);

        return card;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}
