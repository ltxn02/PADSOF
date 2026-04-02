import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public abstract class Item extends BaseElement{
    private String name;
    private String description;
    private double price;
    private String picturePath;
    private ArrayList<Category> categories;
    private Instant lastAddedAt;
    
    public Item(String name, String description, double price, String picturePath, ArrayList<Category> categories) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
        this.name = name;
        this.description = description;
        this.picturePath = picturePath;
        this.categories = categories;
    }

    protected void registerTime() {
        this.lastAddedAt = Instant.now();
    }

    protected void clearInstants() {
        this.lastAddedAt = null;
    }

    public boolean isExpired(Duration time) {
        return this.lastAddedAt.plus(time).isBefore(Instant.now());
    }

    public double getPrice(){
        return this.price;
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
}