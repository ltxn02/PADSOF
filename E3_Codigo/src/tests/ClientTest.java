package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import users.*;
import catalog.*;
import transactions.*;
import utils.*;

/**
 * Clase de pruebas unitarias para la clase {@link Client}.
 * 
 * IMPORTANTE: Estos tests verifican la FUNCIONALIDAD REAL de Client,
 * no adaptan el código para pasar tests.
 */
public class ClientTest {

    private Client testClient;
    private Client otherClient;
    private Category testCategory;
    private ArrayList<Category> testCategories;
    private ArrayList<Review> testReviews;

    @BeforeEach
    void setUp() {
        testClient = new Client(
            "client_test",
            "password123",
            "Test Client",
            "12345678A",
            "15/03/1995",
            "client@test.com",
            "666123456"
        );
        
        otherClient = new Client(
            "other_client",
            "password456",
            "Other Client",
            "87654321B",
            "20/05/1990",
            "other@test.com",
            "666654321"
        );

        testCategory = new Category("Test Category");
        testCategories = new ArrayList<>();
        testCategories.add(testCategory);
        testReviews = new ArrayList<>();
    }

    // ==========================================
    // TESTS DE INICIALIZACIÓN
    // ==========================================

    @Test
    void testCrearCliente() {
        assertNotNull(testClient);
        assertEquals("client_test", testClient.getUsername());
    }

    @Test
    void testClientHeredaDeRegisteredUser() {
        assertTrue(testClient instanceof RegisteredUser);
        assertTrue(testClient instanceof User);
    }

    @Test
    void testClientTieneCarrito() {
        assertNotNull(testClient.getShoppingCart());
        assertEquals(0, testClient.getShoppingCart().getCartItems().size());
    }

    @Test
    void testClientTieneHistoricoPedidos() {
        assertNotNull(testClient.getOrderHistoric());
    }

    @Test
    void testClientTieneHistoricoIntercambios() {
        assertNotNull(testClient.getExchangeHistoric());
    }

    // ==========================================
    // TESTS DE CARRITO DE COMPRAS
    // ==========================================

    @Test
    void testAnadirProductoAlCarrito() {
        Comic comic = new Comic(
            "Test Comic 1", "Desc", 10.0, "img/1.jpg", 100,
            testCategories, testReviews, null, 200, "Pub", 2020, new ArrayList<>()
        );
        
        testClient.addToCart(comic, 2);
        
        List<CartItem> items = testClient.getShoppingCart().getCartItems();
        assertEquals(1, items.size());
        assertEquals(comic.getName(), items.get(0).getProduct().getName());
        assertEquals(2, items.get(0).getQuantity());
    }

    @Test
    void testAnadirMultiplesProductosDiferentesAlCarrito() {
        Comic comic = new Comic(
            "Comic Test", "Desc", 10.0, "img/1.jpg", 100,
            testCategories, testReviews, null, 200, "Pub", 2020, new ArrayList<>()
        );
        Game game = new Game(
            "Game Test", "Desc", 45.0, "img/2.jpg", 50,
            testCategories, testReviews, null, 4, new ArrayList<>(), new AgeRange(10, 99)
        );
        
        testClient.addToCart(comic, 1);
        testClient.addToCart(game, 2);
        
        List<CartItem> items = testClient.getShoppingCart().getCartItems();
        assertEquals(2, items.size()); // DEBE haber 2 items diferentes
    }

    @Test
    void testEliminarProductoDelCarrito() {
        Comic comic = new Comic(
            "Comic to Remove", "Desc", 10.0, "img/1.jpg", 100,
            testCategories, testReviews, null, 200, "Pub", 2020, new ArrayList<>()
        );
        
        testClient.addToCart(comic, 3);
        testClient.removeFromCart(comic, 1); // Quitar 1 de 3
        
        List<CartItem> items = testClient.getShoppingCart().getCartItems();
        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getQuantity()); // Deben quedar 2
    }

    @Test
    void testEliminarTodoDelCarrito() {
        Comic comic = new Comic(
            "Comic All Remove", "Desc", 10.0, "img/1.jpg", 100,
            testCategories, testReviews, null, 200, "Pub", 2020, new ArrayList<>()
        );
        
        testClient.addToCart(comic, 2);
        testClient.removeFromCart(comic, 2); // Quitar todos
        
        List<CartItem> items = testClient.getShoppingCart().getCartItems();
        assertEquals(0, items.size()); // El item debe desaparecer
    }

    @Test
    void testVaciarCarrito() {
        Comic comic = new Comic(
            "Comic", "Desc", 10.0, "img/1.jpg", 100,
            testCategories, testReviews, null, 200, "Pub", 2020, new ArrayList<>()
        );
        Game game = new Game(
            "Game", "Desc", 45.0, "img/2.jpg", 50,
            testCategories, testReviews, null, 4, new ArrayList<>(), new AgeRange(10, 99)
        );
        
        testClient.addToCart(comic, 1);
        testClient.addToCart(game, 1);
        
        assertEquals(2, testClient.getShoppingCart().getCartItems().size());
        
        testClient.getShoppingCart().clearCart();
        assertEquals(0, testClient.getShoppingCart().getCartItems().size());
    }

    // ==========================================
    // TESTS DE PRODUCTOS DE SEGUNDA MANO
    // ==========================================

    /**
     * TEST CRÍTICO: Verifica que NO hay duplicación de productos.
     * En Client.registerSecondHandProduct() se crea el producto,
     * pero en SecondHandProduct constructor ya se registra al cliente.
     * Esto es un BUG si se llama dos veces.
     */
    @Test
    void testRegistrarProductoSegundaManoUnaSolaVez() {
        int initialSize = testClient.getCarteraSegundaMano().size();
        
        testClient.registerSecondHandProduct(
            "Nintendo Switch Original",
            "Consola usada perfecta",
            "img/switch.jpg",
            ItemType.GAME
        );
        
        int finalSize = testClient.getCarteraSegundaMano().size();
        assertEquals(initialSize + 1, finalSize, 
            "El producto debe registrarse UNA SOLA VEZ, no dos");
    }

    @Test
    void testNoPuedoRegistrarProductoDuplicado() {
        testClient.registerSecondHandProduct(
            "PS5",
            "Consola nueva",
            "img/ps5.jpg",
            ItemType.GAME
        );

        // Intentar registrar otro con el mismo nombre
        assertThrows(IllegalArgumentException.class, () -> {
            testClient.registerSecondHandProduct(
                "PS5",
                "Otra PS5",
                "img/ps5_2.jpg",
                ItemType.GAME
            );
        }, "No debe permitir dos productos con el mismo nombre");
    }

    @Test
    void testRegistrarMultiplesProductosSegundaMano() {
        String[] nombres = {"Producto A", "Producto B", "Producto C", "Producto D"};
        int initial = testClient.getCarteraSegundaMano().size();
        
        for (String nombre : nombres) {
            testClient.registerSecondHandProduct(
                nombre,
                "Descripción de " + nombre,
                "img/" + nombre + ".jpg",
                ItemType.COMIC
            );
        }
        
        assertEquals(initial + 4, testClient.getCarteraSegundaMano().size());
    }

    @Test
    void testSecondHandProductAutoRegistraEnCliente() {
        // Cuando creamos un SecondHandProduct, se registra automáticamente en el cliente
        int initialSize = testClient.getCarteraSegundaMano().size();
        
        SecondHandProduct product = new SecondHandProduct(
            "Xbox One",
            "Consola usada",
            "img/xbox.jpg",
            ItemType.GAME,
            testClient
        );
        
        int finalSize = testClient.getCarteraSegundaMano().size();
        assertEquals(initialSize + 1, finalSize,
            "El producto debe auto-registrarse en el constructor de SecondHandProduct");
    }

    @Test
    void testNoSePuedeRegistrarProductoYaExistente() {
        SecondHandProduct product = new SecondHandProduct(
            "producto_unico",
            "Descripción",
            "img/prod.jpg",
            ItemType.GAME,
            testClient
        );

        // Ya está registrado, no podemos registrarlo de nuevo
        assertThrows(IllegalArgumentException.class, () -> {
            testClient.registerSecondHandProduct(product);
        });
    }

    @Test
    void testClientPuedeVerificarSiTieneProducto() {
        SecondHandProduct product = new SecondHandProduct(
            "Producto Verificable",
            "Desc",
            "img/prod.jpg",
            ItemType.GAME,
            testClient
        );

        assertTrue(testClient.hasSecondHandProduct(product),
            "El cliente debe poder verificar que tiene el producto");
    }

    @Test
    void testClientNoPuedeVerificarProductoDeOtro() {
        SecondHandProduct myProduct = new SecondHandProduct(
            "Mi Producto",
            "Desc",
            "img/mine.jpg",
            ItemType.GAME,
            testClient
        );
        
        SecondHandProduct otherProduct = new SecondHandProduct(
            "Su Producto",
            "Desc",
            "img/other.jpg",
            ItemType.COMIC,
            otherClient
        );

        assertTrue(testClient.hasSecondHandProduct(myProduct));
        assertFalse(testClient.hasSecondHandProduct(otherProduct),
            "Un cliente no debe tener el producto de otro");
    }

    // ==========================================
    // TESTS DE DATOS HEREDADOS
    // ==========================================

    @Test
    void testClientAccesoDatosRegisteredUser() {
        String profile = testClient.userProfile();
        assertTrue(profile.contains("Test Client"));
        assertTrue(profile.contains("666123456"));
        assertTrue(profile.contains("15/03/1995"));
        assertTrue(profile.contains("Nombre:"));
    }

    @Test
    void testClientAutenticacion() {
        assertTrue(testClient.login("client_test", "password123"),
            "Debe autenticarse con credenciales correctas");
        assertFalse(testClient.login("client_test", "wrongpassword"),
            "No debe autenticarse con contraseña incorrecta");
        assertFalse(testClient.login("wronguser", "password123"),
            "No debe autenticarse con usuario incorrecto");
    }

    @Test
    void testClientUserPreview() {
        String preview = testClient.userPreview();
        assertTrue(preview.contains("client_test"));
        assertTrue(preview.contains("Test Client"));
    }

    // ==========================================
    // TESTS DE INDEPENDENCIA
    // ==========================================

    @Test
    void testDosCientesNoCompartenCarrito() {
        Comic comic = new Comic(
            "Comic", "Desc", 10.0, "img/1.jpg", 100,
            testCategories, testReviews, null, 200, "Pub", 2020, new ArrayList<>()
        );
        
        testClient.addToCart(comic, 1);
        
        assertEquals(1, testClient.getShoppingCart().getCartItems().size());
        assertEquals(0, otherClient.getShoppingCart().getCartItems().size(),
            "Dos clientes NO deben compartir carrito");
    }

    @Test
    void testDosCientesNoCompartenProductosSegundaMano() {
        testClient.registerSecondHandProduct("Mi Producto", "Desc", "img/1.jpg", ItemType.GAME);
        
        assertTrue(testClient.getCarteraSegundaMano().size() > 0);
        assertEquals(0, otherClient.getCarteraSegundaMano().size(),
            "Dos clientes NO deben compartir productos de segunda mano");
    }

    @Test
    void testClientesUsernamesUnicos() {
        assertNotEquals(testClient.getUsername(), otherClient.getUsername());
    }

    // ==========================================
    // TESTS DE RESEÑAS
    // ==========================================

    @Test
    void testNoSePuedeResenaProductoNoComprado() {
        Comic comic = new Comic(
            "Comic no comprado", "Desc", 10.0, "img/1.jpg", 100,
            testCategories, testReviews, null, 200, "Pub", 2020, new ArrayList<>()
        );
        
        assertThrows(IllegalArgumentException.class, () -> {
            testClient.reviewProduct(comic, 5, "Buen producto");
        }, "No se puede reseñar un producto que no has comprado");
    }

    // ==========================================
    // TESTS DE OFERTAS
    // ==========================================

    @Test
    void testCrearOfertaIntercambio() {
        SecondHandProduct productMio = new SecondHandProduct(
            "Mi Producto", "Desc", "img/1.jpg", ItemType.GAME, testClient
        );
        SecondHandProduct productOtro = new SecondHandProduct(
            "Producto del Otro", "Desc", "img/2.jpg", ItemType.COMIC, otherClient
        );

        assertDoesNotThrow(() -> {
            testClient.makeOffer(productOtro, productMio);
        }, "Debo poder hacer una oferta");

        assertEquals(1, testClient.getOffersMade().size());
    }

    @Test
    void testNoSePuedeHacerOfertaConProductoAjeno() {
        SecondHandProduct productOtro1 = new SecondHandProduct(
            "Producto de Otro 1", "Desc", "img/1.jpg", ItemType.GAME, otherClient
        );
        SecondHandProduct productOtro2 = new SecondHandProduct(
            "Producto de Otro 2", "Desc", "img/2.jpg", ItemType.COMIC, otherClient
        );

        // Intento ofrecer algo que no es mío
        assertThrows(IllegalArgumentException.class, () -> {
            testClient.makeOffer(productOtro2, productOtro1);
        }, "No puedo hacer una oferta con productos que no son míos");
    }

    @Test
    void testClientePuedeRecibirOferta() {
        SecondHandProduct productMio = new SecondHandProduct(
            "Mi Producto", "Desc", "img/1.jpg", ItemType.GAME, testClient
        );
        SecondHandProduct productOtro = new SecondHandProduct(
            "Producto del Otro", "Desc", "img/2.jpg", ItemType.COMIC, otherClient
        );

        ExchangeOffer offer = new ExchangeOffer(
            productMio,
            new ArrayList<>(List.of(productOtro)),
            otherClient
        );

        int initialSize = testClient.obtenerMisOfertasRecibidos().size();
        testClient.receiveOffer(offer);
        
        assertEquals(initialSize + 1, testClient.obtenerMisOfertasRecibidos().size(),
            "El cliente debe poder recibir ofertas");
    }

    // ==========================================
    // TESTS DE VISTAS
    // ==========================================

    @Test
    void testVerCarritoFormatoTexto() {
        Comic comic = new Comic(
            "Comic", "Desc", 10.0, "img/1.jpg", 100,
            testCategories, testReviews, null, 200, "Pub", 2020, new ArrayList<>()
        );
        testClient.addToCart(comic, 1);
        
        String cartView = testClient.viewShoppingCart();
        assertTrue(cartView.contains("CARRITO"));
    }

    @Test
    void testVerProductosSegundaMano() {
        testClient.registerSecondHandProduct("Producto", "Desc", "img/1.jpg", ItemType.GAME);
        
        String view = testClient.viewMyProducts();
        assertNotNull(view);
        // No debe estar vacío si hay productos
        assertNotEquals("", view.trim());
    }

    @Test
    void testPerfilCompletoClienteNoLanzaError() {
        assertDoesNotThrow(() -> {
            String profile = testClient.clientFullProfile();
            assertNotNull(profile);
        }, "El perfil completo no debe lanzar excepciones");
    }

    // ==========================================
    // TESTS DE MULTIPLICIDAD
    // ==========================================

    @Test
    void testClientePuedeTenerMuchosProductosSegundaMano() {
        for (int i = 0; i < 10; i++) {
            testClient.registerSecondHandProduct(
                "Producto " + i,
                "Descripción " + i,
                "img/prod" + i + ".jpg",
                ItemType.GAME
            );
        }

        assertTrue(testClient.getCarteraSegundaMano().size() >= 10,
            "Un cliente debe poder tener múltiples productos");
    }

    @Test
    void testClientePuedeTenerMuchosItemsEnCarrito() {
        Comic[] comics = new Comic[5];
        for (int i = 0; i < 5; i++) {
            comics[i] = new Comic(
                "Comic " + i, "Desc " + i, 10.0 + i, "img/" + i + ".jpg", 100,
                testCategories, testReviews, null, 200, "Pub", 2020, new ArrayList<>()
            );
            testClient.addToCart(comics[i], 1);
        }

        assertTrue(testClient.getShoppingCart().getCartItems().size() >= 5,
            "Un cliente debe poder tener múltiples items en el carrito");
    }
}