package ui;

import users.*;
import catalog.*;
import logic.*;
import discounts.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

public class MenuGestor {

    // Metodo principal de entrada al menú del Gestor
    public static void mostrarMenu(Manager gestor, Scanner scanner) {
        boolean cerrarSesion = false;
        while (!cerrarSesion) {
            System.out.println("\n--- PANEL DE ADMINISTRACIÓN (GESTOR) ---");
            System.out.println("1.- Gestionar empleados (Alta/Baja/Permisos)");
            System.out.println("2.- Gestionar catálogo (categorías y packs)");
            System.out.println("3.- Gestionar descuentos");
            System.out.println("4.- Ver estadísticas de la tienda");
            System.out.println("0.- Cerrar sesión");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    gestionarEmpleados(scanner);
                    break;
                case "2":
                    gestionarCatalogo(scanner);
                    break;
                case "3":
                    gestionarDescuentosMenu(scanner);
                    break;
                case "4":
                    System.out.println(logic.StoreStatistics.generateStoreReport());
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

    // --- SUBMENÚ: GESTIÓN DE EMPLEADOS ---
    private static void gestionarEmpleados(Scanner scanner) {
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
                case "1": altaEmpleado(scanner); break;
                case "2": cambiarEstadoEmpleado(scanner); break;
                case "3": gestionarPermisos(scanner); break;
                case "0": volver = true; break;
                default: System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void altaEmpleado(Scanner scanner) {
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
            Employee nuevoEmpleado = new Employee(username, password, fullname, dni, birthdate, email, phoneNumber, salary, true);
            Application.registerEmployee(nuevoEmpleado);
        } catch (NumberFormatException e) {
            System.out.println("[!] Error: El salario debe ser un número (ej: 1200.50).");
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void cambiarEstadoEmpleado(Scanner scanner) {
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

    private static void gestionarPermisos(Scanner scanner) {
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
            utils.Permission[] todosLosPermisos = utils.Permission.values();
            for (int i = 0; i < todosLosPermisos.length; i++) {
                System.out.println((i + 1) + ".- " + todosLosPermisos[i]);
            }

            System.out.print("Elige el número del permiso: ");
            int indexPermiso = Integer.parseInt(scanner.nextLine());

            if (indexPermiso < 1 || indexPermiso > todosLosPermisos.length) {
                System.out.println("[!] Opción no válida.");
                return;
            }

            utils.Permission permisoSeleccionado = todosLosPermisos[indexPermiso - 1];

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
    private static void gestionarCatalogo(Scanner scanner) {
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
                case "1": verCategorias(); break;
                case "2": crearCategoria(scanner); break;
                case "3": gestionarPacks(scanner); break;
                case "0": volver = true; break;
                default: System.out.println("[!] Opción no válida.");
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

    private static void crearCategoria(Scanner scanner) {
        System.out.println("\n--- CREAR NUEVA CATEGORÍA ---");
        System.out.print("Nombre de la nueva categoría: ");
        String nombre = scanner.nextLine();

        if (nombre.trim().isEmpty()) {
            System.out.println("[!] El nombre no puede estar vacío.");
            return;
        }
        for (Category c : Application.getGlobalCategories()) {
            if (c.getNameCategory().equalsIgnoreCase(nombre)) {
                System.out.println("[!] Ya existe una categoría con ese nombre.");
                return;
            }
        }
        Category nuevaCategoria = new Category(nombre, new ArrayList<Item>());
        Application.addCategory(nuevaCategoria);
        System.out.println("[+] Categoría '" + nombre + "' creada con éxito.");
    }

    private static void gestionarPacks(Scanner scanner) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE PACKS ---");
            System.out.println("1.- Ver Packs creados en la tienda");
            System.out.println("2.- Crear un nuevo Pack");
            System.out.println("0.- Volver al menú anterior");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1": verPacks(); break;
                case "2": crearPack(scanner); break;
                case "0": volver = true; break;
                default: System.out.println("[!] Opción no válida.");
            }
        }
    }

    private static void verPacks() {
        System.out.println("\n--- PACKS ACTUALES ---");
        boolean hayPacks = false;
        for (NewProduct np : Application.getCatalog()) {
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

    private static void crearPack(Scanner scanner) {
        System.out.println("\n--- CREAR NUEVO PACK ---");
        ArrayList<NewProduct> catalogo = Application.getCatalog();
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
                        seleccionando = false;
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

        ArrayList<Category> categoriasPack = new ArrayList<>();
        for (NewProduct np : productosDelPack) {
            for (Category c : np.getCategories()) {
                if (!categoriasPack.contains(c)) {
                    categoriasPack.add(c);
                }
            }
        }

        try {
            Pack nuevoPack = new Pack(nombre, descripcion, precio, image, stock, categoriasPack, new ArrayList<utils.Review>(), productosDelPack);
            Application.getCatalog().add(nuevoPack);
            System.out.println("[+] ¡Éxito! Pack '" + nombre + "' creado y subido al catálogo de la tienda.");
        } catch (Exception e) {
            System.out.println("[!] Error al crear el pack: " + e.getMessage());
        }
    }

    // --- SUBMENÚ: GESTIÓN DE DESCUENTOS ---
    private static void gestionarDescuentosMenu(Scanner scanner) {
        System.out.println("\n--- GESTOR DE PROMOCIONES ---");
        System.out.println("1.- Ver descuentos activos");
        System.out.println("2.- Crear y asignar nueva promoción");
        System.out.println("3.- Asignar descuento existente a producto");
        System.out.println("0.- Volver");
        System.out.print("Selecciona opción: ");

        String opcion = scanner.nextLine();
        switch (opcion) {
            case "1": verDescuentosActivos(); break;
            case "2": menuCreacionDescuento(scanner); break;
            case "3": asignarDescuento(scanner); break;
            case "0": break;
            default: System.out.println("[!] Opción no válida.");
        }
    }

    private static void verDescuentosActivos() {
        System.out.println("\n--- DESCUENTOS EN PRODUCTOS ---");
        for (NewProduct p : Application.getCatalog()) {
            if (p instanceof Product) {
                Product prodConMetodos = (Product) p;
                if (prodConMetodos.getDiscount() != null) {
                    System.out.println("- [" + prodConMetodos.getName() + "]: " + prodConMetodos.getDiscount().getDescription());
                }
            }
        }

        System.out.println("\n--- DESCUENTOS GLOBALES (CARRITO) ---");
        for (IDiscount d : Application.getGlobalDiscounts()) {
            System.out.println("- " + d.getDescription());
        }
    }

    private static void menuCreacionDescuento(Scanner scanner) {
        IDiscountFactory factory = new StandardDiscountFactory();
        System.out.println("\n--- CREAR NUEVA PROMOCIÓN ---");
        System.out.println("1.- Rebaja (%) | 2.- Volumen (€) | 3.- Regalo | 4.- Cantidad (X por Y)");
        System.out.print("Selecciona opción: ");
        String tipo = scanner.nextLine();

        System.out.print("Descripción de la oferta: ");
        String desc = scanner.nextLine();

        try {
            switch (tipo) {
                case "1":
                    System.out.print("Nombre del producto a rebajar: ");
                    NewProduct base = buscarProductoEnCatalogo(scanner.nextLine());
                    if (base instanceof Product) {
                        System.out.print("Porcentaje de rebaja (ej: 20): ");
                        double pct = Double.parseDouble(scanner.nextLine());
                        IRebaja rebaja = factory.createPercentageDiscount(pct, desc);
                        ((Product) base).setDiscount(rebaja);
                        Application.addDiscount((Discount) rebaja);
                        System.out.println("[+] Rebaja aplicada.");
                    } else {
                        System.out.println("[!] Producto no encontrado o es un Pack.");
                    }
                    break;
                case "2":
                    System.out.print("Gasto mínimo en el carrito (€): ");
                    double threshold = Double.parseDouble(scanner.nextLine());
                    System.out.print("Euros a descontar del total (€): ");
                    double amount = Double.parseDouble(scanner.nextLine());

                    IVolumen vol = factory.createVolumeDiscount(threshold, amount, desc);
                    Application.addDiscount((Discount) vol);
                    System.out.println("[+] Descuento de volumen añadido.");
                    break;
                case "3":
                    System.out.print("Gasto mínimo para activar el regalo (€): ");
                    double min = Double.parseDouble(scanner.nextLine());
                    System.out.print("Nombre del producto que se regala: ");
                    NewProduct pRegalo = buscarProductoEnCatalogo(scanner.nextLine());

                    if (pRegalo != null) {
                        IRegalo gift = factory.createGiftDiscount(min, pRegalo, desc);
                        Application.addDiscount((Discount) gift);
                        System.out.println("[+] Promoción de regalo añadida.");
                    } else {
                        System.out.println("[!] El producto para regalo no existe.");
                    }
                    break;
                case "4":
                    System.out.print("Nombre del producto: ");
                    NewProduct pCant = buscarProductoEnCatalogo(scanner.nextLine());
                    if (pCant instanceof Product) {
                        System.out.print("Lleva (X): ");
                        int x = Integer.parseInt(scanner.nextLine());
                        System.out.print("Paga (Y): ");
                        int y = Integer.parseInt(scanner.nextLine());

                        ICantidad promo = factory.createQuantityDiscount(x, y, desc);
                        ((Product) pCant).setDiscount(promo);
                        Application.addDiscount((Discount) promo);
                        System.out.println("[+] Oferta de cantidad guardada.");
                    }
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        } catch (Exception e) {
            System.out.println("[!] Error al procesar los datos: " + e.getMessage());
        }
    }

    private static void asignarDescuento(Scanner scanner) {
        ArrayList<NewProduct> catalogo = Application.getCatalog();
        ArrayList<IDiscount> bolsa = Application.getGlobalDiscounts();

        if (bolsa.isEmpty()) {
            System.out.println("[!] No hay descuentos en la bolsa global. Crea uno primero.");
            return;
        }

        ArrayList<Product> productosValidos = new ArrayList<>();
        for (NewProduct np : catalogo) {
            if (np instanceof Product) {
                productosValidos.add((Product) np);
            }
        }

        if (productosValidos.isEmpty()) {
            System.out.println("[!] No hay productos individuales disponibles.");
            return;
        }

        System.out.println("\n--- SELECCIONA PRODUCTO ---");
        for (int i = 0; i < productosValidos.size(); i++) {
            System.out.println((i + 1) + ".- " + productosValidos.get(i).getName());
        }
        System.out.print("Selección: ");
        int pIdx = Integer.parseInt(scanner.nextLine()) - 1;

        System.out.println("\n--- SELECCIONA DESCUENTO ---");
        for (int i = 0; i < bolsa.size(); i++) {
            System.out.println((i + 1) + ".- " + bolsa.get(i).getDescription());
        }
        System.out.print("Selección: ");
        int dIdx = Integer.parseInt(scanner.nextLine()) - 1;

        if (pIdx >= 0 && pIdx < productosValidos.size() && dIdx >= 0 && dIdx < bolsa.size()) {
            Product elegido = productosValidos.get(pIdx);
            IDiscount desc = bolsa.get(dIdx);
            elegido.setDiscount(desc);
            System.out.println("[+] Descuento aplicado a " + elegido.getName());
        }
    }

    private static NewProduct buscarProductoEnCatalogo(String nombre) {
        for (NewProduct p : Application.getCatalog()) {
            if (p.getName().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }
}