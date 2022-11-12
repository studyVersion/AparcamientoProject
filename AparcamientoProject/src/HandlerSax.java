
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class HandlerSax extends DefaultHandler {

	// List to hold Employees object
	private HashMap<Integer,Vehiculo> vecList = null;
	private Residente resd = null;
	private Oficial ofi = null;
	private StringBuilder data = new StringBuilder();;

	// getter method for employee list
	public HashMap<Integer,Vehiculo> getEmpList() {
		return vecList;
	}

	boolean vecResid = false;
	boolean vecOficial = false;
//	boolean bGender = false;
//	boolean bRole = false;
    
	   @Override
	    public void startDocument() {
	        vecList = new HashMap<>();
	    }
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		data.setLength(0);
		if (qName.equalsIgnoreCase("vehiculo_oficial")) {
//			// create a new Employee and put it in Map	
			//ofi =new Oficial();
			vecOficial = true;
		} 
        if (qName.equalsIgnoreCase("vehiculo_residente")) {
			// set boolean values for fields, will be used in setting Employee variables

			//resd = new Residente();
			vecResid = true;
			//System.out.println("resd");
		}
		

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
			if (qName.equalsIgnoreCase("matricula")){
				int a = Integer.valueOf(data.toString());
				if (vecOficial) {
				//System.out.println(data.toString());
				ofi = new Oficial(a);
				//ofi.setMatricula(a);
				vecList.put(a, ofi);
				vecOficial= false;
				//System.out.println(vecList.containsValue(ofi));
			}else if (vecResid ) {
			
				
				//System.out.println(data.toString());
				resd = new Residente(a);
//				resd.setMatricula(a);
				vecList.put(a, resd);
				vecResid= false;
				//System.out.println(vecList.containsValue(resd));
			}
		}
		
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	
	  }
}