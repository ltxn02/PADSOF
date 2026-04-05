package utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import users.*;
import transactions.*;
import catalog.*;

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
    
    public String reviewPreview() {
    	return this.buildString(15);
    }
    
    private void addReviewToProduct(NewProduct product) {
    	product.addReview(this);
    }
    
    private String buildString(int maxChars) throws IllegalArgumentException {
    	if (maxChars < -1) {
    		throw new IllegalArgumentException("Invalid max characters");
    	}
    	
    	if (this.comment == null) {
    		return "";
    	}
    	
    	StringBuilder sb = new StringBuilder("  ");
    	int end = maxChars;
    	
    	if(maxChars == -1 || maxChars >= this.comment.length()) {
    		end = this.comment.length();
    	}
    	
    	sb.append(this.product.itemAuxPreview() + " | ");
    	sb.append(this.rating + " | ");
    	if(this.comment.length() < end) {
    		sb.append(this.comment);
    	} else {
    		sb.append(this.comment.substring(end - 3) + "...");
    	}
    	return sb.toString();
    }
    
    private String formatInstant(Instant instant) {
    	if(instant == null) return "N/A";
    	
    	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm").withZone(ZoneId.systemDefault());
    	return fmt.format(instant);
    }
}
