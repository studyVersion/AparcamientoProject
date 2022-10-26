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
		System.out.println("| 3. Da de alta vehiculo oficial      |");
		System.out.println("| 4. Da de alta vehiculo de residente |");
		System.out.println("| 5. Comienza mes                     |");
		System.out.println("| 6. Pagos de residentes              |");
		System.out.println("|_____________________________________|");
	}

	public static void main(String[] args) throws ParseException{

		Aparcamiento parking = new Aparcamiento();
		short option = 0;
		short mensajeConsola = 0;
		int matricula = 0;

		while (true) {
			menu();
			option = Short.valueOf(sc.nextLine());

			if (option == 1) {
				System.out.println("Introduzca la matricula del coche que llega:");
				matricula = Integer.valueOf(sc.nextLine());

//				System.out.println("Es correcta la matricula introducida: " + matricula + " (S/N)");
//				String respuesta = sc.nextLine().toLowerCase().trim();
//
//				while (respuesta.equals("n")) {
//					System.out.println("Nueva matricula: ");
//					matricula = Integer.valueOf(sc.nextLine());
//					System.out.println("Es correcta la matricula introducida: " + matricula + " (S/N)");
//					respuesta = sc.nextLine().toLowerCase().trim();
//				}
				mensajeConsola = parking.registrarEntrada(matricula);
				if (mensajeConsola == 0) {
					System.out.println("Entrada de un vehiculo existente en el sistema");
				} else if (mensajeConsola == 1) {
					System.out.println("Entrada de un vehiculo no residente");
				} else if (mensajeConsola == 2) {
					System.out.println("Este vehiculo ya esta aparcado");
				}
				// System.out.println(parking.listaVehiculos.toString());
			}

			if (option == 2) {
				System.out.println("Introduzca la matricula del coche que sale: ");
				matricula = Integer.valueOf(sc.nextLine());
				mensajeConsola = parking.registrarSalida(matricula);
				 if (mensajeConsola == 0) {
					 System.out.println("La salida se ha completado con exito");
				 }else if(mensajeConsola == 1) {
					 System.out.println("No se encuentra ningun coche con esta matrícula");
				 }else if (mensajeConsola == 2) {
					 System.out.println("Este vehiculo no esta aparcado.");
				 }else if (mensajeConsola == 3) {
					 System.out.println(parking.generarImporte(matricula));
				 }
				
			}
			
			if (option == 3) {
				System.out.println("Introduzca la matricula del coche oficial a anadir:");
				matricula = Integer.valueOf(sc.nextLine());
				mensajeConsola = parking.darAltaOficial(matricula);
				if (mensajeConsola < 0) {
					System.out.println("Este vehiculo ya esta registrado, pruebe con otra matricula.");
				}else {
					System.out.println("El vehiculo esta registrado con exito");
				}
			}
			
			if (option == 4) {
				System.out.println("Introduzca la matricula del coche residente a anadir:");
				matricula = Integer.valueOf(sc.nextLine());
				mensajeConsola = parking.darAltaResidente(matricula);
				if (mensajeConsola < 0) {
					System.out.println("Este vehiculo ya esta registrado, pruebe con otra matricula.");
				}else {
					System.out.println("El vehiculo esta registrado con exito");
				}
			}
			if (option == 5) {
				System.out.println("-------------------------------------------------------------\n"+
                  	               "      Matricula      |      Entrada      |      Salida       \n"+
				                   "-------------------------------------------------------------\n");
				System.out.println(parking.comienzaMes());
			}
		}
	}
}
