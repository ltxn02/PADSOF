package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import products.*;

import java.util.ArrayList;

import users.*;
import utils.*;

public class SecondHandProductTest {

    private Client owner;
    private Employee appraiser;

    @BeforeEach
    void setUp() {
        owner = new Client("juan123", "pass", "Juan", "12345678D", "10/05/2004", "juan@gmail.com", "256847855");
        appraiser = new Employee("emp01", "pass", "marcos", "21458725D",  "02/02/2005","admin@shop.com", "254781144", 525,true);
        appraiser.permissions.add(Permission.EXCH_PRODUCT_APPRAISE);
    }

    @Test
    void testConstructorYRestricciones() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SecondHandProduct("Pack Consola", "Lote", "img.png", 100.0, false, ItemType.PACK, Condition.MUY_BUENO, owner);
        });

        SecondHandProduct producto = new SecondHandProduct("GameBoy", "Clásica", "gb.png", ItemType.GAME, owner);
        assertNotNull(producto);
        assertFalse(producto.isAppraised());
        assertEquals(owner, producto.getOwner());
    }

    @Test
    void testProcesoDeTasacion() {
        SecondHandProduct producto = new SecondHandProduct("Zelda NES", "Cartucho solo", "zelda.png", ItemType.COMIC, owner);

        double valorTasado = 45.0;
        producto.appraiseSecondHand(appraiser, Condition.PERFECTO, valorTasado);

        assertTrue(producto.isAppraised());
        assertEquals(valorTasado, producto.getPrice(), 0.01);
        assertEquals(appraiser, producto.getAppraiser());
        assertEquals(Condition.PERFECTO, producto.getCondition());
    }

    @Test
    void testDisponibilidadYBloqueo() {
        SecondHandProduct producto = new SecondHandProduct("PS2", "Usada", "ps2.png", ItemType.GAME, owner);

        assertTrue(producto.isAvailable());

        producto.change_offered_status(true);
        assertFalse(producto.isAvailable());
    }
}