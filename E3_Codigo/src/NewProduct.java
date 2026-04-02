import java.util.ArrayList;

public abstract class NewProduct extends Item {
    private int stock;
    private int effectiveStock;
    private ArrayList<Review> reviews;

    public NewProduct(String name, String description, double price, String image, ArrayList<Category> categories,int stock, ArrayList<Review> reviews) {
        super(name, description, price, image, categories);
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        } else if (categories.size() <1) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.stock = stock;
        this.reviews = reviews;
    }

    public int calculateRating(){
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

    @Override
    public double getPrice() {
        return super.getPrice();
    }


}
