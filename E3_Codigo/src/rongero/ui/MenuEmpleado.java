package rongero.ui;
import java.util.Scanner;
import model.user.Employee;

public class MenuEmpleado {
    public static void MenuEmpleado(Employee empleado, Scanner scanner) {
        boolean cerrarSesion = false;
        while (!cerrarSesion) {
            System.out.println("\n--- PANEL DE EMPLEADO ---");
            System.out.println("1.- Gestionar productos (subida manual/masiva)");
            System.out.println("2.- Gestionar pedidos (cambiar estados)");
            System.out.println("3.- Valorar productos de segunda mano");
            System.out.println("4.- Confirmar intercambios físicos");
            System.out.println("0.- Cerrar sesión");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.println(">> (Simulando) Abriendo gestión de inventario...");
                    break;
                // Añade aquí los demás cases
                case "0":
                    cerrarSesion = true;
                    System.out.println("Cerrando sesión de empleado...");
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }
}
