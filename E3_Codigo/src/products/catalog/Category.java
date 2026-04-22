package products.catalog;
import java.util.ArrayList;
import java.util.List;

import products.Item;
import products.NewProduct;
import products.SecondHandProduct;
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
public class Category<T extends Item> extends BaseElement implements java.io.Serializable {
    private String name;
    private List<T> items;

    /**
     * Constructor para inicializar una categoría e insertar una lista inicial de artículos.
     *
     * @param name  Nombre de la categoría (ej: "Juegos de Mesa", "Cómics Manga").
     * @param items Lista inicial de artículos ({@link Item}) que se clasificarán automáticamente.
     */
    public Category(String name, ArrayList<T> items) {
        this.name = name;
        this.items = items != null ? items : new ArrayList<>();
    }

    /**
     * Constructor simplificado para crear una categoría vacía.
     *
     * @param name Nombre de la nueva categoría.
     */
    public Category(String name) {
        this(name, null);
    }

    /**
     * Añade un nuevo artículo a la categoría.
     * El método detecta automáticamente si el artículo es nuevo o de segunda mano
     * y lo guarda en la lista correspondiente.
     *
     * @param item El artículo ({@link Item}) a añadir.
     * @throws IllegalArgumentException Si el artículo ya existe previamente en esta categoría.
     */
    public void addItem(T item) {							// Override este metodo (??)
        if (item != null && !this.items.contains(item)) {
            this.items.add(item);
        } else if (item instanceof NewProduct) {
        	(NewProduct)item.
        }
    }
    
    public void addItems(List<T> newItems) {
        if (newItems != null) {
            for (T item : newItems) {
                this.addItem(item);
            }
        }
    }
    
    public void removeItem(T item) {
        this.items.remove(item);
    }
    
    public int getItemCount() {
        return this.items.size();
    }
    
    public boolean isEmpty() {
        return this.items.isEmpty();
    }
    
    public void clear() {
        this.items.clear();
    }
    
    public boolean contains(T item) {
        return this.items.contains(item);
    }
    
    public T getItem(String name) {
    	for(T item : this.items) {
    		if(item.isNamed(name)) {
    			return item;
    		}
    	}
    	return null;
    }
    
    /*public T getItem(int index) {
        if (index >= 0 && index < this.items.size()) {
            return this.items.get(index);
        }
        return null;
    }*/

    /**
     * Obtiene el nombre actual de la categoría.
     *
     * @return El nombre de la categoría en formato texto.
     */
    public String getNameCategory() {
        return this.name;
    }

    /**
     * Modifica el nombre de la categoría.
     *
     * @param name El nuevo nombre que se le quiere asignar.
     */
    public void setNameCategory(String name) {
        this.name = name;
    }
    
    public boolean isNamed(String name) {
    	return this.name.equalsIgnoreCase(name);
    }
}