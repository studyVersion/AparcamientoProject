import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Aparcamiento {

	private HashMap<Integer, Vehiculo> listaVehiculos = new HashMap<>();

	public Aparcamiento() {

		this.listaVehiculos = new HashMap<>();

	}

	// encontrar el coche con la imatriculación específicada
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
	public short registrarEntrada(int matricula) throws Exception {
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
			Date[] estancia = { coche.getEntrada(), null };
			coche.listaEstancias.put(estancia, matricula);
			escribirFechajes();
		}
		return codigo;

	}// registrarEntrada

	// eliminar las estancias sin salidas para añadir una enstancia completa
	public boolean eliminarEstancia(int matricula, Date fecha) {
		boolean existe = false;
		Vehiculo coche = encontrarVehiculo(matricula);
		Iterator<Entry<Date[], Integer>> it = coche.listaEstancias.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Date[], Integer> item = it.next();
			if (item.getKey()[0].equals(fecha)) {
				it.remove();
				existe = true;
			}
		}
		return existe;

	}// eliminateEstancia

	/*
	 * Si el código es 0, la salida ha sido registrada, si el código es 1, no se ha
	 * encontrado ningun coche con esta matricula, si el código es 2, el vehículo no
	 * está aparcado, si el codigo es 3 se genera el importe.
	 */
	public short registrarSalida(int matricula) throws Exception {
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
			// elimina la estancia sin salida si hay, y anade una nueva
			if (eliminarEstancia(matricula, coche.getEntrada())) {
				Date[] estancia = { coche.getEntrada(), coche.getSalida() };
				coche.listaEstancias.put(estancia, matricula);
			}

			if (coche instanceof Oficial) {
				// escribe en el xml de fechajes
				escribirFechajes();
			} else if (coche instanceof Residente) {
				Residente resid = (Residente) coche;
				// if(resid.getEstancia()[1]!= null) {
				resid.sumaDuracionEstancia(resid.getEstancia());
				// }
				// escribe en el xml de fechajes
				escribirFechajes();

			} else if (coche instanceof NoResidente) {
				NoResidente noResidente = (NoResidente) coche;

				noResidente.calcularImporte(noResidente.getEstancia());
				codigo = 3;
			}
		}
		return codigo;

	}// registrarSalida

	// devolver el importe a pagar
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
	public short darAltaOficial(int matricula) throws Exception {
		short codigo = 0;
		if (!listaVehiculos.containsKey(matricula)) {
			Oficial vehiculo = new Oficial(matricula);
			listaVehiculos.put(matricula, vehiculo);
			// escribe en el xml de vehiculos
			escribirVehiculos();
		} else {
			codigo = -1;
		}

		return codigo;

	}// darAltaOficial

	// si el código es 0 el vehículo ha sido añadido, si el código es -1 el vehículo
	// ya existe.
	public short darAltaResidente(int matricula) throws Exception {
		short codigo = 0;
		if (!listaVehiculos.containsKey(matricula)) {
			Residente vehiculo = new Residente(matricula);
			listaVehiculos.put(matricula, vehiculo);
			escribirVehiculos();
		} else {
			codigo = -1;
		}
		return codigo;

	}// darAltaResidente

	public String comienzaMes() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String entrada = "";
		String salida = "";
		String info = "";

		for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
			Vehiculo car = vehiculo.getValue();
			if (car instanceof Oficial) {
				for (Entry<Date[], Integer> estancia : car.listaEstancias.entrySet()) {
					entrada = sdf.format(estancia.getKey()[0]);
					if (estancia.getKey()[1] != null) {
						salida = sdf.format(estancia.getKey()[1]);
					}
					info = car.toString() + "\t\t" + entrada + "\t\s\s\s" + salida + "\n\n" + info;
				}
			} else if (car instanceof Residente) {
				for (Entry<Date[], Integer> estancia : car.listaEstancias.entrySet()) {
					Residente resd = (Residente) car;
					entrada = sdf.format(estancia.getKey()[0]);
					if (estancia.getKey()[1] != null) {
						salida = sdf.format(estancia.getKey()[1]);
					}
					info = car.toString() + resd.tiempoEstancia(estancia.getKey()) + "\t\t" + entrada + "\t\s\s\s"
							+ salida + "\n\n" + info;
					// restablecer el tiempo acumilado a 0
					resd.setTiempoAcumulado(0);
				}
				// eliminar todas las estancias
				car.listaEstancias.clear();
			}
		}
		return info;
	}

	public String pagosResidentes() {
		String value = "";
		try (Formatter fmt = new Formatter()) {
			for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
				if (vehiculo.getValue() instanceof Residente) {
					Residente res = (Residente) vehiculo.getValue();
					// crear un formato para que se vea bien
					fmt.format("%12s %22s %33s\n", res.getMatricula(), res.getTiempoAcumulado(),
							res.getPagoResidente() + " Euros\n");
					value = fmt + value;
				}
			}
		}
		return value;

	}// pagosResidente

	// encontrar enstancias en la lista de estacia segun date[] y matricula
	public boolean encontrarEstancia(Date[] estancia, int matricula) {
		boolean existe = false;
		Vehiculo car = encontrarVehiculo(matricula);
		for (Entry<Date[], Integer> estancias : car.listaEstancias.entrySet()) {
			if (estancia[0].equals(estancias.getKey()[0]) && estancia[1].equals(estancias.getKey()[1])) {
				existe = true;
			}
		}
		return existe;
	}// encontrarEstancia

	// leer fichero xml segun SAX
	public short leerXmlSax() {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		File fichero = new File("C:\\Users\\2DAM\\eclipse-workspace\\Aparcamiento\\Vehiculos.xml");
		short gestionErrores = 0;
		if (fichero.exists()) {
			try {
				SAXParser saxParser = saxParserFactory.newSAXParser();
				HandlerSax handler = new HandlerSax();
				saxParser.parse(fichero, handler);
				listaVehiculos = handler.getEmpList();
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		} else {
			gestionErrores = -1;
		}
		return gestionErrores;
	}// leerXmlSax

	// leer fichero xml segun DOM
	public short leerXmlDom() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		File ficheroXML = new File("C:\\Users\\2DAM\\eclipse-workspace\\Aparcamiento\\Entradas-salidas.xml");
		int matricula = 0;
		short gestionErrores = 0;
		Date entrada = null;
		Date salida = null;

		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		Document doc = documentBuilder.parse(ficheroXML);
		doc.getDocumentElement().normalize();

		NodeList listVehiculos = doc.getElementsByTagName("Vehiculo");

		if (ficheroXML.exists()) {
			for (int i = 0; i < listVehiculos.getLength(); i++) {
				Node nodeVehiculo = listVehiculos.item(i);

				if (nodeVehiculo.getNodeType() == Node.ELEMENT_NODE) {

					Element vehiculo = (Element) nodeVehiculo;
					// guardar número de imatriculación
					matricula = Integer.parseInt(vehiculo.getAttribute("matricula"));

					NodeList estancias = doc.getElementsByTagName("estancias");
					Element element = (Element) estancias.item(i);

					if (element.getNodeType() == Node.ELEMENT_NODE) {
						Node listEstancias = (Node) element.getChildNodes();

						for (int j = 0; j < listEstancias.getChildNodes().getLength(); j++) {

							Node nodeEstancia = listEstancias.getChildNodes().item(j);

							if (nodeEstancia.getNodeType() == Node.ELEMENT_NODE) {
								Element estancia = (Element) nodeEstancia;

								String entradaNode = estancia.getElementsByTagName("entrada").item(0).getTextContent();
								String salidaNode = estancia.getElementsByTagName("salida").item(0).getTextContent();

								entrada = sdf.parse(entradaNode);
								if (!salidaNode.equals("")) {
									salida = sdf.parse(salidaNode);
								}
								// registrar una estancia
								Date[] estanciaDate = { entrada, salida };
								if (!encontrarEstancia(estanciaDate, matricula)) {
									Vehiculo car = encontrarVehiculo(matricula);
									if (car instanceof Residente) {
										Residente residente = (Residente) car;
										residente.sumaDuracionEstancia(estanciaDate);
									}
									if (salidaNode.equals("")) {
										car.setEntrada((estanciaDate[0]));
										car.setAparcado(true);
									}
									car.listaEstancias.put(estanciaDate, matricula);
								}
							}
						}
					}
				}
			}
		} else {
			gestionErrores = -1;
		}

		return gestionErrores;
	}// leerXmlDom

	// crear los elementos de los vehículos para implementarlos en el escritor xml
	public void elementosVehiculos(Document doc) {
		Element root = doc.createElement("Vehiculos");
		doc.appendChild(root);
		for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
			Vehiculo coche = vehiculo.getValue();
			if (coche instanceof Oficial) {
				Element oficial = doc.createElement("vehiculo_oficial");
				root.appendChild(oficial);
				Element matricula = doc.createElement("matricula");
				matricula.setTextContent(vehiculo.getKey().toString());
				oficial.appendChild(matricula);

			} else if (coche instanceof Residente) {
				Element residente = doc.createElement("vehiculo_residente");
				root.appendChild(residente);
				Element matricula = doc.createElement("matricula");
				matricula.setTextContent(vehiculo.getKey().toString());
				residente.appendChild(matricula);
			}
		}
	}

	// crear los elementos de los fechajes para implementarlos en el escritor xml
	public void elementosFechajes(Document doc) {
		Element root = doc.createElement("Entradas-salidas");
		doc.appendChild(root);

		for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
			if (vehiculo.getValue().listaEstancias.containsValue(vehiculo.getKey())) {
				Element coche = doc.createElement("Vehiculo");
				// añadir coche to root
				root.appendChild(coche);
				// añadir matricula attribute
				coche.setAttribute("matricula", vehiculo.getKey().toString());
				Element estancias = doc.createElement("estancias");
				coche.appendChild(estancias);
				Vehiculo car = vehiculo.getValue();
				for (Entry<Date[], Integer> estancia : car.listaEstancias.entrySet()) {
					if (estancia.getValue().equals(vehiculo.getKey())) {
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
						if (estancia.getKey()[1] != null) {
							salida.setTextContent(sdf.format(estancia.getKey()[1]));
						}
						estanciaElement.appendChild(salida);
					}
				}
			}
		}
	}// crearElementos

	// escribir el xml de las fechas
	public void escribirFechajes() throws Exception {
		File path = new File("C:\\Users\\2DAM\\eclipse-workspace\\Aparcamiento\\Entradas-salidas.xml");
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		Document doc = documentBuilder.newDocument();

		// llamar al método de los elementos para crear el xml
		elementosFechajes(doc);
		// llamar al método para escribir el xml
		escribirXLM(doc, path);

	}// escribirFechajes

	// escribir el xml de los vehiculos
	public void escribirVehiculos() throws Exception {
		File path = new File("C:\\Users\\2DAM\\eclipse-workspace\\Aparcamiento\\Vehiculos.xml");
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		Document doc = documentBuilder.newDocument();

		// llamar al método de los elementos para crear el xml
		elementosVehiculos(doc);

		// llamar al método para escribir el xml
		escribirXLM(doc, path);

	}// escribirFechajes

	// metodo para escribir correctamente cualquier xml
	public void escribirXLM(Document doc, File path) throws Exception {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(doc);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult streamResult = new StreamResult(path);
		transformer.transform(domSource, streamResult);
	}
}