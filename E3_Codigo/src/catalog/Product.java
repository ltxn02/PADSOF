package catalog;
import java.util.ArrayList;
import java.util.Date;
import utils.*;
import discounts.*;


public abstract class Product extends NewProduct implements java.io.Serializable{
    private static int lastProductId = 1;
    private int productId;
    private IDiscount discount;

    public Product(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, IDiscount discount) {
        super(name, description, price, image, stock, categories, reviews);
        this.discount = discount;
        this.productId = Product.lastProductId;
        Product.lastProductId++;
    }

    public void setDiscount(IDiscount discount) {
        this.discount = discount;
    }

    public IDiscount getDiscount() {
        return this.discount;
    }

    @Override
    public double getPrice() {
        // 1. getPrice devuelve el precio base inalterado
        return super.getPrice();
    }

    public double getPriceWithDiscount() {
        double precioOriginal = this.getPrice();

        if (this.discount != null && this.discount.isValid()) {
            return this.discount.apply(precioOriginal);
        }

        return precioOriginal;
    }
}