package discounts;

/**
 * Interfaz que define el comportamiento de los descuentos basados en el volumen de compra.
 * Extiende de {@link IDiscount} y se especializa en incentivar un mayor gasto total
 * en la transacción. A diferencia de las rebajas unitarias sobre productos específicos,
 * estos descuentos se calculan y aplican sobre el importe bruto acumulado en el carrito.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface IVolumen extends IDiscount {

    /**
     * Aplica la lógica de bonificación sobre el importe total del carrito de la compra.
     * * Este método procesa el valor acumulado de la orden para deducir una
     * cantidad fija o porcentual una vez alcanzado un umbral de gasto determinado.
     * @param totalCart El precio total acumulado en el {@code ShoppingCart} antes de
     * aplicar la reducción por volumen.
     * @return El nuevo importe neto total tras restar la bonificación correspondiente.
     */
    double applyVolumen(double totalCart);
}