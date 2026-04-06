package discounts;
import java.time.LocalDateTime;
import catalog.Item;
/**
 * Implementación estándar de la factoría de descuentos.
 * Esta clase se encarga de instanciar las estrategias concretas de descuento
 * aplicando políticas de vigencia por defecto (ej. 3 meses para porcentajes,
 * 2 semanas para cantidades).
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class StandardDiscountFactory implements IDiscountFactory {
    /**
     * Crea un descuento porcentual con una validez por defecto de 3 meses.
     * * @param pct  Porcentaje de descuento a aplicar.
     * @param desc Descripción comercial de la promoción.
     * @return Una instancia de PercentageDiscount configurada.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public IRebaja createPercentageDiscount(double pct, String desc) {
        return new PercentageDiscount(pct, desc, LocalDateTime.now(), LocalDateTime.now().plusMonths(3));
    }
    /**
     * Crea una promoción de cantidad (ej. 3x2) con una validez de 2 semanas.
     * * @param x    Unidades totales del lote.
     * @param y    Unidades que se cobran al cliente.
     * @param desc Descripción de la oferta.
     * @return Una instancia de QuantityDiscount configurada.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public ICantidad createQuantityDiscount(int x, int y, String desc) {
        return new QuantityDiscount(x, y, desc, LocalDateTime.now(), LocalDateTime.now().plusWeeks(2));
    }
    /**
     * Crea un descuento por volumen de gasto con una validez de 1 año.
     * * @param threshold Importe mínimo necesario en el carrito.
     * @param amount    Cantidad económica a descontar del total.
     * @param desc      Descripción del descuento global.
     * @return Una instancia de VolumeDiscount configurada.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public IVolumen createVolumeDiscount(double threshold, double amount, String desc) {
        return new VolumeDiscount(amount, threshold, desc, LocalDateTime.now(), LocalDateTime.now().plusYears(1));
    }
    /**
     * Crea una promoción de regalo por volumen de compra con validez de 1 mes.
     * * @param threshold Importe mínimo para obtener el obsequio.
     * @param gift      El artículo (Item) que se entrega como regalo.
     * @param desc      Descripción del regalo promocional.
     * @return Una instancia de GiftDiscount configurada.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public IRegalo createGiftDiscount(double threshold, Item gift, String desc) {
        return new GiftDiscount(threshold, gift, desc, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
    }
}