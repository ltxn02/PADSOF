import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Application {
    private static HashMap<String, RegisteredUser> users = new HashMap<>();
    private static HashMap<String, Notification> notifications = new HashMap<>();

    // Lista para guardar el catálogo de productos de la tienda
    private static ArrayList<Product> catalog = new ArrayList<>();

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
                    7.99, "img/onepiece1.jpg", comicCategories, 50, emptyReviews, null,
                    208, "Planeta Cómic", 1997, autores);

            // --- PRODUCTO 2: FIGURA ---
            ArrayList<Category> figuraCategories = new ArrayList<>();
            figuraCategories.add(catFigura);

            Figurine figura1 = new Figurine("Figura Goku", "Figura de colección 15cm",
                    35.50, "img/goku.jpg", figuraCategories, 15, emptyReviews, null,
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
                    45.00, "img/catan.jpg", juegoCategories, 30, emptyReviews, null,
                    4, mecanicas, edadCatan);

            // Añadimos los productos a nuestro catálogo
            catalog.add(comic1);
            catalog.add(figura1);
            catalog.add(juego1);

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

    public static ArrayList<Product> getCatalog() {
        return catalog;
    }
    public static ArrayList<RegisteredUser> getUsers() {
        return new ArrayList<>(users.values());
    }
    public static ArrayList<SecondHandProduct> getSecondHandProducts(){
        ArrayList<SecondHandProduct> listaProductos = new ArrayList<>();

        return listaProductos;
    }
    public static List<Exchangeoffer> getoffersmade(Client c){
        List<Exchangeoffer> ofertas = new ArrayList<>();
        for (Exchangeoffer o: c.obtenerMisOfertasEnviadas()) {
            ofertas.add(o);
        }
        return ofertas;
    }
    public static List<Exchangeoffer> getoffersreceived(Client c){
        List<Exchangeoffer> ofertas = new ArrayList<>();
        for (Exchangeoffer o: c.obtenerMisOfertasRecibidos()) {
            ofertas.add(o);
        }
        return ofertas;
    }

}