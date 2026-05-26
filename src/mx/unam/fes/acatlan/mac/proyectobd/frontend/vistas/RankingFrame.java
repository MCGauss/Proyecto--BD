package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.RankingsHistoricosDAO;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.TorneosDAO;

public class RankingFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    
    JPanel panel;
    JLabel lblTitulo;
    JComboBox<Torneos> comboTorneo;
    JTable tablaRanking;
    JScrollPane scroll;
    JButton btnActualizar;
    JButton btnVolver;
    JPanel cardTabla;

    private Connection conexion;
    private Usuarios usuarioSesion;
    private RankingsHistoricosDAO rankingDAO;
    private TorneosDAO torneoDAO;

    public RankingFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;
        this.rankingDAO = new RankingsHistoricosDAO(conexion);
        this.torneoDAO = new TorneosDAO(conexion);
        
        setTitle("Ranking General");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        iniciarComponentes();
        actualizarTablaReal();
        setVisible(true);
    }

    private void iniciarComponentes() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(226,232,240));

        lblTitulo = new JLabel("TABLA GENERAL DE POSICIONES");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(15,23,42));

        comboTorneo = new JComboBox<>();
        //corregido: 
        List<Torneos> torneos = torneoDAO.listarTorneos();
        if (torneos != null) {
            for (Torneos t : torneos) {
                comboTorneo.addItem(t);
            }
        }
        
        comboTorneo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        comboTorneo.setPreferredSize(new Dimension(220, 38));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(226,232,240));
        panelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 20));
        panelSuperior.add(lblTitulo);
        panelSuperior.add(comboTorneo);

        // NUEVO ESQUEMA DE COLUMNAS CON ESTADÍSTICAS GLOBALES AL FINAL
        String columnas[] = {
            "USUARIO", "J1", "J2", "J3", "J4", "J5", "J6", "J7", "J8", 
            "J9", "J10", "J11", "J12", "J13", "J14", "J15", "J16", "J17", 
            "ACIERTOS", "ERRORES", "TOTAL"
        };

        DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, columnas) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRanking = new JTable(tableModel);
        tablaRanking.setRowHeight(38);
        tablaRanking.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaRanking.setBackground(new Color(30,41,59));
        tablaRanking.setForeground(Color.WHITE);
        tablaRanking.setGridColor(new Color(71,85,105));
        tablaRanking.setSelectionBackground(new Color(59,130,246));
        tablaRanking.setSelectionForeground(Color.WHITE);
        tablaRanking.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tablaRanking.getTableHeader().setBackground(new Color(15,23,42));
        tablaRanking.getTableHeader().setForeground(Color.WHITE);
        tablaRanking.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaRanking.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tablaRanking.getColumnCount(); i++) {
            tablaRanking.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            tablaRanking.getColumnModel().getColumn(i).setPreferredWidth(55);
        }
        tablaRanking.getColumnModel().getColumn(0).setPreferredWidth(150); // Usuario
        tablaRanking.getColumnModel().getColumn(18).setPreferredWidth(80); // Aciertos
        tablaRanking.getColumnModel().getColumn(19).setPreferredWidth(80); // Errores
        tablaRanking.getColumnModel().getColumn(20).setPreferredWidth(90); // Total

        scroll = new JScrollPane(tablaRanking);
        scroll.getViewport().setBackground(new Color(30,41,59));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(59,130,246), 2));

        cardTabla = new JPanel();
        cardTabla.setLayout(new BorderLayout());
        cardTabla.setBackground(new Color(15,23,42));
        cardTabla.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        cardTabla.add(scroll, BorderLayout.CENTER);

        btnActualizar = new JButton("ACTUALIZAR");
        btnActualizar.setPreferredSize(new Dimension(200, 42));
        btnActualizar.setBackground(new Color(59,130,246));
        btnActualizar.setOpaque(true);
        btnActualizar.setBorderPainted(false);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVolver = new JButton("VOLVER");
        btnVolver.setPreferredSize(new Dimension(200, 42));
        btnVolver.setBackground(new Color(71,85,105));
        btnVolver.setOpaque(true);
        btnVolver.setBorderPainted(false);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(226,232,240));
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 15));
        panelBotones.add(btnActualizar);
        panelBotones.add(btnVolver);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(cardTabla, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        add(panel);

        comboTorneo.addActionListener(e -> actualizarTablaReal());

        btnActualizar.addActionListener(e -> {
            actualizarTablaReal();
            JOptionPane.showMessageDialog(this, "Datos sincronizados.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });

        btnVolver.addActionListener(e -> {
            new MenuPrincipal(conexion, usuarioSesion);
            dispose();
        });
    }

    private void actualizarTablaReal() {
        DefaultTableModel modelo = (DefaultTableModel) tablaRanking.getModel();
        modelo.setRowCount(0); // Limpiar filas anteriores

        Torneos torneoSeleccionado = (Torneos) comboTorneo.getSelectedItem();
        if (torneoSeleccionado == null) return;

        // Llamada segura al DAO
        List<Object[]> datosPlanos = rankingDAO.obtenerMatrizPuntosPorTorneo(torneoSeleccionado.getNombreTorneo());
        if (datosPlanos == null || datosPlanos.isEmpty()) {
            return; // Si no hay datos, deja la tabla limpia
        }
        // Mapa: Username -> Contenedor con [int[] de 17 jornadas, int aciertos_totales, int errores_totales]
        Map<String, Object[]> mapeoUsuarios = new LinkedHashMap<>();

        for (Object[] fila : datosPlanos) {
            String usuario = (String) fila[0];
            String jornadaNom = (String) fila[1];
            
            // Conversión segura de tipos de PostgreSQL (evita ClassCastException)
            int puntos = ((Number) fila[2]).intValue();
            int aciertos = ((Number) fila[3]).intValue();
            int errores = ((Number) fila[4]).intValue();

            int numJornada = extraerNumeroJornada(jornadaNom);
            if (numJornada < 1 || numJornada > 17) continue;

            // Si el usuario no existe en el mapa, lo inicializamos de forma limpia
            if (!mapeoUsuarios.containsKey(usuario)) {
                mapeoUsuarios.put(usuario, new Object[]{ new int[17], 0, 0 });
            }

            // Recuperamos el contenedor del usuario
            Object[] contenedor = mapeoUsuarios.get(usuario);
            
            // 1. Asignamos los puntos en la casilla de su jornada (0 a 16)
            int[] miArregloJornadas = (int[]) contenedor[0];
            miArregloJornadas[numJornada - 1] = puntos;

            // 2. Acumulamos de forma segura los aciertos y errores totales del torneo
            int aciertosActuales = ((Number) contenedor[1]).intValue();
            int erroresActuales = ((Number) contenedor[2]).intValue();
            
            contenedor[1] = aciertosActuales + aciertos;
            contenedor[2] = erroresActuales + errores;
        }
       
     // =========================================================
        // LISTA TEMPORAL PARA COMPILAR Y ORDENAR LAS FILAS
        // =========================================================
        List<Object[]> listaFilas = new ArrayList<>();

        for (Map.Entry<String, Object[]> entrada : mapeoUsuarios.entrySet()) {
            String username = entrada.getKey();
            Object[] contenedor = entrada.getValue();
            
            int[] puntosPorJornada = (int[]) contenedor[0];
            int acumuladoAciertos = ((Number) contenedor[1]).intValue();
            int acumuladoErrores = ((Number) contenedor[2]).intValue();

            // Creamos la estructura física de la fila (21 columnas)
            Object[] filaTabla = new Object[21];
            filaTabla[0] = username; // Columna 0: Nombre de usuario

            int sumaTotalPuntos = 0;
            for (int i = 0; i < 17; i++) {
                int pts = puntosPorJornada[i];
                filaTabla[i + 1] = pts; // Columnas 1 a 17: J1 a J17
                sumaTotalPuntos += pts;
            }
            
            filaTabla[18] = acumuladoAciertos; // Columna 18: Aciertos
            filaTabla[19] = acumuladoErrores;  // Columna 19: Errores
            filaTabla[20] = sumaTotalPuntos;   // Columna 20: Total General

            // En lugar de meterlo directo al modelo, lo guardamos en nuestra lista temporal
            listaFilas.add(filaTabla);
        }

        // =========================================================
        // ORDENAMIENTO DESCENDENTE USANDO LAMBDAS (JAVA 8+)
        // =========================================================
        // Compara la columna indexada en 20 (Total General) de ambas filas
        listaFilas.sort((fila1, fila2) -> {
            int total1 = (int) fila1[20];
            int total2 = (int) fila2[20];
            return Integer.compare(total2, total1); // total2 primero garantiza orden de Mayor a Menor
        });

        // =========================================================
        // VOLCAR LAS FILAS YA ORDENADAS AL MODELO DEL JTABLE
        // =========================================================
        for (Object[] filaOrdenada : listaFilas) {
            modelo.addRow(filaOrdenada);
        }
     
    }

    /**
     * Filtro Adaptable Extremo: Si el nombre de la jornada contiene texto ("Jornada 5"),
     * extrae el número. Si Diana guardó únicamente el número string ("5"), lo parsea directamente.
     */
    private int extraerNumeroJornada(String cadena) {
        if (cadena == null) return -1;
        try {
            String digitos = cadena.replaceAll("[^0-9]", "");
            return digitos.isEmpty() ? -1 : Integer.parseInt(digitos);
        } catch (Exception e) {
            return -1;
        }
    }
}