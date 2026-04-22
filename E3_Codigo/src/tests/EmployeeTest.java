package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import products.*;
import products.catalog.Catalog;
import products.catalog.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import users.*;
import transactions.*;
import utils.*;

/**
 * Clase de pruebas unitarias para la clase {@link Employee}.
 * 
 * Verifica el funcionamiento correcto de:
 * <ul>
 *   <li>Creación de cuentas de empleado</li>
 *   <li>Gestión de activación/desactivación</li>
 *   <li>Gestión de permisos y roles</li>
 *   <li>Carga y edición de productos</li>
 *   <li>Validación de seguridad (permisos)</li>
 *   <li>Gestión de productos de segunda mano</li>
 *   <li>Validación y cancelación de intercambios</li>
 * </ul>
 * 
 * @version 1.0
 */
public class EmployeeTest {

    private Employee employee;
    private Employee employeeWithoutPermissions;
    private Catalog testCatalog;
    private Category testCategory;
    private ArrayList<Category> testCategories;
    private ArrayList<Review> testReviews;
    private Client testClient;

    @BeforeEach
    void setUp() {
        // 1. Crear empleados de prueba
        employee = new Employee(
            "employee_test",
            "password123",
            "Test Employee",
            "12345678A",
            "15/03/1995",
            "employee@test.com",
            "666123456",
            30000.0,
            true
        );

        employeeWithoutPermissions = new Employee(
            "employee_limited",
            "password123",
            "Limited Employee",
            "87654321B",
            "20/05/1990",
            "limited@test.com",
            "666654321",
            25000.0,
            true
        );

        // 2. Crear catálogo de prueba
        testCatalog = new Catalog();

        // 3. Crear categorías de prueba
        testCategory = new Category("Test Category");
        testCategories = new ArrayList<>();
        testCategories.add(testCategory);

        // 4. Crear lista de reseñas
        testReviews = new ArrayList<>();

        // 5. Crear cliente de prueba
        testClient = new Client(
            "client_test",
            "password",
            "Test Client",
            "11111111C",
            "01/01/2000",
            "client@test.com",
            "666111111"
        );
    }

    // ==========================================
    // TESTS DE INICIALIZACIÓN
    // ==========================================

    /**
     * Verifica que se puede crear un empleado correctamente.
     */
    @Test
    void testCrearEmpleado() {
        assertNotNull(employee);
        assertEquals("employee_test", employee.getUsername());
        assertTrue(employee.isEnabled());
    }

    /**
     * Verifica que el empleado hereda de Staff.
     */
    @Test
    void testEmpleadoHeredaDeStaff() {
        assertTrue(employee instanceof Staff);
        assertTrue(employee instanceof RegisteredUser);
        assertTrue(employee instanceof User);
    }

    /**
     * Verifica que se puede crear un empleado desactivado.
     */
    @Test
    void testCrearEmpleadoDesactivado() {
        Employee inactiveEmployee = new Employee(
            "inactive",
            "pass",
            "Inactive",
            "22222222D",
            "10/10/1992",
            "inactive@test.com",
            "666222222",
            28000.0,
            false
        );

        assertFalse(inactiveEmployee.isEnabled());
    }

    // ==========================================
    // TESTS DE ACTIVACIÓN/DESACTIVACIÓN
    // ==========================================

    /**
     * Verifica que se puede activar un empleado.
     */
    @Test
    void testActivarEmpleado() {
        Employee emp = new Employee(
            "emp1", "pass", "Emp1", "33333333E", "01/01/1993",
            "emp1@test.com", "666333333", 30000.0, false
        );

        assertFalse(emp.isEnabled());
        emp.activateEmployee();
        assertTrue(emp.isEnabled());
    }

    /**
     * Verifica que se puede desactivar un empleado.
     */
    @Test
    void testDesactivarEmpleado() {
        assertTrue(employee.isEnabled());
        employee.desactivateEmployee();
        assertFalse(employee.isEnabled());
    }

    // ==========================================
    // TESTS DE PERMISOS
    // ==========================================

    /**
     * Verifica que se puede añadir un permiso a un empleado.
     */
    @Test
    void testAnadirPermiso() {
        Permission permiso = Permission.PRODUCT_LOAD;
        employee.add_permisions(permiso);

        assertTrue(employee.permisosEmpleado().contains(permiso));
    }

    /**
     * Verifica que se puede revocar un permiso.
     */
    @Test
    void testRevocarPermiso() {
        Permission permiso = Permission.PRODUCT_EDIT;
        employee.add_permisions(permiso);
        assertTrue(employee.permisosEmpleado().contains(permiso));

        employee.delete_permisions(permiso);
        assertFalse(employee.permisosEmpleado().contains(permiso));
    }

    /**
     * Verifica que se pueden obtener todos los permisos.
     */
    @Test
    void testObtenerPermisos() {
        Permission p1 = Permission.PRODUCT_LOAD;
        Permission p2 = Permission.PRODUCT_EDIT;

        employee.add_permisions(p1);
        employee.add_permisions(p2);

        ArrayList<Permission> permisos = employee.permisosEmpleado();
        assertEquals(2, permisos.size());
        assertTrue(permisos.contains(p1));
        assertTrue(permisos.contains(p2));
    }

    /**
     * Verifica que revocar un permiso que no existe no causa error.
     */
    @Test
    void testRevocarPermisoNoExistente() {
        Permission permiso = Permission.PRODUCT_LOAD;
        assertDoesNotThrow(() -> {
            employee.delete_permisions(permiso);
        });
    }

    // ==========================================
    // TESTS DE ROLES
    // ==========================================

    /**
     * Verifica que se puede añadir un rol.
     */
    @Test
    void testAnadirRol() {
        EmployeeRoles rol = EmployeeRoles.ORDERS_EMPLOYEE;
        assertDoesNotThrow(() -> {
            employee.add_roles(rol);
        });
    }

    /**
     * Verifica que se puede revocar un rol.
     */
    @Test
    void testRevocarRol() {
        EmployeeRoles rol = EmployeeRoles.ORDERS_EMPLOYEE;
        employee.add_roles(rol);
        assertDoesNotThrow(() -> {
            employee.delete_roles(rol);
        });
    }

    // ==========================================
    // TESTS DE CARGA DE PRODUCTOS
    // ==========================================

    /**
     * Verifica que un empleado con permiso puede cargar un producto.
     */
    @Test
    void testCargarProductoConPermiso() {
        employee.add_permisions(Permission.PRODUCT_LOAD);

        ArrayList<String> authors = new ArrayList<>();
        authors.add("Test Author");

        Map<String, Object> comicData = new HashMap<>();
        comicData.put("name", "Test Comic");
        comicData.put("description", "Comic de prueba");
        comicData.put("price", 10.0);
        comicData.put("picturePath", "img/comic.jpg");
        comicData.put("stock", 100);
        comicData.put("categories", testCategories);
        comicData.put("nPages", 200);
        comicData.put("publisher", "Test Publisher");
        comicData.put("publicationYear", 2020);
        comicData.put("writtenBy", authors);

        assertDoesNotThrow(() -> {
            employee.loadProduct(testCatalog, ItemType.COMIC, comicData);
        });

        assertEquals(1, testCatalog.allProducts().size());
    }

    /**
     * Verifica que un empleado sin permiso no puede cargar productos.
     */
    @Test
    void testCargarProductoSinPermiso() {
        ArrayList<String> authors = new ArrayList<>();

        Map<String, Object> comicData = new HashMap<>();
        comicData.put("name", "Test Comic");
        comicData.put("description", "Comic");
        comicData.put("price", 10.0);
        comicData.put("picturePath", "img/comic.jpg");
        comicData.put("stock", 50);
        comicData.put("categories", testCategories);
        comicData.put("nPages", 200);
        comicData.put("publisher", "Publisher");
        comicData.put("publicationYear", 2020);
        comicData.put("writtenBy", authors);

        assertThrows(SecurityException.class, () -> {
            employeeWithoutPermissions.loadProduct(testCatalog, ItemType.COMIC, comicData);
        });
    }

    // ==========================================
    // TESTS DE EDICIÓN DE PRODUCTOS
    // ==========================================

    /**
     * Verifica que un empleado con permiso puede cambiar la visibilidad de un producto.
     */
    @Test
    void testCambiarVisibilidadConPermiso() {
        employee.add_permisions(Permission.PRODUCT_EDIT);

        Comic comic = new Comic(
            "Test Comic",
            "Comic",
            10.0,
            "img/comic.jpg",
            50,
            testCategories,
            testReviews,
            null,
            200,
            "Publisher",
            2020,
            new ArrayList<String>()
        );

        assertTrue(comic.isActive());
        assertDoesNotThrow(() -> {
            employee.changeVisibilityProduct(comic, false);
        });
        assertFalse(comic.isActive());
    }

    /**
     * Verifica que un empleado sin permiso no puede cambiar visibilidad.
     */
    @Test
    void testCambiarVisibilidadSinPermiso() {
        Comic comic = new Comic(
            "Test",
            "Comic",
            10.0,
            "img/comic.jpg",
            50,
            testCategories,
            testReviews,
            null,
            200,
            "Publisher",
            2020,
            new ArrayList<String>()
        );

        assertThrows(SecurityException.class, () -> {
            employeeWithoutPermissions.changeVisibilityProduct(comic, false);
        });
    }

    // ==========================================
    // TESTS DE VALORACIÓN DE PRODUCTOS DE SEGUNDA MANO
    // ==========================================

    /**
     * Verifica que un empleado con permiso puede valorar productos de segunda mano.
     */
    @Test
    void testValorarProductoConPermiso() {
        employee.add_permisions(Permission.EXCH_PRODUCT_APPRAISE);

        SecondHandProduct secondHandProduct = new SecondHandProduct(
            "Test Product",
            "Second hand product",
            "img/product.jpg",
            ItemType.GAME,
            testClient
        );

        assertDoesNotThrow(() -> {
            employee.appraiseSecondHandProduct(testClient, secondHandProduct, Condition.MUY_BUENO, 150.0);
        });

        assertTrue(secondHandProduct.isAppraised());
    }

    /**
     * Verifica que un empleado sin permiso no puede valorar productos.
     */
    @Test
    void testValorarProductoSinPermiso() {
        SecondHandProduct secondHandProduct = new SecondHandProduct(
            "Test",
            "Product",
            "img/product.jpg",
            ItemType.GAME,
            testClient
        );

        assertThrows(SecurityException.class, () -> {
            employeeWithoutPermissions.appraiseSecondHandProduct(testClient, secondHandProduct, Condition.MUY_BUENO, 150.0);
        });
    }

    /**
     * Verifica que no se puede valorar un producto que no pertenece al cliente.
     */
    @Test
    void testValorarProductoAjeno() {
        employee.add_permisions(Permission.EXCH_PRODUCT_APPRAISE);

        Client otroCliente = new Client(
            "otro", "pass", "Otro", "99999999Z", "01/01/2001",
            "otro@test.com", "666999999"
        );

        SecondHandProduct secondHandProduct = new SecondHandProduct(
            "Test",
            "Product",
            "img/product.jpg",
            ItemType.GAME,
            otroCliente
        );

        assertThrows(IllegalArgumentException.class, () -> {
            employee.appraiseSecondHandProduct(testClient, secondHandProduct, Condition.MUY_BUENO, 150.0);
        });
    }

    // ==========================================
    // TESTS DE VALIDACIÓN DE INTERCAMBIOS
    // ==========================================

    /**
     * Verifica que un empleado sin permiso no puede validar intercambios.
     */
    @Test
    void testValidarIntercambioSinPermiso() {
        // Crear un intercambio dummy (para este test solo verificamos el permiso)
        assertThrows(SecurityException.class, () -> {
            employeeWithoutPermissions.validateExchange(null);
        });
    }

    /**
     * Verifica que un empleado sin permiso no puede cancelar intercambios.
     */
    @Test
    void testCancelarIntercambioSinPermiso() {
        assertThrows(SecurityException.class, () -> {
            employeeWithoutPermissions.cancelExchange(null);
        });
    }

    // ==========================================
    // TESTS DE ACTUALIZACIÓN DE ESTADO DE PEDIDOS
    // ==========================================

    /**
     * Verifica que un empleado con permiso puede actualizar estado de pedidos.
     */
    @Test
    void testActualizarEstadoPedidoConPermiso() {
        employee.add_permisions(Permission.ORDER_STATUS_UPDATE);

        // Crear un pedido dummy
        ArrayList<CartItem> items = new ArrayList<>();
	     // Crear al menos un CartItem válido
	     NewProduct testProduct = new Comic(
	         "Test Comic", "Comic", 10.0, "img/comic.jpg", 50,
	         testCategories, testReviews, null, 200, "Publisher", 2020, new ArrayList<>()
	     );
	     items.add(new CartItem(testProduct, 1));
	     Order order = new Order(testClient, items, 10.0);

        assertDoesNotThrow(() -> {
            employee.updateOrderStatus(order, OrderStatus.EN_PREPARACION);
        });
    }

    /**
     * Verifica que un empleado sin permiso no puede actualizar estado de pedidos.
     */
    @Test
    void testActualizarEstadoPedidoSinPermiso() {
        ArrayList<CartItem> items = new ArrayList<>();
        NewProduct testProduct = new Comic(
            "Test Comic", "Comic", 10.0, "img/comic.jpg", 50,
            testCategories, testReviews, null, 200, "Publisher", 2020, new ArrayList<>()
        );
        items.add(new CartItem(testProduct, 1));
        Order order = new Order(testClient, items, 10.0);
        
        assertThrows(SecurityException.class, () -> {
            employeeWithoutPermissions.updateOrderStatus(order, OrderStatus.EN_PREPARACION);
        });
    }

    // ==========================================
    // TESTS DE CASOS LÍMITE
    // ==========================================

    /**
     * Verifica que un empleado puede tener múltiples permisos.
     */
    @Test
    void testMultiplesPermisos() {
        employee.add_permisions(Permission.PRODUCT_LOAD);
        employee.add_permisions(Permission.PRODUCT_EDIT);
        employee.add_permisions(Permission.EXCH_PRODUCT_APPRAISE);
        employee.add_permisions(Permission.EXCH_VALIDATE);

        assertEquals(4, employee.permisosEmpleado().size());
    }

    /**
     * Verifica que un empleado puede tener múltiples roles.
     */
    @Test
    void testMultiplesRoles() {
        employee.add_roles(EmployeeRoles.ORDERS_EMPLOYEE);
        employee.add_roles(EmployeeRoles.EXCHANGES_EMPLOYEE);

        assertEquals(2, employee.Rol.size());
    }

    /**
     * Verifica que se puede activar y desactivar un empleado múltiples veces.
     */
    @Test
    void testAlternarEstadoEmpleado() {
        assertTrue(employee.isEnabled());
        employee.desactivateEmployee();
        assertFalse(employee.isEnabled());
        employee.activateEmployee();
        assertTrue(employee.isEnabled());
        employee.desactivateEmployee();
        assertFalse(employee.isEnabled());
    }

    /**
     * Verifica que múltiples empleados son independientes.
     */
    @Test
    void testIndependenciaEmpleados() {
        employee.add_permisions(Permission.PRODUCT_LOAD);
        employeeWithoutPermissions.add_permisions(Permission.PRODUCT_EDIT);

        assertTrue(employee.permisosEmpleado().contains(Permission.PRODUCT_LOAD));
        assertFalse(employee.permisosEmpleado().contains(Permission.PRODUCT_EDIT));

        assertFalse(employeeWithoutPermissions.permisosEmpleado().contains(Permission.PRODUCT_LOAD));
        assertTrue(employeeWithoutPermissions.permisosEmpleado().contains(Permission.PRODUCT_EDIT));
    }

    /**
     * Verifica que un empleado puede realizar múltiples operaciones seguidas.
     */
    @Test
    void testMultiplesOperacionesSeguidas() {
        // Dar permisos
        employee.add_permisions(Permission.PRODUCT_LOAD);
        employee.add_permisions(Permission.PRODUCT_EDIT);
        employee.add_permisions(Permission.EXCH_PRODUCT_APPRAISE);

        // Cambiar estado
        employee.desactivateEmployee();
        employee.activateEmployee();

        // Cargar producto
        ArrayList<String> authors = new ArrayList<>();
        Map<String, Object> comicData = new HashMap<>();
        comicData.put("name", "Comic");
        comicData.put("description", "Test");
        comicData.put("price", 10.0);
        comicData.put("picturePath", "img/comic.jpg");
        comicData.put("stock", 50);
        comicData.put("categories", testCategories);
        comicData.put("nPages", 200);
        comicData.put("publisher", "Publisher");
        comicData.put("publicationYear", 2020);
        comicData.put("writtenBy", authors);

        assertDoesNotThrow(() -> {
            employee.loadProduct(testCatalog, ItemType.COMIC, comicData);
        });

        assertEquals(1, testCatalog.allProducts().size());
        assertTrue(employee.isEnabled());
        assertTrue(employee.permisosEmpleado().size() >= 3);
    }
}