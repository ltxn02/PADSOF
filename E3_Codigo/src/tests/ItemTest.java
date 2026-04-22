package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import products.*;
import products.catalog.Category;

import java.time.Duration;
import java.util.ArrayList;

public class ItemTest {

    private Category categoriaPrueba;

    /**
     * CLASE DE PRUEBA (MOCK)
     * Como Item es 'abstract', creamos esta clase auxiliar solo para poder
     * instanciarla y probar la lógica interna que has programado en Item.java.
     */
    class TestItem extends Item {
        public TestItem(String name, String description, double price, String picturePath, ArrayList<Category> categories) {
            super(name, description, price, picturePath, categories);
        }

        public TestItem(String name, String description, double price, String picturePath) {
            super(name, description, price, picturePath);
        }

        // Exponemos los métodos protected para poder testearlos desde fuera
        public void testRegisterTime() { super.registerTime(); }
        public void testClearInstants() { super.clearInstants(); }
        public void testEdit(String n, String d, double p, String img) { super.edit(n, d, p, img); }
        public void testSetPrice(double p) { super.setPrice(p); }
    }

    @BeforeEach
    void setUp() {
        categoriaPrueba = new Category("Categoría Test");
    }

    @Test
    void testConstructorYGettersBasicos() {
        TestItem item = new TestItem("Espada Maestra", "Espada del héroe", 500.0, "espada.jpg");

        assertEquals("Espada Maestra", item.getName());
        assertEquals("Espada del héroe", item.getDescription());
        assertEquals(500.0, item.getPrice());
        assertTrue(item.getCategories().isEmpty());
    }

    @Test
    void testPrecioNegativoLanzaExcepcion() {
        // Tu código en la línea 32 de Item.java protege contra precios negativos
        assertThrows(IllegalArgumentException.class, () -> {
            new TestItem("Pocion", "Cura 50PV", -10.0, "pocion.jpg");
        });
    }

    @Test
    void testAddCategory() {
        TestItem item = new TestItem("Escudo", "Escudo de madera", 15.0, "escudo.jpg");

        // Añadimos la categoría la primera vez (debe funcionar)
        assertDoesNotThrow(() -> {
            item.addCategory(categoriaPrueba);
        });
        assertEquals(1, item.getCategories().size());

        // Añadimos la MISMA categoría una segunda vez (debe lanzar excepción por la línea 127)
        assertThrows(IllegalArgumentException.class, () -> {
            item.addCategory(categoriaPrueba);
        });
    }

    @Test
    void testIsNamedFlexible() {
        TestItem item = new TestItem("   Gema Roja   ", "Brilla mucho", 100.0, "gema.jpg");

        // Tu método isNamed ignora mayúsculas y espacios sobrantes (línea 174)
        assertTrue(item.isNamed("gema ROJA"));
        assertTrue(item.isNamed("   Gema Roja   "));
        assertFalse(item.isNamed("Gema Azul"));
    }

    @Test
    void testEdicionSegura() {
        TestItem item = new TestItem("Arco", "Arco simple", 30.0, "arco.jpg");

        // Editamos pasando un null en el nombre y un precio negativo (tu método debe ignorarlos)
        item.testEdit(null, "Arco reforzado", -5.0, null);

        assertEquals("Arco", item.getName()); // El nombre no debió cambiar
        assertEquals("Arco reforzado", item.getDescription()); // La descripción sí
        assertEquals(30.0, item.getPrice()); // El precio no debió cambiar por ser negativo

        // Probamos setPrice directo
        item.testSetPrice(40.0);
        assertEquals(40.0, item.getPrice());
    }

    @Test
    void testTemporizadores() throws InterruptedException {
        TestItem item = new TestItem("Reloj", "Mide el tiempo", 5.0, "reloj.jpg");

        item.testRegisterTime();

        // Pausamos el test 50 milisegundos para forzar que el tiempo pase
        Thread.sleep(50);

        // Comprobamos si el item ha caducado si le damos una duración de 10 milisegundos
        // (Debería devolver true porque ya han pasado 50)
        assertTrue(item.isExpired(Duration.ofMillis(10)));

        // Probamos que el clearInstants funciona y vacía la variable
        item.testClearInstants();
        assertThrows(NullPointerException.class, () -> {
            item.isExpired(Duration.ofMillis(10)); // Al ser null la fecha, lanzar isExpired da error
        });
    }
}