package products;

import java.util.ArrayList;

import products.catalog.Category;
import utils.*;

/**
 * Clase que representa un Pack o lote de productos dentro del catálogo.
 * Hereda de {@link NewProduct} y se compone de una agrupación lógica de dos
 * o más productos individuales. Cumple con la restricción de multiplicidad (2...*)
 * establecida en el diseño del sistema.
 * * @author Iván Sánchez
 * @version 1.5
 */
public class Pack extends NewProduct implements java.io.Serializable {
    private static int lastPackId = 1;
    private int packId;

    // La relación de composición que viene del rombo negro en el UML
    private ArrayList<NewProduct> products;

    /**
     * Constructor para inicializar un nuevo Pack en el sistema.
     *
     * @param name            Nombre del pack promocional.
     * @param description     Descripción detallada del lote.
     * @param price           Precio total conjunto (generalmente rebajado respecto a la suma individual).
     * @param image           Ruta relativa de la imagen representativa del pack.
     * @param stock           Cantidad de packs disponibles en inventario.
     * @param categories      Lista de categorías (generalmente heredadas de los productos que contiene).
     * @param reviews         Lista de reseñas y valoraciones asociadas al pack.
     * @param initialProducts Lista inicial de productos que conforman el pack.
     * @throws IllegalArgumentException Si la lista de productos iniciales es nula o contiene menos de 2 elementos.
     */
    public Pack(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, ArrayList<NewProduct> initialProducts) {
        // 1. Llamamos al constructor de la clase padre (NewProduct)
        super(name, description, price, image, stock, categories, reviews);

        // 2. Validamos la regla de multiplicidad 2...* que pusisteis en el diagrama
        if (initialProducts == null || initialProducts.size() < 2) {
            throw new IllegalArgumentException("Error: Un Pack debe contener al menos 2 productos iniciales.");
        }

        this.packId = Pack.lastPackId;
        Pack.lastPackId++;

        // Inicializamos la lista de productos del pack
        this.products = new ArrayList<>(initialProducts);
    }

    /**
     * Añade un nuevo producto individual al pack.
     * Evita que se introduzcan productos duplicados en el mismo lote.
     *
     * @param p El producto ({@link NewProduct}) a añadir.
     * @return true si se ha añadido correctamente, false si el producto es nulo o ya estaba incluido.
     */
    public boolean addItem(NewProduct p) {
        if (p != null && !this.products.contains(p)) {
            return this.products.add(p);
        }
        return false;
    }

    /**
     * Elimina un producto del pack.
     * Respeta la regla estructural de negocio: no permite eliminar un producto
     * si esto causa que el pack se quede con menos de 2 elementos.
     *
     * @param p El producto a eliminar.
     * @return true si se eliminó correctamente, false si el producto no estaba en el pack o si se infringe la regla de los 2 elementos mínimos.
     */
    public boolean removeItem(NewProduct p) {
        if (!this.products.contains(p)) {
            return false;
        }

        // Protegemos el diseño: no dejamos que el pack se quede con menos de 2 elementos
        if (this.products.size() <= 2) {
            System.out.println("Error: No se puede eliminar el producto. El pack debe mantener al menos 2 elementos (2...*).");
            return false;
        }

        return this.products.remove(p);
    }

    /**
     * Obtiene la lista completa de los productos que componen este pack.
     *
     * @return Una lista ({@link ArrayList}) de los productos contenidos.
     */
    public ArrayList<NewProduct> getProducts() {
        return this.products;
    }
    
    /**
     * Sobreescribe el set de productos de un pack.
     * 
     * @param newProducts El nuevo array de productos del pack.
     */
    public void editPackInfo(ArrayList<NewProduct> newProducts) {
    	this.clearPack();
    	for(NewProduct p: newProducts) {
    		this.addItem(p);
    	}
    }
    
    private void clearPack() {
    	this.products.clear();
    }
}