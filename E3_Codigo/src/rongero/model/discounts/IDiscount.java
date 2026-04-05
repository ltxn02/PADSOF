package model.discounts;
/**
 * Interfaz raíz para el sistema de descuentos de la aplicación.
 * Define el contrato esencial para cualquier lógica de promoción, permitiendo
 * la aplicación de rebajas sobre precios, la gestión de periodos de validez
 * y la obtención de información comercial del descuento.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface IDiscount {
    /**
     * Obtiene una descripción legible y comercial del descuento.
     * Esta cadena se utiliza para mostrar información al usuario en la interfaz
     * (ej. "Rebajas de Enero -20%").
     * * @return Cadena de texto con la descripción de la oferta.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    String getDescription();
    /**
     * Determina si el descuento ha dejado de ser válido basándose en su
     * configuración temporal o de uso.
     * * @return true si el descuento ha expirado o no es aplicable actualmente, false en caso contrario.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    boolean isExpired();
    /**
     * Aplica la lógica del descuento sobre el precio unitario de un producto.
     * * @param price Precio original del artículo antes de la promoción.
     * @return El nuevo precio tras aplicar la reducción correspondiente.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    double apply(double price);
}