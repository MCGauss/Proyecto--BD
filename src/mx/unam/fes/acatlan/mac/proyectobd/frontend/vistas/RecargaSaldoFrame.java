package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection; // INTEGRADO PARA LA PERSISTENCIA DE LA CONEXIÓN
import java.sql.SQLException;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.DAO.*;

public class RecargaSaldoFrame extends JFrame {

    private JTextField txtTarjeta;
    private JTextField txtNombre;
    private JTextField txtVencimiento; 
    private JPasswordField txtCVV;
    private JTextField txtMonto;
    private JButton btnRecargar;
    private JButton btnVolver;

    // ATRIBUTOS DE PERSISTENCIA INYECTADOS
    private Connection conexion;
    private Usuarios usuarioSesion;

    // CONSTRUCTOR MODIFICADO PARA RECIBIR Y MANTENER LA SESIÓN ACTIVA
    public RecargaSaldoFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;
        
        iniciarComponentes();
        configurarEventos();
        
        setTitle("Recargar saldo");
        setSize(700, 620); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true); 
        
    }

    private void iniciarComponentes() {

    	// Panel principal con degradado oscuro elegante
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(15, 23, 42); // Slate 900
                Color color2 = new Color(30, 41, 59); // Slate 800
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelPrincipal.setLayout(null);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título de la ventana
        JLabel lblTitulo = new JLabel("RECARGAR SALDO", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBounds(50, 25, 600, 40);
        panelPrincipal.add(lblTitulo);

        // Formulario de Tarjeta
        JLabel lblNumTarjeta = crearLabel("Número de Tarjeta (16 dígitos):");
        lblNumTarjeta.setBounds(80, 90, 540, 25);
        panelPrincipal.add(lblNumTarjeta);

        txtTarjeta = crearTextField();
        txtTarjeta.setBounds(80, 120, 540, 45);
        panelPrincipal.add(txtTarjeta);

        JLabel lblTitular = crearLabel("Nombre del Titular:");
        lblTitular.setBounds(80, 175, 540, 25);
        panelPrincipal.add(lblTitular);

        txtNombre = crearTextField();
        txtNombre.setBounds(80, 205, 540, 45);
        panelPrincipal.add(txtNombre);

        // Campos divididos (Vencimiento y CVV)
        JLabel lblVencimiento = crearLabel("Vencimiento (MM/AA):");
        lblVencimiento.setBounds(80, 260, 250, 25);
        panelPrincipal.add(lblVencimiento);

        txtVencimiento = crearTextField();
        txtVencimiento.setBounds(80, 290, 250, 45);
        panelPrincipal.add(txtVencimiento);

        JLabel lblCVV = crearLabel("CVV:");
        lblCVV.setBounds(370, 260, 250, 25);
        panelPrincipal.add(lblCVV);

        txtCVV = new JPasswordField();
        estilizarCampo(txtCVV);
        txtCVV.setBounds(370, 290, 250, 45);
        panelPrincipal.add(txtCVV);

        // Campo de Monto a Recargar
        JLabel lblMonto = crearLabel("Monto a Recargar ($MXN):");
        lblMonto.setBounds(80, 355, 540, 25);
        panelPrincipal.add(lblMonto);

        txtMonto = crearTextField();
        txtMonto.setBounds(80, 385, 540, 45);
        txtMonto.setFont(new Font("Segoe UI", Font.BOLD, 20));
        txtMonto.setForeground(new Color(34, 197, 94)); // Texto verde para el dinero
        panelPrincipal.add(txtMonto);

        // Botón de acción Recargar
        btnRecargar = crearBoton("PROCESAR RECARGA", new Color(34, 197, 94), 80, 470);
        panelPrincipal.add(btnRecargar);

        // Botón Volver
        btnVolver = crearBoton("CANCELAR / VOLVER", new Color(239, 68, 68), 370, 470);
        panelPrincipal.add(btnVolver);

        add(panelPrincipal);
    }
    
    //Método para configurar eventos
    private void configurarEventos() {
        // Acción de Recargar e Impactar la Base de Datos
        btnRecargar.addActionListener(e -> {
            ejecutarRecarga();
        });

        // Acción de Volver
        btnVolver.addActionListener(e -> {
            new MiCuentaFrame(conexion, usuarioSesion).setVisible(true);
            dispose();
        });
    }

    //MÉTODO RECARGA 
    private void ejecutarRecarga() {
        String tarjeta = txtTarjeta.getText().trim();
        String nombre = txtNombre.getText().trim();
        String vencimiento = txtVencimiento.getText().trim();
        String cvv = new String(txtCVV.getPassword()).trim();
        String montoStr = txtMonto.getText().trim();

        // 1. Validaciones de campos vacíos
        if (tarjeta.isEmpty() || nombre.isEmpty() || vencimiento.isEmpty() || cvv.isEmpty() || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos del formulario.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Validación de formato de monto numérico
        double montoRecarga;
        try {
            montoRecarga = Double.parseDouble(montoStr);
            if (montoRecarga <= 0) {
                JOptionPane.showMessageDialog(this, "El monto a recargar debe ser mayor a $0.00 pesos.", "Monto Inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El monto ingresado debe ser un valor numérico válido.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

     // 3. Conexión e impacto a la base de datos PostgreSQL mediante procedimiento almacenado
     // 3. Conexión e impacto a la base de datos PostgreSQL reutilizando tu UsuariosDAO
        try {
            // Instanciamos tu DAO pasándole la conexión activa
            UsuariosDAO dao = new UsuariosDAO(conexion);
            
            // Invocamos tu método que ya tiene la lógica del CallableStatement estructurada
            boolean exito = dao.abonarSaldoPorDeposito(usuarioSesion.getIdUsuario(), montoRecarga);
            
            if (exito) {
                // Sincronizamos el objeto de sesión en memoria sumando el monto
                double nuevoSaldo = usuarioSesion.getSaldo() + montoRecarga;
                usuarioSesion.setSaldo(nuevoSaldo);
                
                JOptionPane.showMessageDialog(this, 
                    "¡Recarga exitosa!\nSe han abonado $" + montoRecarga + " MXN a tu cuenta.", 
                    "Operación Completada", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Redirección al Frame anterior (MiCuentaFrame) con la persistencia intacta
                new MiCuentaFrame(conexion, usuarioSesion).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se pudo procesar el depósito. Verifica los parámetros.", 
                    "Error Interno", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al procesar la transacción en la Base de Datos:\n" + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    //---------------- AUXILIARES DE ESTILOS ESTÉTICOS ----------------
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(new Color(203, 213, 225)); // Slate 300
        label.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        return label;
    }

    private JTextField crearTextField() {
        JTextField txt = new JTextField();
        estilizarCampo(txt);
        return txt;
    }

    private void estilizarCampo(JTextField field) {
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 45));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBackground(new Color(51, 65, 85)); // Slate 700
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105), 1), // Slate 600
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
    }

    private JButton crearBoton(String texto, Color colorFondo, int x, int y) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, 250, 50);
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover sutil básico
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
            }
        });
        
        return boton;
    }
}