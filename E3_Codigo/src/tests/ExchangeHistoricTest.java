package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import products.*;

import java.util.ArrayList;

import users.*;
import transactions.*;
import utils.ItemType;

public class ExchangeHistoricTest {

    private Exchange intercambio1;
    private Exchange intercambio2;
    private ExchangeHistoric historial;

    @BeforeEach
    void setUp() {
        Client c1 = new Client("cliente1", "pass", "Juan", "11111111A", "01/01/1990", "a@mail.com", "600111111");
        Client c2 = new Client("cliente2", "pass", "Luis", "22222222B", "01/01/1990", "b@mail.com", "600222222");

        SecondHandProduct p1 = new SecondHandProduct("Juego 1", "Desc", "img.png", ItemType.GAME, c1);
        SecondHandProduct p2 = new SecondHandProduct("Juego 2", "Desc", "img.png", ItemType.GAME, c2);

        ArrayList<SecondHandProduct> ofrecidos1 = new ArrayList<>();
        ofrecidos1.add(p1);
        ExchangeOffer oferta1 = new ExchangeOffer(p2, ofrecidos1, c1);
        oferta1.aceptaroferta();
        intercambio1 = new Exchange(oferta1);

        SecondHandProduct p3 = new SecondHandProduct("Juego 3", "Desc", "img.png", ItemType.GAME, c1);
        SecondHandProduct p4 = new SecondHandProduct("Juego 4", "Desc", "img.png", ItemType.GAME, c2);

        ArrayList<SecondHandProduct> ofrecidos2 = new ArrayList<>();
        ofrecidos2.add(p3);
        ExchangeOffer oferta2 = new ExchangeOffer(p4, ofrecidos2, c1);
        oferta2.aceptaroferta();
        intercambio2 = new Exchange(oferta2);

        historial = new ExchangeHistoric();
    }

    @Test
    void testConstructorPorDefecto() {
        assertNotNull(historial.getExchanges());
        assertTrue(historial.getExchanges().isEmpty());
    }

    @Test
    void testConstructorConParametros() {
        ArrayList<Exchange> listaInicial = new ArrayList<>();
        listaInicial.add(intercambio1);

        ExchangeHistoric historialConLista = new ExchangeHistoric(listaInicial);

        assertEquals(1, historialConLista.getExchanges().size());
        assertTrue(historialConLista.hasExchange(intercambio1));
    }

    @Test
    void testAddYHasExchange() {
        assertFalse(historial.hasExchange(intercambio1));

        historial.addExchange(intercambio1);

        assertTrue(historial.hasExchange(intercambio1));
        assertEquals(1, historial.getExchanges().size());

        historial.addExchange(intercambio2);

        assertTrue(historial.hasExchange(intercambio2));
        assertEquals(2, historial.getExchanges().size());
    }

    @Test
    void testRemoveExchange() {
        historial.addExchange(intercambio1);
        historial.addExchange(intercambio2);

        historial.removeExchange(intercambio1);

        assertFalse(historial.hasExchange(intercambio1));
        assertTrue(historial.hasExchange(intercambio2));
        assertEquals(1, historial.getExchanges().size());
    }

    @Test
    void testSetExchanges() {
        ArrayList<Exchange> nuevaLista = new ArrayList<>();
        nuevaLista.add(intercambio1);
        nuevaLista.add(intercambio2);

        historial.setExchanges(nuevaLista);

        assertEquals(2, historial.getExchanges().size());
        assertTrue(historial.hasExchange(intercambio1));
        assertTrue(historial.hasExchange(intercambio2));
    }
}