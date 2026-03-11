public class Product {
    private static int lastId = 1;
    private int productId;

    public Product() {
        this.productId = Product.lastId;
        Product.lastId++;
    }
}
