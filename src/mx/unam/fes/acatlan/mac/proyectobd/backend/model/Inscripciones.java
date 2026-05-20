package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

import java.time.LocalDateTime;

public class Inscripciones extends Transaccion {
	//Atributos
	private Usuarios user;
    private Jornadas jornada; // Puede ser null si es inscripción al torneo completo
    private TipoInscripcion tipoInscripcion; // "SEMANAL" o "GLOBAL"
	
	//Constructor heredado
	public Inscripciones(int id, double monto, LocalDateTime fecha, String status) {
		super(id, monto, fecha, status);
		// TODO Auto-generated constructor stub
	}

}
