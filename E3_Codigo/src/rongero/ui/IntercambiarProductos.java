package rongero.ui;

import logic.Application;
import model.catalog.SecondHandProduct;
import model.transactions.ExchangeOffer;
import model.user.Client;

import java.util.Scanner;
import java.util.List;

/**
 * Clase controladora de la interfaz de usuario para el sistema de intercambios.
 * Su función principal es actuar como puente entre la lógica de negocio (Application)
 * y la capa de presentación, preparando las colecciones de datos requeridas
 * para gestionar las ofertas y el inventario de segunda mano del cliente.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class IntercambiarProductos {

    /**
     * Orquesta la recopilación de información de intercambios para un cliente específico.
     * Recupera del núcleo de la aplicación los productos de segunda mano, las ofertas
     * emitidas, las recibidas y el estado del inventario (activo/inactivo), delegando
     * posteriormente la interacción visual en la clase IntercambioMostrar.
     * * @param c       El cliente que accede al módulo de intercambios.
     * @param scanner Instancia de Scanner para la gestión de la entrada por terminal.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void IntercambiarProductos(Client c, Scanner scanner) {
        /**
         * Obtención de datos mediante el Singleton de la aplicación.
         */
        List<SecondHandProduct> productos = Application.getInstance().obtenerSecondHandProducts(c);
        List<ExchangeOffer> ofertashechas = Application.getInstance().getOffersMade(c);
        List<ExchangeOffer> ofertasrecibidas = Application.getInstance().getOffersReceived(c);
        List<SecondHandProduct> productosactivos = Application.getInstance().obtenerProductosActivos(c);
        List<SecondHandProduct> productosinactivos = Application.getInstance().obtenerProductosInactivos(c);

        /**
         * Delegación de la lógica de visualización.
         * Se pasan todas las listas procesadas al componente encargado de la renderización del menú.
         */
        IntercambioMostrar.mostrar(
                c,
                scanner,
                productos,
                ofertashechas,
                ofertasrecibidas,
                productosactivos,
                productosinactivos
        );
    }
}