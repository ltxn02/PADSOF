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

public class ExchangeOffer {
    private static int lastOfferId = 1;
    private LocalDateTime createDate;
    private static Duration timeonHold = Duration.ofDays(7);
    private SecondHandProduct requestedProduct;
    private ArrayList<SecondHandProduct> offeredProducts;
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
        Duration TiempoTranscurrido= Duration.between(createDate, LocalDateTime.now());
        return TiempoTranscurrido.compareTo(timeonHold) > 0;
    }

    /**
     * Constructor de la oferta de intercambio
     * @param requestedProduct producto solicitado
     * @param offeredProducts productos ofertados para el intercambio
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
        this.receptor.registrarOfertaRealizada(this);
        this.receptor.registrarOfertaRecibida(this);
        
        this.offerId = ExchangeOffer.lastOfferId;
        ExchangeOffer.lastOfferId++;
    }
    /**
     * Funcion para cancelar una oferta sobre un producto
     * */
    public void cancelar_oferta(){
        this.status = ExchangeStatus.CANCELADA;
        this.requestedProduct.change_offered_status(false);
        for (SecondHandProduct p: this.offeredProducts){
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
            this.liberarProductosofertados();;
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

    @Override
    public String toString(){
        return "Fecha: " + this.createDate + "\nOferta por: " + this.requestedProduct
                + "\nEstado de la oferta: " + this.status
                + "\nOferta recibida por: " + this.receptor
                +"\nProductos ofertados: " + this.offeredProducts
                + "\nTiempo de oferta: " + this.timeonHold ;

    }


}
