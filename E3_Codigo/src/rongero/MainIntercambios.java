import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.time.format.DateTimeFormatter;

import model.catalog.SecondHandProduct;
import model.transactions.ExchangeOffer;
import model.user.Client;
import model.user.Employee;
import util.*;
import model.transactions.*;

/**
 * Clase principal de pruebas para el módulo de intercambios.
 * Simula un flujo completo de negocio: creación de usuarios, asignación de permisos,
 * publicación de artículos de segunda mano, propuesta de oferta y validación
 * administrativa de la transacción.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class MainIntercambios {

    /**
     * Método principal que ejecuta la simulación de un intercambio.
     * Realiza las siguientes etapas:
     * 1. Instanciación de Clientes y Empleados.
     * 2. Configuración de seguridad (Permisos de validación).
     * 3. Creación de productos tasados y disponibles.
     * 4. Generación de una ExchangeOffer y su posterior aceptación.
     * 5. Validación formal mediante la clase Exchange para ejecutar el cambio de dueños.
     * * @param args Argumentos de línea de comandos (no utilizados).
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void main(String[] args) {

        // Inicialización de actores del sistema
        Client taha = new Client("Asus", "Asus", "Taha", "1234l", "19/10/2006", "taha@gmail.com", "123456789");
        Client lidia = new Client("BarçaLover10", "Lidia", "Lidia", "54321d", "20/10/2006", "lidia@gmail.com", "52463584");
        Employee ivan = new Employee("R7_FOREVER", "Ivan", "Ivan", "73736", "23/07/1965", "ivan@gmail.com", "8494859584", 0, true);

        // Configuración de permisos: Ivan debe poder validar intercambios
        ivan.add_permisions(Permission.EXCH_VALIDATE);

        // Creación de inventario de segunda mano (tasados y marcados como disponibles)
        SecondHandProduct ps5 = new SecondHandProduct("PS5", "PLAY", "FOTO", 250, true, ItemType.GAME, Condition.MUY_BUENO, taha);
        SecondHandProduct xbox = new SecondHandProduct("Xbox", "microsoft", "foto2", 300.0, true, ItemType.GAME, Condition.PERFECTO, lidia);

        // Preparación del lote ofrecido para el trueque
        ArrayList<SecondHandProduct> ofrecidos = new ArrayList<>();
        ofrecidos.add(ps5);

        System.out.println("--- INICIANDO SIMULACIÓN DE INTERCAMBIO ---");
        System.out.println("Propuesta: " + taha.getUsername() + " ofrece PS5 a " + lidia.getUsername() + " por su Xbox...");

        // Creación de la oferta técnica
        ExchangeOffer oferta = new ExchangeOffer(xbox, ofrecidos, taha);

        System.out.println("\n[Proceso] Lidia acepta la oferta y el empleado Ivan inicia la validación...");

        // Creación del registro de intercambio y ejecución de la lógica de validación
        Exchange intercambio = new Exchange(oferta);
        oferta.aceptarOferta();

        boolean exito = intercambio.validateExchange(ivan);

        /**
         * Verificación de resultados.
         * Si la validación es exitosa, los punteros de 'owner' en los productos
         * deben haber rotado entre los clientes.
         */
        if (exito) {
            System.out.println("[✔] ¡Intercambio completado con éxito!");
            System.out.println("Nuevo dueño de Xbox: " + xbox.getOwner().getUsername());
            System.out.println("Nuevo dueño de PS5: " + ps5.getOwner().getUsername());

            System.out.print("Estado de disponibilidad de productos: ");
            if (oferta.isAllAvailable()){
                System.out.println("Liberados (Disponibles para nuevos procesos)");
            } else {
                System.out.println("Bloqueados (En proceso o ya intercambiados)");
            }

            System.out.println("\nRESUMEN FINAL:");
            System.out.println(oferta);
        } else {
            System.out.println("[X] Error: La validación administrativa ha fallado.");
        }
    }
}