package mx.unam.fes.acatlan.mac.proyectobd.frontend;

import javax.swing.*;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

import java.awt.*;
import java.io.File;


public abstract class FondoPanel extends JPanel {
	//Atributos
	protected Usuarios user;
	private Image background;
	
	
	public FondoPanel(Usuarios user, Image background) {
		this.user = user;
		setLayout(new BorderLayout());
	}
	
	//Método para darle formato al fondo
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        } else {
            
            g.setColor(new Color(245, 248, 255)); 
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

	//Método para agregar imagen de fondo
    @Override
    public void addNotify() {
        super.addNotify();
        
        String url = "Assets/banner_cancha_futbol.jpg";
        File file = new File(url);
        if (file.exists()) {
            ImageIcon icon = new ImageIcon(url);
            background = icon.getImage();
            repaint();
        } else {
            System.err.println("Error, la imagen no pudo ser encontrada: " + url);
        }
    }
	
}
