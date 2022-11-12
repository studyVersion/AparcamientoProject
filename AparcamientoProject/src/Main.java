import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import java.util.Map.Entry;

public class Main {
	static Scanner sc = new Scanner(System.in);

	public static void menu() {
		System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		System.out.println("| 1. Registra entrada                 |");
		System.out.println("| 2. Registra salida                  |");
		System.out.println("| 3. Da de alta vehiculo oficial      |");
		System.out.println("| 4. Da de alta vehiculo de residente |");
		System.out.println("| 5. Comienza mes                     |");
		System.out.println("| 6. Pagos de residentes              |");
		System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
	}

	public static void main(String[] args) throws Exception {

		Aparcamiento parking = new Aparcamiento();
		short option = 0;
		short mensajeConsola = 0;
		int matricula = 0;

		while (true) {
			menu();
			try {
				option = Short.parseShort(sc.nextLine());

				if (option == 1) {
					System.out.println("Introduzca la matricula del coche que llega:");
					matricula = Integer.parseInt(sc.nextLine());

					mensajeConsola = parking.registrarEntrada(matricula);
					if (mensajeConsola == 0) {
						System.out.println("Entrada de un vehiculo existente en el sistema");
					} else if (mensajeConsola == 1) {
						System.out.println("Entrada de un vehiculo no residente");
					} else if (mensajeConsola == 2) {
						System.out.println("Este vehiculo ya esta aparcado");
					}
				}

				if (option == 2) {
					System.out.println("Introduzca la matricula del coche que sale: ");
					matricula = Integer.parseInt(sc.nextLine());
					mensajeConsola = parking.registrarSalida(matricula);
					if (mensajeConsola == 0) {
						System.out.println("La salida se ha completado con exito");
					} else if (mensajeConsola == 1) {
						System.out.println("No se encuentra ningun coche con esta matrícula");
					} else if (mensajeConsola == 2) {
						System.out.println("Este vehiculo no esta aparcado.");
					} else if (mensajeConsola == 3) {
						System.out.println(parking.generarImporte(matricula));
					}

				}

				if (option == 3) {
					System.out.println("Introduzca la matricula del coche oficial a anadir:");
					matricula = Integer.parseInt(sc.nextLine());
					mensajeConsola = parking.darAltaOficial(matricula);
					if (mensajeConsola < 0) {
						System.out.println("Este vehiculo ya esta registrado, pruebe con otra matricula.");
					} else {
						System.out.println("Vehiculo oficial registrado con exito");
					}
				}

				if (option == 4) {
					System.out.println("Introduzca la matricula del coche residente a anadir:");
					matricula = Integer.parseInt(sc.nextLine());
					mensajeConsola = parking.darAltaResidente(matricula);
					if (mensajeConsola < 0) {
						System.out.println("Este vehiculo ya esta registrado, pruebe con otra matricula.");
					} else {
						System.out.println("Vehiculo Residente registrado con exito");
					}
				}
				if (option == 5) {
					System.out.println(
							          "---------------------+----------------+-------------------+------------------+------------------------+-------------------------+\n"
									+ "      Matricula      |      Tipo      |  Tiempo Estancia  |   Tiempo Total   |         Entrada        |         Salida          |\n"
									+ "---------------------+----------------+-------------------+------------------+------------------------+-------------------------+");
					System.out.println(parking.comienzaMes());
				}
				if (option == 6) {
					try (BufferedWriter bw = new BufferedWriter(
							new FileWriter(new File("C:\\Users\\Administrateur\\Desktop\\output2.txt")))) {
						bw.write( "---------------------+---------------------------+----------------------+\n"
								+ "      Matricula      | Tiempo estacionado (min.) |   Cantidad a pagar   |\n"
								+ "---------------------+---------------------------+----------------------+\n"
								+ parking.pagosResidentes());

						bw.close();
					} catch (FileNotFoundException ex) {
						System.out.println(ex.toString());
					}
					System.out.println("---------------------+---------------------------+----------------------+\n"
							         + "      Matricula      | Tiempo estacionado (min.) |   Cantidad a pagar   |\n"
							         + "---------------------+---------------------------+----------------------+");
					System.out.println(parking.pagosResidentes());
				}

				if (option == 7) {
					mensajeConsola = parking.leerXmlDom();
					
					if (mensajeConsola < 0) {
						System.out.println("Archivo xml no encontrado!");
					}else {
						System.out.println("El documento ha sido leído con éxito.");
					}
				}
				if (option == 8) {
					mensajeConsola = parking.leerXmlSax();
					
					if (mensajeConsola < 0) {
						System.out.println("Archivo xml no encontrado!");
					}else {
						System.out.println("El documento ha sido leído con éxito.");
					}
				}
				
			} catch (NumberFormatException e) {
				System.out.println(e);
			}
		}
	}
}
