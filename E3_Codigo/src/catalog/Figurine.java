package catalog;

import java.util.ArrayList;
import utils.*;
import discounts.*;

/**
 * Clase que representa una figura de colección (Figurine) dentro del catálogo de productos.
 * Hereda de la clase abstracta {@link Product} y añade características físicas específicas
 * de las figuras, como sus dimensiones (alto, ancho, profundidad), el material
 * de fabricación y la franquicia a la que pertenece (ej: Marvel, Star Wars, Anime).
 * * @author Iván Sánchez
 * @version 1.2
 */
public class Figurine extends Product implements java.io.Serializable {
    private double height;
    private double width;
    private double depth;
    private String material;
    private String franchise;

    /**
     * Constructor para inicializar una nueva figura en el catálogo.
     * * @param name        Nombre de la figura.
     * @param description Descripción detallada del artículo.
     * @param price       Precio base de la figura en euros.
     * @param image       Ruta relativa de la imagen representativa del producto.
     * @param stock       Cantidad de unidades iniciales disponibles en el inventario.
     * @param categories  Lista de categorías a las que pertenece la figura.
     * @param reviews     Lista de valoraciones hechas por los usuarios.
     * @param discount    Descuento aplicable al producto (interfaz IDiscount). Puede ser null.
     * @param height      Altura física de la figura en centímetros.
     * @param width       Anchura física de la figura en centímetros.
     * @param depth       Profundidad física de la figura en centímetros.
     * @param material    Material del que está hecha la figura (ej: PVC, Resina, Vinilo).
     * @param franchise   Franquicia o licencia a la que pertenece (ej: Dragon Ball, DC Comics).
     * @throws IllegalArgumentException Si alguna de las dimensiones (altura, anchura o profundidad) es menor que 0.
     */
    public Figurine(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, IDiscount discount, double height, double width, double depth, String material, String franchise) {
        super(name, description, price, image, stock, categories, reviews, discount);
        if (height < 0 || width < 0 || depth < 0) {
            throw new IllegalArgumentException("Argumentos inválidos: Las dimensiones no pueden ser negativas.");
        }

        this.height = height;
        this.width = width;
        this.depth = depth;
        this.material = material;
        this.franchise = franchise;
    }
}