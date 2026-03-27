import java.util.Scanner;

public class main {
    // Declaramos el Scanner a nivel de clase para poder usarlo en todos los métodos
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("      BIENVENIDO A RONGERO           ");
        System.out.println("=====================================");

        // Aquí deberíais cargar los datos persistentes (ej. Sistema.cargarDatos())

        boolean salir = false;

        while (!salir) {
            System.out.println("\n¿Qué deseas hacer?");
            System.out.println("1.- Login");
            System.out.println("2.- Register");
            System.out.println("0.- Salir");
            System.out.print("Elige una opción (0-2): ");

            // Leemos la opción como texto para evitar errores si el usuario teclea letras
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    login();
                    break;
                case "2":
                    register();
                    break;
                case "0":
                    salir = true;
                    System.out.println("Guardando datos y cerrando la aplicación... ¡Hasta pronto!");
                    // Aquí deberíais guardar los datos persistentes (ej. Sistema.guardarDatos())
                    break;
                default:
                    System.out.println("(!) Opción no válida. Por favor, introduce 1 o 2.");
            }
        }

        scanner.close();
    }

    /**
     * Método auxiliar para manejar el Inicio de Sesión
     */
    private static void login() {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        System.out.print("Usuario o Correo: ");
        String identificador = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        // TODO: Aquí llamarías a la lógica de tu aplicación para validar el usuario.
        // Ejemplo: Usuario user = miTienda.iniciarSesion(identificador, password);
        // if (user != null) { mostrarMenuPrincipal(user); } else { error }

        System.out.println(">> (Simulando) Comprobando credenciales para: " + identificador);
    }

    /**
     * Método auxiliar para manejar el Registro de un nuevo Cliente
     */
    private static void register() {
        System.out.println("\n--- REGISTRO DE NUEVO CLIENTE ---");

        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();

        System.out.print("DNI: ");
        String dni = scanner.nextLine();

        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();

        System.out.print("Elige una contraseña: ");
        String password = scanner.nextLine();

        // TODO: Aquí llamarías a la lógica para crear y guardar el objeto Client
        // Ejemplo: Client nuevoCliente = new Client(nombre, dni, correo, password);
        // miTienda.añadirUsuario(nuevoCliente);

        System.out.println(">> (Simulando) Usuario " + nombre + " registrado correctamente.");
    }
}