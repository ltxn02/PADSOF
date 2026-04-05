package ui;

import java.util.List;
import java.util.Scanner;
import model.transactions.ExchangeOffer;
import model.user.Client;
import util.ExchangeStatus;

/**
 * Clase de interfaz de usuario para la gestión interactiva de ofertas de intercambio recibidas.
 * Permite al cliente receptor inspeccionar el lote de productos ofrecidos y tomar una
 * decisión administrativa (Aceptar o Rechazar) que disparará la lógica de negocio
 * de cambio de propietarios.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class IntercambiadorCasoB {

    /**
     * Muestra el listado de ofertas recibidas y habilita las opciones de gestión si
     * la oferta se encuentra en estado PENDIENTE.
     * * Implementa un diseño defensivo capturando posibles errores de formato en la entrada
     * y validando la disponibilidad de los productos antes de confirmar una aceptación.
     * * @param c       El cliente receptor de las ofertas.
     * @param scanner Instancia de Scanner para capturar la decisión del usuario.
     * @param ofertas Lista de objetos ExchangeOffer dirigidas al cliente actual.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void mostrar(Client c, Scanner scanner, List<ExchangeOffer> ofertas) {
        int s = 0;
        System.out.println("\n--- BANDEJA DE ENTRADA DE OFERTAS ---");
        for (ExchangeOffer a : ofertas) {
            System.out.println((s + 1) + ".- Fecha: " + a.getCreateDate() + " | Producto solicitado: " + a.getRequestedProduct().getName());
            s++;
        }
        System.out.println("0.- Volver al menú anterior");

        System.out.println("\nElige el número de la oferta que quieres gestionar (0 para salir): ");
        String inputIndex5 = scanner.nextLine();
        int index5;

        try {
            index5 = Integer.parseInt(inputIndex5);
        } catch (NumberFormatException e) {
            System.out.println("[!] Entrada no válida.");
            return;
        }

        if (index5 == 0) {
            return;
        }

        if (index5 < 1 || index5 > ofertas.size()) {
            System.out.println("[!] Opción no válida. Oferta no encontrada.");
            return;
        }

        ExchangeOffer selectedOffer2 = ofertas.get(index5 - 1);
        System.out.println("\n--- DETALLE DE LA OFERTA SELECCIONADA ---");
        System.out.println(selectedOffer2);

        /**
         * Lógica de Decisión.
         * Solo se permite interactuar con la oferta si su estado actual es PENDIENTE.
         */
        if (selectedOffer2.getStatus() == ExchangeStatus.PENDIENTE) {
            System.out.println("\nOPCIONES DISPONIBLES:");
            System.out.println("1.- Aceptar Oferta");
            System.out.println("2.- Rechazar Oferta");
            System.out.println("0.- Volver al menú anterior");

            String inputIndex6 = scanner.nextLine();
            int index6;
            try {
                index6 = Integer.parseInt(inputIndex6);
            } catch (NumberFormatException e) {
                index6 = -1;
            }

            if (index6 == 0) {
                return;
            } else if (index6 == 1) {
                /** * Al aceptar, la clase ExchangeOffer validará internamente
                 * la disponibilidad y ejecutará el intercambio.
                 */
                selectedOffer2.aceptarOferta();
                System.out.println("[✔] Oferta aceptada. El inventario ha sido actualizado.");
            } else if (index6 == 2) {
                selectedOffer2.rejectOffer();
                System.out.println("[X] Oferta rechazada correctamente.");
            } else {
                System.out.println("[!] Opción no válida.");
            }
        } else {
            /**
             * Caso para ofertas ya procesadas (Aceptadas, Rechazadas o Canceladas).
             */
            System.out.println("\n[i] Esta oferta ya ha sido procesada y no admite más cambios.");
            System.out.println("0.- Volver al menú anterior");
            String inputIndex6 = scanner.nextLine();
            if (!inputIndex6.equals("0")) {
                System.out.println("[!] Opción no válida.");
            }
        }
    }
}