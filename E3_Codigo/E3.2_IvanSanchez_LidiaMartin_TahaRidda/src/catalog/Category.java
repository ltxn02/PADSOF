package catalog;
import java.util.ArrayList;
import utils.*;

/**
 * Clase que representa una categoría (Category) dentro de la tienda.
 * Sirve para clasificar y agrupar lógica y visualmente los artículos del catálogo.
 * Soporta la clasificación tanto de productos nuevos ({@link NewProduct}) como
 * de productos de segunda mano ({@link SecondHandProduct}) de forma independiente.
 * Hereda de {@link BaseElement} para el control de estado y auditoría.
 *
 * @author Iván Sánchez
 * @version 1.4
 */
public class Category extends BaseElement implements java.io.Serializable {
    private String name;
    private ArrayList<NewProduct> newProductItems;
    private ArrayList<SecondHandProduct> secondHandItems;

    /**
     * Constructor para inicializar una categoría e insertar una lista inicial de artículos.
     *
     * @param name  Nombre de la categoría (ej: "Juegos de Mesa", "Cómics Manga").
     * @param items Lista inicial de artículos ({@link Item}) que se clasificarán automáticamente.
     */
    public Category(String name, ArrayList<Item> items) {
        this.name = name;
        // Inicializamos las listas para evitar NullPointerExceptions
        this.newProductItems = new ArrayList<>();
        this.secondHandItems = new ArrayList<>();

        for(Item item: items) {
            this.addItem(item);
        }
    }

    /**
     * Constructor simplificado para crear una categoría vacía.
     *
     * @param name Nombre de la nueva categoría.
     */
    public Category(String name) {
        this.name = name;
        this.newProductItems = new ArrayList<>();
        this.secondHandItems = new ArrayList<>(); // Corregido: inicialización que faltaba
    }

    /**
     * Añade un nuevo artículo a la categoría.
     * El método detecta automáticamente si el artículo es nuevo o de segunda mano
     * y lo guarda en la lista correspondiente.
     *
     * @param item El artículo ({@link Item}) a añadir.
     * @throws IllegalArgumentException Si el artículo ya existe previamente en esta categoría.
     */
    public void addItem(Item item) throws IllegalArgumentException {
        if(this.newProductItems.contains(item) || this.secondHandItems.contains(item)) {
            throw new IllegalArgumentException("Item already exists in " + this.name);
        }

        if(item instanceof NewProduct) {
            this.newProductItems.add((NewProduct)item);
        } else if (item instanceof SecondHandProduct) {
            this.secondHandItems.add((SecondHandProduct)item);
        }
    }

    /**
     * Obtiene el nombre actual de la categoría.
     *
     * @return El nombre de la categoría en formato texto.
     */
    public String getNameCategory() {
        return name;
    }

    /**
     * Modifica el nombre de la categoría.
     *
     * @param name El nuevo nombre que se le quiere asignar.
     */
    public void rename(String name) {
        this.name = name;
    }
}