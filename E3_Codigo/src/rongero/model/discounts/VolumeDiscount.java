package model.discounts;
import java.time.LocalDateTime;
/**
 * Clase que representa un descuento por volumen de compra total.
 * Implementa la interfaz IVolumen para aplicar una reducción económica fija
 * sobre el total del carrito de la compra, siempre que este supere un
 * umbral (threshold) determinado.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class VolumeDiscount extends Discount implements IVolumen {
    private double threshold;
    /**
     * Constructor para un descuento de volumen.
     * * @param discountEuro Cantidad de euros que se restarán al total.
     * @param threshold    Importe mínimo acumulado en el carrito para aplicar la rebaja.
     * @param d            Descripción comercial de la oferta.
     * @param f            Fecha y hora de inicio de la validez.
     * @param t            Fecha y hora de fin de la validez.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public VolumeDiscount(double discountEuro, double threshold, String d, LocalDateTime f, LocalDateTime t) {
        super(discountEuro, d, f, t);
        this.threshold = threshold;
    }
    /**
     * Aplica la rebaja sobre el total del carrito si se cumple la condición de gasto.
     * Verifica que el descuento no esté expirado antes de proceder.
     * * @param total El importe total acumulado en el ShoppingCart.
     * @return El total final tras restar el descuento (si aplica), o el total original.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public double applyVolumen(double total) {
        return (total >= threshold) ? total - value : total;
    }
    /**
     * Implementación del método base apply. Para este tipo de descuento,
     * no se aplica una rebaja unitaria a productos individuales, por lo
     * que devuelve el precio original sin cambios.
     * * @param p Precio unitario del producto.
     * @return El mismo precio recibido.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override public double apply(double p) { return p; }
    /**
     * Genera la descripción comercial detallando el ahorro y el umbral requerido.
     * * @return Cadena informativa (ej. "Cupón Bienvenida (-10.0€ si gastas >100.0€)").
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override public String getDescription() { return description + " (-" + value + "€ si gastas >" + threshold + "€)"; }
}