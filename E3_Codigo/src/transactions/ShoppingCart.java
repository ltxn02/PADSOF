package transactions;

import java.util.*;
import java.time.*;
import java.util.concurrent.*;
import utils.*;
import users.*;
import discounts.*;
import products.*;

/**
 * Clase que representa el carrito de compras persistente de un cliente.
 * 
 * {@code ShoppingCart} gestiona todos los aspectos del carrito de compras incluyendo:
 * <ul>
 *   <li>Adición y eliminación de productos con control de stock</li>
 *   <li>Cálculo automático del precio total con aplicación de descuentos</li>
 *   <li>Gestión de productos caducados (expiración automática tras 48 horas)</li>
 *   <li>Aplicación de descuentos por volumen y promociones con regalo</li>
 *   <li>Sincronización thread-safe para acceso concurrente</li>
 * </ul>
 * 
 * <h3>Características de la expiración:</h3>
 * Los productos añadidos al carrito tienen un tiempo máximo de permanencia (por defecto 48 horas).
 * Un servicio ejecutor en background elimina automáticamente los items expirados cada minuto.
 * Esta característica simula carritos reales donde los productos se "reservan" por tiempo limitado.
 * 
 * <h3>Thread-safety:</h3>
 * Todos los métodos que modifican o acceden a datos compartidos están sincronizados
 * (synchronized) para evitar condiciones de carrera en entornos multi-hilo.
 * El servicio de limpieza de items expirados es un daemon thread que no impide
 * el cierre de la aplicación.
 * 
 * @author Lidia Martin
 * @version 2.3
 */
public class ShoppingCart implements java.io.Serializable{
	private double fullPrice;
	private List<CartItem> cartItems;
	private static Duration timeOnHold = Duration.ofHours(48);
	private List<discounts.IVolumen> globalDiscounts;
	private List<products.Item> gifts;
	
	/**
	 * Servicio ejecutor para la limpieza automática de items expirados.
	 * 
	 * Es un servicio de un único thread configurado como daemon para que no impida
	 * el cierre de la aplicación. Ejecuta {@link #removeExpiredCartItems()} cada minuto.
	 * Se debe detener explícitamente llamando a {@link #shutdownCleaner()} al cerrar la app.
	 */
	private static final ScheduledExecutorService cleaner =
			Executors.newSingleThreadScheduledExecutor(r -> {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			});
	
	/**
	 * Constructor para crear un nuevo carrito de compras vacío.
	 * 
	 * Inicializa el carrito con:
	 * <ul>
	 *   <li>Precio total en 0€</li>
	 *   <li>Lista de items vacía</li>
	 *   <li>Lista de descuentos globales vacía</li>
	 *   <li>Lista de regalos vacía</li>
	 *   <li>Inicia el servicio de limpieza automática cada minuto</li>
	 * </ul>
	 * 
	 * <strong>Nota:</strong> Debe asegurarse de llamar a {@link #shutdownCleaner()} 
	 * al cerrar la aplicación para detener el servicio en background.
	 */
	public ShoppingCart() {
		this.fullPrice = 0;
		this.cartItems = new ArrayList<>();
		this.globalDiscounts = new ArrayList<>();
		this.gifts = new ArrayList<>();
		
		ShoppingCart.cleaner.scheduleAtFixedRate(this::removeExpiredCartItems, 1, 1, TimeUnit.MINUTES);
	}
	
	/**
	 * Añade un producto al carrito o incrementa su cantidad si ya existe.
	 * 
	 * Si el producto ya está en el carrito, se incrementa su cantidad en lugar de crear
	 * un nuevo CartItem. El precio total se actualiza automáticamente.
	 * Se valida que haya stock suficiente para la cantidad solicitada.
	 *
	 * @param p        El producto ({@link NewProduct}) a añadir.
	 * @param quantity La cantidad de unidades a añadir (debe ser > 0).
	 */
	public synchronized void addCartItem(NewProduct p, int quantity) {		
	    CartItem c = null;
	    double oldPrice = 0;
	    
	    if((c = this.cartContains(p)) == null) {
	        c = new CartItem(p, quantity);
	        this.cartItems.add(c);  // ← SOLO AÑADIR SI NO EXISTE
	    } else {
	        oldPrice = c.fullPrice();
	        try {
	            c.orderQuantity(quantity);
	        } catch (Exception e) {
	            System.err.println("Error ordering cart item: " + e.getMessage());
	        }
	    }
	    
	    this.fullPrice -= oldPrice;
	    this.fullPrice += c.fullPrice();
	}
	
	/**
	 * Elimina una cantidad específica de un producto del carrito.
	 * 
	 * Si la cantidad solicitada es >= a la cantidad en el carrito, el CartItem se elimina completamente.
	 * Si la cantidad es menor, solo se reduce la cantidad en el CartItem.
	 * El precio total se recalcula automáticamente.
	 *
	 * @param p        El producto ({@link NewProduct}) a eliminar del carrito.
	 * @param quantity La cantidad de unidades a eliminar.
	 */
	public synchronized void removeCartItem(NewProduct p, int quantity) {		
		CartItem c = null;
		double oldPrice = 0, newPrice = 0;
		
		if((c = this.cartContains(p)) != null) {
			oldPrice = c.fullPrice();
			try {
				c.removeQuantity(quantity);
			} catch (Exception e) {
				System.err.println("Error removing cart item: " + e.getMessage());
			}
			
			if(c.isEmpty() == true) {
				this.cartItems.remove(c);
			} else {
				newPrice = c.fullPrice();
			}
		}
		
		this.fullPrice -= oldPrice;
		this.fullPrice += newPrice;
	 }
	
	/**
	 * Detiene el servicio ejecutor de limpieza de items expirados.
	 * 
	 * <strong>IMPORTANTE:</strong> Este método debe llamarse obligatoriamente
	 * al cerrar la aplicación para detener el thread daemon que limpia items expirados.
	 * En el main() de la aplicación debe haber una llamada a este método.
	 */
	public static void shutdownCleaner() {
		ShoppingCart.cleaner.shutdownNow();
	}
	
	/**
	 * Modifica la duración máxima que un producto puede permanecer en el carrito.
	 * 
	 * Por defecto es de 48 horas. Este método permite cambiar ese valor de forma estática
	 * para todos los carritos de la aplicación.
	 * 
	 * @param newTimeOnHold El nuevo tiempo en horas (se convierte internamente a Duration).
	 *                      Ejemplo: 24 para 1 día, 72 para 3 días.
	 */
	public synchronized void setTimeOnHold(long newTimeOnHold) {
		ShoppingCart.timeOnHold = Duration.ofHours(newTimeOnHold);
	}
	
	/**
	 * Obtiene una vista previa breve del contenido actual del carrito.
	 * 
	 * Retorna una cadena con formato: "X items. Total: YYY.YY€"
	 * Útil para mostrar en menús o listados sin ocupar mucho espacio.
	 * No incluye descuentos aplicados, solo el subtotal.
	 *
	 * @return Una cadena formateada como "3 items. Total: 145.50€".
	 *         Si el carrito está vacío, retorna "0 items. Total: 0.0€".
	 */
	public synchronized String shoppingCartPreview() {
		int items = (this.cartItems == null) ? 0 : this.cartItems.size();	
		return items + " items. Total: " + this.fullPrice;
	}
	
	/**
	 * Obtiene una vista completa y detallada del carrito en formato texto.
	 * 
	 * <h3>Ejemplo de salida:</h3>
	 * <pre>
	 * --- MI CARRITO DE LA COMPRA ---
	 *   1 x One Piece Vol. 1 x 7.99 = 7.99€
	 *   2 x Catan x 45.00 = 90.00€
	 *   1 x Goku Figure (0,00 €) [REGALO]
	 * ----------------
	 * TOTAL: 89.50 €
	 * </pre>
	 *
	 * @return Una cadena formateada con la vista detallada del carrito.
	 *         Si está vacío, retorna solo: "--- MI CARRITO DE LA COMPRA ---\nEl carrito está vacío..."
	 */
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
			for (products.Item gift : this.gifts) {
				sb.append("  1 x " + gift.getName() + " (0,00 €) [REGALO]\n");
			}
		}

		sb.append("----------------\n");
		sb.append("TOTAL: " + String.format("%.2f €", totalReal));
		return sb.toString();
	}
	
	/**
	 * Vacía completamente el carrito eliminando todos los items y regalos.
	 * 
	 * Establece el precio total en 0€ y limpia ambas listas (items y regalos).
	 * Se utiliza típicamente después de procesar un pago exitoso.
	 */
	public synchronized void clearCart() {
		this.cartItems.clear();
		this.fullPrice =0;
		this.gifts.clear();
	}
	
	/**
	 * Obtiene la lista de CartItems actualmente en el carrito.
	 * 
	 * @return Un {@code List<CartItem>} con todos los items del carrito.
	 *         Puede estar vacía si no hay productos.
	 */
	public synchronized List<CartItem> getCartItems() {
		return this.cartItems;
	}

	/**
	 * Calcula el precio total final del carrito con todos los descuentos aplicados.
	 * 
	 * @return El precio total final en euros con precisión de 2 decimales.
	 *         Incluye la aplicación cascada de todos los descuentos:
	 *         individuales, por cantidad, por volumen, y regalos.
	 */
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
	
	/**
	 * Obtiene el precio bruto del carrito sin aplicar descuentos globales.
	 * 
	 * @return El precio neto (subtotal) en euros.
	 */
	public double getFullPrice() {
		return this.fullPrice;
	}
	
	/**
	 * Añade un descuento global por volumen al carrito.
	 *
	 * @param d El descuento global ({@link IVolumen}) a aplicar.
	 *          Puede implementar también {@link IRegalo} para añadir regalos.
	 */
	public void addGlobalDiscount(discounts.IVolumen d) {
		this.globalDiscounts.add(d);
	}
	
	/**
	 * Obtiene la lista de regalos que se aplicarán al carrito.
	 *
	 * @return Un {@code List<Item>} con los artículos regalados.
	 *         Puede estar vacía si no hay regalos.
	 */
	public List<products.Item> getGifts() {
		return this.gifts;
	}
	
	/**
	 * Añade un artículo regalado al carrito.
	 * 
	 * @param item El artículo ({@link Item}) a añadir como regalo.
	 */
	public void addGift(products.Item item) {
		if (item != null && !this.gifts.contains(item)) {
			this.gifts.add(item);
		}
	}

    
    
    
	// ═══════════════════════════════════════════════════════════
    // HELPERS / MÉTODOS AUXILIARES PRIVADOS
    // ═══════════════════════════════════════════════════════════

	
	/**
	 * Verifica si un producto ya existe en el carrito y retorna su CartItem.
	 * 
	 * Método privado auxiliar que busca lineal en la lista de items del carrito.
	 * Útil para evitar duplicados y para incrementar cantidades.
	 *
	 * @param p El producto ({@link NewProduct}) a buscar.
	 * 
	 * @return El {@link CartItem} que contiene el producto, o {@code null} si no está en el carrito.
	 */
	private synchronized CartItem cartContains(NewProduct p) {
		for(CartItem c : this.cartItems) {
			if(c.isProduct(p) == true) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Elimina automáticamente todos los CartItems cuyo tiempo de permanencia en el carrito
	 * ha excedido {@link #timeOnHold} (por defecto 48 horas).
	 * 
	 * Este método es llamado automáticamente cada minuto por el servicio ejecutor ({@link #cleaner}).
	 * Recalcula el precio total después de eliminar los items expirados.
	 */
	private synchronized void removeExpiredCartItems() {
		this.cartItems.removeIf(item -> item.isExpired(ShoppingCart.timeOnHold));
		this.fullPrice = 0;
		for(CartItem item: this.cartItems) {
			this.fullPrice += item.fullPrice();
		}
	}
}