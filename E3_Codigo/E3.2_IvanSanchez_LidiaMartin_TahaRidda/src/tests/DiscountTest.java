package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import discounts.*;

public class DiscountTest {

    class TestDiscount extends Discount {

        public TestDiscount(double value, String desc, LocalDateTime start, LocalDateTime end) {
            super(value, desc, start, end);
        }

        @Override
        public double apply(double amount) {
            return amount;
        }

        @Override
        public boolean isValid() {
            return !isExpired();
        }

        @Override
        public String getDescription() {
            return "Descuento de prueba";
        }
    }

    @Test
    void testIsExpired() {
        TestDiscount caducado = new TestDiscount(0.0, "Caducado",
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(1));

        TestDiscount vigente = new TestDiscount(0.0, "Vigente",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(5));

        assertTrue(caducado.isExpired(), "El descuento debería estar caducado.");
        assertFalse(vigente.isExpired(), "El descuento no debería estar caducado.");
    }
}