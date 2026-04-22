package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import utils.*;
import discounts.*;
import products.*;
import products.catalog.Category;

public class ProductTest {

    private ArrayList<Category> categoriasValidas;
    private ArrayList<Review> reviewsVacias;

    /**
     * MOCK DE PRODUCTO
     * Al ser Product una clase abstracta, necesitamos instanciar esta clase de
     * prueba para evaluar sus métodos concretos.
     */
    class TestProduct extends Product {
        public TestProduct(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, IDiscount discount) {
            super(name, description, price, image, stock, categories, reviews, discount);
        }
    }

    /**
     * MOCK DE DESCUENTO
     * Como IDiscount es una interfaz, creamos una implementación rápida
     * para simular descuentos válidos e inválidos.
     */
    class TestDiscount implements IDiscount {
        private boolean valid;
        private double rebaja;

        public TestDiscount(boolean valid, double rebaja) {
            this.valid = valid;
            this.rebaja = rebaja;
        }

        @Override
        public boolean isValid() { return valid; }

        @Override
        public double apply(double price) { return price - rebaja; }

        @Override
        public String getDescription() { return "Descuento de prueba: -" + rebaja + "€"; }

        @Override
        public boolean isExpired() {
            return !valid; // Si no es válido, decimos que está expirado
        }
    }

    @BeforeEach
    void setUp() {
        Category cat = new Category("Test");
        categoriasValidas = new ArrayList<>();
        categoriasValidas.add(cat);
        reviewsVacias = new ArrayList<>();
    }

    @Test
    void testConstructorYGetters() {
        TestDiscount descuento = new TestDiscount(true, 5.0);
        TestProduct producto = new TestProduct("Silla Gamer", "Silla ergonómica", 150.0, "silla.png", 5, categoriasValidas, reviewsVacias, descuento);

        // Comprobamos que el precio base es el original (super.getPrice())
        assertEquals(150.0, producto.getPrice());

        // Comprobamos que el descuento se ha guardado bien
        assertNotNull(producto.getDiscount());
        assertEquals("Descuento de prueba: -5.0€", producto.getDiscount().getDescription());
    }

    @Test
    void testSetDiscount() {
        TestProduct producto = new TestProduct("Mesa", "Mesa escritorio", 100.0, "mesa.png", 5, categoriasValidas, reviewsVacias, null);
        assertNull(producto.getDiscount());

        // Le asignamos un nuevo descuento
        TestDiscount nuevoDescuento = new TestDiscount(true, 10.0);
        producto.setDiscount(nuevoDescuento);

        assertNotNull(producto.getDiscount());
    }

    @Test
    void testGetPriceWithDiscount_SinDescuento() {
        TestProduct producto = new TestProduct("Teclado", "Teclado RGB", 50.0, "teclado.png", 10, categoriasValidas, reviewsVacias, null);

        assertEquals(50.0, producto.getPriceWithDiscount());
    }

    @Test
    void testGetPriceWithDiscount_DescuentoValido() {
        // Simulamos un descuento VÁLIDO que resta 10 euros
        TestDiscount descuentoValido = new TestDiscount(true, 10.0);
        TestProduct producto = new TestProduct("Ratón", "Ratón Gaming", 40.0, "raton.png", 10, categoriasValidas, reviewsVacias, descuentoValido);

        assertEquals(40.0, producto.getPrice());
        assertEquals(30.0, producto.getPriceWithDiscount());
    }

    @Test
    void testGetPriceWithDiscount_DescuentoInvalido() {
        // Simulamos un descuento INVÁLIDO (ej: caducado) que restaría 20 euros
        TestDiscount descuentoInvalido = new TestDiscount(false, 20.0);
        TestProduct producto = new TestProduct("Monitor", "Monitor 144hz", 200.0, "monitor.png", 5, categoriasValidas, reviewsVacias, descuentoInvalido);

        assertEquals(200.0, producto.getPriceWithDiscount());
    }
}