import java.util.ArrayList;

public abstract class NewProduct extends Item {
    private int stock;
    private ArrayList<Review> reviews;

    public NewProduct(String name, String description, double price, String image, int stock, ArrayList<Review> reviews) {
        super(name, description, price, image);
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
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
}
