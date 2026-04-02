import java.util.*;
import java.time.*;

public class Client extends RegisteredUser {
	private LocalDateTime joiningDate;
	private ShoppingCart shoppingCart;  // Deja solo este
	private OrderHistoric myOrders;     // Y deja solo este
	private ExchangeHistoric myExchanges;
	private List<SecondHandProduct> myProducts;
	private List<Review> myReviews;
	private List<Order> ordersMade;
	private List<Exchange> offersMade;
	private List<Exchange> offersReceived;

	public Client(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber) {
		super(username, password, fullname, dni, birthdate, email, phoneNumber);
		this.joiningDate = LocalDateTime.now();
		this.shoppingCart = new ShoppingCart();
		this.myOrders = new OrderHistoric();
		this.myExchanges = new ExchangeHistoric();
		this.myProducts = new ArrayList<SecondHandProduct>();
		this.myReviews = new ArrayList<Review>();
		this.ordersMade = new ArrayList<Order>();
		this.offersMade = new ArrayList<Exchange>();
		this.offersReceived = new ArrayList<Exchange>();
	}
	
	public void addToCart(NewProduct p, int quantity) throws IllegalArgumentException {
		this.shoppingCart.addCartItem(p, quantity);
	}
	
	public void removeFromCart(NewProduct p, int quantity) throws IllegalArgumentException {
		this.shoppingCart.removeCartItem(p, quantity);
	}
	
	public String buyCart(String cardNumber) {
		List<CartItem> orderedItems = this.shoppingCart.getCartItems();
		double cost = this.shoppingCart.getPrice();
		Order order = new Order(orderedItems, cost);
		
		this.ordersMade.add(order);
		if(order.procesarPago(cardNumber)) {
			String code = order.generateCode();
			return code;
		}
		
		return null;
	}
	
	public void makeOffer() {
		
	}
	/*
	 *	public boolean addToCart(NewProduct p, quantity q) {
	 *	  	if (q <= stock) {
	 *			return this.shoppingCart.addProduct(p, q);
	 *    	}	
	 *    	return false;
	 * 	}
	*/
	public String toString(){
		return this.getUsername();
	}

	// NUEVOS MÉTODOS
	public ShoppingCart getShoppingCart() {
		return this.shoppingCart;
	}

	public OrderHistoric getOrderHistoric() {
		return this.myOrders;
	}

}