package model.discounts;
import java.time.LocalDateTime;
/**
 * Clase que representa un descuento de cantidad fija.
 * A diferencia de los porcentajes, este descuento resta un valor monetario
 * constante al precio original de un producto.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public abstract class FixedAmountDiscount extends Discount {
    public String type = "FIXED_AMOUNT";

    /**
     * Constructor para un descuento de importe fijo.
     * @param value       Cantidad de dinero a descontar (ej. 5.0 para 5€).
     * @param description Descripción detallada de la oferta.
     * @param from        Fecha de inicio de la promoción.
     * @param to          Fecha de fin de la promoción.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public FixedAmountDiscount(double value, String description, LocalDateTime from, LocalDateTime to) {
        super(value, description, from, to);
    }
    /**
     * Aplica la rebaja fija sobre el precio original del producto.
     * @param originalPrice Precio base antes de aplicar el descuento.
     * @return El precio resultante tras restar el valor fijo del descuento.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    //@Override
    public double applyRebaja(double originalPrice) {
            return apply(originalPrice);
        }
    /**
     * Genera una descripción formateada del descuento incluyendo el importe que se resta.
     * @return Cadena de texto con la descripción y el valor del descuento (ej. "Oferta Verano (-5,00€)").
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public String getDescription() {
        return this.description + " (-" + String.format("%.2f", this.value) + "€)";
    }

}
