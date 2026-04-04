import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

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
        ArrayList<NewProduct> productos = Application.getCatalog();

        if (productos.isEmpty()) {
            System.out.println("Actualmente no hay productos en la tienda.");
            return;
        }

        // Recorremos la lista e imprimimos cada producto
        for (NewProduct p : productos) {
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

    // --- MENÚS ESPECÍFICOS POR ROL ---

    private static void menuCliente(Client cliente) {
        boolean cerrarSesion = false;
        while (!cerrarSesion) {
            System.out.println("\n--- PANEL DE CLIENTE: " + cliente.getUsername() + " ---");
            System.out.println("1.- Ver catálogo de productos y comprar");
            System.out.println("2.- Ver mi carrito");
            System.out.println("3.- Ver productos de segunda mano");
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
                case "3":
                    System.out.println("Productos de Segunda mano:" );
                    intercambiarProductos(cliente);
                    break;
                case "4":
                    gestionarCartera(cliente);
                    break;
                case "5":
                    verNotificaciones(cliente);
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

        ArrayList<NewProduct> productos = Application.getCatalog();

        if (productos.isEmpty()) {
            System.out.println("Actualmente no hay productos en la tienda.");
            return;
        }

        // 1. Mostramos los productos con un índice (1, 2, 3...)
        for (int i = 0; i < productos.size(); i++) {
            NewProduct p = productos.get(i);
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
            NewProduct selectedProduct = productos.get(index - 1);

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
    private static void intercambiarProductos(Client c) {
        ArrayList<SecondHandProduct> productos = Application.getSecondHandProducts();
        List<ExchangeOffer> ofertashechas = Application.getoffersmade(c);
        List<ExchangeOffer> ofertasrecibidas = Application.getoffersreceived(c);
        List<SecondHandProduct> misproductos = c.getCarteraSegundaMano();
        List<SecondHandProduct> productosactivos = new ArrayList<>();
        List<SecondHandProduct> productosinactivos = new ArrayList<>();

        for (SecondHandProduct a : misproductos) {
            if (a.isAppraised()) {
                productosactivos.add(a);
            } else {
                productosinactivos.add(a);
            }

        }
        while (true) {
            System.out.println("\n=============================================================================================================================================================================");
            System.out.println("                                                                           TIENDA DE INTERCAMBIO ");
            System.out.println("==============================================================================================================================================================================");


        if (productos.isEmpty()) {
            System.out.println("Actualmente no hay productos para intercambios en la tienda.");
        }
        for (int i = 0; i < productos.size(); i++) {
            SecondHandProduct p = productos.get(i);
            if (p.isAppraised()){
            if(!p.isOwnedBy(c)){
            System.out.println((i + 1) + ".- " + p.getName() + " | Precio: " + p.getPrice() + "€");
        }}}
        System.out.println("\nA.- Ofertas realizadas | B.- Ofertas recibidas | C.- Mis Productos | 0.- Volver");
        System.out.println("\nSelección: ");
        System.out.print("\nElige el número del producto que deseas ver mas en detalle (0 para salir): ");
        String inputIndex = scanner.nextLine().toUpperCase();

        try {
            if (inputIndex.equals("0")) return;

            switch (inputIndex) {
                case "A":
                    int i = 0;
                    for (ExchangeOffer a : ofertashechas) {
                        System.out.println((i + 1) + "Fecha de la oferta: " + a.getCreateDate() + "Producto en oferta: " + a.getRequestedProduct());
                        i++;
                    }
                    System.out.println("0.- Volver al menú anterior");

                    System.out.print("\nElige el número de la oferta que quieres ver detalladamente (0 para salir): ");
                    String inputIndex2 = scanner.nextLine();
                    int index2 = Integer.parseInt(inputIndex2);

                    if (index2 == 0) {
                        return;
                    }

                    if (index2 < 1 || index2 > ofertashechas.size()) {
                        System.out.println("[!] Opción no válida. Oferta no encontrada.");
                        return;
                    }
                    ExchangeOffer selectedOffer = ofertashechas.get(index2 - 1);
                    System.out.println(selectedOffer);

                    System.out.println("0.- Volver al menu anterior");
                    String inputIndex3 = scanner.nextLine();
                    int index3 = Integer.parseInt(inputIndex3);

                    if (index3 == 0) {
                        return;
                    } else {
                        System.out.println("Opcion no valida");
                    }
                    break;
                case "B":
                    int s = 0;
                    for (ExchangeOffer a : ofertasrecibidas) {
                        System.out.println((s + 1) + "Fecha de la oferta: " + a.getCreateDate() + "Producto en oferta: " + a.getRequestedProduct());
                        s++;
                    }
                    System.out.println("0.- Volver al menú anterior");

                    System.out.println("\nElige el número de la oferta que quieres ver detalladamente (0 para salir): ");
                    String inputIndex5 = scanner.nextLine();
                    int index5 = Integer.parseInt(inputIndex5);

                    if (index5 == 0) {
                        return;
                    }

                    if (index5 < 1 || index5 > ofertasrecibidas.size()) {
                        System.out.println("[!] Opción no válida. Oferta no encontrada.");
                        return;
                    }
                    ExchangeOffer selectedOffer2 = ofertasrecibidas.get(index5 - 1);
                    System.out.println(selectedOffer2);
                    if (selectedOffer2.getEstado() == ExchangeStatus.PENDIENTE){
                    System.out.println("1.- Aceptar Oferta");
                    System.out.println("2.- Rechazar Oferta");
                    System.out.println("0.- Volver al menu anterior");
                    String inputIndex6 = scanner.nextLine();
                    int index6 = Integer.parseInt(inputIndex6);

                    if (index6 == 0) {
                        return;
                    } else if (index6 == 1){
                        selectedOffer2.aceptaroferta();
                        System.out.println("Oferta Aceptada correctamente");
                    } else if (index6 == 2){
                        selectedOffer2.reject_offer();
                        System.out.println("Oferta Rechazada Correctamente");
                    } else {
                        System.out.println("[!]Opcion no valida");
                    }} else {
                        System.out.println("0.- Volver al menu anterior");
                        String inputIndex6 = scanner.nextLine();
                        int index6 = Integer.parseInt(inputIndex6);

                        if (index6 == 0) {
                            return;
                        } else {
                            System.out.println("[!]Opcion no valida");
                        }

                        }
                    break;
                case "C":
                    System.out.println("1.- Ver productos activos");
                    System.out.println("2.- Ver productos desactivados");
                    System.out.println("0.- Volver al menu anterior");
                    String inputIndex7 = scanner.nextLine();
                    int index7 = Integer.parseInt(inputIndex7);

                    if (index7 == 0) {
                        return;

                    } else if (index7 < 1 || index7 > 2) {
                        System.out.println("[!] Opción no válida. Oferta no encontrada.");
                        return;
                    }
                    switch (index7) {
                        case 1:
                            int y = 0;
                            for (SecondHandProduct a : productosactivos) {
                                System.out.println((y + 1) + "Fecha añadido: " + a.getDateadded() + "Nombre: " + a.getName() + "Valorado en: " + a.getPrice());

                                y++;
                            }
                            System.out.println("Elige el número del producto que quieres ver detalladamente");
                            System.out.println("0.- Volver al menú anterior");
                            String inputIndex8 = scanner.nextLine();
                            int index8 = Integer.parseInt(inputIndex8);

                            if (index8 == 0) {
                                return;

                            } else if (index8 < 1 || index8 > productosactivos.size()) {
                                System.out.println("[!] Opción no válida. Oferta no encontrada.");
                                return;

                            } else {
                                System.out.println(productosactivos.get(index8 - 1));
                            }
                            break;
                        case 2:
                            int j = 0;
                            for (SecondHandProduct a : productosinactivos) {
                                System.out.println((j + 1) + "Fecha añadido: " + a.getDateadded() + "Nombre: " + a.getName() + "Valorado en: " + a.getPrice());

                                j++;
                            }
                            System.out.println("Elige el número del producto que quieres ver detalladamente");
                            System.out.println("0.- Volver al menú anterior");
                            String inputIndex9 = scanner.nextLine();
                            int index9 = Integer.parseInt(inputIndex9);

                            if (index9 == 0) {
                                return;

                            } else if (index9 < 1 || index9 > productosinactivos.size()) {
                                System.out.println("[!] Opción no válida. Oferta no encontrada.");
                                return;

                            } else {
                                System.out.println(productosinactivos.get(index9 - 1));

                            }
                            break;
                    }
                    break;
                default:
                    int index = Integer.parseInt(inputIndex);
                    if (index >= 1 && index <= productos.size()) {
                        SecondHandProduct select = productos.get(index -1);
                        System.out.println("Detalles: \n" );
                        System.out.println(select);
                    } else {
                        System.out.println("Selección no válida.");
                    }
                    break;

            }
        } catch (NumberFormatException e) {
            System.out.println("[!] Entrada inválida.");
        }

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
                    gestionarEmpleados(gestor);
                    break;
                case "2":
                    gestionarCatalogo(gestor);
                    break;
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

        ArrayList<NewProduct> catalogo = Application.getCatalog();
        ArrayList<Client> listaClientes = todoslosClientes();

        // Cambiamos Product por NewProduct aquí
        ArrayList<NewProduct> recomendados = SistemaRecomendaciones.obtenerRecomendaciones(c, catalogo, listaClientes);

        if (recomendados.isEmpty()) {
            System.out.println("Aún no tenemos suficientes datos para darte recomendaciones.");
            System.out.println("¡Prueba a comprar y valorar productos primero!");
            return;
        }

        // Y cambiamos Product por NewProduct en el for y al seleccionar
        for (int i = 0; i < recomendados.size(); i++) {
            NewProduct p = recomendados.get(i);
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

            // Cambiamos Product por NewProduct aquí también
            NewProduct seleccionado = recomendados.get(index - 1);
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
            NewProduct p = item.getProduct();
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
        Order nuevoPedido = new Order(cliente, carrito.getCartItems(), precioTotal);

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

    /**
     * Submenú para gestionar los productos de segunda mano del cliente
     */
    private static void gestionarCartera(Client cliente) {
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
                    verMisProductosSegundaMano(cliente);
                    break;
                case "2":
                    subirProductoSegundaMano(cliente);
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void verMisProductosSegundaMano(Client cliente) {
        System.out.println("\n--- MIS PRODUCTOS DE SEGUNDA MANO ---");
        List<SecondHandProduct> misProductos = cliente.getMyProducts();

        if (misProductos.isEmpty()) {
            System.out.println("Todavía no has subido ningún producto.");
            return;
        }

        for (SecondHandProduct p : misProductos) {
            System.out.println(p.toString());
            System.out.println("--------------------------------");
        }
    }

    private static void subirProductoSegundaMano(Client cliente) {
        System.out.println("\n--- SUBIR NUEVO PRODUCTO ---");

        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción breve: ");
        String descripcion = scanner.nextLine();

        System.out.print("Ruta de la foto (ej. img/manga.jpg): ");
        String foto = scanner.nextLine();

        // 1. Elegir Tipo de Producto (Enum ItemType)
        System.out.println("Tipo de producto (1. COMIC, 2. GAME, 3. FIGURINE): ");
        String tipoInput = scanner.nextLine();
        ItemType tipo = ItemType.COMIC; // Por defecto
        if (tipoInput.equals("2")) tipo = ItemType.GAME;
        if (tipoInput.equals("3")) tipo = ItemType.FIGURINE;

        // 2. Elegir Condición (Enum Condition)
        System.out.println("Estado de conservación (1. PERFECTO, 2. MUY_BUENO, 3. USO_LIGERO, 4. USO_EVIDENTE, 5. DAÑADO): ");
        String condInput = scanner.nextLine();
        Condition condicion = Condition.PERFECTO; // Por defecto
        switch (condInput) {
            case "2": condicion = Condition.MUY_BUENO; break;
            case "3": condicion = Condition.USO_LIGERO; break;
            case "4": condicion = Condition.USO_EVIDENTE; break;
            case "5": condicion = Condition.DAÑADO; break;
        }

        // 3. Creamos el producto usando tu constructor.
        // Pasamos 0.0 al precio y false a isAppraised porque la tienda aún no lo ha tasado.
        SecondHandProduct nuevoProducto = new SecondHandProduct(
                nombre, descripcion, foto, 0.0, false, tipo, condicion, cliente
        );

        // 4. Lo añadimos a la lista personal del cliente y a la lista general de la app
        cliente.addMyProduct(nuevoProducto);
        Application.addSecondHandProduct(nuevoProducto);

        System.out.println("\n[+] Producto subido con éxito. Un empleado lo tasará pronto.");
    }

    /**
     * Submenú para gestionar la bandeja de entrada de notificaciones
     */
    private static void verNotificaciones(Client cliente) {
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

    // --- SUBMENÚ: GESTIÓN DE EMPLEADOS ---

    private static void gestionarEmpleados(Manager gestor) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE EMPLEADOS ---");
            System.out.println("1.- Dar de alta un nuevo empleado");
            System.out.println("2.- Dar de baja / reactivar empleado");
            System.out.println("3.- Gestionar permisos de un empleado");
            System.out.println("0.- Volver al menú anterior");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    altaEmpleado();
                    break;
                case "2":
                    cambiarEstadoEmpleado();
                    break;
                case "3":
                    gestionarPermisos();
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void altaEmpleado() {
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
            Application.registerEmployee(nuevoEmpleado);

        } catch (NumberFormatException e) {
            System.out.println("[!] Error: El salario debe ser un número (ej: 1200.50).");
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void cambiarEstadoEmpleado() {
        ArrayList<Employee> empleados = obtenerListaEmpleados();
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

    private static void gestionarPermisos() {
        ArrayList<Employee> empleados = obtenerListaEmpleados();
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

    /**
     * Helper para filtrar el HashMap de la aplicación y obtener solo los empleados
     */
    private static ArrayList<Employee> obtenerListaEmpleados() {
        ArrayList<RegisteredUser> usuarios = Application.getUsers();
        ArrayList<Employee> empleados = new ArrayList<>();
        for (RegisteredUser u : usuarios) {
            if (u instanceof Employee) {
                empleados.add((Employee) u);
            }
        }
        return empleados;
    }

    // --- SUBMENÚ: GESTIÓN DE CATÁLOGO ---
    private static void gestionarCatalogo(Manager gestor) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE CATÁLOGO ---");
            System.out.println("1.- Ver categorías existentes");
            System.out.println("2.- Añadir nueva categoría");
            System.out.println("3.- Gestionar Packs (En construcción \uD83D\uDEA7)");
            System.out.println("0.- Volver al menú anterior");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    verCategorias();
                    break;
                case "2":
                    crearCategoria();
                    break;
                case "3":
                    System.out.println("\n[!] La funcionalidad de Packs aún no ha sido implementada por el equipo.");
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void verCategorias() {
        System.out.println("\n--- CATEGORÍAS ACTUALES ---");
        ArrayList<Category> categorias = Application.getGlobalCategories();

        if (categorias.isEmpty()) {
            System.out.println("No hay categorías registradas en el sistema.");
            return;
        }

        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ".- " + categorias.get(i).getNameCategory());
        }
    }

    private static void crearCategoria() {
        System.out.println("\n--- CREAR NUEVA CATEGORÍA ---");
        System.out.print("Nombre de la nueva categoría: ");
        String nombre = scanner.nextLine();

        if (nombre.trim().isEmpty()) {
            System.out.println("[!] El nombre no puede estar vacío.");
            return;
        }

        // Comprobamos si ya existe (ignorando mayúsculas/minúsculas)
        for (Category c : Application.getGlobalCategories()) {
            if (c.getNameCategory().equalsIgnoreCase(nombre)) {
                System.out.println("[!] Ya existe una categoría con ese nombre.");
                return;
            }
        }

        // Creamos la categoría (pasando una lista vacía de items como pide tu constructor)
        Category nuevaCategoria = new Category(nombre, new ArrayList<Item>());
        Application.addCategory(nuevaCategoria);

        System.out.println("[+] Categoría '" + nombre + "' creada con éxito.");
    }
}