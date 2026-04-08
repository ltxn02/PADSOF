package ui;

import logic.Application;
import model.catalog.Category;
import model.catalog.Item;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Clase de interfaz de usuario para la gestión de categorías.
 * Proporciona la lógica necesaria para solicitar datos al usuario, validar
 * la integridad de la información y registrar nuevas categorías en el sistema global.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class CrearCategoria {

    /**
     * Inicia el flujo interactivo por consola para crear una nueva categoría.
     * Realiza validaciones de seguridad para evitar nombres vacíos o duplicados
     * en el catálogo global antes de proceder a la instanciación.
     * * @param scanner Instancia de Scanner para capturar la entrada de texto del usuario.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void CrearCategoria(Scanner scanner) {
        System.out.println("\n--- CREAR NUEVA CATEGORÍA ---");
        System.out.print("Nombre de la nueva categoría: ");
        String nombre = scanner.nextLine();

        if (nombre.trim().isEmpty()) {
            System.out.println("[!] El nombre no puede estar vacío.");
            return;
        }

        for (Category c : Application.getInstance().getGlobalCategories()) {
            if (c.getNameCategory().equalsIgnoreCase(nombre)) {
                System.out.println("[!] Ya existe una categoría con ese nombre.");
                return;
            }
        }

        Category nuevaCategoria = new Category(nombre, new ArrayList<Item>());
        Application.getInstance().addCategory(nuevaCategoria);

        System.out.println("[+] Categoría '" + nombre + "' creada con éxito.");
    }

}