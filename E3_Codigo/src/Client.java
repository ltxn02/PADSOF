import java.util.*;
import java.time.*;

public class Client extends RegisteredUser {
	private LocalDateTime joiningDate;
	private ShoppingCart shoppingCart;
	private OrderHistoric myOrders;
	private ExchangeHistoric myExchanges;
	private List<SecondHandProduct> myProducts;
	private List<Review> myReviews;
	private List<Order> ordersMade;
	private List<Exchange> offersMade;
	private List<Exchange> offersReceived;
	
	public Client(String username, String password, String fullname, String dni, Date birthdate, String email, String phoneNumber) {
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
	/*
	 *	public boolean addToCart(NewProduct p, quantity q) {
	 *	  	if (q <= stock) {
	 *			return this.shoppingCart.addProduct(p, q);
	 *    	}	
	 *    	return false;
	 * 	}
	*/
}