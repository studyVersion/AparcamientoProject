import java.util.Date;

public abstract class Vehiculo {

	protected int matricula;
	//protected int tiempoEstacionado;
	protected int[] estancia;
	protected Date entrada;
	protected Date salida;
	// protected double cantidadPagar;

	public Vehiculo(int matricula) {
		this.matricula = matricula;
		this.estancia = new int [2];
		this.entrada = new Date();
		this.salida = null;
	}

	public int getMatricula() {
		return matricula;
	}

	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}

	public int getTiempoEstacionado() {
		return tiempoEstacionado;
	}

	public void setTiempoEstacionado(int tiempoEstacionado) {
		this.tiempoEstacionado = tiempoEstacionado;
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
}
