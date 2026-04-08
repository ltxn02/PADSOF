package ui;
import java.util.Scanner;
import model.user.Client;

public class GestionarCartera {
    public static void GestionarCartera(Client cliente, Scanner scanner) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- MI CARTERA DE SEGUNDA MANO ---");
            System.out.println("1.- Ver mis productos subidos");
            System.out.println("2.- Subir un nuevo producto para valorar");
            System.out.println("0.- Volver al menú anterior");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    VerMisProductosSegundaMano.VerMisProductosSegundaMano(cliente);
                    break;
                case "2":
                    SubirProductoSegundaMano.SubirProductoSegundaMano(cliente, scanner);
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
