package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import logic.SistemaRecomendacionesSegundamano;
import catalog.*;
import users.*;
import utils.*;

/**
 * Clase de prueba para validar el motor de recomendaciones de segunda mano.
 * Verifica que el sistema filtra correctamente los productos propios/no disponibles,
 * y que pondera adecuadamente los productos por afinidad de categoría y
 * similitud de intereses (filtrado colaborativo).
 */
public class SistemaRecomendacionesSegundamanoTest {

    private Client ana;
    private Client luis;
    private Client pedro;
    private ArrayList<Client> todosLosClientes;
    private ArrayList<SecondHandProduct> catalogo;

    private Category catRetro;
    private Category catModerno;

    /**
     * Clase auxiliar (Mock) para poder inyectar categorías fácilmente en los
     * productos de segunda mano durante los tests, ya que heredan de Item.
     */
    class TestSHProduct extends SecondHandProduct {
        private ArrayList<Category> categorias = new ArrayList<>();

        public TestSHProduct(String name, ItemType type, Client owner, Category cat) {
            super(name, "Descripción de prueba", "img.png", type, owner);
            this.categorias.add(cat);
        }

        @Override
        public ArrayList<Category> getCategories() {
            return this.categorias;
        }
    }

    @BeforeEach
    void setUp() {
        catalogo = new ArrayList<>();
        todosLosClientes = new ArrayList<>();

        catRetro = new Category("Juegos Retro");
        catModerno = new Category("Juegos Modernos");

        ana = new Client("ana_retro", "pass1", "Ana García", "11111111A", "01/01/1990", "ana@mail.com", "600111111");
        luis = new Client("luis_retro", "pass2", "Luis López", "22222222B", "02/02/1992", "luis@mail.com", "600222222");
        pedro = new Client("pedro_mix", "pass3", "Pedro Pérez", "33333333C", "03/03/1993", "pedro@mail.com", "600333333");

        todosLosClientes.add(ana);
        todosLosClientes.add(luis);
        todosLosClientes.add(pedro);
    }

    @Test
    void testNoRecomiendaProductosPropiosNiNoDisponibles() {
        TestSHProduct juegoAna = new TestSHProduct("Tetris GB", ItemType.GAME, ana, catRetro);
        catalogo.add(juegoAna);

        ArrayList<SecondHandProduct> recsAna = SistemaRecomendacionesSegundamano.obtenerRecomendaciones(ana, catalogo, todosLosClientes);

        assertFalse(recsAna.contains(juegoAna), "El motor no debe recomendar productos que ya pertenecen al cliente.");

        TestSHProduct juegoPedro = new TestSHProduct("FIFA 23", ItemType.GAME, pedro, catModerno);
        catalogo.add(juegoPedro);

        assertDoesNotThrow(() -> {
            ana.makeOffer(juegoPedro, juegoAna);
        });

        assertFalse(juegoAna.isAvailable());

        ArrayList<SecondHandProduct> recsLuis = SistemaRecomendacionesSegundamano.obtenerRecomendaciones(luis, catalogo, todosLosClientes);

        assertFalse(recsLuis.contains(juegoAna), "El motor no debe recomendar productos que no estén disponibles (en oferta).");
    }

    @Test
    void testPrioridadPorSimilitudDeUsuarios() {
        TestSHProduct retroPedro1 = new TestSHProduct("Sonic Genesis", ItemType.GAME, pedro, catRetro);
        TestSHProduct retroPedro2 = new TestSHProduct("Metroid NES", ItemType.GAME, pedro, catRetro);

        TestSHProduct basuraAna = new TestSHProduct("Fifa 08", ItemType.GAME, ana, catModerno);
        TestSHProduct basuraLuis = new TestSHProduct("Fifa 09", ItemType.GAME, luis, catModerno);

        TestSHProduct joyaDeLuis = new TestSHProduct("Castlevania", ItemType.GAME, luis, catRetro);

        catalogo.add(retroPedro1); catalogo.add(retroPedro2); catalogo.add(joyaDeLuis);
        catalogo.add(basuraAna); catalogo.add(basuraLuis);

        assertDoesNotThrow(() -> {
            ana.makeOffer(retroPedro1, basuraAna);
            luis.makeOffer(retroPedro2, basuraLuis);
        });

        ArrayList<SecondHandProduct> recs = SistemaRecomendacionesSegundamano.obtenerRecomendaciones(ana, catalogo, todosLosClientes);

        assertTrue(recs.contains(joyaDeLuis));
        assertEquals(joyaDeLuis, recs.get(0), "El filtrado colaborativo debe aupar los productos de usuarios con perfiles similares.");
    }

    @Test
    void testColdStartNuevoUsuario() {
        Client usuarioNuevo = new Client("nuevo_user", "pass", "Nuevo Cliente", "99999999Z", "01/01/2000", "nuevo@mail.com", "600999999");

        TestSHProduct juegoEnVenta = new TestSHProduct("Halo", ItemType.GAME, pedro, catModerno);
        catalogo.add(juegoEnVenta);

        assertDoesNotThrow(() -> {
            ArrayList<SecondHandProduct> recs = SistemaRecomendacionesSegundamano.obtenerRecomendaciones(usuarioNuevo, catalogo, todosLosClientes);

            assertTrue(recs.contains(juegoEnVenta), "Debe devolver el catálogo disponible aunque el usuario no tenga historial.");
        });
    }
}