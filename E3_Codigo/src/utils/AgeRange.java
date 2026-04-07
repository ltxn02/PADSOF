package utils;

/**
 * Clase que representa un rango de edad (AgeRange).
 * Se utiliza principalmente para definir las edades recomendadas de los productos
 * del catálogo, como los juegos de mesa. Hereda de {@link BaseElement} para heredar
 * su visibilidad y es serializable.
 *
 * @author Lidia Martín
 * @version 1.3
 */
public class AgeRange extends BaseElement implements java.io.Serializable {
    private String label;
    private int minAge;
    private int maxAge;

    /**
     * Constructor para inicializar un nuevo rango de edad.
     *
     * @param minAge Edad mínima recomendada.
     * @param maxAge Edad máxima recomendada.
     * @throws IllegalArgumentException Si alguna de las edades es negativa o si la edad mínima es mayor que la máxima.
     */
    public AgeRange(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < 0 || minAge > maxAge) {
            throw new IllegalArgumentException("Invalid age range");
        }
        this.label = minAge + "-" + maxAge;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    /**
     * Comprueba si este rango de edad es exactamente igual a los valores proporcionados.
     *
     * @param min Edad mínima a comparar.
     * @param max Edad máxima a comparar.
     * @return true si los límites coinciden exactamente, false en caso contrario.
     */
    public boolean equalTo(int min, int max) {
        return this.minAge == min && this.maxAge == max;
    }

    /**
     * Comprueba si este rango de edad es exactamente igual a otro objeto AgeRange.
     *
     * @param ageRange El otro rango de edad con el que se va a comparar.
     * @return true si ambos rangos son idénticos, false en caso contrario.
     */
    public boolean equalTo(AgeRange ageRange) {
        return this.minAge == ageRange.minAge && this.maxAge == ageRange.maxAge;
    }

    /**
     * Comprueba si este rango de edad está completamente contenido dentro de unos límites dados.
     *
     * @param min Límite inferior.
     * @param max Límite superior.
     * @return true si este rango no se sale de los límites proporcionados.
     */
    public boolean containedIn(int min, int max) {
        return this.minAge >= min && this.maxAge <= max;
    }

    /**
     * Comprueba si este rango de edad está completamente contenido dentro de otro AgeRange.
     *
     * @param ageRange El rango de edad contenedor.
     * @return true si este rango está dentro del rango proporcionado.
     */
    public boolean containedIn(AgeRange ageRange) {
        return this.minAge >= ageRange.minAge  && this.maxAge <= ageRange.maxAge;
    }

    /**
     * Cambia la visibilidad lógica de este rango de edad en el sistema.
     *
     * @param visible true para hacerlo visible, false para ocultarlo.
     */
    public void changeVisibility(boolean visible) {
        super.markAs(visible);
    }

    /**
     * Convierte una cadena de texto en un objeto AgeRange.
     * Es útil para la carga masiva de productos desde archivos CSV o TXT.
     *
     * @param rangeStr Cadena de texto con el formato "min-max" (ej: "10-99").
     * @return Una nueva instancia de AgeRange configurada con esos valores.
     * @throws IllegalArgumentException Si el formato de la cadena es incorrecto o no se puede procesar.
     */
    public static AgeRange stringToAgeRange(String rangeStr) {
        try {
            String[] parts = rangeStr.split("-");
            int min = Integer.parseInt(parts[0].trim());
            int max = Integer.parseInt(parts[1].trim());

            return new AgeRange(min, max);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid AgeRange format in file: " + rangeStr + ". Expected 'min-max'");
        }
    }
}