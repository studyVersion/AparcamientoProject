import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Map.Entry;

public class Main {
	static Scanner sc = new Scanner(System.in);

	public static void menu() {
		System.out.println(" _____________________________________");
		System.out.println("| 1. Registra entrada                 |");
		System.out.println("| 2. Registra salida                  |");
		System.out.println("| 3. Da de alta vehículo oficial      |");
		System.out.println("| 4. Da de alta vehículo de residente |");
		System.out.println("| 5. Comienza mes                     |");
		System.out.println("| 6. Pagos de residentes              |");
		System.out.println("|_____________________________________|");
	}

	public static void main(String[] args) throws ParseException {

		Aparcamiento parking = new Aparcamiento();
		short option = 0;
		short codigo = 0;
		int matricula = 0;

		while (true) {
			menu();
			option = Short.valueOf(sc.nextLine());

			if (option == 1) {
				System.out.println("Introduzca la matricula del coche que llega:");
				matricula = Integer.valueOf(sc.nextLine());

				System.out.println("Es correcta la matricula introducida? : " + matricula + " (y/n)");
				String respuesta = sc.nextLine().toLowerCase().trim();

				while (respuesta.equals("n")) {
					System.out.println("Nueva matricula: ");
					matricula = Integer.valueOf(sc.nextLine());
					System.out.println("Es correcta la matricula introducida? : " + matricula + " (y/n)");
					respuesta = sc.nextLine().toLowerCase().trim();
				}
				codigo = parking.registrarEntrada(matricula);
				if (codigo == 0) {
					System.out.println("Entrada de un vehículo existente en el sistema");
				} else if (codigo == 1) {
					System.out.println("Entrada de un vehiculo no residente");
				} else if (codigo == 2) {
					System.out.println("Este vehículo ya esta aparcado");
				}
				// System.out.println(parking.listaVehiculos.toString());
			}

			if (option == 2) {
				System.out.println("Introduzca la matricula del coche que sale: ");
				matricula = Integer.valueOf(sc.nextLine());
				parking.registrarSalida(matricula);
				if (parking.listaVehiculos.get(matricula) instanceof NoResidente) {
					System.out.println(parking.getImporte());
				}
			}
		}
	}
}
