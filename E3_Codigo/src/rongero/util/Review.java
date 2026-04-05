package util;
import model.catalog.BaseElement;
import model.catalog.NewProduct;
import model.user.Client;

public class Review extends BaseElement{
	private static int lastReviewId = 1;
	private int reviewId;
    private int rating;
    private String comment;
    private NewProduct product;
    private Client postedBy;

    public Review(int rating, String comment, NewProduct product, Client postedBy) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Invalid rating");
        }
        this.rating = rating;
        this.comment = comment;
        this.product = product;
        this.addReviewToProduct(product);
        this.reviewId = Review.lastReviewId;
        Review.lastReviewId++;
    }

    public int getRating() {
        return rating;
    }
    
    private void addReviewToProduct(NewProduct product) {
    	product.addReview(this);
    }
}
