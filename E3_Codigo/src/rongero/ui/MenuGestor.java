package ui;

import java.util.Scanner;
import model.user.Manager;

public class MenuGestor {

    // He cambiado el nombre del método a 'mostrar' para que no se confunda con un constructor
    public static void mostrar(Manager gestor, Scanner scanner) {
        boolean cerrarSesion = false;

        while (!cerrarSesion) {
            System.out.println("\n--- PANEL DE ADMINISTRACIÓN (GESTOR) ---");
            System.out.println("Usuario: " + gestor.getUsername());
            System.out.println("1.- Gestionar empleados (Alta/Baja/Permisos)");
            System.out.println("2.- Gestionar catálogo (categorías y packs)");
            System.out.println("3.- Gestionar descuentos");
            System.out.println("4.- Ver estadísticas (Próximamente)");
            System.out.println("0.- Cerrar sesión");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    // Asumiendo que GestionarEmpleado tiene este método
                    GestionarEmpleado.GestionarEmpleados(gestor, scanner);
                    break;
                case "2":
                    GestionarCatalogo.mostrar(scanner);
                    break;
                case "3":
                    GestionarDescuentos.mostrar(scanner);
                    break;
                case "4":
                    System.out.println("[!] Funcionalidad en desarrollo...");
                    break;
                case "0":
                    cerrarSesion = true;
                    System.out.println("Cerrando sesión de administrador...");
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }
}