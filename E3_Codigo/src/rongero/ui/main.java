package ui;
import java.util.Scanner;
import util.*;
import logic.Application;


public class main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // 1. Configurar la persistencia
        //SaveLoader loader = new SaveLoader(new BinPersistenceStrategy());

        // 2. Intentar cargar datos previos
        //Application app = loader.loadData();
/*
        // Si no hay datos, Application.getInstance() creará los de por defecto
        if (app == null) {
            app = Application.getInstance();
        } else {
            // Sincronizar la instancia cargada con el Singleton
            Application.setInstance(app);
        }
*/
        System.out.println("=====================================");
        System.out.println("      BIENVENIDO A RONGERO           ");
        System.out.println("=====================================");

        // Aquí deberíais cargar los datos persistentes (ej. Sistema.cargarDatos())

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
                    VerCatalogoInvitado.mostrar();
                    break;
                case "2":
                    Login.Mostrar(scanner);
                    break;
                case "3":
                    Register.mostrar(scanner);
                    break;
                case "0":
                    salirApp = true;
                    System.out.println("Guardando datos y cerrando la aplicación... ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("[!] Opción no válida. Por favor, introduce un número del 0 al 3.");
            }
        }
       // loader.saveData(Application.getInstance());
        scanner.close();
    }





















}