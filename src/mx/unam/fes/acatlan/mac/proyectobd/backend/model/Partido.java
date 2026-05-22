package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

import java.time.LocalDateTime;

public class Partido {
	//Atributos
	protected int idPartido;
	private Jornadas jornada;
	private Equipos equipoLocal;
    private Equipos equipoVisitante;
    protected Integer golesLocal; //Usar objeto Integer para que permita null si no se ha jugado
    protected Integer golesVisitante;
    protected LocalDateTime fechaHoraProg;
    private StatusPartido statusPartido; //Programado, finalizado o pospuesto
	
    public Partido(int idPartido, Jornadas jornada, Equipos equipoLocal, Equipos equipoVisitante, Integer golesLocal,
			Integer golesVisitante, LocalDateTime fechaHoraProg, StatusPartido statusPartido) {
		this.idPartido = idPartido;
		this.jornada = jornada;
		this.equipoLocal = equipoLocal;
		this.equipoVisitante = equipoVisitante;
		this.golesLocal = null; //Iniciar en null porque no se ha jugado
		this.golesVisitante = null;
		this.fechaHoraProg = fechaHoraProg;
		this.statusPartido = statusPartido;
	}

	public Partido() {
		// TODO Auto-generated constructor stub
	}

	public int getIdPartido() {
		return idPartido;
	}

	public void setIdPartido(int idPartido) {
		this.idPartido = idPartido;
	}

	public Jornadas getJornada() {
		return jornada;
	}

	public void setJornada(Jornadas jornada) {
		this.jornada = jornada;
	}

	public Equipos getEquipoLocal() {
		return equipoLocal;
	}

	public void setEquipoLocal(Equipos equipoLocal) {
		this.equipoLocal = equipoLocal;
	}

	public Equipos getEquipoVisitante() {
		return equipoVisitante;
	}

	public void setEquipoVisitante(Equipos equipoVisitante) {
		this.equipoVisitante = equipoVisitante;
	}

	public Integer getGolesLocal() {
		return golesLocal;
	}

	public void setGolesLocal(Integer golesLocal) {
		this.golesLocal = golesLocal;
	}

	public Integer getGolesVisitante() {
		return golesVisitante;
	}

	public void setGolesVisitante(Integer golesVisitante) {
		this.golesVisitante = golesVisitante;
	}
	
	public LocalDateTime getFechaHoraProg() {
		return fechaHoraProg;
	}
	
	public void setFechaHoraProg(LocalDateTime fechaHoraProg) {
		this.fechaHoraProg = fechaHoraProg;
	}

	public StatusPartido getStatusPartido() {
		return statusPartido;
	}

	public void setStatusPartido(StatusPartido statusPartido) {
		this.statusPartido = statusPartido;
	}
    
    
    
}
