package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import users.*;
import catalog.*;
import utils.*;

/**
 * Clase de pruebas unitarias para la clase {@link Manager}.
 * 
 * Verifica el funcionamiento correcto de:
 * <ul>
 *   <li>Creación de cuentas de manager</li>
 *   <li>Creación y gestión de empleados</li>
 *   <li>Cambio de estado de empleados (activar/desactivar)</li>
 *   <li>Asignación y revocación de permisos</li>
 *   <li>Creación y gestión de categorías</li>
 *   <li>Visibilidad de categorías</li>
 *   <li>Creación de packs promocionales</li>
 * </ul>
 * 
 * @version 1.0
 */
public class ManagerTest {

    private Manager manager;
    private Category testCategory;
    private ArrayList<Category> testCategories;
    private ArrayList<Review> testReviews;
    private ArrayList<NewProduct> testProducts;

    @BeforeEach
    void setUp() {
        // 1. Crear un manager de prueba
        manager = new Manager(
            "manager_test",
            "password123",
            "Test Manager",
            "12345678A",
            "15/03/1990",
            "manager@test.com",
            "666123456",
            50000.0
        );

        // 2. Crear categorías de prueba
        testCategory = new Category("Test Category");
        testCategories = new ArrayList<>();
        testCategories.add(testCategory);

        // 3. Crear lista de reseñas y productos para packs
        testReviews = new ArrayList<>();
        testProducts = new ArrayList<>();

        // Crear productos de prueba para packs
        Comic comic = new Comic(
            "Test Comic",
            "Comic de prueba",
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

        Game game = new Game(
            "Test Game",
            "Juego de prueba",
            45.0,
            "img/game.jpg",
            50,
            testCategories,
            testReviews,
            null,
            4,
            new ArrayList<String>(),
            new AgeRange(10, 99)
        );

        testProducts.add(comic);
        testProducts.add(game);
    }

    // ==========================================
    // TESTS DE INICIALIZACIÓN
    // ==========================================

    /**
     * Verifica que se puede crear un manager correctamente.
     */
    @Test
    void testCrearManager() {
        assertNotNull(manager);
        assertEquals("manager_test", manager.getUsername());
    }

    /**
     * Verifica que el manager hereda de Staff.
     */
    @Test
    void testManagerHeredaDeStaff() {
        assertTrue(manager instanceof Staff);
        assertTrue(manager instanceof RegisteredUser);
        assertTrue(manager instanceof User);
    }

    // ==========================================
    // TESTS DE CREACIÓN DE EMPLEADOS
    // ==========================================

    /**
     * Verifica que un manager puede crear una cuenta de empleado.
     */
    @Test
    void testCrearEmpleado() {
        Employee employee = manager.createEmployeeAccount(
            "employee_test",
            "pass123",
            "Test Employee",
            "87654321B",
            "20/05/1995",
            "employee@test.com",
            "666654321",
            30000.0,
            true
        );

        assertNotNull(employee);
        assertEquals("employee_test", employee.getUsername());
    }

    /**
     * Verifica que se puede crear un empleado desactivado.
     */
    @Test
    void testCrearEmpleadoDesactivado() {
        Employee employee = manager.createEmployeeAccount(
            "inactive_employee",
            "pass123",
            "Inactive Employee",
            "11111111C",
            "10/10/1992",
            "inactive@test.com",
            "666111111",
            25000.0,
            false
        );

        assertNotNull(employee);
        assertFalse(employee.isEnabled());
    }

    /**
     * Verifica que se puede crear un empleado activado.
     */
    @Test
    void testCrearEmpleadoActivado() {
        Employee employee = manager.createEmployeeAccount(
            "active_employee",
            "pass123",
            "Active Employee",
            "22222222D",
            "12/08/1993",
            "active@test.com",
            "666222222",
            28000.0,
            true
        );

        assertNotNull(employee);
        assertTrue(employee.isEnabled());
    }

    // ==========================================
    // TESTS DE CAMBIO DE ESTADO DE EMPLEADOS
    // ==========================================

    /**
     * Verifica que un manager puede activar un empleado desactivado.
     */
    @Test
    void testActivarEmpleado() {
        Employee employee = manager.createEmployeeAccount(
            "employee1", "pass", "Employee 1", "33333333E", "01/01/1991",
            "emp1@test.com", "666333333", 30000.0, false
        );

        assertFalse(employee.isEnabled());
        manager.changeEmployeeStatus(employee, true);
        assertTrue(employee.isEnabled());
    }

    /**
     * Verifica que un manager puede desactivar un empleado activado.
     */
    @Test
    void testDesactivarEmpleado() {
        Employee employee = manager.createEmployeeAccount(
            "employee2", "pass", "Employee 2", "44444444F", "02/02/1992",
            "emp2@test.com", "666444444", 30000.0, true
        );

        assertTrue(employee.isEnabled());
        manager.changeEmployeeStatus(employee, false);
        assertFalse(employee.isEnabled());
    }

    /**
     * Verifica que changeEmployeeStatus lanza excepción con empleado nulo.
     */
    @Test
    void testChangeEmployeeStatusConNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.changeEmployeeStatus(null, true);
        });
    }

    // ==========================================
    // TESTS DE PERMISOS
    // ==========================================

    /**
     * Verifica que un manager puede asignar permisos a un empleado.
     */
    @Test
    void testAnadirPermiso() {
        Employee employee = manager.createEmployeeAccount(
            "employee3", "pass", "Employee 3", "55555555G", "03/03/1993",
            "emp3@test.com", "666555555", 30000.0, true
        );

        Permission permiso = Permission.EXCH_VALIDATE;
        assertDoesNotThrow(() -> {
            manager.addPermissionTo(employee, permiso);
        });
        assertTrue(employee.permisosEmpleado().contains(permiso));
    }

    /**
     * Verifica que un manager puede revocar permisos de un empleado.
     */
    @Test
    void testRevocarPermiso() {
        Employee employee = manager.createEmployeeAccount(
            "employee4", "pass", "Employee 4", "66666666H", "04/04/1994",
            "emp4@test.com", "666666666", 30000.0, true
        );

        Permission permiso = Permission.EXCH_VALIDATE;
        manager.addPermissionTo(employee, permiso);
        assertTrue(employee.permisosEmpleado().contains(permiso));
        
        manager.removePermissionTo(employee, permiso);
        assertFalse(employee.permisosEmpleado().contains(permiso));
    }

    /**
     * Verifica que addPermissionTo lanza excepción con empleado nulo.
     */
    @Test
    void testAddPermissionConNull() {
        Permission permiso = Permission.EXCH_VALIDATE;
        assertThrows(IllegalArgumentException.class, () -> {
            manager.addPermissionTo(null, permiso);
        });
    }

    /**
     * Verifica que removePermissionTo lanza excepción con empleado nulo.
     */
    @Test
    void testRemovePermissionConNull() {
        Permission permiso = Permission.EXCH_VALIDATE;
        assertThrows(IllegalArgumentException.class, () -> {
            manager.removePermissionTo(null, permiso);
        });
    }

    // ==========================================
    // TESTS DE CATEGORÍAS
    // ==========================================

    /**
     * Verifica que un manager puede crear una categoría.
     */
    @Test
    void testCrearCategoria() {
        Category newCategory = manager.createCategory("Nueva Categoría");
        
        assertNotNull(newCategory);
        assertEquals("Nueva Categoría", newCategory.getNameCategory());
    }

    /**
     * Verifica que createCategory lanza excepción con nombre nulo.
     */
    @Test
    void testCrearCategoriaConNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.createCategory(null);
        });
    }

    /**
     * Verifica que createCategory lanza excepción con nombre vacío.
     */
    @Test
    void testCrearCategoriaConVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.createCategory("");
        });
    }

    /**
     * Verifica que un manager puede renombrar una categoría.
     */
    @Test
    void testRenombrarCategoria() {
        Category categoria = manager.createCategory("Viejo Nombre");
        assertEquals("Viejo Nombre", categoria.getNameCategory());
        
        manager.renameCategory(categoria, "Nuevo Nombre");
        assertEquals("Nuevo Nombre", categoria.getNameCategory());
    }

    /**
     * Verifica que renameCategory lanza excepción con nombre nulo.
     */
    @Test
    void testRenombrarCategoriaConNull() {
        Category categoria = manager.createCategory("Test");
        assertThrows(IllegalArgumentException.class, () -> {
            manager.renameCategory(categoria, null);
        });
    }

    /**
     * Verifica que renameCategory lanza excepción con categoría nula.
     */
    @Test
    void testRenombrarCategoriaConNullCategory() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.renameCategory(null, "Nuevo Nombre");
        });
    }

    /**
     * Verifica que un manager puede añadir items a una categoría.
     */
    @Test
    void testAnadirItemACategoria() {
        Category categoria = manager.createCategory("Cómics");
        Comic comic = new Comic(
            "Test Comic 2",
            "Otro comic",
            15.0,
            "img/comic2.jpg",
            50,
            testCategories,
            testReviews,
            null,
            150,
            "Editorial",
            2021,
            new ArrayList<String>()
        );

        assertDoesNotThrow(() -> {
            manager.addItemToCategory(categoria, comic);
        });
    }

    /**
     * Verifica que un manager puede cambiar la visibilidad de una categoría.
     */
    @Test
    void testCambiarVisibilidadCategoria() {
        Category categoria = manager.createCategory("Visible Category");
        
        assertDoesNotThrow(() -> {
            manager.changeVisibilityCategory(categoria, false);
            manager.changeVisibilityCategory(categoria, true);
        });
    }

    /**
     * Verifica que changeVisibilityCategory lanza excepción con categoría nula.
     */
    @Test
    void testCambiarVisibilidadConNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            manager.changeVisibilityCategory(null, true);
        });
    }

    // ==========================================
    // TESTS DE PACKS
    // ==========================================

    /**
     * Verifica que un manager puede crear un pack.
     */
    @Test
    void testCrearPack() {
        Pack pack = manager.createPack(
            "Test Pack",
            "Pack de prueba",
            50.0,
            "img/pack.jpg",
            20,
            testCategories,
            testReviews,
            testProducts
        );

        assertNotNull(pack);
        assertEquals("Test Pack", pack.getName());
        assertEquals(50.0, pack.getPrice());
    }

    /**
     * Verifica que createPack requiere al menos 2 productos.
     */
    @Test
    void testCrearPackConUnProducto() {
        ArrayList<NewProduct> unProducto = new ArrayList<>();
        unProducto.add(testProducts.get(0));

        assertThrows(IllegalArgumentException.class, () -> {
            manager.createPack(
                "Invalid Pack",
                "Solo un producto",
                20.0,
                "img/pack.jpg",
                10,
                testCategories,
                testReviews,
                unProducto
            );
        });
    }

    /**
     * Verifica que se puede crear un pack con múltiples productos.
     */
    @Test
    void testCrearPackConMultiplesProductos() {
        Pack pack = manager.createPack(
            "Multi Pack",
            "Pack con múltiples productos",
            80.0,
            "img/multipack.jpg",
            15,
            testCategories,
            testReviews,
            testProducts
        );

        assertNotNull(pack);
        assertTrue(pack.getPrice() > 0);
    }

    // ==========================================
    // TESTS DE CASOS LÍMITE
    // ==========================================

    /**
     * Verifica que un manager puede realizar múltiples operaciones seguidas.
     */
    @Test
    void testMultiplesOperacionesSeguidas() {
        // Crear empleado
        Employee emp = manager.createEmployeeAccount(
            "emp_multi", "pass", "Multi Employee", "77777777I", "05/05/1995",
            "emmulti@test.com", "666777777", 30000.0, true
        );

        // Cambiar estado
        manager.changeEmployeeStatus(emp, false);

        // Crear categoría
        Category cat = manager.createCategory("Multi Category");

        // Renombrar
        manager.renameCategory(cat, "Renamed Multi Category");

        // Crear pack
        Pack pack = manager.createPack(
            "Multi Pack",
            "Pack múltiple",
            75.0,
            "img/multipack.jpg",
            10,
            testCategories,
            testReviews,
            testProducts
        );

        // Verificar que todo funcionó
        assertFalse(emp.isEnabled());
        assertEquals("Renamed Multi Category", cat.getNameCategory());
        assertNotNull(pack);
    }

    /**
     * Verifica que un manager puede crear múltiples empleados.
     */
    @Test
    void testCrearMultiplesEmpleados() {
        ArrayList<Employee> empleados = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Employee emp = manager.createEmployeeAccount(
                "emp" + i,
                "pass" + i,
                "Employee " + i,
                (10000000 + i) + "A",
                "01/01/199" + i,
                "emp" + i + "@test.com",
                "66600000" + i,
                25000.0 + (i * 1000),
                i % 2 == 0
            );
            empleados.add(emp);
        }

        assertEquals(5, empleados.size());
    }

    /**
     * Verifica que un manager puede crear múltiples categorías.
     */
    @Test
    void testCrearMultiplesCategorias() {
        ArrayList<Category> categories = new ArrayList<>();

        String[] categoryNames = {"Cómics", "Juegos", "Figuras", "Libros", "Pósters"};
        for (String name : categoryNames) {
            Category cat = manager.createCategory(name);
            categories.add(cat);
        }

        assertEquals(5, categories.size());
        assertEquals("Cómics", categories.get(0).getNameCategory());
    }
}