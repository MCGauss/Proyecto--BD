package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

import java.time.LocalDateTime;

public class Inscripciones extends Transaccion {
	//Atributos
	private Usuarios usuario;
    private Jornadas jornada; // Puede ser null si es inscripción al torneo completo
    private TipoInscripcion tipoInscripcion; // "SEMANAL" o "GLOBAL"
	
	//Constructor heredado
	public Inscripciones(int idTrans, double montoTrans, LocalDateTime fechaTrans, StatusTransaccion statusTrans) {
		super(idTrans, montoTrans, fechaTrans, statusTrans);
		// TODO Auto-generated constructor stub
	
	}

	public Inscripciones() {
		// TODO Auto-generated constructor stub
	}



	//Getters y setters
	public Usuarios getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}

	public Jornadas getJornada() {
		return jornada;
	}

	public void setJornada(Jornadas jornada) {
		this.jornada = jornada;
	}

	public TipoInscripcion getTipoInscripcion() {
		return tipoInscripcion;
	}

	public void setTipoInscripcion(TipoInscripcion tipoInscripcion) {
		this.tipoInscripcion = tipoInscripcion;
	}

	
}
