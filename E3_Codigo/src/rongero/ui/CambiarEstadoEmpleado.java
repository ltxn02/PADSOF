package rongero.ui;

import model.user.Employee;

import java.util.ArrayList;
import java.util.Scanner;

public class CambiarEstadoEmpleado {

    public static void CambiarEstadoEmpleado(Scanner scanner) {
        ArrayList<Employee> empleados = ObtenerListaEmpleados.ObtenerListaEmpleados();
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados en el sistema.");
            return;
        }

        System.out.println("\n--- ESTADO DE EMPLEADOS ---");
        for (int i = 0; i < empleados.size(); i++) {
            Employee e = empleados.get(i);
            String estado = e.isEnabled() ? "ACTIVO" : "DE BAJA";
            System.out.println((i + 1) + ".- " + e.getUsername() + " | Estado actual: " + estado);
        }

        System.out.print("\nElige el número del empleado para cambiar su estado (0 para salir): ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index == 0) return;
            if (index < 1 || index > empleados.size()) {
                System.out.println("[!] Opción no válida.");
                return;
            }

            Employee seleccionado = empleados.get(index - 1);
            if (seleccionado.isEnabled()) {
                seleccionado.desactivateEmployee();
                System.out.println("[+] El empleado " + seleccionado.getUsername() + " ha sido DADO DE BAJA.");
            } else {
                seleccionado.activateEmployee();
                System.out.println("[+] El empleado " + seleccionado.getUsername() + " ha sido REACTIVADO.");
            }

        } catch (NumberFormatException e) {
            System.out.println("[!] Entrada inválida.");
        }
    }

}
