package discounts;

import java.time.LocalDateTime;
import catalog.Item;

/**
 * Implementación estándar de la factoría de descuentos del sistema.
 * Esta clase centraliza la instanciación de estrategias concretas de ahorro,
 * aplicando políticas de vigencia temporales predefinidas por defecto según el
 * tipo de promoción..
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public class StandardDiscountFactory implements IDiscountFactory {

    /**
     * Crea un descuento porcentual con una validez predefinida de 3 meses.
     * Ideal para campañas de rebajas trimestrales o de temporada.
     * @param pct  Porcentaje de descuento a aplicar.
     * @param desc Descripción comercial de la promoción.
     * @return Una instancia de {@link PercentageDiscount} configurada con la fecha actual como inicio.
     */
    @Override
    public IRebaja createPercentageDiscount(double pct, String desc) {
        return new PercentageDiscount(pct, desc, LocalDateTime.now(), LocalDateTime.now().plusMonths(3));
    }

    /**
     * Crea una promoción de cantidad con una validez de 2 semanas.
     * @param x    Unidades totales que componen el lote promocional.
     * @param y    Unidades que efectivamente se cobran al cliente.
     * @param desc Descripción informativa de la oferta.
     * @return Una instancia de {@link QuantityDiscount} operativa durante 14 días.
     */
    @Override
    public ICantidad createQuantityDiscount(int x, int y, String desc) {
        return new QuantityDiscount(x, y, desc, LocalDateTime.now(), LocalDateTime.now().plusWeeks(2));
    }

    /**
     * Crea un descuento por volumen de gasto total con una validez extensa de 1 año.
     * @param threshold Importe económico mínimo necesario en el carrito para activar el descuento.
     * @param amount    Cantidad monetaria neta a deducir del total del pedido.
     * @param desc      Descripción del beneficio por volumen.
     * @return Una instancia de {@link VolumeDiscount} configurada para el próximo año.
     */
    @Override
    public IVolumen createVolumeDiscount(double threshold, double amount, String desc) {
        return new VolumeDiscount(amount, threshold, desc, LocalDateTime.now(), LocalDateTime.now().plusYears(1));
    }

    /**
     * Crea una promoción de regalo por volumen de compra con una validez de 1 mes.
     * Gestiona la entrega de obsequios físicos tras alcanzar un umbral de gasto.
     * @param threshold Importe mínimo de compra requerido para obtener el beneficio.
     * @param gift      El objeto {@link Item} que se adjudicará como regalo.
     * @param desc      Descripción comercial del obsequio promocional.
     * @return Una instancia de {@link GiftDiscount} válida durante los próximos 30 días.
     */
    @Override
    public IRegalo createGiftDiscount(double threshold, Item gift, String desc) {
        return new GiftDiscount(threshold, gift, desc, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
    }
}