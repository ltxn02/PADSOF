package model.transactions;
import model.catalog.SecondHandProduct;
import java.util.List;
import java.util.*;
import model.user.Client;
import logic.Application;


/**
 * Clase de interfaz de usuario encargada de gestionar el flujo de envío de ofertas.
 * Permite al cliente emisor seleccionar uno o varios productos de su cartera activa
 * para proponer un intercambio por un producto deseado del catálogo de segunda mano.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class EnviarOfertaIntercambio {

    /**
     * Muestra el menú interactivo para la selección de productos y creación de la oferta.
     * Implementa un bucle de selección múltiple que permite añadir o quitar productos
     * del lote antes de confirmar el envío de la ExchangeOffer.
     * * @param emisor   El cliente que desea realizar la oferta.
     * @param deseado  El producto de segunda mano que el emisor desea obtener.
     * @param scanner  Instancia de Scanner para capturar la entrada por consola.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void Mostrar(Client emisor, SecondHandProduct deseado, Scanner scanner) {
            List<SecondHandProduct> misProductos = Application.getInstance().obtenerProductosActivos(emisor);
            List<SecondHandProduct> lote = new ArrayList<>();

            // Bucle para elegir varios productos
            boolean terminado = false;
            while (!terminado && !misProductos.isEmpty()) {
                System.out.println("\nSelecciona productos para tu oferta (escribe 'OK' para terminar):");
                for (int i = 0; i < misProductos.size(); i++) {
                    String check = lote.contains(misProductos.get(i)) ? "[X]" : "[ ]";
                    System.out.println((i+1) + ".- " + check + " " + misProductos.get(i).getName());
                }

                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("OK")) {
                    terminado = true;
                } else {
                    try {
                        int idx = Integer.parseInt(input) - 1;
                        SecondHandProduct p = misProductos.get(idx);
                        if (lote.contains(p)) lote.remove(p); else lote.add(p);
                    } catch (Exception e) { System.out.println("Opción no válida."); }
                }
            }

            if (!lote.isEmpty()) {
                // ¡Aquí es donde la POO brilla! Simplemente instanciamos la oferta
                // y ella sola se encarga de bloquear productos y notificar al dueño.
                new ExchangeOffer(deseado, lote, emisor);
                System.out.println("[✔] Oferta de intercambio enviada correctamente.");
            }
        }}