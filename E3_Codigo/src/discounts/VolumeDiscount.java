package discounts;

import java.time.LocalDateTime;

/**
 * Clase que representa un descuento por volumen de compra total.
 * Implementa la interfaz {@link IVolumen} para aplicar una reducción económica fija
 * sobre el importe bruto del carrito de la compra, siempre que este alcance o supere
 * un umbral de gasto determinado.
 * * Este tipo de promoción incentiva el aumento del ticket medio de compra al ofrecer
 * una bonificación directa sobre el cierre de la transacción.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public class VolumeDiscount extends Discount implements IVolumen {

    private double threshold;

    /**
     * Constructor para inicializar un descuento por volumen de gasto.
     * Define la cuantía de la rebaja y el requisito económico para su aplicación.
     * @param discountEuro Cantidad neta en euros que se deducirá del total del pedido.
     * @param threshold    Importe mínimo necesario para que la oferta sea efectiva.
     * @param d            Descripción comercial o nombre de la campaña promocional.
     * @param f            Fecha y hora de inicio de la validez de la oferta.
     * @param t            Fecha y hora de expiración de la promoción.
     */
    public VolumeDiscount(double discountEuro, double threshold, String d, LocalDateTime f, LocalDateTime t) {
        super(discountEuro, d, f, t);
        this.threshold = threshold;
    }

    /**
     * Aplica la deducción económica sobre el total del carrito si se cumple la condición de gasto.
     * El método valida que el importe acumulado sea superior o igual al {@code threshold}
     * definido en la promoción.
     * @param total El importe total bruto acumulado en el {@code ShoppingCart}.
     * @return El importe neto tras restar la bonificación, o el total original
     * si no se alcanza el umbral de gasto.
     */
    @Override
    public double applyVolumen(double total) {
        if (total >= this.threshold) {
            return total - this.value;
        }
        return total;
    }

    /**
     * Implementación del método base de la interfaz {@link IDiscount}.
     * @param p Precio unitario del producto en el catálogo.
     * @return El mismo valor recibido como parámetro.
     */
    @Override
    public double apply(double p) {
        return p;
    }

    /**
     * Genera una descripción comercial enriquecida detallando el ahorro neto
     * y el umbral de gasto requerido para el beneficio.
     * @return Cadena informativa formateada.
     */
    @Override
    public String getDescription() {
        return String.format("%s (-%.2f€ si gastas >%.2f€)", description, value, threshold);
    }
}