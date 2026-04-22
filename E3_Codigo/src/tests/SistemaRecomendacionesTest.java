package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import logic.SistemaRecomendaciones;
import products.*;
import products.catalog.Category;
import users.*;
import transactions.*;
import utils.*;

/**
 * Clase de prueba para validar el motor de recomendaciones.
 * Simula compras reales instanciando Orders y CartItems para
 * superar las validaciones de seguridad de las clases Client y Order.
 */
public class SistemaRecomendacionesTest {

    private Client ana;
    private Client luis;
    private ArrayList<Client> todosLosClientes;
    private ArrayList<NewProduct> catalogo;

    private Product pQuijote;
    private Product pHamlet;
    private Product pEldenRing;

    /**
     * Clase auxiliar (Mock) para poder instanciar Product, ya que es abstracta.
     */
    class TestProduct extends Product {
        public TestProduct(String name, String description, double price, String image, int stock, ArrayList<Category> categories) {
            super(name, description, price, image, stock, categories, new ArrayList<>(), null);
        }
    }

    /**
     * Clase auxiliar (Mock) para CartItem asumiendo la estructura estándar.
     */
    class TestCartItem extends CartItem {
        public TestCartItem(NewProduct product, int quantity) {
            super(product, quantity);
        }
    }

    @BeforeEach
    void setUp() {
        catalogo = new ArrayList<>();
        todosLosClientes = new ArrayList<>();

        Category catLibros = new Category("Libros");
        Category catJuegos = new Category("Videojuegos");

        ArrayList<Category> catsL = new ArrayList<>(); catsL.add(catLibros);
        ArrayList<Category> catsJ = new ArrayList<>(); catsJ.add(catJuegos);

        pQuijote = new TestProduct("El Quijote", "Libro clásico", 20.0, "q.png", 10, catsL);
        pHamlet = new TestProduct("Hamlet", "Teatro", 15.0, "h.png", 10, catsL);
        pEldenRing = new TestProduct("Elden Ring", "RPG", 60.0, "er.png", 10, catsJ);

        catalogo.add(pQuijote);
        catalogo.add(pHamlet);
        catalogo.add(pEldenRing);

        ana = new Client("ana99", "pass123", "Ana García", "12345678A", "01/01/1999", "ana@mail.com", "600111222");
        luis = new Client("luis99", "pass123", "Luis López", "87654321B", "02/02/1999", "luis@mail.com", "600333444");

        todosLosClientes.add(ana);
        todosLosClientes.add(luis);
    }

    /**
     * Método auxiliar para simular una compra saltándose la pasarela de pago,
     * pero cumpliendo con las validaciones de Order y OrderHistoric.
     */
    private void simularCompra(Client cliente, Product producto) {
        List<CartItem> items = new ArrayList<>();
        items.add(new TestCartItem(producto, 1));

        Order orden = new Order(cliente, items, producto.getPrice());

        cliente.getOrderHistoric().addOrder(orden);
    }

    @Test
    void testRecomendacionesExcluyenProductosYaComprados() {
        simularCompra(ana, pQuijote);

        ArrayList<NewProduct> recs = SistemaRecomendaciones.obtenerRecomendaciones(ana, catalogo, todosLosClientes);

        assertFalse(recs.contains(pQuijote), "El sistema no debe recomendar productos que ya están en el histórico de compras.");
    }

    @Test
    void testRecomendacionPorSimilitudYCategoria() {
        simularCompra(ana, pQuijote);

        assertDoesNotThrow(() -> {
            ana.reviewProduct(pQuijote, 5, "Una obra de arte.");
        });

        simularCompra(luis, pQuijote);
        simularCompra(luis, pHamlet);

        ArrayList<NewProduct> recs = SistemaRecomendaciones.obtenerRecomendaciones(ana, catalogo, todosLosClientes);

        assertFalse(recs.isEmpty(), "Debería haber productos recomendados.");

        assertEquals(pHamlet, recs.get(0), "El sistema debe priorizar Hamlet debido al filtrado colaborativo y de contenido.");
    }

    @Test
    void testClienteSinComprasNoRompeElSistema() {
        Client usuarioNuevo = new Client("nuevo", "pass", "Nuevo User", "00000000C", "01/01/2000", "n@m.com", "600000000");

        assertDoesNotThrow(() -> {
            ArrayList<NewProduct> recs = SistemaRecomendaciones.obtenerRecomendaciones(usuarioNuevo, catalogo, todosLosClientes);

            assertEquals(3, recs.size(), "Debería recomendar todos los productos disponibles en el catálogo para usuarios nuevos.");
        });
    }

    @Test
    void testExcepcionesEnConstructorOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Order(null, new ArrayList<>(), 10.0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Order(ana, new ArrayList<>(), 10.0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            List<CartItem> items = new ArrayList<>();
            items.add(new TestCartItem(pQuijote, 1));
            new Order(ana, items, -5.0);
        });
    }
}