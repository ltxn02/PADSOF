/**
 * Clase para representar las ofertas de intercambio
 *  @author Taha Ridda
 * @version 1.0
 *
 */

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Exchangeoffer {
    private static int offerid= 0;
    private LocalDateTime createDate;
    private Duration timeonHold;
    private SecondHandProduct requestedProduct;
    private ArrayList<SecondHandProduct>  offeredProducts;
    private Client comprador;
    private Client recibidor;
    private ExchangeStatus Estado;

    public Client getComprador() {
        return comprador;
    }

    public Client getRecibidor() {
        return recibidor;
    }

    public boolean is_Expired(){
        Duration TiempoTranscurrido= Duration.between(createDate, LocalDateTime.now());
        return TiempoTranscurrido.compareTo(timeonHold) > 0;
    }

    /**
     * Constructor de la oferta de intercambio
     * @param requestedProduct producto solicitado
     * @param offeredProducts productos ofertados para el intercambio
     * @param comprador usuario que solicita el intercambio
     * @param recibidor usuario que recibe el intercambio
     * @param limit limite de la oferta que puede estar pendiente
     * */

    public Exchangeoffer( SecondHandProduct requestedProduct, ArrayList<SecondHandProduct> offeredProducts, Client comprador, Client recibidor, Duration limit){
        this.Estado= ExchangeStatus.PENDIENTE;
        this.offerid++;
        this.offeredProducts= offeredProducts;
        this.requestedProduct= requestedProduct;
        this.comprador= comprador;
        this.recibidor= recibidor;
        this.timeonHold= limit;
        this.createDate= LocalDateTime.now();

        for (SecondHandProduct p : this.offeredProducts){
            p.change_offered_status(true);
        }
        this.getComprador().registrarOfertaRealizada(this);
        /* offer no existe */
        offer.getRecibidor().registrarOfertaRecibida(this);
    }
    /**
     * Funcion para cancelar una oferta sobre un producto
     * */
    public void cancelar_oferta(){
        this.Estado = ExchangeStatus.CANCELADA;
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
        this.Estado= ExchangeStatus.RECHAZADA;
        this.liberarProductosofertados();

    }
    /**
     * Funcion que caduca las ofertas que quedan pendientes
     * */
    public void expired_offer(){
        if(is_Expired()){
            this.Estado= ExchangeStatus.EXPIRADA;
            this.liberarProductosofertados();;
    }
    }

    public boolean ofertaaceptada(){
        if (this.Estado == ExchangeStatus.ACEPTADA){
        return true;
        }
        return false;
    }

    public void aceptaroferta(){
        this.Estado= ExchangeStatus.ACEPTADA;
    }

    /**
     * funcion para liberar los productos ofertados en un intercambio
     * */


    public boolean intercambiar_propietarios() {
        requestedProduct.change_owners(this.comprador);

        for (SecondHandProduct p : offeredProducts) {
            p.change_owners(this.recibidor);
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
    public boolean estátodoDisponible(){
        if(!this.requestedProduct.estádisponible()){
            return false;
        }
        for(SecondHandProduct p: offeredProducts){
            if (!p.estádisponible()){
                return false;
            }
        }
        return true;

    }

    public SecondHandProduct getRequestedProduct() {
        return requestedProduct;
    }

    @Override
    public String toString(){
        return "Estado de la oferta: " + this.Estado;
    }


}
