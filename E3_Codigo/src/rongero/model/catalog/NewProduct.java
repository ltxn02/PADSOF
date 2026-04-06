package model.catalog;
import java.util.List;
import java.util.ArrayList;
import util.Review;
import model.discounts.*;

public  class NewProduct extends Item {
    private int stock;
    private int effectiveStock;
    private List<Review> reviews;
    private IDiscount discount;

    public NewProduct(String name, String description, double price, String image, List<Category> categories,int stock, List<Review> reviews) {
        super(name, description, price, image, categories);
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        } else if (categories.size() <1) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.stock = stock;
        this.reviews = reviews;
        this.effectiveStock= stock;
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
    
    @Override
    public double getPrice() {
        return super.getPrice();
    }
    public void setDiscount(IDiscount discount) {
        this.discount = discount;
    }
    public IDiscount getDiscount() {
        return this.discount;
    }
    public double getPriceWithDiscount() {
        // Si no hay descuento o ha caducado, devolvemos el precio normal
        if (this.discount == null || this.discount.isExpired()) {
            return this.getPrice();
        }

        // Si el descuento es una "Rebaja" (Porcentaje o Fijo), lo aplicamos
        if (this.discount instanceof IRebaja) {
            return ((IRebaja) this.discount).applyRebaja(this.getPrice());
        }

        // Para otros tipos (como Cantidad), el precio unitario base no cambia aquí
        return this.getPrice();
    }




}
