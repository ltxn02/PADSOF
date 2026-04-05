package rongero.ui;

import logic.Application;
import logic.SistemaRecomendacionesSegundamano;
import model.catalog.SecondHandProduct;
import model.user.Client;
import model.user.TodosLosClientes;
import model.transactions.EnviarOfertaIntercambio;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase de interfaz de usuario para la recomendación de artículos de segunda mano.
 * Implementa la lógica de presentación para sugerir productos de intercambio
 * basados en el comportamiento histórico del cliente y la afinidad con otros usuarios,
 * facilitando el inicio de nuevas ofertas de intercambio.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class MostrarRecomendacionesSegundaMano {

    /**
     * Renderiza el menú de recomendaciones de segunda mano y gestiona el flujo de intercambio.
     * Recupera los productos sugeridos por el motor de recomendaciones y permite al usuario
     * seleccionar uno para ver sus detalles o proponer un intercambio de forma directa.
     * * @param c       El cliente activo que consulta las recomendaciones.
     * @param scanner Instancia de Scanner para procesar la entrada de datos.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void MostrarRecomendaciones(Client c, Scanner scanner) {
        System.out.println("\n--- PRODUCTOS RECOMENDADOS (SEGUNDA MANO) ---");

        /**
         * Recopilación de datos.
         * Se filtran los productos de segunda mano del catálogo global y se obtiene
         * la lista de clientes para alimentar el motor de recomendaciones.
         */
        List<SecondHandProduct> catalogo = Application.getInstance().obtenerSecondHandProducts(c);
        ArrayList<Client> listaClientes = TodosLosClientes.todoslosClientes();

        /**
         * Invocación de la lógica de recomendación específica para segunda mano.
         */
        List<SecondHandProduct> recomendados = SistemaRecomendacionesSegundamano.obtenerRecomendaciones(c, catalogo, listaClientes);

        if (recomendados.isEmpty()) {
            System.out.println("Aún no tenemos suficientes datos para darte recomendaciones de segunda mano.");
            System.out.println("¡Prueba a realizar intercambios o valorar productos usados primero!");
            return;
        }

        /**
         * Listado de productos recomendados.
         */
        int y = 0;
        for (SecondHandProduct a : recomendados) {
            System.out.println((y + 1) + ".- [Añadido: " + a.getDateadded() + "] " + a.getName() + " | Valor: " + a.getPrice() + "€");
            y++;
        }

        System.out.println("\nElige el número del producto que quieres ver detalladamente:");
        System.out.println("0.- Volver al menú anterior");

        String inputIndex8 = scanner.nextLine();
        int index8;

        try {
            index8 = Integer.parseInt(inputIndex8);
        } catch (NumberFormatException e) {
            System.out.println("[!] Error: Introduce un número válido.");
            return;
        }

        if (index8 == 0) {
            return;
        } else if (index8 < 1 || index8 > recomendados.size()) {
            System.out.println("[!] Opción no válida. Producto no encontrado.");
        } else {
            /**
             * Acción sobre el producto recomendado.
             * Muestra el toString detallado del producto y redirige al flujo
             * de creación de oferta de intercambio.
             */
            SecondHandProduct seleccionado = recomendados.get(index8 - 1);
            System.out.println("\n--- DETALLES DEL PRODUCTO RECOMENDADO ---");
            System.out.println(seleccionado);

            // Iniciamos el proceso de intercambio sobre la recomendación elegida
            EnviarOfertaIntercambio.Mostrar(c, seleccionado, scanner);
        }
    }
}