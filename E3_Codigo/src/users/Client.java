package users;
import utils.*;
import transactions.*;
import catalog.*;

import java.util.*;
import java.util.function.Function;
import java.time.*;

public class Client extends RegisteredUser {
	private LocalDateTime joiningDate;
	private ShoppingCart shoppingCart;  // Deja solo este
	private OrderHistoric myOrders;     // Y deja solo este
	private ExchangeHistoric myExchanges;
	private List<SecondHandProduct> myProducts;
	private List<Review> myReviews;
	private List<Order> ordersMade;
	private List<Exchange> exchangesMade;
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
		this.exchangesMade = new ArrayList<>();
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
		this.myOrders.addOrder(order);
		
		if(order.procesarPago(cardNumber)) {
			this.shoppingCart.clearCart();
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
	
	public void registerSecondHandProduct(SecondHandProduct product) {
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
	
	public void receiveOffer(ExchangeOffer oferta) {
		this.offersReceived.add(oferta);
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
	
	public void makeExchange(Exchange exchange) {
		this.exchangesMade.add(exchange);
		this.myExchanges.addExchange(exchange);
	}
	
	public String clientFullProfile() {
		StringBuilder sb = new StringBuilder(super.userProfile());
		
		sb.append("Se unió el: " + this.joiningDate + "\n");
		sb.append("--- Resumen de actividad ---\n");
		sb.append("Carrito: " + String.format("%.2f €", this.shoppingCart.shoppingCartPreview()) + "\n");
		sb.append(formatListPreview("Pedidos", Order::orderPreview, this.ordersMade));
		sb.append(formatListPreview("Intercambios", null, this.exchangesMade));
		sb.append(formatListPreview("Productos de segunda mano", SecondHandProduct::secondHandProductPreview, this.myProducts));
		sb.append(formatListPreview("Reseñas", Review::reviewPreview, this.myReviews));
		sb.append(formatListPreview("Ofertas hechas", ExchangeOffer::offerMadePreview, this.offersMade));
		sb.append(formatListPreview("Ofertas recibidas", ExchangeOffer::offerReceivedPreview, this.offersReceived));
		
		return sb.toString();
	}
	
	public String viewShoppingCart() {
		return this.shoppingCart.shoppingCartView();
	}
	
	public String viewMyProducts() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n--- MI CARTERA DE SEGUNDA MANO ---\n");
		
		if(this.myProducts.isEmpty()) {
			sb.append("Todavía no has subido ningún producto\n");
			return sb.toString();
		}
		
		for(SecondHandProduct p: this.myProducts) {
			sb.append(p + "\n\"--------------------------------\n");
		}
		
		return sb.toString();
	}
	
	public String toString(){
		return super.userPreview();
	}
	
	// -- HELPERS ------------------------------------------------------------------
	
	private <T> String formatListPreview(String listName, Function<T, String> previewFunction, List<T> list) {
		StringBuilder sb = new StringBuilder();
		sb.append(listName + ": ");
		if(list == null) {
			sb.append("N/A\n");
			return sb.toString();
		}
		
		if(list.isEmpty()) {
			sb.append("0 items");
			return sb.toString();
		}
		
		sb.append(list.size() + " items\n");
		if(previewFunction != null) {
			int limit = Math.min(list.size(), 3);
			for(int i = 0; i < limit; i++) {
				sb.append(previewFunction.apply((T)list.get(i)) + "\n");
			}
			
			if(list.size() > 3) sb.append(" ... y \n" + (list.size() - 3) + "más\n");
		}
		
		return sb.toString();
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
	
	
	
	
	
	
	
	
	
	
	
	

	// NUEVOS MÉTODOS

	public OrderHistoric getOrderHistoric() {
		return this.myOrders;
	}

	public List<ExchangeOffer> getOffersMade(){
		return this.offersMade;
	}
	public List<ExchangeOffer> obtenerMisOfertasRecibidos(){
		return this.offersReceived;
	}

	public List<SecondHandProduct> getCarteraSegundaMano() {
		return this.myProducts;
	}
}