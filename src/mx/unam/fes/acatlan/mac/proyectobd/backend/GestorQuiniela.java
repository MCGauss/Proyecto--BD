package mx.unam.fes.acatlan.mac.proyectobd.backend;

import java.io.*;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Usuarios;

public class GestorQuiniela {
	
	//Conexión con la BD
	//private static final String conexionBD = "BD/data";
	
	//Atributos
	private ArrayList <Usuarios> users;

	//Constructor
	public GestorQuiniela() {
		this.users = new ArrayList<>();
		//cargarDatos();
	}

	//Getters y Setters
	public ArrayList<Usuarios> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<Usuarios> users) {
		this.users = users;
	}
	
	
	//Métodos CRUD
	public void cargarDatos() {}
	
}
