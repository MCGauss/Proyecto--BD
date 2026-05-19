package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class StatusPartido {
	// Atributos
	protected int idStatusPartido;
    protected String descripcionStatus;

    public StatusPartido(int idStatusPartido, String descripcionStatus) {
        this.idStatusPartido = idStatusPartido;
        this.descripcionStatus = descripcionStatus;
    }

    // getters y setters
    public int getIdStatusPartido() {
    	return idStatusPartido; 
    }
    public void setIdStatusPartido(int idStatusPartido) {
    	this.idStatusPartido = idStatusPartido; 
    }

    public String getDescripcionStatus() {
    	return descripcionStatus;
    }
    public void setDescripcionStatus(String descripcionStatus) {
    	this.descripcionStatus = descripcionStatus;
    }

}
