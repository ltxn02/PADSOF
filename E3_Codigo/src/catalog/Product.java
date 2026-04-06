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
        // 1. Obtenemos el precio original llamando a la clase padre
        double precioOriginal = super.getPrice();

        // 2. Comprobamos si tiene un descuento y si no está caducado
        if (this.discount != null && this.discount.isValid()) {
            // El metodo applyDiscount devuelve cuánto dinero te ahorras (ej. 20% de 50€ = 10€)
            double rebaja = this.discount.apply(precioOriginal);
            // El precio final es el original menos la rebaja (50€ - 10€ = 40€)
            return precioOriginal - rebaja;
        }

        // Si no hay descuento o está caducado, devolvemos el precio normal
        return precioOriginal;
    }
    public double getPriceWithDiscount() {
        if (this.discount == null || this.discount.isExpired()) {
            return this.getPrice();
        }
        if (this.discount instanceof discounts.IRebaja) {
            return ((discounts.IRebaja) this.discount).applyRebaja(this.getPrice());
        }
        return this.getPrice();
    }
}