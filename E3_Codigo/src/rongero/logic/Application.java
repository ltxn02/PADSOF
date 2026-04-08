package logic;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import model.user.*;
import model.catalog.*;
import model.transactions.*;
import model.discounts.*;
import util.*;

public class Application {

    // 1. ATRIBUTOS DE INSTANCIA (EL ESTADO DEL SISTEMA)
    private static Application instance;
    private HashMap<String, RegisteredUser> users;
    private HashMap<String, Notification> notifications;
    private ArrayList<SecondHandProduct> secondHandProducts;
    private ArrayList<NewProduct> catalog;
    private ArrayList<Category> globalCategories;
    private List<IVolumen> globalDiscounts;

    // 2. CONSTRUCTOR PRIVADO (PATRÓN SINGLETON)
    private Application() {
        this.users = new HashMap<>();
        this.notifications = new HashMap<>();
        this.secondHandProducts = new ArrayList<>();
        this.catalog = new ArrayList<>();
        this.globalCategories = new ArrayList<>();
        this.globalDiscounts = new ArrayList<>();

        inicializarDatosPorDefecto();
    }

    // 3. MÉTODO DE ACCESO ÚNICO
    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    private void inicializarDatosPorDefecto() {
        try {
            // Managers por defecto
            Manager lidia = new Manager("lidia", "lidia123", "Lidia Martin Teres", "XXXXXXXXX", "01/01/2002", "lidia@es", "600000000", 10000.00);
            Manager taha = new Manager("taha", "taha123", "Taha Ridda", "XXXXXXXXX", "01/01/2002", "taha@es", "600000000", 10000.00);
            Manager ivan = new Manager("ivan", "ivan123", "Ivan Sanchez", "XXXXXXXXX", "01/01/2002", "ivan@es", "600000000", 10000.00);

            users.put(lidia.getUsername(), lidia);
            users.put(taha.getUsername(), taha);
            users.put(ivan.getUsername(), ivan);

            // Categoría inicial
            Category catComic = new Category("Cómics y manga", new ArrayList<>());
            globalCategories.add(catComic);

            // Producto inicial
            Comic comic1 = new Comic("One Piece Vol. 1", "El inicio de la aventura", 7.99, "img/onepiece.jpg",
                    new ArrayList<>(Arrays.asList(catComic)), 50, new ArrayList<>(), null, 208, "Planeta", 1997, new ArrayList<>(Arrays.asList("Oda")));

            catalog.add(comic1);
            System.out.println("[Sistema] Datos cargados con éxito.");
        } catch (Exception e) {
            System.err.println("Error en carga inicial: " + e.getMessage());
        }
    }

    // --- MÉTODOS DE USUARIO ---
    public RegisteredUser login(String username, String password) throws IOException {
        RegisteredUser user = users.get(username);
        if (user == null || !user.login(username, password)) {
            throw new IOException("Incorrect username or password.");
        }
        return user;
    }

    public void registerClient(Client client) throws IOException {
        if (users.containsKey(client.getUsername())) throw new IOException("Usuario ya existe");
        users.put(client.getUsername(), client);
    }

    public void registerEmployee(Employee employee) throws IOException {
        if (users.containsKey(employee.getUsername())) throw new IOException("Empleado ya existe");
        users.put(employee.getUsername(), employee);
    }

    public ArrayList<RegisteredUser> getUsers() {
        return new ArrayList<>(users.values());
    }

    // --- MÉTODOS DE CATÁLOGO Y PRODUCTOS ---
    public ArrayList<NewProduct> getCatalog() {
        return this.catalog;
    }

    public Item buscarProducto(String nombre) {
        return catalog.stream()
                .filter(p -> p.getName().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }


    public void addCategory(Category c) {
        globalCategories.add(c);
    }

    // --- MÉTODOS DE SEGUNDA MANO ---

    /**
     * Obtiene los productos de segunda mano disponibles en el sistema que no
     * pertenecen al cliente que realiza la consulta.
     * * @param c El cliente que realiza la consulta.
     * @return Lista de productos de segunda mano disponibles y ajenos al cliente.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public List<SecondHandProduct> obtenerSecondHandProducts(Client c) {
        return secondHandProducts.stream()
                .filter(p -> p.isAvailable() && !p.isOwnedBy(c))
                .collect(Collectors.toList());
    }
    /**
     * Establece la instancia única de la aplicación (usado por SaveLoader).
     * * @param loadedApp La instancia de la aplicación cargada desde persistencia.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void setInstance(Application loadedApp) {
        instance = loadedApp;
    }
    /**
     * Añade un nuevo producto de segunda mano al registro global.
     * * @param p El producto de segunda mano a añadir.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public void addSecondHandProduct(SecondHandProduct p) {
        secondHandProducts.add(p);
    }
    /**
     * Obtiene la cartera completa de productos de segunda mano de un cliente.
     * * @param c El cliente del que se desea obtener la cartera.
     * @return Lista de todos los productos de segunda mano en propiedad del cliente.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public List<SecondHandProduct> productosSegundaManoOwned(Client c){
        return c.getCarteraSegundaMano();
    }
    /**
     * Obtiene los productos de un cliente que ya han sido tasados y están activos.
     * * @param c El cliente dueño de los productos.
     * @return Lista de productos tasados (activos).
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public List<SecondHandProduct> obtenerProductosActivos (Client c){
        return productosSegundaManoOwned(c).stream()
                .filter(SecondHandProduct::isAppraised)
                .collect(Collectors.toList());
    }
    /**
     * Obtiene los productos de un cliente que aún no han sido tasados.
     * * @param c El cliente dueño de los productos.
     * @return Lista de productos pendientes de tasación (inactivos).
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public List<SecondHandProduct> obtenerProductosInactivos (Client c){
        return productosSegundaManoOwned(c).stream()
                .filter(p -> !p.isAppraised())
                .collect(Collectors.toList());
    }
    /**
     * Obtiene el historial de ofertas de intercambio enviadas por un cliente.
     * * @param c El cliente que envió las ofertas.
     * @return Lista de ofertas realizadas por el cliente.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public List<ExchangeOffer> getOffersMade(Client c){
        return c.obtenerMisOfertasEnviadas();
    }
    /**
     * Obtiene las ofertas de intercambio recibidas por un cliente de parte de otros.
     * * @param c El cliente receptor de las ofertas.
     * @return Lista de ofertas recibidas pendientes o gestionadas.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public List<ExchangeOffer> getOffersReceived(Client c){
        return c.obtenerMisOfertasRecibidos();
    }
    /**
     * Obtiene la lista global de categorías registradas en la aplicación.
     * * @return Lista de categorías disponibles.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public List<Category> getGlobalCategories() {
        return this.globalCategories;
    }

    // --- MÉTODOS DE DESCUENTOS ---
    /**
     * Añade un nuevo descuento de volumen aplicable al total del carrito.
     * * @param d El descuento de volumen a registrar.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public void addGlobalDiscount(IVolumen d) {
        this.globalDiscounts.add(d);
    }
    /**
     * Obtiene la lista de todos los descuentos de volumen activos.
     * * @return Lista de descuentos globales.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public List<IVolumen> getGlobalDiscounts() {
        return globalDiscounts;
    }
    /**
     * Busca un producto por nombre y le asigna un descuento específico.
     * * @param nombreProd El nombre exacto del producto.
     * @param d El descuento a aplicar.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public void aplicarDescuentoAProducto(String nombreProd, IDiscount d) {
        this.catalog.stream()
                .filter(p -> p.getName().equalsIgnoreCase(nombreProd))
                .findFirst()
                .ifPresent(p -> p.setDiscount(d));
    }
    /**
     * Busca una categoría y aplica un descuento a todos los productos contenidos en ella.
     * * @param nombreCat El nombre de la categoría.
     * @param d El descuento a aplicar a todos los ítems de la categoría.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public void aplicarDescuentoACategoria(String nombreCat, IDiscount d) {
        this.globalCategories.stream()
                .filter(c -> c.getNameCategory().equalsIgnoreCase(nombreCat))
                .findFirst()
                .ifPresent(cat -> {
                    for (Item item : cat.getItems()) {
                        item.setDiscount(d);
                    }
                });
    }
}