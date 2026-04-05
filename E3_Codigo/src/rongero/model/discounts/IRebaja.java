package model.discounts;
/**
 * Interfaz que define el comportamiento de los descuentos de tipo rebaja directa.
 * Se utiliza para promociones que modifican el precio unitario de un producto
 * de forma inmediata (ej. -20% o -5€), permitiendo obtener el precio neto tras la oferta.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface IRebaja extends IDiscount {
    /**
     * Aplica la lógica de rebaja específica sobre el precio unitario del producto.
     * * @param price Precio base original del artículo.
     * @return El nuevo precio del producto tras aplicar la reducción directa.
     * * @author Taha Ridda En Naji
     * @version 3.0
     */
    double applyRebaja(double price);
}