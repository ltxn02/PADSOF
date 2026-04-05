package model.transactions;
import java.util.ArrayList;
/**
 * Clase que actúa como repositorio de los intercambios realizados.
 * Gestiona el almacenamiento y la consulta del historial de transacciones
 * de segunda mano que han sido validadas y completadas con éxito.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class ExchangeHistoric {
    private ArrayList<Exchange> exchanges;
    /**
     * Constructor por defecto.
     * Inicializa un historial de intercambios vacío.
     * * @author Taha Ridda En Naji
     * @version 3.0
     */
    public ExchangeHistoric() {
        this.exchanges = new ArrayList<>();
    }
    /**
     * Constructor que permite inicializar el historial con una lista de intercambios existente.
     * * @param exchanges Lista inicial de objetos Exchange.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public ExchangeHistoric(ArrayList<Exchange> exchanges) {
        this.exchanges = exchanges;
    }
    /**
     * Obtiene la lista completa de intercambios registrados en el historial.
     * * @return ArrayList con todos los objetos Exchange almacenados.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public ArrayList<Exchange> getExchanges() {
        return exchanges;
    }

    /**
     * Reemplaza el historial actual por una nueva lista de intercambios.
     * * @param exchanges La nueva lista de intercambios a asignar.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public void setExchanges(ArrayList<Exchange> exchanges) {
        this.exchanges = exchanges;
    }
    /**
     * Registra un nuevo intercambio finalizado en el historial.
     * Este método debe invocarse una vez que un empleado ha validado la transacción.
     * * @param exchange El objeto Exchange que representa la transacción completada.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public void addExchange(Exchange exchange) {
        this.exchanges.add(exchange);
    }
    /**
     * Elimina un registro de intercambio específico del historial.
     * Útil para labores de mantenimiento o corrección de errores administrativos.
     * * @param exchange El objeto Exchange que se desea retirar del registro.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public void removeExchange(Exchange exchange) {
        this.exchanges.remove(exchange);
    }
}
