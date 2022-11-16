import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NoResidente extends Vehiculo {

	private double importe;

	public NoResidente(int matricula) {
		super(matricula);
		this.importe = 0;
	}

	public void calcularImporte(Date[] estancia) {		
		long diferenciaTiempos = estancia[1].getTime() - estancia[0].getTime();
		diferenciaTiempos = TimeUnit.MILLISECONDS.toSeconds(diferenciaTiempos);
		importe = diferenciaTiempos * 0.02;

	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	@Override
	public String toString() {
		return "------------------------------------------------\n" 
	         + "                    Importe\n"
		     + "------------------------------------------------\n" 
	              + "\tEntrada: " + entradaString()+ "\n\tSalida: " + salidaString() + "\n\tPago: " + importe + " euros\n";
	}
}
