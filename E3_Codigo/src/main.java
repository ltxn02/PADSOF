import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        ShoppingCart.shutdownCleaner();
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
            cliente.addToCart(selectedProduct, quantity);
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
                case "3":
                    gestionarDescuentos();
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

            c.addToCart(seleccionado, quantity);
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
        System.out.println(cliente.viewShoppingCart());

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
        
        String code = cliente.buyCart(numeroTarjeta);
        
        if(code == null) {
        	System.out.println("[!] La compra no se ha podido completar. Revisa tu método de pago.");
        } else {
        	System.out.println("[+] ¡Compra finalizada con éxito! Tu código de recogida es: " + code);
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
                    System.out.println(cliente.viewMyProducts());
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
        cliente.registerSecondHandProduct(nuevoProducto);
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
            System.out.println("3.- Gestionar Packs");
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
                    gestionarPacks();
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

    // --- SUBMENÚ: GESTIÓN DE PACKS ---

    private static void gestionarPacks() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE PACKS ---");
            System.out.println("1.- Ver Packs creados en la tienda");
            System.out.println("2.- Crear un nuevo Pack");
            System.out.println("0.- Volver al menú anterior");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    verPacks();
                    break;
                case "2":
                    crearPack();
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void verPacks() {
        System.out.println("\n--- PACKS ACTUALES ---");
        boolean hayPacks = false;

        for (NewProduct np : Application.getCatalog()) {
            // Filtramos el catálogo para mostrar solo los que sean instancia de Pack
            if (np instanceof Pack) {
                Pack pack = (Pack) np;
                hayPacks = true;
                System.out.println("- " + pack.getName() + " | Precio especial: " + pack.getPrice() + "€");
                System.out.println("  Contiene: " + pack.getProducts().size() + " productos.");
            }
        }
        if (!hayPacks) {
            System.out.println("Actualmente no hay Packs creados en el sistema.");
        }
    }

    private static void crearPack() {
        System.out.println("\n--- CREAR NUEVO PACK ---");
        ArrayList<NewProduct> catalogo = Application.getCatalog();

        // Filtramos para que solo salgan productos individuales (evitamos meter packs dentro de packs para no liar al usuario)
        ArrayList<NewProduct> productosIndividuales = new ArrayList<>();
        for (NewProduct np : catalogo) {
            if (!(np instanceof Pack)) {
                productosIndividuales.add(np);
            }
        }

        if (productosIndividuales.size() < 2) {
            System.out.println("[!] No hay suficientes productos en la tienda para crear un pack. Se necesitan al menos 2.");
            return;
        }

        ArrayList<NewProduct> productosDelPack = new ArrayList<>();
        boolean seleccionando = true;

        // Bucle para añadir productos al Pack
        while (seleccionando) {
            System.out.println("\nProductos disponibles para añadir:");
            for (int i = 0; i < productosIndividuales.size(); i++) {
                System.out.println((i + 1) + ".- " + productosIndividuales.get(i).getName() + " (" + productosIndividuales.get(i).getPrice() + "€)");
            }

            System.out.println("\nProductos en el pack actual: " + productosDelPack.size());
            System.out.print("Elige el número del producto a añadir (o 0 para terminar la selección): ");

            try {
                int index = Integer.parseInt(scanner.nextLine());
                if (index == 0) {
                    if (productosDelPack.size() >= 2) {
                        seleccionando = false; // Salimos del bucle si ya tiene al menos 2
                    } else {
                        System.out.println("[!] Recuerda la regla de negocio: Un pack necesita contener al menos 2 productos.");
                    }
                } else if (index > 0 && index <= productosIndividuales.size()) {
                    NewProduct seleccionado = productosIndividuales.get(index - 1);
                    if (!productosDelPack.contains(seleccionado)) {
                        productosDelPack.add(seleccionado);
                        System.out.println("[+] '" + seleccionado.getName() + "' añadido al pack.");
                    } else {
                        System.out.println("[!] Ese producto ya está dentro del pack.");
                    }
                } else {
                    System.out.println("[!] Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Introduce un número válido.");
            }
        }

        // Una vez elegidos los productos, pedimos los datos generales del Pack
        System.out.print("\nNombre del Pack (ej. Pack Especial Anime): ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Precio total rebajado del Pack (ej: 40.50): ");
        double precio = Double.parseDouble(scanner.nextLine());
        System.out.print("Unidades de este pack en stock (ej: 10): ");
        int stock = Integer.parseInt(scanner.nextLine());
        System.out.print("Ruta de la imagen (ej: img/pack.jpg): ");
        String image = scanner.nextLine();

        // El programa recopila automáticamente las categorías de los productos interiores para asignárselas al Pack
        ArrayList<Category> categoriasPack = new ArrayList<>();
        for (NewProduct np : productosDelPack) {
            for (Category c : np.getCategories()) {
                if (!categoriasPack.contains(c)) {
                    categoriasPack.add(c);
                }
            }
        }

        try {
            // Usamos el constructor de Pack que subiste en tus archivos
            Pack nuevoPack = new Pack(nombre, descripcion, precio, image, stock, categoriasPack, new ArrayList<Review>(), productosDelPack);
            Application.getCatalog().add(nuevoPack);
            System.out.println("[+] ¡Éxito! Pack '" + nombre + "' creado y subido al catálogo de la tienda.");
        } catch (Exception e) {
            System.out.println("[!] Error al crear el pack: " + e.getMessage());
        }
    }

    // --- SUBMENÚ: GESTIÓN DE DESCUENTOS ---
    private static void gestionarDescuentos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE DESCUENTOS ---");
            System.out.println("1.- Ver descuentos creados");
            System.out.println("2.- Crear un nuevo descuento");
            System.out.println("3.- Asignar descuento a un producto");
            System.out.println("0.- Volver al menú anterior");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    verDescuentos();
                    break;
                case "2":
                    crearDescuento();
                    break;
                case "3":
                    asignarDescuento();
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void verDescuentos() {
        System.out.println("\n--- DESCUENTOS ACTUALES ---");
        ArrayList<Discount> descuentos = Application.getGlobalDiscounts();

        if (descuentos.isEmpty()) {
            System.out.println("No hay descuentos registrados en el sistema.");
            return;
        }

        for (int i = 0; i < descuentos.size(); i++) {
            Discount d = descuentos.get(i);
            // Multiplicamos por 100 y lo pasamos a entero para que 0.2 se vea como "20"
            int porcentajeReal = (int) (d.getPercentage() * 100);
            System.out.println((i + 1) + ".- [" + d.getType() + "] " + porcentajeReal + "% de rebaja | " + d.getDescription());
        }
    }

    private static void crearDescuento() {
        System.out.println("\n--- CREAR NUEVO DESCUENTO ---");

        System.out.print("Porcentaje de descuento (ej: 0.20 para 20%): ");
        double percentage;
        try {
            percentage = Double.parseDouble(scanner.nextLine());
            if (percentage < 0 || percentage > 1) {
                System.out.println("[!] El porcentaje debe estar entre 0.0 y 1.0");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("[!] Error: Introduce un número válido.");
            return;
        }

        System.out.print("Tipo de descuento (ej: REBAJA, 2X1, EMPLEADO): ");
        String type = scanner.nextLine();

        System.out.print("Descripción breve: ");
        String description = scanner.nextLine();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // Para que no invente fechas raras como 32/13/2024

        Date fromDate = null;
        Date toDate = null;

        try {
            System.out.print("Fecha de inicio (DD/MM/YYYY): ");
            fromDate = sdf.parse(scanner.nextLine());

            System.out.print("Fecha de fin (DD/MM/YYYY): ");
            toDate = sdf.parse(scanner.nextLine());

            if (toDate.before(fromDate)) {
                System.out.println("[!] La fecha de fin no puede ser anterior a la fecha de inicio.");
                return;
            }

            // Usamos el constructor de tu clase Discount
            Discount nuevoDescuento = new Discount(percentage, type, description, fromDate, toDate);
            Application.addDiscount(nuevoDescuento);
            System.out.println("[+] ¡Descuento creado y guardado en el sistema!");

        } catch (Exception e) {
            System.out.println("[!] Error al procesar las fechas. Usa el formato DD/MM/YYYY.");
        }
    }

    private static void asignarDescuento() {
        ArrayList<Discount> descuentos = Application.getGlobalDiscounts();
        if (descuentos.isEmpty()) {
            System.out.println("[!] Primero debes crear un descuento en la opción 2.");
            return;
        }

        // 1. Mostrar productos disponibles (solo individuales, no Packs)
        ArrayList<Product> productosIndividuales = new ArrayList<>();
        for (NewProduct np : Application.getCatalog()) {
            if (np instanceof Product) {
                productosIndividuales.add((Product) np);
            }
        }

        if (productosIndividuales.isEmpty()) {
            System.out.println("[!] No hay productos individuales en el catálogo para asignar descuentos.");
            return;
        }

        System.out.println("\n--- ASIGNAR DESCUENTO ---");
        System.out.println("Elige el producto al que quieres aplicar el descuento:");
        for (int i = 0; i < productosIndividuales.size(); i++) {
            Product p = productosIndividuales.get(i);
            String tieneDescuento = (p.getDiscount() != null) ? " (Ya tiene descuento)" : "";
            System.out.println((i + 1) + ".- " + p.getName() + " - " + p.getPrice() + "€" + tieneDescuento);
        }

        try {
            System.out.print("Número del producto (0 para cancelar): ");
            int indexProd = Integer.parseInt(scanner.nextLine());
            if (indexProd == 0) return;
            if (indexProd < 1 || indexProd > productosIndividuales.size()) {
                System.out.println("[!] Opción no válida.");
                return;
            }
            Product prodSeleccionado = productosIndividuales.get(indexProd - 1);

            // 2. Mostrar descuentos disponibles con el nuevo formato
            System.out.println("\nDescuentos disponibles:");
            for (int i = 0; i < descuentos.size(); i++) {
                Discount d = descuentos.get(i);
                int porcentajeReal = (int) (d.getPercentage() * 100);
                System.out.println((i + 1) + ".- [" + d.getType() + "] " + porcentajeReal + "% - " + d.getDescription());
            }

            System.out.print("Número del descuento a aplicar (0 para cancelar): ");
            int indexDesc = Integer.parseInt(scanner.nextLine());
            if (indexDesc == 0) return;
            if (indexDesc < 1 || indexDesc > descuentos.size()) {
                System.out.println("[!] Opción no válida.");
                return;
            }

            Discount descSeleccionado = descuentos.get(indexDesc - 1);

            // 3. Asignar usando el setter que acabamos de crear en Product.java
            prodSeleccionado.setDiscount(descSeleccionado);
            System.out.println("[+] ¡Éxito! Descuento [" + descSeleccionado.getType() + "] aplicado a '" + prodSeleccionado.getName() + "'.");

        } catch (NumberFormatException e) {
            System.out.println("[!] Entrada inválida.");
        }
    }
}