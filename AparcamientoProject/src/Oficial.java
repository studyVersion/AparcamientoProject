import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Oficial extends Vehiculo {
	

	public Oficial(int matricula) {
		super(matricula);

	}

   
	
	public String toString() {	
		return  "\t"+ getMatricula()+"\t\t   Oficial"+ "\s\s\t\t-\t\t\s\s-";
}
}