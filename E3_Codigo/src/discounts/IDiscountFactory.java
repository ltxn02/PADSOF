package discounts;

import products.Item;

/**
 * Interfaz que define la factoría abstracta para la creación centralizada de descuentos.
 * Sigue el patrón Factory para desacoplar la instanciación de las diversas estrategias
 * de ahorro, facilitando la escalabilidad
 * y el mantenimiento del sistema de marketing de la aplicación.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface IDiscountFactory {

    /**
     * Crea un descuento basado en la aplicación de un porcentaje sobre el precio unitario.
     * @param pct  Valor porcentual de la rebaja.
     * @param desc Descripción comercial detallada de la promoción.
     * @return Una instancia de {@link IRebaja} configurada con la lógica porcentual indicada.
     */
    IRebaja createPercentageDiscount(double pct, String desc);

    /**
     * Crea una promoción basada en el número de unidades adquiridas del mismo producto.
     * @param x    Umbral de unidades necesarias en el lote para activar la promoción.
     * @param y    Valor representativo del beneficio (unidades de regalo o factor de descuento).
     * @param desc Descripción informativa de la oferta por cantidad.
     * @return Una instancia de {@link ICantidad} optimizada para el procesamiento de lotes.
     */
    ICantidad createQuantityDiscount(int x, int y, String desc);

    /**
     * Crea un descuento supeditado al volumen de gasto total acumulado en la transacción.
     * @param threshold Importe económico mínimo (en euros) para activar la bonificación.
     * @param amount    Cantidad monetaria que se deducirá del total del carrito.
     * @param desc      Descripción de la campaña de fidelización por volumen.
     * @return Una instancia de {@link IVolumen} aplicable al cierre de la transacción.
     */
    IVolumen createVolumeDiscount(double threshold, double amount, String desc);

    /**
     * Crea una promoción de fidelidad que otorga un artículo de obsequio al usuario.
     * Se activa cuando el importe del carrito alcanza o supera el umbral definido.
     * @param threshold Importe mínimo de compra requerido para obtener el beneficio en especie.
     * @param gift      El objeto {@link Item} (producto nuevo o usado) que se entrega sin coste.
     * @param desc      Descripción comercial del regalo promocional.
     * @return Una instancia de {@link IRegalo} encargada de gestionar la bonificación física.
     */
    IRegalo createGiftDiscount(double threshold, Item gift, String desc);
}