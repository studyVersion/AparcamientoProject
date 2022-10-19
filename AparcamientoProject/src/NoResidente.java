import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NoResidente extends Vehiculo {

	private double importe;

	public NoResidente(int matricula) {
		super(matricula);

	}

	public double generarImporte(Date entrada, Date salida) {
		
		long diferenciaTiempos = salida.getTime() - entrada.getTime();

		diferenciaTiempos= TimeUnit.MILLISECONDS.toSeconds(diferenciaTiempos);
		
		importe = diferenciaTiempos * 0.02;
		
		return importe;

	}
}
