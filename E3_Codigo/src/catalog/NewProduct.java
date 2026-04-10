package catalog;

import java.util.ArrayList;
import java.util.List;
import utils.*;

/**
 * Clase abstracta que representa un producto nuevo (NewProduct) dentro de la tienda.
 * Hereda de {@link Item} y añade toda la lógica necesaria para la gestión de inventario,
 * incluyendo el stock físico real, el stock efectivo (reservado en carritos) y el
 * sistema de reseñas y valoraciones de los usuarios.
 * * @author Iván Sánchez y Taha Ridda
 * @version 2.1
 */
public abstract class NewProduct extends Item implements java.io.Serializable {
    private int stock;
    private int effectiveStock;
    private ArrayList<Review> reviews;

    /**
     * Constructor principal para inicializar un producto nuevo con todos sus datos.
     *
     * @param name        Nombre del producto.
     * @param description Descripción detallada del producto.
     * @param price       Precio de venta al público en euros.
     * @param image       Ruta relativa de la imagen del producto.
     * @param stock       Cantidad de unidades físicas disponibles inicialmente en el almacén.
     * @param categories  Lista de categorías a las que pertenece el producto.
     * @param reviews     Lista inicial de reseñas del producto.
     * @throws IllegalArgumentException Si el stock inicial es negativo o si la lista de categorías está vacía.
     */
    public NewProduct(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews) {
        super(name, description, price, image, categories);
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        } else if (categories.size() < 1) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.stock = stock;
        this.reviews = reviews;
    }

    /**
     * Constructor simplificado para inicializar un producto nuevo sin categorías ni reseñas previas.
     *
     * @param name        Nombre del producto.
     * @param description Descripción del producto.
     * @param price       Precio del producto en euros.
     * @param image       Ruta relativa de la imagen.
     * @param stock       Cantidad de unidades en stock.
     */
    public NewProduct(String name, String description, double price, String image, int stock) {
        this(name, description, price, image, stock, new ArrayList<Category>(), new ArrayList<Review>());
    }

    /**
     * Calcula la valoración media del producto basándose en las reseñas de los clientes.
     *
     * @return La media entera de las valoraciones (ej. de 1 a 5). Devuelve 0 si no hay reseñas.
     */
    public int calculateRating(){
        if (reviews == null || reviews.isEmpty()) {
            return 0; // Evitamos la división por cero
        }

        int rating = 0;
        for (Review review : reviews){
            rating += review.getRating();
        }

        rating /= reviews.size();
        return rating;
    }

    /**
     * Comprueba si el stock efectivo (unidades libres no reservadas en carritos) se ha agotado.
     *
     * @return true si no quedan unidades libres, false en caso contrario.
     */
    public boolean isEffectiveStockEmpty(){
        return this.effectiveStock == 0;
    }

    /**
     * Comprueba si hay suficiente stock efectivo para satisfacer una demanda específica.
     *
     * @param quantity Cantidad de unidades solicitadas.
     * @return true si el stock efectivo es mayor o igual a la cantidad solicitada.
     */
    public boolean isEffectiveStockHigher(int quantity){
        return this.effectiveStock >= quantity;
    }

    /**
     * Reduce el stock físico real del almacén (por ejemplo, cuando se confirma una venta).
     *
     * @param quantity Cantidad a restar del inventario.
     * @throws IllegalArgumentException Si se intenta restar una cantidad mayor al stock disponible.
     */
    public void decreaseStock(int quantity) throws IllegalArgumentException {
        if(quantity > this.stock) {
            throw new IllegalArgumentException("Invalid quantity, stock is lower");
        }
        this.stock -= quantity;
    }

    /**
     * Aumenta el stock físico real del almacén (por ejemplo, al recibir mercancía nueva).
     *
     * @param quantity Cantidad a sumar al inventario.
     * @throws IllegalArgumentException Si la cantidad a sumar es negativa.
     */
    public void increaseStock(int quantity) throws IllegalArgumentException {
        if(quantity < 0) {
            throw new IllegalArgumentException("Invalid quantity, stock cannot be negative");
        }
        this.stock += quantity;
    }

    /**
     * Reserva temporalmente una cantidad de productos, reduciendo el stock efectivo.
     * Se usa cuando un usuario añade el producto a su carrito pero aún no lo ha pagado.
     *
     * @param quantity Cantidad de unidades a reservar.
     * @throws IllegalArgumentException Si la cantidad es negativa.
     */
    public void orderProduct(int quantity) throws IllegalArgumentException{
        if (quantity < 0){
            throw new IllegalArgumentException("Invalid quantity, cannot be negative");
        }
        this.effectiveStock -= quantity;
        super.registerTime();
    }

    /**
     * Libera productos que estaban reservados, devolviéndolos al stock efectivo.
     * Se usa cuando un usuario elimina el producto de su carrito o caduca la sesión.
     *
     * @param quantity Cantidad de unidades a liberar.
     * @param isAll    true si se liberan todas las unidades reservadas y se deben resetear las marcas de tiempo.
     * @throws IllegalArgumentException Si la cantidad es negativa.
     */
    public void returnProduct(int quantity, boolean isAll) throws IllegalArgumentException{
        if (quantity < 0){
            throw new IllegalArgumentException("Invalid quantity, cannot be negative");
        }

        this.effectiveStock += quantity;
        if (isAll){
            super.clearInstants();
        }
    }

    /**
     * Comprueba si el nombre del producto contiene una cadena de texto específica.
     * Útil para los motores de búsqueda del catálogo.
     *
     * @param str Cadena de texto a buscar.
     * @return true si el nombre incluye la cadena (ignorando mayúsculas/minúsculas).
     * @throws IllegalArgumentException Si la cadena proporcionada es null.
     */
    public boolean contains(String str) throws IllegalArgumentException {
        if(str == null) {
            throw new IllegalArgumentException("Invalid string");
        }
        return super.getName().toLowerCase().contains(str.toLowerCase());
    }

    /**
     * Añade una nueva reseña al historial de valoraciones del producto.
     *
     * @param review Objeto {@link Review} con la calificación y el comentario.
     */
    public void addReview(Review review) {
        this.reviews.add(review);
    }

    /**
     * Edita la información básica del producto y actualiza su stock si procede.
     *
     * @param name        Nuevo nombre.
     * @param description Nueva descripción.
     * @param price       Nuevo precio.
     * @param picturePath Nueva ruta de imagen.
     * @param stock       Nuevo stock (se ignora si es negativo).
     */
    public void editProductInfo(String name, String description, double price, String picturePath, int stock) {
        super.edit(name, description, price, picturePath);
        if(stock >= 0) this.stock = stock;
    }

    /**
     * Cambia la visibilidad del producto en la tienda (alta/baja lógica).
     *
     * @param visible true para hacerlo visible a los clientes, false para ocultarlo.
     */
    public void changeVisibilityProduct(boolean visible) {
        this.markAs(visible);
    }

    /**
     * Obtiene el precio base del producto.
     *
     * @return El precio de venta sin descuentos.
     */
    @Override
    public double getPrice() {
        return super.getPrice();
    }
    
    /**
     * Obtiene el stock del producto.
     *
     * @return El stock del producto.
     */
    public double getStock() {
        return this.stock;
    }
    
    /**
     * Registra el momento en que este producto es añadido al carrito de un cliente.
     */
    public void addedToCart() {
    	this.registerTime();
    }
    
    protected List<String> stockString() {
    	return List.of(Integer.toString(this.stock), Integer.toString(this.effectiveStock));
    }
}