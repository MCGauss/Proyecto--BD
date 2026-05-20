package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class StatusBolsa {
	// Atributos
	protected int idStatusBolsa;
    protected String descripcionStatus;

    public StatusBolsa(int idStatusBolsa, String descripcionStatus) {
        this.idStatusBolsa = idStatusBolsa;
        this.descripcionStatus = descripcionStatus;
    }

    // getters y setters
    public int getIdStatusBolsa() {
    	return idStatusBolsa; 
    }
    public void setIdStatusBolsa(int idStatusBolsa) {
    	this.idStatusBolsa = idStatusBolsa;
    }

    public String getDescripcionStatus() {
    	return descripcionStatus; 
    }
    public void setDescripcionStatus(String descripcionStatus) {
    	this.descripcionStatus = descripcionStatus; 
    }

}
