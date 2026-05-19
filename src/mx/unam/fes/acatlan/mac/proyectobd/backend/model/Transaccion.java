package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

import java.time.LocalDateTime;

public abstract class Transaccion {

	//Atributos
	protected int id;
	protected double monto;
	protected LocalDateTime fecha;
	protected String status;
	
	//Constructor
	public Transaccion(int id, double monto, LocalDateTime fecha, String status) {
		super();
		this.id = id;
		this.monto = monto;
		this.fecha = fecha;
		this.status = status;
	}

	//Getters y Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	} 
	
	
	
}
