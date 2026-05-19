package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class Partido {
	//Atributos
	protected int idPartido;
	private Jornadas jornada;
	private Equipos equipoLocal;
    private Equipos equipoVisitante;
    protected Integer golesLocal; //Usar objeto Integer para que permita null si no se ha jugado
    protected Integer golesVisitante;
    protected String statusPartido; //Programado, finalizado o pospuesto
	
    public Partido(int idPartido, Jornadas jornada, Equipos equipoLocal, Equipos equipoVisitante, Integer golesLocal,
			Integer golesVisitante, String statusPartido) {
		super();
		this.idPartido = idPartido;
		this.jornada = jornada;
		this.equipoLocal = equipoLocal;
		this.equipoVisitante = equipoVisitante;
		this.golesLocal = null; //Iniciar en null porque no se ha jugado
		this.golesVisitante = null;
		this.statusPartido = statusPartido;
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

	public String getStatusPartido() {
		return statusPartido;
	}

	public void setStatusPartido(String statusPartido) {
		this.statusPartido = statusPartido;
	}
    
    
    
}
