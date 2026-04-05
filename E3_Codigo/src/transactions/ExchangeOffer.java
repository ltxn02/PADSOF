package transactions;

/**
 * Clase para representar las ofertas de intercambio
 *  @author Taha Ridda
 * @version 2.0
 *
 */

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import utils.*;
import users.*;
import catalog.*;


public class ExchangeOffer {
    private static int lastOfferId = 1;
    private int offerId;
    private LocalDateTime createDate;
    private static Duration timeonHold = Duration.ofDays(7);
    private SecondHandProduct requestedProduct;
    private List<SecondHandProduct> offeredProducts;
    private Client offeror;
    private Client receptor;
    private ExchangeStatus status;

    public Client getOfferor() {
        return offeror;
    }

    public Client getReceptor() {
        return receptor;
    }

    public boolean is_Expired(){
        Duration TiempoTranscurrido = Duration.between(createDate, LocalDateTime.now());
        return TiempoTranscurrido.compareTo(timeonHold) > 0;
    }

    /**
     * Constructor de la oferta de intercambio
     * @param requestedProduct producto solicitado
     * @param offeredProducts productos ofertados para el intercambio
     * @param offeror usuario que solicita el intercambio
     * */

    public ExchangeOffer(SecondHandProduct requestedProduct, ArrayList<SecondHandProduct> offeredProducts, Client offeror){
        this.status = ExchangeStatus.PENDIENTE;
        this.offeredProducts= offeredProducts;
        this.requestedProduct= requestedProduct;
        this.offeror= offeror;
        this.receptor= requestedProduct.getOwner();
        this.createDate= LocalDateTime.now();

        for (SecondHandProduct p : this.offeredProducts){
            p.change_offered_status(true);
        }
        this.receptor.receiveOffer(this);
        
        this.offerId = ExchangeOffer.lastOfferId;
        ExchangeOffer.lastOfferId++;
    }
    /**
     * Funcion para cancelar una oferta sobre un producto
     * */
    public void cancelOffer() throws IllegalStateException {
        if(this.status != ExchangeStatus.PENDIENTE) {
        	throw new IllegalStateException("Can only cancel an order that is pending");
        }
    	this.status = ExchangeStatus.CANCELADA;
        for (SecondHandProduct p: this.offeredProducts) {
            p.change_offered_status(false);
        }
    }
/**
 * Funcion para rechazar la oferta
 *
 *
 * */
    public void reject_offer(){
        this.status = ExchangeStatus.RECHAZADA;
        this.liberarProductosofertados();

    }
    /**
     * Funcion que caduca las ofertas que quedan pendientes
     * */
    public void expired_offer(){
        if(is_Expired()){
            this.status = ExchangeStatus.EXPIRADA;
            this.liberarProductosofertados();
        }
    }

    public boolean ofertaaceptada(){
        if (this.status == ExchangeStatus.ACEPTADA){
        	return true;
        }
        return false;
    }

    public void aceptaroferta(){
        this.status = ExchangeStatus.ACEPTADA;
    }

    /**
     * funcion para liberar los productos ofertados en un intercambio
     * */


    public boolean intercambiar_propietarios() {
        requestedProduct.change_owners(this.offeror);

        for (SecondHandProduct p : offeredProducts) {
            p.change_owners(this.receptor);
        }
        liberarProductos();
        return true;
    }

    public boolean liberarProductos(){
        this.requestedProduct.change_offered_status(false);
        liberarProductosofertados();
        return true;
    }
    public boolean liberarProductosofertados(){
        for (SecondHandProduct p: offeredProducts){
            p.change_offered_status(false);
        }
        return true;
    }
    public boolean isAllAvailable(){
        if(!this.requestedProduct.isAvailable()){
            return false;
        }
        for(SecondHandProduct p: offeredProducts){
            if (!p.isAvailable()){
                return false;
            }
        }
        return true;

    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public ExchangeStatus getEstado() {
        return status;
    }

    public SecondHandProduct getRequestedProduct() {
        return requestedProduct;
    }
    
    public String offerMadePreview() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("  #" + this.offerId + " | ");
    	sb.append("to: " + this.receptor + " | ");
    	sb.append(this.status);
    	
    	return sb.toString();
    }
    
    public String offerReceivedPreview() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("  #" + this.offerId + " | ");
    	sb.append("from: " + this.offeror + " | ");
    	sb.append(this.status);
    	
    	return sb.toString();
    }
    
    @Override
    public String toString(){
        return "Fecha: " + this.createDate + "\nOferta por: " + this.requestedProduct
                + "\nEstado de la oferta: " + this.status
                + "\nOferta recibida por: " + this.receptor
                +"\nProductos ofertados: " + this.offeredProducts
                + "\nTiempo de oferta: " + this.timeonHold ;

    }
}
