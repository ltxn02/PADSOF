package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import catalog.*;
import users.*;
import transactions.*;
import utils.*;

public class ExchangeOfferTest {

    private Client ofertante;
    private Client receptor;
    private SecondHandProduct productoPedido;
    private SecondHandProduct productoOfrecido1;
    private SecondHandProduct productoOfrecido2;
    private ExchangeOffer oferta;

    @BeforeEach
    void setUp() {
        ofertante = new Client("user_ofer", "pass", "Ana Ofertante", "11111111A", "01/01/1990", "ana@mail.com", "600111111");
        receptor = new Client("user_recep", "pass", "Luis Receptor", "22222222B", "02/02/1990", "luis@mail.com", "600222222");

        productoPedido = new SecondHandProduct("Zelda N64", "Clásico", "img1.png", ItemType.GAME, receptor);
        productoOfrecido1 = new SecondHandProduct("Mario Kart", "Usado", "img2.png", ItemType.GAME, ofertante);
        productoOfrecido2 = new SecondHandProduct("Donkey Kong", "Buen estado", "img3.png", ItemType.GAME, ofertante);

        ArrayList<SecondHandProduct> ofrecidos = new ArrayList<>();
        ofrecidos.add(productoOfrecido1);
        ofrecidos.add(productoOfrecido2);

        oferta = new ExchangeOffer(productoPedido, ofrecidos, ofertante);
    }

    @Test
    void testEstadoInicialYBloqueoDeProductos() {
        assertEquals(ExchangeOfferStatus.PENDIENTE, oferta.getEstado());
        assertFalse(productoOfrecido1.isAvailable());
        assertFalse(productoOfrecido2.isAvailable());
        assertTrue(productoPedido.isAvailable());
        assertEquals(ofertante, oferta.getOfferor());
        assertEquals(receptor, oferta.getReceptor());
    }

    @Test
    void testCancelOffer() {
        assertDoesNotThrow(() -> {
            oferta.cancelOffer();
        });

        assertEquals(ExchangeOfferStatus.CANCELADA, oferta.getEstado());
        assertTrue(productoOfrecido1.isAvailable());
        assertTrue(productoOfrecido2.isAvailable());

        assertThrows(IllegalStateException.class, () -> {
            oferta.cancelOffer();
        });
    }

    @Test
    void testRejectOffer() {
        oferta.reject_offer();

        assertEquals(ExchangeOfferStatus.RECHAZADA, oferta.getEstado());
        assertTrue(productoOfrecido1.isAvailable());
        assertTrue(productoOfrecido2.isAvailable());
    }

    @Test
    void testAceptarOferta() {
        oferta.aceptaroferta();

        assertTrue(oferta.ofertaaceptada());
        assertEquals(ExchangeOfferStatus.ACEPTADA, oferta.getEstado());
    }

    @Test
    void testIntercambiarPropietarios() {
        oferta.aceptaroferta();
        oferta.intercambiar_propietarios();

        assertEquals(ofertante, productoPedido.getOwner());
        assertEquals(receptor, productoOfrecido1.getOwner());
        assertEquals(receptor, productoOfrecido2.getOwner());

        assertTrue(productoPedido.isAvailable());
        assertTrue(productoOfrecido1.isAvailable());
        assertTrue(productoOfrecido2.isAvailable());
    }

    @Test
    void testIsRequestedProduct() {
        assertTrue(oferta.isRequestedProduct(productoPedido));
        assertFalse(oferta.isRequestedProduct(productoOfrecido1));
    }

    @Test
    void testExpiredOfferNoExpiraInmediatamente() {
        assertFalse(oferta.is_Expired());

        oferta.expired_offer();
        assertEquals(ExchangeOfferStatus.PENDIENTE, oferta.getEstado());
    }
}