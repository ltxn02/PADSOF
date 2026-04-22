package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import utils.*;
import discounts.*;
import products.*;
import products.catalog.Category;

public class ComicTest {

    private ArrayList<Category> categoriasValidas;
    private ArrayList<String> autores;

    @BeforeEach
    void setUp() {
        // Igual que en CategoryTest, preparamos una categoría válida
        // para no incumplir la regla de "Category cannot be empty" de NewProduct
        Category catComic = new Category("Cómics y Manga");
        categoriasValidas = new ArrayList<>();
        categoriasValidas.add(catComic);

        // Preparamos una lista de autores de prueba
        autores = new ArrayList<>();
        autores.add("Stan Lee");
        autores.add("Steve Ditko");
    }

    @Test
    void testCrearComicValido() {
        Comic comic = new Comic("Spiderman", "Origen de Spiderman", 15.50, "spidey.jpg", 10,
                categoriasValidas, new ArrayList<Review>(), null,
                30, "Marvel", 1962, autores);

        // Comprobamos que hereda bien los métodos de la clase Item/Product
        assertEquals("Spiderman", comic.getName());
        assertEquals(15.50, comic.getPrice());
        assertNull(comic.getDiscount());
    }

    @Test
    void testCrearComicPaginasInvalidas() {
        assertDoesNotThrow(() -> {
            new Comic("Batman", "Año Uno", 20.0, "batman.jpg", 5,
                    categoriasValidas, new ArrayList<Review>(), null,
                    -5, "DC Comics", 1987, autores);
        });
    }

    @Test
    void testCrearComicStockNegativo() {
        // El stock negativo (-10) debe hacer saltar la validación de la clase padre (NewProduct)
        assertThrows(IllegalArgumentException.class, () -> {
            new Comic("Iron Man", "El demonio en una botella", 18.0, "iron.jpg", -10,
                    categoriasValidas, new ArrayList<Review>(), null,
                    120, "Marvel", 1979, autores);
        });
    }

    @Test
    void testEditComicInfo() {
        Comic comic = new Comic("X-Men", "Días del futuro pasado", 25.0, "xmen.jpg", 15,
                categoriasValidas, new ArrayList<Review>(), null,
                150, "Marvel", 1981, autores);

        // Probamos a editarlo. Pasamos un número de páginas inválido (0)
        // para asegurarnos de que el metodo no falla al hacer la autocorrección.
        assertDoesNotThrow(() -> {
            comic.editComicInfo(null, 0, "Panini Comics", 2024, autores);
        });

        // Verificamos que el descuento sigue siendo el esperado (null en este caso) tras la edición
        assertNull(comic.getDiscount());
    }
}