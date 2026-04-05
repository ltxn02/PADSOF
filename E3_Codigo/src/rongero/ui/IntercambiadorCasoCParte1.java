package ui;

import model.catalog.SecondHandProduct;
import java.util.List;
import java.util.Scanner;
import model.transactions.EnviarOfertaIntercambio;
import model.user.Client;

/**
 * Clase de interfaz de usuario para la inspección detallada y acción sobre productos de segunda mano.
 * Proporciona el listado final de artículos donde el usuario puede seleccionar un producto
 * específico para ver sus metadatos o utilizarlo como base para iniciar una nueva
 * propuesta de intercambio.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class IntercambiadorCasoCParte1 {

    /**
     * Muestra la lista de productos disponibles y permite interactuar con uno de ellos.
     * Si el usuario selecciona un producto válido, el sistema muestra sus detalles y
     * redirige automáticamente al flujo de creación de ofertas (EnviarOfertaIntercambio).
     * * @param c                El cliente que navega por su propio inventario.
     * @param scanner          Instancia de Scanner para la entrada de datos.
     * @param productosActivos Lista de productos (filtrados previamente por estado) a mostrar.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void mostrar(Client c, Scanner scanner, List<SecondHandProduct> productosActivos) {
        int y = 0;

        System.out.println("\n--- DETALLE DE INVENTARIO ---");
        for (SecondHandProduct a : productosActivos) {
            System.out.println((y + 1) + ".- [Añadido: " + a.getDateadded() + "] " + a.getName() + " | Valor: " + a.getPrice() + "€");
            y++;
        }

        System.out.println("\nElige el número del producto para ver detalles o iniciar oferta:");
        System.out.println("0.- Volver al menú anterior");

        String inputIndex8 = scanner.nextLine();
        int index8;

        try {
            index8 = Integer.parseInt(inputIndex8);
        } catch (NumberFormatException e) {
            System.out.println("[!] Entrada no válida.");
            return;
        }

        if (index8 == 0) {
            return;
        }

        if (index8 < 1 || index8 > productosActivos.size()) {
            System.out.println("[!] Opción no válida. Producto no encontrado.");
            return;
        }

        /**
         * Selección y Acción.
         * Se recupera el producto y se invoca la lógica de envío de ofertas.
         */
        SecondHandProduct productoSeleccionado = productosActivos.get(index8 - 1);
        System.out.println("\n--- INFORMACIÓN DEL PRODUCTO ---");
        System.out.println(productoSeleccionado);

        /**
         * Conexión de sub-sistemas de UI:
         * Delegamos en EnviarOfertaIntercambio para gestionar el lote de intercambio.
         */
        EnviarOfertaIntercambio.Mostrar(c, productoSeleccionado, scanner);
    }
}