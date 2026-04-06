package catalog;
import java.util.*;
import utils.*;
import discounts.*;

public class Catalog {
	private List<Category> categories;
	private List<AgeRange> ageRanges;
	private List<NewProduct> productsOnSale;
	private Map<AgeRange, List<Game>> gamesByAge;
	
	public Catalog(ArrayList<Category> categories, ArrayList<AgeRange> ageRanges, ArrayList<NewProduct> products) {
		this.categories = categories;
		this.ageRanges = ageRanges;
		this.productsOnSale = products;
		this.gamesByAge = new HashMap<>();
	}
	
	public Catalog() {
		this(new ArrayList<Category>(), new ArrayList<AgeRange>(), new ArrayList<NewProduct>());
	}
	
	/*public void addCategory() {
		
	}*/
	
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
	
	private void addAgeRange(AgeRange ageRange) {
		for(AgeRange a: this.ageRanges) {
			if(a.equalTo(ageRange)) {
				return;
			}
		}
		this.ageRanges.add(ageRange);
		this.gamesByAge.put(ageRange, new ArrayList<Game>());
	}
	
	public void markAgeRangeAs(AgeRange ageRange, boolean visible) {
		ageRange.changeVisibility(visible);
	}
	
	public void organiseGamesByAgeRange() {
		for(NewProduct p: this.productsOnSale) {
			if(p instanceof Game) {
				this.organiseGame((Game)p);
			}
		}
	}
	
	private void organiseGame(Game p) {
		for(AgeRange age: this.ageRanges) {
			if(age.containedIn(p.getAgeRange()) == true) {
				this.gamesByAge.get(age).add(p);
			}
		}
		
		this.addAgeRange(p.getAgeRange());
		this.gamesByAge.get(p.getAgeRange()).add(p);
	}
	
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
	
	private NewProduct productNamed(String name) {
		for(NewProduct p: this.productsOnSale) {
			if(p.isNamed(name)) {
				return p;
			}
		}
		return null;
	}
	
	private void addExistingProduct(NewProduct p, int stock) {
		p.increaseStock(stock);
	}
	
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
	
	public List<NewProduct> visibleProducts() {
		List<NewProduct> products = new ArrayList<>();
		for(NewProduct p: this.productsOnSale) {
			if(p.isActive() == true) {
				products.add(p);
			}
		}
		return products;
	}
	
	public List<NewProduct> allProducts() {
		return this.productsOnSale;
	}
	
	public List<NewProduct> searchProducts(String str) {
		List<NewProduct> products = new ArrayList<>();
		for (NewProduct p: this.productsOnSale) {
			if(p.contains(str) == true && p.isActive() == true) {
				products.add(p);
			}
		}
		return products;
	}
	
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
	
	@Override
	public String toString() {
		return "Full catalog:\n" + this.allProducts();
	}
	
	private void validateData(ItemType itemType, Map<String, Object> data) {
		// 1. Validate common attributes (obligatory)
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
	}
	
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
