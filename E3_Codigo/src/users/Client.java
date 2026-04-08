package users;
import utils.*;
import transactions.*;
import catalog.*;

import java.util.*;
import java.util.function.Function;
import java.time.*;

/**
 * Clase que representa a un Cliente (Client) del sistema de la tienda.
 * 
 * Los clientes son usuarios registrados que pueden realizar compras, crear reseñas,
 * y participar en el sistema de intercambio de productos de segunda mano. Hereda de
 * {@link RegisteredUser} y proporciona funcionalidades específicas del negocio tales como:
 * <ul>
 *   <li>Gestión de carrito de compras ({@link ShoppingCart})</li>
 *   <li>Realización y cancelación de compras ({@link Order})</li>
 *   <li>Creación de reseñas de productos ({@link Review})</li>
 *   <li>Registro y gestión de productos de segunda mano ({@link SecondHandProduct})</li>
 *   <li>Participación en intercambios entre clientes ({@link Exchange})</li>
 *   <li>Creación, envío y respuesta a ofertas de intercambio ({@link ExchangeOffer})</li>
 *   <li>Mantenimiento del histórico de pedidos e intercambios</li>
 * </ul>
 * 
 * Cada cliente tiene un carrito de compras persistente, un histórico de órdenes,
 * un histórico de intercambios, y puede poseer múltiples productos de segunda mano
 * disponibles para intercambiar con otros clientes.
 * 
 * @author Lidia Martin
 * @version 2.8
 */
public class Client extends RegisteredUser implements java.io.Serializable{
	private LocalDateTime joiningDate;
	private ShoppingCart shoppingCart;
	private OrderHistoric myOrders;
	private ExchangeHistoric myExchanges;
	private List<SecondHandProduct> myProducts;
	private List<Review> myReviews;
	private List<Order> ordersMade;
	private List<Exchange> exchangesMade;
	private List<ExchangeOffer> offersMade;
	private List<ExchangeOffer> offersReceived;
	
	/**
	 * Constructor para crear una nueva cuenta de cliente en el sistema.
	 * 
	 * Inicializa todos los atributos del cliente, incluyendo el carrito de compras,
	 * históricos de compras e intercambios, y listas de productos y reseñas.
	 * La fecha de registro se establece automáticamente al momento de creación.
	 *
	 * @param username       Nombre de usuario único para el inicio de sesión (ej: "juanperez").
	 * @param password       Contraseña de autenticación del cliente.
	 * @param fullname       Nombre completo del cliente (ej: "Juan Pérez García").
	 * @param dni            Documento Nacional de Identidad del cliente (ej: "12345678X").
	 * @param birthdate      Fecha de nacimiento en formato DD/MM/YYYY (ej: "15/03/1995").
	 * @param email          Dirección de correo electrónico (ej: "juan@email.com").
	 * @param phoneNumber    Número de teléfono de contacto (ej: "666123456").
	 */
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
	
	/**
	 * Añade un producto al carrito de compras del cliente.
	 * 
	 * El cliente puede añadir una cantidad específica de un producto nuevo al carrito.
	 * Si el producto ya está en el carrito, se incrementa la cantidad.
	 * La cantidad debe ser mayor que 0 y no puede exceder el stock disponible.
	 *
	 * @param p        El producto ({@link NewProduct}) a añadir al carrito.
	 * @param quantity La cantidad de unidades a añadir (debe ser > 0).
	 */
	public void addToCart(NewProduct p, int quantity) {
		try {
			this.shoppingCart.addCartItem(p, quantity);
		} catch (Exception e) {
			System.err.println("Error adding to cart: " + e.getMessage());
		}
	}
	
	/**
	 * Elimina una cantidad específica de un producto del carrito de compras.
	 * 
	 * Si la cantidad es igual o mayor que la cantidad actual en el carrito,
	 * el producto se elimina completamente. De lo contrario, solo se reduce la cantidad.
	 *
	 * @param p        El producto ({@link NewProduct}) a eliminar del carrito.
	 * @param quantity La cantidad de unidades a eliminar.
	 */
	public void removeFromCart(NewProduct p, int quantity) throws IllegalArgumentException {
		try {
			this.shoppingCart.removeCartItem(p, quantity);
		} catch (Exception e) {
			System.err.println("Error removing from cart: " + e.getMessage());
		}
	}
	
	/**
	 * Procesa la compra del contenido actual del carrito de compras.
	 * 
	 * Este método crea un nuevo pedido con todos los artículos en el carrito,
	 * intenta procesar el pago con el número de tarjeta proporcionado, y si es exitoso,
	 * limpia el carrito y devuelve un código de orden único. Si el pago falla,
	 * la orden se mantiene en el histórico pero no se genera código de confirmación.
	 * 
	 * <h3>Flujo del proceso:</h3>
	 * <ol>
	 *   <li>Obtiene los artículos del carrito</li>
	 *   <li>Crea una nueva orden ({@link Order})</li>
	 *   <li>Registra la orden en el histórico</li>
	 *   <li>Procesa el pago mediante pasarela de pago</li>
	 *   <li>Si pago exitoso: limpia carrito y retorna código</li>
	 *   <li>Si pago falla: retorna null</li>
	 * </ol>
	 *
	 * @param cardNumber El número de tarjeta de crédito/débito para procesar el pago
	 *                   (formato de validación según {@link Order#procesarPago(String)}).
	 * 
	 * @return Un código único de orden si el pago fue exitoso,
	 *         {@code null} si el pago fue rechazado.
	 */
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
	
	/**
	 * Cancela una orden previamente realizada por el cliente.
	 * 
	 * El pedido debe estar en el histórico del cliente. Una vez cancelada,
	 * se procesa el reembolso (si aplica) y se actualiza el estado del pedido.
	 *
	 * @param order La orden ({@link Order}) a cancelar.
	 * 
	 * @throws IllegalArgumentException Si el pedido no pertenece a este cliente.
	 */
	public void cancelOrder(Order order) throws IllegalArgumentException, IllegalStateException {
		if(!this.myOrders.hasOrder(order)) {
			throw new IllegalArgumentException("Invalid order");
		}
		
		try {
			order.cancelOrder();
		} catch (Exception e) {
			System.err.println("Error cancelling order: " + e.getMessage());
		}
	}
	
	/**
	 * Crea una reseña (review) de un producto comprado por el cliente.
	 * El cliente solo puede escribir reseñas de productos que ha adquirido previamente.
	 *
	 * @param product El producto ({@link NewProduct}) a reseñar.
	 *                Debe estar en el histórico de órdenes del cliente.
	 * @param rating  La valoración del producto en estrellas (1-5).
	 * @param comment El comentario o descripción detallada de la experiencia.
	 *                No puede estar vacío.
	 * 
	 * @throws IllegalArgumentException Si el producto no ha sido comprado por el cliente.
	 */
	public void reviewProduct(NewProduct product, int rating, String comment) throws IllegalArgumentException {
		if(this.myOrders.hasProduct(product) == false) {
			throw new IllegalArgumentException("Invalid product, not bought by the client");
		}
		Review review = new Review(rating, comment, product, this);
		this.myReviews.add(review);
	}
	
	/**
	 * Obtiene el carrito de compras del cliente.
	 *
	 * @return El objeto {@link ShoppingCart} con los artículos actuales pendientes de compra.
	 */
	public ShoppingCart getShoppingCart() {
		return this.shoppingCart;
	}
	
	/**
	 * Registra un nuevo producto de segunda mano en la cartera del cliente.
	 * 
	 * El cliente puede poner en venta productos que posee. Estos productos quedarán
	 * en estado "no valorado" hasta que un empleado autorizado los inspeccione y asigne un valor.
	 * Solo después de la valoración pueden ser ofrecidos en intercambios.
	 * 
	 * El nombre del producto debe ser único en la cartera del cliente.
	 *
	 * @param name        Nombre descriptivo del producto (ej: "Nintendo Switch original").
	 * @param description Descripción detallada del estado y características del producto.
	 * @param picturePath Ruta relativa de la imagen del producto (ej: "img/consola.jpg").
	 * @param itemType    El tipo de producto ({@link ItemType}: COMIC, GAME o FIGURINE).
	 *                    Nota: No puede ser PACK.
	 * 
	 * @throws IllegalArgumentException Si ya existe un producto con el mismo nombre
	 *                                  en la cartera del cliente.
	 */
	public void registerSecondHandProduct(String name, String description, String picturePath, ItemType itemType) throws IllegalArgumentException {
	    if(this.productNamedExists(name) == true) {
	        throw new IllegalArgumentException("A product with the same name is already registered in your wallet");
	    }
	    // NO añadir aquí, ya se añade en el constructor de SecondHandProduct
	    new SecondHandProduct(name, description, picturePath, itemType, this);
	}
	
	/**
	 * Registra un producto de segunda mano ya instanciado en la cartera del cliente.
	 * 
	 * Este método es útil cuando el producto ya ha sido creado previamente
	 * (ej: después de un intercambio completado).
	 *
	 * @param product El producto de segunda mano ({@link SecondHandProduct}) a registrar.
	 * 
	 * @throws IllegalArgumentException Si el producto ya existe en la cartera del cliente.
	 */
	public void registerSecondHandProduct(SecondHandProduct product) throws IllegalArgumentException {
		if(this.hasSecondHandProduct(product) == true) {
			throw new IllegalArgumentException("This product is already registered in your wallet");
		}
		this.myProducts.add(product);
	}
	
	/**
	 * Elimina un producto de segunda mano de la cartera del cliente.
	 * 
	 * Generalmente se usa cuando el producto es transferido a otro cliente
	 * mediante un intercambio exitoso.
	 *
	 * @param product El producto ({@link SecondHandProduct}) a eliminar.
	 * 
	 * @throws IllegalArgumentException Si el producto no existe en la cartera del cliente.
	 */
	public void removeSecondHandProduct(SecondHandProduct product) throws IllegalArgumentException {
		if(!this.myProducts.contains(product)) {
			throw new IllegalArgumentException("Invalid product, doesn't exist in your wallet");
		}
		this.myProducts.remove(product);
	}
	
	/**
	 * Comprueba si un producto de segunda mano pertenece a este cliente.
	 *
	 * @param product El producto ({@link SecondHandProduct}) a verificar.
	 * 
	 * @return {@code true} si el producto está en la cartera del cliente,
	 *         {@code false} en caso contrario.
	 */
	public boolean hasSecondHandProduct(SecondHandProduct product) {
		return this.myProducts.contains(product);
	}
	
	/**
	 * Crea y envía una oferta de intercambio a otro cliente.
	 * 
	 * El cliente propone intercambiar uno o más de sus productos de segunda mano
	 * por un producto de segunda mano específico que pertenece a otro cliente.
	 * 
	 * <h3>Validaciones:</h3>
	 * <ul>
	 *   <li>El cliente debe ser propietario de todos los productos ofrecidos</li>
	 *   <li>Todos los productos (ofrecidos y solicitado) deben estar disponibles</li>
	 *   <li>Los productos no pueden estar ya vinculados a otras ofertas</li>
	 * </ul>
	 *
	 * @param requested El producto de segunda mano que el cliente desea recibir.
	 *                  Pertenece a otro cliente.
	 * @param offered   Uno o más productos de segunda mano que el cliente ofrece a cambio.
	 *                  Todos deben ser propiedad de este cliente.
	 * 
	 * @throws IllegalArgumentException Si los productos ofrecidos no pertenecen al cliente,
	 *                                  o si alguno de ellos (u el solicitado) no está disponible.
	 */
	public void makeOffer(SecondHandProduct requested, SecondHandProduct...offered) throws IllegalArgumentException {
		if(this.productIsYours(offered) == false) {
			throw new IllegalArgumentException("Invalid offered products, you must own them");
		} else if(this.productIsAvailable(offered) == false || requested.isAvailable() == false) {
			throw new IllegalArgumentException("Invalid products, they must be available");
		}
		ExchangeOffer offer = new ExchangeOffer(requested, new ArrayList<>(Arrays.asList(offered)), this);
		this.offersMade.add(offer);
	}
	
	/**
	 * Cancela una oferta de intercambio previamente enviada por el cliente.
	 * 
	 * Una oferta cancelada no puede ser aceptada. Los productos quedan nuevamente
	 * disponibles para ser ofrecidos en otras transacciones.
	 *
	 * @param offer La oferta ({@link ExchangeOffer}) a cancelar.
	 * 
	 * @throws IllegalArgumentException Si la oferta no pertenece a este cliente.
	 */
	public void cancelOffer(ExchangeOffer offer) throws IllegalArgumentException, IllegalStateException {
		if(!this.offersMade.contains(offer)) {
			throw new IllegalArgumentException("Invalid offer, it's not in your register");
		}
		try {
			offer.cancelOffer();
		} catch (Exception e) {
			System.err.println("Error cancelling offer: " + e.getMessage());
		}
	}
	
	/**
	 * Recibe una nueva oferta de intercambio de otro cliente.
	 * 
	 * Este método es llamado automáticamente cuando otro cliente envía una oferta
	 * que involucra un producto de este cliente. La oferta se añade a la lista de
	 * ofertas recibidas para que el cliente pueda revisarla y responder.
	 *
	 * @param oferta La oferta ({@link ExchangeOffer}) recibida de otro cliente.
	 */
	public void receiveOffer(ExchangeOffer oferta) {
		this.offersReceived.add(oferta);
	}
	
	/**
	 * Responde a una oferta de intercambio aceptándola o rechazándola.
	 * 
	 * El cliente que recibe una oferta puede aceptarla (iniciando el proceso de intercambio)
	 * o rechazarla (cancelando la transacción y devolviendo los productos a disponibilidad).
	 *
	 * @param offer  La oferta ({@link ExchangeOffer}) a la que responder.
	 * @param accept {@code true} para aceptar la oferta y proceder con el intercambio,
	 *               {@code false} para rechazarla.
	 * 
	 * @throws IllegalArgumentException Si la oferta no está en el registro del cliente.
	 */
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
	
	/**
	 * Responde a una oferta de intercambio identificándola por producto solicitado.
	 * 
	 * Este método alternativo busca la oferta que tiene como producto solicitado
	 * el especificado, y responde a esa oferta. Es más conveniente cuando el cliente
	 * solo conoce el producto involucrado.
	 *
	 * @param product El producto de segunda mano ({@link SecondHandProduct}) que fue solicitado
	 *                en la oferta a la que se desea responder.
	 * @param accept  {@code true} para aceptar, {@code false} para rechazar.
	 * 
	 * @throws IllegalArgumentException Si el producto no pertenece a este cliente.
	 * @throws IllegalStateException    Si no existe ninguna oferta para ese producto.
	 */
	public void answerOffer(SecondHandProduct product, boolean accept) throws IllegalArgumentException, IllegalStateException {
		if(!this.productIsYours(product)) {
			throw new IllegalArgumentException("Invalid product, it is not in your wallet");
		}
		
		ExchangeOffer offer = this.offerRegarding(product);
		if(offer == null) {
			throw new IllegalStateException("This offer does not exist in your register");
		}
		
		this.answerOffer(offer, accept);
	}
	
	/**
	 * Busca una oferta recibida que solicita un producto específico.
	 * 
	 * @param product El producto ({@link SecondHandProduct}) buscado en las ofertas recibidas.
	 * 
	 * @return La oferta ({@link ExchangeOffer}) que solicita ese producto,
	 *         o {@code null} si no existe ninguna.
	 */
	public ExchangeOffer findReceivingOffer(SecondHandProduct product) throws IllegalArgumentException {
		for(ExchangeOffer offer: this.offersReceived) {
			if(offer.isRequestedProduct(product)) {
				return offer;
			}
		}
		
		return null;
	}
	
	/**
	 * Registra un nuevo intercambio completado en el histórico del cliente.
	 * 
	 * Este método es llamado después de que un intercambio ha sido validado
	 * exitosamente por un empleado autorizado.
	 *
	 * @param exchange El intercambio ({@link Exchange}) a registrar.
	 */
	public void addExchange(Exchange exchange) {
		this.exchangesMade.add(exchange);
		this.myExchanges.addExchange(exchange);
	}
	
	/**
	 * Encuentra un intercambio que involucra un producto específico de segunda mano.
	 * 
	 * @param product El producto ({@link SecondHandProduct}) cuyo intercambio se busca.
	 * 
	 * @return El intercambio ({@link Exchange}) que involucra ese producto,
	 *         o {@code null} si no existe.
	 * 
	 * @throws IllegalArgumentException Si hay errores en la búsqueda.
	 */
	public Exchange findExchange(SecondHandProduct product) throws IllegalArgumentException {
		for(Exchange exchange: this.exchangesMade) {
			if(exchange.isThisExchangeOffer(findReceivingOffer(product))) {
				return exchange;
			}
		}
		return null;
	}
	
	/**
	 * Genera un perfil completo del cliente en formato de texto para mostrar en la UI.
	 * 
	 * El perfil incluye información del usuario básica (heredada de {@link RegisteredUser}),
	 * fecha de registro, carrito actual, y previsualizaciones de hasta 3 items de:
	 * <ul>
	 *   <li>Pedidos realizados</li>
	 *   <li>Intercambios completados</li>
	 *   <li>Productos de segunda mano en cartera</li>
	 *   <li>Reseñas creadas</li>
	 *   <li>Ofertas enviadas</li>
	 *   <li>Ofertas recibidas</li>
	 * </ul>
	 *
	 * @return Una cadena formateada con el perfil completo del cliente.
	 *         Si una lista tiene más de 3 items, muestra una línea indicando cuántos hay más.
	 */
	public String clientFullProfile() {
		StringBuilder sb = new StringBuilder(super.userProfile());
		
		sb.append("Se unió el: " + this.joiningDate + "\n");
		sb.append("--- Resumen de actividad ---\n");
		sb.append("Carrito: " + this.shoppingCart.shoppingCartPreview() + "\n");
		sb.append(formatListPreview("Pedidos", Order::orderPreview, this.ordersMade));
		sb.append(formatListPreview("Intercambios", null, this.exchangesMade));
		sb.append(formatListPreview("Productos de segunda mano", SecondHandProduct::secondHandProductPreview, this.myProducts));
		sb.append(formatListPreview("Reseñas", Review::reviewPreview, this.myReviews));
		sb.append(formatListPreview("Ofertas hechas", ExchangeOffer::offerMadePreview, this.offersMade));
		sb.append(formatListPreview("Ofertas recibidas", ExchangeOffer::offerReceivedPreview, this.offersReceived));
		
		return sb.toString();
	}
	
	/**
	 * Obtiene una visualización textual del carrito de compras actual.
	 *
	 * @return Una cadena formateada con los artículos en el carrito y el precio total.
	 */
	public String viewShoppingCart() {
		return this.shoppingCart.shoppingCartView();
	}
	
	/**
	 * Obtiene una visualización textual de los productos de segunda mano del cliente.
	 * 
	 * Muestra todos los productos en la cartera del cliente con sus detalles completos.
	 * Si la cartera está vacía, muestra un mensaje informativo.
	 *
	 * @return Una cadena formateada con los productos de segunda mano del cliente.
	 */
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
	
	/**
	 * Obtiene una representación textual breve del cliente.
	 *
	 * @return Una cadena con la vista previa del usuario.
	 * 
	 * @see RegisteredUser#userPreview()
	 */
	public String toString(){
		return super.userPreview();
	}
	
	/**
	 * Obtiene el histórico de intercambios del cliente.
	 *
	 * @return El objeto {@link ExchangeHistoric} con todos los intercambios registrados.
	 */
	public transactions.ExchangeHistoric getExchangeHistoric() {
		return this.myExchanges;
	}

	/**
	 * Obtiene el histórico de pedidos (órdenes de compra) del cliente.
	 *
	 * @return El objeto {@link OrderHistoric} con todas las órdenes registradas.
	 */
	public OrderHistoric getOrderHistoric() {
		return this.myOrders;
	}

	/**
	 * Obtiene la lista de ofertas de intercambio enviadas por el cliente.
	 *
	 * @return Lista de {@link ExchangeOffer} creadas por este cliente.
	 */
	public List<ExchangeOffer> getOffersMade(){
		return this.offersMade;
	}
	
	/**
	 * Obtiene la lista de ofertas de intercambio recibidas por el cliente.
	 *
	 * @return Lista de {@link ExchangeOffer} de otros clientes dirigidas a este cliente.
	 */
	public List<ExchangeOffer> obtenerMisOfertasRecibidos(){
		return this.offersReceived;
	}
	
	/**
	 * Obtiene la lista de productos de segunda mano en la cartera del cliente.
	 *
	 * @return Lista de {@link SecondHandProduct} propiedad del cliente.
	 */
	public List<SecondHandProduct> getCarteraSegundaMano() {
		return this.myProducts;
	}

    
    
    
	// ═══════════════════════════════════════════════════════════
    // HELPERS / MÉTODOS AUXILIARES PRIVADOS
    // ═══════════════════════════════════════════════════════════

	
	/**
	 * Método auxiliar privado que genera una vista previa de una lista de elementos.
	 * 
	 * Mostrará hasta 3 elementos de la lista. Si hay más de 3, añade una línea
	 * indicando cuántos elementos adicionales no se muestran.
	 * 
	 * @param <T>               El tipo genérico de elementos en la lista.
	 * @param listName          El nombre descriptivo de la lista (ej: "Pedidos", "Intercambios").
	 * @param previewFunction   Una función que convierte cada elemento a su representación textual.
	 *                          Si es {@code null}, solo se muestra el número de items.
	 * @param list              La lista de elementos a previsualizar.
	 * 
	 * @return Una cadena formateada con la vista previa de la lista.
	 */
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
	
	/**
	 * Verifica si todos los productos especificados están disponibles.
	 * 
	 * @param products Array de productos ({@link SecondHandProduct}) a verificar.
	 * 
	 * @return {@code true} si todos están disponibles, {@code false} si alguno no lo está.
	 */
	private boolean productIsAvailable(SecondHandProduct...products) {
		for(SecondHandProduct s: products) {
			if(!s.isAvailable()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Verifica si todos los productos especificados pertenecen a este cliente.
	 * 
	 * @param products Array de productos ({@link SecondHandProduct}) a verificar.
	 * 
	 * @return {@code true} si todos pertenecen al cliente, {@code false} si alguno no.
	 */
	private boolean productIsYours(SecondHandProduct...products) {
		for(SecondHandProduct p: products) {
			if(p.isOwnedBy(this) == false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Comprueba si existe un producto con un nombre específico en la cartera del cliente.
	 * 
	 * @param name El nombre del producto a buscar.
	 * 
	 * @return {@code true} si existe un producto con ese nombre, {@code false} en caso contrario.
	 */
	private boolean productNamedExists(String name) {
		for(SecondHandProduct product : this.myProducts) {
			if(product.isNamed(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Encuentra la oferta recibida que solicita un producto específico del cliente.
	 * 
	 * @param p El producto ({@link SecondHandProduct}) buscado en las ofertas recibidas.
	 * 
	 * @return La oferta ({@link ExchangeOffer}) que lo solicita, o {@code null} si no existe.
	 */
	private ExchangeOffer offerRegarding(SecondHandProduct p) {
		for(ExchangeOffer offer: this.offersReceived) {
			if(offer.isRequestedProduct(p) == true) {
				return offer;
			}
		}
		return null;
	}
}