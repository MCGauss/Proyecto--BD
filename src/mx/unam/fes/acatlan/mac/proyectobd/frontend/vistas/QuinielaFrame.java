package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.BolsaPremiosDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.PartidosDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.PrediccionesDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Partido;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Predicciones;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class QuinielaFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // Paneles estructurales
    private JPanel panelPrincipal;
    private JPanel panelPartidos;
    private JScrollPane scrollPane;

    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JLabel lblPremio;

    private JButton btnGuardar;
    private JButton btnVolver;

    // Control de Persistencia y Sesión Global
    private Connection conexion;
    private Usuarios usuarioSesion;
    private int idJornada; 
    public boolean editable = true;

    // DAOs de la Arquitectura
    private PartidosDAO partidosDAO;
    private BolsaPremiosDAO bolsaPremiosDAO;
    private PrediccionesDAO prediccionesDAO;

    // Estructuras de rastreo interno para recolectar marcadores al guardar
    private List<Partido> partidosCartelera;
    private List<JSpinner> spinnersGolesLocal;
    private List<JSpinner> spinnersGolesVis;

    public QuinielaFrame(Connection conexion, Usuarios usuarioSesion, int idJornada) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;
        this.idJornada = idJornada;

        // Inicialización de DAOs
        this.partidosDAO = new PartidosDAO(conexion);
        this.bolsaPremiosDAO = new BolsaPremiosDAO(conexion);
        this.prediccionesDAO = new PrediccionesDAO(conexion);

        this.spinnersGolesLocal = new ArrayList<>();
        this.spinnersGolesVis = new ArrayList<>();

        setTitle("Captura de Pronósticos - Quiniela FES Acatlán");
        setSize(1550, 900); // Dimensiones ajustadas para evitar desbordamientos
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);

        iniciarComponentes();
    }

    private void iniciarComponentes() {
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(null);
        panelPrincipal.setBackground(new Color(241, 245, 249)); // Fondo Slate claro

        // ========================================================
        // 1. OBTENCIÓN DE INFORMACIÓN DINÁMICA DE LA BD
        // ========================================================
        double montoBolsa = bolsaPremiosDAO.obtenerMontoAcumulado(idJornada);
        String bolsaFormateada = String.format("$%.2f MXN", montoBolsa);

        lblTitulo = new JLabel("PRONÓSTICOS DE LA JORNADA " + idJornada);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblTitulo.setBounds(60, 25, 700, 50);
        panelPrincipal.add(lblTitulo);

        lblSubtitulo = new JLabel("Ingresa tus predicciones para cada partido. Revisa los horarios antes de guardar.");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(new Color(71, 85, 105));
        lblSubtitulo.setBounds(60, 80, 700, 25);
        panelPrincipal.add(lblSubtitulo);

        lblPremio = new JLabel("<html><div style='text-align: right;'><font color='#475569' size='4'>Bolsa Acumulada</font><br><font color='#10B981' size='6'><b>" + bolsaFormateada + "</b></font></div></html>");
        lblPremio.setBounds(1100, 15, 380, 90);
        panelPrincipal.add(lblPremio);

        // ========================================================
        // 2. CONTENEDOR ELÁSTICO DE PARTIDOS CON GRIDBAGLAYOUT
        // ========================================================
        panelPartidos = new JPanel();
        panelPartidos.setLayout(new GridBagLayout()); // Permite crecimiento dinámico real para el Scroll
        panelPartidos.setBackground(new Color(241, 245, 249));
        
        // Carga de partidos programados desde el DAO
        partidosCartelera = partidosDAO.obtenerPartidosPorJornada(idJornada);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0); // Espaciado inferior simétrico entre tarjetas
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        if (partidosCartelera == null || partidosCartelera.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay partidos programados o pendientes por jugar en esta jornada.", SwingConstants.CENTER);
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            lblVacio.setForeground(new Color(100, 116, 139));
            panelPartidos.add(lblVacio, gbc);
        } else {
            for (Partido partido : partidosCartelera) {
                panelPartidos.add(crearCardPartido(partido), gbc);
                gbc.gridy++;
            }
            // Agrega un empujador vertical invisible al final para alinear todo perfectamente arriba
            gbc.weighty = 1.0;
            panelPartidos.add(Box.createGlue(), gbc);
        }

        scrollPane = new JScrollPane(panelPartidos);
        scrollPane.setBounds(60, 130, 1420, 600); // Espacio exacto optimizado
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        panelPrincipal.add(scrollPane);

        // ========================================================
        // 3. BARRA DE ACCIONES INFERIORES (SIEMPRE VISIBLES)
        // ========================================================
        btnVolver = crearBoton("VOLVER", new Color(100, 116, 139));
        btnVolver.setBounds(60, 760, 250, 50);
        panelPrincipal.add(btnVolver);

        btnGuardar = crearBoton("GUARDAR PREDICCIÓN", new Color(16, 185, 129)); 
        btnGuardar.setBounds(1230, 760, 250, 50);
        btnGuardar.setEnabled(editable);
        panelPrincipal.add(btnGuardar);

        // Eventos de Navegación
        btnVolver.addActionListener(e -> {
            new InscripcionJornadaFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        btnGuardar.addActionListener(e -> ejecutarGuardadoPredicciones());

        add(panelPrincipal);
    }

 // 1. Modificación dentro de crearCardPartido para asegurar la lectura de nombres
    private JPanel crearCardPartido(Partido partido) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        card.setPreferredSize(new Dimension(1380, 120));
        card.setMinimumSize(new Dimension(1380, 120));

        // Obtención de datos reales validados desde el DAO modificado
        String nombreLocal = (partido.getEquipoLocal() != null) ? partido.getEquipoLocal().getNombreEquipo() : "Equipo Local";
        String logoLocal = (partido.getEquipoLocal() != null) ? partido.getEquipoLocal().getLogoURL() : "";
        
        // Generar icono (con iniciales de respaldo si es .eps o null)
        ImageIcon iconoLocal = obtenerIconoOIniciales(logoLocal, nombreLocal, 65, 65);

        JLabel lblLogoLocal = new JLabel(iconoLocal);
        lblLogoLocal.setBounds(40, 27, 65, 65);
        card.add(lblLogoLocal);

        JLabel lblNombreLocal = new JLabel(nombreLocal);
        lblNombreLocal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombreLocal.setForeground(new Color(15, 23, 42));
        lblNombreLocal.setBounds(125, 40, 320, 35);
        card.add(lblNombreLocal);

        // [Aquí se mantienen exactamente iguales tus componentes intermedios: Spinners y el "VS"]
        SpinnerModel modelLocal = new SpinnerNumberModel(0, 0, 99, 1);
        JSpinner spinGolesLocal = new JSpinner(modelLocal);
        spinGolesLocal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        spinGolesLocal.setBounds(470, 37, 80, 45);
        spinGolesLocal.setEnabled(editable);
        JComponent editorL = spinGolesLocal.getEditor();
        if (editorL instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editorL).getTextField().setHorizontalAlignment(JTextField.CENTER);
        }
        card.add(spinGolesLocal);
        spinnersGolesLocal.add(spinGolesLocal);

        JLabel lblVs = new JLabel("VS", SwingConstants.CENTER);
        lblVs.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblVs.setForeground(new Color(148, 163, 184));
        lblVs.setBounds(650, 40, 80, 35);
        card.add(lblVs);

        SpinnerModel modelVis = new SpinnerNumberModel(0, 0, 99, 1);
        JSpinner spinGolesVis = new JSpinner(modelVis);
        spinGolesVis.setFont(new Font("Segoe UI", Font.BOLD, 22));
        spinGolesVis.setBounds(830, 37, 80, 45);
        spinGolesVis.setEnabled(editable);
        JComponent editorV = spinGolesVis.getEditor();
        if (editorV instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editorV).getTextField().setHorizontalAlignment(JTextField.CENTER);
        }
        card.add(spinGolesVis);
        spinnersGolesVis.add(spinGolesVis);
        // [Fin del bloque de spinners]

        // Datos del Equipo Visitante reales del DAO
        String nombreVis = (partido.getEquipoVisitante() != null) ? partido.getEquipoVisitante().getNombreEquipo() : "Equipo Visitante";
        String logoVis = (partido.getEquipoVisitante() != null) ? partido.getEquipoVisitante().getLogoURL() : "";
        
        ImageIcon iconoVis = obtenerIconoOIniciales(logoVis, nombreVis, 65, 65);

        JLabel lblNombreVis = new JLabel(nombreVis, SwingConstants.RIGHT);
        lblNombreVis.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombreVis.setForeground(new Color(15, 23, 42));
        lblNombreVis.setBounds(940, 40, 320, 35);
        card.add(lblNombreVis);

        JLabel lblLogoVis = new JLabel(iconoVis);
        lblLogoVis.setBounds(1280, 27, 65, 65);
        card.add(lblLogoVis);

        return card;
    }

    // 2. Nuevo método inteligente de renderizado que procesa fallbacks y crea iniciales elegantes
    private ImageIcon obtenerIconoOIniciales(String nombreArchivo, String nombreEquipo, int ancho, int alto) {
        try {
            // Intentar cargar si no es un vector .eps y la ruta existe
            if (nombreArchivo != null && !nombreArchivo.trim().isEmpty() && !nombreArchivo.toLowerCase().endsWith(".eps")) {
                String rutaCompleta = "/Assets/" + nombreArchivo.trim();
                URL urlRecurso = getClass().getResource(rutaCompleta);
                if (urlRecurso != null) {
                    ImageIcon iconoOriginal = new ImageIcon(urlRecurso);
                    Image imgEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                    return new ImageIcon(imgEscalada);
                }
            }
        } catch (Exception ex) {
            // Fallback silencioso
        }

        /*
         *  UTILERÍA VISUAL: Generar un escudo circular plano con las iniciales si el archivo es .eps o no existe
         */
        BufferedImage fallback = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = fallback.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Color de fondo tipo Slate/Indigo universitario muy estético
        g2.setColor(new Color(30, 41, 59));
        g2.fillOval(0, 0, ancho, alto);
        
        // Obtener las iniciales del nombre del equipo (Ej: "Pumas UNAM" -> "PU", "América" -> "AM")
        String iniciales = "";
        if (nombreEquipo != null && !nombreEquipo.trim().isEmpty()) {
            String[] partes = nombreEquipo.trim().split(" ");
            if (partes.length > 0 && partes[0].length() > 0) iniciales += partes[0].charAt(0);
            if (partes.length > 1 && partes[1].length() > 0) {
                iniciales += partes[1].charAt(0);
            } else if (partes[0].length() > 1) {
                iniciales += partes[0].charAt(1); // Segunda letra de la palabra si es una sola
            }
        } else {
            iniciales = "EQ";
        }
        iniciales = iniciales.toUpperCase();

        // Dibujar texto centrado en el círculo
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int x = (ancho - fm.stringWidth(iniciales)) / 2;
        int y = ((alto - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(iniciales, x, y);
        
        g2.dispose();
        return new ImageIcon(fallback);
    }

    private void ejecutarGuardadoPredicciones() {
        if (partidosCartelera == null || partidosCartelera.isEmpty()) return;

        double costoQuiniela = 50.00; 
        if (usuarioSesion.getSaldo() < costoQuiniela) {
            JOptionPane.showMessageDialog(this, 
                "<html><b>Transacción Rechazada:</b><br>No cuentas con saldo suficiente.<br>Costo: $50.00 | Tu Saldo: $" + String.format("%.2f", usuarioSesion.getSaldo()) + "</html>",
                "Fondos Insuficientes", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Confirmas que deseas registrar tus pronósticos?", "Confirmar Pronósticos", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) return;

        try {
            conexion.setAutoCommit(false);

            for (int i = 0; i < partidosCartelera.size(); i++) {
                Partido partido = partidosCartelera.get(i);
                int golesLocal = (int) spinnersGolesLocal.get(i).getValue();
                int golesVis = (int) spinnersGolesVis.get(i).getValue();

                Predicciones pred = new Predicciones();
                pred.setUsuario(usuarioSesion);
                pred.setPartido(partido);
                pred.setPredGolesLocal(golesLocal);
                pred.setPredGolesVis(golesVis);
                pred.setPuntosObtenidos(null);

                prediccionesDAO.guardarActualizarPred(pred);
            }

            conexion.commit();
            JOptionPane.showMessageDialog(this, "¡Tus pronósticos han sido registrados de forma exitosa!", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            new InscripcionJornadaFrame(conexion, usuarioSesion).setVisible(true);
            this.dispose();

        } catch (SQLException ex) {
            try { conexion.rollback(); } catch (SQLException rEx) { rEx.printStackTrace(); }
            JOptionPane.showMessageDialog(this, "<html><b>Error (PostgreSQL):</b><br>" + ex.getMessage() + "</html>", "Violación de Regla de Negocio", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { conexion.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private ImageIcon obtenerIconoRedondeado(String nombreArchivo, int anchoDeseado, int altoDeseado) {
        try {
            if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                String rutaCompleta = "/Assets/" + nombreArchivo.trim();
                URL urlRecurso = getClass().getResource(rutaCompleta);
                
                if (urlRecurso != null) {
                    ImageIcon iconoOriginal = new ImageIcon(urlRecurso);
                    Image imgEscalada = iconoOriginal.getImage().getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);
                    return new ImageIcon(imgEscalada);
                }
            }
        } catch (Exception ex) {
            // Failsafe silencioso
        }

        // Círculo plano de Fallback estético (Gris elegante) por si no encuentra la imagen física
        BufferedImage fallback = new BufferedImage(anchoDeseado, altoDeseado, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = fallback.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(203, 213, 225));
        g2.fillOval(0, 0, anchoDeseado, altoDeseado);
        g2.dispose();
        return new ImageIcon(fallback);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}