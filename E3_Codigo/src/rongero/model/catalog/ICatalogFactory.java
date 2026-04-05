package model.catalog;

import java.util.List;
import util.Review;
/**
 * Interfaz que define la factoría abstracta para la creación de elementos del catálogo.
 * Permite desacoplar la lógica de creación de productos individuales y de
 * productos compuestos (Packs) de la interfaz de usuario.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public interface ICatalogFactory {
    /**
     * Crea una nueva instancia de un producto individual (NewProduct).
     * @param name  Nombre identificativo del producto.
     * @param desc  Descripción detallada del artículo.
     * @param price Precio base de venta.
     * @param img   Ruta o identificador de la imagen del producto.
     * @param cats  Lista de categorías a las que pertenece el producto.
     * @param stock Cantidad inicial de unidades disponibles.
     * @return Una instancia de NewProduct (o una de sus subclases como Comic, Game, etc.).
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    NewProduct createProduct(String name, String desc, double price, String img, List<Category> cats, int stock);

    Pack createPack(String name, String desc, double price, List<NewProduct> content, int stock);
}