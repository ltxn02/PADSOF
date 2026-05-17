package discounts;

/**
 * Interfaz raíz y contrato base para el sistema de promociones de la aplicación.
 * Define las operaciones esenciales para cualquier lógica de descuento, permitiendo
 * la aplicación de rebajas sobre precios unitarios, la gestión de periodos de validez
 * y la extracción de información comercial para el usuario final.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface IDiscount {

    /**
     * Obtiene una descripción legible, comercial y detallada del descuento.
     * Esta cadena está destinada a ser mostrada en la interfaz de usuario y en los
     * resúmenes de compra.
     * @return Cadena de texto con la información descriptiva de la oferta.
     */
    String getDescription();

    /**
     * Determina si el descuento ha dejado de ser válido basándose en su
     * configuración cronológica o condiciones de uso específicas.
     * @return true si el descuento ha expirado o no es aplicable en el momento actual,
     * false en caso contrario.
     */
    boolean isExpired();

    /**
     * Aplica la lógica matemática del descuento sobre el precio unitario de un producto.
     * * Este método es el núcleo del cálculo de precios para artículos individuales
     * dentro del catálogo.
     * @param price Precio original del artículo antes de procesar la promoción.
     * @return El nuevo valor monetario tras aplicar la reducción correspondiente.
     */
    double apply(double price);

    /**
     * Verifica si el descuento es apto para ser utilizado en una transacción activa.
     * Es una utilidad lógica que invierte el estado de expiración para mejorar la legibilidad.
     * @return true si el descuento no ha expirado y está dentro de su periodo de vigencia.
     */
    default boolean isValid() {
        return !isExpired();
    }
}