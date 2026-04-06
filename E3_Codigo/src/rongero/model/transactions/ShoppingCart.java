package model.transactions;
import model.catalog.NewProduct;
import model.discounts.IVolumen;
import java.util.*;
import java.time.*;
import java.util.concurrent.*;
import model.catalog.Item;
import model.discounts.*;

public class ShoppingCart {
	private double fullPrice;
	private List<CartItem> cartItems;
	private static Duration timeOnHold = Duration.ofHours(48);
	List<IVolumen> globalDiscounts;
	List<Item> gifts= new ArrayList<>();
	private final ScheduledExecutorService cleaner;
	
	public ShoppingCart() {
		this.fullPrice = 0;
		this.cartItems = new ArrayList<>();
		this.globalDiscounts= new ArrayList<>();
		this.cleaner = Executors.newSingleThreadScheduledExecutor();
		this.cleaner.scheduleAtFixedRate(this::removeExpiredCartItems, 1, 1, TimeUnit.MINUTES);
	}
	
	public synchronized void addCartItem(NewProduct p, int quantity) throws IllegalArgumentException {		
		CartItem c = this.cartContains(p);
		double oldPrice = 0;

		if (c== null){
			c = new CartItem(p, quantity);
			this.cartItems.add(c);
			this.fullPrice += c.fullPrice();
		} else {
			oldPrice = c.fullPrice();
			c.orderQuantity(quantity);
			this.fullPrice= this.fullPrice - oldPrice + c.fullPrice();
		}}

	
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

	/**
	 * Vacía completamente el contenido del carrito de la compra.
	 * * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public synchronized void clearCart() {
		this.cartItems.clear();
	}

	/**
	 * Obtiene la lista de elementos (productos y cantidades) presentes en el carrito.
	 * * @return Lista de objetos CartItem.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public synchronized List<CartItem> getCartItems() {
		return this.cartItems;
	}

	/**
	 * Calcula el precio final del carrito aplicando los descuentos por volumen activos.
	 * Este método también gestiona la asignación de regalos: limpia la lista de regalos
	 * actual y vuelve a evaluarlos según el precio total y las promociones vigentes.
	 * * @return El precio final redondeado a dos decimales.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public synchronized double getPrice() {
		this.gifts.clear();
		double finalPrice = this.fullPrice;
		double mejorDescuento = 0;

		for (IVolumen d : globalDiscounts) {
			if (!d.isExpired()) {
				double precioConEsteDescuento = d.applyVolumen(this.fullPrice);
				double ahorro = this.fullPrice - precioConEsteDescuento;

				// Si quieres que se ACUMULEN:
				finalPrice -= ahorro;

				// Si es un regalo, se procesa aparte
				if (d instanceof IRegalo) {
					((IRegalo) d).aplicarRegalo(this);
				}
			}
		}
		return Math.round(finalPrice * 100.0) / 100.0;
	}

	/**
	 * Obtiene la lista de artículos de regalo que se han añadido al carrito tras
	 * aplicar las promociones de volumen.
	 * * @return Lista de artículos (Item) sin coste.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public List<Item> getGifts(){
		return gifts;
	}

	/**
	 * Obtiene el precio total base del carrito antes de aplicar descuentos globales.
	 * * @return Valor double del precio total bruto.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public double getFullPrice(){
		return this.fullPrice;
	}

	/**
	 * Añade un artículo de regalo al carrito.
	 * Se utiliza internamente por las interfaces de descuento de tipo IRegalo.
	 * * @param item El artículo que se desea regalar al cliente.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public synchronized void addGlobalDiscount(IVolumen discount) {
		if (discount != null) {
			this.globalDiscounts.add(discount);
		}
	}

	// En model.transactions.ShoppingCart
	public synchronized void addGift(Item item) {
		if (item != null && !this.gifts.contains(item)) {
			this.gifts.add(item);
		}
	}
}