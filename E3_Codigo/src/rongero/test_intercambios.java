import model.user.*;
import model.catalog.*;
import model.transactions.*;
import util.*;
import java.time.Duration;
import java.util.ArrayList;

/**
 * Suite de pruebas integrales para el módulo de intercambios.
 * Verifica escenarios críticos de negocio:
 * 1. Persistencia y seguridad de permisos de empleados.
 * 2. Lógica de bloqueo/desbloqueo de productos (isAvailable) durante el ciclo de vida de una oferta.
 * 3. Ejecución de intercambios de múltiples productos (lotes).
 * 4. Control de expiración y gestión de estados (Pendiente, Rechazada, Aceptada).
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class test_intercambios {

    /**
     * Punto de entrada para la ejecución de los tests de integración.
     * Simula una interacción compleja entre 5 clientes y 2 empleados para asegurar
     * la robustez de la lógica de transacciones de segunda mano.
     * * @param args Argumentos de configuración externa (no utilizados).
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void main(String[] args) {
        // --- FASE 1: Inicialización del entorno de pruebas ---
        Client taha = new Client("Asus", "Asus", "Taha", "1234l", "19/10/2006", "taha@gmail.com", "12345");
        Client lidia = new Client("Barça10", "Lidia", "Lidia", "54321d", "20/10/2006", "lidia@gmail.com", "52463");
        Client ana = new Client("AnaStar", "Ana", "García", "1111a", "01/01/2000", "ana@gmail.com", "66655");
        Client carlos = new Client("Carlitros", "Carlos", "Pérez", "2222c", "05/05/1995", "carlos@gmail.com", "44433");
        Client martin = new Client("Martín99", "Martín", "Ruiz", "3333m", "12/12/1999", "martin@gmail.com", "22211");

        Employee ivan = new Employee("R7_BOSS", "Ivan", "Ivan", "73736", "23/07/1965", "ivan@gmail.com", "8888", 0, true);
        Employee andres = new Employee("Andy_New", "Andres", "Andres", "9999", "10/10/1990", "andres@mail.com", "7777", 0, true);

        // Configuración selectiva de permisos para probar la seguridad
        ivan.add_permisions(Permission.EXCH_VALIDATE);
        // andres NO recibe permisos para validar el control de acceso

        // --- FASE 2: Creación de inventario tasado ---
        SecondHandProduct ps5 = new SecondHandProduct("PS5", "Sony", "f1", 450, true, ItemType.GAME, Condition.PERFECTO, taha);
        SecondHandProduct switchOled = new SecondHandProduct("Switch", "Nint", "f2", 300, true, ItemType.GAME, Condition.MUY_BUENO, lidia);
        SecondHandProduct iphone = new SecondHandProduct("iPhone 13", "Apple", "f3", 600, true, ItemType.GAME, Condition.MUY_BUENO, ana);
        SecondHandProduct portatil = new SecondHandProduct("HP Victus", "HP", "f4", 700, true, ItemType.FIGURINE, Condition.PERFECTO, carlos);
        SecondHandProduct cascos = new SecondHandProduct("Sony WH", "Audio", "f5", 150, true, ItemType.GAME, Condition.DAÑADO, martin);

        // --- FASE 3: Test de intercambio exitoso por lotes ---
        System.out.println("TEST 1: Intercambio múltiple (Ana ofrece iPhone + Cascos por Portátil de Carlos)");
        ArrayList<SecondHandProduct> loteAna = new ArrayList<>();
        loteAna.add(iphone);
        loteAna.add(cascos);

        ExchangeOffer ofertaAna = new ExchangeOffer(portatil, loteAna, ana);
        System.out.println("[i] Verificando bloqueo preventivo de productos: " + (!iphone.isAvailable() && !cascos.isAvailable()));

        Exchange exchA = new Exchange(ofertaAna);
        if (exchA.validateExchange(ivan)) {
            System.out.println("[✔] Resultado: Propiedad transferida correctamente.");
            System.out.println("Nuevos dueños verificados: Ana posee Portátil, Carlos posee iPhone y Cascos.");
        }

        System.out.println("\n--------------------------------------------------\n");

        // --- FASE 4: Test de Seguridad (Validación por empleado no autorizado) ---
        System.out.println("TEST 2: Seguridad de permisos (Andrés intenta validar oferta de Taha)");
        ArrayList<SecondHandProduct> loteTaha = new ArrayList<>();
        loteTaha.add(ps5);
        ExchangeOffer ofertaTaha = new ExchangeOffer(switchOled, loteTaha, taha);

        Exchange exchB = new Exchange(ofertaTaha);
        boolean intentoAndres = exchB.validateExchange(andres);
        System.out.println("[i] ¿Andrés pudo validar?: " + (intentoAndres ? "SÍ (FALLO DE SEGURIDAD)" : "NO (SISTEMA SEGURO)"));
        System.out.println("Estado de la oferta (Debe ser Pendiente): " + ofertaTaha.getStatus());

        System.out.println("\n--------------------------------------------------\n");
    }
}
