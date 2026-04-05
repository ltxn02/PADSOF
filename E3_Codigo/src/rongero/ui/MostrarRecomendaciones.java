package rongero.ui;

import logic.Application;
import logic.SistemaRecomendaciones;
import model.catalog.NewProduct;
import model.user.Client;
import java.util.Scanner;
import model.user.TodosLosClientes;
import java.util.ArrayList;

/**
 * Clase de interfaz de usuario encargada de presentar productos personalizados al cliente.
 * Utiliza algoritmos de filtrado colaborativo para sugerir artículos del catálogo
 * basados en el historial y preferencias de usuarios con perfiles similares.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class MostrarRecomendaciones {

    /**
     * Renderiza el menú de recomendaciones y gestiona la interacción de compra directa.
     * Recupera la lista de productos sugeridos y permite al cliente añadirlos
     * inmediatamente a su carrito de la compra especificando la cantidad deseada.
     * * @param c       El cliente activo que recibe las recomendaciones.
     * @param scanner Instancia de Scanner para procesar la selección del usuario.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void MostrarRecomendaciones(Client c, Scanner scanner) {
        System.out.println("\n--- PRODUCTOS RECOMENDADOS PARA TI ---");

        /**
         * Recopilación de datos para el motor de recomendaciones.
         * Se requiere el catálogo completo y la base de datos de clientes para
         * realizar el análisis de afinidad.
         */
        ArrayList<NewProduct> catalogo = Application.getInstance().getCatalog();
        ArrayList<Client> listaClientes = TodosLosClientes.todoslosClientes();

        /**
         * Invocación de la lógica de negocio.
         * El SistemaRecomendaciones encapsula la complejidad del algoritmo de filtrado.
         */
        ArrayList<NewProduct> recomendados = SistemaRecomendaciones.obtenerRecomendaciones(c, catalogo, listaClientes);

        if (recomendados.isEmpty()) {
            System.out.println("Aún no tenemos suficientes datos para darte recomendaciones personalizadas.");
            System.out.println("¡Prueba a realizar compras y valorar productos para que podamos conocer tus gustos!");
            return;
        }

        /**
         * Listado de productos sugeridos.
         */
        for (int i = 0; i < recomendados.size(); i++) {
            NewProduct p = recomendados.get(i);
            System.out.println((i + 1) + ".- " + p.getName() + " | Precio: " + p.getPrice() + "€");
        }
        System.out.println("0.- Volver al menú anterior");

        System.out.print("\n¿Deseas añadir algún producto recomendado a tu carrito? (0 para salir): ");
        String inputIndex = scanner.nextLine();

        try {
            int index = Integer.parseInt(inputIndex);

            if (index == 0) return;

            if (index < 1 || index > recomendados.size()) {
                System.out.println("[!] Opción no válida. Selección fuera de rango.");
                return;
            }

            /**
             * Flujo de adición al carrito.
             * Una vez seleccionado el producto recomendado, se procede a la validación
             * de cantidad y stock antes de insertarlo en el ShoppingCart del cliente.
             */
            NewProduct seleccionado = recomendados.get(index - 1);
            System.out.print("¿Cuántas unidades de '" + seleccionado.getName() + "' deseas añadir? ");
            int quantity = Integer.parseInt(scanner.nextLine());

            if (quantity <= 0) {
                System.out.println("[!] La cantidad debe ser superior a cero.");
                return;
            }

            c.getShoppingCart().addCartItem(seleccionado, quantity);
            System.out.println("[✔] ¡Éxito! " + seleccionado.getName() + " se ha añadido correctamente a tu carrito.");

        } catch (NumberFormatException e) {
            System.out.println("[!] Error: Por favor, introduce un valor numérico válido.");
        } catch (IllegalArgumentException e) {
            /**
             * Gestión de excepciones de negocio.
             * Captura errores como falta de stock o parámetros inválidos definidos en el modelo.
             */
            System.out.println("[!] Error en la operación: " + e.getMessage());
        }
    }
}