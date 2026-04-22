package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import discounts.GiftDiscount;
import products.*;
import products.catalog.Category;
import transactions.ShoppingCart;

public class GiftDiscountTest {

    class MockProduct extends Product {
        public MockProduct(String name) {
            super(name,
                    "Descripción de regalo",
                    0.0,
                    "img_path",
                    100,
                    new ArrayList<>(List.of(new Category("Regalos", new ArrayList<>()))),
                    new ArrayList<>(),
                    null);
        }
    }

    @Test
    void testApplyNoCambiaElPrecio() {
        MockProduct regalo = new MockProduct("Llavero");
        GiftDiscount descRegalo = new GiftDiscount(50.0, regalo, "Promo Regalo",
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(5));

        assertEquals(100.0, descRegalo.apply(100.0), 0.01, "El descuento de regalo no debe modificar el precio unitario.");
    }

    @Test
    void testAplicarRegaloAlCarrito() {
        MockProduct regalo = new MockProduct("Póster");
        GiftDiscount descRegalo = new GiftDiscount(50.0, regalo, "Promo Póster",
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(5));

        ShoppingCart carrito = new ShoppingCart();

        assertDoesNotThrow(() -> {
            descRegalo.aplicarRegalo(carrito);
        }, "El método aplicarRegalo no debería lanzar excepciones con un carrito válido.");
    }
}