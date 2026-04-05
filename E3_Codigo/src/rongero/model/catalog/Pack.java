package model.catalog;

import java.util.ArrayList;
import java.util.List;
import util.Review;
/**
 * Clase que representa un Pack de productos, implementando el patrón Composite.
 * Un Pack se comporta como un NewProduct individual, pero internamente contiene
 * una colección de al menos dos productos.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */

public class Pack extends NewProduct {
    private List<NewProduct> products;
    /**
     * Constructor para la creación de un Pack.
     * Valida que el pack contenga al menos dos productos iniciales para cumplir
     * con las reglas de negocio de multiplicidad.
     * * @param name            Nombre del pack.
     * @param description     Descripción detallada del pack y su contenido.
     * @param price           Precio de venta del conjunto.
     * @param image           Ruta de la imagen representativa del pack.
     * @param categories      Categorías a las que se asigna el pack.
     * @param stock           Unidades disponibles del pack.
     * @param reviews         Lista de reseñas y valoraciones de usuarios.
     * @param initialProducts Lista de productos que forman la base del pack (mínimo 2).
     * @throws IllegalArgumentException si la lista de productos es nula o contiene menos de 2 elementos.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public Pack(String name, String description, double price, String image,
                List<Category> categories, int stock, List<Review> reviews,
                List<NewProduct> initialProducts) {
        super(name, description, price, image, categories, stock, reviews);


        if (initialProducts == null || initialProducts.size() < 2) {
            throw new IllegalArgumentException("Un Pack debe tener al menos 2 productos.");
        }
        this.products = new ArrayList<>(initialProducts);
    }

    @Override
    /**
     * Obtiene el precio del pack.
     * * @return El precio establecido para el conjunto de productos.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public double getPrice() {
      return super.getPrice();
    }
    /**
     * Devuelve la lista de productos contenidos dentro del pack.
     * * @return Lista de NewProduct que integran este pack.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public List<NewProduct> getProducts() {
        return products;
    }
}