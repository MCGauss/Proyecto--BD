package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class Usuarios implements DatosPersonales{
	//Atributos
	protected int idUsuario;
	protected String username;
	protected String email;
	protected String password;
	protected double saldo;
	private Rol rol;
	
	//Constructor
	public Usuarios(int idUsuario, String username, String email, String password, double saldo, Rol rol) {
		this.idUsuario = idUsuario;
		this.username = username;
		this.email = email;
		this.password = password;
		this.saldo = saldo;
		this.rol = rol;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public String mostrarInformacion() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Métodos
	
	
}
