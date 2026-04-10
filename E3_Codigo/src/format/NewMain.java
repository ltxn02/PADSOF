package format;

import java.util.Scanner;

import transactions.ShoppingCart;

public class NewMain {
	private static Scanner scanner = new Scanner(System.in);
	private static final String fileName = "rongero_data.dat";
	
	public static void main(String[] args) {
		init();
		mainLoop();
		close();
	}
	
	public static void init() {
		logic.Application.cargarDatos(fileName);
		System.out.println(MenuBuilder.welcomeMenu());
	}
	
	public static void mainLoop() {
		boolean exitApp = false;
		
		while(!exitApp) {
			int option = scanner.nextInt();
			
			switch (option) {
				case 1:
					// verCatalogoInvitado();
					break;
				case 2:
					// login()
					break;
				case 3:
					// register()
					break;
				case 0:
					// exitApp = true;
					break;
				default:
					System.out.println(MenuBuilder.invalidMenuOption(null));
			}
		}
	}
	
	public static void close() {
		System.out.println(MenuBuilder.closingApp());
		
		logic.Application.guardarDatos(fileName);
		ShoppingCart.shutdownCleaner();
		scanner.close();
	}
}