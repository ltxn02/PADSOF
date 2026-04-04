import java.util.*;
import java.time.*;
import java.util.concurrent.*;

public class ShoppingCart {
	private double fullPrice;
	private List<CartItem> cartItems;
	private static Duration timeOnHold = Duration.ofHours(48);
	
	private final ScheduledExecutorService cleaner;
	
	public ShoppingCart() {
		this.fullPrice = 0;
		this.cartItems = new ArrayList<>();
		
		this.cleaner = Executors.newSingleThreadScheduledExecutor();
		this.cleaner.scheduleAtFixedRate(this::removeExpiredCartItems, 1, 1, TimeUnit.MINUTES);
	}
	
	public synchronized void addCartItem(NewProduct p, int quantity) throws IllegalArgumentException {		
		CartItem c = null;
		double oldPrice = 0;
		
		// Devuelve el CartItem c correspondiente al producto p
		if((c = this.cartContains(p)) == null) {
			c = new CartItem(p, quantity);	// Crea uno nuevo si c no existe
		} else {
			oldPrice = c.fullPrice();
			c.orderQuantity(quantity);
		}
		
		this.fullPrice -= oldPrice;
		this.fullPrice += c.fullPrice();
		this.cartItems.add(c);
	}
	
	private synchronized CartItem cartContains(NewProduct p) {
		for(CartItem c : this.cartItems) {
			if(c.isProduct(p) == true) {
				return c;
			}
		}
		return null;
	}
	
	public synchronized void removeCartItem(NewProduct p, int quantity) throws IllegalArgumentException {		
		CartItem c = null;
		double oldPrice = 0, newPrice = 0;
		
		if((c = this.cartContains(p)) != null) {
			oldPrice = c.fullPrice();
			c.removeQuantity(quantity);
			
			if(c.isEmpty() == true) {
				this.cartItems.remove(c);
			} else {
				newPrice = c.fullPrice();
			}
		}
		
		this.fullPrice -= oldPrice;
		this.fullPrice += newPrice;
	 }
	
	public synchronized void removeExpiredCartItems() {
		this.cartItems.removeIf(item -> item.isExpired(ShoppingCart.timeOnHold));
		this.fullPrice = 0;
		for(CartItem item: this.cartItems) {
			this.fullPrice += item.fullPrice();
		}
	}
	
	public synchronized void setTimeOnHold(long newTimeOnHold) {
		ShoppingCart.timeOnHold = Duration.ofHours(newTimeOnHold);
	}

	// NUEVO METODO
	public synchronized void clearCart() {
		this.cartItems.clear();
	}

	public synchronized List<CartItem> getCartItems() {
		return this.cartItems;
	}


	public synchronized double getPrice() {
		return this.fullPrice;
	}
}