
public class Oficial extends Vehiculo {
	public Oficial(int matricula) {
		super(matricula);

	}

	public String toString() {

		return "\t" + getMatricula() + "\t\tOficial   " + "\s\s\t\t-\t\t\s\s-";
	}
}