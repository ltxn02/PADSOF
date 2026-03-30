
import java.time.Duration;
import java.util.ArrayList;

public class test_intercambios {

    public static void main(String[] args) {
        // --- 1. REGISTRO DE USUARIOS ---
        Client taha = new Client("Asus", "Asus", "Taha", "1234l", "19/10/2006", "taha@gmail.com", "12345");
        Client lidia = new Client("Barça10", "Lidia", "Lidia", "54321d", "20/10/2006", "lidia@gmail.com", "52463");
        Client ana = new Client("AnaStar", "Ana", "García", "1111a", "01/01/2000", "ana@gmail.com", "66655");
        Client carlos = new Client("Carlitros", "Carlos", "Pérez", "2222c", "05/05/1995", "carlos@gmail.com", "44433");
        Client martin = new Client("Martín99", "Martín", "Ruiz", "3333m", "12/12/1999", "martin@gmail.com", "22211");

        // --- 2. PERSONAL DE LA TIENDA ---
        Employee ivan = new Employee("R7_BOSS", "Ivan", "Ivan", "73736", "23/07/1965", "ivan@gmail.com", "8888", 0, true);
        Employee andres = new Employee("Andy_New", "Andres", "Andres", "9999", "10/10/1990", "andres@mail.com", "7777", 0, true);

        // Configuración de permisos: Ivan es veterano, Andres es nuevo (sin permisos aún)
        ivan.add_permisions(Permission.EXCH_VALIDATE);

        // --- 3. INVENTARIO DE PRODUCTOS ---
        SecondHandProduct ps5 = new SecondHandProduct(1, "PS5", "Sony", "f1", 450, true, ItemType.GAME, Condition.PERFECTO, taha);
        SecondHandProduct switchOled = new SecondHandProduct(2, "Switch", "Nint", "f2", 300, true, ItemType.GAME, Condition.MUY_BUENO, lidia);
        SecondHandProduct iphone = new SecondHandProduct(3, "iPhone 13", "Apple", "f3", 600, true, ItemType.GAME, Condition.MUY_BUENO, ana);
        SecondHandProduct portatil = new SecondHandProduct(4, "HP Victus", "HP", "f4", 700, true, ItemType.FIGURINE, Condition.PERFECTO, carlos);
        SecondHandProduct cascos = new SecondHandProduct(5, "Sony WH", "Audio", "f5", 150, true, ItemType.GAME, Condition.DAÑADO, martin);

        // --- ESCENARIO A: EL INTERCAMBIO TRIPLE (Ana ofrece iPhone + Cascos por el Portátil de Carlos) ---
        System.out.println("--- ESCENARIO A: Intercambio múltiple ---");
        ArrayList<SecondHandProduct> loteAna = new ArrayList<>();
        loteAna.add(iphone);
        loteAna.add(cascos); // Martín le vendió los cascos a Ana previamente (simulado)

        Exchangeoffer ofertaAna = new Exchangeoffer(101, portatil, loteAna, ana, carlos, Duration.ofDays(3));
        System.out.println("¿Productos de Ana bloqueados?: " + (!iphone.estádisponible() && !cascos.estádisponible()));

        Exchange exchA = new Exchange(501, ofertaAna);
        if (exchA.validateExchange(ivan)) {
            System.out.println("RESULTADO: Carlos ahora tiene: " + iphone + " y " + cascos);
            System.out.println("RESULTADO: Ana ahora tiene: " + portatil);
        }

        System.out.println("\n--------------------------------------------------\n");

        // --- ESCENARIO B: SEGURIDAD Y PERMISOS (Andres intenta validar sin permiso) ---
        System.out.println("--- ESCENARIO B: Seguridad (Andres vs Taha/Lidia) ---");
        ArrayList<SecondHandProduct> loteTaha = new ArrayList<>();
        loteTaha.add(ps5);
        Exchangeoffer ofertaTaha = new Exchangeoffer(102, switchOled, loteTaha, taha, lidia, Duration.ofDays(1));

        Exchange exchB = new Exchange(502, ofertaTaha);
        boolean intentoAndres = exchB.validateExchange(andres);
        System.out.println("¿Andres pudo validar?: " + (intentoAndres ? "SÍ (FALLO DE SEGURIDAD)" : "NO (SISTEMA SEGURO)"));
        System.out.println("Estado de la oferta tras intento fallido: " + ofertaTaha);

        System.out.println("\n--------------------------------------------------\n");

        // --- ESCENARIO C: RECHAZO Y LIBERACIÓN (Lidia rechaza a Taha) ---
        System.out.println("--- ESCENARIO C: Rechazo manual ---");
        System.out.println("Antes de rechazar, ¿PS5 bloqueada?: " + (!ps5.estádisponible()));
        ofertaTaha.reject_offer();
        System.out.println("Tras rechazo, ¿PS5 disponible?: " + ps5.estádisponible());
        System.out.println("Estado final oferta Taha: " + ofertaTaha);

        System.out.println("\n--------------------------------------------------\n");

        // --- ESCENARIO D: LA CARRERA CONTRA EL TIEMPO (Expiración) ---
        System.out.println("--- ESCENARIO D: Expiración de oferta ---");
        Exchangeoffer ofertaCaduca = new Exchangeoffer(103, ps5, new ArrayList<>(), martin, taha, Duration.ZERO);

        // Esperamos un milisegundo para asegurar que el tiempo pase
        try { Thread.sleep(5); } catch (Exception e) {}

        if (ofertaCaduca.is_Expired()) {
            ofertaCaduca.expired_offer();
            System.out.println("La oferta de Martín ha expirado correctamente.");
            System.out.println("Estado oferta: " + ofertaCaduca);
            System.out.println("¿PS5 liberada para otros?: " + ps5.estádisponible()); // Usando tu método de oferta
        }

        System.out.println("\n--- TEST FINALIZADO CON ÉXITO ---");
    }
}
