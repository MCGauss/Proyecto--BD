package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

import java.time.LocalDateTime;

public class PagosPremios extends Transaccion {

	//ATributos
	protected int idPago;
	protected double montoPagado;
	protected LocalDateTime fechaPago;
	private UsuariosGanadores ganador;
	private MetodosPagos metodoPago;
	
	//Constructor heredado
	public PagosPremios(int idTrans, double montoTrans, LocalDateTime fechaTrans, StatusTransaccion statusTrans) {
			super(idTrans, montoTrans, fechaTrans, statusTrans);
			// TODO Auto-generated constructor stub
		
	}
		
	public PagosPremios() {
		
	}

	//Getters y setters
	public int getIdPago() {
		return idPago;
	}

	public void setIdPago(int idPago) {
		this.idPago = idPago;
	}

	public double getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(double montoPagado) {
		this.montoPagado = montoPagado;
	}

	public LocalDateTime getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDateTime fechaPago) {
		this.fechaPago = fechaPago;
	}

	public UsuariosGanadores getGanador() {
		return ganador;
	}

	public void setGanador(UsuariosGanadores ganador) {
		this.ganador = ganador;
	}

	public MetodosPagos getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(MetodosPagos metodoPago) {
		this.metodoPago = metodoPago;
	}
	
	
	
	
}
