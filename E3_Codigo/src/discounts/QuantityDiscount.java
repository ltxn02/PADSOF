package discounts;

import java.time.LocalDateTime;

/**
 * Clase que representa un descuento por volumen de unidades idénticas.
 * Implementa la interfaz {@link ICantidad} para calcular el coste total de un lote de
 * productos, donde el cliente solo abona una fracción de las unidades adquiridas
 * tras alcanzar un umbral específico.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public class QuantityDiscount extends Discount implements ICantidad {

    private int buyX;

    private int payY;

    /**
     * Constructor para inicializar un descuento por cantidad de unidades.
     * Valida que la promoción sea lógicamente ventajosa para el cliente.
     * @param buyX   Total de artículos que componen el lote promocional.
     * @param payY   Cantidad de artículos que el cliente debe pagar por cada lote.
     * @param desc   Descripción comercial o nombre de la campaña.
     * @param f      Fecha y hora de inicio de la validez de la oferta.
     * @param t      Fecha y hora de expiración de la promoción.
     * @throws IllegalArgumentException si el número de artículos pagados no es estrictamente
     * menor al total del lote ({@code payY >= buyX}).
     */
    public QuantityDiscount(int buyX, int payY, String desc, LocalDateTime f, LocalDateTime t) {
        super(0, desc, f, t);
        if (payY >= buyX) {
            throw new IllegalArgumentException("La promoción debe ser ventajosa: payY debe ser menor que buyX.");
        }
        this.buyX = buyX;
        this.payY = payY;
    }

    /**
     * Calcula el precio total de un conjunto de unidades aplicando la lógica de lotes.
     * El algoritmo descompone la cantidad total en grupos completos que cumplen la oferta
     * y añade las unidades restantes al precio ordinario.
     * @param price    Precio unitario original del producto en el catálogo.
     * @param quantity Cantidad total de unidades de este producto presentes en el carrito.
     * @return El importe neto total a pagar por el conjunto de unidades tras la promoción.
     */
    @Override
    public double applyCantidad(double price, int quantity) {
        if (isExpired() || quantity < buyX) {
            return price * quantity;
        }

        int fullGroups = quantity / buyX;
        int remainingItems = quantity % buyX;

        int totalToPay = (fullGroups * payY) + remainingItems;

        return totalToPay * price;
    }

    /**
     * Implementación del método base de la interfaz {@link IDiscount}.
     * Para este tipo de promoción, no existe una rebaja unitaria aplicable de forma aislada,
     * por lo que devuelve el precio original sin alteraciones.
     * @param p Precio unitario del artículo.
     * @return El mismo precio recibido como parámetro.
     */
    @Override
    public double apply(double p) {
        return p;
    }

    /**
     * Genera la descripción comercial formateada de la oferta, indicando claramente
     * la proporción del lote.
     * @return Cadena informativa.
     */
    @Override
    public String getDescription() {
        return String.format("%s (Oferta %d x %d)", description, buyX, payY);
    }
}