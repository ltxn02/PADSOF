import java.util.ArrayList;

public abstract class Item extends BaseElement{
    private String name;
    private String description;
    private double price;
    private String picturePath;
    private ArrayList<Category> categories;
    
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
}