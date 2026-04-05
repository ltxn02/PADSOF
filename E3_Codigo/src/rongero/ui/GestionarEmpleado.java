package rongero.ui;
import java.util.Scanner;
import model.user.Manager;

public class GestionarEmpleado {
    public static void GestionarEmpleados(Manager gestor, Scanner scanner) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE EMPLEADOS ---");
            System.out.println("1.- Dar de alta un nuevo empleado");
            System.out.println("2.- Dar de baja / reactivar empleado");
            System.out.println("3.- Gestionar permisos de un empleado");
            System.out.println("0.- Volver al menú anterior");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    AltaEmpleado.mostrar(scanner);
                    break;
                case "2":
                    CambiarEstadoEmpleado.CambiarEstadoEmpleado(scanner);
                    break;
                case "3":
                     GestionarPermisos.GestionarPermisos(scanner);
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

}
