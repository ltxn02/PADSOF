package discounts;

/**
 * Interfaz que define el comportamiento de los descuentos basados en el volumen de unidades.
 * Extiende de {@link IDiscount} y se especializa en promociones donde el beneficio
 * económico depende directamente de alcanzar un umbral específico de unidades de un mismo
 * productos.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface ICantidad extends IDiscount {

    /**
     * Calcula el importe total del lote de productos aplicando la lógica de
     * descuento por cantidad acumulada.
     * * A diferencia de los descuentos porcentuales simples, este método procesa
     * el conjunto de unidades presentes en el carrito para determinar el ahorro grupal.
     * @param price    Precio unitario original del producto.
     * @param quantity Número total de unidades del producto presentes en la transacción.
     * @return El importe total neto resultante tras aplicar la promoción
     * sobre el lote completo.
     */
    double applyCantidad(double price, int quantity);
}