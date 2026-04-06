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


public class ExchangeOffer implements java.io.Serializable{
    private static int lastOfferId = 1;
    private int offerId;
    private LocalDateTime createDate;
    private static Duration timeonHold = Duration.ofDays(7);
    private SecondHandProduct requestedProduct;
    private List<SecondHandProduct> offeredProducts;
    private Client offeror;
    private Client receptor;
    private ExchangeOfferStatus status;

    public Client getOfferor() {
        return this.offeror;
    }

    public Client getReceptor() {
        return this.receptor;
    }

    public boolean is_Expired(){
        Duration TiempoTranscurrido = Duration.between(createDate, LocalDateTime.now());
        return TiempoTranscurrido.compareTo(ExchangeOffer.timeonHold) > 0;
    }

    /**
     * Constructor de la oferta de intercambio
     * @param requestedProduct producto solicitado
     * @param offeredProducts productos ofertados para el intercambio
     * @param offeror usuario que solicita el intercambio
     * */

    public ExchangeOffer(SecondHandProduct requestedProduct, ArrayList<SecondHandProduct> offeredProducts, Client offeror){
        this.status = ExchangeOfferStatus.PENDIENTE;
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
        if(this.status != ExchangeOfferStatus.PENDIENTE) {
        	throw new IllegalStateException("Can only cancel an order that is pending");
        }
    	this.status = ExchangeOfferStatus.CANCELADA;
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
        this.status = ExchangeOfferStatus.RECHAZADA;
        this.liberarProductosofertados();

    }
    /**
     * Funcion que caduca las ofertas que quedan pendientes
     * */
    public void expired_offer(){
        if(is_Expired()){
            this.status = ExchangeOfferStatus.EXPIRADA;
            this.liberarProductosofertados();
        }
    }

    public boolean ofertaaceptada() {
        if (this.status == ExchangeOfferStatus.ACEPTADA){
        	return true;
        }
        return false;
    }

    public void aceptaroferta(){
        this.status = ExchangeOfferStatus.ACEPTADA;
        
        try {
	        Exchange exchange = new Exchange(this);
	        
	        this.offeror.addExchange(exchange);
	        this.receptor.addExchange(exchange);
	        
        } catch (Exception e) {
        	System.err.println("Error creating new Exchange: " + e.getMessage());
        }
    }

    /**
     * funcion para liberar los productos ofertados en un intercambio
     * */


    public boolean intercambiar_propietarios() {
        requestedProduct.change_owners(this.offeror);

        for (SecondHandProduct p : offeredProducts) {
            p.change_owners(this.receptor);
        }
        this.liberarProductos();
        return true;
    }

    public boolean liberarProductos() {
        this.requestedProduct.change_offered_status(false);
        this.liberarProductosofertados();
        return true;
    }
    public boolean liberarProductosofertados() {
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
        return this.createDate;
    }

    public ExchangeOfferStatus getEstado() {
        return this.status;
    }

    public SecondHandProduct getRequestedProduct() {
        return this.requestedProduct;
    }
    
    public boolean isRequestedProduct(SecondHandProduct p) {
    	return this.requestedProduct == p;
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

    /**
     * Muestra la oferta desde el punto de vista de los clientes involucrados.
     * Enfocado en la claridad del trueque y el valor de los productos.
     */
    public void imprimirCliente() {
        String linea = "----------------------------------------------------------------";
        System.out.println("\n" + linea);
        System.out.println("                PROPUESTA DE INTERCAMBIO #" + this.offerId);
        System.out.println(linea);

        System.out.println("  OFERTANTE: " + this.offeror.getUsername());
        System.out.println("  RECEPTOR:  " + this.receptor.getUsername());
        System.out.println("  ESTADO:    [" + this.status + "]");
        System.out.println(linea);

        System.out.println("  LO QUE SE PIDE:");
        System.out.println("    => " + requestedProduct.getName() + " (" + requestedProduct.getPrice() + "€)");

        System.out.println("\n  LO QUE SE OFRECE A CAMBIO:");
        double suma = 0;
        for (SecondHandProduct p : offeredProducts) {
            System.out.println("    + " + p.getName() + " [" + p.getCondition() + "]");
            suma += p.getPrice();
        }
        System.out.printf("\n  VALOR TOTAL OFRECIDO: %.2f€\n", suma);
        System.out.println(linea + "\n");
    }

    /**
     * Muestra la oferta con detalles técnicos para empleados y managers.
     * Incluye control de tiempos y estados internos.
     */
    public void imprimirSuperior() {
        String marca = "################################################################";
        System.out.println("\n" + marca);
        System.out.println("  AUDITORÍA DE INTERCAMBIO - ID: EXCH-OFFER-" + this.offerId);
        System.out.println(marca);

        System.out.println("  REGISTRO:  " + this.createDate);
        System.out.println("  EXPIRADA:  " + (is_Expired() ? "SÍ" : "NO"));
        System.out.println("  ESTADO:    " + this.status);

        System.out.println("\n  TRAZABILIDAD DE USUARIOS:");
        System.out.println("    - ID Ofertante: " + this.offeror.getUsername() + " (Solicitante)");
        System.out.println("    - ID Receptor:  " + this.receptor.getUsername() + " (Dueño del ítem)");

        System.out.println("\n  VERIFICACIÓN DE DISPONIBILIDAD:");
        System.out.println("    - Item Solicitado disponible: " + (requestedProduct.isAvailable() ? "SÍ" : "NO (BLOQUEADO)"));
        boolean todosDisp = true;
        for(SecondHandProduct p : offeredProducts) if(!p.isAvailable()) todosDisp = false;
        System.out.println("    - Lote ofrecido disponible:   " + (todosDisp ? "SÍ" : "REVISAR ITEMS"));

        System.out.println(marca + "\n");
    }










}
