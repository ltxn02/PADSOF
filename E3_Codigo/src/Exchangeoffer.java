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
    public int offerid;
    public LocalDateTime createDate;
    public Duration timeonHold;
    public SecondHandProduct requestedProduct;
    public ArrayList<SecondHandProduct>  offeredProducts;
    public Client comprador;
    public Client recibidor;
    public ExchangeStatus Estado;


    public boolean is_Expired(){
        Duration TiempoTranscurrido= Duration.between(createDate, LocalDateTime.now());
        return TiempoTranscurrido.compareTo(timeonHold) > 0;
    }

    /**
     * Constructor de la oferta de intercambio
     * @param offerid id de la oferta
     * @param requestedProduct producto solicitado
     * @param offeredProducts productos ofertados para el intercambio
     * @param comprador usuario que solicita el intercambio
     * @param recibidor usuario que recibe el intercambio
     * @param limit limite de la oferta que puede estar pendiente
     * */

    public Exchangeoffer(int offerid, SecondHandProduct requestedProduct, ArrayList<SecondHandProduct> offeredProducts, Client comprador, Client recibidor, Duration limit){
        this.Estado= ExchangeStatus.PENDIENTE;
        this.offerid= offerid;
        this.offeredProducts= offeredProducts;
        this.requestedProduct= requestedProduct;
        this.comprador= comprador;
        this.recibidor= recibidor;
        this.timeonHold= limit;
        this.createDate= LocalDateTime.now();

        this.requestedProduct.isOffered= true;
        for (SecondHandProduct p : this.offeredProducts){
            p.isOffered = true;
        }


    }
    /**
     * Funcion para cancelar una oferta sobre un producto
     * */
    public void cancelar_oferta(){
        this.Estado = ExchangeStatus.CANCELADA;
        this.requestedProduct.isOffered = false;
        for (SecondHandProduct p: this.offeredProducts){
            p.isOffered = false;
        }

    }
/**
 * Funcion para rechazar la oferta
 *
 *
 * */
    public void reject_offer(){
        this.Estado= ExchangeStatus.RECHAZADA;
        this.liberar_productos();

    }
    /**
     * Funcion que caduca las ofertas que quedan pendientes
     * */
    public void expired_offer(){
        if(is_Expired()){
            this.Estado= ExchangeStatus.EXPIRADA;
            this.liberar_productos();;
    }
    }
    /**
     * funcion para liberar los productos ofertados en un intercambio
     * */
    private void liberar_productos(){
        this.requestedProduct.isOffered = false;
        for (SecondHandProduct p: this.offeredProducts){
            p.isOffered = false;
        }

    }


}
