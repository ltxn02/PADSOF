package catalog;

import java.util.ArrayList;
import utils.*;
import discounts.*;

/**
 * Clase que representa un cómic o manga (Comic) dentro del catálogo de productos.
 * Hereda de la clase abstracta {@link Product} e incorpora atributos específicos
 * de las publicaciones impresas, como el número de páginas, la editorial, el año
 * de publicación y la lista de autores/ilustradores.
 *
 * @author Iván Sánchez
 * @version 1.2
 */
public class Comic extends Product implements java.io.Serializable {
    private int nPages;
    private String publisher;
    private int publicationYear;
    private ArrayList<String> writtenBy;

    /**
     * Constructor para inicializar un nuevo cómic en el catálogo de la tienda.
     *
     * @param name            Nombre o título del cómic (ej: "Batman: Año Uno").
     * @param description     Descripción detallada o sinopsis de la obra.
     * @param price           Precio base del cómic en euros.
     * @param image           Ruta relativa de la imagen de la portada.
     * @param stock           Cantidad de unidades iniciales disponibles para la venta.
     * @param categories      Lista de categorías temáticas a las que pertenece.
     * @param reviews         Lista de valoraciones hechas por los clientes.
     * @param discount        Descuento aplicable al producto (interfaz IDiscount). Puede ser null.
     * @param nPages          Número total de páginas del cómic. Si el valor es menor o igual a 0, se ajustará automáticamente a 1.
     * @param publisher       Nombre de la editorial que lo publica (ej: "DC Comics", "Norma Editorial").
     * @param publicationYear Año original en el que fue publicado.
     * @param writtenBy       Lista con los nombres de los autores principales (guionistas y dibujantes).
     */
    public Comic(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, IDiscount discount, int nPages, String publisher, int publicationYear, ArrayList<String> writtenBy) {
        super(name, description, price, image, stock, categories, reviews, discount);

        // Validación del número de páginas: no puede ser 0 o negativo
        if (nPages <= 0) {
            this.nPages = 1;
        } else {
            this.nPages = nPages;
        }

        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.writtenBy = writtenBy;
    }
}