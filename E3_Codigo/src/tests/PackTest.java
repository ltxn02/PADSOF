package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import catalog.*;
import utils.*;

public class PackTest {

    private Comic comic1;
    private Game juego1;
    private Figurine figura1;
    private ArrayList<Category> categoriasValidas;

    @BeforeEach
    void setUp() {
        Category catPacks = new Category("Packs Especiales");
        categoriasValidas = new ArrayList<>();
        categoriasValidas.add(catPacks);

        // Instanciamos productos de prueba para meterlos en el pack
        comic1 = new Comic("Spiderman", "Cómic", 10.0, "img.jpg", 5,
                categoriasValidas, new ArrayList<Review>(), null,
                100, "Marvel", 2000, new ArrayList<String>());

        juego1 = new Game("Monopoly", "Juego", 20.0, "img.jpg", 5,
                categoriasValidas, new ArrayList<Review>(), null,
                4, new ArrayList<String>(), new AgeRange(8, 99));

        figura1 = new Figurine("Batman", "Figura", 15.0, "img.jpg", 5,
                categoriasValidas, new ArrayList<Review>(), null,
                15.0, 5.0, 5.0, "PVC", "DC");
    }

    @Test
    void testCrearPackValido() {
        ArrayList<NewProduct> items = new ArrayList<>();
        items.add(comic1);
        items.add(juego1);

        Pack miPack = new Pack("Pack Diversión", "Un pack", 25.0, "pack.jpg", 10,
                categoriasValidas, new ArrayList<Review>(), items);

        assertEquals(2, miPack.getProducts().size());
        assertEquals("Pack Diversión", miPack.getName());
    }

    @Test
    void testCrearPackInvalido_MenosDeDosProductos() {
        ArrayList<NewProduct> itemsIncompletos = new ArrayList<>();
        itemsIncompletos.add(comic1);

        assertThrows(IllegalArgumentException.class, () -> {
            new Pack("Pack Fallido", "No funciona", 10.0, "pack.jpg", 10,
                    categoriasValidas, new ArrayList<Review>(), itemsIncompletos);
        });
    }

    @Test
    void testAddItem() {
        ArrayList<NewProduct> items = new ArrayList<>();
        items.add(comic1);
        items.add(juego1);
        Pack miPack = new Pack("Pack Diversión", "Un pack", 25.0, "pack.jpg", 10,
                categoriasValidas, new ArrayList<Review>(), items);

        boolean añadido = miPack.addItem(figura1);
        assertTrue(añadido);
        assertEquals(3, miPack.getProducts().size());

        boolean añadidoDuplicado = miPack.addItem(figura1);
        assertFalse(añadidoDuplicado);
    }

    @Test
    void testRemoveItem_ProhibidoPorMultiplicidad() {
        ArrayList<NewProduct> items = new ArrayList<>();
        items.add(comic1);
        items.add(juego1);
        Pack miPack = new Pack("Pack Diversión", "Un pack", 25.0, "pack.jpg", 10,
                categoriasValidas, new ArrayList<Review>(), items);

        boolean borrado = miPack.removeItem(comic1);

        assertFalse(borrado);
        assertEquals(2, miPack.getProducts().size());
    }

    @Test
    void testRemoveItem_Permitido() {
        ArrayList<NewProduct> items = new ArrayList<>();
        items.add(comic1);
        items.add(juego1);
        items.add(figura1); // Aquí tenemos 3 productos
        Pack miPack = new Pack("Pack Diversión", "Un pack", 25.0, "pack.jpg", 10,
                categoriasValidas, new ArrayList<Review>(), items);

        boolean borrado = miPack.removeItem(comic1);

        assertTrue(borrado);
        assertEquals(2, miPack.getProducts().size());
    }

    @Test
    void testEditPackInfo() {
        ArrayList<NewProduct> itemsIniciales = new ArrayList<>();
        itemsIniciales.add(comic1);
        itemsIniciales.add(juego1);
        Pack miPack = new Pack("Pack Diversión", "Un pack", 25.0, "pack.jpg", 10,
                categoriasValidas, new ArrayList<Review>(), itemsIniciales);

        // Preparamos la nueva lista
        ArrayList<NewProduct> itemsNuevos = new ArrayList<>();
        itemsNuevos.add(juego1);
        itemsNuevos.add(figura1); // Sustituimos a Spiderman por Batman

        miPack.editPackInfo(itemsNuevos);

        assertEquals(2, miPack.getProducts().size());
        assertTrue(miPack.getProducts().contains(figura1));
        assertFalse(miPack.getProducts().contains(comic1));
    }
}