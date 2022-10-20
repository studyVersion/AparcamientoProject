import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class Aparcamiento {

	HashMap<Integer, Vehiculo> listaVehiculos = new HashMap<>();
	static Date entradaAparcamiento = null;
	static Date salidaAparcamiento = null;
	private static double importe= 0;

	public Aparcamiento() {
		this.listaVehiculos = new HashMap<>();
	}

	// Generar nueva fecha en un String con formato
	public String generarNuevaHora() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date fecha = new Date();
		String fechaString = sdf.format(fecha);
		return fechaString;

	}// generarNuevaHora

	/*
	 * Si el código es 0, la entrada ha sido registrada, si el código es 1, no se ha
	 * encontrado la matrícula entonces no residente, si el código es 2, el vehículo está aparcado.
	 */
	public short registrarEntrada(int matricula) throws ParseException {
		short codigo = 0;

		if (!listaVehiculos.containsKey(matricula)) {
			NoResidente noResid = new NoResidente(matricula);
			noResid.setAparcado(true);
			listaVehiculos.put(matricula, noResid);
			noResid.setEntrada(new Date());
			entradaAparcamiento = noResid.getEntrada();
			codigo = 1;

		} else if (listaVehiculos.get(matricula).isAparcado()) {
			codigo = 2;
		} else {
			for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
				if (vehiculo.getKey().equals(matricula)) {
					// entradaAparcamiento = new Date();
					vehiculo.getValue().setEntrada(new Date());
					entradaAparcamiento = vehiculo.getValue().getEntrada();
					vehiculo.getValue().setAparcado(true);
				}
			}
		}

		return codigo;

	}// registrarEntrada

	public short registrarSalida(int matricula) {
		short codigo = 0;

		if (!listaVehiculos.containsKey(matricula)) {
			codigo = 1;
		} else if (!listaVehiculos.get(matricula).isAparcado()) {
			codigo = 2;
		} else {
			for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {

				if (vehiculo.getKey().equals(matricula)) {

					vehiculo.getValue().setSalida(new Date());
					salidaAparcamiento = vehiculo.getValue().getSalida();

					if (vehiculo.getValue() instanceof Oficial) {
						Oficial vehiculoOficial = (Oficial) vehiculo.getValue();
						vehiculoOficial.asociarEstancia(matricula, entradaAparcamiento, salidaAparcamiento);
						vehiculoOficial.setAparcado(false);
						break;

					} else if (vehiculo.getValue() instanceof Residente) {
						Residente vehiculoResid = (Residente) vehiculo.getValue();
						vehiculoResid.sumaDuracionEstancia(entradaAparcamiento, salidaAparcamiento);
						vehiculoResid.setAparcado(false);
						break;
					} else {
						NoResidente vehiculoNoResid = (NoResidente) vehiculo.getValue();
						importe =  vehiculoNoResid.generarImporte(entradaAparcamiento, salidaAparcamiento);
						vehiculoNoResid.setAparcado(false);
						listaVehiculos.remove(matricula);
						break;
					}
				}
			}
		}
		return codigo;
	}
	
	public static double getImporte() {
		return importe;
	}

	public static void setImporte(double importe) {
		Aparcamiento.importe = importe;
	}

	public String generarImporte(int matricula) {
		
		String payement = "";
		if(listaVehiculos.containsKey(matricula)){
			payement =listaVehiculos.get(matricula).toString();
		}
		return payement;
		
		
	}
	// si el código es 0 el vehículo ha sido añadido, si el código es -1 el vehículo ya existe.
	public short darAltaOficial(int matricula) {
		short codigo = 0;
		if (!listaVehiculos.containsKey(matricula)) {
			Oficial vehiculo = new Oficial(matricula);
			listaVehiculos.put(matricula, vehiculo);
		}else {
			codigo = -1;
		}
		
		return codigo;
		
	}//darAltaOficial
	
	
	// si el código es 0 el vehículo ha sido añadido, si el código es -1 el vehículo ya existe.
		public short darAltaResidente(int matricula) {
			short codigo = 0;
			if (!listaVehiculos.containsKey(matricula)) {
				Residente vehiculo = new Residente(matricula);
				listaVehiculos.put(matricula, vehiculo);
			}else {
				codigo = -1;
			}
			
			return codigo;
		}

}
