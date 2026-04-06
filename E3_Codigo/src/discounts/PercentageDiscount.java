package discounts;
import java.time.LocalDateTime;
/**
 * Clase que representa un descuento porcentual aplicable a productos individuales.
 * Implementa la interfaz IRebaja para reducir el precio original en un factor
 * basado en el valor porcentual configurado.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */

public class PercentageDiscount extends Discount implements IRebaja {
    /**
     * Constructor para un descuento porcentual.
     * * @param val   Valor del porcentaje (ej. 15.0 para un 15% de descuento).
     * @param d     Descripción comercial de la oferta.
     * @param f     Fecha y hora de inicio de la validez.
     * @param t     Fecha y hora de fin de la validez.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public PercentageDiscount(double val, String d, LocalDateTime f, LocalDateTime t) {
        super(val, d, f, t);
    }
    /**
     * Aplica la rebaja porcentual sobre el precio unitario del producto.
     * * @param p Precio base original.
     * @return El precio resultante tras aplicar el porcentaje de descuento.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override public double applyRebaja(double p) { return p * (1 - (value / 100)); }
    /**
     * Implementación del método general de la interfaz IDiscount.
     * Delega la lógica en el método applyRebaja.
     * * @param p Precio original del artículo.
     * @return Precio con el descuento aplicado.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override public double apply(double p) { return applyRebaja(p); }
    /**
     * Obtiene la descripción comercial incluyendo el valor del porcentaje.
     * * @return Cadena informativa (ej. "Rebajas de Verano (-15.0%)").
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override public String getDescription() { return description + " (-" + value + "%)"; }
}