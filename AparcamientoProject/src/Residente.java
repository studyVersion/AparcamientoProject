import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Residente extends Vehiculo{
	
    private long tiempoAcumulado;
    
	public Residente(int matricula) {
		super(matricula);
		
	}
    
	public long sumaDuracionEstancia(Date entrada, Date salida) {
		
		long diferenciaTiempos = salida.getTime() - entrada.getTime();
		
		tiempoAcumulado += TimeUnit.MILLISECONDS.toSeconds(diferenciaTiempos);
		
		return tiempoAcumulado;
		
	}
}
