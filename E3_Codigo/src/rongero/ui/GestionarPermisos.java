package rongero.ui;

import model.user.Employee;
import util.Permission;
import java.util.Scanner;
import java.util.ArrayList;

public class GestionarPermisos {

    public static void GestionarPermisos(Scanner scanner) {
        ArrayList<Employee> empleados = ObtenerListaEmpleados.ObtenerListaEmpleados();
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados en el sistema.");
            return;
        }

        System.out.println("\n--- GESTIÓN DE PERMISOS ---");
        for (int i = 0; i < empleados.size(); i++) {
            System.out.println((i + 1) + ".- " + empleados.get(i).getUsername());
        }

        System.out.print("\nElige el número del empleado (0 para salir): ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index == 0) return;
            if (index < 1 || index > empleados.size()) {
                System.out.println("[!] Opción no válida.");
                return;
            }

            Employee emp = empleados.get(index - 1);

            System.out.println("\nPermisos actuales de " + emp.getUsername() + ": " + emp.permisosEmpleado());
            System.out.println("1.- Añadir permiso");
            System.out.println("2.- Quitar permiso");
            System.out.print("Elige una opción: ");
            String accion = scanner.nextLine();

            if (!accion.equals("1") && !accion.equals("2")) {
                System.out.println("[!] Acción no válida.");
                return;
            }

            System.out.println("\nPermisos disponibles:");
            Permission[] todosLosPermisos = Permission.values();
            for (int i = 0; i < todosLosPermisos.length; i++) {
                System.out.println((i + 1) + ".- " + todosLosPermisos[i]);
            }

            System.out.print("Elige el número del permiso: ");
            int indexPermiso = Integer.parseInt(scanner.nextLine());

            if (indexPermiso < 1 || indexPermiso > todosLosPermisos.length) {
                System.out.println("[!] Opción no válida.");
                return;
            }

            Permission permisoSeleccionado = todosLosPermisos[indexPermiso - 1];

            if (accion.equals("1")) {
                if (!emp.permisosEmpleado().contains(permisoSeleccionado)) {
                    emp.add_permisions(permisoSeleccionado);
                    System.out.println("[+] Permiso " + permisoSeleccionado + " añadido a " + emp.getUsername());
                } else {
                    System.out.println("[!] El empleado ya tiene ese permiso.");
                }
            } else {
                emp.delete_permisions(permisoSeleccionado);
                System.out.println("[+] Permiso " + permisoSeleccionado + " retirado de " + emp.getUsername());
            }

        } catch (NumberFormatException e) {
            System.out.println("[!] Entrada inválida.");
        }
    }
}
