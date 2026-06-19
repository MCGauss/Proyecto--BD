package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class Ligas {
	//Atributos
	protected int idLiga;
	protected String nombreLiga;
	
	//Constructor
	public Ligas(int idLiga, String nombreLiga) {
		this.idLiga = idLiga;
		this.nombreLiga = nombreLiga;
	}
	
	//Getters y Setters
	public int getIdLiga() {
		return idLiga;
	}
	public void setIdLiga(int idLiga) {
		this.idLiga = idLiga;
	}
	public String getNombreLiga() {
		return nombreLiga;
	}
	public void setNombreLiga(String nombreLiga) {
		this.nombreLiga = nombreLiga;
	}
	
	
}
