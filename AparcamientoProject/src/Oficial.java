import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Oficial extends Vehiculo {
	private Map<Integer, Date[]> listaEstancia = new HashMap<>();
	private Date[] estancia;

	public Oficial(int matricula, Map<Integer, Date[]> listaEntradas, Date[] estancia) {
		super(matricula);
		this.listaEstancia = new HashMap<>();
		this.estancia = new Date[2];
	}

    
	public short asociarEstancia(int matricula, Date entrada, Date salida) {
		short codigo = 0;
		estancia[0] = entrada;
		estancia[1] = salida;
		listaEstancia.put(matricula, estancia);
		return codigo;

	}


	
	

}
