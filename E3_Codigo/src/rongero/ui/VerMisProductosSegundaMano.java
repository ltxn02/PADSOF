package rongero.ui;

import model.catalog.SecondHandProduct;
import model.user.Client;
import java.util.List;

/**
 * Clase de interfaz de usuario para la consulta del inventario personal del cliente.
 * Proporciona una vista detallada de todos los artículos de segunda mano que el
 * usuario ha registrado en la plataforma, permitiendo verificar su estado de
 * tasación y sus detalles técnicos.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class VerMisProductosSegundaMano {

    /**
     * Recupera y muestra por consola la lista completa de productos de segunda mano
     * asociados al cliente actual.
     * * Si el cliente no dispone de productos registrados, informa mediante un mensaje
     * descriptivo. En caso contrario, recorre la colección imprimiendo la representación
     * textual de cada artículo.
     * * @param cliente El cliente propietario del inventario que se desea visualizar.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void VerMisProductosSegundaMano(Client cliente) {
        System.out.println("\n--- MI PORTFOLIO DE SEGUNDA MANO ---");

        /**
         * Obtención de la lista de productos personales.
         * Se accede directamente a la colección gestionada por el objeto Client.
         */
        List<SecondHandProduct> misProductos = cliente.getMyProducts();

        if (misProductos.isEmpty()) {
            System.out.println("[i] Todavía no has registrado ningún producto en nuestro sistema de intercambio.");
            return;
        }

        /**
         * Renderizado del listado.
         * Se utiliza el método toString() de SecondHandProduct para mostrar
         * metadatos como el estado de tasación, condición y precio asignado.
         */
        for (SecondHandProduct p : misProductos) {
            System.out.println(p.toString());
            System.out.println("----------------------------------------------------------------");
        }

        System.out.println("[Total: " + misProductos.size() + " artículos encontrados]");
    }
}