package catalog;
import java.util.*;
import utils.*;

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
	
	/*public void addProductOnSale(ItemType itemType, Map<String, Object> data) {
		try {
			this.validateData(itemType, data);
			
			NewProduct p = productNamed((String)data.get("name"));
			if(p != null) {
				this.addExistingProduct(p, (Integer) data.get("stock"));
			} else {
				switch(itemType) {
					case COMIC: {
						this.productsOnSale.add(new Comic(
								(String) data.get("name"),
								(String) data.get("description"),
								(Double) data.get("price"),
								(String) data.get("picturePath"),
								(Integer) data.get("stock"),
								(ArrayList<Category>) data.getOrDefault("categories", new ArrayList<>()),
								(ArrayList<Review>) data.getOrDefault("reviews", new ArrayList<>()),
								(Discount) new Discount(0, null, null, null, null),
								(Integer) data.get("nPages"),
								(String) data.get("publisher"),
								(Integer) data.get("publicationYear"),
								(ArrayList<String>) data.get("writtenBy")));
					}
					case
				}
			}
		}*/
		
		/*if(this.validateData(data)) {
			throw new IllegalArgumentException("Invalid data map");
		}
		
		if(p != null) {
			this.addExistingProduct(p, stock);
		} else {
			NewProduct product = new NewProduct(name, description, price, picturePath, stock, categories, reviews);
			this.productsOnSale.add(product);
			if(product instanceof Game) {
				this.organiseGame((Game)product);
			}
		}*/
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
	
	private boolean validateData(ItemType itemType, Map<String, Object> data) {
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
		
		switch(itemType) {
			case COMIC: {
				checkField(data, "nPages", Integer.class);
				checkField(data, "publisher", String.class);
				checkField(data, "publicationYear", Integer.class);
				checkField(data, "writtenBy", ArrayList.class);
			}
			case GAME: {
				checkField(data, "nPlayers", Integer.class);
				checkField(data, "mechanics", ArrayList.class);
				checkField(data, "ageRange", AgeRange.class);
			}
			case FIGURINE: {
				checkField(data, "height", Double.class);
				checkField(data, "width", Double.class);
				checkField(data, "depth", Double.class);
				checkField(data, "material", String.class);
				checkField(data, "franchise", String.class);
			}
			case PACK: {
				checkField(data, "products", ArrayList.class);
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
