import java.util.ArrayList;

public class Comic extends Product{
    private int nPages;
    private String publisher;
    private int publicationYear;
    private ArrayList<String> writtenBy;

    public Comic(String name, String description, double price, String image, ArrayList<Category> categories, int stock, ArrayList<Review> reviews, Discount discount, int nPages, String publisher, int publicationYear, ArrayList<String> writtenBy) {
        super(name, description, price, image, categories, stock, reviews, discount);
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
