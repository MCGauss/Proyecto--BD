package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class BolsaPremios {
	// Atributos
	protected int idBolsa;
	protected double montoAcumulado;
	private Jornadas jornada;
	private StatusBolsa statusBolsa;
	
	// Constructor completo
    public BolsaPremios(int idBolsa, double montoAcumulado, Jornadas jornada, StatusBolsa statusBolsa) {
        this.idBolsa = idBolsa;
        this.montoAcumulado = montoAcumulado;
        this.jornada = jornada;
        this.statusBolsa = statusBolsa;
    }

    // getters y setters
    public int getIdBolsa() {
        return idBolsa;
    }

    public void setIdBolsa(int idBolsa) {
        this.idBolsa = idBolsa;
    }

    public double getMontoAcumulado() {
        return montoAcumulado;
    }

    public void setMontoAcumulado(double montoAcumulado) {
        this.montoAcumulado = montoAcumulado;
    }

    public Jornadas getJornada() {
        return jornada;
    }

    public void setJornada(Jornadas jornada) {
        this.jornada = jornada;
    }

    public StatusBolsa getStatusBolsa() {
        return statusBolsa;
    }

    public void setStatusBolsa(StatusBolsa statusBolsa) {
        this.statusBolsa = statusBolsa;
    }
	
}
