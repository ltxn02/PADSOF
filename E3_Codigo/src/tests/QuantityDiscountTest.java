package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import discounts.QuantityDiscount;

public class QuantityDiscountTest {

    @Test
    void testConstructorValidaCantidades() {
        assertDoesNotThrow(() -> {
            new QuantityDiscount(3, 2, "3x2", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityDiscount(2, 3, "2x3 Falso", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        });
    }

    @Test
    void testApplyCalculoDeGrupos() {
        QuantityDiscount promo3x2 = new QuantityDiscount(3, 2, "Promo 3x2",
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(5));

        double precioUnidad = 10.0;

        assertEquals(20.0, promo3x2.applyCantidad(precioUnidad, 2), 0.01);
        assertEquals(20.0, promo3x2.applyCantidad(precioUnidad, 3), 0.01);
        assertEquals(50.0, promo3x2.applyCantidad(precioUnidad, 7), 0.01);
    }
}