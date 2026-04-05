package rongero.ui;

import java.util.List;
import java.util.Scanner;
import model.transactions.ExchangeOffer;
import model.user.Client;

/**
 * Clase de interfaz de usuario para la visualización detallada de ofertas de intercambio.
 * Proporciona un flujo de navegación que permite al usuario listar ofertas resumidas
 * y seleccionar una específica para inspeccionar sus detalles completos (lote de productos).
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class IntercambiadorCasoA {

    /**
     * Muestra una lista de ofertas de intercambio y permite profundizar en el detalle de una de ellas.
     * Gestiona la entrada por consola para validar que la oferta seleccionada exista
     * dentro de la lista proporcionada antes de mostrar el desglose mediante el toString de la oferta.
     * * @param c       El cliente que está consultando las ofertas.
     * @param scanner   Instancia de Scanner para la navegación por menús.
     * @param ofertas   Lista de objetos ExchangeOffer que se desean mostrar.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void mostrar(Client c, Scanner scanner, List<ExchangeOffer> ofertas) {
        int i = 0;

        System.out.println("\n--- LISTADO DE OFERTAS ---");
        for (ExchangeOffer a : ofertas) {
            System.out.println((i + 1) + ".- Fecha: " + a.getCreateDate() + " | Producto: " + a.getRequestedProduct().getName());
            i++;
        }
        System.out.println("0.- Volver al menú anterior");

        System.out.print("\nElige el número de la oferta que quieres ver detalladamente (0 para salir): ");
        String inputIndex2 = scanner.nextLine();

        int index2;
        try {
            index2 = Integer.parseInt(inputIndex2);
        } catch (NumberFormatException e) {
            System.out.println("[!] Por favor, introduce un número válido.");
            return;
        }

        if (index2 == 0) {
            return;
        }

        if (index2 < 1 || index2 > ofertas.size()) {
            System.out.println("[!] Opción no válida. Oferta no encontrada.");
            return;
        }

        /**
         * Visualización detallada.
         * Se recupera la oferta seleccionada y se imprime su información completa.
         */
        ExchangeOffer selectedOffer = ofertas.get(index2 - 1);
        System.out.println("\n--- DETALLES DE LA OFERTA ---");
        System.out.println(selectedOffer);

        System.out.println("\n0.- Volver al menú anterior");
        String inputIndex3 = scanner.nextLine();

        try {
            int index3 = Integer.parseInt(inputIndex3);
            if (index3 != 0) {
                System.out.println("Opción no válida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opción no válida.");
        }
    }
}