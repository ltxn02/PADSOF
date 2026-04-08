package discounts;

import catalog.*;
import transactions.ShoppingCart;
import java.time.LocalDateTime;

/**
 * Clase que representa una promoción de tipo regalo.
 * Se activa cuando el volumen de compra del usuario alcanza un umbral económico mínimo
 * ({@link IVolumen}), otorgando un artículo adicional sin coste directo ({@link IRegalo}).
 * * Esta clase no altera el valor monetario de los productos, sino que gestiona beneficios
 * en especie dentro del flujo de la transacción.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public class GiftDiscount extends Discount implements IRegalo, IVolumen {

    private Item regalo;

    private double minGasto;

    /**
     * Constructor para inicializar una promoción basada en regalo por volumen de compra.
     * @param minGasto Importe mínimo acumulado en el carrito para obtener el regalo.
     * @param regalo   El objeto de la clase {@link Item} que se adjudicará al cliente.
     * @param desc     Descripción comercial o nombre de la campaña.
     * @param f        Fecha y hora de inicio de la validez de la oferta.
     * @param t        Fecha y hora de expiración de la promoción.
     */
    public GiftDiscount(double minGasto, Item regalo, String desc, LocalDateTime f, LocalDateTime t) {
        super(0, desc, f, t);
        this.regalo = regalo;
        this.minGasto = minGasto;
    }

    /**
     * Ejecuta la lógica de adjudicación del regalo al carrito de la compra.
     * Verifica que la promoción esté vigente y que el importe total del carrito
     * supere el umbral {@code minGasto}. En el caso de productos nuevos, se valida
     * además la disponibilidad de stock efectivo.
     * @param cart El {@link ShoppingCart} sobre el que se procesará la validación y entrega.
     */
    @Override
    public void aplicarRegalo(ShoppingCart cart) {
        if (!isExpired() && cart.getFullPrice() >= minGasto) {
            if (this.regalo instanceof NewProduct) {
                NewProduct pRegalo = (NewProduct) this.regalo;

                if (pRegalo.isEffectiveStockHigher(0)) {
                    cart.addGift(this.regalo);
                }
            } else {
                cart.addGift(this.regalo);
            }
        }
    }

    /**
     * Implementación de la interfaz {@link IVolumen}.
     * En este esquema promocional, el precio total de la orden permanece inalterado
     * ya que el beneficio es una bonificación de producto.
     * @param totalCart Precio total del carrito antes de aplicar esta promoción.
     * @return El mismo precio de entrada, sin deducciones monetarias.
     */
    @Override
    public double applyVolumen(double totalCart) {
        return totalCart;
    }

    /**
     * Método de aplicación de descuento individual heredado de {@link Discount}.
     * No realiza modificaciones sobre el precio unitario del artículo.
     * @param price Precio unitario del producto.
     * @return El valor original del precio sin alteraciones.
     */
    @Override
    public double apply(double price) {
        return price;
    }

    /**
     * Genera una descripción detallada y comercial de la promoción.
     * Incluye el nombre del artículo de regalo y el requisito de gasto mínimo.
     * @return Cadena informativa.
     */
    @Override
    public String getDescription() {
        return description + " (¡REGALO!: " + regalo.getName() + " por compras > " + minGasto + "€)";
    }

}