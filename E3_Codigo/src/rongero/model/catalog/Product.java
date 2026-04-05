package model.catalog;
import util.Review;
import java.util.List;
import java.util.ArrayList;
import model.discounts.IDiscount;


public abstract class Product extends NewProduct {
    private static int lastProductId = 1;
    private int productId;


    public Product(String name, String description, double price, String image, List<Category> categories, int stock, ArrayList<Review> reviews, IDiscount discount) {
        super(name, description, price, image, categories, stock, reviews);
        this.setDiscount(discount);
        this.productId = Product.lastProductId;
        Product.lastProductId++;
    }





}
