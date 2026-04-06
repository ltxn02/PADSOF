package logic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import utils.*;
import users.*;
import catalog.*;
import transactions.*;

public class Application {
    private static HashMap<String, RegisteredUser> users = new HashMap<>();
    private static HashMap<String, Notification> notifications = new HashMap<>();
    private static ArrayList<SecondHandProduct> secondHandProducts = new ArrayList<>();
    private static ArrayList<NewProduct> catalog = new ArrayList<>();
    private static ArrayList<Category> globalCategories = new ArrayList<>();
    private static ArrayList<Discount> globalDiscounts = new ArrayList<>();

    // --- BLOQUE DE INICIALIZACIÓN ESTÁTICA ---
    static {
        // 1. INICIALIZACIÓN DE USUARIOS POR DEFECTO
        try {
            Manager lidia = new Manager("lidia", "lidia123", "Lidia Martin Teres", "XXXXXXXXX", "01/01/2002", "lidia@rongero.es", "600000000", 10000.00);
            Manager taha = new Manager("taha", "taha123", "Taha Ridda", "XXXXXXXXX", "01/01/2002", "taha@rongero.es", "600000000", 10000.00);
            Manager ivan = new Manager("ivan", "ivan123", "Ivan Sanchez", "XXXXXXXXX", "01/01/2002", "ivan@rongero.es", "600000000", 10000.00);

            users.put(lidia.getUsername(), lidia);
            users.put(taha.getUsername(), taha);
            users.put(ivan.getUsername(), ivan);

            System.out.println("[Sistema] Cuentas de gestor creadas por defecto.");
        } catch (Exception e) {
            System.err.println("Error al crear gestores por defecto: " + e.getMessage());
        }

        // 2. INICIALIZACIÓN DE PRODUCTOS POR DEFECTO
        try {
            // Preparamos lista vacía para reseñas (nadie ha comentado aún)
            ArrayList<Review> emptyReviews = new ArrayList<>();

            // Creamos las categorías (su constructor pide un ArrayList<Item>)
            ArrayList<Item> emptyItems = new ArrayList<>();
            Category catComic = new Category("Cómics y manga", emptyItems);
            Category catFigura = new Category("Figuras de colección", emptyItems);
            Category catJuego = new Category("Juegos de mesa", emptyItems);

            // --- PRODUCTO 1: CÓMIC ---
            ArrayList<Category> comicCategories = new ArrayList<>();
            comicCategories.add(catComic);

            ArrayList<String> autores = new ArrayList<>();
            autores.add("Eiichiro Oda");

            Comic comic1 = new Comic("One Piece Vol. 1", "El inicio de la aventura de Luffy",
                    7.99, "img/onepiece1.jpg", 50, comicCategories, emptyReviews, null,
                    208, "Planeta Cómic", 1997, autores);

            // --- PRODUCTO 2: FIGURA ---
            ArrayList<Category> figuraCategories = new ArrayList<>();
            figuraCategories.add(catFigura);

            Figurine figura1 = new Figurine("Figura Goku", "Figura de colección 15cm",
                    35.50, "img/goku.jpg", 15, figuraCategories, emptyReviews, null,
                    15.0, 5.0, 5.0, "PVC", "Anime");

            // --- PRODUCTO 3: JUEGO DE MESA ---
            ArrayList<Category> juegoCategories = new ArrayList<>();
            juegoCategories.add(catJuego);

            ArrayList<String> mecanicas = new ArrayList<>();
            mecanicas.add("Gestión de recursos");
            mecanicas.add("Tirar dados");

            // Instanciamos el rango de edad usando tu clase AgeRange
            AgeRange edadCatan = new AgeRange(10, 99);

            Game juego1 = new Game("Catán", "Juego de estrategia y negociación",
                    45.00, "img/catan.jpg", 30, juegoCategories, emptyReviews, null,
                    4, mecanicas, edadCatan);

            // Añadimos los productos a nuestro catálogo
            catalog.add(comic1);
            catalog.add(figura1);
            catalog.add(juego1);
            globalCategories.add(catComic);

            System.out.println("[Sistema] Catálogo inicializado con 3 productos por defecto.");

        } catch (Exception e) {
            System.err.println("Error al crear los productos por defecto: " + e.getMessage());
            e.printStackTrace(); // Esto os ayudará a depurar si falla algún constructor
        }
    }
    // ------------------------------------------------

    public static RegisteredUser login(String username, String password) throws IOException {
        RegisteredUser user = Application.users.get(username);

        if (user == null) {
            throw new IOException("Incorrect username or password.");
        }

        if (!(user.login(username, password))) {
            throw new IOException("Incorrect username or password.");
        }

        System.out.println("[+] Se ha iniciado correctamente con el usuario " + username);
        return user;
    }

    public static void registerClient(Client client) throws IOException {
        // Validación extra recomendada: no permitir registrar usuarios que ya existen
        if (users.containsKey(client.getUsername())) {
            throw new IOException("El nombre de usuario ya está en uso.");
        }

        Application.users.put(client.getUsername(), client);
        System.out.println("User " + client.getUsername() + " registered successfully.");
    }

    public static void registerEmployee(Employee employee) throws IOException {
        if (users.containsKey(employee.getUsername())) {
            throw new IOException("El nombre de usuario ya está en uso.");
        }
        users.put(employee.getUsername(), employee);
        System.out.println("[+] Empleado " + employee.getUsername() + " registrado en el sistema.");
    }

    public static ArrayList<NewProduct> getCatalog() {
        return catalog;
    }

    public static ArrayList<RegisteredUser> getUsers() {
        return new ArrayList<>(users.values());
    }

    public static ArrayList<SecondHandProduct> getSecondHandProducts() {
        return secondHandProducts;
    }

    public static List<ExchangeOffer> getoffersmade(Client c){
        List<ExchangeOffer> ofertas = new ArrayList<>();
        for (ExchangeOffer o: c.getOffersMade()) {
            ofertas.add(o);
        }
        return ofertas;
    }
    public static List<ExchangeOffer> getoffersreceived(Client c){
        List<ExchangeOffer> ofertas = new ArrayList<>();
        for (ExchangeOffer o: c.obtenerMisOfertasRecibidos()) {
            ofertas.add(o);
        }
        return ofertas;
    }

    public static void addSecondHandProduct(SecondHandProduct p) {
        secondHandProducts.add(p);
    }

    public static ArrayList<Category> getGlobalCategories() {
        return globalCategories;
    }

    public static void addCategory(Category c) {
        globalCategories.add(c);
    }

    public static ArrayList<Discount> getGlobalDiscounts() {
        return globalDiscounts;
    }

    public static void addDiscount(Discount d) {
        globalDiscounts.add(d);
    }

    // --- PERSISTENCIA DE DATOS ---
    public static void guardarDatos(String rutaArchivo) {
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream(rutaArchivo))) {
            oos.writeObject(users);
            oos.writeObject(notifications);
            oos.writeObject(secondHandProducts);
            oos.writeObject(catalog);
            oos.writeObject(globalCategories);
            oos.writeObject(globalDiscounts);
            System.out.println("[Sistema] Datos guardados correctamente en " + rutaArchivo);
        } catch (java.io.IOException e) {
            System.out.println("[!] Error al guardar los datos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void cargarDatos(String rutaArchivo) {
        java.io.File archivo = new java.io.File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("[Sistema] No se encontró archivo de guardado previo. Iniciando con datos por defecto.");
            return;
        }

        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(rutaArchivo))) {
            users = (java.util.HashMap<String, RegisteredUser>) ois.readObject();
            notifications = (java.util.HashMap<String, Notification>) ois.readObject();
            secondHandProducts = (java.util.ArrayList<SecondHandProduct>) ois.readObject();
            catalog = (java.util.ArrayList<NewProduct>) ois.readObject();
            globalCategories = (java.util.ArrayList<Category>) ois.readObject();
            globalDiscounts = (java.util.ArrayList<Discount>) ois.readObject();
            System.out.println("[Sistema] Datos cargados correctamente desde " + rutaArchivo);
        } catch (java.io.IOException | ClassNotFoundException e) {
            System.out.println("[!] Error al cargar los datos: " + e.getMessage());
        }
    }
}