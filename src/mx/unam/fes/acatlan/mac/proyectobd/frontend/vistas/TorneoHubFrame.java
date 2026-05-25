package mx.unam.fes.acatlan.mac.proyectobd.frontend.vistas;

import java.awt.*;
import java.sql.Connection; // NUEVO
import javax.swing.*;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios; // NUEVO

public class TorneoHubFrame extends JFrame {

    JPanel panelPrincipal;
    JLabel lblTitulo;
    JPanel cardPanel;
    JButton btnEntrar, btnRanking, btnVolver;

    // NUEVO: Atributos para mantener el estado de la sesión
    private Connection conexion;
    private Usuarios usuarioSesion;

    // MODIFICADO: Ahora el constructor recibe los datos
    public TorneoHubFrame(Connection conexion, Usuarios usuarioSesion) {
        this.conexion = conexion;
        this.usuarioSesion = usuarioSesion;

        setTitle("Torneo Completo");
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        iniciarComponentes();
        configurarEventosBD(); // NUEVO: Mover los listeners aquí

        setVisible(true);
    }

    private void iniciarComponentes() {
        // ... (Todo el diseño visual de Tony intacto) ...
        // NOTA: Recuerda remover los actionListeners que Tony puso aquí al final
    }

    private void configurarEventosBD() {
        // Al volver, regresamos al Menú Principal con la sesión activa
        btnVolver.addActionListener(e -> {
            new MenuPrincipal(conexion, usuarioSesion);
            dispose();
        });

        // Al entrar a las predicciones del torneo, le heredamos la sesión
        btnEntrar.addActionListener(e -> {
            new TorneoPrediccionesFrame(conexion, usuarioSesion);
            dispose();
        });

        // Si tienes un frame de ranking específico para torneo:
        // btnRanking.addActionListener(e -> { ... });
    }
}