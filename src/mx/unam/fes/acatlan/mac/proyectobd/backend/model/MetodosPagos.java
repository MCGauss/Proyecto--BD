package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class MetodosPagos {
	//Atributos
	protected int idMetodo;
	protected String metodoPago;
	
	//Constructor
	public MetodosPagos(int idMetodo, String metodoPago) {
		this.idMetodo = idMetodo;
		this.metodoPago = metodoPago;
	}
	
	public MetodosPagos() {
		
	}
	
	//Getters y setters
	public int getIdMetodo() {
		return idMetodo;
	}
	
	public void setIdMetodo(int idMetodo) {
		this.idMetodo = idMetodo;
	}
	
	public String getMetodoPago() {
		return metodoPago;
	}
	
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
}
