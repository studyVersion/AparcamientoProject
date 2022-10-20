import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Residente extends Vehiculo{
	
    private long tiempoAcumulado;
    
	public Residente(int matricula) {
		super(matricula);
		this.tiempoAcumulado = 0;
	}
    
	public long sumaDuracionEstancia(Date entrada, Date salida) {
		
		long diferenciaTiempos = salida.getTime() - entrada.getTime();
		
		tiempoAcumulado += TimeUnit.MILLISECONDS.toSeconds(diferenciaTiempos);
		
		
		return tiempoAcumulado;
		
	}

	public long getTiempoAcumulado() {
		return tiempoAcumulado;
	}

	public void setTiempoAcumulado(long tiempoAcumulado) {
		this.tiempoAcumulado = tiempoAcumulado;
	}
}
