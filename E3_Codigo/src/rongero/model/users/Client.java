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
	private List<ExchangeOffer> offersMade;
	private List<ExchangeOffer> offersReceived;
	
	public Client(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber) {
		super(username, password, fullname, dni, birthdate, email, phoneNumber);
		this.joiningDate = LocalDateTime.now();
		this.shoppingCart = new ShoppingCart();
		this.myOrders = new OrderHistoric();
		this.myExchanges = new ExchangeHistoric();
		this.myProducts = new ArrayList<>();
		this.myReviews = new ArrayList<>();
		this.ordersMade = new ArrayList<>();
		this.offersMade = new ArrayList<>();
		this.offersReceived = new ArrayList<>();
	}
	
	public void addToCart(NewProduct p, int quantity) throws IllegalArgumentException {
		this.shoppingCart.addCartItem(p, quantity);
	}
	
	public void removeFromCart(NewProduct p, int quantity) throws IllegalArgumentException {
		this.shoppingCart.removeCartItem(p, quantity);
	}
	
	public String buyCart(String cardNumber) {
		List<CartItem> orderedItems = this.shoppingCart.getCartItems();
		double price = this.shoppingCart.getPrice();
		Order order = new Order(this, orderedItems, price);
		
		this.ordersMade.add(order);
		if(order.procesarPago(cardNumber)) {
			String code = order.generateCode();
			return code;
		}
		
		return null;
	}
	
	public void cancelOrder(Order order) throws IllegalArgumentException, IllegalStateException {
		if(!this.myOrders.hasOrder(order)) {
			throw new IllegalArgumentException("Invalid order");
		}
		order.cancelOrder();
	}
	
	public void reviewProduct(NewProduct product, int rating, String comment) throws IllegalArgumentException {
		if(this.myOrders.hasProduct(product) == false) {
			throw new IllegalArgumentException("Invalid product, not bought by the client");
		}
		Review review = new Review(rating, comment, product, this);
		this.myReviews.add(review);
	}
	
	public void registerSecondHandProduct(String name, String description, String picturePath, ItemType itemType) {
		SecondHandProduct product = new SecondHandProduct(name, description, picturePath, itemType, this);
		this.myProducts.add(product);
	}
	
	public void removeSecondHandProduct(SecondHandProduct product) throws IllegalArgumentException {
		if(!this.myProducts.contains(product)) {
			throw new IllegalArgumentException("Invalid product, doesn't exist in your wallet");
		}
		this.myProducts.remove(product);
	}
	
	public void makeOffer(SecondHandProduct requested, SecondHandProduct...offered) throws IllegalArgumentException {
		if(this.productIsYours(offered) == false) {
			throw new IllegalArgumentException("Invalid offered products, you must own them");
		} else if(this.productIsAvailable(offered) == false || requested.isAvailable() == false) {
			throw new IllegalArgumentException("Invalid products, they must be available");
		}
		ExchangeOffer offer = new ExchangeOffer(requested, new ArrayList<>(Arrays.asList(offered)), this);
		this.offersMade.add(offer);
	}
	
	public void cancelOffer(ExchangeOffer offer) throws IllegalArgumentException, IllegalStateException {
		if(!this.offersMade.contains(offer)) {
			throw new IllegalArgumentException("Invalid offer, it's not in your register");
		}
		offer.cancelOffer();
	}
	
	public void answerOffer(ExchangeOffer offer, boolean accept) throws IllegalArgumentException {
		if(!this.offersMade.contains(offer)) {
			throw new IllegalArgumentException("Invalid offer, it's not in your register");
		}
		
		if(accept == true) {
			offer.aceptaroferta();
		} else if(accept == false) {
			offer.reject_offer();
		}
	}
	
	private boolean productIsAvailable(SecondHandProduct...products) {
		for(SecondHandProduct s: products) {
			if(s.isAvailable()) {
				return false;
			}
		}
		return true;
	}
	
	private boolean productIsYours(SecondHandProduct...products) {
		for(SecondHandProduct p: products) {
			if(p.isOwnedBy(this) == false) {
				return false;
			}
		}
		return true;
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

	public void registrarOfertaRealizada(ExchangeOffer oferta) {
		this.offersMade.add(oferta);
	}

	public List<ExchangeOffer> obtenerMisOfertasEnviadas(){

		return this.offersMade;
	}
	public List<ExchangeOffer> obtenerMisOfertasRecibidos(){
		return this.offersReceived;
	}

	public void receiveOffer(ExchangeOffer oferta) {
		this.offersReceived.add(oferta);
	}

	public List<SecondHandProduct> getCarteraSegundaMano() {
		return this.myProducts;
	}

	public void addMyProduct(SecondHandProduct p) {
		this.myProducts.add(p);
	}

	public List<SecondHandProduct> getMyProducts() {
		return this.myProducts;
	}
}