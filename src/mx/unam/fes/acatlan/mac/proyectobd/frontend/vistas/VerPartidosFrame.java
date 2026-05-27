package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.PartidosDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Partido;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.StatusPartido;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class VerPartidosFrame extends JFrame {

    private final Connection conexion;
    private final Usuarios usuarioSesion;
    private final int idJornada;

    private JTable tablaPartidos;
    private JScrollPane scrollPane;
    private JButton btnVolver;

    public VerPartidosFrame(Connection conexion, Usuarios usuarioSesion, int idJornada) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;
        this.idJornada = idJornada;

        setTitle("Partidos de la Jornada " + idJornada);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        inicializarComponentes();
        cargarDatosTabla();
        
        setVisible(true);
    }

    private void inicializarComponentes() {
        // Panel Principal con diseño estético Slate Claro
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(new Color(241, 245, 249));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Cabecera / Título
        JLabel lblTitulo = new JLabel("CARTELERA OFICIAL DE PARTIDOS (JORNADA " + idJornada + ")", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(15, 23, 42));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Estructura de Columnas Solicitada
        String[] columnas = {
            "Equipo Local", 
            "Goles Local", 
            "Goles Visitante", 
            "Equipo Visitante", 
            "Estatus del Partido"
        };

        // Modelo que impide la edición directa de celdas
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPartidos = new JTable(modelo);
        tablaPartidos.setRowHeight(90); // Alto ideal para que quepa Logo PNG + Nombre abajo
        tablaPartidos.setBackground(Color.WHITE);
        tablaPartidos.setGridColor(new Color(226, 232, 240));
        tablaPartidos.setSelectionBackground(new Color(239, 246, 255)); // Azul selección sutil
        tablaPartidos.setSelectionForeground(new Color(29, 78, 216));

        // Diseño del Header de la tabla
        tablaPartidos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaPartidos.getTableHeader().setBackground(new Color(30, 41, 59));
        tablaPartidos.getTableHeader().setForeground(Color.WHITE);
        tablaPartidos.getTableHeader().setReorderingAllowed(false);

        // Asignación de Renderizadores Personalizados
        tablaPartidos.getColumnModel().getColumn(0).setCellRenderer(new EquipoPNGCellRenderer());
        tablaPartidos.getColumnModel().getColumn(3).setCellRenderer(new EquipoPNGCellRenderer());
        
        CentradoCellRenderer renderCentro = new CentradoCellRenderer();
        tablaPartidos.getColumnModel().getColumn(1).setCellRenderer(renderCentro);
        tablaPartidos.getColumnModel().getColumn(2).setCellRenderer(renderCentro);
        tablaPartidos.getColumnModel().getColumn(4).setCellRenderer(renderCentro);

        scrollPane = new JScrollPane(tablaPartidos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Barra inferior para botón Volver
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(new Color(241, 245, 249));
        
        btnVolver = new JButton("VOLVER");
        btnVolver.setPreferredSize(new Dimension(200, 45));
        btnVolver.setBackground(new Color(100, 116, 139)); // Slate gris para coherencia visual
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Evento de navegación seguro para regresar al menú anterior
        btnVolver.addActionListener(e -> {
            new TorneoPrediccionesFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });

        panelBoton.add(btnVolver);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void cargarDatosTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tablaPartidos.getModel();
        PartidosDAO partidosDAO = new PartidosDAO(conexion);
        
        // Recuperamos la cartelera del DAO
        List<Partido> partidos = partidosDAO.obtenerPartidosPorJornada(idJornada);

        if (partidos != null) {
            for (Partido p : partidos) {
                Object[] fila = new Object[5];
                fila[0] = p.getEquipoLocal();      // Objeto completo Equipos (nombre y logo .png)
                fila[1] = p.getGolesLocal();
                fila[2] = p.getGolesVisitante();
                fila[3] = p.getEquipoVisitante();  // Objeto completo Equipos
                
                // Obtenemos el objeto ENUM
                StatusPartido status = p.getStatusPartido(); 
                
                if (status != null) {
                    // SOLUCIÓN: Usar solo el nombre de la constante sin "StatusPartido."
                    switch (status) {
                        case PROGRAMADO -> fila[4] = "PROGRAMADO";
                        case EN_CURSO   -> fila[4] = "EN CURSO";
                        case FINALIZADO -> fila[4] = "FINALIZADO"; 
                        case POSPUESTO  -> fila[4] = "POSPUESTO";
                        default         -> fila[4] = "DESCONOCIDO";
                    }
                } else {
                    fila[4] = "DESCONOCIDO";
                }
                
                modelo.addRow(fila);
            }
        }
    }

 // =========================================================================
    // RENDERIZADOR: LOGO PNG ARRIBA Y NOMBRE ABAJO (DENTRO DE LA MISMA CELDA)
    // =========================================================================
    private class EquipoPNGCellRenderer extends JPanel implements TableCellRenderer {
        private final JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        private final JLabel lblNombre = new JLabel("", SwingConstants.CENTER);

        public EquipoPNGCellRenderer() {
            setLayout(new BorderLayout(2, 2));
            setOpaque(true);
            
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblNombre.setForeground(new Color(15, 23, 42));
            
            add(lblLogo, BorderLayout.CENTER);
            add(lblNombre, BorderLayout.SOUTH);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            // Colores de fondo de celda alternadas para legibilidad (Zebra striping)
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                lblNombre.setForeground(table.getSelectionForeground());
            } else {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                lblNombre.setForeground(new Color(15, 23, 42));
            }

            if (value instanceof mx.unam.fes.acatlan.mac.proyectobd.backend.model.Equipos equipo) {
                lblNombre.setText(equipo.getNombreEquipo());
                
                // NOTA IMPORTANTE: Verifica si tu modelo usa getLogoURL() o getLogo(). 
                // Debe coincidir con lo que extraes en el PartidosDAO usando rs.getString("local_logo")
                String nombreArchivo = equipo.getLogoURL(); 
                
                ImageIcon iconoPng = cargarImagenPNG(nombreArchivo, 45, 45);
                lblLogo.setIcon(iconoPng);
            } else {
                lblNombre.setText("");
                lblLogo.setIcon(null);
            }

            return this;
        }

        private ImageIcon cargarImagenPNG(String nombreArchivo, int ancho, int alto) {
            try {
                if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                    // Si el String viene de la BD con extensión .eps, la forzamos a .png
                    if (nombreArchivo.toLowerCase().endsWith(".eps")) {
                        nombreArchivo = nombreArchivo.substring(0, nombreArchivo.length() - 4) + ".png";
                    }
                    
                    // Al usar getResource, busca desde la raíz del classpath del JAR/Build
                    String rutaCompleta = "/Proyecto--BD/Assets/" + nombreArchivo.trim();
                    URL urlRecurso = getClass().getResource(rutaCompleta);
                    
                    if (urlRecurso != null) {
                        ImageIcon iconoOriginal = new ImageIcon(urlRecurso);
                        Image imgEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                        return new ImageIcon(imgEscalada);
                    } else {
                        System.err.println("No se encontró el archivo físico en el classpath: " + rutaCompleta);
                    }
                }
            } catch (Exception ex) {
                System.err.println("Error cargando logo PNG: " + ex.getMessage());
            }
            
            // Fallback estético circular si no se encuentra la imagen
            BufferedImage fallback = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = fallback.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(226, 232, 240));
            g2.fillOval(0, 0, ancho, alto);
            g2.dispose();
            return new ImageIcon(fallback);
        }
    }

    // =========================================================================
    // RENDERIZADOR CENTRAL: DISEÑO PARA MARCADORES Y ESTATUS DE PARTIDO
    // =========================================================================
    private class CentradoCellRenderer extends JLabel implements TableCellRenderer {
        public CentradoCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            setText(value != null ? value.toString() : "");

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                
                // Formato especial para la columna del Estado del partido
                if (column == 4) {
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                    switch (getText()) {
                        case "FINALIZADO" -> setForeground(new Color(22, 163, 74));  // Verde Emerald
                        case "EN CURSO" -> setForeground(new Color(234, 179, 8));   // Amarillo Amber
                        case "PROGRAMADO" -> setForeground(new Color(37, 99, 235));  // Azul Blue-600
                        case "POSPUESTO" -> setForeground(new Color(225, 29, 72));   // Rojo Rose
                        default -> setForeground(Color.DARK_GRAY);
                    }
                } else {
                    // Formato destacado para los goles anotados
                    setFont(new Font("Segoe UI", Font.BOLD, 18));
                    setForeground(new Color(51, 65, 85));
                }
            }
            return this;
        }
    }
}