package products;
import java.util.ArrayList;
import utils.*;
import discounts.*;
import products.catalog.Category;

/**
 * Clase abstracta que representa un producto estándar (Product) del catálogo.
 * Hereda de {@link NewProduct} y añade la funcionalidad de manejar descuentos
 * personalizados mediante el uso de la interfaz {@link IDiscount}, así como
 * un sistema de identificación única (productId).
 * @author Iván Sánchez
 * @version 2.0
 */
public abstract class Product extends NewProduct implements java.io.Serializable {
    private static int lastProductId = 1;
    private int productId;
    private IDiscount discount;

    /**
     * Constructor para inicializar un nuevo producto en el sistema.
     * Asigna automáticamente un ID único autoincremental al producto.
     *
     * @param name        Nombre del producto.
     * @param description Descripción detallada del producto.
     * @param price       Precio base original del producto en euros.
     * @param image       Ruta relativa de la imagen representativa del producto.
     * @param stock       Cantidad de unidades iniciales disponibles en el inventario.
     * @param categories  Lista de categorías a las que pertenece el producto.
     * @param reviews     Lista de valoraciones o reseñas hechas por los usuarios.
     * @param discount    Descuento aplicable al producto (interfaz IDiscount). Puede ser null si no tiene rebaja.
     */
    public Product(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, IDiscount discount) {
        super(name, description, price, image, stock, categories, reviews);
        this.discount = discount;
        this.productId = Product.lastProductId;
        Product.lastProductId++;
    }

    /**
     * Establece o actualiza el descuento aplicado a este producto.
     *
     * @param discount Objeto que implementa {@link IDiscount} con la nueva promoción, o null para quitarlo.
     */
    public void setDiscount(IDiscount discount) {
        this.discount = discount;
    }

    /**
     * Obtiene el descuento actualmente asociado a este producto.
     *
     * @return El objeto de descuento ({@link IDiscount}), o null si no tiene ninguno asignado.
     */
    public IDiscount getDiscount() {
        return this.discount;
    }

    /**
     * Obtiene el precio base original del producto, sin aplicar ninguna promoción.
     *
     * @return El precio base inalterado en euros.
     */
    @Override
    public double getPrice() {
        // 1. getPrice devuelve el precio base inalterado
        return super.getPrice();
    }

    /**
     * Calcula y obtiene el precio final del producto tras aplicarle su descuento activo.
     * Si el producto no tiene descuento o este ha expirado/es inválido, devuelve el precio original.
     *
     * @return El precio final de venta en euros.
     */
    public double getPriceWithDiscount() {
        double precioOriginal = this.getPrice();

        if (this.discount != null && this.discount.isValid()) {
            return this.discount.apply(precioOriginal);
        }

        return precioOriginal;
    }
}