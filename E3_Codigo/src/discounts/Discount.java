package discounts;

import java.time.LocalDateTime;

/**
 * Clase abstracta que define la estructura base para cualquier tipo de descuento
 * o promoción dentro del sistema de catálogo.
 * Gestiona los atributos comunes como el valor del descuento, su descripción
 * y el rango temporal de validez. Las clases que hereden de esta deberán
 * implementar la lógica específica de aplicación del descuento.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public abstract class Discount implements IDiscount {
    protected double value;
    protected String description;
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructor para inicializar los atributos comunes de una promoción.
     * Define el periodo de vigencia y la magnitud del beneficio.
     *
     * @param value Valor numérico asociado al descuento (ej. porcentaje o cantidad fija).
     * @param desc  Descripción detallada de la promoción para mostrar al usuario.
     * @param from  Fecha y hora de inicio de la validez del descuento.
     * @param to    Fecha y hora de expiración de la promoción.
     */
    public Discount(double value, String desc, LocalDateTime from, LocalDateTime to) {
        this.value = value;
        this.description = desc;
        this.from = from;
        this.to = to;
    }

    /**
     * Determina si el descuento ha expirado o si aún no ha entrado en vigor
     * comparando el rango de fechas con la hora actual del sistema.
     * @return true si la fecha actual es anterior a {@code from} o posterior a {@code to}.
     */
    @Override
    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(to) || now.isBefore(from);
    }
}