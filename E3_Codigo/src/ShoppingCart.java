import java.util.*;
import java.time.*;

public class ShoppingCart {
	private double fullPrice;
	private List<CartItem> cartItems;
	private static Duration timeOnHold = Duration.ofHours(48);
	
	public ShoppingCart() {
		this.fullPrice = 0;
		this.cartItems = new ArrayList<>();
	}
	
	public void addCartItem(NewProduct p, int quantity) throws IllegalArgumentException {		
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
	
	private CartItem cartContains(NewProduct p) {
		for(CartItem c : this.cartItems) {
			if(c.isProduct(p) == true) {
				return c;
			}
		}
		return null;
	}
	
	public void removeCartItem(NewProduct p, int quantity) throws IllegalArgumentException {		
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
	
	public void removeExpiredCartItems() {
		this.cartItems.removeIf(item -> item.isExpired(ShoppingCart.timeOnHold));
	}

	// NUEVO METODO
	public void clearCart() {
		this.cartItems.clear();
	}

	public List<CartItem> getCartItems() {
		return this.cartItems;
	}


	public double getPrice() {
		return this.fullPrice;
	}
}