package transactions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import products.*;
import utils.*;
import users.*;

/**
 * Clase que representa una propuesta formal de trueque entre dos clientes.
 * Gestiona el ciclo de vida de una oferta, desde su creación y el bloqueo temporal
 * de los productos involucrados, hasta su resolución mediante aceptación,
 * rechazo, cancelación o expiración por tiempo.
 * @author Taha Ridda En Naji
 * @version 2.0
 */
public class ExchangeOffer implements java.io.Serializable {
    private static int lastOfferId = 1;
    private int offerId;
    private LocalDateTime createDate;

    private static Duration timeonHold = Duration.ofDays(7);

    private SecondHandProduct requestedProduct;
    private List<SecondHandProduct> offeredProducts;
    private Client offeror;
    private Client receptor;
    private ExchangeOfferStatus status;

    /**
     * Constructor para inicializar una nueva oferta de intercambio.
     * Al crearse, la oferta bloquea automáticamente los productos ofrecidos para
     * evitar que sean utilizados en otras transacciones simultáneas.
     * @param requestedProduct Producto de segunda mano que se desea obtener.
     * @param offeredProducts  Lista de productos que el ofertante ofrece a cambio.
     * @param offeror          {@link Client} que inicia la propuesta.
     */
    public ExchangeOffer(SecondHandProduct requestedProduct, ArrayList<SecondHandProduct> offeredProducts, Client offeror){
        this.status = ExchangeOfferStatus.PENDIENTE;
        this.offeredProducts = offeredProducts;
        this.requestedProduct = requestedProduct;
        this.offeror = offeror;
        this.receptor = requestedProduct.getOwner();
        this.createDate = LocalDateTime.now();

        for (SecondHandProduct p : this.offeredProducts){
            p.change_offered_status(true);
        }
        this.receptor.receiveOffer(this);

        this.offerId = ExchangeOffer.lastOfferId;
        ExchangeOffer.lastOfferId++;
    }

    /**
     * Verifica si la oferta ha superado el tiempo límite de espera establecido.
     * @return true si el tiempo transcurrido es mayor a {@code timeonHold}.
     */
    public boolean is_Expired(){
        Duration tiempoTranscurrido = Duration.between(createDate, LocalDateTime.now());
        return tiempoTranscurrido.compareTo(ExchangeOffer.timeonHold) > 0;
    }

    /**
     * Cancela la oferta por iniciativa del ofertante.
     * Solo es posible si la oferta se encuentra en estado PENDIENTE.
     * @throws IllegalStateException Si la oferta ya ha sido procesada o cancelada.
     */
    public void cancelOffer() throws IllegalStateException {
        if(this.status != ExchangeOfferStatus.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden cancelar ofertas en estado pendiente.");
        }
        this.status = ExchangeOfferStatus.CANCELADA;
        this.liberarProductosofertados();
    }

    /**
     * Rechaza la oferta por parte del receptor.
     * Cambia el estado y libera los productos del ofertante para su uso en el catálogo.
     */
    public void reject_offer(){
        this.status = ExchangeOfferStatus.RECHAZADA;
        this.liberarProductosofertados();
    }

    /**
     * Procesa la expiración de la oferta si el tiempo de validez ha concluido.
     */
    public void expired_offer(){
        if(is_Expired()){
            this.status = ExchangeOfferStatus.EXPIRADA;
            this.liberarProductosofertados();
        }
    }

    /**
     * Indica si la oferta ha sido aceptada satisfactoriamente.
     * @return true si el estado es ACEPTADA.
     */
    public boolean ofertaaceptada() {
        return this.status == ExchangeOfferStatus.ACEPTADA;
    }

    /**
     * Acepta formalmente la oferta e inicia la creación de un objeto {@link Exchange}.
     * Vincula el nuevo intercambio a los historiales de ambos clientes.
     */
    public void aceptaroferta(){
        this.status = ExchangeOfferStatus.ACEPTADA;
        try {
            Exchange exchange = new Exchange(this);
            this.offeror.addExchange(exchange);
            this.receptor.addExchange(exchange);
        } catch (Exception e) {
            System.err.println("Error al generar el intercambio técnico: " + e.getMessage());
        }
    }

    /**
     * Realiza el traspaso efectivo de la propiedad de todos los productos involucrados.
     * El producto solicitado pasa al ofertante y los ofrecidos al receptor.
     * @return true tras completar la operación de cambio de dueño.
     */
    public boolean intercambiar_propietarios() {
        requestedProduct.change_owners(this.offeror);
        for (SecondHandProduct p : offeredProducts) {
            p.change_owners(this.receptor);
        }
        this.liberarProductos();
        return true;
    }

    /**
     * Libera el estado de "ofrecido" tanto del producto solicitado como de los ofertados.
     * @return true al finalizar la liberación de bloqueos.
     */
    public boolean liberarProductos() {
        this.requestedProduct.change_offered_status(false);
        this.liberarProductosofertados();
        return true;
    }

    /**
     * Libera exclusivamente el lote de productos que el ofertante puso a disposición.
     * @return true tras desbloquear el lote.
     */
    public boolean liberarProductosofertados() {
        for (SecondHandProduct p: offeredProducts){
            p.change_offered_status(false);
        }
        return true;
    }

    /**
     * Verifica que todos los ítems involucrados sigan disponibles para el intercambio.
     * @return true si ninguno de los productos está bloqueado por otra oferta.
     */
    public boolean isAllAvailable(){
        if(!this.requestedProduct.isAvailable()) return false;
        for(SecondHandProduct p: offeredProducts){
            if (!p.isAvailable()) return false;
        }
        return true;
    }

    /**
     * Genera una previsualización de la oferta para el emisor.
     * @return String con ID, destinatario y estado.
     */
    public String offerMadePreview() {
        return "  #" + this.offerId + " | to: " + this.receptor.getUsername() + " | " + this.status;
    }

    /**
     * Genera una previsualización de la oferta para el receptor.
     * @return String con ID, remitente y estado.
     */
    public String offerReceivedPreview() {
        return "  #" + this.offerId + " | from: " + this.offeror.getUsername() + " | " + this.status;
    }

    /**
     * Muestra la propuesta detallada enfocada en la experiencia del cliente.
     * Incluye valores económicos y condiciones de los productos.
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
     * Muestra la auditoría técnica de la oferta para niveles de gestión.
     * Incluye trazabilidad de IDs y verificación de disponibilidad técnica.
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
        System.out.println("    - Ofertante: " + this.offeror.getUsername());
        System.out.println("    - Receptor:  " + this.receptor.getUsername());
        System.out.println("\n  VERIFICACIÓN DE DISPONIBILIDAD:");
        System.out.println("    - Item Solicitado: " + (requestedProduct.isAvailable() ? "SÍ" : "BLOQUEADO"));
        boolean todosDisp = isAllAvailable();
        System.out.println("    - Lote Ofrecido:   " + (todosDisp ? "SÍ" : "REVISAR ITEMS"));
        System.out.println(marca + "\n");
    }

    /**
     *
     * Getters
     */
    public Client getOfferor() { return this.offeror; }
    public Client getReceptor() { return this.receptor; }
    public LocalDateTime getCreateDate() { return this.createDate; }
    public ExchangeOfferStatus getEstado() { return this.status; }
    public SecondHandProduct getRequestedProduct() { return this.requestedProduct; }

    public boolean isRequestedProduct(SecondHandProduct product) {
        if(product == this.requestedProduct){
            return true;
        }
        return false;
    }
}