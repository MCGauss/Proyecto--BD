package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

import java.time.LocalDateTime;

public abstract class Transaccion {

	//Atributos
	protected int idTrans;
	protected double montoTrans;
	protected LocalDateTime fechaTrans;
	protected StatusTransaccion statusTrans; // PENDIENTE / APROBADO / RECHAZADO
	
	//Constructor
	public Transaccion(int idTrans, double montoTrans, LocalDateTime fechaTrans, StatusTransaccion statusTrans) {
		this.idTrans = idTrans;
		this.montoTrans = montoTrans;
		this.fechaTrans = fechaTrans;
		this.statusTrans= statusTrans;
	}

	//Getters y Setters
	public int getIdTrans() {
		return idTrans;
	}

	public void setIdTrans(int idTrans) {
		this.idTrans = idTrans;
	}

	public double getMontoTrans() {
		return montoTrans;
	}

	public void setMontoTrans(double montoTrans) {
		this.montoTrans = montoTrans;
	}

	public LocalDateTime getFechaTrans() {
		return fechaTrans;
	}

	public void setFechaTrans(LocalDateTime fechaTrans) {
		this.fechaTrans = fechaTrans;
	}

	public StatusTransaccion getStatusTrans() {
		return statusTrans;
	}

	public void setStatusTrans(StatusTransaccion statusTrans) {
		this.statusTrans = statusTrans;
	} 
	
	
	
}
