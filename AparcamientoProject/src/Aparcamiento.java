import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class Aparcamiento {

	HashMap<Integer, Vehiculo> listaVehiculos = new HashMap<>();
	static Date entradaAparcamiento = null;
	static Date salidaAparcamiento = null;

	public Aparcamiento() {
		this.listaVehiculos = new HashMap<>();
	}

	// Generar nueva fecha en un String con formato
	public String generarNuevaHora() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date entrada = new Date();
		String fecha = sdf.format(entrada);
		return fecha;

	}// generarNuevaHora

	/*
	 * Si el código es 0, la entrada ha sido registrada, si el código es 1, no se ha
	 * encontrado la matrícula, si el código es 2, el vehículo está aparcado.
	 */
	public short registrarEntrada(int matricula) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String fecha = "";
		short codigo = 0;

		if (!listaVehiculos.containsKey(matricula)) {
			NoResidente noResid = new NoResidente(matricula);
			noResid.setAparcado(true);
			listaVehiculos.put(matricula, noResid);			
			fecha = generarNuevaHora();
			entradaAparcamiento = sdf.parse(fecha);
			codigo = 1;
			
		} else if (listaVehiculos.get(matricula).isAparcado()) {
			codigo = 2;
		} else {
			for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
				if (vehiculo.getKey().equals(matricula)) {
					fecha = generarNuevaHora();
					entradaAparcamiento = vehiculo.getValue().fechaEntrada(fecha);
					vehiculo.getValue().setAparcado(true);
				}
			}
		}

		return codigo;

	}// registrarEntrada

	public short registrarSalida(int matricula) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		short codigo = 0;

		if (!listaVehiculos.containsKey(matricula)) {
			codigo = 1;
		} else if (!listaVehiculos.get(matricula).isAparcado()) {
			codigo = 2;
		} else {
			for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
				if (vehiculo.getKey().equals(matricula)) {
					String fecha = generarNuevaHora();

					if (vehiculo.getValue() instanceof Oficial) {
						salidaAparcamiento = vehiculo.getValue().fechaSalida(fecha);
						Oficial vOficial = (Oficial) vehiculo.getValue();
						codigo = vOficial.asociarEstancia(matricula, entradaAparcamiento, salidaAparcamiento);
						vOficial.setAparcado(false);
						break;

					} else if (vehiculo.getValue() instanceof Residente) {
                       
					}
				}
			}
		}
		return codigo;
	}

}
