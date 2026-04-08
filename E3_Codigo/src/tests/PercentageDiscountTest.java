package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import discounts.PercentageDiscount;

public class PercentageDiscountTest {

    @Test
    void testApplyPercentage() {
        PercentageDiscount desc20 = new PercentageDiscount(20.0, "Promo 20",
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(5));

        assertEquals(80.0, desc20.apply(100.0), 0.01, "El 20% de 100 debe dejar el precio en 80.");

        assertEquals(40.0, desc20.apply(50.0), 0.01, "El 20% de 50 debe dejar el precio en 40.");
    }
}