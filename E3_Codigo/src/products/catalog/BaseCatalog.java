package products.catalog;

import java.util.*;
import products.*;
// import utils.*;

public abstract class BaseCatalog<T extends Item> {
	protected List<Category> categories;
	protected List<T> products;
	
    public BaseCatalog(ArrayList<Category> categories, ArrayList<T> products) {
        this.categories = categories != null ? categories : new ArrayList<>();
        this.products = products != null ? products : new ArrayList<>();
    }
    
    public BaseCatalog() {
    	this(null, null);
    }
    
    public List<T> allProducts() {
    	return this.products;
    }
    
    public List<T> visibleProducts() {
    	List<T> visible = new ArrayList<>();
    	for(T p : this.products) {
    		if(p.isActive()) {
    			visible.add(p);
    		} 
    	}
    	return visible;
    }
    
    public List<Category> getCategories() {
    	return this.categories;
    }
    
    public void addCategory(String name, ArrayList<T> products) {
    	if(!categoryAlreadyExists(name)) {
    		this.categories.add(new Category(name, products));
    	} else {
    		Category c = this.getCategory(name);
    		if(c != null) {
    			c.addItems(products);
    		}
    	}
    }
    
    private boolean categoryAlreadyExists(String name) {
    	for(Category c : this.categories) {
    		if(c.isNamed(name)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    private Category getCategory(String name) {
    	for(Category c : this.categories) {
    		if(c.isNamed(name)) {
    			return c;
    		}
    	}
    	return null;
    }
}
