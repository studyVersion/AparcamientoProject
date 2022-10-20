import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NoResidente extends Vehiculo {

	private double importe;

	public NoResidente(int matricula) {
		super(matricula);
        this.importe = 0;
	}

	public double generarImporte(Date entrada, Date salida) {
		
		long diferenciaTiempos = salida.getTime() - entrada.getTime();

		diferenciaTiempos= TimeUnit.MILLISECONDS.toSeconds(diferenciaTiempos);
		
		importe = diferenciaTiempos * 0.02;
		
		return importe;

	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	@Override
	public String toString() {
		return  "Entrada: " + getEntrada() + ", Salida: "+ getSalida() +"Importe: " + importe + "€" ;
	}
}
