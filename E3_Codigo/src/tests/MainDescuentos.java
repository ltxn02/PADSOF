package tests;
import  utils.*;

import catalog.*;
import discounts.*;
import transactions.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainDescuentos {
    public static void main(String[] args) {
        // 1. Configuración de Fechas y Categorías (Evita Exception: Category cannot be empty)
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusYears(1);

        ArrayList<Category> cats = new ArrayList<>();
        cats.add(new Category("General", new ArrayList<>()));
        ArrayList<Review> reviews = new ArrayList<>();
        ArrayList<String> autores = new ArrayList<>();

        // 2. DESCUENTO 1: REBAJA PORCENTUAL (Pasado por constructor)
        // 10% de descuento. Si el comic vale 100€, el precio con descuento será 90€.
        PercentageDiscount rebaja10 = new PercentageDiscount(10.0, "Oferta Manga", inicio, fin);

        Comic berserk = new Comic(
                "Berserk", "Manga", 100.0, "b.jpg", 10,
                cats, reviews, rebaja10, 600, "Dark Horse", 2019, autores
        );

        // 3. DESCUENTO 2: REGALO (Pasado por constructor global)
        // Regalo de un llavero si se gastan más de 50€.
        Figurine llavero = new Figurine("Llavero", "R", 0.0, "l.jpg", 1, cats, reviews, null, 1, 1, 1, "M", "B");
        GiftDiscount promoRegalo = new GiftDiscount(50.0, llavero, "Regalo Oro", inicio, fin);

        // 4. DESCUENTO 3: BONO POR VOLUMEN
        // -10€ si la compra supera los 80€.
        VolumeDiscount bono80 = new VolumeDiscount(10.0, 80.0, "Bono Ahorro", inicio, fin);
        // 5. PRUEBAS EN EL CARRITO
        ShoppingCart carrito = new ShoppingCart();
        carrito.addGlobalDiscount(promoRegalo);
        carrito.addGlobalDiscount(bono80);

        System.out.println("=== EJECUTANDO TEST DE DESCUENTOS SIN SETTERS ===");

        // --- TEST 1: REBAJA INDIVIDUAL ---
        System.out.println("\n[TEST 1] Rebaja del Item:");
        System.out.println("  Precio Base: 100.0€");
        System.out.println("  Precio Rebajado (Esperado 90.0): " + berserk.getPriceWithDiscount() + "€");

        // --- TEST 2: BONO VOLUMEN ---
        carrito.addCartItem(berserk, 1); // Añadimos 1 Berserk (90€)
        double totalFinal = carrito.getPrice();
        // Explicación: 90€ neto > 80€ umbral -> 90€ - 10€ bono = 80€
        System.out.println("\n[TEST 2] Bono Volumen (90€ - 10€ bono):");
        System.out.println("  Total Final (Esperado 80.0): " + totalFinal + "€");

        // --- TEST 3: REGALO (STOCK 1) ---
        System.out.println("\n[TEST 3] Regalo Automático (Stock 1):");
        System.out.println("  Regalos detectados (Esperado 1): " + carrito.getGifts().size());


}}