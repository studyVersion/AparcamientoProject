import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.TreeMap;

public class Aparcamiento {

	private HashMap<Integer, Vehiculo> listaVehiculos = new HashMap<>();
	 Map<Date[], Integer> listaEstancias = new HashMap<>();

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
			Date[] estancia = { coche.getEntrada(), coche.getSalida() };
			listaEstancias.put(estancia, matricula);

			if (coche instanceof Residente) {
				Residente residente = (Residente) coche;
				residente.sumaDuracionEstancia();
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
	//	Formatter fmt = new Formatter();  
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
    
	public void crearXML(Document doc) {
		
		
		Element root = doc.createElement("Entradas-salidas");
		doc.appendChild(root);
		
		for (Entry<Integer, Vehiculo> vehiculo : listaVehiculos.entrySet()) {
			if (listaEstancias.containsValue(vehiculo.getKey())) {
				Element coche = doc.createElement("Vehiculo");
				// add coche to root
				root.appendChild(coche);
				// add matricula attribute
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
	}//crearXML
	public void readXML() throws ParseException, SAXException, IOException, ParserConfigurationException {
		int matricula = 0;
		Date entrada = null;
		Date salida = null;
		 
	    File path = new File("C:\\Users\\Administrateur\\Desktop\\Entradas-salidas.xml");
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();			
			Document doc = documentBuilder.parse(path);
		 
		    doc.getDocumentElement().normalize();
		    
		    System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
	         
		    NodeList listVehiculos = doc.getElementsByTagName("Vehiculo");
            
         for (int temp = 0; temp < listVehiculos.getLength(); temp++) {

             Node node = listVehiculos.item(temp);
            // System.out.println();
             if (node.getNodeType() == Node.ELEMENT_NODE) {

                 Element vehiculo = (Element) node;

                 String nodeMatricula = vehiculo.getAttribute("matricula");
                 //System.out.println(nodeMatricula);
                 
                 matricula = Integer.parseInt(nodeMatricula);
                 
                 Node n = vehiculo.getChildNodes().item(0);
                 if (n instanceof Element) {
                   NodeList listEstancias = n.getChildNodes();
                 
//                 Element root = doc.getDocumentElement(); //Network
//                 for (int i = 0; i < root.getChildNodes().getLength(); i++) {
//                     Node n = root.getChildNodes().item(i);
//                     if (n instanceof Element) {
//                         NodeList nodes = n.getChildNodes();
//                         for (int j = 0; j < nodes.getLength(); j++) {
//                             Node theNode = nodes.item(j);
//                             if (theNode instanceof Element) {
//                                 System.out.println(((Element) theNode).getAttribute("id"));
//                             }
//                         }
//                     }
//                 }
                   
                   System.out.println(listEstancias.getLength());
                 for (int temp2 = 0; temp < listEstancias.getLength(); temp2++) {

                     Node node2 = listEstancias.item(temp2);
                     
                     if (node2.getNodeType() == Node.ELEMENT_NODE) {
                    	 Element estancia = (Element) node;
                    	 
                    	 String entradaNode = estancia.getElementsByTagName("entrada").item(0).getTextContent();
                    	 String salidaNode = estancia.getElementsByTagName("salida").item(0).getTextContent();
                         
                    	 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                         entrada = sdf.parse(entradaNode);
                         salida =  sdf.parse(salidaNode);
                     }
                     Date [] estancia = {entrada, salida};
//                     System.out.println(matricula);
//                     System.out.println(estancia);
                     listaEstancias.put(estancia, matricula);
                   }
                   //System.out.println(listaEstancias.size());
             }}}

	}//crearXML
	public void recuperarFechajes() throws ParserConfigurationException, TransformerException, SAXException, IOException, ParseException {
        File path = new File("C:\\Users\\Administrateur\\Desktop\\Entradas-salidas.xml");
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
	
		//Document doc = documentBuilder.parse(path);
		//readXML(doc);

		Document doc = documentBuilder.newDocument();
        //llamar al método para crear un xml
		crearXML(doc);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(doc);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult streamResult = new StreamResult(path);
		transformer.transform(domSource, streamResult);
	}

}