package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class TipoInscripcion {
	//Atributos
	protected int idTipoInscripcion;
	protected String tipoInscripcion;
	
	//Cosntructor
	public TipoInscripcion(int idTipoInscripcion, String tipoInscripcion) {
		this.idTipoInscripcion = idTipoInscripcion;
		this.tipoInscripcion = tipoInscripcion;
	}

	//Getters y setters
	public int getIdTipoInscripcion() {
		return idTipoInscripcion;
	}

	public void setIdTipoInscripcion(int idTipoInscripcion) {
		this.idTipoInscripcion = idTipoInscripcion;
	}

	public String getTipoInscripcion() {
		return tipoInscripcion;
	}

	public void setTipoInscripcion(String tipoInscripcion) {
		this.tipoInscripcion = tipoInscripcion;
	}
	
	
}
