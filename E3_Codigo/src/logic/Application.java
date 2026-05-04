package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import utils.*;
import users.*;
import catalog.*;
import transactions.*;
import discounts.*;

public class Application {
    private static HashMap<String, RegisteredUser> users = new HashMap<>();
    private static HashMap<String, Notification> notifications = new HashMap<>();
    private static ArrayList<SecondHandProduct> secondHandProducts = new ArrayList<>();
    private static ArrayList<NewProduct> catalog = new ArrayList<>();
    private static ArrayList<Category> globalCategories = new ArrayList<>();
    private static ArrayList<IDiscount> globalDiscounts = new ArrayList<>();

    // --- BLOQUE DE INICIALIZACIÓN ESTÁTICA ---
    static {
        // 1. INICIALIZACIÓN DE USUARIOS POR DEFECTO
        try {
            Manager lidia = new Manager("lidia", "lidia123", "Lidia Martin Teres", "12345678A", "01/01/2002", "lidia@rongero.es", "600000000", 10000.00);
            Manager taha = new Manager("taha", "taha123", "Taha Ridda", "12345678A", "01/01/2002", "taha@rongero.es", "600000000", 10000.00);
            Manager ivan = new Manager("ivan", "ivan123", "Ivan Sanchez", "12345678A", "01/01/2002", "ivan@rongero.es", "600000000", 10000.00);

            Employee empleadoDefecto = new Employee("empleado", "empleado123", "Empleado de Prueba", "87654321B", "15/05/1995", "empleado@rongero.es", "600000000", 1200.00, true);

            users.put(lidia.getUsername(), lidia);
            users.put(taha.getUsername(), taha);
            users.put(ivan.getUsername(), ivan);
            users.put(empleadoDefecto.getUsername(), empleadoDefecto);

            System.out.println("[Sistema] Cuentas de gestor y empleado creadas por defecto.");
        } catch (Exception e) {
            System.err.println("Error al crear usuarios por defecto: " + e.getMessage());
        }

        // 2. INICIALIZACIÓN DE PRODUCTOS POR DEFECTO
        try {
            ArrayList<Review> emptyReviews = new ArrayList<>();
            ArrayList<Item> emptyItems = new ArrayList<>();

            Category catComic = new Category("Cómics y manga", emptyItems);
            Category catFigura = new Category("Figuras de colección", emptyItems);
            Category catJuego = new Category("Juegos de mesa", emptyItems);

            ArrayList<Category> comicCategories = new ArrayList<>(Arrays.asList(catComic));
            ArrayList<Category> figuraCategories = new ArrayList<>(Arrays.asList(catFigura));
            ArrayList<Category> juegoCategories = new ArrayList<>(Arrays.asList(catJuego));

            // --- 10 CÓMICS ---
            catalog.add(new Comic("One Piece Vol. 1", "El inicio de la gran aventura de Luffy.", 7.95, new ArrayList<>(Arrays.asList("src/imgProductos/op1.jpg")), 50, comicCategories, emptyReviews, null, 208, "Planeta Cómic", 1997, new ArrayList<>(Arrays.asList("Eiichiro Oda"))));
            catalog.add(new Comic("Dragon Ball Vol. 1", "Goku conoce a Bulma y comienza la búsqueda.", 8.50, new ArrayList<>(Arrays.asList("src/imgProductos/db1.jpg")), 40, comicCategories, emptyReviews, null, 192, "Planeta Cómic", 1984, new ArrayList<>(Arrays.asList("Akira Toriyama"))));
            catalog.add(new Comic("Tintín en el Tíbet", "Tintín viaja al Himalaya en busca de Chang.", 14.90, new ArrayList<>(Arrays.asList("src/imgProductos/tintin.jpg")), 25, comicCategories, emptyReviews, null, 64, "Juventud", 1960, new ArrayList<>(Arrays.asList("Hergé"))));
            catalog.add(new Comic("Batman: Año Uno", "Bruce Wayne regresa a Gotham para ser Batman.", 18.00, new ArrayList<>(Arrays.asList("src/imgProductos/batman.jpg")), 15, comicCategories, emptyReviews, null, 144, "ECC Ediciones", 1987, new ArrayList<>(Arrays.asList("Frank Miller"))));
            catalog.add(new Comic("Naruto Vol. 1", "Un joven ninja busca el reconocimiento de su aldea.", 7.50, new ArrayList<>(Arrays.asList("src/imgProductos/naruto1.jpg")), 60, comicCategories, emptyReviews, null, 192, "Planeta Cómic", 1999, new ArrayList<>(Arrays.asList("Masashi Kishimoto"))));
            catalog.add(new Comic("Spiderman: Kraven", "Kraven intenta derrotar definitivamente a la araña.", 22.00, new ArrayList<>(Arrays.asList("src/imgProductos/spiderman.jpg")), 10, comicCategories, emptyReviews, null, 160, "Panini Comics", 1987, new ArrayList<>(Arrays.asList("J.M. DeMatteis"))));
            catalog.add(new Comic("Akira Vol. 1", "En Neo-Tokyo, experimentos secretos cambian todo.", 25.00, new ArrayList<>(Arrays.asList("src/imgProductos/akira1.jpg")), 20, comicCategories, emptyReviews, null, 360, "Norma Editorial", 1982, new ArrayList<>(Arrays.asList("Katsuhiro Otomo"))));
            catalog.add(new Comic("Watchmen", "Obra maestra sobre la moralidad de los héroes.", 35.00, new ArrayList<>(Arrays.asList("src/imgProductos/watchmen.jpg")), 12, comicCategories, emptyReviews, null, 416, "DC Comics", 1986, new ArrayList<>(Arrays.asList("Alan Moore"))));
            catalog.add(new Comic("Death Note Vol. 1", "Un cuaderno capaz de matar con solo escribir un nombre.", 8.00, new ArrayList<>(Arrays.asList("src/imgProductos/deathnote1.jpg")), 45, comicCategories, emptyReviews, null, 200, "Norma Editorial", 2003, new ArrayList<>(Arrays.asList("Tsugumi Ohba"))));
            catalog.add(new Comic("Berserk Vol. 1", "Guts busca venganza en un mundo de fantasía oscura.", 10.00, new ArrayList<>(Arrays.asList("src/imgProductos/berserk1.jpg")), 30, comicCategories, emptyReviews, null, 224, "Panini Comics", 1989, new ArrayList<>(Arrays.asList("Kentaro Miura"))));

            // --- 10 FIGURAS ---
            catalog.add(new Figurine("Figura Gon Freecss", "Gon en pose de batalla de Hunter x Hunter.", 45.00, new ArrayList<>(Arrays.asList("src/imgProductos/gon.jpg")), 10, figuraCategories, emptyReviews, null, 15.0, 5.0, 5.0, "PVC", "Banpresto"));
            catalog.add(new Figurine("Funko Pop Zoro", "Zoro Roronoa con sus tres katanas.", 16.99, new ArrayList<>(Arrays.asList("src/imgProductos/zoro_pop.jpg")), 25, figuraCategories, emptyReviews, null, 10.0, 6.0, 6.0, "Vinilo", "Funko"));
            catalog.add(new Figurine("Figura Eren Titán", "Eren en su imponente forma de Titán de Ataque.", 89.00, new ArrayList<>(Arrays.asList("src/imgProductos/eren.jpg")), 5, figuraCategories, emptyReviews, null, 25.0, 15.0, 15.0, "Resina", "Good Smile"));
            catalog.add(new Figurine("Nendoroid Link", "Link de BOTW con múltiples accesorios.", 55.00, new ArrayList<>(Arrays.asList("src/imgProductos/link.jpg")), 8, figuraCategories, emptyReviews, null, 10.0, 4.0, 4.0, "ABS", "Good Smile"));
            catalog.add(new Figurine("Figura Nezuko", "Nezuko saliendo de su caja protectora.", 39.90, new ArrayList<>(Arrays.asList("src/imgProductos/nezuko.jpg")), 15, figuraCategories, emptyReviews, null, 12.0, 7.0, 7.0, "PVC", "Sega"));
            catalog.add(new Figurine("Funko Pop Iron Man", "Tony Stark con armadura Mark 85.", 15.00, new ArrayList<>(Arrays.asList("src/imgProductos/ironman.jpg")), 30, figuraCategories, emptyReviews, null, 10.0, 6.0, 6.0, "Vinilo", "Funko"));
            catalog.add(new Figurine("Figura Darth Vader", "Lord Sith con capa de tela y gran detalle.", 120.00, new ArrayList<>(Arrays.asList("src/imgProductos/vader.jpg")), 3, figuraCategories, emptyReviews, null, 30.0, 12.0, 10.0, "PVC", "Hot Toys"));
            catalog.add(new Figurine("Figura Sailor Moon", "Usagi Tsukino con su báculo lunar.", 42.00, new ArrayList<>(Arrays.asList("src/imgProductos/sailormoon.jpg")), 12, figuraCategories, emptyReviews, null, 18.0, 6.0, 6.0, "PVC", "Bandai"));
            catalog.add(new Figurine("Figura Geralt Rivia", "El brujo cazando un monstruo.", 65.00, new ArrayList<>(Arrays.asList("src/imgProductos/geralt.jpg")), 7, figuraCategories, emptyReviews, null, 24.0, 10.0, 10.0, "PVC", "Dark Horse"));
            catalog.add(new Figurine("Funko Pop Pikachu", "El Pokémon eléctrico en formato Pop.", 14.50, new ArrayList<>(Arrays.asList("src/imgProductos/pikachu.jpg")), 40, figuraCategories, emptyReviews, null, 10.0, 5.0, 5.0, "Vinilo", "Funko"));

            // --- 10 JUEGOS ---
            catalog.add(new Game("Catan", "Estrategia y negociación en la isla.", 45.00, new ArrayList<>(Arrays.asList("src/imgProductos/catan.jpg")), 20, juegoCategories, emptyReviews, null, 4, new ArrayList<>(Arrays.asList("Gestión")), new AgeRange(10, 99)));
            catalog.add(new Game("Dixit", "Juego de interpretación y arte onírico.", 32.00, new ArrayList<>(Arrays.asList("src/imgProductos/dixit.jpg")), 15, juegoCategories, emptyReviews, null, 6, new ArrayList<>(Arrays.asList("Cartas")), new AgeRange(8, 99)));
            catalog.add(new Game("Carcassonne", "Construye la Francia medieval con losetas.", 28.00, new ArrayList<>(Arrays.asList("src/imgProductos/carcassonne.jpg")), 25, juegoCategories, emptyReviews, null, 5, new ArrayList<>(Arrays.asList("Losetas")), new AgeRange(7, 99)));
            catalog.add(new Game("Exploding Kittens", "Ruleta rusa con gatos explosivos.", 20.00, new ArrayList<>(Arrays.asList("src/imgProductos/kittens.jpg")), 50, juegoCategories, emptyReviews, null, 5, new ArrayList<>(Arrays.asList("Azar")), new AgeRange(7, 99)));
            catalog.add(new Game("Pandemic", "Cooperativo para salvar al mundo de virus.", 40.00, new ArrayList<>(Arrays.asList("src/imgProductos/pandemic.jpg")), 12, juegoCategories, emptyReviews, null, 4, new ArrayList<>(Arrays.asList("Cooperación")), new AgeRange(10, 99)));
            catalog.add(new Game("Zombicide", "Sobrevive a la horda zombi con amigos.", 95.00, new ArrayList<>(Arrays.asList("src/imgProductos/zombicide.jpg")), 6, juegoCategories, emptyReviews, null, 6, new ArrayList<>(Arrays.asList("Dados")), new AgeRange(14, 99)));
            catalog.add(new Game("Aventureros Tren", "Crea rutas ferroviarias por Europa.", 44.00, new ArrayList<>(Arrays.asList("src/imgProductos/tren.jpg")), 18, juegoCategories, emptyReviews, null, 5, new ArrayList<>(Arrays.asList("Rutas")), new AgeRange(8, 99)));
            catalog.add(new Game("Risk", "El clásico de conquista militar mundial.", 35.00, new ArrayList<>(Arrays.asList("src/imgProductos/risk.jpg")), 22, juegoCategories, emptyReviews, null, 6, new ArrayList<>(Arrays.asList("Dados")), new AgeRange(10, 99)));
            catalog.add(new Game("Cluedo", "Resuelve el misterio del asesinato.", 29.90, new ArrayList<>(Arrays.asList("src/imgProductos/cluedo.jpg")), 20, juegoCategories, emptyReviews, null, 6, new ArrayList<>(Arrays.asList("Deducción")), new AgeRange(8, 99)));
            catalog.add(new Game("Uno", "Quedate sin cartas antes que los demás.", 10.00, new ArrayList<>(Arrays.asList("src/imgProductos/uno.jpg")), 100, juegoCategories, emptyReviews, null, 10, new ArrayList<>(Arrays.asList("Cartas")), new AgeRange(6, 99)));

            globalCategories.addAll(Arrays.asList(catComic, catFigura, catJuego));
            System.out.println("[Sistema] Catálogo inicializado con 30 productos reales.");

        } catch (Exception e) {
            System.err.println("Error al crear los productos por defecto: " + e.getMessage());
            e.printStackTrace();
        }

        // =========================================================
        // 3. INICIALIZACIÓN DE INTERCAMBIOS POR DEFECTO (PRUEBAS)
        // =========================================================
        try {
            // a) Creamos un par de clientes "falsos" para que sean los dueños
            Client cliente1 = new Client("Gon67", "pass", "Gon Freecss", "11111111A", "05/05/2005", "gon@mail.com", "600111222");
            Client cliente2 = new Client("asus09", "pass", "Asus Gamer", "22222222B", "10/10/2000", "asus@mail.com", "600333444");
            Client cliente3 = new Client("akatsuki9", "pass", "Itachi Uchiha", "33333333C", "09/06/1998", "itachi@mail.com", "600555666");

            users.put(cliente1.getUsername(), cliente1);
            users.put(cliente2.getUsername(), cliente2);
            users.put(cliente3.getUsername(), cliente3);

            // c) Creamos los productos de Segunda Mano usando ArrayList
            SecondHandProduct sh1 = new SecondHandProduct(
                    "Figura Killua (HXH)", "Figura de la guerra con las hormigas",
                    new ArrayList<>(Arrays.asList("gon.jpg")), 100.0, true, ItemType.FIGURINE, Condition.PERFECTO, cliente1);

            SecondHandProduct sh2 = new SecondHandProduct(
                    "Funko de Po (Kung Fu Panda)", "Tercera película",
                    new ArrayList<>(Arrays.asList("pikachu.jpg")), 15.0, true, ItemType.FIGURINE, Condition.USO_LIGERO, cliente2);

            SecondHandProduct sh3 = new SecondHandProduct(
                    "Manga Naruto volumen 23", "Edición antigua Glénat",
                    new ArrayList<>(Arrays.asList("naruto1.jpg")), 21.0, true, ItemType.COMIC, Condition.USO_EVIDENTE, cliente3);

            // d) Los añadimos a la lista global de intercambios de la aplicación
            secondHandProducts.add(sh1);
            secondHandProducts.add(sh2);
            secondHandProducts.add(sh3);

            System.out.println("[Sistema] Intercambios de prueba inicializados.");

        } catch (Exception e) {
            System.err.println("Error al crear intercambios por defecto: " + e.getMessage());
        }
    }

    // --- RESTO DE MÉTODOS DE LA CLASE (Login, Registro, Persistencia, etc.) ---
    public static RegisteredUser login(String username, String password) throws IOException {
        RegisteredUser user = Application.users.get(username);
        if (user == null || !(user.login(username, password))) {
            throw new IOException("Incorrect username or password.");
        }
        return user;
    }

    public static void registerClient(Client client) throws IOException {
        if (users.containsKey(client.getUsername())) {
            throw new IOException("El nombre de usuario ya está en uso.");
        }
        Application.users.put(client.getUsername(), client);
    }

    public static void registerEmployee(Employee employee) throws IOException {
        if (users.containsKey(employee.getUsername())) {
            throw new IOException("El nombre de usuario ya está en uso.");
        }
        users.put(employee.getUsername(), employee);
    }

    public static ArrayList<NewProduct> getCatalog() { return catalog; }
    public static ArrayList<RegisteredUser> getUsers() { return new ArrayList<>(users.values()); }
    public static ArrayList<SecondHandProduct> getSecondHandProducts() { return secondHandProducts; }
    public static List<ExchangeOffer> getoffersmade(Client c){ return new ArrayList<>(c.getOffersMade()); }
    public static List<ExchangeOffer> getoffersreceived(Client c){ return new ArrayList<>(c.obtenerMisOfertasRecibidos()); }
    public static void addSecondHandProduct(SecondHandProduct p) { secondHandProducts.add(p); }
    public static ArrayList<Category> getGlobalCategories() { return globalCategories; }
    public static void addCategory(Category c) { globalCategories.add(c); }
    public static ArrayList<IDiscount> getGlobalDiscounts() { return globalDiscounts; }
    public static void addDiscount(IDiscount d) { globalDiscounts.add(d); }

    public static void guardarDatos(String rutaArchivo) {
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream(rutaArchivo))) {
            oos.writeObject(users);
            oos.writeObject(notifications);
            oos.writeObject(secondHandProducts);
            oos.writeObject(catalog);
            oos.writeObject(globalCategories);
            oos.writeObject(globalDiscounts);
        } catch (java.io.IOException e) {
            System.out.println("[!] Error al guardar: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void cargarDatos(String rutaArchivo) {
        java.io.File archivo = new java.io.File(rutaArchivo);
        if (!archivo.exists()) return;
        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(rutaArchivo))) {
            users = (java.util.HashMap<String, RegisteredUser>) ois.readObject();
            notifications = (java.util.HashMap<String, Notification>) ois.readObject();
            secondHandProducts = (java.util.ArrayList<SecondHandProduct>) ois.readObject();
            catalog = (java.util.ArrayList<NewProduct>) ois.readObject();
            globalCategories = (java.util.ArrayList<Category>) ois.readObject();
            globalDiscounts = (java.util.ArrayList<IDiscount>) ois.readObject();
        } catch (java.io.IOException | ClassNotFoundException e) {
            System.out.println("[!] Error al cargar: " + e.getMessage());
        }
    }
}
