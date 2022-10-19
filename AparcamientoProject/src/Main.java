import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Map.Entry;

public class Main {
	static Scanner sc = new Scanner(System.in);
	public static void menu() {
		System.out.println("1. Registra entrada");
		System.out.println("2. Registra salida");
		System.out.println("3. Da de alta vehículo oficial");
		System.out.println("4. Da de alta vehículo de residente");
		System.out.println("5. Comienza mes");
		System.out.println("6. Pagos de residentes");
	}
	
	public static void main(String[] args) throws ParseException {
	
		Aparcamiento a = new Aparcamiento();
		int option = 0;
		int matricula = 0;
		menu();
		option = Integer.valueOf(sc.nextLine());
		if (option == 1) {
			System.out.println( "matricula?");
			matricula = Integer.valueOf(sc.nextLine());
			System.out.println("¿Es correcta la matrícula introducida? : "+ matricula +" (y/n)");
			String op = sc.nextLine().toLowerCase().trim();
			
			while(op.equals("n")) {
				System.out.println( "nueva matricula?");
				matricula = Integer.valueOf(sc.nextLine());
				System.out.println("¿Es correcta la matrícula introducida? : "+ matricula +" (y/n)");
			    op = sc.nextLine().toLowerCase().trim();
			}
			a.registrarEntrada(matricula);
			for (Entry<Integer, Vehiculo> vehiculo : a.listaVehiculos.entrySet()) {
				System.out.println(vehiculo.getValue().isAparcado());
			}
			//System.out.println(a.registrarEntrada(matricula));
			}
	}
}
