import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Oficial extends Vehiculo {
	
	//private Date[] estancia;

	public Oficial(int matricula) {
		super(matricula);
		//this.listaEstancia = new HashMap<>();
		//this.estancia = new Date[2];
	}

    
	/*public void asociarEstancia(Date entrada, Date salida) {
		estancia[0] = entrada;
		estancia[1] = salida;

	}*/


//	public Date[] getEstancia() {
//		return estancia;
//	}
//
//	public void setEstancia(Date entrada, Date salida) {
//		this.estancia[0] = entrada;
//		this.estancia[1] = salida;
//	
//	}
	
	public String toString() {
		
		return  entradaString()+ "\t  "+ salidaString();
	}
}
