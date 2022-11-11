import java.util.Date;

public class Estancia {
	
	
	private Date entrada;
	private Date Salida;
	

	
	public Estancia(Date entrada, Date salida) {
		this.entrada = entrada;
		Salida = salida;
	}
	
	
	public Date getEntrada() {
		return entrada;
	}
	public void setEntrada(Date entrada) {
		this.entrada = entrada;
	}
	public Date getSalida() {
		return Salida;
	}
	public void setSalida(Date salida) {
		Salida = salida;
	}
	
	
}
