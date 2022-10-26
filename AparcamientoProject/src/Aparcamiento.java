import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Aparcamiento {

	HashMap<Integer, Vehiculo> listaVehiculos = new HashMap<>();
	private TreeMap<Integer, Date[]> listaEstancias = new TreeMap<>();

	public Aparcamiento() {
		this.listaVehiculos = new HashMap<>();
		this.listaEstancias = new TreeMap<>();
	}

	// Generar nueva fecha en un String con formato
	public String generarNuevaHora() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date fecha = new Date();
		String fechaString = sdf.format(fecha);
		return fechaString;

	}// generarNuevaHora

	public Vehiculo encontrarVehiculo(int matricula) {
		Vehiculo coche = null;
		for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
			if (vehiculo.getKey().equals(matricula)) {
				coche = vehiculo.getValue();
			}
		}

		return coche;

	}

	/*
	 * Si el código es 0, la entrada ha sido registrada, si el código es 1, no se ha
	 * encontrado la matrícula entonces no residente, si el código es 2, el vehículo
	 * está aparcado.
	 */
	public short registrarEntrada(int matricula) throws ParseException {
		short codigo = 0;
		Vehiculo coche = encontrarVehiculo(matricula);
		if (coche == null) {
			NoResidente noResid = new NoResidente(matricula);
			noResid.setAparcado(true);
			noResid.setEntrada(new Date());
			listaVehiculos.put(matricula, noResid);
			codigo = 1;

		} else if (coche.isAparcado()) {
			codigo = 2;
		} else {
			coche.setEntrada(new Date());
			coche.setAparcado(true);

		}

		return codigo;

	}// registrarEntrada

	public short registrarSalida(int matricula) {
		short codigo = 0;
		Vehiculo coche = encontrarVehiculo(matricula);

		if (coche == null) {
			codigo = 1;
		} else if (!coche.isAparcado()) {
			codigo = 2;
		} else {
			// for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {

			// if (vehiculo.getKey().equals(matricula)) {

			coche.setSalida(new Date());
			coche.setAparcado(false);
			// salidaAparcamiento = vehiculo.getValue().getSalida();

			if (coche instanceof Oficial) {
				Oficial oficial = (Oficial) coche;
				// v_Oficial.setAparcado(false);
				oficial.setEstancia(oficial.getEntrada(), oficial.getSalida());
				listaEstancias.put(matricula, oficial.getEstancia());

			} else if (coche instanceof Residente) {
				Residente residente = (Residente) coche;
				// v_residente.setAparcado(false);
				residente.setEstancia(residente.getEntrada(), residente.getSalida());
				residente.sumaDuracionEstancia(residente.getEstancia());
				listaEstancias.put(matricula, residente.getEstancia());

			} else {
				NoResidente noResidente = (NoResidente) coche;
				noResidente.setEstancia(noResidente.getEntrada(), noResidente.getSalida());
				noResidente.calcularImporte(noResidente.getEstancia());
				codigo = 3;
				// v_noResidente.setAparcado(false);

			}
		}

		return codigo;
	}

	public String generarImporte(int matricula) {
		String payement = "";
		if (listaVehiculos.containsKey(matricula)) {
			payement = listaVehiculos.get(matricula).toString();
			listaVehiculos.remove(matricula);
		}
		return payement;

	}

	// si el código es 0 el vehículo ha sido añadido, si el código es -1 el vehículo
	// ya existe.
	public short darAltaOficial(int matricula) {
		short codigo = 0;
		if (!listaVehiculos.containsKey(matricula)) {
			Oficial vehiculo = new Oficial(matricula);
			listaVehiculos.put(matricula, vehiculo);
		} else {
			codigo = -1;
		}

		return codigo;

	}// darAltaOficial

	// si el código es 0 el vehículo ha sido añadido, si el código es -1 el vehículo
	// ya existe.
	public short darAltaResidente(int matricula) {
		short codigo = 0;
		if (!listaVehiculos.containsKey(matricula)) {
			Residente vehiculo = new Residente(matricula);
			listaVehiculos.put(matricula, vehiculo);
		} else {
			codigo = -1;
		}

		return codigo;
	}

	public String comienzaMes() {
		int matricula = 0;
		String info="";
		for (Entry<Integer, Date[]> estancia : listaEstancias.entrySet()) {
			matricula = estancia.getKey();
			Vehiculo car = encontrarVehiculo(matricula);
			if (car instanceof Oficial) {
				info = info + "\n" +car.toString()+"\n";
				
			}

		}
		return info;

	}

}
