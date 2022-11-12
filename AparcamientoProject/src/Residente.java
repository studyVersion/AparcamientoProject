import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Residente extends Vehiculo {

	private long tiempoAcumulado;
	private double pagoResidente;
    private long tiempoEstancia;
	public Residente(int matricula) {
		super(matricula);
		this.tiempoAcumulado = 0;
		this.pagoResidente = 0;
		this.tiempoEstancia = 0;

	}

	public long sumaDuracionEstancia() {

		long diferenciaTiempos = getEstancia()[1].getTime() - getEstancia()[0].getTime();

		tiempoAcumulado += TimeUnit.MILLISECONDS.toSeconds(diferenciaTiempos);

		return tiempoAcumulado;

	}

	public double getPagoResidente() {
		this.pagoResidente = tiempoAcumulado * 0.002;
		return pagoResidente;
	}

	public long tiempoEstancia() {
		long diferenciaTiempos = getEstancia()[1].getTime() - getEstancia()[0].getTime();
		tiempoEstancia = TimeUnit.MILLISECONDS.toSeconds(diferenciaTiempos);

		return tiempoEstancia;

	}

	public long getTiempoAcumulado() {
		return tiempoAcumulado;
	}

	public void setTiempoAcumulado(long tiempoAcumulado) {
		this.tiempoAcumulado = tiempoAcumulado;
	}

	public String toString() {

		return "\t" + getMatricula() + "\t\tResidente" + "\t\t" + tiempoEstancia + "\t\t\s\s" + tiempoAcumulado;
//	}
	}
}
