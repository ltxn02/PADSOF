package discounts;

import catalog.*;
import transactions.ShoppingCart;
import java.time.LocalDateTime;
/**
 * Clase que representa un descuento de tipo regalo.
 * Se activa cuando el usuario alcanza un gasto mínimo en su carrito (IVolumen),
 * otorgando un artículo adicional sin coste (IRegalo).
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class GiftDiscount extends Discount implements IRegalo, IVolumen {
    private Item regalo;
    private double minGasto;
    /**
     * Constructor para una promoción con regalo.
     * * @param minGasto Importe mínimo acumulado en el carrito para obtener el regalo.
     * @param regalo   El objeto de la clase Item que se regalará.
     * @param desc     Descripción comercial de la promoción.
     * @param f        Fecha de inicio de la validez.
     * @param t        Fecha de fin de la validez.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public GiftDiscount(double minGasto, Item regalo, String desc, LocalDateTime f, LocalDateTime t) {
        super(0, desc, f, t);
        this.regalo = regalo;
        this.minGasto = minGasto;
    }
    /**
     * Ejecuta la lógica para añadir el regalo al carrito de la compra si se cumplen
     * las condiciones de gasto mínimo y vigencia temporal.
     * * @param cart El carrito de la compra sobre el que se aplicará el regalo.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override


    public void aplicarRegalo(ShoppingCart cart) {
        if (!isExpired() && cart.getFullPrice() >= minGasto) {
            // Solo añadimos si el regalo es un NewProduct y tiene stock
            if (this.regalo instanceof NewProduct) {
                NewProduct pRegalo = (NewProduct) this.regalo;
                if (pRegalo.isEffectiveStockHigher(1)) {
                    cart.addGift(this.regalo);
                }
            } else {
                // Si es un producto de segunda mano o genérico, se añade igual
                cart.addGift(this.regalo);
            }
        }
    }


    /**
     * Implementación de la interfaz IVolumen. En este tipo de descuento, el precio
     * total no se ve alterado numéricamente, por lo que devuelve el total original.
     * * @param totalCart Precio total del carrito antes de este descuento.
     * @return El mismo precio recibido, ya que el beneficio es en especie (regalo).
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public double applyVolumen(double totalCart) {
        return totalCart;
    }
    /**
     * Método genérico de aplicación de descuento individual. No altera el precio.
     * * @param price Precio unitario del producto.
     * @return El mismo precio sin cambios.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public double apply(double price) {
        return price;
    }
    /**
     * Genera una descripción detallada de la promoción indicando el regalo y el umbral de gasto.
     * * @return Cadena informativa (ej. "Promo Navidad (¡REGALO!: Figura Goku por compras > 100€)").
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public String getDescription() {
        return description + " (¡REGALO!: " + regalo.getName() + " por compras > " + minGasto + "€)";
    }
    /**
     * Obtiene el artículo definido como regalo en esta promoción.
     * @return El objeto Item de regalo.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public Item getRegalo() { return regalo; }
    /**
     * Obtiene el gasto mínimo requerido para esta promoción.
     * @return Valor del umbral de gasto.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public double getMinGasto() { return minGasto; }
}