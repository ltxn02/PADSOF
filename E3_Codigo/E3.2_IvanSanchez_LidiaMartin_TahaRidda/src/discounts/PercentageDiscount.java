package discounts;

import java.time.LocalDateTime;

/**
 * Clase que representa un descuento porcentual aplicable a productos individuales.
 * Implementa la interfaz {@link IRebaja} para reducir el precio original de un artículo
 * mediante un factor multiplicativo basado en el valor porcentual configurado.
 * * Es la implementación estándar para promociones de catálogo (ej. "20% de descuento").
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public class PercentageDiscount extends Discount implements IRebaja {

    /**
     * Constructor para inicializar un nuevo descuento porcentual.
     * Define la magnitud de la rebaja y su periodo de vigencia temporal.
     * @param val  Valor numérico del porcentaje (ej. 15.0 para aplicar un 15% de descuento).
     * @param d    Descripción comercial o nombre de la campaña promocional.
     * @param f    Fecha y hora de inicio de la validez de la oferta.
     * @param t    Fecha y hora de expiración de la promoción.
     */
    public PercentageDiscount(double val, String d, LocalDateTime f, LocalDateTime t) {
        super(val, d, f, t);
    }

    /**
     * Aplica la rebaja porcentual específica sobre el precio unitario del producto.
     * @param p Precio base original del artículo antes de la promoción.
     * @return El precio resultante tras aplicar la reducción porcentual.
     */
    @Override
    public double applyRebaja(double p) {
        return p * (1.0 - (this.value / 100.0));
    }

    /**
     * Implementación del método general de la interfaz {@link IDiscount}.
     * Calcula el precio final del artículo integrando la lógica de descuento porcentual.
     * * @param price Precio original del artículo en el catálogo.
     * @return Precio neto con el descuento aplicado.
     */
    @Override
    public double apply(double price) {
        return price * (1.0 - (this.value / 100.0));
    }

    /**
     * Obtiene la descripción comercial enriquecida, incluyendo el valor del porcentaje
     * aplicado para su visualización en el front-end o tickets de compra.
     * * @return Cadena informativa formateada (ej. "Rebajas de Verano (-15.0%)").
     */
    @Override
    public String getDescription() {
        return description + " (-" + value + "%)";
    }
}