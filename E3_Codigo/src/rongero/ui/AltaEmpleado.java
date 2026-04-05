package rongero.ui;

import logic.Application;
import model.user.Employee;

import java.io.IOException;
import java.util.Scanner;

public class AltaEmpleado {

    public static void mostrar(Scanner scanner) {
        System.out.println("\n--- ALTA DE EMPLEADO ---");
        System.out.print("Nombre completo: ");
        String fullname = scanner.nextLine();
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Fecha de nacimiento: ");
        String birthdate = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Teléfono: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Salario mensual: ");

        try {
            double salary = Double.parseDouble(scanner.nextLine());

            // Se crea activo por defecto (true)
            Employee nuevoEmpleado = new Employee(username, password, fullname, dni, birthdate, email, phoneNumber, salary, true);
            Application.getInstance().registerEmployee(nuevoEmpleado);

        } catch (NumberFormatException e) {
            System.out.println("[!] Error: El salario debe ser un número (ej: 1200.50).");
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

}
