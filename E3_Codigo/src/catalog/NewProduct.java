package catalog;

import java.util.ArrayList;
import utils.*;


public abstract class NewProduct extends Item {
    private int stock;
    private int effectiveStock;
    private ArrayList<Review> reviews;

    public NewProduct(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews) {
        super(name, description, price, image, categories);
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        } else if (categories.size() <1) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.stock = stock;
        this.reviews = reviews;
    }
    
    public NewProduct(String name, String description, double price, String image, int stock) {
    	this(name, description, price, image, stock, new ArrayList<Category>(), new ArrayList<Review>());
    }

    public int calculateRating(){
        if (reviews == null || reviews.isEmpty()) {
            return 0; // Evitamos la división por cero
        }

        int rating = 0;
        for (Review review : reviews){
            rating += review.getRating();
        }

        rating /= reviews.size();
        return rating;
    }

    public boolean isEffectiveStockEmpty(){
        return this.effectiveStock == 0;
    }

    public boolean isEffectiveStockHigher(int quantity){
        return this.effectiveStock >= quantity;
    }
    
    public void decreaseStock(int quantity) throws IllegalArgumentException {
    	if(quantity > this.stock) {
    		throw new IllegalArgumentException("Invalid quantity, stock is lower");
    	}
    	this.stock -= quantity;
    }
    
    public void increaseStock(int quantity) throws IllegalArgumentException {
    	if(quantity < 0) {
    		throw new IllegalArgumentException("Invalid quantity, stock cannot be negative");
    	}
    	this.stock += quantity;
    }

    public void orderProduct(int quantity) throws IllegalArgumentException{
        if (quantity < 0){
            throw new IllegalArgumentException("Invalid quantity, cannot be negative");
        }
        this.effectiveStock -= quantity;
        super.registerTime();
    }

    public void returnProduct(int quantity, boolean isAll) throws IllegalArgumentException{
        if (quantity < 0){
            throw new IllegalArgumentException("Invalid quantity, cannot be negative");
        }

        this.effectiveStock += quantity;
        if (isAll){
            super.clearInstants();
        }
    }
    
    public boolean contains(String str) throws IllegalArgumentException {
    	if(str == null) {
    		throw new IllegalArgumentException("Invalid string");
    	}
    	return super.getName().toLowerCase().contains(str.toLowerCase());
    }
    
    public void addReview(Review review) {
    	this.reviews.add(review);
    }
    
    public void editProductInfo(String name, String description, double price, String picturePath, int stock) {
    	super.edit(name, description, price, picturePath);
    	if(stock >= 0) this.stock = stock;
    }
    
    @Override
    public double getPrice() {
        return super.getPrice();
    }
}
