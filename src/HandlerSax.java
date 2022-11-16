
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlerSax extends DefaultHandler {

	// Lista para sostener el objeto vehiculos
	private HashMap<Integer, Vehiculo> vecList = null;
	private Residente residente = null;
	private Oficial oficial = null;
	private StringBuilder data = new StringBuilder();;

	// método getter para la lista de vehiculos
	public HashMap<Integer, Vehiculo> getEmpList() {
		return vecList;
	}

	boolean vecResid = false;
	boolean vecOficial = false;

	@Override
	public void startDocument() {
		vecList = new HashMap<>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		data.setLength(0);
		if (qName.equalsIgnoreCase("vehiculo_oficial")) {
			vecOficial = true;
		}
		if (qName.equalsIgnoreCase("vehiculo_residente")) {
			vecResid = true;
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (qName.equalsIgnoreCase("matricula")) {
			int matricula = Integer.valueOf(data.toString());
			if (vecOficial) {
				oficial = new Oficial(matricula);

				vecList.put(matricula, oficial);
				vecOficial = false;
			} else if (vecResid) {
				residente = new Residente(matricula);
				vecList.put(matricula, residente);
				vecResid = false;
			}
		}

	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));

	}
}