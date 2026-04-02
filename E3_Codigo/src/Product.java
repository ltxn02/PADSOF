import java.util.ArrayList;

public abstract class Product extends NewProduct {
    private static int lastId = 1;
    private int productId;
    private Discount discount;

    public Product(String name, String description, double price, String image, ArrayList<Category> categories, int stock, ArrayList<Review> reviews, Discount discount) {
        super(name, description, price, image, categories, stock, reviews);
        this.productId = Product.lastId;
        Product.lastId++;
    }





}
