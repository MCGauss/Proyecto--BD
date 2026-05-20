package mx.unam.fes.acatlan.mac.proyectobd.backend.interfaz;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Partido;
import mx.unam.fes.acatlan.mac.proyectobd.backend.model.Predicciones;

public interface CalcularPuntos {
	int calcularPuntosObtenidos(Predicciones prediccion, Partido partidoReal);
}
