import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Vehiculo {

	protected int matricula;
	protected Date entrada;
	protected Date salida;
	protected  boolean aparcado;
	
	public Vehiculo(int matricula) {
		this.matricula = matricula;
		this.entrada = null;
		this.salida = null;
		this.aparcado = false;
	}

	public int getMatricula() {
		return matricula;
	}

	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}

	public Date getEntrada() {
		return entrada;
	}

	public void setEntrada(Date entrada) {
		this.entrada = entrada;
	}

	public Date getSalida() {
		return salida;
	}

	public void setSalida(Date salida) {
		this.salida = salida;
	}

	public boolean isAparcado() {
		return aparcado;
	}

	public void setAparcado(boolean aparcado) {
		this.aparcado = aparcado;
	}
    
	// Generar fecha entrada
	public Date fechaEntrada(String fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		try {
			entrada = sdf.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return entrada;
	}
	
	
	// Generar fecha salida
	public Date fechaSalida(String fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		try {
			entrada = sdf.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return salida;
	}
	
	
	
	
	

}