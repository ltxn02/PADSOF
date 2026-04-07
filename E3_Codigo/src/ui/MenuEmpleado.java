package ui;

import users.*;
import catalog.*;
import transactions.*;
import logic.*;
import discounts.*;
import utils.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuEmpleado {

    public static void mostrarMenu(Employee empleado, Scanner scanner) {
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
                    gestionarInventario(empleado, scanner);
                    break;
                case "2":
                    gestionarPedidos(empleado, scanner);
                    break;
                case "3":
                    valorarProductosSegundaMano(empleado, scanner);
                    break;
                case "4":
                    confirmarIntercambios(empleado, scanner);
                    break;
                case "0":
                    cerrarSesion = true;
                    System.out.println("Cerrando sesión de empleado...");
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

    // --- SUBMENÚ: GESTIÓN DE INVENTARIO ---
    private static void gestionarInventario(Employee empleado, Scanner scanner) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE INVENTARIO ---");
            System.out.println("1.- Subida manual de producto");
            System.out.println("2.- Subida masiva (Leer archivo CSV/TXT)");
            System.out.println("0.- Volver al panel de empleado");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    subidaManual(empleado, scanner);
                    break;
                case "2":
                    subidaMasiva(empleado, scanner);
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void subidaManual(Employee empleado, Scanner scanner) {
        System.out.println("\n--- SUBIDA MANUAL DE PRODUCTO ---");
        try {
            java.util.Map<String, Object> data = new java.util.HashMap<>();

            System.out.print("Nombre: "); data.put("name", scanner.nextLine());
            System.out.print("Descripción: "); data.put("description", scanner.nextLine());

            System.out.print("Precio: "); data.put("price", Double.parseDouble(scanner.nextLine()));
            System.out.print("Stock inicial: "); data.put("stock", Integer.parseInt(scanner.nextLine()));
            System.out.print("Ruta Imagen (ej: img/test.jpg): "); data.put("picturePath", scanner.nextLine());

            System.out.println("Tipo (1: COMIC, 2: GAME, 3: FIGURINE): ");
            String tipo = scanner.nextLine();
            utils.ItemType itemType = null;

            if (tipo.equals("1")) {
                itemType = utils.ItemType.COMIC;
                System.out.print("Nº de Páginas: "); data.put("nPages", Integer.parseInt(scanner.nextLine()));
                System.out.print("Editorial: "); data.put("publisher", scanner.nextLine());
                System.out.print("Año Publicación: "); data.put("publicationYear", Integer.parseInt(scanner.nextLine()));
                System.out.print("Autores (separados por comas): ");
                data.put("writtenBy", new ArrayList<>(java.util.Arrays.asList(scanner.nextLine().split(","))));

            } else if (tipo.equals("2")) {
                itemType = utils.ItemType.GAME;
                System.out.print("Nº de Jugadores: "); data.put("nPlayers", Integer.parseInt(scanner.nextLine()));
                System.out.print("Mecánicas (separadas por comas): ");
                data.put("mechanics", new ArrayList<>(java.util.Arrays.asList(scanner.nextLine().split(","))));
                System.out.print("Rango de edad (ej: 10-99): ");
                String[] edades = scanner.nextLine().split("-");
                data.put("ageRange", new utils.AgeRange(Integer.parseInt(edades[0]), Integer.parseInt(edades[1])));

            } else if (tipo.equals("3")) {
                itemType = utils.ItemType.FIGURINE;
                System.out.print("Altura (cm): "); data.put("height", Double.parseDouble(scanner.nextLine()));
                System.out.print("Anchura (cm): "); data.put("width", Double.parseDouble(scanner.nextLine()));
                System.out.print("Profundidad (cm): "); data.put("depth", Double.parseDouble(scanner.nextLine()));
                System.out.print("Material: "); data.put("material", scanner.nextLine());
                System.out.print("Franquicia: "); data.put("franchise", scanner.nextLine());
            } else {
                System.out.println("[!] Tipo no válido.");
                return;
            }

            ArrayList<Category> defaultCategories = new ArrayList<>();
            defaultCategories.add(new Category(itemType.toString(), new ArrayList<>()));
            data.put("categories", defaultCategories);

            Catalog catalogoVirtual = new Catalog(
                    Application.getGlobalCategories(),
                    new ArrayList<>(),
                    Application.getCatalog()
            );

            empleado.loadProduct(catalogoVirtual, itemType, data);
            System.out.println("[+] Producto añadido al catálogo con éxito.");

        } catch (NumberFormatException e) {
            System.out.println("[!] Error: Has introducido texto donde se esperaba un número.");
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("[!] Error inesperado: " + e.getMessage());
        }
    }

    private static void subidaMasiva(Employee empleado, Scanner scanner) {
        System.out.println("\n--- SUBIDA MASIVA DE PRODUCTOS ---");
        System.out.print("Ruta del archivo (Pulsa Enter para usar 'fileLoadBulkTest'): ");
        String ruta = scanner.nextLine();
        if (ruta.trim().isEmpty()) ruta = "fileLoadBulkTest";

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(ruta))) {
            String linea;
            int procesados = 0;

            Catalog catalogoVirtual = new Catalog(Application.getGlobalCategories(), new ArrayList<>(), Application.getCatalog());

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length < 6) continue;

                utils.ItemType type = utils.ItemType.valueOf(partes[0].trim().toUpperCase());

                java.util.Map<String, Object> data = new java.util.HashMap<>();
                data.put("name", partes[1]);
                data.put("description", partes[2]);
                data.put("price", Double.parseDouble(partes[3]));
                data.put("stock", Integer.parseInt(partes[4]));
                data.put("picturePath", partes[5]);

                if (type == utils.ItemType.COMIC) {
                    data.put("nPages", Integer.parseInt(partes[6]));
                    data.put("publisher", partes[7]);
                    data.put("publicationYear", Integer.parseInt(partes[8]));
                    data.put("writtenBy", new ArrayList<>(java.util.Arrays.asList(partes[9].split(","))));

                } else if (type == utils.ItemType.GAME) {
                    data.put("nPlayers", Integer.parseInt(partes[6]));
                    data.put("mechanics", new ArrayList<>(java.util.Arrays.asList(partes[7].split(","))));
                    String[] edades = partes[8].split("-");
                    data.put("ageRange", new utils.AgeRange(Integer.parseInt(edades[0]), Integer.parseInt(edades[1])));

                } else if (type == utils.ItemType.FIGURINE) {
                    data.put("height", Double.parseDouble(partes[6]));
                    data.put("width", Double.parseDouble(partes[7]));
                    data.put("depth", Double.parseDouble(partes[8]));
                    data.put("material", partes[9]);
                    data.put("franchise", partes[10]);

                } else if (type == utils.ItemType.PACK) {
                    data.put("products", catalogoVirtual.packProducts(partes[6]));
                }

                ArrayList<Category> defaultCategories = new ArrayList<>();
                defaultCategories.add(new Category(type.toString(), new ArrayList<>()));
                data.put("categories", defaultCategories);

                empleado.loadProduct(catalogoVirtual, type, data);
                procesados++;
            }
            System.out.println("[+] ¡Subida masiva completada! Se han cargado " + procesados + " productos mediante Catalog.");

        } catch (java.io.FileNotFoundException e) {
            System.out.println("[!] No se ha encontrado el archivo: " + ruta);
        } catch (Exception e) {
            System.out.println("[!] Error leyendo el archivo: " + e.getMessage());
        }
    }

    // --- SUBMENÚ: GESTIÓN DE PEDIDOS ---
    private static void gestionarPedidos(Employee empleado, Scanner scanner) {
        System.out.println("\n--- GESTIÓN DE ESTADO DE PEDIDOS ---");

        ArrayList<Order> todosLosPedidos = new ArrayList<>();
        for (RegisteredUser u : Application.getUsers()) {
            if (u instanceof Client) {
                Client c = (Client) u;
                if (c.getOrderHistoric() != null && c.getOrderHistoric().getOrders() != null) {
                    todosLosPedidos.addAll(c.getOrderHistoric().getOrders());
                }
            }
        }

        if (todosLosPedidos.isEmpty()) {
            System.out.println("No hay ningún pedido registrado en el sistema actualmente.");
            return;
        }

        for (int i = 0; i < todosLosPedidos.size(); i++) {
            Order o = todosLosPedidos.get(i);
            System.out.println((i + 1) + ".- " + o.orderPreview());
        }

        System.out.print("\nElige el número del pedido que deseas actualizar (o 0 para volver): ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index == 0) return;
            if (index < 1 || index > todosLosPedidos.size()) {
                System.out.println("[!] Opción no válida.");
                return;
            }

            Order pedidoSeleccionado = todosLosPedidos.get(index - 1);

            System.out.println("\nEl estado actual es: " + pedidoSeleccionado.getOrderStatus());
            System.out.println("¿A qué estado deseas cambiarlo?");

            utils.OrderStatus[] estados = utils.OrderStatus.values();
            for (int i = 0; i < estados.length; i++) {
                System.out.println((i + 1) + ".- " + estados[i].name());
            }

            System.out.print("Elige el número del nuevo estado: ");
            int estadoIndex = Integer.parseInt(scanner.nextLine());
            if (estadoIndex < 1 || estadoIndex > estados.length) {
                System.out.println("[!] Opción no válida.");
                return;
            }

            utils.OrderStatus nuevoEstado = estados[estadoIndex - 1];

            empleado.updateOrderStatus(pedidoSeleccionado, nuevoEstado);
            System.out.println("[+] ¡Éxito! El pedido ha pasado al estado: " + nuevoEstado.name());

        } catch (NumberFormatException e) {
            System.out.println("[!] Entrada inválida. Introduce un número.");
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    // --- SUBMENÚ: VALORAR PRODUCTOS SEGUNDA MANO ---
    private static void valorarProductosSegundaMano(Employee empleado, Scanner scanner) {
        System.out.println("\n--- VALORAR PRODUCTOS DE SEGUNDA MANO ---");

        ArrayList<SecondHandProduct> todos = logic.Application.getSecondHandProducts();
        ArrayList<SecondHandProduct> pendientes = new ArrayList<>();

        for (SecondHandProduct p : todos) {
            if (!p.isAppraised()) {
                pendientes.add(p);
            }
        }

        if (pendientes.isEmpty()) {
            System.out.println("¡Genial! No hay productos de segunda mano pendientes de valoración.");
            return;
        }

        for (int i = 0; i < pendientes.size(); i++) {
            SecondHandProduct p = pendientes.get(i);
            System.out.println((i + 1) + ".- " + p.getName() + " | Subido por: " + p.getOwner().getUsername());
            System.out.println("    Descripción del cliente: " + p.itemPreview());
        }

        System.out.print("\nElige el número del producto a valorar (o 0 para volver): ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index == 0) return;
            if (index < 1 || index > pendientes.size()) {
                System.out.println("[!] Opción no válida.");
                return;
            }

            SecondHandProduct seleccionado = pendientes.get(index - 1);

            System.out.println("\nSelecciona el estado de conservación real del producto:");
            utils.Condition[] condiciones = utils.Condition.values();
            for (int i = 0; i < condiciones.length; i++) {
                System.out.println((i + 1) + ".- " + condiciones[i].name());
            }

            System.out.print("Elige el número de la condición: ");
            int condIndex = Integer.parseInt(scanner.nextLine());
            if (condIndex < 1 || condIndex > condiciones.length) {
                System.out.println("[!] Opción no válida.");
                return;
            }
            utils.Condition condicion = condiciones[condIndex - 1];

            System.out.print("Introduce el precio de tasación en tienda (ej: 15.50): ");
            double precio = Double.parseDouble(scanner.nextLine());

            empleado.appraiseSecondHandProduct(seleccionado.getOwner(), seleccionado, condicion, precio);
            System.out.println("[+] ¡Éxito! Producto '" + seleccionado.getName() + "' tasado oficialmente en " + precio + "€ con estado " + condicion.name() + ".");

        } catch (NumberFormatException e) {
            System.out.println("[!] Entrada inválida. Introduce un número correcto.");
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    // --- SUBMENÚ: CONFIRMAR INTERCAMBIOS FÍSICOS ---
    private static void confirmarIntercambios(Employee empleado, Scanner scanner) {
        System.out.println("\n--- CONFIRMAR INTERCAMBIOS FÍSICOS ---");

        ArrayList<transactions.Exchange> todosLosIntercambios = new ArrayList<>();

        for (users.RegisteredUser u : logic.Application.getUsers()) {
            if (u instanceof users.Client) {
                users.Client c = (users.Client) u;
                if (c.getExchangeHistoric() != null && c.getExchangeHistoric().getExchanges() != null) {
                    for (transactions.Exchange ex : c.getExchangeHistoric().getExchanges()) {
                        if (!todosLosIntercambios.contains(ex)) {
                            todosLosIntercambios.add(ex);
                        }
                    }
                }
            }
        }

        ArrayList<transactions.Exchange> pendientes = new ArrayList<>();
        for (transactions.Exchange ex : todosLosIntercambios) {
            if (ex.isExchange(ExchangeStatus.EN_PROCESO)) {
                pendientes.add(ex);
            }
        }

        if (pendientes.isEmpty()) {
            System.out.println("No hay intercambios físicos pendientes de validación en la tienda.");
            return;
        }

        for (int i = 0; i < pendientes.size(); i++) {
            transactions.Exchange ex = pendientes.get(i);
            transactions.ExchangeOffer oferta = ex.getAssociatedOffer();

            System.out.println((i + 1) + ".- Intercambio #" + ex.getExchangeId() + " | De: "
                    + oferta.getOfferor().getUsername() + " para: " + oferta.getReceptor().getUsername());
            System.out.println("    -> Producto implicado: " + oferta.getRequestedProduct().getName());
        }

        System.out.print("\nElige el número del intercambio que los clientes han traído a la tienda (o 0 para volver): ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index == 0) return;
            if (index < 1 || index > pendientes.size()) {
                System.out.println("[!] Opción no válida.");
                return;
            }

            transactions.Exchange seleccionado = pendientes.get(index - 1);

            empleado.validateExchange(seleccionado);

            System.out.println("[+] ¡Éxito! El intercambio #" + seleccionado.getExchangeId() + " ha sido validado en tienda.");
            System.out.println("    Los productos han cambiado de propietario oficialmente en el sistema.");

        } catch (NumberFormatException e) {
            System.out.println("[!] Entrada inválida. Introduce un número correcto.");
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("[!] No se puede validar: " + e.getMessage());
        }
    }
}