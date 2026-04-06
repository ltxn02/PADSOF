package transactions;
import utils.*;
import users.*;
import catalog.*;

/**
 * Clase para representar los intercambios
 * @author Taha Ridda
 * @version 1.0
 *
 */

public class Exchange {
    private static int lastExchangeId = 1;
    private int exchangeId;
    private ExchangeOffer associatedOffer;
    private ExchangeStatus status;
    private Employee processedBy;
    /**
     * Constructor para el intercambio
     * @param  offer la oferta de intercamcbio
     * */
    public Exchange(ExchangeOffer offer) {
    	if(!offer.ofertaaceptada()) {
    		throw new IllegalArgumentException("Cannot create an exchange from an offer that hasn't been accepted");
    	}
        this.associatedOffer = offer;
        this.status = ExchangeStatus.EN_PROCESO;
        this.exchangeId = Exchange.lastExchangeId;
        Exchange.lastExchangeId++;
    }
    
    /**
     * Funcion para que el empleado valide un intercambio ofertado, ya aceptado
     * @param e El empleado que hace la validación del intercambio
     * @return retorna true si se valida el intercamio y false si no se intercambia
     * */
    public void validateExchange(Employee e) throws IllegalStateException {
    	if(!associatedOffer.ofertaaceptada()) {
    		throw new IllegalStateException("Cannot validate an offer that hasn't been accepted");
    	}
        this.processedBy = e;
        this.status = ExchangeStatus.COMPLETADO;
        this.associatedOffer.intercambiar_propietarios();
    }
    
    public void cancelExchange(Employee e) throws IllegalStateException {
    	if(this.status != ExchangeStatus.EN_PROCESO) {
    		throw new IllegalStateException("Cannot cancel an exchange that is not in progress");
    	}
    	this.processedBy = e;
    	this.status = ExchangeStatus.CANCELADO;
    	this.associatedOffer.liberarProductos();
    }
    
    public boolean isExchange(ExchangeStatus status) {
    	return this.status == status;
    }
    
    public boolean isThisExchangeOffer(ExchangeOffer offer) {
    	return this.associatedOffer == offer;
    }
    
    public ExchangeOffer getAssociatedOffer() {
    	return this.associatedOffer;
    }
    
    public int getExchangeId() {
    	return this.exchangeId;
    }
}
