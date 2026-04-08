package tests;

import static org.junit.jupiter.api.Assertions.*;

import logic.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import logic.StoreStatistics;
import catalog.*;
import users.*;
import transactions.*;
import utils.*;

public class StoreStatisticsTest {

    private Client clienteTop;
    private Client clienteNormal;
    private Employee empleadoEstrella;
    private Product pConsola;
    private SecondHandProduct shpTasado;

    private String topName;
    private String normName;
    private String empName;

    private static int counter = 0;

    class TestCartItem extends CartItem {
        public TestCartItem(NewProduct product, int quantity) {
            super(product, quantity);
        }
    }

    class TestProduct extends Product {
        public TestProduct(String name, double price, ArrayList<Category> categorias) {
            super(name, "Desc", price, "img.png", 100, categorias, new ArrayList<>(), null);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        counter++;
        topName = "top_buyer_" + counter;
        normName = "normal_buyer_" + counter;
        empName = "emp_tasador_" + counter;

        String dniTop = String.format("%08d", 11111110 + counter) + "A";
        String dniNorm = String.format("%08d", 22222220 + counter) + "B";
        String dniEmp = String.format("%08d", 33333330 + counter) + "C";

        clienteTop = new Client(topName, "pass", "Comprador Compulsivo", dniTop, "01/01/1990", "top@mail.com", "600111111");
        clienteNormal = new Client(normName, "pass", "Comprador Normal", dniNorm, "01/01/1990", "norm@mail.com", "600222222");
        empleadoEstrella = new Employee(empName, "pass", "Tasador Experto", dniEmp, "01/01/1980", "emp@mail.com", "600333333", 1500.0, true);

        empleadoEstrella.permissions.add(Permission.EXCH_PRODUCT_APPRAISE);

        Category categoriaTest = new Category("Electrónica");
        ArrayList<Category> listaCategorias = new ArrayList<>();
        listaCategorias.add(categoriaTest);

        pConsola = new TestProduct("Nintendo Switch", 300.0, listaCategorias);
        shpTasado = new SecondHandProduct("Zelda Antiguo", "Viejo", "img.png", ItemType.GAME, clienteNormal);

        List<CartItem> itemsTop = new ArrayList<>();
        itemsTop.add(new TestCartItem(pConsola, 2));
        Order ordenTop = new Order(clienteTop, itemsTop, 600.0);
        clienteTop.getOrderHistoric().addOrder(ordenTop);

        List<CartItem> itemsNormal = new ArrayList<>();
        itemsNormal.add(new TestCartItem(pConsola, 1));
        Order ordenNormal = new Order(clienteNormal, itemsNormal, 300.0);
        clienteNormal.getOrderHistoric().addOrder(ordenNormal);

        shpTasado.appraiseSecondHand(empleadoEstrella, Condition.PERFECTO, 45.0);

        Application.registerClient(clienteTop);
        Application.registerClient(clienteNormal);
        Application.registerEmployee(empleadoEstrella);
        Application.addSecondHandProduct(shpTasado);
    }

    @Test
    void testGeneracionDeReporteConDatos() {
        String reporte = StoreStatistics.generateStoreReport();

        assertNotNull(reporte);
        assertTrue(reporte.contains("ESTADÍSTICAS AVANZADAS"));

        assertTrue(reporte.contains(topName));
        assertTrue(reporte.contains(normName));
        assertTrue(reporte.contains("Nintendo Switch"));
        assertTrue(reporte.contains(empName));
    }

    @Test
    void testCalculoDeIntercambiosYVentasEvitaErroresConVacios() throws Exception {
        String fantasmaName = "fantasma_" + counter;
        String dniFantasma = String.format("%08d", 99999990 + counter) + "Z";

        Client clienteFantasma = new Client(fantasmaName, "pass", "Sin Compras", dniFantasma, "01/01/2000", "f@mail.com", "600999999");

        Application.registerClient(clienteFantasma);

        assertDoesNotThrow(() -> {
            String reporte = StoreStatistics.generateStoreReport();
            assertNotNull(reporte);
        });
    }
}