package ui;
import utils.*;
import users.*;
import catalog.*;
import transactions.*;
import logic.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import discounts.*;

public class main {
    private static Scanner scanner = new Scanner(System.in);

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

    // --- MÉTODOS DEL MENÚ INICIAL ---
    private static void verCatalogolnvitado() {
        System.out.println("\n--- CATÁLOGO DE PRODUCTOS ---");

        // Obtenemos la lista de productos desde Application
        ArrayList<NewProduct> productos = Application.getCatalog();

        if (productos.isEmpty()) {
            System.out.println("Actualmente no hay productos en la tienda.");
            return;
        }

        // Recorremos la lista e imprimimos cada producto
        for (NewProduct p : productos) {
            System.out.println("- " + p.getName() + " | Precio: " + p.getPrice() + "€");
        }
    }

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
            // 1. Creamos la lista de destinatarios (el constructor de Notification lo pide)
            ArrayList<RegisteredUser> destinatarios = new ArrayList<>();
            destinatarios.add(nuevoCliente);

            // 2. Creamos el mensaje largo para probar que el preview de tu compañera funciona (corta a los 20 caracteres)
            String mensajeLargo = "¡Bienvenido a Rongero! Esperamos que disfrutes comprando y vendiendo tus productos frikis de segunda mano. Esto es texto extra para ver cómo se ve un mensaje largo.Esto es texto extra para ver cómo se ve un mensaje largo.Esto es texto extra para ver cómo se ve un mensaje largo.Esto es texto extra para ver cómo se ve un mensaje largo.";
            Notification bienvenida = new Notification(mensajeLargo, destinatarios);

            // 3. Se la metemos en la bandeja al cliente
            nuevoCliente.addNotification(bienvenida);

            // --- FIN CÓDIGO DE PRUEBA ---

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}