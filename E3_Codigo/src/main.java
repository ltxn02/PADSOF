import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("      BIENVENIDO A RONGERO           ");
        System.out.println("=====================================");

        // Aquí deberíais cargar los datos persistentes (ej. Sistema.cargarDatos())

        boolean salirApp = false;

        while (!salirApp) {
            System.out.println("\n--- MENÚ INICIAL (INVITADO) ---");
            System.out.println("1.- Ver catálogo de productos");
            System.out.println("2.- Login");
            System.out.println("3.- Registrarse");
            System.out.println("0.- Salir de la aplicación");
            System.out.print("Elige una opción (0-3): ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    verCatalogolnvitado();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    register();
                    break;
                case "0":
                    salirApp = true;
                    System.out.println("Guardando datos y cerrando la aplicación... ¡Hasta pronto!");
                    // Aquí deberíais guardar los datos persistentes (ej. Sistema.guardarDatos())
                    break;
                default:
                    System.out.println("[!] Opción no válida. Por favor, introduce un número del 0 al 3.");
            }
        }
        scanner.close();
    }

    // --- MÉTODOS DEL MENÚ INICIAL ---
    private static void verCatalogolnvitado() {
        System.out.println("\n--- CATÁLOGO DE PRODUCTOS ---");

        // Obtenemos la lista de productos desde Application
        ArrayList<Product> productos = Application.getCatalog();

        if (productos.isEmpty()) {
            System.out.println("Actualmente no hay productos en la tienda.");
            return;
        }

        // Recorremos la lista e imprimimos cada producto
        for (Product p : productos) {
            System.out.println("- " + p.getName() + " | Precio: " + p.getPrice() + "€");
        }
    }

    private static void login() {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        System.out.print("Username: ");
        String identificador = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            RegisteredUser user = Application.login(identificador, password);

            // Redirigir al menú correcto según el tipo de usuario logueado
            if (user instanceof Manager) {
                System.out.println(">> ¡Bienvenido, Gestor " + user.getUsername() + "!");
                menuGestor((Manager) user);
            } else if (user instanceof Client) {
                System.out.println(">> ¡Bienvenido, Cliente " + user.getUsername() + "!");
                menuCliente((Client) user);
            } else if (user instanceof Employee) {
                System.out.println(">> ¡Bienvenido, Empleado " + user.getUsername() + "!");
                menuEmpleado((Employee) user);
            }

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void register() {
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
            Application.registerClient(nuevoCliente);
            System.out.println(">> Usuario " + username + " registrado correctamente. Ya puedes iniciar sesión.");
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // --- MENÚS ESPECÍFICOS POR ROL ---

    private static void menuCliente(Client cliente) {
        boolean cerrarSesion = false;
        while (!cerrarSesion) {
            System.out.println("\n--- PANEL DE CLIENTE: " + cliente.getUsername() + " ---");
            System.out.println("1.- Ver catálogo de productos y comprar");
            System.out.println("2.- Ver mi carrito");
            System.out.println("3.- Explorar ofertas de intercambio");
            System.out.println("4.- Gestionar mi cartera de segunda mano");
            System.out.println("5.- Ver mis notificaciones");
            System.out.println("6.- Ver recomendaciones personalizadas");
            System.out.println("0.- Cerrar sesión");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    comprarProducto(cliente);
                    break;
                case "2":
                    System.out.println(">> Mostrando carrito y procesando pago...");
                    verCarrito(cliente);
                    break;
                case "6":
                    mostrarRecomendaciones(cliente);
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

    /**
     * Metodo para mostrar el catálogo numerado y añadir productos al carrito
     */
    private static void comprarProducto(Client cliente) {
        System.out.println("\n--- CATÁLOGO DE PRODUCTOS ---");

        ArrayList<Product> productos = Application.getCatalog();

        if (productos.isEmpty()) {
            System.out.println("Actualmente no hay productos en la tienda.");
            return;
        }

        // 1. Mostramos los productos con un índice (1, 2, 3...)
        for (int i = 0; i < productos.size(); i++) {
            Product p = productos.get(i);
            System.out.println((i + 1) + ".- " + p.getName() + " | Precio: " + p.getPrice() + "€");
        }
        System.out.println("0.- Volver al menú anterior");

        System.out.print("\nElige el número del producto que deseas añadir al carrito (0 para salir): ");
        String inputIndex = scanner.nextLine();

        try {
            int index = Integer.parseInt(inputIndex);

            if (index == 0) {
                return; // El usuario ha elegido volver
            }

            if (index < 1 || index > productos.size()) {
                System.out.println("[!] Opción no válida. Producto no encontrado.");
                return;
            }

            // Obtenemos el producto seleccionado (restamos 1 porque los arrays empiezan en 0)
            Product selectedProduct = productos.get(index - 1);

            System.out.print("¿Cuántas unidades de '" + selectedProduct.getName() + "' deseas añadir? ");
            String inputQty = scanner.nextLine();
            int quantity = Integer.parseInt(inputQty);

            if (quantity <= 0) {
                System.out.println("[!] La cantidad debe ser mayor que 0.");
                return;
            }

            // 2. Añadimos el producto al carrito del cliente
            // Funciona porque Product hereda de NewProduct (que es lo que pide addCartItem)
            cliente.getShoppingCart().addCartItem(selectedProduct, quantity);
            System.out.println("[+] ¡Éxito! " + quantity + "x " + selectedProduct.getName() + " añadido(s) a tu carrito.");

        } catch (NumberFormatException e) {
            // Capturamos el error si el usuario escribe texto en lugar de números
            System.out.println("[!] Error: Por favor, introduce un número válido.");
        } catch (IllegalArgumentException e) {
            // Capturamos el error de vuestra clase CartItem si no hay stock suficiente
            System.out.println("[!] Error de stock: " + e.getMessage());
        }
    }

    private static void menuGestor(Manager gestor) {
        boolean cerrarSesion = false;
        while (!cerrarSesion) {
            System.out.println("\n--- PANEL DE ADMINISTRACIÓN (GESTOR) ---");
            System.out.println("1.- Gestionar empleados (Alta/Baja/Permisos)");
            System.out.println("2.- Gestionar catálogo (categorías y packs)");
            System.out.println("3.- Gestionar descuentos");
            System.out.println("4.- Ver estadísticas");
            System.out.println("0.- Cerrar sesión");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.println(">> (Simulando) Abriendo panel de empleados...");
                    break;
                // Añadir aquí los demás cases
                case "0":
                    cerrarSesion = true;
                    System.out.println("Cerrando sesión de administrador...");
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }
    private static ArrayList<Client> todoslosClientes() {
        ArrayList<RegisteredUser> usuarios = Application.getUsers();
        ArrayList<Client> clientes = new ArrayList<>();
        for (RegisteredUser u : usuarios) {
            if (u instanceof Client) {
                clientes.add((Client) u);
            }
        }
        return clientes;
    }

    private static void mostrarRecomendaciones(Client c) {
        System.out.println("\nPRODUCTOS RECOMENDADOS:");

        ArrayList<Product> catalogo = Application.getCatalog();
        ArrayList<Client> listaClientes = todoslosClientes();

        ArrayList<Product> recomendados = SistemaRecomendaciones.obtenerRecomendaciones(c, catalogo, listaClientes);

        if (recomendados.isEmpty()) {
            System.out.println("Aún no tenemos suficientes datos para darte recomendaciones.");
            System.out.println("¡Prueba a comprar y valorar productos primero!");
            return;
        }

        for (int i = 0; i < recomendados.size(); i++) {
            Product p = recomendados.get(i);
            System.out.println((i + 1) + ".- " + p.getName() + " | Precio: " + p.getPrice() + "€");
        }
        System.out.println("0.- Volver al menú anterior");

        System.out.print("\n¿Deseas añadir algun producto a tu carrito? (0 para salir): ");
        String inputIndex = scanner.nextLine();

        try {
            int index = Integer.parseInt(inputIndex);

            if (index == 0) return;

            if (index < 1 || index > recomendados.size()) {
                System.out.println("ERROR Opción no válida.");
                return;
            }

            Product seleccionado = recomendados.get(index - 1);
            System.out.print("¿Cuántas unidades de '" + seleccionado.getName() + "' deseas? ");
            int quantity = Integer.parseInt(scanner.nextLine());

            if (quantity <= 0) {
                System.out.println("[!] La cantidad debe ser mayor que 0.");
                return;
            }

            c.getShoppingCart().addCartItem(seleccionado, quantity);
            System.out.println("[+] ¡Añadido! " + seleccionado.getName() + " ya está en tu carrito.");

        } catch (NumberFormatException e) {
            System.out.println("[!] Error: Introduce un número válido.");
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Error de stock: " + e.getMessage());
        }
    }

    private static void menuEmpleado(Employee empleado) {
        boolean cerrarSesion = false;
        while (!cerrarSesion) {
            System.out.println("\n--- PANEL DE EMPLEADO ---");
            System.out.println("1.- Gestionar productos (subida manual/masiva)");
            System.out.println("2.- Gestionar pedidos (cambiar estados)");
            System.out.println("3.- Valorar productos de segunda mano");
            System.out.println("4.- Confirmar intercambios físicos");
            System.out.println("0.- Cerrar sesión");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.println(">> (Simulando) Abriendo gestión de inventario...");
                    break;
                // Añade aquí los demás cases
                case "0":
                    cerrarSesion = true;
                    System.out.println("Cerrando sesión de empleado...");
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

    /**
     * Metodo para mostrar el carrito y procesar el pago con la librería del profesor
     */
    private static void verCarrito(Client cliente) {
        System.out.println("\n--- TU CARRITO DE LA COMPRA ---");

        ShoppingCart carrito = cliente.getShoppingCart();

        // 1. Comprobamos si el carrito está vacío
        if (carrito.getCartItems().isEmpty()) {
            System.out.println("Tu carrito está vacío. ¡Ve al catálogo a añadir cosas!");
            return;
        }

        // 2. Mostramos los productos del carrito y el precio total
        double precioTotal = 0.0;
        for (CartItem item : carrito.getCartItems()) {
            // Asumiendo que CartItem tiene métodos para obtener el producto y la cantidad
            Product p = (Product) item.getProduct();
            int cantidad = item.getQuantity();
            double subtotal = p.getPrice() * cantidad;
            precioTotal += subtotal;

            System.out.println("- " + cantidad + "x " + p.getName() + " | Subtotal: " + subtotal + "€");
        }
        System.out.println("---------------------------------");
        System.out.println("TOTAL A PAGAR: " + precioTotal + "€");

        // 3. Preguntamos si quiere tramitar el pedido
        System.out.print("\n¿Deseas finalizar la compra y pagar ahora? (S/N): ");
        String respuesta = scanner.nextLine();

        if (!respuesta.equalsIgnoreCase("S")) {
            System.out.println("Compra aplazada. Los productos siguen en tu carrito.");
            return;
        }

        // 4. Proceso de Pago usando la librería (a través de tu clase Order)
        System.out.print("Introduce tu número de tarjeta de crédito (16 dígitos): ");
        String numeroTarjeta = scanner.nextLine();

        System.out.println(">> Conectando con la pasarela de pago...");

        // Creamos el pedido (asumo que tu constructor de Order recibe el cliente, el carrito y el total)
        Order nuevoPedido = new Order(cliente, carrito, precioTotal);

        // Llamamos al metodo procesarPago que creaste antes en Order.java con sus try-catch
        boolean pagoExitoso = nuevoPedido.procesarPago(numeroTarjeta);

        if (pagoExitoso) {
            // Si la librería dice que OK, vaciamos el carrito y guardamos el pedido
            carrito.clearCart(); // Asegúrate de tener un metodo en ShoppingCart para vaciar la lista
            cliente.getOrderHistoric().addOrder(nuevoPedido); // Guardamos el pedido en el historial del cliente
            System.out.println("[+] ¡Compra finalizada con éxito! Tu código de recogida es: " + nuevoPedido.getPickupCode());
        } else {
            // Si la librería lanza excepción (tarjeta falsa, sin internet...), el pedido se cancela
            System.out.println("[!] La compra no se ha podido completar. Revisa tu método de pago.");
        }
    }
}