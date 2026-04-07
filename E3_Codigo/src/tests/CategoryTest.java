package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import catalog.*;
import utils.*;

public class CategoryTest {

    private Comic comic1;
    private Comic comic2;

    @BeforeEach
    void setUp() {
        // 1. Creamos una categoría de prueba para cumplir tu regla de negocio
        Category catPrueba = new Category("TestCategory");
        ArrayList<Category> categorias = new ArrayList<>();
        categorias.add(catPrueba);

        // 2. Ahora sí, le pasamos la lista con 1 categoría a los cómics
        comic1 = new Comic("Spiderman", "Cómic de Spiderman", 10.0, "img.jpg", 5,
                categorias, new ArrayList<Review>(), null,
                100, "Marvel", 2000, new ArrayList<String>());

        comic2 = new Comic("Batman", "Cómic de Batman", 12.0, "img2.jpg", 3,
                categorias, new ArrayList<Review>(), null,
                120, "DC", 2005, new ArrayList<String>());
    }

    @Test
    void testCrearCategoriaVacia() {
        Category cat = new Category("Manga");
        assertEquals("Manga", cat.getNameCategory());
    }

    @Test
    void testCrearCategoriaConLista() {
        ArrayList<Item> listaItems = new ArrayList<>();
        listaItems.add(comic1);
        listaItems.add(comic2);

        Category cat = new Category("Superhéroes", listaItems);
        assertEquals("Superhéroes", cat.getNameCategory());
    }

    @Test
    void testAddItemNuevo() {
        Category cat = new Category("Cómics");
        assertDoesNotThrow(() -> {
            cat.addItem(comic1);
        });
    }

    @Test
    void testAddItemDuplicado() {
        Category cat = new Category("Cómics");
        cat.addItem(comic1);

        assertThrows(IllegalArgumentException.class, () -> {
            cat.addItem(comic1);
        });
    }

    @Test
    void testRenameCategoria() {
        Category cat = new Category("Ficción");
        cat.rename("Ciencia Ficción");
        assertEquals("Ciencia Ficción", cat.getNameCategory());
    }
}