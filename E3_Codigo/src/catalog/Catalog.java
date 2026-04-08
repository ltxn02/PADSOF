package catalog;
import java.util.*;
import utils.*;
import discounts.*;

/**
 * Clase que gestiona el catálogo completo de productos de la tienda.
 * 
 * @author Lidia Martín
 * @version 2.1
 */
public class Catalog {
	private List<Category> categories;
	private List<AgeRange> ageRanges;
	private List<NewProduct> productsOnSale;
	private Map<AgeRange, List<Game>> gamesByAge;
	
	/**
	 * Constructor para crear un catálogo con contenido inicial.
	 * 
	 * Inicializa el catálogo con categorías, rangos de edad y productos existentes.
	 * Este constructor se usa típicamente para cargar un catálogo desde persistencia.
	 *
	 * @param categories   Lista de {@link Category} existentes. Si es nulo, se ignora.
	 * @param ageRanges    Lista de {@link AgeRange} existentes. Si es nulo, se ignora.
	 * @param products     Lista de {@link NewProduct} existentes. Si es nulo, se ignora.
	 */
	public Catalog(ArrayList<Category> categories, ArrayList<AgeRange> ageRanges, ArrayList<NewProduct> products) {
		this.categories = categories;
		this.ageRanges = ageRanges;
		this.productsOnSale = products;
		this.gamesByAge = new HashMap<>();
	}
	
	/**
	 * Constructor para crear un catálogo vacío.
	 * 
	 * Inicializa un catálogo sin productos, categorías ni rangos de edad.
	 * Los datos se añaden posteriormente mediante {@link #addProductOnSale(ItemType, Map)}
	 * y {@link #addAgeRange(int, int)}.
	 */
	public Catalog() {
		this(new ArrayList<Category>(), new ArrayList<AgeRange>(), new ArrayList<NewProduct>());
	}
	
	/**
	 * Añade un nuevo rango de edad al catálogo.
	 * 
	 * Crea un nuevo {@link AgeRange} si no existe ya un rango idéntico.
	 * Esto se utiliza para permitir filtrados de juegos por edad recomendada.
	 * Evita duplicados mediante comparación con {@link AgeRange#equalTo(int, int)}.
	 *
	 * @param min La edad mínima del rango (inclusive). Ejemplo: 4.
	 * @param max La edad máxima del rango (inclusive). Ejemplo: 10.
	 */
	public void addAgeRange(int min, int max) {
		for(AgeRange a: this.ageRanges) {
			if(a.equalTo(min, max) == true) {
				return;
			}
		}
		AgeRange a = new AgeRange(min, max);
		this.ageRanges.add(a);
		this.gamesByAge.put(a, new ArrayList<Game>());
	}
	
	/**
	 * Cambia la visibilidad de un rango de edad en el catálogo.
	 * 
	 * Rangos invisibles no aparecen en búsquedas de filtrado pero los datos se mantienen.
	 *
	 * @param ageRange El rango de edad a modificar.
	 * @param visible  {@code true} para hacerlo visible, {@code false} para ocultarlo.
	 */
	public void markAgeRangeAs(AgeRange ageRange, boolean visible) {
		ageRange.changeVisibility(visible);
	}
	
	/**
	 * Organiza todos los juegos del catálogo en grupos por su rango de edad recomendado.
	 * 
	 * Itera sobre todos los productos, identifica los que son juegos ({@link Game})
	 * y los clasifica automáticamente en el índice {@link #gamesByAge} según su rango
	 * de edad. Esto optimiza las búsquedas posteriores por edad.
	 */
	public void organiseGamesByAgeRange() {
		for(NewProduct p: this.productsOnSale) {
			if(p instanceof Game) {
				this.organiseGame((Game)p);
			}
		}
	}
	
	/**
	 * Añade un nuevo producto al catálogo o incrementa el stock de uno existente.
	 *
	 * @param itemType El tipo de producto a crear ({@link ItemType}: COMIC, GAME, FIGURINE, PACK).
	 * @param data     Mapa con todos los atributos del producto.
	 * 
	 * @throws IllegalArgumentException Si los datos no cumplen las validaciones.
	 *                                  El error se captura y registra sin lanzar excepción al llamador.
	 */
	public void addProductOnSale(ItemType itemType, Map<String, Object> data) {
		try {
			this.validateData(itemType, data);
			
			NewProduct p = productNamed((String)data.get("name"));
			if(p != null) {
				this.addExistingProduct(p, (Integer) data.get("stock"));
			} else {
				String name = (String) data.get("name"), description = (String) data.get("description"), picturePath = (String) data.get("picturePath");
				double price = (Double) data.get("price");
				int stock = (Integer) data.get("stock");
				IDiscount discount = (IDiscount) data.get("discount");
				@SuppressWarnings("unchecked")
				ArrayList<Category> categories = data.get("categories") != null ? (ArrayList<Category>) data.get("categories") : new ArrayList<>();
				@SuppressWarnings("unchecked")
				ArrayList<Review> reviews = data.get("reviews") != null ? (ArrayList<Review>) data.get("reviews") : new ArrayList<>();
				
				switch(itemType) {
					case COMIC: {
						@SuppressWarnings("unchecked")
		                ArrayList<String> authors = (ArrayList<String>) data.get("writtenBy");
						
						this.productsOnSale.add(new Comic(
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
						
						this.productsOnSale.add(new Game(
								name, description, price, picturePath, stock, categories, reviews, discount,
								(Integer) data.get("nPlayers"), mechanics,
								(AgeRange) data.get("ageRange")));
						break;
					}
					case FIGURINE: {
						this.productsOnSale.add(new Figurine(
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
		                ArrayList<NewProduct> products = (ArrayList<NewProduct>) data.get("products");
						
						this.productsOnSale.add(new Pack(
								name, description, price, picturePath, stock, categories, reviews, products));
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			System.err.println("Product couldn't be loaded: " + e.getMessage());
		}
	}
	
	/**
	 * Construye una lista de productos a partir de una cadena con nombres de productos.
	 * 
	 * Útil para crear packs que hacen referencia a otros productos mediante sus nombres.
	 * Realiza búsqueda de cada producto por nombre y lanza excepción si alguno no existe.
	 *
	 * @param productStr Cadena con nombres de productos separados por comas.
	 *                   Ejemplo: "One Piece Vol 1, Catan, Goku Figure".
	 * 
	 * @return Un {@code ArrayList<NewProduct>} con los productos encontrados,
	 *         en el mismo orden que aparecen en la cadena.
	 * 
	 * @throws IllegalArgumentException Si alguno de los productos no existe en el catálogo.
	 */
	public ArrayList<NewProduct> packProducts(String productStr) {
		ArrayList<NewProduct> packItems = new ArrayList<>();
		
		String[] names = productStr.split(",");
		for(String name: names) {
			String trimmedName = name.trim();
			
			NewProduct p = this.productNamed(name);
			
			if(p != null) {
				packItems.add(p);
			} else {
				throw new IllegalArgumentException("Component product not found in catalog: " + trimmedName);
			}
		}
		return packItems;
	}
	
	/**
	 * Obtiene una lista de todos los productos visibles (activos) en el catálogo.
	 * 
	 * Los productos invisibles/inactivos no aparecen pero se mantienen en la base de datos.
	 * Se utiliza para mostrar el catálogo a clientes y usuarios.
	 *
	 * @return Una lista de {@link NewProduct} que están marcados como activos.
	 *         Si no hay productos visibles, retorna una lista vacía.
	 */
	public List<NewProduct> visibleProducts() {
		List<NewProduct> products = new ArrayList<>();
		for(NewProduct p: this.productsOnSale) {
			if(p.isActive() == true) {
				products.add(p);
			}
		}
		return products;
	}
	
	/**
	 * Obtiene una lista de TODOS los productos en el catálogo, incluyendo inactivos.
	 * 
	 * Retorna la lista completa sin filtrar. Se usa internamente y para operaciones
	 * administrativas que necesitan ver todos los productos.
	 *
	 * @return Una lista de {@link NewProduct} con todos los productos sin restricciones.
	 *         Si el catálogo está vacío, retorna una lista vacía.
	 */
	public List<NewProduct> allProducts() {
		return this.productsOnSale;
	}
	
	/**
	 * Busca productos en el catálogo que contengan un término de búsqueda.
	 * 
	 * @param str El término de búsqueda (cadena de texto).
	 *            La búsqueda es parcial y no case-sensitive.
	 * 
	 * @return Una lista de {@link NewProduct} que contienen el término de búsqueda
	 *         y están activos en el catálogo.
	 *         Si no hay coincidencias, retorna una lista vacía.
	 */
	public List<NewProduct> searchProducts(String str) {
		List<NewProduct> products = new ArrayList<>();
		for (NewProduct p: this.productsOnSale) {
			if(p.contains(str) == true && p.isActive() == true) {
				products.add(p);
			}
		}
		return products;
	}
	
	/**
	 * Filtra los juegos del catálogo por rango de edad recomendado.
	 * 
	 * Retorna todos los juegos cuyo rango de edad recomendado está contenido dentro
	 * del rango de filtrado especificado [min, max].
	 * 
	 * @param min La edad mínima del rango de filtrado.
	 * @param max La edad máxima del rango de filtrado.
	 * 
	 * @return Una lista de {@link Game} cuyos rangos de edad recomendado están
	 *         contenidos en [min, max] y están activos.
	 *         Si no hay juegos que coincidan, retorna una lista vacía.
	 */
	public List<Game> filterByAge(int min, int max) {
		List<Game> products = new ArrayList<>();
		
		for(AgeRange a: this.ageRanges) {
			if(a.containedIn(min, max) == true) {
				
				for(Game p: this.gamesByAge.get(a)) {
					if(p.isActive() == true)
						products.add(p);
				}
			}
		}
		
		return products;
	}
	
	/**
	 * Obtiene una representación en texto del catálogo completo.
	 *
	 * @return Una cadena formateada con lista de todos los productos.
	 */
	@Override
	public String toString() {
		return "Full catalog:\n" + this.allProducts();
	}

    
    
    
	// ═══════════════════════════════════════════════════════════
    // HELPERS / MÉTODOS AUXILIARES PRIVADOS
    // ═══════════════════════════════════════════════════════════

	
	/**
	 * Añade un rango de edad ya instanciado al catálogo.
	 * 
	 * Método privado auxiliar que añade un {@link AgeRange} específico si no existe
	 * ya un rango idéntico. Se usa internamente cuando se organizan juegos por edad.
	 *
	 * @param ageRange El {@link AgeRange} a añadir.
	 */
	private void addAgeRange(AgeRange ageRange) {
		for(AgeRange a: this.ageRanges) {
			if(a.equalTo(ageRange)) {
				return;
			}
		}
		this.ageRanges.add(ageRange);
		this.gamesByAge.put(ageRange, new ArrayList<Game>());
	}
	
	/**
	 * Organiza un juego específico en el índice de juegos por rango de edad.
	 * 
	 * Método privado que:
	 * <ol>
	 *   <li>Busca todos los rangos de edad que contienen el rango del juego</li>
	 *   <li>Añade el juego a esos rangos en el mapa gamesByAge</li>
	 *   <li>Crea un nuevo rango si el del juego no existe aún</li>
	 * </ol>
	 *
	 * @param p El juego ({@link Game}) a organizar.
	 */
	private void organiseGame(Game p) {
		for(AgeRange age: this.ageRanges) {
			if(age.containedIn(p.getAgeRange()) == true) {
				this.gamesByAge.get(age).add(p);
			}
		}
		
		this.addAgeRange(p.getAgeRange());
		this.gamesByAge.get(p.getAgeRange()).add(p);
	}
	
	/**
	 * Busca un producto en el catálogo por su nombre exacto.
	 * 
	 * Búsqueda lineal que retorna el primer producto cuyo nombre coincida exactamente.
	 * Se usa internamente para evitar duplicados al añadir productos.
	 *
	 * @param name El nombre del producto a buscar.
	 * 
	 * @return El {@link NewProduct} encontrado, o {@code null} si no existe.
	 */
	private NewProduct productNamed(String name) {
		for(NewProduct p: this.productsOnSale) {
			if(p.isNamed(name)) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Incrementa el stock de un producto existente.
	 * 
	 * Se usa cuando se intenta añadir un producto cuyo nombre ya existe en el catálogo.
	 * En lugar de crear un duplicado, simplemente se aumenta el stock del producto existente.
	 *
	 * @param p     El producto ({@link NewProduct}) existente.
	 * @param stock La cantidad de unidades a añadir.
	 */
	private void addExistingProduct(NewProduct p, int stock) {
		p.increaseStock(stock);
	}
	
	/**
	 * Valida que todos los datos necesarios para un tipo de producto están presentes y tienen el tipo correcto.
	 * 
	 * <h3>Validaciones realizadas:</h3>
	 * <ol>
	 *   <li><strong>Datos comunes (todos los tipos):</strong>
	 *       name (String), description (String), price (Double), picturePath (String), stock (Integer)
	 *   </li>
	 *   <li><strong>Datos opcionales comunes:</strong>
	 *       categories (ArrayList), reviews (ArrayList), discount (Discount)
	 *   </li>
	 *   <li><strong>Datos específicos según ItemType:</strong>
	 *       <ul>
	 *         <li><strong>COMIC:</strong> nPages, publisher, publicationYear, writtenBy</li>
	 *         <li><strong>GAME:</strong> nPlayers, mechanics, ageRange</li>
	 *         <li><strong>FIGURINE:</strong> height, width, depth, material, franchise</li>
	 *         <li><strong>PACK:</strong> products</li>
	 *       </ul>
	 *   </li>
	 * </ol>
	 * 
	 * Si alguna validación falla, se registra un error pero no se lanza excepción
	 * (el error se captura en {@link #addProductOnSale(ItemType, Map)}).
	 *
	 * @param itemType El tipo de producto a validar.
	 * @param data     Mapa con los datos a validar.
	 * 
	 * @throws IllegalArgumentException Si falta algún campo requerido o tiene tipo incorrecto.
	 *                                  Se captura internamente y se registra.
	 */
	private void validateData(ItemType itemType, Map<String, Object> data) {
		// 1. Validate common attributes (obligatory)
		try {
			this.checkField(data, "name", String.class);
			this.checkField(data, "description", String.class);
			this.checkField(data, "price", Double.class);
			this.checkField(data, "picturePath", String.class);
			this.checkField(data, "stock", Integer.class);
			if(data.get("categories") != null) {
				this.checkField(data, "categories", ArrayList.class);
			}
			if(data.get("reviews") != null) {
				this.checkField(data, "reviews", ArrayList.class);
			}
			if(data.get("discount") != null) {
				this.checkField(data, "discount", Discount.class);
			}
			
			switch(itemType) {
				case COMIC: {
					checkField(data, "nPages", Integer.class);
					checkField(data, "publisher", String.class);
					checkField(data, "publicationYear", Integer.class);
					checkField(data, "writtenBy", ArrayList.class);
					break;
				}
				case GAME: {
					checkField(data, "nPlayers", Integer.class);
					checkField(data, "mechanics", ArrayList.class);
					checkField(data, "ageRange", AgeRange.class);
					break;
				}
				case FIGURINE: {
					checkField(data, "height", Double.class);
					checkField(data, "width", Double.class);
					checkField(data, "depth", Double.class);
					checkField(data, "material", String.class);
					checkField(data, "franchise", String.class);
					break;
				}
				case PACK: {
					checkField(data, "products", ArrayList.class);
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("Error validating data: " + e.getMessage());
		}
	}
	
	/**
	 * Valida que un campo del mapa existe y tiene el tipo esperado.
	 * 
	 * Método privado auxiliar que lanza excepción si:
	 * <ul>
	 *   <li>La clave no existe en el mapa</li>
	 *   <li>El valor es nulo</li>
	 *   <li>El valor no es instancia de la clase esperada</li>
	 * </ul>
	 *
	 * @param data         El mapa a validar.
	 * @param key          La clave del campo a verificar.
	 * @param expectedType La clase que se espera para el valor.
	 * 
	 * @throws IllegalArgumentException Si el campo no existe, es nulo, o tiene tipo incorrecto.
	 */
	private void checkField(Map<String, Object> data, String key, Class<?> expectedType) throws IllegalArgumentException {
		Object value = data.get(key);
		
		if(value == null) {
			throw new IllegalArgumentException("Invalid key: " + key + "does not exist");
		}
		
		if(!expectedType.isInstance(value)) {
			throw new IllegalArgumentException("Invalid class: " + key + " must be of " + expectedType + " class");
		}
	}
}
