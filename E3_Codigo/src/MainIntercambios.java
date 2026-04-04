import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.time.format.DateTimeFormatter;


public class MainIntercambios {

    public static void main(String[] args) {

            Client taha = new Client("Asus", "Asus", "Taha", "1234l",   "19/10/2006","taha@gmail.com", "123456789");
            Client lidia = new Client("BarçaLover10", "Lidia", "Lidia", "54321d",   "20/10/2006","lidia@gmail.com", "52463584");
            Employee ivan = new Employee("R7_FOREVER", "Ivan", "Ivan",  "73736", "23/07/1965", "ivan@gmail.com", "8494859584", 0, true);

            ivan.add_permisions(Permission.EXCH_VALIDATE);

            SecondHandProduct ps5 = new SecondHandProduct( "PS5", "PLAY", "FOTO", 250, true, ItemType.GAME, Condition.MUY_BUENO, taha);
            SecondHandProduct xbox = new SecondHandProduct( "Xbox", "microsoft", "foto2", 300.0, true, ItemType.GAME, Condition.PERFECTO, lidia);

            ArrayList<SecondHandProduct> ofrecidos = new ArrayList<>();
            ofrecidos.add(ps5);

            System.out.println("Creando oferta: taha ofrece PS5 a lidia por su Xbox...");
            ExchangeOffer oferta = new ExchangeOffer( xbox, ofrecidos, taha, lidia, Duration.ofDays(2));

            System.out.println("\nEmpleado ivan valida el intercambio...");
            Exchange intercambio = new Exchange(oferta);
            oferta.aceptaroferta();
            boolean exito = intercambio.validateExchange(ivan);

            if (exito) {
                System.out.println("Nuevo dueño de Xbox: " + xbox);
                System.out.println("Nuevo dueño de PS5: " + ps5);
                System.out.print("Productos liberados: " );
                if (oferta.isAllAvailable()){
                    System.out.println("Si");
                } else{
                    System.out.println("No");
                }
                System.out.println(oferta);
            } else {
                System.out.println("Error: La validación falló.");
            }}}
