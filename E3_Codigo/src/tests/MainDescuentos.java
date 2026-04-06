package tests;

import catalog.*;
import discounts.*;
import discounts.PercentageDiscount;
import transactions.*;
import utils.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class MainDescuentos {
    public static void main(String[] args) {
        StandardDiscountFactory factory = new StandardDiscountFactory();
        ShoppingCart carrito = new ShoppingCart();

        // --- SETUP DE DATOS AUXILIARES ---
        ArrayList<Category> catComics = new ArrayList<>();
        catComics.add(new Category("Manga", new ArrayList<>()));

        ArrayList<Category> catFiguras = new ArrayList<>();
        catFiguras.add(new Category("Coleccionables", new ArrayList<>()));

        ArrayList<Review> reviewsVacias = new ArrayList<>();
        ArrayList<String> autores = new ArrayList<>(Arrays.asList("Kentaro Miura"));
        ArrayList<String> mecanicas = new ArrayList<>(Arrays.asList("Estrategia", "Dados"));

        System.out.println("=== EJECUTANDO STRESS TEST: CATÁLOGO REAL (V4.2) ===");

        // --- 1. INSTANCIACIÓN DE PRODUCTOS REALES ---
        // Comic de 100€
        Comic berserkDeluxe = new Comic(
                "Berserk Deluxe Vol 1", "Edición Coleccionista", 100.0, "berserk.jpg",
                10, catComics, reviewsVacias, null, 600, "Dark Horse", 2019, autores
        );

        // Figura que servirá de regalo (0€)
        Figurine regaloFig = new Figurine(
                "Llavero Behelit", "Regalo exclusivo", 0.0, "behelit.jpg",
                1, catFiguras, reviewsVacias, null, 5.0, 3.0, 3.0, "Resina", "Berserk"
        );

        // --- 2. TEST 1: DESCUENTO EXPIRADO ---
        System.out.println("\n[TEST 1] Verificando Descuento Expirado...");
        PercentageDiscount caducado = new PercentageDiscount(50.0, "Oferta Antigua",
                LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5));
        berserkDeluxe.setDiscount(caducado);

        System.out.println("  Precio esperado (100.0) | Resultado: " + berserkDeluxe.getPriceWithDiscount() + "€");

        // --- 3. TEST 2: UMBRAL DE VOLUMEN (100€) ---
        System.out.println("\n[TEST 2] Verificando Límite de Gasto (Bono 10€ si gastas >= 100€)...");
        IVolumen bono10 = factory.createVolumeDiscount(100.0, 10.0, "Bono Bienvenida");
        carrito.addGlobalDiscount(bono10);

        // Añadimos el cómic de 100€
        carrito.addCartItem(berserkDeluxe, 1);
        System.out.println("  Gasto Bruto: " + carrito.getFullPrice() + "€");
        System.out.println("  Gasto Final (esperado 90.0): " + carrito.getPrice() + "€");

        // --- 4. TEST 3: STOCK DE REGALOS (FIGURINE) ---
        System.out.println("\n[TEST 3] Verificando Stock de Regalo (Figurine)...");
        IRegalo promoRegalo = factory.createGiftDiscount(50.0, regaloFig, "Regalo por compra");
        carrito.addGlobalDiscount((IVolumen) promoRegalo);

        System.out.println("  --- Cálculo inicial (Stock: 1) ---");
        carrito.getPrice();
        System.out.println("  Regalos en carrito: " + carrito.getGifts().size());

        // Agotamos el stock de la figura manualmente
        regaloFig.orderProduct(1);

        System.out.println("  --- Cálculo tras agotar stock (Stock: 0) ---");
        carrito.getPrice();
        if (carrito.getGifts().isEmpty()) {
            System.out.println("  [✔] ÉXITO: No se entregan figuras sin stock.");
        } else {
            System.out.println("  [X] ERROR: El sistema generó un regalo sin existencias.");
        }

        // --- 5. TEST 4: ACUMULACIÓN REBABA + VOLUMEN ---
        System.out.println("\n[TEST 4] Acumulación Rebaja de Comic + Bono Volumen...");
        carrito.clearCart(); // Limpiamos para el test final

        // 10% de rebaja en el item (100€ -> 90€)
        IRebaja rebajaValida = factory.createPercentageDiscount(10.0, "Semana Manga");
        berserkDeluxe.setDiscount(rebajaValida);

        // Al añadirlo, el fullPrice debe ser 90. Como 90 < 100, NO aplica el bono de 10€
        carrito.addCartItem(berserkDeluxe, 1);

        System.out.println("  Subtotal tras rebaja item: " + carrito.getFullPrice() + "€");
        System.out.println("  Total final (esperado 90.0, bono volumen NO aplicado): " + carrito.getPrice() + "€");

        if (carrito.getPrice() == 90.0) {
            System.out.println("  [✔] ÉXITO: El sistema prioriza el precio neto para el volumen.");
        } else {
            System.out.println("  [X] ERROR: El descuento de volumen se aplicó incorrectamente.");
        }

        System.out.println("\n=== STRESS TEST FINALIZADO ===");
    }
}