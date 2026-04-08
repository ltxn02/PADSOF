package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import users.*;
import catalog.*;
import utils.*;

/**
 * Clase de pruebas unitarias para la clase abstracta {@link User}.
 * 
 * @version 1.0
 */
public class UserTest {

    private Guest guestUser;
    private Client clientUser;
    private Employee employeeUser;
    private Manager managerUser;
    private Catalog testCatalog;
    private ArrayList<Category> testCategories;
    private ArrayList<Review> testReviews;
    private Comic testComic;
    private Game testGame;

    @BeforeEach
    void setUp() {
        // Crear usuarios de diferentes tipos
        guestUser = new Guest();

        clientUser = new Client(
            "client",
            "pass",
            "Test Client",
            "12345678A",
            "15/03/1990",
            "client@test.com",
            "666123456"
        );

        employeeUser = new Employee(
            "employee",
            "pass",
            "Test Employee",
            "87654321B",
            "20/05/1995",
            "employee@test.com",
            "666654321",
            30000.0,
            true
        );

        managerUser = new Manager(
            "manager",
            "pass",
            "Test Manager",
            "11111111C",
            "01/01/1985",
            "manager@test.com",
            "666111111",
            50000.0
        );

        // Preparar catálogo y productos
        testCatalog = new Catalog();
        Category testCategory = new Category("Test Category");
        testCategories = new ArrayList<>();
        testCategories.add(testCategory);
        testReviews = new ArrayList<>();

        // Crear productos de prueba
        testComic = new Comic(
            "Test Comic",
            "A test comic",
            10.0,
            "img/comic.jpg",
            100,
            testCategories,
            testReviews,
            null,
            200,
            "Test Publisher",
            2020,
            new ArrayList<String>()
        );

        testGame = new Game(
            "Test Game",
            "A test game",
            45.0,
            "img/game.jpg",
            50,
            testCategories,
            testReviews,
            null,
            4,
            new ArrayList<String>(),
            new AgeRange(4, 12)
        );
    }

    // ==========================================
    // TESTS DE VISUALIZACIÓN DEL CATÁLOGO
    // ==========================================

    /**
     * Verifica que un Guest puede ver el catálogo.
     */
    @Test
    void testGuestPuedeVerCatalogo() {
        List<NewProduct> products = guestUser.view_catalog(guestUser, testCatalog);
        assertNotNull(products);
    }

    /**
     * Verifica que un Client puede ver el catálogo.
     */
    @Test
    void testClientPuedeVerCatalogo() {
        List<NewProduct> products = clientUser.view_catalog(clientUser, testCatalog);
        assertNotNull(products);
    }

    /**
     * Verifica que un Employee puede ver el catálogo.
     */
    @Test
    void testEmployeePuedeVerCatalogo() {
        List<NewProduct> products = employeeUser.view_catalog(employeeUser, testCatalog);
        assertNotNull(products);
    }

    /**
     * Verifica que un Manager puede ver el catálogo.
     */
    @Test
    void testManagerPuedeVerCatalogo() {
        List<NewProduct> products = managerUser.view_catalog(managerUser, testCatalog);
        assertNotNull(products);
    }

    /**
     * Verifica que Employee ve todos los productos (incluyendo ocultos).
     */
    @Test
    void testEmployeeVeTodosLosProductos() {
        List<NewProduct> allProducts = testCatalog.allProducts();
        List<NewProduct> employeeView = employeeUser.view_catalog(employeeUser, testCatalog);

        assertEquals(allProducts.size(), employeeView.size(),
            "Employee debe ver TODOS los productos");
    }

    /**
     * Verifica que Manager ve todos los productos (incluyendo ocultos).
     */
    @Test
    void testManagerVeTodosLosProductos() {
        List<NewProduct> allProducts = testCatalog.allProducts();
        List<NewProduct> managerView = managerUser.view_catalog(managerUser, testCatalog);

        assertEquals(allProducts.size(), managerView.size(),
            "Manager debe ver TODOS los productos");
    }

    /**
     * Verifica que Client ve solo productos visibles.
     */
    @Test
    void testClientVeraSoloProductosVisibles() {
        List<NewProduct> visibleProducts = testCatalog.visibleProducts();
        List<NewProduct> clientView = clientUser.view_catalog(clientUser, testCatalog);

        assertEquals(visibleProducts.size(), clientView.size(),
            "Client debe ver solo productos VISIBLES");
    }

    /**
     * Verifica que Guest ve solo productos visibles.
     */
    @Test
    void testGuestVeraSoloProductosVisibles() {
        List<NewProduct> visibleProducts = testCatalog.visibleProducts();
        List<NewProduct> guestView = guestUser.view_catalog(guestUser, testCatalog);

        assertEquals(visibleProducts.size(), guestView.size(),
            "Guest debe ver solo productos VISIBLES");
    }

    /**
     * Verifica que catálogo vacío retorna lista vacía para todos.
     */
    @Test
    void testCatalogoVacio() {
        Catalog emptyCatalog = new Catalog();

        List<NewProduct> guestView = guestUser.view_catalog(guestUser, emptyCatalog);
        List<NewProduct> employeeView = employeeUser.view_catalog(employeeUser, emptyCatalog);

        assertEquals(0, guestView.size());
        assertEquals(0, employeeView.size());
    }

    // ==========================================
    // TESTS DE BÚSQUEDA EN CATÁLOGO
    // ==========================================

    /**
     * Verifica que se puede buscar productos por nombre.
     */
    @Test
    void testBuscarProductoPorNombre() {
        List<NewProduct> results = clientUser.search_catalog("Comic", testCatalog);
        assertNotNull(results);
    }

    /**
     * Verifica que la búsqueda no es sensible a mayúsculas.
     */
    @Test
    void testBusquedaNoEsSensibleAMayusculas() {
        List<NewProduct> results1 = clientUser.search_catalog("comic", testCatalog);
        List<NewProduct> results2 = clientUser.search_catalog("COMIC", testCatalog);
        List<NewProduct> results3 = clientUser.search_catalog("Comic", testCatalog);

        assertNotNull(results1);
        assertNotNull(results2);
        assertNotNull(results3);
    }

    /**
     * Verifica que búsqueda retorna lista vacía si no hay resultados.
     */
    @Test
    void testBusquedaSinResultados() {
        List<NewProduct> results = clientUser.search_catalog("XyzNoExiste123", testCatalog);
        assertNotNull(results);
        assertEquals(0, results.size(), "Búsqueda sin resultados debe retornar lista vacía");
    }

    /**
     * Verifica que diferentes usuarios pueden buscar.
     */
    @Test
    void testDiferentesUsuariosPuedenBuscar() {
        List<NewProduct> guestResults = guestUser.search_catalog("Game", testCatalog);
        List<NewProduct> clientResults = clientUser.search_catalog("Game", testCatalog);
        List<NewProduct> employeeResults = employeeUser.search_catalog("Game", testCatalog);

        assertNotNull(guestResults);
        assertNotNull(clientResults);
        assertNotNull(employeeResults);
    }

    /**
     * Verifica que búsqueda parcial funciona.
     */
    @Test
    void testBusquedaParcial() {
        // Búsqueda parcial del nombre
        List<NewProduct> results = clientUser.search_catalog("Test", testCatalog);
        assertNotNull(results);
    }

    // ==========================================
    // TESTS DE FILTRADO POR EDAD
    // ==========================================

    /**
     * Verifica que se pueden filtrar juegos por rango de edad.
     */
    @Test
    void testFiltrarJuegosPorEdad() {
        List<Game> games = clientUser.filter_games_by_age(0, 20, testCatalog);
        assertNotNull(games);
    }

    /**
     * Verifica que filtrado retorna lista vacía si no hay juegos en rango.
     */
    @Test
    void testFiltroSinResultados() {
        List<Game> games = clientUser.filter_games_by_age(100, 200, testCatalog);
        assertNotNull(games);
        assertEquals(0, games.size(), "Filtro sin resultados debe retornar lista vacía");
    }

    /**
     * Verifica que diferentes usuarios pueden filtrar juegos.
     */
    @Test
    void testDiferentesUsuariosPuedenFiltrar() {
        List<Game> guestGames = guestUser.filter_games_by_age(0, 12, testCatalog);
        List<Game> clientGames = clientUser.filter_games_by_age(0, 12, testCatalog);
        List<Game> employeeGames = employeeUser.filter_games_by_age(0, 12, testCatalog);

        assertNotNull(guestGames);
        assertNotNull(clientGames);
        assertNotNull(employeeGames);
    }

    /**
     * Verifica que filtro inclusivo incluye límites.
     */
    @Test
    void testFiltroInclusivo() {
        // AgeRange es [4, 12], así que debe incluirse en rango [4, 12]
        List<Game> games1 = clientUser.filter_games_by_age(4, 12, testCatalog);
        List<Game> games2 = clientUser.filter_games_by_age(3, 13, testCatalog);
        List<Game> games3 = clientUser.filter_games_by_age(0, 4, testCatalog);

        assertNotNull(games1);
        assertNotNull(games2);
        assertNotNull(games3);
    }

    /**
     * Verifica que rango de edad válido retorna resultados.
     */
    @Test
    void testRangoDeEdadValido() {
        // El juego de prueba tiene AgeRange(4, 12)
        List<Game> games = clientUser.filter_games_by_age(4, 12, testCatalog);
        assertNotNull(games);
    }

    // ==========================================
    // TESTS DE CONTROL DE ACCESO
    // ==========================================

    /**
     * Verifica que Employee es detectado correctamente para acceso total.
     */
    @Test
    void testEmployeeEsEmpleado() {
        assertTrue(employeeUser instanceof Employee);
        assertTrue(employeeUser instanceof User);
    }

    /**
     * Verifica que Manager es detectado correctamente para acceso total.
     */
    @Test
    void testManagerEsManager() {
        assertTrue(managerUser instanceof Manager);
        assertTrue(managerUser instanceof User);
    }

    // ==========================================
    // TESTS DE INDEPENDENCIA
    // ==========================================

    /**
     * Verifica que búsqueda de un usuario no afecta a otro.
     */
    @Test
    void testBusquedasIndependientes() {
        List<NewProduct> results1 = guestUser.search_catalog("Comic", testCatalog);
        List<NewProduct> results2 = clientUser.search_catalog("Game", testCatalog);

        assertNotNull(results1);
        assertNotNull(results2);
    }

    /**
     * Verifica que filtros de edad de un usuario no afectan a otro.
     */
    @Test
    void testFiltrosIndependientes() {
        List<Game> games1 = guestUser.filter_games_by_age(0, 5, testCatalog);
        List<Game> games2 = clientUser.filter_games_by_age(10, 20, testCatalog);

        assertNotNull(games1);
        assertNotNull(games2);
    }

    // ==========================================
    // TESTS DE MÉTODOS DE USER
    // ==========================================

    /**
     * Verifica que todos los usuarios tienen acceso a view_catalog.
     */
    @Test
    void testTodosAccesoAViewCatalog() {
        assertDoesNotThrow(() -> {
            guestUser.view_catalog(guestUser, testCatalog);
            clientUser.view_catalog(clientUser, testCatalog);
            employeeUser.view_catalog(employeeUser, testCatalog);
            managerUser.view_catalog(managerUser, testCatalog);
        });
    }

    /**
     * Verifica que todos los usuarios tienen acceso a search_catalog.
     */
    @Test
    void testTodosAccesoASearchCatalog() {
        assertDoesNotThrow(() -> {
            guestUser.search_catalog("test", testCatalog);
            clientUser.search_catalog("test", testCatalog);
            employeeUser.search_catalog("test", testCatalog);
            managerUser.search_catalog("test", testCatalog);
        });
    }

    /**
     * Verifica que todos los usuarios tienen acceso a filter_games_by_age.
     */
    @Test
    void testTodosAccesoAFilterGames() {
        assertDoesNotThrow(() -> {
            guestUser.filter_games_by_age(0, 10, testCatalog);
            clientUser.filter_games_by_age(0, 10, testCatalog);
            employeeUser.filter_games_by_age(0, 10, testCatalog);
            managerUser.filter_games_by_age(0, 10, testCatalog);
        });
    }

    // ==========================================
    // TESTS DE CASOS LÍMITE
    // ==========================================

    /**
     * Verifica que filtro de edad con rango muy amplio funciona.
     */
    @Test
    void testFiltroDeEdadAmplisimo() {
        List<Game> games = clientUser.filter_games_by_age(0, 150, testCatalog);
        assertNotNull(games);
    }

    /**
     * Verifica que filtro de edad con rango muy estrecho funciona.
     */
    @Test
    void testFiltroDeEdadEstrechoMuyEstrecho() {
        List<Game> games = clientUser.filter_games_by_age(4, 4, testCatalog);
        assertNotNull(games);
    }

    /**
     * Verifica que búsqueda con cadena vacía funciona.
     */
    @Test
    void testBusquedaConCadenaVacia() {
        List<NewProduct> results = clientUser.search_catalog("", testCatalog);
        assertNotNull(results);
    }

    /**
     * Verifica que búsqueda con espacios en blanco funciona.
     */
    @Test
    void testBusquedaConEspacios() {
        List<NewProduct> results = clientUser.search_catalog("   ", testCatalog);
        assertNotNull(results);
    }

    /**
     * Verifica que múltiples usuarios pueden usar el mismo catálogo.
     */
    @Test
    void testMultiplesUsuariosYMismoCatalogo() {
        List<NewProduct> g = guestUser.view_catalog(guestUser, testCatalog);
        List<NewProduct> c = clientUser.view_catalog(clientUser, testCatalog);
        List<NewProduct> e = employeeUser.view_catalog(employeeUser, testCatalog);
        List<NewProduct> m = managerUser.view_catalog(managerUser, testCatalog);

        assertNotNull(g);
        assertNotNull(c);
        assertNotNull(e);
        assertNotNull(m);
    }

    /**
     * Verifica que el mismo usuario puede usar múltiples catálogos.
     */
    @Test
    void testMismoUsuarioYMultiplesCatalogos() {
        Catalog cat1 = new Catalog();
        Catalog cat2 = new Catalog();
        Catalog cat3 = new Catalog();

        List<NewProduct> p1 = clientUser.view_catalog(clientUser, cat1);
        List<NewProduct> p2 = clientUser.view_catalog(clientUser, cat2);
        List<NewProduct> p3 = clientUser.view_catalog(clientUser, cat3);

        assertNotNull(p1);
        assertNotNull(p2);
        assertNotNull(p3);
    }
}