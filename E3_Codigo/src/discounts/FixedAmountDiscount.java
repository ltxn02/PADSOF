package discounts;

import java.time.LocalDateTime;

/**
 * Clase que representa un descuento de cuantía fija dentro del sistema de promociones.
 * A diferencia de los descuentos porcentuales, esta clase resta un valor monetario
 * constante al precio original de un producto.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public abstract class FixedAmountDiscount extends Discount {

    /** Identificador del tipo de descuento para procesos de serialización o lógica de negocio. */
    public String type = "FIXED_AMOUNT";

    /**
     * Constructor para inicializar un descuento de importe fijo.
     * Define la cantidad exacta en euros que se deducirá del precio base del producto.
     * @param value       Cantidad de dinero a descontar.
     * @param description Descripción detallada de la oferta o campaña publicitaria.
     * @param from        Fecha y hora en la que comienza la vigencia de la promoción.
     * @param to          Fecha y hora en la que expira la promoción.
     */
    public FixedAmountDiscount(double value, String description, LocalDateTime from, LocalDateTime to) {
        super(value, description, from, to);
    }

    /**
     * Genera una descripción enriquecida del descuento, concatenando el texto descriptivo
     * con el importe exacto que se resta al precio del producto.
     * @return Cadena de texto formateada.
     */
    @Override
    public String getDescription() {
        return this.description + " (-" + String.format("%.2f", this.value) + "€)";
    }
}