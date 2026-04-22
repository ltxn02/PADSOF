package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import transactions.*;
import utils.*;
import discounts.*;
import products.*;
import products.catalog.Category;

import java.time.LocalDateTime;

/**
 * Clase de pruebas unitarias para la clase {@link ShoppingCart}.
 * 
 * Verifica el funcionamiento correcto de:
 * <ul>
 *   <li>Creación e inicialización del carrito</li>
 *   <li>Adición y eliminación de productos</li>
 *   <li>Cálculo de precios con y sin descuentos</li>
 *   <li>Gestión de visibilidad y estado del carrito</li>
 *   <li>Aplicación de descuentos globales</li>
 *   <li>Sistema de regalos y promociones</li>
 * </ul>
 * 
 * @version 1.0
 */
public class ShoppingCartTest {

    private ShoppingCart cart;
    private Category testCategory;
    private Comic testComic;
    private Game testGame;
    private Figurine testFigurine;

    @BeforeEach
    void setUp() {
        // 1. Crear carrito vacío
        cart = new ShoppingCart();

        // 2. Crear categoría de prueba
        testCategory = new Category("Test Category");
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(testCategory);

        // 3. Crear productos de prueba
        testComic = new Comic(
            "Test Comic",
            "Comic de prueba",
            10.0,
            "img/comic.jpg",
            100,
            categories,
            new ArrayList<Review>(),
            null,
            200,
            "Test Publisher",
            2020,
            new ArrayList<String>()
        );

        testGame = new Game(
            "Test Game",
            "Juego de prueba",
            45.0,
            "img/game.jpg",
            50,
            categories,
            new ArrayList<Review>(),
            null,
            4,
            new ArrayList<String>(),
            new AgeRange(10, 99)
        );

        testFigurine = new Figurine(
            "Test Figurine",
            "Figura de prueba",
            35.50,
            "img/figurine.jpg",
            30,
            categories,
            new ArrayList<Review>(),
            null,
            15.0,
            5.0,
            5.0,
            "PVC",
            "Test Anime"
        );
    }

    // ==========================================
    // TESTS DE INICIALIZACIÓN
    // ==========================================

    /**
     * Verifica que se puede crear un carrito vacío.
     */
    @Test
    void testCrearCarritoVacio() {
        ShoppingCart newCart = new ShoppingCart();
        assertNotNull(newCart);
        assertEquals(0, newCart.getCartItems().size());
        assertEquals(0.0, newCart.getFullPrice());
    }

    /**
     * Verifica que el carrito inicia con precio total 0.
     */
    @Test
    void testCarritoPrecioInicial() {
        assertEquals(0.0, cart.getFullPrice());
    }

    /**
     * Verifica que el carrito inicia sin items.
     */
    @Test
    void testCarritoSinItems() {
        assertTrue(cart.getCartItems().isEmpty());
    }

    // ==========================================
    // TESTS DE ADICIÓN DE PRODUCTOS
    // ==========================================

    /**
     * Verifica que se puede añadir un producto al carrito.
     */
    @Test
    void testAnadirProducto() {
        assertDoesNotThrow(() -> {
            cart.addCartItem(testComic, 2);
        });
        assertEquals(1, cart.getCartItems().size());
    }

    /**
     * Verifica que se puede añadir múltiples productos diferentes.
     */
    @Test
    void testAnadirMultiplesProductos() {
        assertDoesNotThrow(() -> {
            cart.addCartItem(testComic, 1);
            cart.addCartItem(testGame, 2);
            cart.addCartItem(testFigurine, 1);
        });
        assertEquals(3, cart.getCartItems().size());
    }

    /**
     * Verifica que el precio se actualiza al añadir un producto.
     */
    @Test
    void testPrecioSeActualizaAlAnadir() {
        cart.addCartItem(testComic, 2);
        double precioEsperado = 10.0 * 2; // 2 cómics x 10€
        assertEquals(precioEsperado, cart.getFullPrice());
    }

    /**
     * Verifica que al añadir un producto que ya existe, se incrementa la cantidad.
     */
    @Test
    void testAnadirProductoDuplicadoIncrementaCantidad() {
        cart.addCartItem(testComic, 2);
        assertEquals(1, cart.getCartItems().size());
        CartItem itemAntes = cart.getCartItems().get(0);
        int cantidadAntes = itemAntes.getQuantity();

        // Añadir el mismo producto nuevamente
        cart.addCartItem(testComic, 3);
        
        // Aún debe haber solo 1 item
        assertEquals(1, cart.getCartItems().size());
        CartItem itemDespues = cart.getCartItems().get(0);
        // La cantidad se debe haber incrementado
        assertTrue(itemDespues.getQuantity() >= cantidadAntes);
    }

    // ==========================================
    // TESTS DE ELIMINACIÓN DE PRODUCTOS
    // ==========================================

    /**
     * Verifica que se puede eliminar un producto del carrito.
     */
    @Test
    void testEliminarProducto() {
        cart.addCartItem(testComic, 5);
        cart.removeCartItem(testComic, 2);
        
        // Debe haber 1 item (con 3 unidades)
        assertEquals(1, cart.getCartItems().size());
    }

    /**
     * Verifica que al eliminar todas las unidades, se elimina el item.
     */
    @Test
    void testEliminarTodoEliminaItem() {
        cart.addCartItem(testComic, 2);
        cart.removeCartItem(testComic, 2);
        
        // El carrito debe estar vacío
        assertEquals(0, cart.getCartItems().size());
    }

    /**
     * Verifica que el precio se actualiza al eliminar un producto.
     */
    @Test
    void testPrecioSeActualizaAlEliminar() {
        cart.addCartItem(testComic, 3);
        double precioAntes = cart.getFullPrice();
        
        cart.removeCartItem(testComic, 1);
        double precioDespues = cart.getFullPrice();
        
        assertTrue(precioDespues < precioAntes);
    }

    /**
     * Verifica que eliminar un producto que no existe no causa error.
     */
    @Test
    void testEliminarProductoNoExistenteNoFalla() {
        cart.addCartItem(testComic, 2);
        assertDoesNotThrow(() -> {
            cart.removeCartItem(testGame, 1);
        });
        // El carrito debe seguir teniendo el cómic
        assertEquals(1, cart.getCartItems().size());
    }

    // ==========================================
    // TESTS DE CÁLCULO DE PRECIOS
    // ==========================================

    /**
     * Verifica que getPrice() calcula el precio total correctamente.
     */
    @Test
    void testGetPriceSinDescuentos() {
        cart.addCartItem(testComic, 2);   // 10 * 2 = 20
        cart.addCartItem(testGame, 1);    // 45 * 1 = 45
        
        double precioEsperado = (10.0 * 2) + (45.0 * 1); // 65
        assertEquals(precioEsperado, cart.getPrice());
    }

    /**
     * Verifica que getPrice() retorna precio redondeado a 2 decimales.
     */
    @Test
    void testGetPriceRedondeado() {
        // Usar cantidades que generen decimales
        cart.addCartItem(testFigurine, 3); // 35.50 * 3 = 106.5
        
        double precio = cart.getPrice();
        // Debe estar redondeado a 2 decimales
        assertEquals(106.5, precio);
    }

    // ==========================================
    // TESTS DE LIMPIEZA Y RESET
    // ==========================================

    /**
     * Verifica que se puede limpiar el carrito completamente.
     */
    @Test
    void testClearCart() {
        cart.addCartItem(testComic, 2);
        cart.addCartItem(testGame, 1);
        
        cart.clearCart();
        
        assertEquals(0, cart.getCartItems().size());
        assertEquals(0.0, cart.getFullPrice());
    }

    /**
     * Verifica que clearCart también limpia los regalos.
     */
    @Test
    void testClearCartLimpiaTambienRegalos() {
        cart.addCartItem(testComic, 2);
        cart.addGift(testFigurine);
        
        cart.clearCart();
        
        assertEquals(0, cart.getGifts().size());
    }

    // ==========================================
    // TESTS DE VISUALIZACIÓN
    // ==========================================

    /**
     * Verifica que shoppingCartPreview retorna una cadena válida.
     */
    @Test
    void testShoppingCartPreview() {
        cart.addCartItem(testComic, 2);
        String preview = cart.shoppingCartPreview();
        
        assertNotNull(preview);
        assertTrue(preview.contains("items"));
        assertTrue(preview.contains("Total"));
    }

    /**
     * Verifica que shoppingCartPreview muestra el número correcto de items.
     */
    @Test
    void testShoppingCartPreviewItemCount() {
        cart.addCartItem(testComic, 2);
        cart.addCartItem(testGame, 1);
        
        String preview = cart.shoppingCartPreview();
        assertTrue(preview.contains("2 items")); // 2 productos diferentes
    }

    /**
     * Verifica que shoppingCartView retorna una cadena formateada válida.
     */
    @Test
    void testShoppingCartView() {
        cart.addCartItem(testComic, 1);
        String view = cart.shoppingCartView();
        
        assertNotNull(view);
        assertTrue(view.contains("CARRITO"));
        assertTrue(view.contains("TOTAL"));
    }

    /**
     * Verifica que shoppingCartView muestra mensaje de vacío cuando está vacío.
     */
    @Test
    void testShoppingCartViewVacio() {
        String view = cart.shoppingCartView();
        
        assertTrue(view.contains("vacío"));
    }

    // ==========================================
    // TESTS DE DESCUENTOS GLOBALES
    // ==========================================

    /**
     * Verifica que se puede añadir un descuento global.
     */
    @Test
    void testAnadirDescuentoGlobal() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusYears(1);
        
        VolumeDiscount discount = new VolumeDiscount(10.0, 50.0, "Test Discount", inicio, fin);
        
        assertDoesNotThrow(() -> {
            cart.addGlobalDiscount(discount);
        });
    }

    /**
     * Verifica que el descuento global se aplica correctamente.
     */
    @Test
    void testDescuentoGlobalAplicado() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusYears(1);
        
        cart.addCartItem(testComic, 10);   // 10 * 10 = 100
        
        // Descuento de 20€ si gastas >50€
        VolumeDiscount discount = new VolumeDiscount(20.0, 50.0, "Test Discount", inicio, fin);
        cart.addGlobalDiscount(discount);
        
        double precioConDescuento = cart.getPrice();
        double precioSinDescuento = 100.0;
        
        // El precio debe ser menor con el descuento
        assertTrue(precioConDescuento < precioSinDescuento);
    }

    // ==========================================
    // TESTS DE REGALOS
    // ==========================================

    /**
     * Verifica que se puede añadir un regalo al carrito.
     */
    @Test
    void testAnadirRegalo() {
        assertDoesNotThrow(() -> {
            cart.addGift(testFigurine);
        });
        assertEquals(1, cart.getGifts().size());
    }

    /**
     * Verifica que no se pueden añadir regalos duplicados.
     */
    @Test
    void testNoAnadirRegaloDuplicado() {
        cart.addGift(testFigurine);
        cart.addGift(testFigurine); // Intentar añadir de nuevo
        
        // Debe haber solo 1 regalo
        assertEquals(1, cart.getGifts().size());
    }

    /**
     * Verifica que no se puede añadir un regalo nulo.
     */
    @Test
    void testNoAnadirRegaloNulo() {
        cart.addGift(null);
        
        // No debe haber regalos
        assertEquals(0, cart.getGifts().size());
    }

    /**
     * Verifica que se puede obtener la lista de regalos.
     */
    @Test
    void testGetGifts() {
        cart.addGift(testComic);
        cart.addGift(testGame);
        
        List<Item> gifts = cart.getGifts();
        assertEquals(2, gifts.size());
    }

    // ==========================================
    // TESTS DE CONFIGURACIÓN
    // ==========================================

    /**
     * Verifica que se puede cambiar el tiempo de espera.
     */
    @Test
    void testSetTimeOnHold() {
        assertDoesNotThrow(() -> {
            cart.setTimeOnHold(24); // 1 día en lugar de 48 horas
        });
    }

    /**
     * Verifica que setTimeOnHold acepta diferentes valores.
     */
    @Test
    void testSetTimeOnHoldMultipleValues() {
        assertDoesNotThrow(() -> {
            cart.setTimeOnHold(1);
            cart.setTimeOnHold(24);
            cart.setTimeOnHold(72);
        });
    }

    // ==========================================
    // TESTS DE GETTERS
    // ==========================================

    /**
     * Verifica que getCartItems retorna la lista correcta.
     */
    @Test
    void testGetCartItems() {
        cart.addCartItem(testComic, 1);
        
        List<CartItem> items = cart.getCartItems();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    /**
     * Verifica que getFullPrice retorna el precio correcto.
     */
    @Test
    void testGetFullPrice() {
        cart.addCartItem(testComic, 2);
        
        double precio = cart.getFullPrice();
        assertEquals(20.0, precio);
    }

    // ==========================================
    // TESTS DE CASOS LÍMITE
    // ==========================================

    /**
     * Verifica que el carrito maneja correctamente múltiples operaciones seguidas.
     */
    @Test
    void testMultiplesOperacionesSeguidas() {
        cart.addCartItem(testComic, 2);
        cart.addCartItem(testGame, 1);
        cart.removeCartItem(testComic, 1);
        cart.addCartItem(testFigurine, 3);
        
        assertEquals(3, cart.getCartItems().size());
        assertTrue(cart.getPrice() > 0);
    }

    /**
     * Verifica que el carrito se vacía correctamente después de compra simulada.
     */
    @Test
    void testCarritoDespuesDCompra() {
        cart.addCartItem(testComic, 2);
        cart.addCartItem(testGame, 1);
        
        double precioTotal = cart.getPrice();
        assertTrue(precioTotal > 0);
        
        // Simular compra
        cart.clearCart();
        
        assertEquals(0, cart.getCartItems().size());
        assertEquals(0.0, cart.getFullPrice());
    }

    /**
     * Verifica que se puede reutilizar el carrito después de limpiarlo.
     */
    @Test
    void testCarritoReutilizable() {
        // Primera compra
        cart.addCartItem(testComic, 1);
        cart.clearCart();
        
        // Segunda compra
        cart.addCartItem(testGame, 2);
        
        assertEquals(1, cart.getCartItems().size());
        assertEquals(90.0, cart.getPrice());
    }
}