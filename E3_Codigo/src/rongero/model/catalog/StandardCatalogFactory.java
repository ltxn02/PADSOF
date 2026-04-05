package model.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
/**
 * Implementación concreta de la factoría de catálogo.
 * Se encarga de instanciar productos individuales y de gestionar la lógica de
 * creación de Packs, incluyendo la consolidación de categorías (Composite).
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class StandardCatalogFactory implements ICatalogFactory {
    /**
     * Crea una instancia de un producto nuevo (por defecto un Comic).
     * * @param name  Nombre del producto.
     * @param desc  Descripción del producto.
     * @param price Precio de venta.
     * @param img   Ruta de la imagen.
     * @param cats  Lista de categorías asociadas.
     * @param stock Unidades iniciales en inventario.
     * @return Una nueva instancia de Comic con valores genéricos para los campos específicos.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public NewProduct createProduct(String name, String desc, double price, String img, List<Category> cats, int stock) {
       return new Comic(name, desc, price, img, new ArrayList<>(cats), stock,
                new ArrayList<>(), null, 0, "Genérica", 2024, new ArrayList<>());
    }
    /**
     * Crea un Pack combinando múltiples productos existentes.
     * Implementa la lógica de negocio para que el Pack herede automáticamente todas
     * las categorías de los productos que lo componen.
     * * @param name    Nombre del pack.
     * @param desc    Descripción del pack.
     * @param price   Precio final del pack.
     * @param content Lista de productos que integran el pack.
     * @param stock   Cantidad de packs disponibles.
     * @return Una nueva instancia de Pack con las categorías consolidadas.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    @Override
    public Pack createPack(String name, String desc, double price, List<NewProduct> content, int stock) {
        Set<Category> combinedCategories = new HashSet<>();
        for (NewProduct p : content) {
            combinedCategories.addAll(p.getCategories());
        }

        return new Pack(name, desc, price, "img/pack_default.jpg",
                new ArrayList<>(combinedCategories), stock, new ArrayList<>(), content);
    }
}