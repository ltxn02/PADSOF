package discounts;

/**
 * Interfaz que define el comportamiento de los descuentos de tipo rebaja directa.
 * Extiende de {@link IDiscount} y se especializa en promociones que modifican el precio
 * unitario de un producto de forma inmediata, permitiendo obtener
 * el valor neto del artículo tras la aplicación de la oferta.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface IRebaja extends IDiscount {

    /**
     * Aplica la lógica de rebaja específica sobre el precio unitario del producto.
     * * Este método se encarga de realizar el cálculo matemático directo
     *  para determinar el coste final de una unidad individual.
     * @param price Precio base original del artículo (antes de impuestos o promociones).
     * @return El nuevo valor monetario del producto tras aplicar la reducción directa.
     */
    double applyRebaja(double price);
}