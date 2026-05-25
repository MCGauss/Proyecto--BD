package mx.unam.fes.acatlan.mac.proyectobd.backend.model;


public class Usuarios {
    
    // Atributos protegidos que mapean la Tabla 18 del LDD
    protected int idUsuario;     // id_usuario SERIAL
    protected String username;   // username CHARACTER VARYING(25)
    protected String email;      // email CHARACTER VARYING(80)
    protected String passsword;   // Mapea internamente a 'passsword' de la BD
    protected double saldo;      // saldo NUMERIC(8,2)
    private Rol rol;             // id_rol INTEGER (Mapeado a tu Enum)
    
    // Constructor vacío estándar para el uso de DAOs y mapeadores recurrentes
    public Usuarios() {
    }


    // Constructor completo para flujos de login y registro
    public Usuarios(int idUsuario, String username, String email, String passsword, double saldo, Rol rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.email = email;
        this.passsword = passsword;
        this.saldo = saldo;
        this.rol = rol;
    }

    // Getters y Setters limpios
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

	public String getPasssword() {
		return passsword;
	}

	public void setPasssword(String passsword) {
		this.passsword = passsword;
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
}
