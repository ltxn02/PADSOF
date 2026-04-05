package model.discounts;
/**
 * Interfaz que define el comportamiento de los descuentos basados en la cantidad
 * de unidades de un mismo producto adquiridas.
 * Se utiliza para promociones donde el ahorro depende directamente de alcanzar
 * un umbral de unidades (ej. "Compra 3 y paga 2").
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface ICantidad extends IDiscount {
    /**
     * Calcula el precio total del lote de productos aplicando la lógica de
     * descuento por cantidad especificada.
     * * @param price    Precio unitario original del producto.
     * @param quantity Cantidad de unidades del producto presentes en el carrito.
     * @return El importe total resultante tras aplicar la promoción sobre el conjunto de unidades.
     * * @author Taha Ridda En Naji
     * @version 3.0
     */
    double applyCantidad(double price, int quantity);
}