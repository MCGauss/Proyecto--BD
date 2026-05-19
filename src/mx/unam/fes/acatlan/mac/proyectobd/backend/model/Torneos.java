package mx.unam.fes.acatlan.mac.proyectobd.backend.model;
import java.time.LocalDate;

public class Torneos {
	//Atributos
	protected int idTorneo;
	private Ligas liga;
	protected String nombreTorneo;
	protected LocalDate fechaInicio;
	protected LocalDate fechaFin;
	
	//Constructor
	public Torneos(int idTorneo, Ligas liga, String nombreTorneo, LocalDate fechaInicio, LocalDate fechaFin) {
		this.idTorneo = idTorneo;
		this.liga = liga;
		this.nombreTorneo = nombreTorneo;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

	//Getters y setters
	public int getIdTorneo() {
		return idTorneo;
	}

	public void setIdTorneo(int idTorneo) {
		this.idTorneo = idTorneo;
	}

	public Ligas getLiga() {
		return liga;
	}

	public void setLiga(Ligas liga) {
		this.liga = liga;
	}

	public String getNombreTorneo() {
		return nombreTorneo;
	}

	public void setNombreTorneo(String nombreTorneo) {
		this.nombreTorneo = nombreTorneo;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	
}
