import java.util.ArrayList;

public abstract class Product extends NewProduct {
    private static int lastProductId = 1;
    private int productId;
    private Discount discount;

    public Product(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, Discount discount) {
        super(name, description, price, image, stock, categories, reviews);
        this.productId = Product.lastProductId;
        Product.lastProductId++;
    }





}
