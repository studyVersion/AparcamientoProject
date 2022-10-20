import java.text.ParseException;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main2 {
	public static void main(String[] args) throws ParseException {
		Scanner sc = new Scanner(System.in);
		Aparcamiento parking = new Aparcamiento();
		NoResidente a = new NoResidente(444);
		parking.listaVehiculos.put(444, a);
		while(true) {
		System.out.println("choice");
		int option = Integer.valueOf(sc.nextLine());
		if(option ==1) {
		parking.registrarEntrada(444);
		}
		if (option == 2) {
		parking.registrarSalida(444);
	
			System.out.println(parking.generarImporte(444));
		
		}
		
			}
}
}
