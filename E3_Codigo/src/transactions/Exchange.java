package transactions;

import utils.*;
import products.*;
import users.*;

/**
 * Clase que representa la ejecución física y administrativa de un intercambio de productos.
 * Gestiona el ciclo de vida de la transacción una vez que una {@link ExchangeOffer} ha sido
 * aceptada por ambas partes, permitiendo su validación final o cancelación por parte de un empleado.
 * @author Taha Ridda En Naji
 * @version 2.0
 */
public class Exchange implements java.io.Serializable {
    private static int lastExchangeId = 1;
    private int exchangeId;
    private ExchangeOffer associatedOffer;
    private ExchangeStatus status;
    private Employee processedBy;

    /**
     * Constructor para inicializar un proceso de intercambio formal.
     * Crea un registro de intercambio en estado pendiente a partir de una oferta aceptada.
     * @param offer La {@link ExchangeOffer} que sirve como base para la transacción.
     * @throws IllegalArgumentException si la oferta proporcionada aún no ha sido aceptada por el destinatario.
     */
    public Exchange(ExchangeOffer offer) {
        if(!offer.ofertaaceptada()) {
            throw new IllegalArgumentException("No se puede crear un intercambio a partir de una oferta no aceptada.");
        }
        this.associatedOffer = offer;
        this.status = ExchangeStatus.EN_PROCESO;
        this.exchangeId = Exchange.lastExchangeId;
        Exchange.lastExchangeId++;
    }

    /**
     * Valida y finaliza el intercambio de productos.
     * Este método confirma la entrega física, cambia la titularidad de los productos
     * involucrados y registra al empleado responsable de la supervisión.
     * @param e El {@link Employee} que actúa como mediador y valida la operación.
     * @throws IllegalStateException si la oferta asociada no cumple con los requisitos de aceptación.
     */
    public void validateExchange(Employee e) throws IllegalStateException {
        if(!associatedOffer.ofertaaceptada()) {
            throw new IllegalStateException("No se puede validar una oferta que no ha sido aceptada previamente.");
        }
        this.processedBy = e;
        this.status = ExchangeStatus.COMPLETADO;
        this.associatedOffer.intercambiar_propietarios();
    }

    /**
     * Cancela el proceso de intercambio antes de su finalización.
     * Revierte el bloqueo de los productos involucrados para que vuelvan a estar
     * disponibles en el catálogo de sus dueños originales.
     * @param e El {@link Employee} que procesa la cancelación del intercambio.
     * @throws IllegalStateException si el intercambio ya ha sido completado o cancelado previamente.
     */
    public void cancelExchange(Employee e) throws IllegalStateException {
        if(this.status != ExchangeStatus.EN_PROCESO) {
            throw new IllegalStateException("Solo se pueden cancelar intercambios que estén actualmente en proceso.");
        }
        this.processedBy = e;
        this.status = ExchangeStatus.CANCELADO;
        this.associatedOffer.liberarProductos();
    }

    /**
     * Verifica si el intercambio se encuentra en un estado específico.
     * @param status El estado ({@link ExchangeStatus}) a comprobar.
     * @return true si coincide con el estado actual del intercambio.
     */
    public boolean isExchange(ExchangeStatus status) {
        return this.status == status;
    }

    /**
     * Comprueba si una oferta de intercambio coincide con la asociada a esta transacción.
     * @param offer La oferta a comparar.
     * @return true si es la misma instancia de oferta.
     */
    public boolean isThisExchangeOffer(ExchangeOffer offer) {
        return this.associatedOffer == offer;
    }

    /**
     * Obtiene la oferta de intercambio vinculada a esta ejecución.
     * @return El objeto {@link ExchangeOffer} asociado.
     */
    public ExchangeOffer getAssociatedOffer() {
        return this.associatedOffer;
    }

    /**
     * Obtiene el identificador único del intercambio.
     * @return El ID numérico autoincremental.
     */
    public int getExchangeId() {
        return this.exchangeId;
    }
}