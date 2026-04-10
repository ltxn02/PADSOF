package format;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import users.*;

/**
 * Clase de utilidad para formatear toda la salida de texto de la aplicación.
 * Centraliza la generación de menús, listas de productos y mensajes.
 * 
 * @author Lidia Martin
 * @version 2.0
 */

public class TextFormat {
	
	
	// ================================================================================
	// 1. MENÚS PRINCIPALES Y HEADERS
	// ================================================================================
	
	/**
	 * Genera el encabezado de bienvenida de la aplicación.
	 * @return Cadena con el banner de bienvenida a Rongero
	 */
	static String welcomeHeader() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("======================================\n");
		sb.append("         BIENVENIDO A RONGERO         \n");
		sb.append("======================================\n");
		
		return sb.toString();
	}
	
	/**
	 * Genera el menú inicial de bienvenida (invitado sin autenticar).
	 * @param user Usuario actual (null para invitado)
	 * @param menuOptions Lista de opciones disponibles
	 * @return Menú formateado con header de bienvenida
	 */
	static String welcomeInitMenu(User user, List<String> menuOptions) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(TextFormat.welcomeHeader());
		sb.append("\n");
		sb.append(TextFormat.initMenu(user, menuOptions));
		
		return sb.toString();
	}
	
	/**
	 * Genera un menú genérico para cualquier usuario autenticado.
	 * Adapta el título según el tipo de usuario (Cliente, Empleado, Gestor).
	 * @param user Usuario actual
	 * @param menuOptions Lista de opciones del menú
	 * @return Menú formateado con opciones numeradas
	 */
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
	
	
	// ================================================================================
	// 2. HEADERS Y ELEMENTOS AUXILIARES DE MENÚ
	// ================================================================================
	
	/**
	 * Construye el encabezado con el título del menú y datos del usuario.
	 * @param title Título del menú
	 * @param user Usuario actual (muestra su preview si está autenticado)
	 * @return Encabezado formateado: "--- TÍTULO: usuario ---" o "--- TÍTULO (INVITADO) ---"
	 */
	static String buildHeader(String str, User user) {
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
	
	/**
	 * Construye un elemento de lista numerado para menús.
	 * @param str Texto del elemento
	 * @param i Número de opción
	 * @return Elemento formateado: "3.- Opción del menú"
	 */
	static String buildMenuListElement(String str, int i) {
		return (i + ".- " + str + "\n");	// "4.- Esto es un elemento de lista de ejemplo"
	}
	
	
	// ================================================================================
	// 3. MENSAJES DE ERROR, ÉXITO E INFORMACIÓN
	// ================================================================================
	
	/**
	 * Mensaje genérico de opción no válida.
	 * @return "[!] Opción no válida"
	 */
	static String genericErrorMessage() {
		return "[!] Opción no válida";
	}
	
	/**
	 * Mensaje de error genérico con contexto adicional.
	 * @param str Contexto o razón del error
	 * @return Mensaje de error concatenado
	 */
	static String genericErrorMessage(String str) {
		return genericErrorMessage() + " " + str;
	}
	
	/**
	 * Mensaje de error específico para entrada inválida en menús.
	 * @param user Usuario actual
	 * @param maxOptions Número máximo de opciones válidas
	 * @return Mensaje de error completo con instrucciones
	 */
	static String invalidMenuOption(User user, int maxOptions) {
		return genericErrorMessage() + " Por favor, introduce un número del 0 al " + maxOptions + ".";
	}
	
	/**
	 * Mensaje de cierre de la aplicación.
	 * @return Mensaje de despedida con indicación de guardado
	 */
	static String closeAppMessage() {
		return "Guardando datos y cerrando la aplicación... ¡Hasta pronto!";
	}
	
	
	// ================================================================================
	// 4. FORMATOS PARA ITEMS GENÉRICOS (Nombre, precio, descripción)
	// ================================================================================
	
	/**
	 * Muestra un item en formato muy breve: nombre y precio.
	 * @param name Nombre del producto
	 * @param price Precio en euros
	 * @return Formato: "Nombre (12.50 €)" o "Nombre (Sin valorar)" si precio es 0
	 */
	public static String itemShort(String name, double price) {
		if(price == 0.0) {
			return name + " (Sin valorar)";
		}
		return name + " (" + String.format("%.2f €", price) + ")";
	}
	
	/**
	 * Muestra un item en formato breve para listas: nombre, precio y descripción truncada.
	 * @param name Nombre del producto
	 * @param description Descripción (se trunca a 12 caracteres si es muy larga)
	 * @param price Precio en euros
	 * @return Formato: "Nombre (12.50 €) | Descripción breve..."
	 */
	public static String itemBrief(String name, String description, double price) {
		StringBuilder sb = new StringBuilder();
		int maxChar = 50;
		
		sb.append(itemShort(name, price) + " | ");
		if(name.length() + description.length() < maxChar) {
			sb.append(description);
		} else {
			sb.append(description.substring(0, maxChar - name.length() - 3) + "...");
		}
		
		return sb.toString();
	}
	
	/**
	 * Muestra un item en formato detallado: nombre, precio, descripción completa y categorías.
	 * @param name Nombre del producto
	 * @param description Descripción completa (preserva saltos de línea)
	 * @param price Precio en euros
	 * @param categories Lista de nombres de categorías
	 * @return Item formateado con viñetas, descripción envuelta y categorías
	 */
	public static String itemDetailed(String name, String description, double price, List<String> categories) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n" + centeredHeader(name.toUpperCase())).append("\n");
		sb.append("Precio: ").append(String.format("%.2f €", price)).append("\n");
		
		if (!categories.isEmpty()) {
			sb.append("#").append(String.join(" #", categories)).append("\n");
		}
		
		sb.append("> ").append(description.replace("\n", "\n  ")).append("\n");
		
		return sb.toString();
	}
	
	public static String centeredHeader(String name) {
	    int totalWidth = 50;
	    int nameWithSpaces = name.length() + 2; // Nombre + 1 espacio a cada lado
	    int remainingDashes = totalWidth - nameWithSpaces;
	    int leftDashes = remainingDashes / 2;
	    int rightDashes = remainingDashes - leftDashes;
	    
	    return "-".repeat(leftDashes) + " " + name + " " + "-".repeat(rightDashes);
	}
	
	
	// ================================================================================
	// 5. FORMATOS PARA PRODUCTOS DE SEGUNDA MANO (SecondHandProduct)
	// ================================================================================
	
	/**
	 * Muestra un producto de segunda mano con tipo y condición (vista del comprador).
	 * @param itemString Item en formato corto (nombre + precio)
	 * @param itemType Tipo de producto (COMIC, GAME, FIGURINE)
	 * @param condition Estado de conservación (PERFECTO, MUY_BUENO, etc.)
	 * @return Producto formateado con tipo y condición
	 */
	public static String secondHandDetailedBrowser(String itemString, String itemType, String condition) {
		return itemString + "\n  Tipo de producto: " + itemType + "\n  Estado del producto: " + condition.toUpperCase();
	}
	
	/**
	 * Muestra un producto de segunda mano con información del propietario (vista interna).
	 * @param itemString Item en formato corto
	 * @param itemType Tipo de producto
	 * @param condition Estado de conservación
	 * @param appraised Estado de tasación (Sí/No)
	 * @param offered Si está en oferta o no
	 * @return Producto con información adicional del propietario
	 */
	public static String secondHandDetailedOwner(String itemString, String itemType, String condition, String appraised, String offered) {
		return secondHandDetailedBrowser(itemString, itemType, condition) + "\n\n  Tasado: " + appraised + "\n  En oferta: " + offered;
	}
	
	
	// ================================================================================
	// 6. FORMATOS PARA PRODUCTOS ESPECÍFICOS (Comic, Game, Figurine)
	// ================================================================================
	
	/**
	 * Genera la vista detallada de un Comic.
	 * @param itemString Item formateado con nombre y precio (de itemDetailed)
	 * @param nPages Número de páginas
	 * @param publisher Editorial
	 * @param publicationYear Año de publicación
	 * @param writtenBy Lista de autores
	 * @return Vista completa del cómic con todos sus atributos
	 */
	public static String comicDetailed(String itemString, String nPages, String publisher, String publicationYear, List<String> writtenBy) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(itemString + "\n");
		sb.append("Páginas: ").append(nPages).append(" | ");
		sb.append("Editorial: ").append(publisher).append(" | ");
		sb.append("Año: ").append(publicationYear).append("\n");
		sb.append("Autor(es): ");
		
		if (writtenBy == null || writtenBy.isEmpty()) {
			sb.append("Sin autores registrados\n");
		} else {
			sb.append(String.join(", ", writtenBy)).append("\n");
		}
		
		return sb.toString();
	}

	/**
	 * Genera la vista detallada de un Juego.
	 * @param itemString Item formateado con nombre y precio
	 * @param nPlayers Número de jugadores
	 * @param mechanics Lista de mecánicas
	 * @param ageRange Rango de edad recomendada
	 * @return Vista completa del juego con todos sus atributos
	 */
	public static String gameDetailed(String itemString, String nPlayers, List<String> mechanics, String ageRange) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(itemString + "\n");
		sb.append("Jugadores: ").append(nPlayers).append("\n");
		sb.append("Mecánicas: ");
		
		if (mechanics == null || mechanics.isEmpty()) {
			sb.append("Sin mecánicas registradas\n");
		} else {
			sb.append(String.join(", ", mechanics)).append("\n");
		}
		
		sb.append("Edad recomendada: ").append(ageRange).append("\n");
		
		return sb.toString();
	}

	/**
	 * Genera la vista detallada de una Figura.
	 * @param itemString Item formateado con nombre y precio
	 * @param dimensions Dimensiones en formato "15.0x5.0x5.0 cm"
	 * @param material Material de la figura
	 * @param franchise Franquicia a la que pertenece
	 * @return Vista completa de la figura con todos sus atributos
	 */
	public static String figurineDetailed(String itemString, String dimensions, String material, String franchise) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(itemString);
		sb.append("  Dimensiones: ").append(dimensions).append("\n");
		sb.append("  Material: ").append(material).append("\n");
		sb.append("  Franquicia: ").append(franchise).append("\n");
		
		return sb.toString();
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