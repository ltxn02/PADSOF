package ui;
import utils.*;
import users.*;
import catalog.*;
import transactions.*;
import logic.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import discounts.*;

public class main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("      BIENVENIDO A RONGERO           ");
        System.out.println("=====================================");

        logic.Application.cargarDatos("rongero_data.dat");

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
                    // --- Guarda los datos antes de salir ---
                    logic.Application.guardarDatos("rongero_data.dat");
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
                MenuGestor.mostrarMenu((Manager) user, scanner);
            } else if (user instanceof Client) {
                System.out.println(">> ¡Bienvenido, Cliente " + user.getUsername() + "!");
                menuCliente((Client) user);
            } else if (user instanceof Employee) {
                System.out.println(">> ¡Bienvenido, Empleado " + user.getUsername() + "!");
                MenuEmpleado.mostrarMenu((Employee) user, scanner);
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
        System.out.println("\nA.- Ofertas realizadas | B.- Ofertas recibidas | C.- Mis Productos | D.- Subir Producto| 0.- Volver");
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
                    selectedOffer.imprimirCliente();
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
                    if (selectedOffer2.getEstado() == ExchangeOfferStatus.PENDIENTE){
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
                                productosactivos.get(index8 - 1).imprimirCliente();
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
                                productosinactivos.get(index9 - 1).imprimirCliente();

                            }
                            break;
                    }
                    break;
                case "D":
                    subirProductoSegundaMano(c);
                    break;
                default:
                    int index = Integer.parseInt(inputIndex);
                    if (index >= 1 && index <= productos.size()) {
                        SecondHandProduct select = productos.get(index -1);
                        System.out.println("Detalles: \n" );
                        System.out.println(select);

                        System.out.println("\n¿Deseas proponer un intercambio por este producto?");
                        System.out.println("1.- Sí, hacer oferta");
                        System.out.println("0.- Volver");
                        System.out.print("Elige una opción: ");
                        String hacerOferta = scanner.nextLine();

                        if (hacerOferta.equals("1")) {
                            if (productosactivos.isEmpty()) {
                                System.out.println("[!] No tienes productos propios que estén tasados para poder ofrecer a cambio.");
                            } else {
                                System.out.println("\nTus productos disponibles para ofrecer:");
                                for (int k = 0; k < productosactivos.size(); k++) {
                                    SecondHandProduct miProd = productosactivos.get(k);
                                    System.out.println((k + 1) + ".- " + miProd.getName() + " (Tasado en: " + miProd.getPrice() + "€)");
                                }
                                System.out.print("Elige el número de tu producto que quieres ofrecer (0 para cancelar): ");
                                int miProdIndex = Integer.parseInt(scanner.nextLine());

                                if (miProdIndex > 0 && miProdIndex <= productosactivos.size()) {
                                    SecondHandProduct miProductoOfrecido = productosactivos.get(miProdIndex - 1);
                                    try {
                                        // 1. Llamamos al metodo makeOffer del cliente
                                        c.makeOffer(select, miProductoOfrecido);

                                        // 2. PARCHE: Nos aseguramos de que el receptor reciba la oferta en su lista
                                        // (Por si no estaba enlazado en el constructor interno de ExchangeOffer)
                                        List<transactions.ExchangeOffer> misOfertas = c.getOffersMade();
                                        transactions.ExchangeOffer ultimaOferta = misOfertas.get(misOfertas.size() - 1);
                                        select.getOwner().receiveOffer(ultimaOferta);

                                        System.out.println("[+] ¡Oferta enviada con éxito a " + select.getOwner().getUsername() + "!");
                                    } catch (Exception e) {
                                        System.out.println("[!] Error al hacer la oferta: " + e.getMessage());
                                    }
                                }
                            }
                        }

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

        ArrayList<NewProduct> recomendados = SistemaRecomendaciones.obtenerRecomendaciones(c, catalogo, listaClientes);

        if (recomendados.isEmpty()) {
            System.out.println("Aún no tenemos suficientes datos para darte recomendaciones.");
            System.out.println("¡Prueba a comprar y valorar productos primero!");
            return;
        }

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

    /**
     * Metodo para mostrar el carrito y procesar el pago con la librería del profesor
     */
    /**
     * Metodo para mostrar el carrito y procesar el pago con la librería del profesor
     */
    private static void verCarrito(Client cliente) {
        System.out.println(">> Mostrando carrito y procesando pago...");

        // --- 1. INYECTAR DESCUENTOS GLOBALES (REGALOS Y BONOS) ---
        // Recuperamos los descuentos activos de la App y se los pasamos al carrito
        for (IDiscount descuentoActivo : Application.getGlobalDiscounts()) {
            if (descuentoActivo instanceof discounts.IVolumen) {
                cliente.getShoppingCart().addGlobalDiscount((discounts.IVolumen) descuentoActivo);
            }
        }

        // 2. Mostrar el ticket (Aquí es donde el ShoppingCart calcula si te toca regalo y lo imprime a 0,00€)
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
            cliente.getShoppingCart().clearCart();
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
}