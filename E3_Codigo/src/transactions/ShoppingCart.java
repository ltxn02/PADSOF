package transactions;

import java.util.*;
import java.time.*;
import java.util.concurrent.*;
import utils.*;
import users.*;
import catalog.*;
import discounts.*;

public class ShoppingCart implements java.io.Serializable{
	private double fullPrice;
	private List<CartItem> cartItems;
	private static Duration timeOnHold = Duration.ofHours(48);
	private List<discounts.IVolumen> globalDiscounts = new ArrayList<>();
	private List<catalog.Item> gifts = new ArrayList<>();

	private static final ScheduledExecutorService cleaner =
			Executors.newSingleThreadScheduledExecutor(r -> {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			});
	
	public ShoppingCart() {
		this.fullPrice = 0;
		this.cartItems = new ArrayList<>();
		
		ShoppingCart.cleaner.scheduleAtFixedRate(this::removeExpiredCartItems, 1, 1, TimeUnit.MINUTES);
	}
	
	public synchronized void addCartItem(NewProduct p, int quantity) throws IllegalArgumentException {		
		CartItem c = null;
		double oldPrice = 0;
		
		if((c = this.cartContains(p)) == null) {
			c = new CartItem(p, quantity);
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
	
	public static void shutdownCleaner() {
		ShoppingCart.cleaner.shutdownNow();
	}
	
	public synchronized void setTimeOnHold(long newTimeOnHold) {
		ShoppingCart.timeOnHold = Duration.ofHours(newTimeOnHold);
	}
	
	public synchronized String shoppingCartPreview() {
		int items = (this.cartItems == null) ? 0 : this.cartItems.size();	
		return items + " items. Total: " + this.fullPrice;
	}

	public synchronized String shoppingCartView() {
		double totalReal = this.getPrice();

		StringBuilder sb = new StringBuilder("--- MI CARRITO DE LA COMPRA ---\n");

		if(this.cartItems == null || this.cartItems.isEmpty()) {
			sb.append("El carrito está vacío...\n");
			return sb.toString();
		}

		for(CartItem item: this.cartItems) {
			sb.append("  " + item + "\n");
		}

		if (this.gifts != null && !this.gifts.isEmpty()) {
			for (catalog.Item gift : this.gifts) {
				sb.append("  1 x " + gift.getName() + " (0,00 €) [REGALO]\n");
			}
		}

		sb.append("----------------\n");
		sb.append("TOTAL: " + String.format("%.2f €", totalReal));
		return sb.toString();
	}

	public synchronized void clearCart() {
		this.cartItems.clear();
		this.fullPrice =0;
		this.gifts.clear();
	}

	public synchronized List<CartItem> getCartItems() {
		return this.cartItems;
	}


	public synchronized double getPrice() {
		this.gifts.clear();
		double totalNeto = 0;

		for (CartItem item : this.cartItems) {
			NewProduct np = item.getProduct();
			double subtotalItem = 0;

			if (np instanceof Product) {
				Product prod = (Product) np;
				IDiscount desc = prod.getDiscount();

				if (desc != null && desc.isValid() && desc instanceof discounts.ICantidad) {
					subtotalItem = ((discounts.ICantidad) desc).applyCantidad(prod.getPrice(), item.getQuantity());
				} else {
					subtotalItem = prod.getPriceWithDiscount() * item.getQuantity();
				}
			} else {
				subtotalItem = np.getPrice() * item.getQuantity();
			}

			totalNeto += subtotalItem;
		}

		this.fullPrice = totalNeto;
		double totalFinal = totalNeto;

		for (discounts.IVolumen d : globalDiscounts) {
			if (d instanceof IDiscount && !((IDiscount) d).isExpired()) {
				totalFinal = d.applyVolumen(totalFinal);

				if (d instanceof IRegalo) {
					((IRegalo) d).aplicarRegalo(this);
				}
			}
		}
		return Math.round(totalFinal * 100.0) / 100.0;
	}

	public double getFullPrice() {
		return this.fullPrice;
	}

	public void addGlobalDiscount(discounts.IVolumen d) {
		this.globalDiscounts.add(d);
	}

	public List<catalog.Item> getGifts() {
		return this.gifts;
	}

	public void addGift(catalog.Item item) {
		if (item != null && !this.gifts.contains(item)) {
			this.gifts.add(item);
		}
	}


}