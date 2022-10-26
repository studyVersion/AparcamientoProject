import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Residente extends Vehiculo {

	private long tiempoAcumulado;
	

	public Residente(int matricula) {
		super(matricula);
		this.tiempoAcumulado = 0;
		
	}

	public void sumaDuracionEstancia(Date[] estancia) {

		long diferenciaTiempos = estancia[1].getTime() - estancia[0].getTime();

		tiempoAcumulado += TimeUnit.MILLISECONDS.toSeconds(diferenciaTiempos);

		//return tiempoAcumulado;

	}

	public long getTiempoAcumulado() {
		return tiempoAcumulado;
	}

	public void setTiempoAcumulado(long tiempoAcumulado) {
		this.tiempoAcumulado = tiempoAcumulado;
	}

	
}
