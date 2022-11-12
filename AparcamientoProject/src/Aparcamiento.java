import java.io.File;
import java.io.IOException;
import java.text.ParseException;
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
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Aparcamiento {

	private HashMap<Integer, Vehiculo> listaVehiculos = new HashMap<>();
	private Map<Date[], Integer> listaEstancias = new HashMap<>();

	public Aparcamiento() {
		this.listaVehiculos = new HashMap<>();
		this.listaEstancias = new HashMap<>();
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
			escribirFechajes();
		}
		return codigo;

	}// registrarEntrada

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
			Date[] estancia = { coche.getEntrada(), coche.getSalida() };
			listaEstancias.put(estancia, matricula);
			
			if (coche instanceof Oficial) {
				escribirFechajes();
			}
			if (coche instanceof Residente) {
				Residente residente = (Residente) coche;
				residente.sumaDuracionEstancia();
				escribirFechajes();
			} else if (coche instanceof NoResidente) {
				NoResidente noResidente = (NoResidente) coche;
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
	public short darAltaOficial(int matricula) throws Exception {
		short codigo = 0;
		if (!listaVehiculos.containsKey(matricula)) {
			Oficial vehiculo = new Oficial(matricula);
			listaVehiculos.put(matricula, vehiculo);
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
		int matricula = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String info = "";
		for (Entry<Date[], Integer> estancia : listaEstancias.entrySet()) {
			matricula = estancia.getValue();
			Vehiculo car = encontrarVehiculo(matricula);
			String entrada = sdf.format(estancia.getKey()[0]);
			String salida = sdf.format(estancia.getKey()[1]);

			if (car instanceof Oficial) {
				info = car.toString() + "\t\t" + entrada + "\t\s\s\s" + salida + "\n\n" + info;
			} else if (car instanceof Residente) {
				info = car.toString() + "\t\t" + entrada + "\t\s\s\s" + salida + "\n\n" + info;
				((Residente) car).setTiempoAcumulado(0);
			}
		}
		listaEstancias.clear();
		return info;

	}

	public String pagosResidentes() {
		String value = "";
		// Formatter fmt = new Formatter();
		for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
			if (vehiculo.getValue() instanceof Residente) {
				Residente res = (Residente) vehiculo.getValue();
//				fmt.format("%12d %25s %30s\n", res.getMatricula(), res.getTiempoAcumulado(), res.getPagoResidente()+" Euros\n");
//				value = fmt + value;
				value = "\t" + res.getMatricula() + "\t\t\t" + res.getTiempoAcumulado() + "\t\t\t"
						+ res.getPagoResidente() + " Euros\n\n" + value;
			}
		}
		return value;

	}// pagosResidente

	public boolean encontrarEstancia(Date[] estancia) {
		boolean existe = false;
		for (Entry<Date[], Integer> estancias : listaEstancias.entrySet()) {
			if (estancia[0].equals(estancias.getKey()[0]) && estancia[1].equals(estancias.getKey()[1])) {
				// if(estancias.getValue().equals(mat)) {
				existe = true;
			}
		}
		return existe;
	}

	public short leerXmlSax() {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		File fichero = new File("Vehiculos.xml");
		short gestionErrores = 0;
		if (fichero.exists()) {
			try {
				SAXParser saxParser = saxParserFactory.newSAXParser();
				HandlerSax handler = new HandlerSax();
				saxParser.parse(fichero, handler);
				// Get Employees list
				listaVehiculos = handler.getEmpList();
//	        //print employee information
//	        
//	        List<Vehiculo> result = handler.getResult();
				// System.out.println(aa.size());
				//System.out.println(listaVehiculos.size());
//			for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
//
//				System.out.println(vehiculo);
//
//			}

			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		} else {
			gestionErrores = -1;
		}
		return gestionErrores;
	}

	public short leerXmlDom() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		File ficheroXML = new File("Entradas-salidas.xml");
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
					String nodeMatricula = vehiculo.getAttribute("matricula");
					matricula = Integer.parseInt(nodeMatricula);
//					Vehiculo coche = new Vehiculo(matricula);
//					listaVehiculos.put(matricula, coche);

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
								salida = sdf.parse(salidaNode);

								Date[] estanciaDate = { entrada, salida };
								if (!encontrarEstancia(estanciaDate)) {
									listaEstancias.put(estanciaDate, matricula);
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

//		for (Entry<Date[], Integer> a : listaEstancias.entrySet()) {
//			System.out
//					.println(sdf.format(a.getKey()[0]) + " |||| " + sdf.format(a.getKey()[1]) + " ||| " + a.getValue());
//		}
	}// leerXML

	// crear los elementos de los vehículos para implementarlos en el escritor xml
	public void elementosVehiculos(Document doc) {
		Element root = doc.createElement("Vehiculos");
		doc.appendChild(root);
		//System.out.println(listaVehiculos.size());
		for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
			// System.out.println("HOLA");
			Vehiculo coche = encontrarVehiculo(vehiculo.getKey());
			if (coche instanceof Oficial) {
				// System.out.println("HOLA");
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
			if (listaEstancias.containsValue(vehiculo.getKey())) {
				Element coche = doc.createElement("Vehiculo");
				// añadir coche to root
				root.appendChild(coche);
				// añadir matricula attribute
				coche.setAttribute("matricula", vehiculo.getKey().toString());
				Element estancias = doc.createElement("estancias");
				coche.appendChild(estancias);
				for (Entry<Date[], Integer> estancia : listaEstancias.entrySet()) {
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
						salida.setTextContent(sdf.format(estancia.getKey()[1]));
						estanciaElement.appendChild(salida);
					}
				}
			}
		}
	}// crearElementos

	public void escribirFechajes() throws Exception {
		File path = new File("Entradas-salidas.xml");
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		Document doc = documentBuilder.newDocument();

		// llamar al método de los elementos para crear el xml
		elementosFechajes(doc);
		// llamar al método para escribir el xml
		escribirXLM(doc, path);

	}// escribirFechajes

	public void escribirVehiculos() throws Exception {
		File path = new File("Vehiculos.xml");
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		Document doc = documentBuilder.newDocument();

		// llamar al método de los elementos para crear el xml
		elementosVehiculos(doc);

		// llamar al método para escribir el xml
		escribirXLM(doc, path);

	}// escribirFechajes

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