package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class Predicciones {
	//Atributos
	protected int idPrediccion;
	protected int predGolesLocal;
	protected int predGolesVis;
	protected Integer puntosObtenidos;
	private Usuarios usuario;
	private Partido partido;
	
	//Constructor
	public Predicciones(int idPrediccion, int predGolesLocal, int predGolesVis, Integer puntosObtenidos,
			Usuarios usuario, Partido partido) {
		this.idPrediccion = idPrediccion;
		this.predGolesLocal = predGolesLocal;
		this.predGolesVis = predGolesVis;
		this.puntosObtenidos = null;
		this.usuario = usuario;
		this.partido = partido;
	}

	public Predicciones() {
		// TODO Auto-generated constructor stub
	}

	//Getters y Setters
	public int getIdPrediccion() {
		return idPrediccion;
	}

	public void setIdPrediccion(int idPrediccion) {
		this.idPrediccion = idPrediccion;
	}

	public int getPredGolesLocal() {
		return predGolesLocal;
	}

	public void setPredGolesLocal(int predGolesLocal) {
		this.predGolesLocal = predGolesLocal;
	}

	public int getPredGolesVis() {
		return predGolesVis;
	}

	public void setPredGolesVis(int predGolesVis) {
		this.predGolesVis = predGolesVis;
	}

	public Integer getPuntosObtenidos() {
		return puntosObtenidos;
	}

	public void setPuntosObtenidos(Integer puntosObtenidos) {
		this.puntosObtenidos = puntosObtenidos;
	}

	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}

	public Partido getPartido() {
		return partido;
	}

	public void setPartido(Partido partido) {
		this.partido = partido;
	}
	
	
}
