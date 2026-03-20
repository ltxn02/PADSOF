import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class Order {
    private static int lastId = 1;
    private int orderId;
    private Instant orderedAt;
    private ArrayList<NewProduct> items;
    private Instant paidAt;
    private double price;
    private String pickupCode;

    public Order(ArrayList<NewProduct> items, double price) {
        this.items = items;
        this.price = price;
        this.orderedAt = Instant.now();

    }

    private String generateCode() {
        Random rand = new Random();
        // Genera un número del 0 al 99999
        int numero = rand.nextInt(100000);
        // Lo formatea para que siempre tenga 5 dígitos (ej. si sale 42, pondrá 00042) y le pone la 'C' delante
        return String.format("C%05d", numero);
    }
}
