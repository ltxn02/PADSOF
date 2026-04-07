package catalog;
import java.util.ArrayList;
import utils.*;


public class Category extends BaseElement implements java.io.Serializable{
    private String name;
    private ArrayList<NewProduct> newProductItems;
    private ArrayList<SecondHandProduct> secondHandItems;

    public Category(String name, ArrayList<Item> items) {
        this.name = name;
        for(Item item: items) {
        	this.addItem(item);
        }
    }
    
    public Category(String name) {
    	this.name = name;
    	this.newProductItems = new ArrayList<>();
    }
    
    public void addItem(Item item) throws IllegalArgumentException {
    	if(this.newProductItems.contains(item) || this.secondHandItems.contains(item)) {
    		throw new IllegalArgumentException("Item already exists in " + this.name);
    	}
    	
    	if(item instanceof NewProduct) {
    		this.newProductItems.add((NewProduct)item);
    	} else if (item instanceof SecondHandProduct) {
    		this.secondHandItems.add((SecondHandProduct)item);
    	}
    }
    
    public String getNameCategory() {
        return name;
    }
    
    public void rename(String name) {
    	this.name = name;
    }
}
