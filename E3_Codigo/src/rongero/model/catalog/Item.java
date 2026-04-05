package model.catalog;

import model.discounts.IDiscount;
import java.util.List;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import model.discounts.IRebaja;

public abstract class Item extends BaseElement{
    private String name;
    private String description;
    private double price;
    private String picturePath;
    private List<Category> categories;
    private IDiscount discount;
    private Instant lastAddedAt;
    private IRebaja rebaja;
    
    public Item(String name, String description, double price, String picturePath, List<Category> categories) {
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

    public double getPrice() {
        if (this.discount == null || this.discount.isExpired()) {
            return this.price;
        }

        // Si es una rebaja (porcentaje o fijo), la aplicamos
        if (this.discount instanceof IRebaja) {
            return ((IRebaja) this.discount).applyRebaja(this.price);
        }

        return this.price;
    }

    public void setDiscount(IDiscount discount) {
        this.discount = discount;
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
    
    protected void setPrice(double price) {
    	this.price = price;
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
    
    public List<Category> getCategories() {
        return categories;
    }
    
    public StringBuilder itemInfo() {
    	StringBuilder res = new StringBuilder();
    	res.append("\nName: " + this.name + "\n\t'" + this.description + "'\n");
    	res.append("\tCategories: " + this.categories + "\n");
    	return res;
    }
}