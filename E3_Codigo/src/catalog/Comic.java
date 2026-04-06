package catalog;

import java.util.ArrayList;
import utils.*;
import discounts.*;

public class Comic extends Product implements java.io.Serializable{
    private int nPages;
    private String publisher;
    private int publicationYear;
    private ArrayList<String> writtenBy;

    public Comic(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, IDiscount discount, int nPages, String publisher, int publicationYear, ArrayList<String> writtenBy) {
        super(name, description, price, image, stock, categories, reviews, discount);
        if (nPages <= 0) {
            this.nPages = 1;
        } else {
            this.nPages = nPages;
        }

        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.writtenBy = writtenBy;
    }
}
