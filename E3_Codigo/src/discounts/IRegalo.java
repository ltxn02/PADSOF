package discounts;

import transactions.ShoppingCart;

/**
 * Interfaz que define el comportamiento de las promociones basadas en obsequios.
 * Extiende de {@link IDiscount} y se especializa en la gestión de beneficios en especie.
 * A diferencia de los descuentos monetarios, esta interfaz se encarga de la adición
 * de artículos de regalo directamente al inventario del cliente cuando se cumplen
 * las condiciones de la campaña.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface IRegalo extends IDiscount {

    /**
     * Ejecuta la lógica necesaria para adjudicar un artículo de regalo al carrito
     * de la compra especificado.
     * * Este método debe validar tanto la vigencia de la promoción como el cumplimiento
     * de los requisitos específicos (ej. stock del regalo o umbral de compra) antes de
     * modificar el contenido del carrito.
     * @param cart El {@link ShoppingCart} de la transacción donde se debe incluir
     * el artículo de regalo si la promoción es válida.
     */
    void aplicarRegalo(ShoppingCart cart);
}