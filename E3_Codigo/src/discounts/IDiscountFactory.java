package discounts;
/**
 * Interfaz que define la factoría abstracta para la creación de descuentos y promociones.
 * Centraliza la instanciación de diversas estrategias de ahorro (porcentajes, cantidades,
 * volumen y regalos), facilitando la extensión del sistema de marketing de la aplicación.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
import catalog.Item;

public interface IDiscountFactory {
    /**
     * Crea un descuento basado en un porcentaje sobre el precio original.
     * * @param pct  Porcentaje de descuento (ej. 20.0 para un 20%).
     * @param desc Descripción comercial de la promoción.
     * @return Una instancia de IRebaja configurada con el porcentaje indicado.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    IRebaja createPercentageDiscount(double pct, String desc);
    /**
     * Crea una promoción basada en la cantidad de unidades (ej. 3x2, segunda unidad al 50%).
     * * @param x    Cantidad de unidades necesarias para activar la promoción.
     * @param y    Valor o unidades que resultan de beneficio.
     * @param desc Descripción de la oferta de cantidad.
     * @return Una instancia de ICantidad para aplicar sobre lotes de productos.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    ICantidad createQuantityDiscount(int x, int y, String desc);
    /**
     * Crea un descuento por volumen de gasto total en el carrito.
     * * @param threshold Importe mínimo de compra para activar el descuento.
     * @param amount    Cantidad económica que se resta al total.
     * @param desc      Descripción del descuento por volumen.
     * @return Una instancia de IVolumen aplicable al total de la transacción.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    IVolumen createVolumeDiscount(double threshold, double amount, String desc);
    /**
     * Crea una promoción que otorga un artículo de regalo al alcanzar un umbral de gasto.
     * * @param threshold Importe mínimo de compra necesario.
     * @param gift      El artículo (Item) que se entrega sin coste adicional.
     * @param desc      Descripción del regalo promocional.
     * @return Una instancia de IRegalo que gestiona la adición del obsequio al carrito.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    IRegalo createGiftDiscount(double threshold, Item gift, String desc); // NUEVO
}