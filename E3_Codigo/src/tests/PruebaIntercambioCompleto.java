/**
 * Escenario de prueba avanzado para el sistema de intercambios.
 * @author Taha Ridda
 * @version 3.0
 */
package tests;
import users.*;
import catalog.*;
import transactions.*;
import utils.*;
import java.util.ArrayList;
public class PruebaIntercambioCompleto {

    public static void main(String[] args) {

            Employee admin = new Employee("Admin_01", "Marcos", "Perez", "8888", "10/10/1985", "admin@tienda.com", "600111222", 1500, true);
            admin.add_permisions(Permission.EXCH_VALIDATE);

            Client taha = new Client("Taha_Dev", "Taha", "Ridda", "1111", "19/10/2006", "taha@mail.com", "666555444");
            Client lidia = new Client("Lidia_G", "Lidia", "García", "2222", "20/10/2006", "lidia@mail.com", "777888999");
            Client ivan = new Client("Ivan_R7", "Ivan", "Rodriguez", "3333", "23/07/1990", "ivan@mail.com", "888999000");


            SecondHandProduct switchOled = new SecondHandProduct("Switch OLED", "Consola Nintendo", "img1.jpg", 320.0, true, ItemType.GAME, Condition.PERFECTO, taha);
            SecondHandProduct zelda = new SecondHandProduct("Zelda BOTW", "Juego Switch", "img2.jpg", 45.0, true, ItemType.GAME, Condition.MUY_BUENO, taha);

            SecondHandProduct ps5 = new SecondHandProduct("PS5 Slim", "Consola Sony", "img3.jpg", 450.0, true, ItemType.GAME, Condition.PERFECTO, lidia);
            SecondHandProduct eldenRing = new SecondHandProduct("Elden Ring", "Juego PS5", "img4.jpg", 50.0, true, ItemType.GAME, Condition.MUY_BUENO, lidia);

            SecondHandProduct steamDeck = new SecondHandProduct("Steam Deck", "PC Portátil", "img5.jpg", 400.0, true, ItemType.GAME, Condition.USO_LIGERO, ivan);

            System.out.println("--- ESTADO INICIAL: PRODUCTOS CREADOS Y ASIGNADOS ---");

            ArrayList<SecondHandProduct> loteTaha = new ArrayList<>();
            loteTaha.add(switchOled);
            loteTaha.add(zelda);

            System.out.println("\n[SISTEMA] Generando oferta de Taha para Lidia...");
            ExchangeOffer oferta1 = new ExchangeOffer(ps5, loteTaha, taha);

            oferta1.imprimir();

            System.out.println("[INTERACCIÓN] Lidia acepta la oferta de Taha.");
            oferta1.aceptaroferta();

            Exchange exch1 = new Exchange(oferta1);
            exch1.validateExchange(admin);
                System.out.println("[✔] Intercambio 1 VALIDADO por el empleado " + admin.getUsername());
                System.out.println("Nuevo dueño de PS5: " + ps5.getOwner().getUsername()); // Debería ser Taha
                System.out.println("Nuevo dueño de Switch: " + switchOled.getOwner().getUsername()); // Debería ser Lidia


            System.out.println("\n" + "=".repeat(64) + "\n");

            ArrayList<SecondHandProduct> loteIvan = new ArrayList<>();
            loteIvan.add(steamDeck);

            System.out.println("[SISTEMA] Generando oferta de Ivan para Lidia...");
            ExchangeOffer oferta2 = new ExchangeOffer(switchOled, loteIvan, ivan);

            oferta2.imprimir();

            System.out.println("[INTERACCIÓN] Lidia RECHAZA la oferta de Ivan.");
            oferta2.reject_offer();

            System.out.println("Estado final Oferta 2: " + oferta2.getEstado());
            System.out.println("¿Steam Deck sigue disponible para Ivan?: " + steamDeck.isAvailable());

            System.out.println("\n--- FIN DE LA PRUEBA ---");
        }
    }



