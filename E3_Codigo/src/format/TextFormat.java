package format;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import users.*;

public class TextFormat {
	
	// ==============================================================================
	//  GENERACIÓN DE MENÚS:
	//  A continuación se incluyen los métodos implementados para crear el formato
	//  de los menús de usuario y el de bienvenida.
	// ==============================================================================
	
	private static String welcomeHeader() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("======================================\n");
		sb.append("         BIENVENIDO A RONGERO         \n");
		sb.append("======================================\n");
		
		return sb.toString();
	}
	
	static String welcomeInitMenu(User user, List<String> menuOptions) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(TextFormat.welcomeHeader());
		sb.append("\n");
		sb.append(TextFormat.initMenu(user, menuOptions));
		
		return sb.toString();
	}
	
	static String initMenu(User user, List<String> menuOptions) {
		StringBuilder sb = new StringBuilder();
		int size = menuOptions.size();
		String title = 
				(user instanceof Client) ? "Panel de cliente" :
				(user instanceof Employee) ? "Panel de empleado" :
				(user instanceof Manager) ? "Panel de gestor" :
				"Menú inicial";
		
		sb.append(TextFormat.buildHeader(title, user));
		
		for (int i = 0; i < size; i++) {
			if (i != size) {
				sb.append(TextFormat.buildMenuListElement(menuOptions.get(i), i+1));
			} else {
				sb.append(TextFormat.buildMenuListElement(menuOptions.get(i), 0));
			}
		}
		sb.append("Elige una opción (0-" + size +"): ");
		
		return sb.toString();
	}
	
	private static String genericErrorMessage() {
		return "[!] Opción no válida";
	}
	
	static String genericErrorMessage(String str) {
		return genericErrorMessage() + " " + str;
	}
	
	static String invalidMenuOption(User user, int maxOptions) {
		return genericErrorMessage() + " Por favor, introduce un número del 0 al " + maxOptions + ".";
	}
	
	static String closeAppMessage() {
		return "Guardando datos y cerrando la aplicación... ¡Hasta pronto!";
	}
	
	private static String buildHeader(String str, User user) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("--- ");
		sb.append(str.toUpperCase());
		sb.append(
				(user instanceof RegisteredUser) ? ": " + ((RegisteredUser)user).userPreview() :
				" (INVITADO)"
		);
		sb.append(" ---");
		
		return sb.toString();
	}
	
	private static String buildMenuListElement(String str, int i) {
		return (i + ".- " + str + "\n");	// "4.- Esto es un elemento de lista de ejemplo"
	}
	
	
	// ==============================================================================
	//  GENERACIÓN DE FORMATO PARA PRODUCTOS:
	//  A continuación se incluyen los métodos implementados que llamarán las clases
	// 	que lo necesiten para generar la visualización de productos.
	// ==============================================================================
	
	// GENERIC ITEMS
	
	public static String itemShort(String name, double price) {
		if(price == 0.0) {
			return name + " (Sin valorar)";
		}
		return name + " (" + String.format("%.2f €", price) + ")";
	}
	
	public static String itemBrief(String name, String description, double price) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(itemShort(name, price) + " | ");
		if(description.length() < 15) {
			sb.append(description);
		} else {
			sb.append(description.substring(0, 12) + "...");
		}
		
		return sb.toString();
	}
	
	public static String itemDetailed(String name, String description, double price, List<String> categories) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("  " + itemShort(name.toUpperCase(), price) +  "\n");
		sb.append("  > " + description.replace("\n", "\n    ") + "\n");
		if(categories.isEmpty()) { sb.append("  Sin categorías\n"); }
		else {
			sb.append("  ").append(String.join(" · ", categories)).append("\n");
		}
		
		return sb.toString();
	}
	
	// SECOND HAND PRODUCTS
	
	public static String secondHandDetailedBrowser(String itemString, String itemType, String condition) {
		return itemString + "\n  Tipo de producto: " + itemType + "\n  Estado del producto: " + condition.toUpperCase();
	}
	
	public static String secondHandDetailedOwner(String itemString, String itemType, String condition, String appraised, String offered) {
		return secondHandDetailedBrowser(itemString, itemType, condition) + "\n\n  Tasado: " + appraised + "\n  En oferta: " + offered;
	}
	
	// NEW PRODUCTS EMPLOYEE
	
	// public static String newProductDetailEmployee(String itemString, List<String> stock) {}
	
	
	/*static String visibleCatalogProducts(Catalog catalog) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("--- CATÁLOGO DE PRODUCTOS ---\n");
		
		// Hay que hacer llamada aquí a la función para construir el catálogo y añadirlo
		// a continuación del header.
		
		return sb.toString();
	}*/
	
	
}