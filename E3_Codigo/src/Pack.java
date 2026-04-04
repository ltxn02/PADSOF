import java.util.ArrayList;

public class Pack extends NewProduct {
    private static int lastPackId = 1;
    private int packId;

    // La relación de composición que viene del rombo negro en el UML
    private ArrayList<NewProduct> products;

    public Pack(String name, String description, double price, String image, ArrayList<Category> categories, int stock, ArrayList<Review> reviews, ArrayList<NewProduct> initialProducts) {
        // 1. Llamamos al constructor de la clase padre (NewProduct)
        super(name, description, price, image, categories, stock, reviews);

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
     * Añade un nuevo producto al pack
     * @param p El producto a añadir
     * @return true si se ha añadido correctamente, false en caso contrario
     */
    public boolean addItem(NewProduct p) {
        if (p != null && !this.products.contains(p)) {
            return this.products.add(p);
        }
        return false;
    }

    /**
     * Elimina un producto del pack, respetando la regla de un mínimo de 2 productos
     * @param p El producto a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
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

    // Getter opcional para poder leer qué tiene dentro el pack desde el main
    public ArrayList<NewProduct> getProducts() {
        return this.products;
    }
}