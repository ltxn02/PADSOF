package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import discounts.VolumeDiscount;

public class VolumeDiscountTest {

    @Test
    void testApplyVolumeDiscount() {
        VolumeDiscount descVolumen = new VolumeDiscount(15.0, 100.0, "Descuento 15", LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(5));

        assertEquals(80.0, descVolumen.applyVolumen(80.0), 0.01, "No debe aplicar si no supera el umbral.");

        assertEquals(105.0, descVolumen.applyVolumen(120.0), 0.01, "Debe restar 15€ al superar el umbral.");
    }
}