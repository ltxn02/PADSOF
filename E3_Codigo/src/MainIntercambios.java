import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.time.format.DateTimeFormatter;


// --- CLASE PRINCIPAL DE PRUEBA ---
public class MainIntercambios {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TEST DE INTERCAMBIOS ===\n");

            // 1. Configuración del escenario
            Client taha = new Client("Asus", "Asus", "Taha", "1234l",   "19/10/2006","taha@gmail.com", "123456789");
            Client lidia = new Client("BarçaLover10", "Lidia", "Lidia", "54321d",   "20/10/2006","lidia@gmail.com", "52463584");
            Employee ivan = new Employee("R7_FOREVER", "Ivan", "Ivan",  "73736", "23/07/1965", "ivan@gmail.com", "8494859584", 0, true);

            ivan.add_permisions(Permission.EXCH_VALIDATE);

            SecondHandProduct ps5 = new SecondHandProduct(1, "PS5", "PLAY", "FOTO", 250, true, ItemType.GAME, Condition.MUY_BUENO, taha);
            SecondHandProduct xbox = new SecondHandProduct(2, "Xbox", "microsoft", "foto2", 300.0, true, ItemType.GAME, Condition.PERFECTO, lidia);

            // Crear la Oferta
            ArrayList<SecondHandProduct> ofrecidos = new ArrayList<>();
            ofrecidos.add(ps5);

            System.out.println("Creando oferta: taha ofrece PS5 a lidia por su Xbox...");
            Exchangeoffer oferta = new Exchangeoffer(101, xbox, ofrecidos, taha, lidia, Duration.ofDays(2));

            // 3. Validar Intercambio
            System.out.println("\nEmpleado ivan valida el intercambio...");
            Exchange intercambio = new Exchange(500, oferta);
            boolean exito = intercambio.validateExchange(ivan);

            // 4. Resultados
            if (exito) {
                System.out.println("¡Validación exitosa!");
                System.out.println("Nuevo dueño de Xbox: " + xbox.owner);
                System.out.println("Nuevo dueño de PS5: " + ps5.owner);
                System.out.println("¿Productos liberados?: " + (!ps5.isOffered && !xbox.isOffered));
                System.out.println("Estado final: " + oferta.Estado);
            } else {
                System.out.println("Error: La validación falló.");
            }}}
