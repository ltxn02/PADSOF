package products.catalog;

import java.util.*;
import products.*;
import utils.*;
import discounts.*;

/**
 * Catálogo especializado para productos nuevos (Comic, Game, Figurine, Pack).
 * Hereda de BaseCatalog pero añade lógica específica para NewProduct.
 * 
 * @author Lidia Martín
 * @version 2.0
 */
public class NewProductCatalog extends BaseCatalog<NewProduct> {
    private List<AgeRange> ageRanges;
    private Map<AgeRange, List<Game>> gamesByAge;
    
    /**
     * Constructor con contenido inicial.
     */
    public NewProductCatalog(ArrayList<Category> categories, ArrayList<AgeRange> ageRanges, 
                             ArrayList<NewProduct> products) {
        super(categories, products);
        this.ageRanges = ageRanges != null ? ageRanges : new ArrayList<>();
        this.gamesByAge = new HashMap<>();
    }
    
    /**
     * Constructor vacío.
     */
    public NewProductCatalog() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
    
    /**
     * Obtiene los rangos de edad.
     */
    public List<AgeRange> getAgeRanges() {
        return this.ageRanges;
    }
    
    /**
     * Añade un rango de edad.
     */
    public void addAgeRange(int min, int max) {
        for (AgeRange a : this.ageRanges) {
            if (a.equalTo(min, max)) {
                return;
            }
        }
        AgeRange a = new AgeRange(min, max);
        this.ageRanges.add(a);
        this.gamesByAge.put(a, new ArrayList<>());
    }
    
    /**
     * Cambia la visibilidad de un rango de edad.
     */
    public void markAgeRangeAs(AgeRange ageRange, boolean visible) {
        ageRange.changeVisibility(visible);
    }
    
    /**
     * Filtra juegos por rango de edad (ESPECÍFICO DE NEWPRODUCT).
     */
    public List<Game> filterByAge(int min, int max) {
        List<Game> result = new ArrayList<>();
        
        for (AgeRange a : this.ageRanges) {
            if (a.containedIn(min, max)) {
                List<Game> games = this.gamesByAge.get(a);
                if (games != null) {
                    for (Game g : games) {
                        if (g.isActive()) {
                            result.add(g);
                        }
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * Construye lista de productos desde cadena para packs.
     */
    public ArrayList<NewProduct> packProducts(String productStr) {
        ArrayList<NewProduct> packItems = new ArrayList<>();
        String[] names = productStr.split(",");
        
        for (String name : names) {
            String trimmedName = name.trim();
            NewProduct p = this.productNamed(trimmedName);
            
            if (p != null) {
                packItems.add(p);
            } else {
                throw new IllegalArgumentException("Component product not found: " + trimmedName);
            }
        }
        
        return packItems;
    }
    
    /**
     * Organiza todos los juegos por rango de edad.
     */
    public void organiseGamesByAgeRange() {
        for (NewProduct p : this.products) {
            if (p instanceof Game) {
                this.organiseGame((Game) p);
            }
        }
    }
    
    /**
     * Añade un producto nuevo con toda la lógica de validación.
     * SOBRESCRIBE el método abstracto de BaseCatalog.
     */
    @Override
    public void addProduct(Map<String, Object> data) {
        // Redirige a addProductOnSale para mantener la lógica antigua
        ItemType type = (ItemType) data.get("itemType");
        if (type != null) {
            this.addProductOnSale(type, data);
        }
    }
    
    /**
     * Añade un producto nuevo manteniendo la lógica EXACTA antigua.
     */
    public void addProductOnSale(ItemType itemType, Map<String, Object> data) {
        try {
            this.validateData(data);
            
            NewProduct p = productNamed((String) data.get("name"));
            if (p != null) {
                this.addExistingProduct(p, (Integer) data.get("stock"));
            } else {
                String name = (String) data.get("name");
                String description = (String) data.get("description");
                String picturePath = (String) data.get("picturePath");
                double price = (Double) data.get("price");
                int stock = (Integer) data.get("stock");
                IDiscount discount = (IDiscount) data.get("discount");
                
                @SuppressWarnings("unchecked")
                ArrayList<Category> categories = data.get("categories") != null ? 
                    (ArrayList<Category>) data.get("categories") : new ArrayList<>();
                @SuppressWarnings("unchecked")
                ArrayList<Review> reviews = data.get("reviews") != null ? 
                    (ArrayList<Review>) data.get("reviews") : new ArrayList<>();
                
                switch (itemType) {
                    case COMIC: {
                        @SuppressWarnings("unchecked")
                        ArrayList<String> authors = (ArrayList<String>) data.get("writtenBy");
                        
                        this.products.add(new Comic(
                            name, description, price, picturePath, stock, categories, reviews, discount,
                            (Integer) data.get("nPages"),
                            (String) data.get("publisher"),
                            (Integer) data.get("publicationYear"),
                            authors));
                        break;
                    }
                    case GAME: {
                        @SuppressWarnings("unchecked")
                        ArrayList<String> mechanics = (ArrayList<String>) data.get("mechanics");
                        
                        Game game = new Game(
                            name, description, price, picturePath, stock, categories, reviews, discount,
                            (Integer) data.get("nPlayers"), mechanics,
                            (AgeRange) data.get("ageRange"));
                        
                        this.products.add(game);
                        this.organiseGame(game);
                        break;
                    }
                    case FIGURINE: {
                        this.products.add(new Figurine(
                            name, description, price, picturePath, stock, categories, reviews, discount,
                            (Double) data.get("height"),
                            (Double) data.get("width"),
                            (Double) data.get("depth"),
                            (String) data.get("material"),
                            (String) data.get("franchise")));
                        break;
                    }
                    case PACK: {
                        @SuppressWarnings("unchecked")
                        ArrayList<NewProduct> packProducts = (ArrayList<NewProduct>) data.get("products");
                        
                        this.products.add(new Pack(
                            name, description, price, picturePath, stock, categories, reviews, packProducts));
                        break;
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Product couldn't be loaded: " + e.getMessage());
        }
    }
    
    @Override
    public void validateData(Map<String, Object> data) throws IllegalArgumentException {
        try {
            this.checkField(data, "name", String.class);
            this.checkField(data, "description", String.class);
            this.checkField(data, "price", Double.class);
            this.checkField(data, "picturePath", String.class);
            this.checkField(data, "stock", Integer.class);
        } catch (Exception e) {
            System.err.println("Error validating data: " + e.getMessage());
        }
    }
    
    // ════════════════════════════════════════════════════════════════
    // MÉTODOS PRIVADOS
    // ════════════════════════════════════════════════════════════════
    
    private NewProduct productNamed(String name) {
        for (NewProduct p : this.products) {
            if (p.isNamed(name)) {
                return p;
            }
        }
        return null;
    }
    
    private void addExistingProduct(NewProduct p, int stock) {
        p.increaseStock(stock);
    }
    
    private void organiseGame(Game game) {
        for (AgeRange age : this.ageRanges) {
            if (age.containedIn(game.getAgeRange())) {
                List<Game> gamesList = this.gamesByAge.get(age);
                if (gamesList != null) {
                    gamesList.add(game);
                }
            }
        }
        
        this.addAgeRange(game.getAgeRange());
        List<Game> gamesList = this.gamesByAge.get(game.getAgeRange());
        if (gamesList != null) {
            gamesList.add(game);
        }
    }
    
    private void addAgeRange(AgeRange ageRange) {
        for (AgeRange a : this.ageRanges) {
            if (a.equalTo(ageRange)) {
                return;
            }
        }
        this.ageRanges.add(ageRange);
        this.gamesByAge.put(ageRange, new ArrayList<>());
    }
    
    private void checkField(Map<String, Object> data, String key, Class<?> expectedType) 
            throws IllegalArgumentException {
        Object value = data.get(key);
        
        if (value == null) {
            throw new IllegalArgumentException("Invalid key: " + key + " does not exist");
        }
        
        if (!expectedType.isInstance(value)) {
            throw new IllegalArgumentException("Invalid class: " + key + " must be of " + expectedType);
        }
    }
}