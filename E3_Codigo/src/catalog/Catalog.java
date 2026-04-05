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
	
	/*public void addProductOnSale(String name, String description, double price, String picturePath, ArrayList<Category> categories, int stock, ArrayList<Review> reviews) {
		NewProduct p = this.productNamed(name);
		
		if(p != null) {
			this.addExistingProduct(p, stock);
		} else {
			NewProduct product = new NewProduct(name, description, price, picturePath, categories, stock, reviews);
			this.productsOnSale.add(product);
			if(product instanceof Game) {
				this.organiseGame((Game)product);
			}
		}
	}*/
	
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
}
