package discounts;
import java.time.LocalDateTime;

/**
 * Clase abstracta que define la estructura base para cualquier tipo de descuento
 * o promoción en el sistema.
 * Gestiona los atributos comunes como el valor del descuento, su descripción
 * y el rango de fechas de validez.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */

public abstract class Discount implements IDiscount {
    protected double value;
    protected String description;
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructor para inicializar los atributos comunes de un descuento.
     * * @param value Valor asociado al descuento.
     * @param desc  Descripción breve de la promoción.
     * @param from  Fecha de inicio de la promoción.
     * @param to    Fecha de expiración de la promoción.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public Discount(double value, String desc, LocalDateTime from, LocalDateTime to) {
        this.value = value;
        this.description = desc;
        this.from = from;
        this.to = to;
    }
    /**
     * Comprueba si el descuento ha expirado o aún no ha comenzado basándose
     * en la fecha y hora actual del sistema.
     * * @return true si la fecha actual está fuera del rango [from, to], false en caso contrario.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(to) || LocalDateTime.now().isBefore(from);
    }
}
