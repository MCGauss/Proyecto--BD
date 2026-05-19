package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class Equipos {
	//Atributos
	protected int idEquipo;
	protected String nombreEquipo;
	protected String logoURL;
	private Ligas liga;
	
	//Constructor
	public Equipos(int idEquipo, String nombreEquipo, String logoURL, Ligas liga) {
		this.idEquipo = idEquipo;
		this.nombreEquipo = nombreEquipo;
		this.logoURL = logoURL;
		this.liga = liga;
	}

	//Getters y Setters
	public int getIdEquipo() {
		return idEquipo;
	}

	public void setIdEquipo(int idEquipo) {
		this.idEquipo = idEquipo;
	}

	public String getNombreEquipo() {
		return nombreEquipo;
	}

	public void setNombreEquipo(String nombreEquipo) {
		this.nombreEquipo = nombreEquipo;
	}

	public String getLogoURL() {
		return logoURL;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}
	
	public Ligas getLiga() {
		return liga;
	}
	
	public void setLiga(Ligas liga) {
		this.liga = liga;
	}
	
	//Métodos
}
