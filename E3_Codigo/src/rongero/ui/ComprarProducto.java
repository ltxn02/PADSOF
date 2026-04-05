package rongero.ui;

import logic.Application;
import model.catalog.NewProduct;
import model.user.Client;
import java.util.Scanner;
import java.util.ArrayList;

public class ComprarProducto {
    public static void ComprarProducto(Client cliente, Scanner scanner) {
        System.out.println("\n--- CATÁLOGO DE PRODUCTOS ---");

        ArrayList<NewProduct> productos = Application.getInstance().getCatalog();

        if (productos.isEmpty()) {
            System.out.println("Actualmente no hay productos en la tienda.");
            return;
        }

        // 1. Mostramos los productos con un índice (1, 2, 3...)
        for (int i = 0; i < productos.size(); i++) {
            NewProduct p = productos.get(i);
            System.out.println((i + 1) + ".- " + p.getName() + " | Precio: " + p.getPrice() + "€");
        }
        System.out.println("0.- Volver al menú anterior");

        System.out.print("\nElige el número del producto que deseas añadir al carrito (0 para salir): ");
        String inputIndex = scanner.nextLine();

        try {
            int index = Integer.parseInt(inputIndex);

            if (index == 0) {
                return; // El usuario ha elegido volver
            }

            if (index < 1 || index > productos.size()) {
                System.out.println("[!] Opción no válida. Producto no encontrado.");
                return;
            }

            // Obtenemos el producto seleccionado (restamos 1 porque los arrays empiezan en 0)
            NewProduct selectedProduct = productos.get(index - 1);

            System.out.print("¿Cuántas unidades de '" + selectedProduct.getName() + "' deseas añadir? ");
            String inputQty = scanner.nextLine();
            int quantity = Integer.parseInt(inputQty);

            if (quantity <= 0) {
                System.out.println("[!] La cantidad debe ser mayor que 0.");
                return;
            }

            // 2. Añadimos el producto al carrito del cliente
            // Funciona porque Product hereda de NewProduct (que es lo que pide addCartItem)
            cliente.getShoppingCart().addCartItem(selectedProduct, quantity);
            System.out.println("[+] ¡Éxito! " + quantity + "x " + selectedProduct.getName() + " añadido(s) a tu carrito.");

        } catch (NumberFormatException e) {
            // Capturamos el error si el usuario escribe texto en lugar de números
            System.out.println("[!] Error: Por favor, introduce un número válido.");
        } catch (IllegalArgumentException e) {
            // Capturamos el error de vuestra clase CartItem si no hay stock suficiente
            System.out.println("[!] Error de stock: " + e.getMessage());
        }
    }
}
