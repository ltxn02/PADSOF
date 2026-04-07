package catalog;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import utils.*;

/**
 * Clase abstracta que representa un artículo base (Item) dentro de la tienda.
 * Es la raíz de la jerarquía del catálogo, de la cual heredan todos los tipos
 * de productos (nuevos, de segunda mano, packs, etc.). Gestiona los datos comunes
 * como el nombre, precio, descripción, categorías y marcas de tiempo temporales.
 * Hereda de {@link BaseElement} para el control de auditoría.
 *
 * @author Iván Sánchez y Lidia Martin
 * @version 2.2
 */
public abstract class Item extends BaseElement implements java.io.Serializable {
    private String name;
    private String description;
    private double price;
    private String picturePath;
    private ArrayList<Category> categories;
    private Instant lastAddedAt;

    /**
     * Constructor principal para inicializar un artículo base con categorías.
     *
     * @param name        Nombre del artículo.
     * @param description Descripción detallada del artículo.
     * @param price       Precio base en euros.
     * @param picturePath Ruta relativa de la imagen del artículo.
     * @param categories  Lista de categorías a las que pertenece.
     * @throws IllegalArgumentException Si el precio introducido es negativo.
     */
    public Item(String name, String description, double price, String picturePath, ArrayList<Category> categories) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
        this.name = name;
        this.description = description;
        this.picturePath = picturePath;
        this.categories = categories;
        this.lastAddedAt = null;
    }

    /**
     * Constructor simplificado para inicializar un artículo sin categorías iniciales.
     *
     * @param name        Nombre del artículo.
     * @param description Descripción del artículo.
     * @param price       Precio base en euros.
     * @param picturePath Ruta relativa de la imagen.
     */
    public Item(String name, String description, double price, String picturePath) {
        this(name, description, price, picturePath, new ArrayList<Category>());
    }

    /**
     * Registra el instante exacto actual.
     * Se utiliza habitualmente para llevar el control del tiempo cuando el artículo
     * es añadido a un carrito de la compra temporal.
     */
    protected void registerTime() {
        this.lastAddedAt = Instant.now();
    }

    /**
     * Limpia la marca de tiempo temporal dejándola nula.
     * Se usa cuando el artículo se retira del carrito o se formaliza la compra.
     */
    protected void clearInstants() {
        this.lastAddedAt = null;
    }

    /**
     * Comprueba si el tiempo transcurrido desde la última marca de tiempo
     * registrada ha superado una duración máxima permitida.
     *
     * @param time Duración ({@link Duration}) máxima de validez.
     * @return true si el tiempo ha expirado, false en caso contrario.
     */
    public boolean isExpired(Duration time) {
        return this.lastAddedAt.plus(time).isBefore(Instant.now());
    }

    /**
     * Edita los atributos básicos del artículo de forma segura.
     * Solo se actualizarán los valores que no sean nulos o, en el caso del precio, mayores que 0.
     *
     * @param name        Nuevo nombre (ignorado si es null).
     * @param description Nueva descripción (ignorada si es null).
     * @param price       Nuevo precio (ignorado si es 0 o negativo).
     * @param picturePath Nueva ruta de la imagen (ignorada si es null).
     */
    protected void edit(String name, String description, double price, String picturePath) {
        if(name != null) this.name = name;
        if(description != null) this.description = description;
        if(price > 0) this.price = price;
        if(picturePath != null) this.picturePath = picturePath;
    }

    /**
     * Actualiza forzosamente el precio del artículo.
     *
     * @param price Nuevo precio en euros.
     */
    protected void setPrice(double price) {
        this.price = price;
    }

    /**
     * Obtiene el precio base del artículo.
     *
     * @return El precio actual en euros.
     */
    public double getPrice(){
        return this.price;
    }

    /**
     * Obtiene el nombre del artículo.
     *
     * @return El nombre del artículo en formato texto.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Añade una nueva categoría a la lista de categorías del artículo.
     *
     * @param category La categoría a añadir ({@link Category}).
     * @throws IllegalArgumentException Si la categoría ya está asociada a este artículo.
     */
    public void addCategory(Category category) throws IllegalArgumentException {
        if(this.categories.contains(category)) {
            throw new IllegalArgumentException("Invalid category, already exists");
        }
        this.categories.add(category);
    }

    /**
     * Obtiene la lista completa de las categorías a las que pertenece el artículo.
     *
     * @return Lista de objetos {@link Category}.
     */
    public ArrayList<Category> getCategories() {
        return categories;
    }

    /**
     * Genera un resumen visual muy breve del artículo.
     *
     * @return Cadena con el formato: "Nombre (Precio €)"
     */
    public String itemAuxPreview() {
        return this.name + " (" + String.format("%.2f €", this.price) + ")";
    }

    /**
     * Genera una vista previa del artículo para listas, acortando descripciones largas.
     *
     * @return Cadena formateada con el nombre y un extracto de la descripción.
     */
    public String itemPreview() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name + " | ");
        if(this.description.length() < 15) {
            sb.append(this.description + " | ");
        } else {
            sb.append(this.description.substring(12) + "...");
        }
        return sb.toString();
    }

    /**
     * Genera un bloque de texto informativo detallado sobre el artículo.
     *
     * @return Cadena multilínea con el nombre, descripción completa y sus categorías.
     */
    public String itemInfo() {
        StringBuilder res = new StringBuilder();
        res.append("\nName: " + this.name + "\n  '" + this.description + "'\n");
        res.append("  Categories: " + this.categories + "\n");
        return res.toString();
    }

    /**
     * Comprueba si el artículo tiene el nombre exacto que se le pasa por parámetro.
     * La comparación es flexible, ignorando mayúsculas, minúsculas y espacios sobrantes.
     *
     * @param name Nombre a comparar.
     * @return true si los nombres coinciden lógicamente, false en caso contrario.
     */
    public boolean isNamed(String name) {
        if (this.name != null && this.name.equalsIgnoreCase(name.trim())) {
            return true;
        }
        return false;
    }

    /**
     * Obtiene la descripción detallada del artículo.
     *
     * @return La descripción en formato texto.
     */
    public String getDescription() {
        return description;
    }
}