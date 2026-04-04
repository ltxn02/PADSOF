import java.time.Instant;
import java.util.*;
import java.util.Random;
import es.uam.eps.padsof.telecard.*;

public class Order {
    private static int lastOrderId = 1;
    private int orderId;
    private Instant orderedAt;
    private Client client;
    private List<CartItem> items;
    private Instant paidAt;
    private double price;
    private String pickupCode;
    private OrderStatus orderStatus;

    public Order(Client client, List<CartItem> cartItems, double price) {
        this.client = client;
        this.price = price;
        this.orderStatus = OrderStatus.SIN_PAGAR;
        this.pickupCode = generateCode();
        this.items = cartItems;
        this.orderId = Order.lastOrderId;
        Order.lastOrderId++;
    }

    public String generateCode() {
        Random rand = new Random();
        // Genera un número del 0 al 99999
        int numero = rand.nextInt(100000);
        // Lo formatea para que siempre tenga 5 dígitos (ej. si sale 42, pondrá 00042) y le pone la 'C' delante
        return String.format("C%05d", numero);
    }

    /**
     * Intenta cobrar el pedido usando la pasarela del profesor.
     * @param numeroTarjeta El número de tarjeta de 16 dígitos.
     * @return true si el pago es exitoso, false si falla.
     */
    public boolean procesarPago(String numeroTarjeta) {
        // 1. Validar la tarjeta antes de cobrar
        if (!TeleChargeAndPaySystem.isValidCardNumber(numeroTarjeta)) {
            System.out.println("Error: El número de tarjeta no es válido (debe tener 16 dígitos).");
            return false;
        }

        // 2. Intentar hacer el cargo con un try-catch
        try {
            TeleChargeAndPaySystem.charge(numeroTarjeta, "Pedido Rongero #" + this.orderId, this.price, true);

            // Si llega aquí, el pago ha funcionado
            this.orderStatus = OrderStatus.EN_PREPARACION;
            this.paidAt = Instant.now();
            System.out.println("¡Pago aceptado! El pedido pasa a estar en preparación.");
            return true;

        } catch (InvalidCardNumberException e) {
            System.out.println("Pago rechazado: Tarjeta inválida.");
        } catch (FailedInternetConnectionException e) { // <-- ¡LA HIJA PRIMERO!
            System.out.println("Pago fallido: Error de conexión con el banco. Inténtalo más tarde.");
        } catch (OrderRejectedException e) {            // <-- ¡EL PADRE DESPUÉS!
            System.out.println("Pago rechazado: El banco ha denegado la operación.");
        }

        // Si cae en algún catch, el pago falló y cancelamos el pedido
        /*
        LO PONEMOS COMO SIN PAGAR O AÑADIMOS EL STATUS CANCELADO?????
        */
        this.orderStatus = OrderStatus.CANCELADO;
        return false;
    }

    public List<CartItem> getItems() {
        return items;
    }
    
    public boolean hasProduct(NewProduct p) {
    	for(CartItem c: this.items) {
    		if(c.isProduct(p)) {
    			return true;
    		}
    	}
    	return false;
    }

    public String getPickupCode() {
        return this.pickupCode;
    }
    
    public void cancelOrder() throws IllegalStateException {
    	if(this.orderStatus == OrderStatus.EN_PREPARACION || this.orderStatus == OrderStatus.ENTREGADO) {
    		throw new IllegalStateException("A paid order cannot be cancelled");
    	}
    	this.orderStatus = OrderStatus.CANCELADO;
    }

    public double getPrice() {
        return price;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Instant getOrderedAt() {
        return orderedAt;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(this.orderId)
                .append(" | Estado: ").append(this.orderStatus)
                .append(" | Total: ").append(this.price).append("€\n");

        if (this.paidAt != null) {
            sb.append("   Pagado el: ").append(this.paidAt).append("\n");
            sb.append("   Código de recogida: ").append(this.pickupCode).append("\n");
        }

        sb.append("   Artículos:\n");
        for (CartItem item : items) {
            sb.append("    - ").append(item.getQuantity()).append("x ").append(item.getProduct().getName()).append("\n");
        }
        return sb.toString();
    }
}
