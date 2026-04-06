package discounts;
import java.time.LocalDateTime;
/**
 * Clase que representa un descuento por cantidad de unidades (ej. 3x2 o 2x1).
 * Implementa la interfaz ICantidad para calcular el coste total de un lote de
 * productos, donde el cliente paga solo una fracción de las unidades adquiridas.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class QuantityDiscount extends Discount implements ICantidad {
    private int buyX;
    private int payY;
    /**
     * Constructor para un descuento por cantidad.
     * Valida que la promoción sea ventajosa para el cliente (pagar menos de lo que se compra).
     * * @param buyX   Total de artículos del lote (ej. 3 para un 3x2).
     * @param payY   Artículos que se cobran (ej. 2 para un 3x2).
     * @param desc   Descripción comercial de la oferta.
     * @param f      Fecha de inicio de la validez.
     * @param t      Fecha de fin de la validez.
     * @throws IllegalArgumentException si payY no es menor que buyX.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public QuantityDiscount(int buyX, int payY, String desc, LocalDateTime f, LocalDateTime t) {
        super(0, desc, f, t);
        if (payY >= buyX) {
            throw new IllegalArgumentException("El número de artículos pagados debe ser menor al total.");
        }
        this.buyX = buyX;
        this.payY = payY;
    }
    /**
     * Calcula el precio total de un conjunto de unidades aplicando la promoción.
     * Divide la cantidad total en grupos completos de la oferta y suma las unidades sueltas.
     * * @param price    Precio unitario original del producto.
     * @param quantity Cantidad total de unidades en el carrito.
     * @return El importe total a pagar por todas las unidades tras aplicar el descuento.
     * @author Taha Ridda En Naji
     * @version 3.0
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
     * Implementación del método base apply. Para este tipo de descuento,
     * no se aplica una rebaja unitaria directa, por lo que devuelve el precio original.
     * * @param p Precio unitario.
     * @return El mismo precio sin cambios.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override public double apply(double p) { return p; }
    /**
     * Genera la descripción comercial formateada de la oferta.
     * * @return Cadena informativa (ej. "Especial Cómics (Oferta 3 x 2)").
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public String getDescription() {
        return String.format("%s (Oferta %d x %d)", description, buyX, payY);
    }
}