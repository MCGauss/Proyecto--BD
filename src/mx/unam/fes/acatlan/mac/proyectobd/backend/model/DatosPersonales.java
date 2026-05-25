package mx.unam.fes.acatlan.mac.proyectobd.backend.model;

public class DatosPersonales {
    
    // Atributos que mapean al 100% las columnas de tu Tabla 19 del LDD
    private String nombreUsuario; // nombre_usuario
    private String apPaterno;     // ap_paterno
    private String apMaterno;     // ap_materno
    private String telefono;      // telefono
    private Usuarios usuario;     // pfk_rol_usuario (id_usuario como PK y FK)

    // Constructor vacío estándar
    public DatosPersonales() {
    }

    // Constructor completo
    public DatosPersonales(String nombreUsuario, String apPaterno, String apMaterno, String telefono, Usuarios usuario) {
        this.nombreUsuario = nombreUsuario;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.telefono = telefono;
        this.usuario = usuario;
    }

    /**
     * Implementación del método que tenías pensado para tu UI en Swing.
     * @return Cadena con el nombre completo y teléfono formateado.
     */
    public String mostrarInformacion() {
        return apPaterno + " " + apMaterno + ", " + nombreUsuario + " - Tel: " + telefono;
    }

    // Getters y Setters necesarios para que el DAO pueda leer y escribir en la BD
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }
}
