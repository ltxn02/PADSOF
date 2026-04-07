package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import catalog.*;
import utils.*;
import discounts.*;

public class FigurineTest {

    private ArrayList<Category> categoriasValidas;

    @BeforeEach
    void setUp() {
        // Preparamos una categoría válida para evitar el error "Category cannot be empty"
        Category catFiguras = new Category("Figuras de colección");
        categoriasValidas = new ArrayList<>();
        categoriasValidas.add(catFiguras);
    }

    @Test
    void testCrearFigurineValida() {
        Figurine figura = new Figurine("Goku Super Saiyan", "Figura de alta calidad", 59.99, "goku.jpg", 15,
                categoriasValidas, new ArrayList<Review>(), null,
                25.5, 10.0, 8.5, "PVC", "Dragon Ball Z");

        // Comprobamos que se crea correctamente y hereda los atributos básicos
        assertEquals("Goku Super Saiyan", figura.getName());
        assertEquals(59.99, figura.getPrice());
        assertNull(figura.getDiscount());
    }

    @Test
    void testCrearFigurineDimensionesInvalidas_Altura() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Figurine("Vegeta", "Figura estropeada", 45.0, "vegeta.jpg", 5,
                    categoriasValidas, new ArrayList<Review>(), null,
                    -10.0, 5.0, 5.0, "Resina", "Dragon Ball Z");
        });
    }

    @Test
    void testCrearFigurineDimensionesInvalidas_Anchura() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Figurine("Luffy", "Figura", 30.0, "luffy.jpg", 10,
                    categoriasValidas, new ArrayList<Review>(), null,
                    15.0, -2.0, 5.0, "Plástico", "One Piece");
        });
    }

    @Test
    void testEditFigurineInfo() {
        Figurine figura = new Figurine("Iron Man", "Figura articulada", 80.0, "ironman.jpg", 20,
                categoriasValidas, new ArrayList<Review>(), null,
                30.0, 12.0, 10.0, "Metal", "Marvel");

        // Nos aseguramos de que el metodo de edición se ejecuta sin lanzar errores
        assertDoesNotThrow(() -> {
            figura.editFigurineInfo(null, 32.0, 15.0, 10.0, "Metal/PVC", "Marvel");
        });
    }
}