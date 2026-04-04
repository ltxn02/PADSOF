import java.util.ArrayList;

public class OrderHistoric {
    private ArrayList<Order> orders;

    public OrderHistoric(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public OrderHistoric() {
        this.orders = new ArrayList<>();
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
    }
    
    public boolean hasOrder(Order order) {
    	return this.orders.contains(order);
    }
    
    public boolean hasProduct(NewProduct p) {
    	for(Order order: this.orders) {
    		if(order.hasProduct(p)) {
    			return true;
    		}
    	}
    	return false;
    }
}
