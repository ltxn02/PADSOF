package ui;

import java.util.Scanner;
import java.time.LocalDateTime;
import logic.Application;
import model.discounts.*;
import model.catalog.Item;
import model.catalog.Category;

/**
 * Interfaz de terminal para que el Manager configure las promociones del sistema.
 */
public class MenuCreacion {

    public static void mostrar(Scanner scanner) {
        IDiscountFactory factory = new StandardDiscountFactory();

        System.out.println("\n=====================================");
        System.out.println("   GESTOR DE PROMOCIONES Y DESCUENTOS");
        System.out.println("=====================================");
        System.out.println("1.- Rebaja Directa (%)");
        System.out.println("2.- Oferta por Cantidad (X por Y)");
        System.out.println("3.- Descuento por Volumen (Gasto Total)");
        System.out.println("4.- Promoción con Regalo");
        System.out.println("0.- Volver");
        System.out.print("Selecciona el tipo de promoción: ");

        String opcion = scanner.nextLine();
        if (opcion.equals("0")) return;

        try {
            switch (opcion) {
                case "1": // REBAJA PORCENTUAL
                    configurarRebaja(scanner, factory);
                    break;
                case "2": // CANTIDAD (3x2, 2x1...)
                    configurarCantidad(scanner, factory);
                    break;
                case "3": // VOLUMEN (Si gastas > X, ahorras Y)
                    configurarVolumen(scanner, factory);
                    break;
                case "4": // REGALO
                    configurarRegalo(scanner, factory);
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        } catch (Exception e) {
            System.out.println("[!] Error al configurar el descuento: " + e.getMessage());
        }
    }

    private static void configurarRebaja(Scanner scanner, IDiscountFactory factory) {
        System.out.print("Nombre del producto: ");
        Item p = Application.getInstance().buscarProducto(scanner.nextLine());

        if (p != null) {
            System.out.print("Porcentaje de rebaja (ej. 20): ");
            double pct = Double.parseDouble(scanner.nextLine());
            System.out.print("Descripción de la oferta: ");
            String desc = scanner.nextLine();

            IRebaja rebaja = factory.createPercentageDiscount(pct, desc);
            p.setDiscount(rebaja);
            System.out.println("[+] Rebaja aplicada con éxito a " + p.getName());
        } else {
            System.out.println("[!] Producto no encontrado.");
        }
    }

    private static void configurarCantidad(Scanner scanner, IDiscountFactory factory) {
        System.out.print("Nombre del producto: ");
        Item p = Application.getInstance().buscarProducto(scanner.nextLine());

        if (p != null) {
            System.out.print("¿Cuántos se lleva el cliente? (X): ");
            int x = Integer.parseInt(scanner.nextLine());
            System.out.print("¿Cuántos paga realmente? (Y): ");
            int y = Integer.parseInt(scanner.nextLine());
            System.out.print("Descripción (ej. Super 3x2): ");
            String desc = scanner.nextLine();

            ICantidad promo = factory.createQuantityDiscount(x, y, desc);
            p.setDiscount(promo);
            System.out.println("[+] Oferta " + x + "x" + y + " aplicada a " + p.getName());
        } else {
            System.out.println("[!] Producto no encontrado.");
        }
    }

    private static void configurarVolumen(Scanner scanner, IDiscountFactory factory) {
        System.out.print("Gasto mínimo en el carrito (€): ");
        double threshold = Double.parseDouble(scanner.nextLine());
        System.out.print("Euros a descontar del total (€): ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Descripción: ");
        String desc = scanner.nextLine();

        IVolumen vol = factory.createVolumeDiscount(threshold, amount, desc);
        Application.getInstance().addGlobalDiscount(vol);
        System.out.println("[+] Descuento de volumen activado globalmente.");
    }

    private static void configurarRegalo(Scanner scanner, IDiscountFactory factory) {
        System.out.print("Gasto mínimo para activar el regalo (€): ");
        double threshold = Double.parseDouble(scanner.nextLine());
        System.out.print("Nombre del producto que se regala: ");
        Item regalo = Application.getInstance().buscarProducto(scanner.nextLine());

        if (regalo != null) {
            System.out.print("Descripción de la promo: ");
            String desc = scanner.nextLine();

            IRegalo gift = factory.createGiftDiscount(threshold, regalo, desc);
            Application.getInstance().addGlobalDiscount((IVolumen) gift);
            System.out.println("[+] Promoción de regalo activada.");
        } else {
            System.out.println("[!] El producto para regalo no existe en el catálogo.");
        }
    }
}