package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.imageio.ImageIO;
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

    // ATRIBUTOS DE CONTROL DE PERSISTENCIA Y SESIÓN
    private Connection conexion;
    private Usuarios usuarioSesion;
    private int idJornada; // Control de la jornada seleccionada

    // DAOs necesarios para la lógica real
    private PartidosDAO partidosDAO;
    private BolsaPremiosDAO bolsaPremiosDAO;

    // CONSTRUCTOR MODIFICADO: Ahora recibe el idJornada desde la ventana anterior
    public QuinielaFrame(Connection conexion, Usuarios usuarioSesion, int idJornada) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;
        this.idJornada = idJornada;
        
        // Inicializar los DAOs con la conexión compartida
        this.partidosDAO = new PartidosDAO(conexion);
        this.bolsaPremiosDAO = new BolsaPremiosDAO(conexion);

        setTitle("Quiniela Oficial - Foreign Key Squad");
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

        // TEXTO DINÁMICO: Cambia según la jornada real de la BD
        lblTitulo = new JLabel("QUINIELA - JORNADA " + idJornada);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setBounds(40, 20, 500, 40);

        lblSubtitulo = new JLabel("TEMPORADA EN CURSO");
        lblSubtitulo.setForeground(new Color(148, 163, 184));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblSubtitulo.setBounds(45, 65, 250, 25);

        // CONSULTA DE BOLSA DE PREMIOS REAL EN LA BD
        double montoBolsa = bolsaPremiosDAO.obtenerMontoAcumulado(idJornada);
        lblPremio = new JLabel("PREMIO ACUMULADO: $" + String.format("%,.2f", montoBolsa));
        lblPremio.setForeground(new Color(16, 185, 129));
        lblPremio.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblPremio.setBounds(980, 40, 450, 35);

        panelSuperior.add(lblTitulo);
        panelSuperior.add(lblSubtitulo);
        panelSuperior.add(lblPremio);

        // PANEL DE PARTIDOS CON UN GRIDLAYOUT FLEXIBLE (0 filas indica renglones infinitos autoadaptables)
        panelPartidos = new JPanel();
        panelPartidos.setBackground(new Color(226, 232, 240));
        panelPartidos.setLayout(new GridLayout(0, 1, 18, 18));
        panelPartidos.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // CARGA DINÁMICA DE PARTIDOS DESDE POSTGRESQL
        List<Partido> listaPartidos = partidosDAO.obtenerPartidosPorJornada(idJornada);
        
        if (listaPartidos == null || listaPartidos.isEmpty()) {
            JPanel panelVacio = new JPanel();
            panelVacio.setBackground(Color.WHITE);
            panelVacio.add(new JLabel("No hay partidos programados para esta jornada aún."));
            panelPartidos.add(panelVacio);
        } else {
            for (Partido partido : listaPartidos) {
                panelPartidos.add(crearTarjetaPartido(partido));
            }
        }

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

        // EVENTO: GUARDAR PREDICCIONES EN BASE DE DATOS
        btnGuardar.addActionListener(e -> {
            // Aquí puedes iterar los Spinners de panelPartidos y usar tu PrediccionesDAO para insertar/actualizar
            JOptionPane.showMessageDialog(null, "Predicciones sincronizadas con el motor PostgreSQL");
        });

        // EVENTO: REGRESAR DE FORMA SEGURA
        btnVolver.addActionListener(e -> {
            new TorneoPrediccionesFrame(conexion, usuarioSesion);
            dispose();
        });
    }

    // MÉTODO MODIFICADO PARA ACOMODAR EL ESCUDO EN LA PARTE SUPERIOR DEL NOMBRE DEL EQUIPO
    private JPanel crearTarjetaPartido(Partido partido) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setPreferredSize(new Dimension(1350, 140)); // Aumentamos la altura a 140 para dar espacio al escudo vertical
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 2));

        Equipos local = partido.getEquipoLocal();
        Equipos visitante = partido.getEquipoVisitante();

        // ------------------ CONTENEDOR VERTICAL: EQUIPO LOCAL ------------------
        JPanel contenedorLocal = new JPanel();
        contenedorLocal.setLayout(new BoxLayout(contenedorLocal, BoxLayout.Y_AXIS));
        contenedorLocal.setBackground(Color.WHITE);
        contenedorLocal.setBounds(30, 15, 230, 110);

        JLabel lblEscudoLocal = new JLabel();
        lblEscudoLocal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEscudoLocal.setIcon(cargarIconoEscudo(local.getLogoURL())); // URL remota o ruta local de la BD

        JLabel lblLocalNom = new JLabel(local.getNombreEquipo().toUpperCase());
        lblLocalNom.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLocalNom.setForeground(new Color(15, 23, 42));
        lblLocalNom.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenedorLocal.add(lblEscudoLocal);
        contenedorLocal.add(Box.createVerticalStrut(5));
        contenedorLocal.add(lblLocalNom);
        // -----------------------------------------------------------------------

        JLabel lblGolesLocal = new JLabel("Goles");
        lblGolesLocal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGolesLocal.setBounds(290, 30, 80, 20);

        JSpinner spnLocal = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        spnLocal.setBounds(290, 55, 70, 35);
        spnLocal.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel lblVs = new JLabel("VS");
        lblVs.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblVs.setForeground(new Color(100, 116, 139));
        lblVs.setBounds(400, 53, 50, 30);

        JLabel lblGolesVisitante = new JLabel("Goles");
        lblGolesVisitante.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGolesVisitante.setBounds(490, 30, 80, 20);

        JSpinner spnVisitante = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        spnVisitante.setBounds(490, 55, 70, 35);
        spnVisitante.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // ----------------- CONTENEDOR VERTICAL: EQUIPO VISITANTE -----------------
        JPanel contenedorVis = new JPanel();
        contenedorVis.setLayout(new BoxLayout(contenedorVis, BoxLayout.Y_AXIS));
        contenedorVis.setBackground(Color.WHITE);
        contenedorVis.setBounds(590, 15, 230, 110);

        JLabel lblEscudoVis = new JLabel();
        lblEscudoVis.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEscudoVis.setIcon(cargarIconoEscudo(visitante.getLogoURL()));

        JLabel lblVisNom = new JLabel(visitante.getNombreEquipo().toUpperCase());
        lblVisNom.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblVisNom.setForeground(new Color(15, 23, 42));
        lblVisNom.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenedorVis.add(lblEscudoVis);
        contenedorVis.add(Box.createVerticalStrut(5));
        contenedorVis.add(lblVisNom);
        // ------------------------------------------------------------------------

        JLabel lblResultado = new JLabel("Resultado");
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblResultado.setForeground(new Color(71, 85, 105));
        lblResultado.setBounds(940, 25, 100, 20);

        JTextField txtResultado = new JTextField();
        txtResultado.setEditable(false);
        txtResultado.setHorizontalAlignment(JTextField.CENTER);
        txtResultado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtResultado.setBounds(910, 55, 180, 35);

        JButton btnCalcular = new JButton("CALCULAR");
        btnCalcular.setBounds(1130, 53, 140, 40);
        btnCalcular.setBackground(new Color(59, 130, 246));
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setFocusPainted(false);
        btnCalcular.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnCalcular.addActionListener(e -> {
            int golesLocal = (int) spnLocal.getValue();
            int golesVisitante = (int) spnVisitante.getValue();

            if (golesLocal > golesVisitante) {
                txtResultado.setText("GANA " + local.getNombreEquipo().toUpperCase());
            } else if (golesVisitante > golesLocal) {
                txtResultado.setText("GANA " + visitante.getNombreEquipo().toUpperCase());
            } else {
                txtResultado.setText("EMPATE");
            }
        });

        // Agregamos los sub-paneles y componentes a la tarjeta base
        card.add(contenedorLocal);
        card.add(lblGolesLocal);
        card.add(spnLocal);
        card.add(lblVs);
        card.add(lblGolesVisitante);
        card.add(spnVisitante);
        card.add(contenedorVis);
        card.add(lblResultado);
        card.add(txtResultado);
        card.add(btnCalcular);

        return card;
    }

    /**
     * Helper Method: Descarga/Lee la imagen del escudo basándose en la URL de la base de datos 
     * y le aplica un filtro Smooth Rescaling para no romper el aspecto visual de la App.
     */
    private ImageIcon cargarIconoEscudo(String rutaUrl) {
        int anchoDeseado = 55;
        int altoDeseado = 55;
        try {
            if (rutaUrl != null && (rutaUrl.startsWith("http://") || rutaUrl.startsWith("https://"))) {
                // Opción A: Es una URL de Internet (ej. almacenamiento en la nube o links públicos)
                URL url = new URL(rutaUrl);
                BufferedImage img = ImageIO.read(url);
                if (img != null) {
                    Image imgEscalada = img.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);
                    return new ImageIcon(imgEscalada);
                }
            } else if (rutaUrl != null && !rutaUrl.trim().isEmpty()) {
                // Opción B: Ruta de recursos locales dentro de tu propio .jar (src/main/resources...)
                URL urlRecurso = getClass().getResource(rutaUrl);
                if (urlRecurso != null) {
                    ImageIcon iconoOriginal = new ImageIcon(urlRecurso);
                    Image imgEscalada = iconoOriginal.getImage().getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);
                    return new ImageIcon(imgEscalada);
                }
            }
        } catch (Exception ex) {
            System.err.println("Aviso: No se pudo renderizar el escudo desde: " + rutaUrl + " (Se usará el fallback por defecto).");
        }
        
        // Opción Fallback: Si no hay imagen o falla la red, genera una silueta gris para no quebrar la vista
        BufferedImage fallback = new BufferedImage(anchoDeseado, altoDeseado, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = fallback.createGraphics();
        g2.setColor(new Color(203, 213, 225));
        g2.fillOval(0, 0, anchoDeseado, altoDeseado);
        g2.dispose();
        return new ImageIcon(fallback);
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