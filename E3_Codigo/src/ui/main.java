package ui;
import utils.*;
import users.*;
import catalog.*;
import transactions.*;
import logic.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import discounts.*;

/**
 * Clase principal que actúa como punto de entrada (Entry Point) a la aplicación Rongero.
 * Gestiona el bucle inicial de ejecución, la carga y guardado de datos persistentes,
 * y el enrutamiento de los usuarios hacia sus respectivos menús de interfaz según
 * su rol (Gestor, Empleado o Cliente).
 *
 * @author Iván Sánchez
 * @version 3.0
 */
public class main {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Metodo principal que arranca la ejecución del demostrador del sistema.
     * Carga automáticamente los datos guardados en disco y muestra el menú
     * inicial para usuarios invitados. Al salir, asegura el guardado del estado.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("      BIENVENIDO A RONGERO           ");
        System.out.println("=====================================");

        logic.Application.cargarDatos("rongero_data.dat");

        boolean salirApp = false;

        while (!salirApp) {
            System.out.println("\n--- MENÚ INICIAL (INVITADO) ---");
            System.out.println("1.- Ver catálogo de productos");
            System.out.println("2.- Login");
            System.out.println("3.- Registrarse");
            System.out.println("0.- Salir de la aplicación");
            System.out.print("Elige una opción (0-3): ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    verCatalogolnvitado();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    register();
                    break;
                case "0":
                    salirApp = true;
                    System.out.println("Guardando datos y cerrando la aplicación... ¡Hasta pronto!");
                    // --- Guarda los datos antes de salir ---
                    logic.Application.guardarDatos("rongero_data.dat");
                    break;
                default:
                    System.out.println("[!] Opción no válida. Por favor, introduce un número del 0 al 3.");
            }
        }
        scanner.close();
        ShoppingCart.shutdownCleaner();
    }

    /**
     * Muestra una vista previa del catálogo de la tienda para los usuarios
     * que navegan como invitados (sin haber iniciado sesión).
     */
    private static void verCatalogolnvitado() {
        System.out.println("\n--- CATÁLOGO DE PRODUCTOS ---");

        ArrayList<NewProduct> productos = Application.getCatalog();

        if (productos.isEmpty()) {
            System.out.println("Actualmente no hay productos en la tienda.");
            return;
        }

        for (NewProduct p : productos) {
            System.out.println("- " + p.getName() + " | Precio: " + p.getPrice() + "€");
        }
    }

    /**
     * Procesa el inicio de sesión de un usuario.
     * Solicita las credenciales, valida contra la lógica de negocio y, en caso
     * de éxito, transfiere el control de la consola a la clase de menú específica
     * correspondiente al rol del usuario logueado.
     */
    private static void login() {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        System.out.print("Username: ");
        String identificador = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            RegisteredUser user = Application.login(identificador, password);

            // Redirigir al menú correcto según el tipo de usuario logueado
            if (user instanceof Manager) {
                System.out.println(">> ¡Bienvenido, Gestor " + user.getUsername() + "!");
                MenuGestor.mostrarMenu((Manager) user, scanner);
            } else if (user instanceof Client) {
                System.out.println(">> ¡Bienvenido, Cliente " + user.getUsername() + "!");
                MenuCliente.mostrarMenu((Client) user, scanner);
            } else if (user instanceof Employee) {
                System.out.println(">> ¡Bienvenido, Empleado " + user.getUsername() + "!");
                MenuEmpleado.mostrarMenu((Employee) user, scanner);
            }

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Guía al usuario invitado a través del proceso de creación de una nueva cuenta
     * de Cliente en el sistema. Incluye una prueba técnica de generación de la primera
     * notificación de bienvenida en la bandeja de entrada del usuario.
     */
    private static void register() {
        System.out.println("\n--- REGISTRO DE NUEVO CLIENTE ---");

        System.out.print("Full name: ");
        String fullname = scanner.nextLine();
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Birthdate: ");
        String birthdate = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone number: ");
        String phoneNumber = scanner.nextLine();

        Client nuevoCliente = new Client(username, password, fullname, dni, birthdate, email, phoneNumber);

        try {
            Application.registerClient(nuevoCliente);
            System.out.println(">> Usuario " + username + " registrado correctamente. Ya puedes iniciar sesión.");

            // --- CÓDIGO DE PRUEBA DE NOTIFICACIONES ---
            ArrayList<RegisteredUser> destinatarios = new ArrayList<>();
            destinatarios.add(nuevoCliente);

            String mensajeLargo = "¡Bienvenido a Rongero! Esperamos que disfrutes comprando y vendiendo tus productos frikis de segunda mano. Esto es texto extra para ver cómo se ve un mensaje largo.Esto es texto extra para ver cómo se ve un mensaje largo.Esto es texto extra para ver cómo se ve un mensaje largo.Esto es texto extra para ver cómo se ve un mensaje largo.";
            Notification bienvenida = new Notification(mensajeLargo, destinatarios);

            nuevoCliente.addNotification(bienvenida);
            // --- FIN CÓDIGO DE PRUEBA ---

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}