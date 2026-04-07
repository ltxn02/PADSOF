package ui;

import users.*;
import catalog.*;
import transactions.*;
import logic.*;
import utils.*;
import discounts.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase que encapsula la interfaz de usuario por consola para el rol de Cliente (Client).
 * Permite a los usuarios registrados navegar por el catálogo completo, realizar compras
 * procesando pagos mediante carrito, proponer e interactuar con ofertas de segunda mano,
 * consultar sus notificaciones y recibir recomendaciones de productos personalizadas.
 *
 * @author Iván Sánchez
 * @version 1.0
 */
public class MenuCliente {

    /**
     * Muestra el panel personal del cliente y gestiona el bucle principal de compra e interacción.
     * * @param cliente El usuario cliente que ha iniciado sesión.
     * @param scanner Objeto Scanner compartido para leer las entradas del teclado.
     */
    public static void mostrarMenu(Client cliente, Scanner scanner) {
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
                    comprarProducto(cliente, scanner);
                    break;
                case "2":
                    System.out.println(">> Mostrando carrito y procesando pago...");
                    verCarrito(cliente, scanner);
                    break;
                case "3":
                    System.out.println("Productos de Segunda mano:");
                    intercambiarProductos(cliente, scanner);
                    break;
                case "4":
                    gestionarCartera(cliente, scanner);
                    break;
                case "5":
                    verNotificaciones(cliente, scanner);
                    break;
                case "6":
                    mostrarRecomendaciones(cliente, scanner);
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
     * Muestra el catálogo completo de productos nuevos y permite al cliente
     * seleccionar artículos y cantidades para añadirlos a su carrito de compras.
     * * @param cliente El cliente que realiza la compra.
     * @param scanner Objeto Scanner para la entrada de datos.
     */
    private static void comprarProducto(Client cliente, Scanner scanner) {
        System.out.println("\n--- CATÁLOGO DE PRODUCTOS ---");

        ArrayList<NewProduct> productos = Application.getCatalog();

        if (productos.isEmpty()) {
            System.out.println("Actualmente no hay productos en la tienda.");
            return;
        }

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
                return;
            }

            if (index < 1 || index > productos.size()) {
                System.out.println("[!] Opción no válida. Producto no encontrado.");
                return;
            }

            NewProduct selectedProduct = productos.get(index - 1);

            System.out.print("¿Cuántas unidades de '" + selectedProduct.getName() + "' deseas añadir? ");
            String inputQty = scanner.nextLine();
            int quantity = Integer.parseInt(inputQty);

            if (quantity <= 0) {
                System.out.println("[!] La cantidad debe ser mayor que 0.");
                return;
            }

            cliente.addToCart(selectedProduct, quantity);
            System.out.println("[+] ¡Éxito! " + quantity + "x " + selectedProduct.getName() + " añadido(s) a tu carrito.");

        } catch (NumberFormatException e) {
            System.out.println("[!] Error: Por favor, introduce un número válido.");
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Error de stock: " + e.getMessage());
        }
    }

    /**
     * Gestiona la interfaz completa de la Tienda de Intercambios.
     * Permite visualizar productos de otros usuarios, hacer propuestas de intercambio,
     * responder a ofertas recibidas (Aceptar/Rechazar) y revisar el estado de los productos propios.
     * * @param c       El cliente que interactúa con la tienda de intercambios.
     * @param scanner Objeto Scanner para la entrada de datos.
     */
    private static void intercambiarProductos(Client c, Scanner scanner) {
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
                    }
                }
            }
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
                            }
                        } else {
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
                        subirProductoSegundaMano(c, scanner);
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
                                            c.makeOffer(select, miProductoOfrecido);

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

    /**
     * Metodo auxiliar que extrae y devuelve la lista de todos los usuarios registrados
     * en la aplicación que tienen el rol específico de Cliente.
     * * @return Lista de objetos {@link Client}.
     */
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

    /**
     * Interactúa con el Sistema de Recomendaciones para sugerir productos afines
     * al cliente basados en su historial y el de clientes similares. Permite añadir
     * dichas recomendaciones al carrito de compra al instante.
     * * @param c       El cliente que solicita las recomendaciones.
     * @param scanner Objeto Scanner para la entrada de datos.
     */
    private static void mostrarRecomendaciones(Client c, Scanner scanner) {
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
     * Muestra el resumen del carrito, aplica automáticamente los descuentos globales
     * por volumen y procesa el pago a través de la pasarela simulada usando una tarjeta.
     * * @param cliente El cliente propietario del carrito.
     * @param scanner Objeto Scanner para la entrada de los datos de pago.
     */
    private static void verCarrito(Client cliente, Scanner scanner) {
        System.out.println(">> Mostrando carrito y procesando pago...");

        for (IDiscount descuentoActivo : Application.getGlobalDiscounts()) {
            if (descuentoActivo instanceof discounts.IVolumen) {
                cliente.getShoppingCart().addGlobalDiscount((discounts.IVolumen) descuentoActivo);
            }
        }

        System.out.println(cliente.viewShoppingCart());

        System.out.print("\n¿Deseas finalizar la compra y pagar ahora? (S/N): ");
        String respuesta = scanner.nextLine();

        if (!respuesta.equalsIgnoreCase("S")) {
            System.out.println("Compra aplazada. Los productos siguen en tu carrito.");
            return;
        }

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
     * Menú intermedio para que el usuario gestione sus artículos de segunda mano,
     * pudiendo consultar el estado de su cartera y subir nuevos productos al sistema.
     * * @param cliente El cliente que visualiza su cartera.
     * @param scanner Objeto Scanner para la entrada de datos.
     */
    private static void gestionarCartera(Client cliente, Scanner scanner) {
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
                    subirProductoSegundaMano(cliente, scanner);
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

    /**
     * Recoge interactivamente los datos de un artículo físico propiedad del cliente
     * para registrarlo en el sistema. El producto quedará a la espera de tasación por un empleado.
     * * @param cliente El cliente que quiere vender o intercambiar su artículo.
     * @param scanner Objeto Scanner para la introducción de datos.
     */
    private static void subirProductoSegundaMano(Client cliente, Scanner scanner) {
        System.out.println("\n--- SUBIR NUEVO PRODUCTO ---");

        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción breve: ");
        String descripcion = scanner.nextLine();

        System.out.print("Ruta de la foto (ej. img/manga.jpg): ");
        String foto = scanner.nextLine();

        System.out.println("Tipo de producto (1. COMIC, 2. GAME, 3. FIGURINE): ");
        String tipoInput = scanner.nextLine();
        ItemType tipo = ItemType.COMIC;
        if (tipoInput.equals("2")) tipo = ItemType.GAME;
        if (tipoInput.equals("3")) tipo = ItemType.FIGURINE;

        System.out.println("Estado de conservación (1. PERFECTO, 2. MUY_BUENO, 3. USO_LIGERO, 4. USO_EVIDENTE, 5. DAÑADO): ");
        String condInput = scanner.nextLine();
        Condition condicion = Condition.PERFECTO;
        switch (condInput) {
            case "2": condicion = Condition.MUY_BUENO; break;
            case "3": condicion = Condition.USO_LIGERO; break;
            case "4": condicion = Condition.USO_EVIDENTE; break;
            case "5": condicion = Condition.DAÑADO; break;
        }

        SecondHandProduct nuevoProducto = new SecondHandProduct(
                nombre, descripcion, foto, 0.0, false, tipo, condicion, cliente
        );

        cliente.registerSecondHandProduct(nuevoProducto);
        Application.addSecondHandProduct(nuevoProducto);

        System.out.println("\n[+] Producto subido con éxito. Un empleado lo tasará pronto.");
    }

    /**
     * Muestra la bandeja de entrada del cliente, presentando un listado de alertas
     * (previews) y permitiéndole seleccionar una para leer el mensaje completo.
     * * @param cliente El cliente cuyas notificaciones se consultan.
     * @param scanner Objeto Scanner para navegar por la bandeja de entrada.
     */
    private static void verNotificaciones(Client cliente, Scanner scanner) {
        System.out.println("\n--- MIS NOTIFICACIONES ---");

        System.out.print(cliente.view_notifications());

        List<Notification> notificaciones = cliente.getMyNotifications();

        if (notificaciones == null || notificaciones.isEmpty()) {
            System.out.println("Tu bandeja de entrada está vacía.");
            return;
        }

        System.out.println("\n----------------------------------");
        System.out.print("Elige el número de la notificación que quieres leer (orden de arriba a abajo, 1-" + notificaciones.size() + ") o 0 para salir: ");
        String input = scanner.nextLine();

        try {
            int index = Integer.parseInt(input);

            if (index == 0) {
                return;
            }

            if (index < 1 || index > notificaciones.size()) {
                System.out.println("[!] Opción no válida.");
                return;
            }

            Notification seleccionada = notificaciones.get(index - 1);

            System.out.println("\n[MENSAJE COMPLETO]");
            System.out.println(cliente.read_notification(seleccionada));

            System.out.println("\n(Pulsa Enter para continuar...)");
            scanner.nextLine();

        } catch (NumberFormatException e) {
            System.out.println("[!] Error: Por favor, introduce un número válido.");
        }
    }
}