import java.time.Instant;
import java.util.*;
import java.util.Random;
import es.uam.eps.padsof.telecard.*;

public class Order {
    private static int lastId = 1;
    private int orderId;
    private Instant orderedAt;
    private Client client;
    private ArrayList<Item> items;
    private Instant paidAt;
    private double price;
    private String pickupCode;
    private OrderStatus orderStatus;

    public Order(Client client, ShoppingCart cart, double price) {
        this.client = client;
        this.price = price;
        this.orderStatus = OrderStatus.SIN_PAGAR;
        this.orderId = lastId++;
        Order.lastId++;
        this.pickupCode = generateCode();

        this.items = new ArrayList<>();
        for (CartItem ci : cart.getCartItems()) {
            for (int i = 0; i < ci.getQuantity(); i++) {
                this.items.add((Item) ci.getProduct());
            }
        }
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

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getPickupCode() {
        return this.pickupCode;
    }
}
