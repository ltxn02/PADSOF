package discounts;

import transactions.ShoppingCart;
/**
 * Interfaz que define el comportamiento de las promociones basadas en obsequios.
 * A diferencia de los descuentos monetarios, esta interfaz gestiona la adición
 * de artículos de regalo directamente al carrito del cliente cuando se cumplen
 * las condiciones promocionales.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface IRegalo extends IDiscount {
    /**
     * Ejecuta la lógica necesaria para añadir un artículo de regalo al carrito
     * de la compra especificado.
     * * @param cart El carrito de la compra (ShoppingCart) donde se debe incluir
     * el artículo de regalo si la promoción es válida.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    void aplicarRegalo(ShoppingCart cart);
}
