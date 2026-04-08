package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import catalog.*;
import users.*;
import transactions.*;
import utils.*;

public class ExchangeTest {

    private Client ofertante;
    private Client receptor;
    private Employee empleadoValidador;

    private SecondHandProduct productoPedido;
    private SecondHandProduct productoOfrecido;
    private ExchangeOffer oferta;

    @BeforeEach
    void setUp() {
        ofertante = new Client("user_ofer", "pass1", "Ana Ofertante", "11111111A", "01/01/1990", "ana@mail.com", "600111111");
        receptor = new Client("user_recep", "pass2", "Luis Receptor", "22222222B", "02/02/1990", "luis@mail.com", "600222222");
        empleadoValidador = new Employee("emp_val", "pass3", "Empleado Validador", "33333333C", "03/03/1980", "emp@mail.com", "600333333", 1500.0, true);

        productoPedido = new SecondHandProduct("Zelda N64", "Clásico", "img1.png", ItemType.GAME, receptor);
        productoOfrecido = new SecondHandProduct("Mario Kart", "Como nuevo", "img2.png", ItemType.GAME, ofertante);

        ArrayList<SecondHandProduct> ofrecidos = new ArrayList<>();
        ofrecidos.add(productoOfrecido);

        oferta = new ExchangeOffer(productoPedido, ofrecidos, ofertante);
    }

    @Test
    void testConstructorFallaSiOfertaNoAceptada() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Exchange(oferta);
        }, "Debe lanzar excepción si se intenta iniciar un Exchange con una oferta PENDIENTE.");
    }

    @Test
    void testConstructorExitoSiOfertaAceptada() {
        oferta.aceptaroferta();

        Exchange exchange = new Exchange(oferta);

        assertNotNull(exchange);
        assertTrue(exchange.isExchange(ExchangeStatus.EN_PROCESO), "El estado inicial del intercambio debe ser EN_PROCESO.");
        assertTrue(exchange.isThisExchangeOffer(oferta), "El intercambio debe estar vinculado a la oferta original.");
    }

    @Test
    void testValidateExchangeCambiaEstadosYPropietarios() {
        oferta.aceptaroferta();
        Exchange exchange = new Exchange(oferta);

        assertDoesNotThrow(() -> {
            exchange.validateExchange(empleadoValidador);
        });

        assertTrue(exchange.isExchange(ExchangeStatus.COMPLETADO), "El estado debe cambiar a COMPLETADO tras la validación.");

        assertEquals(ofertante, productoPedido.getOwner(), "El dueño del producto solicitado debe ser ahora el ofertante.");
        assertEquals(receptor, productoOfrecido.getOwner(), "El dueño del producto ofrecido debe ser ahora el receptor.");
    }

    @Test
    void testCancelExchangeLiberaProductos() {
        oferta.aceptaroferta();
        Exchange exchange = new Exchange(oferta);

        assertDoesNotThrow(() -> {
            exchange.cancelExchange(empleadoValidador);
        });

        assertTrue(exchange.isExchange(ExchangeStatus.CANCELADO), "El estado debe cambiar a CANCELADO.");

        assertEquals(receptor, productoPedido.getOwner(), "El dueño original no debe cambiar tras una cancelación.");
        assertTrue(productoOfrecido.isAvailable(), "El producto ofrecido debe ser desbloqueado y volver a estar disponible.");
    }

    @Test
    void testCancelExchangeFallaSiNoEstaEnProceso() {
        oferta.aceptaroferta();
        Exchange exchange = new Exchange(oferta);

        exchange.validateExchange(empleadoValidador);

        assertThrows(IllegalStateException.class, () -> {
            exchange.cancelExchange(empleadoValidador);
        }, "No se debe permitir cancelar un intercambio que ya ha sido COMPLETADO.");
    }
}