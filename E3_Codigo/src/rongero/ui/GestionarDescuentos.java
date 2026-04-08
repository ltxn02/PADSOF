package ui;

import java.util.Scanner;
import java.time.LocalDateTime;
import logic.Application;
import model.discounts.*;
import model.catalog.NewProduct;
import model.catalog.Category;

/**
 * Interfaz de terminal encargada de la administración de promociones y descuentos.
 * Permite al perfil Manager supervisar las ofertas vigentes tanto a nivel
 * de producto individual como a nivel global (carrito de compra).
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class GestionarDescuentos {

    /**
     * Muestra el menú principal de gestión de descuentos y redirige el flujo
     * según la elección del administrador.
     * * @param scanner Instancia de Scanner para capturar la navegación del usuario.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void mostrar(Scanner scanner) {
        System.out.println("\n--- GESTIÓN DE DESCUENTOS ---");
        System.out.println("1.- Ver descuentos activos");
        System.out.println("2.- Crear nuevo descuento");
        System.out.println("0.- Volver");
        System.out.print("Opción: ");

        String subOpcion = scanner.nextLine();

        if (subOpcion.equals("1")) {
            verDescuentosActivos();
        } else if (subOpcion.equals("2")) {
            MenuCreacion.mostrar(scanner);
        }
    }

    /**
     * Recorre el catálogo de productos nuevos y la lista de descuentos globales
     * para mostrar un resumen de todas las promociones configuradas en el sistema.
     * * @author Taha Ridda En Naji
     * @version 3.0
     */
    private static void verDescuentosActivos() {
        System.out.println("\n--- DESCUENTOS EN CATÁLOGO ---");
        for (NewProduct p : Application.getInstance().getCatalog()) {
            if (p.getDiscount() != null) {
                System.out.println("- [" + p.getName() + "]: " + p.getDiscount().getDescription());
            }
        }

        System.out.println("\n--- DESCUENTOS GLOBALES (CARRITO) ---");
        for (IVolumen d : Application.getInstance().getGlobalDiscounts()) {
            System.out.println("- " + d.getDescription());
        }
    }
}