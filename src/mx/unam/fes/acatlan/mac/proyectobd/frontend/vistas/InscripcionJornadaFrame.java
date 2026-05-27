package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.JornadasDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.TorneosDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class InscripcionJornadaFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // Paneles Estructurales Contenedores
    private JPanel panelCabecera;
    private JPanel panelColumnas; // NUEVO: Barra de nombres de columnas
    private JPanel panelContenedorCards;
    private JPanel panelInferior;

    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JLabel lblUsuarioInfo; // NUEVO: Nombre y saldo en verde
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
        
        // Estructura BorderLayout indestructible para evitar encimamientos
        setLayout(new BorderLayout());

        iniciarComponentes(nombreTorneo);
    }

    private void iniciarComponentes(String nombreTorneo) {
        // ========================================================
        // 1. CABECERA SUPERIOR (NORTE) - Modificada para incluir Saldo
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

        // NUEVO: Renderizado del usuario y Saldo en verde esmeralda (Alineado a la derecha)
        String saldoFormateado = String.format("$%.2f", usuarioSesion.getSaldo());
        lblUsuarioInfo = new JLabel("<html><font color='#475569'>Usuario: </font><b>" + usuarioSesion.getUsername() + "</b>"
                + " &nbsp;&nbsp;|&nbsp;&nbsp; <font color='#475569'>Saldo disponible: </font><font color='#10B981'><b>" + saldoFormateado + "</b></font></html>");
        lblUsuarioInfo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblUsuarioInfo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUsuarioInfo.setBounds(750, 40, 700, 40);
        panelCabecera.add(lblUsuarioInfo);

        // ========================================================
        // NUEVO: PANEL DE NOMBRES DE COLUMNAS (Alineación perfecta con las cards)
        // ========================================================
        panelColumnas = new JPanel();
        panelColumnas.setLayout(null);
        panelColumnas.setPreferredSize(new Dimension(1600, 40));
        panelColumnas.setBackground(new Color(241, 245, 249));

        // Los Bounds (X) coinciden milimétricamente con los componentes de las tarjetas de abajo
        JLabel colJornada = new JLabel("JORNADA");
        colJornada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        colJornada.setForeground(new Color(148, 163, 184)); // Color Slate tenue de tabla
        colJornada.setBounds(105, 10, 200, 25); // 60 de margen + 45 interno de la card
        panelColumnas.add(colJornada);

        JLabel colEstatus = new JLabel("ESTATUS");
        colEstatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        colEstatus.setForeground(new Color(148, 163, 184));
        colEstatus.setBounds(445, 10, 200, 25); // Mismo X donde inicia lblEstado
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
        colAccion.setBounds(1220, 10, 220, 25); // Mismo X y Ancho que el botón de acción
        panelColumnas.add(colAccion);

        // Agrupamos la cabecera y las columnas en un subpanel Norte para mantener el BorderLayout limpio
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

        // Consulta analítica de estatus a la BD
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
                panelContenedorCards.add(Box.createRigidArea(new Dimension(0, 20))); // Separación simétrica
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
        // CORRECCIÓN: Se redujo la altura del panel a 85 (antes 120) para compactar el espacio inferior
        panelInferior.setPreferredSize(new Dimension(1300, 75));
        panelInferior.setBackground(new Color(241, 245, 249));

        btnVolver = crearBoton("VOLVER", new Color(15, 23, 42));
        // CORRECCIÓN: Se subió el botón cambiando Y=15 (antes 35) para pegarlo más al scrollbar
        btnVolver.setBounds(690, 15, 220, 50); 
        panelInferior.add(btnVolver);

        btnVolver.addActionListener(e -> {
            new JornadaHubFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        add(panelInferior, BorderLayout.CENTER);
    }

    // ========================================================
    // CONSTRUCCIÓN DINÁMICA DE LA CARD SEGÚN EL ESTATUS DE LA BD
    // ========================================================
    private JPanel crearCardJornada(Map<String, String> datosJornada) {
        int idJornada = Integer.parseInt(datosJornada.get("id_jornada"));
        String nombreJornada = datosJornada.get("nombre_jornada").toUpperCase();
        String estatus = datosJornada.get("estatus");

        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        
        // Dimensiones estrictas para el BoxLayout vertical
        card.setMinimumSize(new Dimension(1430, 120));
        card.setMaximumSize(new Dimension(1430, 120));
        card.setPreferredSize(new Dimension(1430, 120));

        // Nombre de la Jornada
        JLabel lblNombre = new JLabel(nombreJornada);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblNombre.setForeground(new Color(15, 23, 42));
        lblNombre.setBounds(45, 40, 300, 40); // Ajustado a X=45 para alineación con título
        card.add(lblNombre);

        // Etiquetas de estado
        JLabel lblEstado = new JLabel();
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEstado.setBounds(385, 40, 250, 40); // Ajustado a X=385
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
        btnAccion.setBorderPainted(false);
        btnAccion.setBounds(1160, 35, 220, 45); // X=1160 + 60 del panelContenedor = 1220 (Alineación con cabecera)
        card.add(btnAccion);

        // APLICACIÓN DE REGLAS DE NEGOCIO EN BASE AL ESTATUS CALCULADO
        switch (estatus) {
            case "DISPONIBLE":
                lblEstado.setText("DISPONIBLE / ABIERTA");
                lblEstado.setForeground(new Color(16, 185, 129)); // Esmeralda
                
                btnAccion.setText("INSCRIBIRSE");
                btnAccion.setBackground(new Color(16, 185, 129));
                btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btnAccion.addActionListener(e -> {
                    new QuinielaFrame(conexion, usuarioSesion, idJornada).setVisible(true);
                    dispose();
                });
                break;

            case "EN CURSO":
                lblEstado.setText("EN CURSO / EN JUEGO");
                lblEstado.setForeground(new Color(245, 158, 11)); // Ámbar/Naranja
                
                btnAccion.setText("VER PARTIDOS");
                btnAccion.setBackground(new Color(59, 130, 246)); // Azul
                btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btnAccion.addActionListener(e -> {
                    JOptionPane.showMessageDialog(this, "Abriendo panel de visualización de partidos en curso para la jornada ID: " + idJornada);
                    // Ejemplo: new PartidosJornadaEstatusFrame(conexion, usuarioSesion, idJornada).setVisible(true);
                    // dispose();
                });
                break;

            case "FINALIZADA":
                lblEstado.setText("FINALIZADA");
                lblEstado.setForeground(new Color(239, 68, 68)); // Rojo
                
                btnAccion.setText("CERRADA");
                btnAccion.setBackground(new Color(100, 116, 139)); // Gris Slate neutro
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