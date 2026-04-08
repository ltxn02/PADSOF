package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import users.*;
import catalog.*;
import utils.*;

/**
 * Clase de pruebas unitarias para la clase {@link Guest}.
 * 
 * @version 1.0
 */
public class GuestTest {

    private Guest testGuest;
    private Guest anotherGuest;
    private Catalog testCatalog;
    private ArrayList<Category> testCategories;
    private ArrayList<Review> testReviews;

    @BeforeEach
    void setUp() {
        // Crear instancias de Guest (no necesitan parámetros)
        testGuest = new Guest();
        anotherGuest = new Guest();

        // Preparar catálogo y categorías para pruebas
        testCatalog = new Catalog();
        Category testCategory = new Category("Test Category");
        testCategories = new ArrayList<>();
        testCategories.add(testCategory);
        testReviews = new ArrayList<>();
    }

    // ==========================================
    // TESTS DE INICIALIZACIÓN
    // ==========================================

    /**
     * Verifica que se puede crear una instancia de Guest.
     */
    @Test
    void testCrearGuest() {
        assertNotNull(testGuest);
        assertTrue(testGuest instanceof Guest);
    }

    /**
     * Verifica que Guest hereda de User.
     */
    @Test
    void testGuestHeredaDeUser() {
        assertTrue(testGuest instanceof User);
    }

    /**
     * Verifica que se pueden crear múltiples instancias de Guest.
     */
    @Test
    void testCrearMultiplesGuests() {
        Guest guest1 = new Guest();
        Guest guest2 = new Guest();
        Guest guest3 = new Guest();

        assertNotNull(guest1);
        assertNotNull(guest2);
        assertNotNull(guest3);
        assertNotSame(guest1, guest2);
        assertNotSame(guest2, guest3);
    }

    // ==========================================
    // TESTS DE CREACIÓN DE CUENTA DE CLIENTE
    // ==========================================

    /**
     * Verifica que un Guest puede crear una cuenta de cliente con datos válidos.
     */
    @Test
    void testCrearClienteDesdeGuest() {
        Client newClient = testGuest.createClientAccount(
            "newclient",
            "password123",
            "New Client",
            "12345678A",
            "15/03/1990",
            "newclient@test.com",
            "666123456"
        );

        assertNotNull(newClient);
        assertInstanceOf(Client.class, newClient);
        assertEquals("newclient", newClient.getUsername());
    }

    /**
     * Verifica que el cliente creado tiene todas sus estructuras inicializadas.
     */
    @Test
    void testClienteCreadoTieneCarrito() {
        Client newClient = testGuest.createClientAccount(
            "client2",
            "pass",
            "Client 2",
            "11111111B",
            "01/01/1991",
            "client2@test.com",
            "666111111"
        );

        assertNotNull(newClient.getShoppingCart());
    }

    /**
     * Verifica que el cliente creado tiene histórico de pedidos.
     */
    @Test
    void testClienteCreadoTieneHistoricoPedidos() {
        Client newClient = testGuest.createClientAccount(
            "client3",
            "pass",
            "Client 3",
            "22222222C",
            "02/02/1992",
            "client3@test.com",
            "666222222"
        );

        assertNotNull(newClient.getOrderHistoric());
    }

    /**
     * Verifica que el cliente creado tiene histórico de intercambios.
     */
    @Test
    void testClienteCreadoTieneHistoricoIntercambios() {
        Client newClient = testGuest.createClientAccount(
            "client4",
            "pass",
            "Client 4",
            "33333333D",
            "03/03/1993",
            "client4@test.com",
            "666333333"
        );

        assertNotNull(newClient.getExchangeHistoric());
    }

    /**
     * Verifica que se pueden crear múltiples clientes desde Guest.
     */
    @Test
    void testCrearMultiplesClientesDesdeGuest() {
        ArrayList<Client> clients = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Client client = testGuest.createClientAccount(
                "client" + i,
                "pass" + i,
                "Client " + i,
                "1111111" + i + "A",
                "01/01/199" + i,
                "client" + i + "@test.com",
                "66600000" + i
            );
            clients.add(client);
        }

        assertEquals(3, clients.size());
        assertTrue(clients.get(0) instanceof Client);
        assertTrue(clients.get(1) instanceof Client);
        assertTrue(clients.get(2) instanceof Client);
    }

    /**
     * Verifica que diferentes Guests pueden crear clientes.
     */
    @Test
    void testDiferentesGuestsCreanClientes() {
        Client client1 = testGuest.createClientAccount(
            "guest1client",
            "pass",
            "Guest 1 Client",
            "44444444E",
            "04/04/1994",
            "g1client@test.com",
            "666444444"
        );

        Client client2 = anotherGuest.createClientAccount(
            "guest2client",
            "pass",
            "Guest 2 Client",
            "55555555F",
            "05/05/1995",
            "g2client@test.com",
            "666555555"
        );

        assertNotEquals(client1.getUsername(), client2.getUsername());
        assertTrue(client1.login("guest1client", "pass"));
        assertTrue(client2.login("guest2client", "pass"));
    }

    // ==========================================
    // TESTS DE VALIDACIÓN DE DATOS
    // ==========================================

    /**
     * Verifica que se rechaza un DNI inválido al crear cliente.
     */
    @Test
    void testRechazaDniInvalidoAlCrearCliente() {
        assertThrows(IllegalArgumentException.class, () -> {
            testGuest.createClientAccount(
                "baduser",
                "pass",
                "Bad User",
                "12345678",  // Sin letra
                "01/01/1990",
                "bad@test.com",
                "666123456"
            );
        }, "Debe rechazar DNI sin letra");
    }

    /**
     * Verifica que se rechaza un email inválido al crear cliente.
     */
    @Test
    void testRechazaEmailInvalidoAlCrearCliente() {
        assertThrows(IllegalArgumentException.class, () -> {
            testGuest.createClientAccount(
                "baduser",
                "pass",
                "Bad User",
                "12345678A",
                "01/01/1990",
                "notanemail",  // Sin @
                "666123456"
            );
        }, "Debe rechazar email inválido");
    }

    /**
     * Verifica que se rechaza un teléfono inválido al crear cliente.
     */
    @Test
    void testRechazaTelefonoInvalidoAlCrearCliente() {
        assertThrows(IllegalArgumentException.class, () -> {
            testGuest.createClientAccount(
                "baduser",
                "pass",
                "Bad User",
                "12345678A",
                "01/01/1990",
                "user@test.com",
                "66612345"  // Menos de 9 dígitos
            );
        }, "Debe rechazar teléfono inválido");
    }

    /**
     * Verifica que se rechaza una fecha inválida al crear cliente.
     */
    @Test
    void testRechazaFechaInvalidaAlCrearCliente() {
        assertThrows(IllegalArgumentException.class, () -> {
            testGuest.createClientAccount(
                "baduser",
                "pass",
                "Bad User",
                "12345678A",
                "32/01/1990",  // Día inválido
                "user@test.com",
                "666123456"
            );
        }, "Debe rechazar fecha inválida");
    }

    /**
     * Verifica que se rechaza usuario vacío al crear cliente.
     */
    @Test
    void testRechazaUsuarioVacioAlCrearCliente() {
        assertThrows(IllegalArgumentException.class, () -> {
            testGuest.createClientAccount(
                "",  // Usuario vacío
                "pass",
                "User",
                "12345678A",
                "01/01/1990",
                "user@test.com",
                "666123456"
            );
        }, "Debe rechazar usuario vacío");
    }

    /**
     * Verifica que se rechaza contraseña vacía al crear cliente.
     */
    @Test
    void testRechazaContrasenaVaciaAlCrearCliente() {
        assertThrows(IllegalArgumentException.class, () -> {
            testGuest.createClientAccount(
                "user",
                "",  // Contraseña vacía
                "User",
                "12345678A",
                "01/01/1990",
                "user@test.com",
                "666123456"
            );
        }, "Debe rechazar contraseña vacía");
    }

    // ==========================================
    // TESTS DE ACCESO AL CATÁLOGO
    // ==========================================

    /**
     * Verifica que un Guest puede ver el catálogo (heredado de User).
     */
    @Test
    void testGuestPuedeVerCatalogo() {
        Catalog catalog = new Catalog();
        
        var products = testGuest.view_catalog(testGuest, catalog);
        assertNotNull(products);
    }

    /**
     * Verifica que un Guest puede usar las funcionalidades de User.
     */
    @Test
    void testGuestTieneAccesoAFuncionesDeUser() {
        assertNotNull(testGuest);
        // Guest hereda todos los métodos de User
        assertTrue(testGuest instanceof User);
    }

    // ==========================================
    // TESTS DE INDEPENDENCIA
    // ==========================================

    /**
     * Verifica que múltiples Guests son independientes.
     */
    @Test
    void testMultiplesGuestsIndependientes() {
        Guest guest1 = new Guest();
        Guest guest2 = new Guest();

        Client client1 = guest1.createClientAccount(
            "u1", "p1", "U1", "11111111A", "01/01/1990", "u1@test.com", "666111111"
        );

        Client client2 = guest2.createClientAccount(
            "u2", "p2", "U2", "22222222B", "02/02/1991", "u2@test.com", "666222222"
        );

        assertNotEquals(client1.getUsername(), client2.getUsername());
        assertNotEquals(guest1, guest2);
    }

    /**
     * Verifica que clientes creados por diferentes Guests son independientes.
     */
    @Test
    void testClientesDeGuestsDiferentesIndependientes() {
        Guest guest1 = new Guest();
        Guest guest2 = new Guest();

        Client c1 = guest1.createClientAccount(
            "clientA", "passA", "Client A", "33333333C", "03/03/1992", "a@test.com", "666333333"
        );

        Client c2 = guest2.createClientAccount(
            "clientB", "passB", "Client B", "44444444D", "04/04/1993", "b@test.com", "666444444"
        );

        assertEquals(0, c1.getCarteraSegundaMano().size());
        assertEquals(0, c2.getCarteraSegundaMano().size());

        c1.registerSecondHandProduct("Producto A", "Desc", "img/a.jpg", ItemType.GAME);

        assertEquals(1, c1.getCarteraSegundaMano().size());
        assertEquals(0, c2.getCarteraSegundaMano().size(),
            "Los productos de un cliente no deben afectar a otro");
    }

    // ==========================================
    // TESTS DE CONVERSIÓN DE GUEST A CLIENT
    // ==========================================

    /**
     * Verifica que un Guest sigue siendo Guest después de crear cliente.
     */
    @Test
    void testGuestSiguesSiendoGuestDespuesDeCrearCliente() {
        assertTrue(testGuest instanceof Guest);

        Client newClient = testGuest.createClientAccount(
            "converted",
            "pass",
            "Converted User",
            "55555555E",
            "05/05/1994",
            "conv@test.com",
            "666555555"
        );

        assertTrue(testGuest instanceof Guest, "Guest debe seguir siendo Guest");
        assertTrue(newClient instanceof Client, "El nuevo usuario debe ser Client");
        assertNotSame(testGuest, newClient);
    }

    /**
     * Verifica que se pueden crear múltiples clientes desde el mismo Guest.
     */
    @Test
    void testMismoGuestCreaMultiplesClientes() {
        Guest guest = new Guest();

        ArrayList<Client> clients = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Client c = guest.createClientAccount(
                "user" + i,
                "pass" + i,
                "User " + i,
                "1111111" + i + "A",
                "01/01/199" + i,
                "user" + i + "@test.com",
                "66600000" + i
            );
            clients.add(c);
        }

        assertEquals(5, clients.size());
        for (Client c : clients) {
            assertTrue(c instanceof Client);
        }
    }

    // ==========================================
    // TESTS DE CASOS LÍMITE
    // ==========================================

    /**
     * Verifica que se pueden crear clientes con datos válidos pero diferentes.
     */
    @Test
    void testCrearClientesConDatosVariados() {
        Client c1 = testGuest.createClientAccount(
            "short", "p", "A", "66666666F", "06/06/1995", "s@t.co", "666666666"
        );

        Client c2 = testGuest.createClientAccount(
            "verylongusernamethatisvalid",
            "verylongpasswordthatisvalid",
            "Very Long Name That Is Valid",
            "77777777G",
            "07/07/1996",
            "verylongemail@subdomain.example.com",
            "666777777"
        );

        assertTrue(c1.login("short", "p"));
        assertTrue(c2.login("verylongusernamethatisvalid", "verylongpasswordthatisvalid"));
    }

    /**
     * Verifica que el cliente creado puede autenticarse.
     */
    @Test
    void testClienteCreadoPuedeAutenticarse() {
        Client newClient = testGuest.createClientAccount(
            "authuser",
            "authpass",
            "Auth User",
            "88888888H",
            "08/08/1997",
            "auth@test.com",
            "666888888"
        );

        assertTrue(newClient.login("authuser", "authpass"),
            "El cliente debe poder autenticarse con las credenciales proporcionadas");
    }

    /**
     * Verifica que el cliente creado hereda todas las funcionalidades.
     */
    @Test
    void testClienteHeredaTodosDeMiembro() {
        Client newClient = testGuest.createClientAccount(
            "fulluser",
            "fullpass",
            "Full User",
            "99999999I",
            "09/09/1998",
            "full@test.com",
            "666999999"
        );

        assertTrue(newClient instanceof Client);
        assertTrue(newClient instanceof RegisteredUser);
        assertTrue(newClient instanceof User);
        
        assertNotNull(newClient.getShoppingCart());
        assertNotNull(newClient.getOrderHistoric());
        assertNotNull(newClient.getExchangeHistoric());
        assertNotNull(newClient.getCarteraSegundaMano());
    }
}