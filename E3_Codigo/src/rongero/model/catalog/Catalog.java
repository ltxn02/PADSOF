package model.catalog;
import util.AgeRange;
import java.util.*;

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
			//if(age.containedIn(p.getAgeRange()) == true) {
				this.gamesByAge.get(age).add(p);
			//}
		}

	}
	
	public void addProductsOnSale(NewProduct...products) {
		for(NewProduct p: products) {
			this.productsOnSale.add(p);
			if(p instanceof Game) {
				this.organiseGame((Game)p);
			}
		}
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
