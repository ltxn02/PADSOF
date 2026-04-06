package util;

import model.discounts.*;
import model.catalog.*;
import model.transactions.ShoppingCart;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainDescuentos {
    public static void main(String[] args) {
        StandardDiscountFactory factory = new StandardDiscountFactory();
        ShoppingCart carrito = new ShoppingCart();
        List<Category> cats = new ArrayList<>();
        cats.add(new Category("Test", new ArrayList<>()));
        List<Review> revs = new ArrayList<>();

        System.out.println("=== INICIANDO STRESS TEST DE DESCUENTOS V4.0 ===");

        // 1. SETUP DE PRODUCTOS
        // Producto A: 100€, Stock 5
        NewProduct p1 = new NewProduct("Laptop Pro", "Caro", 100.0, "i.jpg", cats, 5, revs);
        // Regalo: Stock solo 1 unidad
        NewProduct regaloUnico = new NewProduct("Figura Oro", "Edicion Limitada", 0.0, "r.jpg", cats, 1, revs);

        // 2. FORZAR DESCUENTO EXPIRADO (Ayer a hace una hora)
        System.out.println("\n[TEST 1] Verificando Descuento Expirado...");
        PercentageDiscount descCaducado = new PercentageDiscount(50.0, "Oferta Caducada",
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1));
        p1.setDiscount(descCaducado);
        System.out.println("  Precio con 50% caducado (esperado 100): " + p1.getPriceWithDiscount() + "€");

        // 3. FORZAR UMBRAL DE VOLUMEN (Justo en el límite)
        System.out.println("\n[TEST 2] Verificando Límite de Gasto (Umbral)...");
        // Descuento: -10€ si gastas >= 100€
        IVolumen descVol = factory.createVolumeDiscount(100.0, 10.0, "Bono 100");
        carrito.addGlobalDiscount(descVol);

        // Añadimos producto de 100€
        carrito.addCartItem(p1, 1);
        System.out.println("  Gasto bruto: " + carrito.getFullPrice() + "€");
        System.out.println("  Gasto tras -10€ volumen (esperado 90): " + carrito.getPrice() + "€");

        // 4. FORZAR STOCK DE REGALOS (Agotar el stock)
        System.out.println("\n[TEST 3] Verificando Stock de Regalos (Agotamiento)...");
        // Promo: Regalo por gastar más de 20€
        IRegalo promoRegalo = factory.createGiftDiscount(20.0, regaloUnico, "Promo Unica");
        carrito.addGlobalDiscount((IVolumen) promoRegalo);

        System.out.println("  --- Primer cálculo (debería haber regalo) ---");
        carrito.getPrice();
        System.out.println("  Regalos en carrito: " + carrito.getGifts().size());

        // Simulamos que el stock del regalo se agota físicamente
        regaloUnico.orderProduct(1); // Stock efectivo ahora es 0

        System.out.println("  --- Segundo cálculo (con stock de regalo 0) ---");
        carrito.getPrice();
        if (carrito.getGifts().isEmpty()) {
            System.out.println("  [✔] ÉXITO: El sistema no dio el regalo porque no hay stock.");
        } else {
            System.out.println("  [X] ERROR: El sistema está regalando aire (sin stock).");
        }

        // 5. TEST DE ACUMULACIÓN (Rebaja + Volumen)
        System.out.println("\n[TEST 4] Acumulación de Rebaja e IVolumen...");
        IRebaja rebajaValida = factory.createPercentageDiscount(10.0, "Promo 10%");
        p1.setDiscount(rebajaValida); // 100 -> 90€

        // El carrito tiene 1 item de 90€. Total bruto 90€.
        // El descuento de volumen pide 100€ para aplicar -10€.
        System.out.println("  Nuevo total bruto (con item rebajado): " + carrito.getFullPrice() + "€");
        System.out.println("  Total final (esperado 90, ya que no llega a los 100 del volumen): " + carrito.getPrice() + "€");

        System.out.println("\n=== STRESS TEST FINALIZADO ===");
    }
}