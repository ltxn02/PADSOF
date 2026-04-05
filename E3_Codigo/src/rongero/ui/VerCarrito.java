package rongero.ui;

import model.catalog.NewProduct;
import model.transactions.CartItem;
import model.transactions.Order;
import model.transactions.ShoppingCart;
import model.user.Client;
import java.util.Scanner;
public class VerCarrito {
    public static void VerCarrito(Client cliente, Scanner scanner) {
        System.out.println("\n--- TU CARRITO DE LA COMPRA ---");

        ShoppingCart carrito = cliente.getShoppingCart();

        // 1. Comprobamos si el carrito está vacío
        if (carrito.getCartItems().isEmpty()) {
            System.out.println("Tu carrito está vacío. ¡Ve al catálogo a añadir cosas!");
            return;
        }

        // 2. Mostramos los productos del carrito y el precio total
        double precioTotal = 0.0;
        for (CartItem item : carrito.getCartItems()) {
            // Asumiendo que CartItem tiene métodos para obtener el producto y la cantidad
            NewProduct p = item.getProduct();
            int cantidad = item.getQuantity();
            double subtotal = item.fullPrice();

            System.out.println("- " + cantidad + "x " + p.getName() + " | Subtotal: " + String.format(".2f",subtotal) + "€");
        }
        System.out.println("---------------------------------");
        System.out.println("TOTAL A PAGAR: " + precioTotal + "€");

        // 3. Preguntamos si quiere tramitar el pedido
        System.out.print("\n¿Deseas finalizar la compra y pagar ahora? (S/N): ");
        String respuesta = scanner.nextLine();

        if (!respuesta.equalsIgnoreCase("S")) {
            System.out.println("Compra aplazada. Los productos siguen en tu carrito.");
            return;
        }

        // 4. Proceso de Pago usando la librería (a través de tu clase Order)
        System.out.print("Introduce tu número de tarjeta de crédito (16 dígitos): ");
        String numeroTarjeta = scanner.nextLine();

        System.out.println(">> Conectando con la pasarela de pago...");

        // Creamos el pedido (asumo que tu constructor de Order recibe el cliente, el carrito y el total)
        Order nuevoPedido = new Order(cliente, carrito.getCartItems(), precioTotal);

        // Llamamos al metodo procesarPago que creaste antes en Order.java con sus try-catch
        boolean pagoExitoso = nuevoPedido.procesarPago(numeroTarjeta);

        if (pagoExitoso) {
            // Si la librería dice que OK, vaciamos el carrito y guardamos el pedido
            carrito.clearCart(); // Asegúrate de tener un metodo en ShoppingCart para vaciar la lista
            cliente.getOrderHistoric().addOrder(nuevoPedido); // Guardamos el pedido en el historial del cliente
            System.out.println("[+] ¡Compra finalizada con éxito! Tu código de recogida es: " + nuevoPedido.getPickupCode());
        } else {
            // Si la librería lanza excepción (tarjeta falsa, sin internet...), el pedido se cancela
            System.out.println("[!] La compra no se ha podido completar. Revisa tu método de pago.");
        }
    }
}
