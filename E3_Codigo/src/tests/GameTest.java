package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import utils.*;
import discounts.*;
import products.*;
import products.catalog.Category;

public class GameTest {

    private ArrayList<Category> categoriasValidas;
    private ArrayList<String> mecanicas;
    private AgeRange rangoEdad;

    @BeforeEach
    void setUp() {
        // Preparamos la categoría válida para no fallar la regla de NewProduct
        Category catJuegos = new Category("Juegos de Mesa");
        categoriasValidas = new ArrayList<>();
        categoriasValidas.add(catJuegos);

        // Preparamos mecánicas de prueba
        mecanicas = new ArrayList<>();
        mecanicas.add("Tirar dados");
        mecanicas.add("Gestión de recursos");

        // Preparamos un rango de edad de prueba usando tu clase AgeRange
        rangoEdad = new AgeRange(8, 99);
    }

    @Test
    void testCrearGameValido() {
        Game juego = new Game("Catán", "Juego de comercio y construcción", 45.0, "catan.jpg", 20,
                categoriasValidas, new ArrayList<Review>(), null,
                4, mecanicas, rangoEdad);

        // Comprobamos que hereda los métodos de Item/Product
        assertEquals("Catán", juego.getName());
        assertEquals(45.0, juego.getPrice());

        // ¡Usamos tu getter específico de Game y el método equalTo de AgeRange!
        assertTrue(juego.getAgeRange().equalTo(8, 99));
    }

    @Test
    void testCrearGameJugadoresInvalidos() {
        // Tu código lanza IllegalArgumentException si nPlayers < 1
        assertThrows(IllegalArgumentException.class, () -> {
            new Game("Solitario Malo", "Juego injugable", 10.0, "solitario.jpg", 5,
                    categoriasValidas, new ArrayList<Review>(), null,
                    0, mecanicas, rangoEdad); // Le pasamos 0 jugadores, ¡debe fallar!
        });
    }

    @Test
    void testEditGameInfo() {
        Game juego = new Game("Catán", "Juego de comercio", 45.0, "catan.jpg", 20,
                categoriasValidas, new ArrayList<Review>(), null,
                4, mecanicas, rangoEdad);

        AgeRange nuevoRango = new AgeRange(10, 99);
        ArrayList<String> nuevasMecanicas = new ArrayList<>();
        nuevasMecanicas.add("Uso de cartas");

        // Verificamos que se puede editar sin problemas
        assertDoesNotThrow(() -> {
            juego.editGameInfo(null, 6, nuevasMecanicas, nuevoRango);
        });

        // Comprobamos que el rango de edad se ha actualizado correctamente tras la edición
        assertTrue(juego.getAgeRange().equalTo(10, 99));
    }
}