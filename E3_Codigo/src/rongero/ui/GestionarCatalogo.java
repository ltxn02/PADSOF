package rongero.ui;

import model.user.Manager;
import model.catalog.*;
import model.catalog.*;
import logic.Application;
import java.util.*;

public class GestionarCatalogo {
    public static void mostrar(Scanner scanner) { // Cambiado a 'mostrar' para consistencia
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE CATÁLOGO ---");
            System.out.println("1.- Ver categorías existentes");
            System.out.println("2.- Añadir nueva categoría");
            System.out.println("3.- Crear Nuevo Pack (Composite Pattern)");
            System.out.println("0.- Volver al menú anterior");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    VerCategoria.VerCategorias();
                    break;
                case "2":
                    CrearCategoria.CrearCategoria(scanner);
                    break;
                case "3":
                    menuCrearPack(scanner);
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }
    /**
     * Proporciona el menú interactivo para la creación de Packs de productos.
     * Utiliza la ICatalogFactory para instanciar objetos complejos (Composite) asegurando
     * que se cumplan las reglas de negocio, como la cantidad mínima de artículos.
     * * @author Taha Ridda En Naji
     * @version 3.0
     */
    private static void menuCrearPack(Scanner scanner) {
        System.out.println("\n--- CREADOR DE PACKS ---");
        List<NewProduct> productosParaPack = new ArrayList<>();
        List<NewProduct> catalogo = Application.getInstance().getCatalog();

        while (productosParaPack.size() < 2 || true) {
            System.out.println("\nProductos seleccionados: " + productosParaPack.size());
            System.out.println("Selecciona un producto por su nombre (o escribe 'FIN' para terminar):");

            catalogo.forEach(p -> System.out.print("[" + p.getName() + "] "));
            System.out.println();

            String nombre = scanner.nextLine();
            if (nombre.equalsIgnoreCase("FIN")) {
                if (productosParaPack.size() < 2) {
                    System.out.println("[!] Error: Un pack requiere al menos 2 productos.");
                    continue;
                }
                break;
            }

            NewProduct p = (NewProduct) Application.getInstance().buscarProducto(nombre);
            if (p != null) {
                productosParaPack.add(p);
                System.out.println("[+] " + p.getName() + " añadido al pack temporal.");
            } else {
                System.out.println("[!] Producto no encontrado.");
            }
        }

        System.out.print("Nombre del Pack: ");
        String nombrePack = scanner.nextLine();
        System.out.print("Descripción breve: ");
        String descPack = scanner.nextLine();
        System.out.print("Precio especial del Pack (€): ");
        double precio = Double.parseDouble(scanner.nextLine());
        System.out.print("Stock inicial: ");
        int stock = Integer.parseInt(scanner.nextLine());

        ICatalogFactory factory = new StandardCatalogFactory();
        Pack nuevoPack = factory.createPack(nombrePack, descPack, precio, productosParaPack, stock);

        Application.getInstance().getCatalog().add(nuevoPack);
        System.out.println("\n[✔] ¡Pack '" + nombrePack + "' creado y añadido al catálogo!");
    }
}