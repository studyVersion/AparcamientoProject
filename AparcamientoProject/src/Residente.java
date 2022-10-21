import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Residente extends Vehiculo {

	private long tiempoAcumulado;
	private Date[] estancia;

	public Residente(int matricula) {
		super(matricula);
		this.tiempoAcumulado = 0;
		this.estancia = new Date[2];
	}

	public void sumaDuracionEstancia(Date[] estancia) {

		long diferenciaTiempos = estancia[1].getTime() - estancia[2].getTime();

		tiempoAcumulado += TimeUnit.MILLISECONDS.toSeconds(diferenciaTiempos);

		//return tiempoAcumulado;

	}

	public long getTiempoAcumulado() {
		return tiempoAcumulado;
	}

	public void setTiempoAcumulado(long tiempoAcumulado) {
		this.tiempoAcumulado = tiempoAcumulado;
	}

	public Date[] getEstancia() {
		return estancia;
	}

	public void setEstancia(Date entrada, Date salida) {
		this.estancia[0] = entrada;
		this.estancia[1] = salida;	
	}
}
