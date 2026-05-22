package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class UsuariosGanadores {
	// Atributos
	protected int idGanador;
	protected double montoPremio;
	private BolsaPremios bolsa;
	private Usuarios usuario;
	private RankingsHistoricos ranking;
	
	// Constructor
	
	public UsuariosGanadores(int idGanador, double montoPremio, BolsaPremios bolsa,
			Usuarios usuario, RankingsHistoricos ranking) {
		
		this.idGanador = idGanador;
		this.montoPremio = montoPremio;
		this.bolsa = bolsa;
		this.usuario = usuario;
		this.ranking = ranking;
	}
	
	public UsuariosGanadores() {
		// TODO Auto-generated constructor stub
	}

	// getters y setters
    public int getIdGanador() {
        return idGanador;
    }
    public void setIdGanador(int idGanador) {
        this.idGanador = idGanador;
    }

    public double getMontoPremio() {
        return montoPremio;
    }
    public void setMontoPremio(double montoPremio) {
        this.montoPremio = montoPremio;
    }

    public BolsaPremios getBolsa() {
        return bolsa;
    }
    public void setBolsa(BolsaPremios bolsa) {
        this.bolsa = bolsa;
    }

    public Usuarios getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public RankingsHistoricos getRanking() {
        return ranking;
    }
    public void setRanking(RankingsHistoricos ranking) {
        this.ranking = ranking;
    }
	
	
}
