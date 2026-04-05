package rongero.ui;

import logic.Application;
import model.user.Client;
import model.user.Employee;
import model.user.Manager;
import model.user.RegisteredUser;
import java.io.IOException;
import java.util.Scanner;
public class Login {
    public static void Mostrar(Scanner scanner) {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        System.out.print("Username: ");
        String identificador = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            RegisteredUser user = Application.getInstance().login(identificador, password);

            // Redirigir al menú correcto según el tipo de usuario logueado
            if (user instanceof Manager) {
                System.out.println(">> ¡Bienvenido, Gestor " + user.getUsername() + "!");
                MenuGestor.mostrar((Manager) user, scanner);
            } else if (user instanceof Client) {
                System.out.println(">> ¡Bienvenido, Cliente " + user.getUsername() + "!");
                MenuCliente.MenuCliente((Client) user, scanner);
            } else if (user instanceof Employee) {
                System.out.println(">> ¡Bienvenido, Empleado " + user.getUsername() + "!");
                MenuEmpleado.MenuEmpleado((Employee) user, scanner);
            }

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

}
