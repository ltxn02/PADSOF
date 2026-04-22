package products;

import java.util.ArrayList;
import utils.*;
import discounts.*;
import products.catalog.Category;

/**
 * Clase que representa un juego (Game) dentro del catálogo de productos.
 * Hereda de la clase abstracta {@link Product} e incluye características específicas
 * de los juegos de mesa o videojuegos, como el número de jugadores, las mecánicas
 * y el rango de edad recomendado.
 * * @author Iván Sánchez
 * @version 2.0
 */
public class Game extends Product implements java.io.Serializable{
    private int nPlayers;
    private ArrayList<String> mechanics;
    private AgeRange ageRange;

    /**
     * Constructor para inicializar un nuevo juego en el catálogo.
     * * @param name        Nombre del juego.
     * @param description Descripción detallada del juego.
     * @param price       Precio base del juego en euros.
     * @param image       Ruta relativa de la imagen representativa del producto.
     * @param stock       Cantidad de unidades iniciales disponibles en el inventario.
     * @param categories  Lista de categorías a las que pertenece el juego.
     * @param reviews     Lista de valoraciones hechas por los usuarios.
     * @param discount    Descuento aplicable al producto (interfaz IDiscount). Puede ser null.
     * @param nPlayers    Número de jugadores recomendados o requeridos.
     * @param mechanics   Lista de cadenas de texto con las mecánicas del juego (ej: "Gestión de recursos").
     * @param ageRange    Rango de edad recomendado para jugar.
     * @throws IllegalArgumentException Si el número de jugadores (nPlayers) es menor que 1.
     */
    public Game(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, IDiscount discount, int nPlayers, ArrayList<String> mechanics, AgeRange ageRange) {
        super(name, description, price, image, stock, categories, reviews, discount);
        if (nPlayers < 1) {
            throw new IllegalArgumentException("Argumentos inválidos: El número de jugadores no puede ser menor a 1");
        }
        this.nPlayers = nPlayers;
        this.mechanics = mechanics;
        this.ageRange = ageRange;
    }

    /**
     * Obtiene el rango de edad recomendado para jugar a este juego.
     * * @return Un objeto {@link AgeRange} que define la edad mínima y máxima.
     */
    public AgeRange getAgeRange() {
        return this.ageRange;
    }
    
    /**
     * Modifica la información específica de este juego.
     * @param discount		Descuento aplicable al producto (interfaz IDiscount). Puede ser null.
     * @param nPlayers		Número de jugadores recomendados o requeridos.
     * @param mechanics		Lista de cadenas de texto con las mecánicas del juego (ej: "Gestión de recursos").
     * @param ageRange		Rango de edad recomendado para jugar.
     */
    public void editGameInfo(IDiscount discount, int nPlayers, ArrayList<String> mechanics, AgeRange ageRange) {
    	super.setDiscount(discount);
    	this.nPlayers = nPlayers;
    	this.mechanics = mechanics;
    	this.ageRange = ageRange;
    }
}