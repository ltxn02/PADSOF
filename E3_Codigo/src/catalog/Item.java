package catalog;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import utils.*;


public abstract class Item extends BaseElement{
    private String name;
    private String description;
    private double price;
    private String picturePath;
    private ArrayList<Category> categories;
    private Instant lastAddedAt;
    
    public Item(String name, String description, double price, String picturePath, ArrayList<Category> categories) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
        this.name = name;
        this.description = description;
        this.picturePath = picturePath;
        this.categories = categories;
        this.lastAddedAt = null;
    }
    
    public Item(String name, String description, double price, String picturePath) {
    	this(name, description, price, picturePath, new ArrayList<Category>());
    }

    protected void registerTime() {
        this.lastAddedAt = Instant.now();
    }

    protected void clearInstants() {
        this.lastAddedAt = null;
    }

    public boolean isExpired(Duration time) {
        return this.lastAddedAt.plus(time).isBefore(Instant.now());
    }
    
    protected void edit(String name, String description, double price, String picturePath) {
    	if(name != null) this.name = name;
    	if(description != null) this.description = description;
    	if(price > 0) this.price = price;
    	if(picturePath != null) this.picturePath = picturePath;
    }
    
    protected void setPrice(double price) {
    	this.price = price;
    }
    
    public double getPrice(){
        return this.price;
    }

    public String getName(){
        return this.name;
    }
    
    public void addCategory(Category category) throws IllegalArgumentException {
    	if(this.categories.contains(category)) {
    		throw new IllegalArgumentException("Invalid category, already exists");
    	}
    	this.categories.add(category);
    }
    
    public ArrayList<Category> getCategories() {
        return categories;
    }
    
    public String itemAuxPreview() {
    	return this.name + " (" + String.format("%.2f €", this.price) + ")";
    }
    
    public String itemPreview() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(this.name + " | ");
    	if(this.description.length() < 15) {
    		sb.append(this.description + " | ");
    	} else {
    		sb.append(this.description.substring(12) + "...");
    	}
    	return sb.toString();
    }
    
    public String itemInfo() {
    	StringBuilder res = new StringBuilder();
    	res.append("\nName: " + this.name + "\n  '" + this.description + "'\n");
    	res.append("  Categories: " + this.categories + "\n");
    	return res.toString();
    }

    public boolean isNamed(String name) {
        // Usamos equalsIgnoreCase para comparar el texto real, ignorando mayúsculas/minúsculas
        if (this.name != null && this.name.equalsIgnoreCase(name.trim())) {
            return true;
        }
        return false;
    }
}