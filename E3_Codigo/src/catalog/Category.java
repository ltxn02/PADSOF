package catalog;
import java.util.ArrayList;
import utils.*;


public class Category extends BaseElement implements java.io.Serializable{
    private String name;
    private ArrayList<Item> items;

    public Category(String name, ArrayList<Item> items) {
        this.name = name;
        this.items = items;
    }

    public String getNameCategory() {
        return name;
    }
}
