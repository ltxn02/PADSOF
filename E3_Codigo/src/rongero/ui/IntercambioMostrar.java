package rongero.ui;

import model.catalog.SecondHandProduct;
import model.transactions.ExchangeOffer;
import model.user.Client;
import java.util.Scanner;
import java.util.List;

/**
 * Clase principal de la interfaz de usuario para el módulo de intercambios.
 * Implementa un menú interactivo persistente que permite a los clientes visualizar
 * el catálogo de segunda mano y acceder a la gestión de sus ofertas y productos propios.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class IntercambioMostrar {

    /**
     * Renderiza el panel principal de la tienda de intercambio y gestiona el bucle de navegación.
     * El método permite la selección de productos individuales por índice numérico o el acceso
     * a submenús de gestión mediante caracteres alfabéticos (A, B, C).
     * * @param c                   El cliente que está interactuando con la tienda.
     * @param scanner             Instancia de Scanner para capturar la entrada por consola.
     * @param productos           Lista global de productos de segunda mano disponibles.
     * @param ofertashechas       Historial de ofertas enviadas por el cliente actual.
     * @param ofertasrecibidas    Lista de ofertas pendientes de respuesta para el cliente.
     * @param productosactivos    Inventario propio del cliente disponible para ofrecer.
     * @param productosinactivos  Inventario propio del cliente ya comprometido o retirado.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void mostrar(Client c, Scanner scanner, List<SecondHandProduct> productos,
                               List<ExchangeOffer> ofertashechas, List<ExchangeOffer> ofertasrecibidas,
                               List<SecondHandProduct> productosactivos, List<SecondHandProduct> productosinactivos) {
        while (true) {
            System.out.println("\n=============================================================================================================================================================================");
            System.out.println("                                                                           TIENDA DE INTERCAMBIO ");
            System.out.println("==============================================================================================================================================================================");

            if (productos.isEmpty()) {
                System.out.println("Actualmente no hay productos para intercambios en la tienda.");
            }

            for (int i = 0; i < productos.size(); i++) {
                SecondHandProduct p = productos.get(i);
                System.out.println((i + 1) + ".- " + p.getName() + " | Precio: " + p.getPrice() + "€");
            }

            System.out.println("\nA.- Ofertas realizadas | B.- Ofertas recibidas | C.- Mis Productos | 0.- Volver");
            System.out.print("\nSelección (número de producto o letra de menú): ");

            String inputIndex = scanner.nextLine().toUpperCase();

            try {
                if (inputIndex.equals("0")) return;

                /**
                 * Enrutamiento de navegación.
                 * Se utiliza un switch para manejar las opciones de gestión (A, B, C)
                 * y el bloque default para la selección numérica de productos.
                 */
                switch (inputIndex) {
                    case "A":
                        IntercambiadorCasoA.mostrar(c, scanner, ofertashechas);
                        break;
                    case "B":
                        IntercambiadorCasoB.mostrar(c, scanner, ofertasrecibidas);
                        break;
                    case "C":
                        IntercambiadorCasoC.mostrar(c, scanner, productosactivos, productosinactivos);
                        break;
                    default:
                        // Intenta procesar la entrada como el índice de un producto
                        int index = Integer.parseInt(inputIndex);
                        IntercambiadorCasoDefault.mostrar(productos, index);
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Entrada inválida: Por favor, elija un número de producto o una letra de menú.");
            }
        }
    }
}