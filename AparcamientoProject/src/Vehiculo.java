import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class  Vehiculo {

	protected int matricula;
	protected Date entrada;
	protected Date salida;
	protected boolean aparcado;
	protected Date[] estancia;
	// protected ArrayList<Estancia> estancias;

	public Vehiculo(int matricula) {
		this.matricula = matricula;
		this.entrada = null;
		this.salida = null;
		this.aparcado = false;
		this.estancia = new Date[2];
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

	public Date[] getEstancia() {

		return estancia;
	}

	public void setEstancia(Date entrada, Date salida) {
		this.estancia[0] = entrada;
		this.estancia[1] = salida;
	}

	// Generar fecha entrada en forma de String
	public String entradaString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String entrada = sdf.format(getEntrada());
		return entrada;
	}

	// Generar fecha salida en forma de String
	public String salidaString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String salida = sdf.format(getSalida());
		return salida;
	}

}