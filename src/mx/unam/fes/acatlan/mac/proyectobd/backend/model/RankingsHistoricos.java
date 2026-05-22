package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class RankingsHistoricos {
	//Atributos
	protected int idRanking;
	protected int puntos;
	protected int aciertos;
	protected int errores;
	private Usuarios usuario;
	private Predicciones prediccion;
	
	//Constructor
	public RankingsHistoricos(int idRanking, int puntos, int aciertos, int errores, Usuarios usuario,
			Predicciones prediccion) {
		this.idRanking = idRanking;
		this.puntos = puntos;
		this.aciertos = aciertos;
		this.errores = errores;
		this.usuario = usuario;
		this.prediccion = prediccion;
	}
	
	public RankingsHistoricos() {
		
	}

	//Getters y Setters
	public int getIdRanking() {
		return idRanking;
	}

	public void setIdRanking(int idRanking) {
		this.idRanking = idRanking;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public int getAciertos() {
		return aciertos;
	}

	public void setAciertos(int aciertos) {
		this.aciertos = aciertos;
	}

	public int getErrores() {
		return errores;
	}

	public void setErrores(int errores) {
		this.errores = errores;
	}

	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}

	public Predicciones getPrediccion() {
		return prediccion;
	}

	public void setPrediccion(Predicciones prediccion) {
		this.prediccion = prediccion;
	}
	
}
