package transactions;

import utils.*;
import users.*;

import java.util.ArrayList;

import products.*;

/**
 * Clase que representa el registro histórico de intercambios de un usuario o del sistema.
 * Actúa como un contenedor especializado para almacenar y gestionar una colección
 * de objetos {@link Exchange}, facilitando el seguimiento de transacciones pasadas
 * y en proceso.
 * @author Taha Ridda En Naji
 * @version 2.0
 */
public class ExchangeHistoric implements java.io.Serializable {

    private ArrayList<Exchange> exchanges;

    /**
     * Constructor por defecto.
     * Inicializa un nuevo historial de intercambios vacío.
     */
    public ExchangeHistoric() {
        this.exchanges = new ArrayList<>();
    }

    /**
     * Constructor que permite inicializar el historial con una lista preexistente.
     * @param exchanges Lista de objetos {@link Exchange} para cargar en el historial.
     */
    public ExchangeHistoric(ArrayList<Exchange> exchanges) {
        this.exchanges = exchanges;
    }

    /**
     * Obtiene la lista completa de intercambios registrados en este historial.
     * @return {@link ArrayList} con todos los intercambios almacenados.
     */
    public ArrayList<Exchange> getExchanges() {
        return exchanges;
    }

    /**
     * Reemplaza la lista actual de intercambios por una nueva colección.
     * @param exchanges La nueva lista de {@link Exchange} a establecer.
     */
    public void setExchanges(ArrayList<Exchange> exchanges) {
        this.exchanges = exchanges;
    }

    /**
     * Añade un nuevo intercambio al registro histórico.
     * @param exchange El objeto {@link Exchange} que se desea registrar.
     */
    public void addExchange(Exchange exchange) {
        this.exchanges.add(exchange);
    }

    /**
     * Elimina un intercambio específico del historial.
     * @param exchange El objeto {@link Exchange} que se desea remover.
     */
    public void removeExchange(Exchange exchange) {
        this.exchanges.remove(exchange);
    }

    /**
     * Verifica si un intercambio concreto ya existe dentro del registro histórico.
     * @param exchange El intercambio a buscar en la colección.
     * @return true si el intercambio está presente, false en caso contrario.
     */
    public boolean hasExchange(Exchange exchange) {
        return this.exchanges.contains(exchange);
    }
}