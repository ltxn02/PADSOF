package model.transactions;

import model.catalog.SecondHandProduct;
import model.user.Client;
import util.ExchangeStatus;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona una oferta de intercambio entre dos clientes.
 * Contiene la lógica para proponer un lote de productos a cambio de uno deseado,
 * controlando los estados de aceptación, rechazo y expiración.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public class ExchangeOffer {
    private static int lastOfferId = 1;
    private final int offerId;
    private final LocalDateTime createDate;
    private static Duration timeOnHold = Duration.ofDays(7);

    private final SecondHandProduct requestedProduct;
    private final List<SecondHandProduct> offeredProducts;
    private final Client offeror;
    private final Client receptor;
    private ExchangeStatus status;

    public ExchangeOffer(SecondHandProduct requestedProduct, List<SecondHandProduct> offeredProducts, Client offeror) {
        if (requestedProduct == null || offeredProducts == null || offeredProducts.isEmpty() || offeror == null) {
            throw new IllegalArgumentException("Datos de oferta incompletos.");
        }

        this.offerId = ExchangeOffer.lastOfferId++;
        this.createDate = LocalDateTime.now();
        this.status = ExchangeStatus.PENDIENTE;

        this.requestedProduct = requestedProduct;
        this.offeredProducts = new ArrayList<>(offeredProducts);
        this.offeror = offeror;
        this.receptor = requestedProduct.getOwner();

        for (SecondHandProduct p : this.offeredProducts) {
            p.change_offered_status(true);
        }

        this.receptor.receiveOffer(this);
    }

    public boolean isExpired() {
        return Duration.between(createDate, LocalDateTime.now()).compareTo(timeOnHold) > 0;
    }

    public boolean isAllAvailable() {
        if (!requestedProduct.isAvailable()) return false;
        for (SecondHandProduct p : offeredProducts) {
            if (!p.isAvailable()) return false;
        }
        return true;
    }

    public synchronized void ejecutarIntercambio() {
        if (this.status != ExchangeStatus.ACEPTADA) {
            throw new IllegalStateException("Solo se puede ejecutar un intercambio aceptado.");
        }

        this.requestedProduct.change_owners(this.offeror);

        for (SecondHandProduct p : offeredProducts) {
            p.change_owners(this.receptor);
        }

        liberarProductos();
        this.status = ExchangeStatus.ACEPTADA;
    }

    public void cancelarOferta() {
        if (this.status == ExchangeStatus.PENDIENTE) {
            this.status = ExchangeStatus.CANCELADA;
            liberarProductosOfertados();
        }
    }

    public ExchangeStatus getStatus() {
        return status;
    }

    /**
     * Acepta la oferta de intercambio y ejecuta la transferencia de productos.
     * @throws IllegalStateException si la oferta no está en estado PENDIENTE.
     */
    public synchronized void aceptarOferta() {
        if (this.status != ExchangeStatus.PENDIENTE) {
            throw new IllegalStateException("Solo se puede aceptar una oferta que esté PENDIENTE.");
        }

        if (!isAllAvailable()) {
            this.status = ExchangeStatus.CANCELADA;
            liberarProductos();
            System.out.println("[!] Error: Algunos productos ya no están disponibles. Oferta cancelada.");
            return;
        }

        this.status = ExchangeStatus.ACEPTADA;

        this.requestedProduct.change_owners(this.offeror);

        for (SecondHandProduct p : this.offeredProducts) {
            p.change_owners(this.receptor);
        }

        liberarProductos();
        this.status = ExchangeStatus.ACEPTADA;

        System.out.println("[✔] Intercambio ejecutado: " + offeror.getUsername() + " <-> " + receptor.getUsername());
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public SecondHandProduct getRequestedProduct() {
        return requestedProduct;
    }

    public void rejectOffer() {
        if (this.status == ExchangeStatus.PENDIENTE) {
            this.status = ExchangeStatus.RECHAZADA;
            liberarProductosOfertados();
        }
    }

    private void liberarProductos() {
        this.requestedProduct.change_offered_status(false);
        liberarProductosOfertados();
    }

    private void liberarProductosOfertados() {
        for (SecondHandProduct p : offeredProducts) {
            p.change_offered_status(false);
        }
    }
    public boolean ofertaAceptada(){
        if (this.getStatus()== ExchangeStatus.ACEPTADA){
            return true;
        }   else {
            return false;
        }
    }
    public int getOfferId() { return offerId; }
    public Client getOfferor() { return offeror; }
    public Client getReceptor() { return receptor; }
    public List<SecondHandProduct> getOfferedProducts() { return offeredProducts; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- OFERTA #").append(offerId).append(" ---\n");
        sb.append("Producto Deseado: ").append(requestedProduct.getName()).append(" (").append(requestedProduct.getPrice()).append("€)\n");
        sb.append("Lote Ofrecido (").append(offeredProducts.size()).append(" productos):\n");
        for (SecondHandProduct p : offeredProducts) {
            sb.append("  - ").append(p.getName()).append(" (").append(p.getPrice()).append("€)\n");
        }
        sb.append("Estado: ").append(status);
        return sb.toString();
    }
}