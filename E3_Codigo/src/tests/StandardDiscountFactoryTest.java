package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import discounts.*;
import products.*;
import products.catalog.Category;

public class StandardDiscountFactoryTest {

    private StandardDiscountFactory factory;

    class TestProduct extends Product {
        public TestProduct(String name, ArrayList<Category> categorias) {
            super(name, "Regalo", 0.0, "img.png", 100, categorias, new ArrayList<>(), null);
        }
    }

    @BeforeEach
    void setUp() {
        factory = new StandardDiscountFactory();
    }

    @Test
    void testCreatePercentageDiscount() {
        IRebaja descuento = factory.createPercentageDiscount(20.0, "Promo 20%");

        assertNotNull(descuento);
        assertTrue(descuento instanceof PercentageDiscount);
    }

    @Test
    void testCreateQuantityDiscount() {
        ICantidad descuento = factory.createQuantityDiscount(3, 2, "Promo 3x2");

        assertNotNull(descuento);
        assertTrue(descuento instanceof QuantityDiscount);
    }

    @Test
    void testCreateVolumeDiscount() {
        IVolumen descuento = factory.createVolumeDiscount(100.0, 15.0, "Promo Volumen");

        assertNotNull(descuento);
        assertTrue(descuento instanceof VolumeDiscount);
    }

    @Test
    void testCreateGiftDiscount() {
        Category catRegalo = new Category("Promociones");
        ArrayList<Category> categorias = new ArrayList<>();
        categorias.add(catRegalo);

        TestProduct regalo = new TestProduct("Llavero", categorias);
        IRegalo descuento = factory.createGiftDiscount(50.0, regalo, "Regalo por compra");

        assertNotNull(descuento);
        assertTrue(descuento instanceof GiftDiscount);
    }
}