package ui;
import java.util.Scanner;
import model.user.Client;

public class MenuCliente {
    public static void MenuCliente(Client cliente, Scanner scanner) {
        boolean cerrarSesion = false;
        while (!cerrarSesion) {
            System.out.println("\n--- PANEL DE CLIENTE: " + cliente.getUsername() + " ---");
            System.out.println("1.- Ver catálogo de productos y comprar");
            System.out.println("2.- Ver mi carrito");
            System.out.println("3.- Ver productos de segunda mano");
            System.out.println("4.- Gestionar mi cartera de segunda mano");
            System.out.println("5.- Ver mis notificaciones");
            System.out.println("6.- Ver recomendaciones personalizadas");
            System.out.println("7.- Ver Historial de compras");
            System.out.println("0.- Cerrar sesión");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    ComprarProducto.ComprarProducto(cliente, scanner);
                    break;
                case "2":
                    System.out.println(">> Mostrando carrito y procesando pago...");
                    VerCarrito.VerCarrito(cliente, scanner);
                    break;
                case "3":
                    System.out.println("Productos de Segunda mano:" );
                    IntercambiarProductos.IntercambiarProductos(cliente, scanner);
                    break;
                case "4":
                    GestionarCartera.GestionarCartera(cliente, scanner);
                    break;
                case "5":
                    VerNotificaciones.VerNotificaciones(cliente, scanner);
                    break;
                case "6":
                    MostrarRecomendaciones.MostrarRecomendaciones(cliente, scanner);
                    break;
                case "7":
                    VerHistorial.mostrar(cliente);
                    break;
                case "0":
                    cerrarSesion = true;
                    System.out.println("Cerrando sesión de " + cliente.getUsername() + "...");
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

}
