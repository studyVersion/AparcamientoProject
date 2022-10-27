import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Residente extends Vehiculo {

	private long tiempoAcumulado;

	public Residente(int matricula) {
		super(matricula);
		this.tiempoAcumulado = 0;

	}

	public long sumaDuracionEstancia() {

		long diferenciaTiempos =  getEstancia()[1].getTime() - getEstancia()[0].getTime();

		tiempoAcumulado += TimeUnit.MILLISECONDS.toSeconds(diferenciaTiempos);

		 return tiempoAcumulado;

	}
	
	public long tiempoEstancia() {
		long diferenciaTiempos =  getEstancia()[1].getTime() - getEstancia()[0].getTime();
		diferenciaTiempos = TimeUnit.MILLISECONDS.toSeconds(diferenciaTiempos);
		
		return diferenciaTiempos;
		
	}

	public long getTiempoAcumulado() {
		return tiempoAcumulado;
	}

	public void setTiempoAcumulado(long tiempoAcumulado) {
		this.tiempoAcumulado = tiempoAcumulado;
	}

	public String toString() {

		return entradaString() + "\t  " + salidaString() +"\t  " + tiempoEstancia() + "\t  " + tiempoAcumulado;
	}
}
