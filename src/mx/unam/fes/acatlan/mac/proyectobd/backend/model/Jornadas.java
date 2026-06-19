package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class Jornadas {
	//ATributos
	protected int idJornada;
	protected String nombreJornada;
	private Torneos torneo;
	
	//Constructor
	public Jornadas(int idJornada, String nombreJornada, Torneos torneo) {
		this.idJornada = idJornada;
		this.nombreJornada = nombreJornada;
		this.torneo = torneo;
	}
	
	public Jornadas() {
		// TODO Auto-generated constructor stub
	}

	//getters y setters
	public int getIdJornada() {
		return idJornada;
	}
	public void setIdJornada(int idJornada) {
		this.idJornada = idJornada;
	}
	public String getNombreJornada() {
		return nombreJornada;
	}
	public void setNombreJornada(String nombreJornada) {
		this.nombreJornada = nombreJornada;
	}
	public Torneos getTorneo() {
		return torneo;
	}
	public void setTorneo(Torneos torneo) {
		this.torneo = torneo;
	}
	
	
}
