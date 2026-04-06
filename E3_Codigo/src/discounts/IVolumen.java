package discounts;
/**
 * Interfaz que define el comportamiento de los descuentos basados en el volumen de compra.
 * A diferencia de las rebajas unitarias, estos descuentos se calculan sobre el
 * importe total acumulado en el carrito de la compra, incentivando un mayor gasto total.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface IVolumen extends IDiscount {
    /**
     * Aplica la lógica de descuento sobre el importe total del carrito de la compra.
     * * @param totalCart El precio total acumulado en el ShoppingCart antes de
     * aplicar este descuento de volumen.
     * @return El nuevo importe total tras restar la bonificación por volumen de gasto.
     * * @author Taha Ridda En Naji
     * @version 3.0
     */
    double applyVolumen(double totalCart);
}