import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.TreeMap;

public class Aparcamiento {

	private HashMap<Integer, Vehiculo> listaVehiculos = new HashMap<>();
	// private TreeMap<String, Integer> listaEstancias = new TreeMap<>();
	private TreeMap<Date[], Integer> lista = new TreeMap<>();

	public Aparcamiento() {
		this.listaVehiculos = new HashMap<>();
		this.lista = new TreeMap<>();
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

	}// encontrarVehiculo

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
			coche.setSalida(new Date());
			coche.setAparcado(false);
			coche.setEstancia(coche.getEntrada(), coche.getSalida());
			lista.put(coche.getEstancia(), matricula);
			// if (coche instanceof Oficial) {
			// Oficial oficial = (Oficial) coche;
			// oficial.setEstancia(oficial.getEntrada(), oficial.getSalida());
			// String estancia = "\t" + matricula + "\t\t" + oficial.toString();
			// listaEstancias.put(estancia, matricula);

			// }
			if (coche instanceof Residente) {
				Residente residente = (Residente) coche;
				// residente.setEstancia(residente.getEntrada(), residente.getSalida());
				residente.sumaDuracionEstancia();
				// String estancia = "\t" + matricula + "\t\t" + residente.toString();
				// listaEstancias.put(estancia, matricula);
			} else if (coche instanceof NoResidente) {
				NoResidente noResidente = (NoResidente) coche;
				// noResidente.setEstancia(noResidente.getEntrada(), noResidente.getSalida());
				noResidente.calcularImporte(noResidente.getEstancia());
				codigo = 3;
			}
		}
		return codigo;

	}// registrarSalida

	public String generarImporte(int matricula) {
		String payement = "";
		if (listaVehiculos.containsKey(matricula)) {
			payement = listaVehiculos.get(matricula).toString();
			listaVehiculos.remove(matricula);
		}
		return payement;

	}// generarImporte

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

	}// darAltaResidente

	public String comienzaMes() {
		int matricula = 0;
		String info = "";
		for (Entry<Date[], Integer> estancia : lista.entrySet()) {
			matricula = estancia.getValue();
			Vehiculo car = encontrarVehiculo(matricula);
			if (car instanceof Oficial) {
				info = "\t" + matricula + "\t\t" + car.toString() + "\n\n" + info;
			} else if (car instanceof Residente) {
				info = "\t" + matricula + "\t\t" + car.toString() + "\n\n" + info;
				((Residente) car).setTiempoAcumulado(0);
			}
		}
		lista.clear();
		return info;

	}

	public String pagosResidentes() {
		String value = "";
		for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
			if (vehiculo.getValue() instanceof Residente) {
				Residente res = (Residente) vehiculo.getValue();
				value = "\t" + res.getMatricula() + "\t\t\t" + res.getTiempoAcumulado() + "\t\t\t"
						+ res.getPagoResidente() + " Euros\n\n" + value;
			}
		}
		return value;

	}

	public void recuperarFechajes() throws ParserConfigurationException, TransformerException {

		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		Document doc = documentBuilder.newDocument();

		Element root = doc.createElement("Entradas-salidas");
		doc.appendChild(root);
		for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
			if (lista.containsValue(vehiculo.getKey())) {
				Element coche = doc.createElement("Vehiculo");
				// add staff to root
				root.appendChild(coche);
				// add xml attribute
				coche.setAttribute("matricula", vehiculo.getKey().toString());

				Element estancias = doc.createElement("estancias");
				coche.appendChild(estancias);
				for (Entry<Date[], Integer> estancia : lista.entrySet()) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					// estancia
					Element estanciaElement = doc.createElement("estancia");
					estancias.appendChild(estanciaElement);

					// entrada
					Element entrada = doc.createElement("entrada");
					entrada.setTextContent(sdf.format(estancia.getKey()[0]));
					estanciaElement.appendChild(entrada);

					// salida
					Element salida = doc.createElement("salida");
					salida.setTextContent(sdf.format(estancia.getKey()[1]));
					estanciaElement.appendChild(salida);

				}
			}
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(doc);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult streamResult = new StreamResult(
				new File("C:\\Users\\Administrateur\\Desktop\\Entradas-salidas.xml"));
		transformer.transform(domSource, streamResult);
	}

}
