package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import products.*;
import products.catalog.Category;

import java.util.ArrayList;

import utils.*;

public class NewProductTest {

    private ArrayList<Category> categoriasValidas;
    private ArrayList<Review> reviewsVacias;

    /**
     * CLASE DE PRUEBA (MOCK)
     * Como NewProduct es 'abstract', creamos esta clase auxiliar para probar
     * los métodos de gestión de stock, reservas y reseñas.
     */
    class TestNewProduct extends NewProduct {
        public TestNewProduct(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews) {
            super(name, description, price, image, stock, categories, reviews);
        }
    }

    @BeforeEach
    void setUp() {
        Category cat = new Category("Tecnología");
        categoriasValidas = new ArrayList<>();
        categoriasValidas.add(cat);
        reviewsVacias = new ArrayList<>();
    }

    @Test
    void testConstructorLanzaExcepciones() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TestNewProduct("Ratón", "Ratón USB", 15.0, "img.png", -5, categoriasValidas, reviewsVacias);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new TestNewProduct("Teclado", "Teclado mecánico", 50.0, "img.png", 10, new ArrayList<Category>(), reviewsVacias);
        });
    }

    @Test
    void testGestionDeStockFisico() {
        TestNewProduct producto = new TestNewProduct("Monitor", "Pantalla 24", 150.0, "img.png", 10, categoriasValidas, reviewsVacias);

        // Aumentamos el stock
        producto.increaseStock(5);

        // Reducimos el stock (probando una compra confirmada)
        assertDoesNotThrow(() -> {
            producto.decreaseStock(12); // Había 15 (10+5), así que restar 12 es válido
        });

        // Intentar restar más stock del que hay debe lanzar excepción (línea 90)
        assertThrows(IllegalArgumentException.class, () -> {
            producto.decreaseStock(10); // Solo quedan 3, intentar quitar 10 debe fallar
        });

        // Intentar aumentar stock negativo debe fallar (línea 102)
        assertThrows(IllegalArgumentException.class, () -> {
            producto.increaseStock(-2);
        });
    }

    @Test
    void testGestionDeStockEfectivo() {
        TestNewProduct producto = new TestNewProduct("Cascos", "Auriculares", 30.0, "img.png", 20, categoriasValidas, reviewsVacias);

        // Forzamos el stock efectivo a ser bajo para la prueba (simulando que alguien reservó)
        // Ya que el stock real no actualiza el efectivo en el constructor
        producto.returnProduct(20, false); // Le sumamos 20 al stock efectivo manualmente

        assertTrue(producto.isEffectiveStockHigher(10)); // Quedan 20, 10 es menor

        // Reservamos productos (se añaden a un carrito)
        producto.orderProduct(15);

        // Quedan 5 en stock efectivo
        assertFalse(producto.isEffectiveStockHigher(10)); // 10 ya es mayor que los 5 que quedan libres

        // Devolvemos 15 (se vacía el carrito)
        producto.returnProduct(15, true);

        assertTrue(producto.isEffectiveStockHigher(10)); // Vuelven a haber 20 libres
    }

    @Test
    void testContainsMetodoBusqueda() {
        TestNewProduct producto = new TestNewProduct("Auriculares Sony ZX", "Buenos bajos", 40.0, "img.png", 10, categoriasValidas, reviewsVacias);

        assertTrue(producto.contains("sony"));
        assertTrue(producto.contains("AURICULARES"));
        assertFalse(producto.contains("Samsung"));

        // Buscar un null debe lanzar excepción (línea 146)
        assertThrows(IllegalArgumentException.class, () -> {
            producto.contains(null);
        });
    }
}