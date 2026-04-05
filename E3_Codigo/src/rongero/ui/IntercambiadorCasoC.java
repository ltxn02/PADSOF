package ui;

import model.catalog.SecondHandProduct;
import java.util.List;
import java.util.Scanner;
import model.user.Client;

/**
 * Clase de interfaz de usuario para la gestión del inventario de segunda mano del cliente.
 * Actúa como un menú de navegación de segundo nivel para clasificar la visualización
 * de los artículos propios entre aquellos que están activos (disponibles para intercambio)
 * y aquellos inactivos (ya intercambiados o retirados).
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class IntercambiadorCasoC {

    /**
     * Muestra las opciones de filtrado de inventario y redirige el flujo hacia
     * la visualización detallada de la parte 1.
     * * Realiza el control de flujo mediante la captura de la opción del usuario
     * y delega la impresión de las listas a la clase especializada IntercambiadorCasoCParte1.
     * * @param c                 El cliente propietario de los productos.
     * @param scanner           Instancia de Scanner para la navegación por terminal.
     * @param productosActivos   Lista de productos de segunda mano marcados como disponibles.
     * @param productosInactivos Lista de productos que ya no están disponibles para nuevas ofertas.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void mostrar(Client c, Scanner scanner, List<SecondHandProduct> productosActivos, List<SecondHandProduct> productosInactivos){
        System.out.println("\n--- GESTIÓN DE MIS PRODUCTOS ---");
        System.out.println("1.- Ver productos activos (Disponibles)");
        System.out.println("2.- Ver productos desactivados (Intercambiados/Retirados)");
        System.out.println("0.- Volver al menú anterior");

        String inputIndex7 = scanner.nextLine();
        int index7;

        try {
            index7 = Integer.parseInt(inputIndex7);
        } catch (NumberFormatException e) {
            System.out.println("[!] Por favor, introduce un número válido.");
            return;
        }

        if (index7 == 0) {
            return;
        } else if (index7 < 1 || index7 > 2) {
            System.out.println("[!] Opción no válida. Selección fuera de rango.");
            return;
        }

        /**
         * Delegación de visualización.
         * Se reutiliza el componente Parte1 pasando la sublista correspondiente.
         */
        switch (index7) {
            case 1:
                System.out.println("\n[ Mostrando Productos Activos ]");
                IntercambiadorCasoCParte1.mostrar(c, scanner, productosActivos);
                break;
            case 2:
                System.out.println("\n[ Mostrando Productos Inactivos ]");
                IntercambiadorCasoCParte1.mostrar(c, scanner, productosInactivos);
                break;
            default:
                System.out.println("[!] Error inesperado en la selección.");
                break;
        }
    }
}