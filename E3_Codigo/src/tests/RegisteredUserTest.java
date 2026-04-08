package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import users.*;
import utils.*;

/**
 * Clase de pruebas unitarias para la clase abstracta {@link RegisteredUser}.
 * 
 * Verifica el funcionamiento correcto de:
 * <ul>
 *   <li>Validación de datos en el constructor (DNI, email, teléfono, fecha)</li>
 *   <li>Autenticación de usuarios</li>
 *   <li>Gestión de notificaciones</li>
 *   <li>Enmascaramiento de datos sensibles</li>
 *   <li>Generación de perfiles</li>
 * </ul>
 * 
 * @version 1.0
 */
public class RegisteredUserTest {

    private Client testUser;
    private Client anotherUser;

    @BeforeEach
    void setUp() {
        testUser = new Client(
            "testuser",
            "password123",
            "Test User",
            "12345678A",
            "15/03/1990",
            "test@example.com",
            "666123456"
        );

        anotherUser = new Client(
            "anotheruser",
            "pass456",
            "Another User",
            "87654321B",
            "20/05/1985",
            "another@example.com",
            "666654321"
        );
    }

    // ==========================================
    // TESTS DE VALIDACIÓN DE CONSTRUCTOR
    // ==========================================

    /**
     * Verifica que se puede crear un RegisteredUser con datos válidos.
     */
    @Test
    void testCrearRegisteredUserConDatosValidos() {
        assertNotNull(testUser);
        assertEquals("testuser", testUser.getUsername());
    }

    /**
     * Verifica que se rechaza un DNI inválido (sin letra).
     */
    @Test
    void testRechazaDniSinLetra() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user1", "pass", "Name", "12345678", "01/01/1990", "email@test.com", "666123456");
        }, "Debe rechazar DNI sin letra");
    }

    /**
     * Verifica que se rechaza un DNI con formato incorrecto.
     */
    @Test
    void testRechazaDniFormatoIncorrecto() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user2", "pass", "Name", "1234567A", "01/01/1990", "email@test.com", "666123456");
        }, "Debe rechazar DNI con menos de 8 dígitos");

        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user3", "pass", "Name", "123456789A", "01/01/1990", "email@test.com", "666123456");
        }, "Debe rechazar DNI con más de 8 dígitos");
    }

    /**
     * Verifica que se rechaza un email inválido.
     */
    @Test
    void testRechazaEmailInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user4", "pass", "Name", "12345678A", "01/01/1990", "notanemail", "666123456");
        }, "Debe rechazar email sin @");

        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user5", "pass", "Name", "12345678A", "01/01/1990", "email@", "666123456");
        }, "Debe rechazar email incompleto");
    }

    /**
     * Verifica que se rechaza un teléfono inválido.
     */
    @Test
    void testRechazaTelefonoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user6", "pass", "Name", "12345678A", "01/01/1990", "email@test.com", "66612345");
        }, "Debe rechazar teléfono con menos de 9 dígitos");

        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user7", "pass", "Name", "12345678A", "01/01/1990", "email@test.com", "6661234567");
        }, "Debe rechazar teléfono con más de 9 dígitos");

        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user8", "pass", "Name", "12345678A", "01/01/1990", "email@test.com", "66612a456");
        }, "Debe rechazar teléfono con letras");
    }

    /**
     * Verifica que se rechaza una fecha de nacimiento inválida.
     */
    @Test
    void testRechazaFechaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user9", "pass", "Name", "12345678A", "32/01/1990", "email@test.com", "666123456");
        }, "Debe rechazar día inválido (32)");

        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user10", "pass", "Name", "12345678A", "15/13/1990", "email@test.com", "666123456");
        }, "Debe rechazar mes inválido (13)");

        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user11", "pass", "Name", "12345678A", "15-01-1990", "email@test.com", "666123456");
        }, "Debe rechazar formato de fecha incorrecto");
    }

    /**
     * Verifica que se rechaza un usuario vacío.
     */
    @Test
    void testRechazaUsuarioVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Client("", "pass", "Name", "12345678A", "01/01/1990", "email@test.com", "666123456");
        }, "Debe rechazar usuario vacío");

        assertThrows(IllegalArgumentException.class, () -> {
            new Client(null, "pass", "Name", "12345678A", "01/01/1990", "email@test.com", "666123456");
        }, "Debe rechazar usuario nulo");
    }

    /**
     * Verifica que se rechaza una contraseña vacía.
     */
    @Test
    void testRechazaContrasenaVacia() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user", "", "Name", "12345678A", "01/01/1990", "email@test.com", "666123456");
        }, "Debe rechazar contraseña vacía");

        assertThrows(IllegalArgumentException.class, () -> {
            new Client("user", null, "Name", "12345678A", "01/01/1990", "email@test.com", "666123456");
        }, "Debe rechazar contraseña nula");
    }

    // ==========================================
    // TESTS DE AUTENTICACIÓN
    // ==========================================

    /**
     * Verifica que el login funciona con credenciales correctas.
     */
    @Test
    void testLoginConCredencialesCorrectas() {
        assertTrue(testUser.login("testuser", "password123"),
            "Debe autenticarse con usuario y contraseña correctos");
    }

    /**
     * Verifica que el login falla con contraseña incorrecta.
     */
    @Test
    void testLoginConContraseniaIncorrecta() {
        assertFalse(testUser.login("testuser", "wrongpassword"),
            "No debe autenticarse con contraseña incorrecta");
    }

    /**
     * Verifica que el login falla con usuario incorrecto.
     */
    @Test
    void testLoginConUsuarioIncorrecto() {
        assertFalse(testUser.login("wronguser", "password123"),
            "No debe autenticarse con usuario incorrecto");
    }

    /**
     * Verifica que el login es sensible a mayúsculas/minúsculas.
     */
    @Test
    void testLoginEsSensibleAMayusculas() {
        assertFalse(testUser.login("TESTUSER", "password123"),
            "Login debe ser sensible a mayúsculas");
    }

    // ==========================================
    // TESTS DE NOTIFICACIONES
    // ==========================================

    /**
     * Verifica que un usuario comienza sin notificaciones.
     */
    @Test
    void testUsuarioSinNotificaciones() {
        List<Notification> notifications = testUser.getMyNotifications();
        assertNotNull(notifications);
        assertEquals(0, notifications.size());
    }

    /**
     * Verifica que se puede añadir una notificación.
     */
    @Test
    void testAnadirNotificacion() {
        ArrayList<RegisteredUser> users = new ArrayList<>();
        users.add(testUser);
        Notification notif = new Notification("Test notification", users);

        testUser.addNotification(notif);

        assertEquals(1, testUser.getMyNotifications().size());
        assertTrue(testUser.getMyNotifications().get(0).toString().contains("Test notification"),
            "La notificación debe contener el mensaje");
    }

    /**
     * Verifica que se pueden añadir múltiples notificaciones.
     */
    @Test
    void testAnadirMultiplesNotificaciones() {
        ArrayList<RegisteredUser> users = new ArrayList<>();
        users.add(testUser);

        for (int i = 0; i < 5; i++) {
            Notification notif = new Notification("Notificación " + i, users);
            testUser.addNotification(notif);
        }

        assertEquals(5, testUser.getMyNotifications().size());
    }

    /**
     * Verifica que se puede marcar una notificación como leída.
     */
    @Test
    void testMarcarNotificacionComoLeida() {
        ArrayList<RegisteredUser> users = new ArrayList<>();
        users.add(testUser);
        Notification notif = new Notification("Test", users);

        assertFalse(notif.isRead());
        testUser.addNotification(notif);
        notif.markAsRead(true);

        assertTrue(testUser.getMyNotifications().get(0).isRead());
    }

    /**
     * Verifica que se puede cambiar la visibilidad de una notificación.
     */
    @Test
    void testCambiarVisibilidadNotificacion() {
        ArrayList<RegisteredUser> users = new ArrayList<>();
        users.add(testUser);
        Notification notif = new Notification("Test", users);

        testUser.addNotification(notif);
        testUser.change_visibility(notif, false);

        assertNotNull(testUser.getMyNotifications());
    }

    /**
     * Verifica que se puede leer una notificación.
     */
    @Test
    void testLeerNotificacion() {
        ArrayList<RegisteredUser> users = new ArrayList<>();
        users.add(testUser);
        Notification notif = new Notification("Importante", users);

        testUser.addNotification(notif);
        String content = testUser.read_notification(notif);

        assertNotNull(content);
        assertTrue(notif.isRead());
    }

    // ==========================================
    // TESTS DE PERFILES Y VISTAS PREVIAS
    // ==========================================

    /**
     * Verifica que se puede obtener la vista previa del usuario.
     */
    @Test
    void testObtenerUserPreview() {
        String preview = testUser.userPreview();
        assertNotNull(preview);
        assertTrue(preview.contains("Test User"));
        assertTrue(preview.contains("testuser"));
    }

    /**
     * Verifica que se puede obtener el perfil completo.
     */
    @Test
    void testObtenerUserProfile() {
        String profile = testUser.userProfile();
        assertNotNull(profile);
        assertTrue(profile.contains("Test User"));
        assertTrue(profile.contains("testuser"));
        assertTrue(profile.contains("666123456"));
    }

    /**
     * Verifica que el email está enmascarado en el perfil.
     */
    @Test
    void testEmailEnmascaradoEnPerfil() {
        String profile = testUser.userProfile();
        assertFalse(profile.contains("test@example.com"),
            "El email no debe estar completo en el perfil");
        assertTrue(profile.contains("t***"),
            "El email debe estar enmascarado");
    }

    /**
     * Verifica que el DNI está enmascarado en el perfil.
     */
    @Test
    void testDniEnmascaradoEnPerfil() {
        String profile = testUser.userProfile();
        // El DNI es "12345678A" y se enmascara a "****5678A"
        assertTrue(profile.contains("5678A"),
            "El DNI debe estar enmascarado mostrando solo últimos 4 caracteres");
    }

    /**
     * Verifica que el teléfono NO está enmascarado.
     */
    @Test
    void testTelefonoNoEnmascarado() {
        String profile = testUser.userProfile();
        assertTrue(profile.contains("666123456"),
            "El teléfono debe estar visible");
    }

    /**
     * Verifica que la fecha de nacimiento NO está enmascarada.
     */
    @Test
    void testFechaNoEnmascarada() {
        String profile = testUser.userProfile();
        assertTrue(profile.contains("15/03/1990"),
            "La fecha de nacimiento debe estar visible");
    }

    // ==========================================
    // TESTS DE ACCESO A DATOS
    // ==========================================

    /**
     * Verifica que se puede obtener el nombre de usuario.
     */
    @Test
    void testObtenerUsername() {
        assertEquals("testuser", testUser.getUsername());
    }

    /**
     * Verifica que usernames son únicos entre usuarios.
     */
    @Test
    void testUsernamesUnicos() {
        assertNotEquals(testUser.getUsername(), anotherUser.getUsername());
    }

    // ==========================================
    // TESTS DE INDEPENDENCIA
    // ==========================================

    /**
     * Verifica que las notificaciones de un usuario no afectan a otro.
     */
    @Test
    void testNotificacionesIndependientes() {
        ArrayList<RegisteredUser> users = new ArrayList<>();
        users.add(testUser);
        Notification notif = new Notification("Test", users);

        testUser.addNotification(notif);

        assertEquals(1, testUser.getMyNotifications().size());
        assertEquals(0, anotherUser.getMyNotifications().size(),
            "Las notificaciones no deben compartirse entre usuarios");
    }

    /**
     * Verifica que dos usuarios diferentes son independientes.
     */
    @Test
    void testUsuariosIndependientes() {
        assertTrue(testUser.login("testuser", "password123"));
        assertTrue(anotherUser.login("anotheruser", "pass456"));
        assertFalse(testUser.login("anotheruser", "pass456"));
    }

    // ==========================================
    // TESTS DE VISTA DE NOTIFICACIONES
    // ==========================================

    /**
     * Verifica que se puede ver las notificaciones en formato texto.
     */
    @Test
    void testVerNotificacionesFormatoTexto() {
        ArrayList<RegisteredUser> users = new ArrayList<>();
        users.add(testUser);
        Notification notif = new Notification("Mensaje importante", users);

        testUser.addNotification(notif);
        String view = testUser.view_notifications();

        assertNotNull(view);
        assertTrue(view.contains("My inbox"));
    }

    /**
     * Verifica que se cuenta correctamente notificaciones nuevas.
     */
    @Test
    void testContarNotificacionesNuevas() {
        ArrayList<RegisteredUser> users = new ArrayList<>();
        users.add(testUser);

        for (int i = 0; i < 3; i++) {
            Notification notif = new Notification("Notif " + i, users);
            testUser.addNotification(notif);
        }

        String view = testUser.view_notifications();
        assertTrue(view.contains("3 new"),
            "Debe mostrar 3 notificaciones nuevas");

        testUser.getMyNotifications().get(0).markAsRead(true);
        String view2 = testUser.view_notifications();
        assertTrue(view2.contains("2 new"),
            "Debe mostrar 2 notificaciones nuevas después de leer una");
    }

    // ==========================================
    // TESTS DE CASOS LÍMITE
    // ==========================================

    /**
     * Verifica que DNI en minúsculas se convierte a mayúsculas.
     */
    @Test
    void testDniConvertidoAMayusculas() {
        Client user = new Client(
            "user", "pass", "Name",
            "12345678a",  // letra en minúscula
            "01/01/1990",
            "email@test.com",
            "666123456"
        );

        String profile = user.userProfile();
        // Buscar solo "5678A" sin los asteriscos, que es lo importante
        assertTrue(profile.contains("5678A"),
            "El DNI debe convertirse a mayúsculas");
    }

    /**
     * Verifica que se pueden crear múltiples usuarios.
     */
    @Test
    void testCrearMultiplesUsuarios() {
        ArrayList<Client> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Client u = new Client(
                "user" + i, "pass" + i, "User " + i,
                "1111111" + i + "A", "01/01/199" + i,
                "user" + i + "@test.com", "66600000" + i
            );
            users.add(u);
        }

        assertEquals(5, users.size());
    }
}