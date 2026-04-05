package rongero.ui;

import logic.Application;
import model.user.Client;
import model.user.RegisteredUser;
import util.Notification;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

public class Register {

    public static void mostrar(Scanner scanner) {
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
            Application.getInstance().registerClient(nuevoCliente);
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
