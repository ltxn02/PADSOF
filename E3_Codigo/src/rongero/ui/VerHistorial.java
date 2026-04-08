package ui;
import model.user.Client;
import model.transactions.Order;

public class VerHistorial {
    public static void mostrar(Client cliente) {
        System.out.println("\n--- MI HISTORIAL DE COMPRAS ---");
        if (cliente.getOrderHistoric().getOrders().isEmpty()) {
            System.out.println("Aún no has realizado ninguna compra.");
            return;
        }

        for (Order o : cliente.getOrderHistoric().getOrders()) {
            System.out.println("Pedido #" + o.getPickupCode() +
                    " | Fecha: " + o.getOrderedAt() +
                    " | Total: " + o.getPrice() + "€");
        }
    }
}