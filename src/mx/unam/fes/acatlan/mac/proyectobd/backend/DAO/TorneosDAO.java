package mx.unam.fes.acatlan.mac.proyectobd.backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Ligas;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Torneos;

public class TorneosDAO {

    private final Connection conexion;

    /**
     * Constructor unificado que recibe la conexión activa (Arquitectura MCGauss & Diana).
     */
    public TorneosDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * SELECT: Obtiene la lista completa de torneos registrados.
     * CORREGIDO: Mapeo de fechas adaptado al tipo TIMESTAMP del LDD usando getTimestamp().
     * @return Lista de objetos Torneos.
     */
    public List<Torneos> listarTorneos() {
        List<Torneos> lista = new ArrayList<>();
        String query = "SELECT id_torneo, id_liga, nombre_torneo, fecha_inicio, fecha_fin FROM torneos;";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
                
            while (rs.next()) {
                // Mapeo relacional con la entidad Ligas (Tabla 1 del LDD)
                Ligas ligaTemporal = new Ligas(rs.getInt("id_liga"), null);
                
                // CORRECCIÓN LDD: Leemos como Timestamp porque en la BD son de tipo TIMESTAMP
                Timestamp tsInicio = rs.getTimestamp("fecha_inicio");
                Timestamp tsFin = rs.getTimestamp("fecha_fin");
                
                // Conversión segura de LocalDateTime a LocalDate para tu modelo
                LocalDate fechaInicio = (tsInicio != null) ? tsInicio.toLocalDateTime().toLocalDate() : null;
                LocalDate fechaFin = (tsFin != null) ? tsFin.toLocalDateTime().toLocalDate() : null;
                
                // Construcción de la entidad de negocio respetando tu constructor
                Torneos torneo = new Torneos(
                    rs.getInt("id_torneo"),
                    ligaTemporal,
                    rs.getString("nombre_torneo"),
                    fechaInicio,
                    fechaFin
                );
                
                lista.add(torneo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
