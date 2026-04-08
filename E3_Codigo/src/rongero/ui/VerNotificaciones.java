package ui;

import model.user.Client;
import util.Notification;
import java.util.Scanner;
import java.util.List;

public class VerNotificaciones {
    /**
     * Submenú para gestionar la bandeja de entrada de notificaciones
     */
    public static void VerNotificaciones(Client cliente, Scanner scanner) {
        System.out.println("\n--- MIS NOTIFICACIONES ---");

        // 1. Usamos el metodo view_notifications para ver el resumen y los "No leídos"
        System.out.print(cliente.view_notifications());

        // 2. Pedimos la lista al cliente para saber si está vacía y poder elegir
        List<Notification> notificaciones = cliente.getMyNotifications();

        if (notificaciones == null || notificaciones.isEmpty()) {
            System.out.println("Tu bandeja de entrada está vacía.");
            return;
        }

        // 3. Menú interactivo para leerlas completas
        System.out.println("\n----------------------------------");
        System.out.print("Elige el número de la notificación que quieres leer (orden de arriba a abajo, 1-" + notificaciones.size() + ") o 0 para salir: ");
        String input = scanner.nextLine();

        try {
            int index = Integer.parseInt(input);

            if (index == 0) {
                return; // Volver al menú de cliente
            }

            if (index < 1 || index > notificaciones.size()) {
                System.out.println("[!] Opción no válida.");
                return;
            }

            // Obtenemos el objeto Notificación exacto (restamos 1 porque la lista empieza en 0)
            Notification seleccionada = notificaciones.get(index - 1);

            System.out.println("\n[MENSAJE COMPLETO]");
            System.out.println(cliente.read_notification(seleccionada));

            System.out.println("\n(Pulsa Enter para continuar...)");
            scanner.nextLine(); // Pausa para que el cliente pueda leer

        } catch (NumberFormatException e) {
            System.out.println("[!] Error: Por favor, introduce un número válido.");
        }
    }

}
