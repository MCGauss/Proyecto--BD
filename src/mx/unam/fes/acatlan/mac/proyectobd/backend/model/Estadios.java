package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class Estadios {
	// Atributos 
	protected int idEstadio;
    protected String nombreEstadio;
    protected String ciudad;

    public Estadios(int idEstadio, String nombreEstadio, String ciudad) {
        this.idEstadio = idEstadio;
        this.nombreEstadio = nombreEstadio;
        this.ciudad = ciudad;
    }

    // getters y setters
    public int getIdEstadio() { 
    	return idEstadio; 
    }
    public void setIdEstadio(int idEstadio) {
    	this.idEstadio = idEstadio;
    }

    public String getNombreEstadio() {
    	return nombreEstadio; 
    }
    public void setNombreEstadio(String nombreEstadio) {
    	this.nombreEstadio = nombreEstadio;
    }

    public String getCiudad() {
    	return ciudad; 
    }
    public void setCiudad(String ciudad) {
    	this.ciudad = ciudad;
    }

}
