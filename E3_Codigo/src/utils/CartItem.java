package utils;

import catalog.NewProduct;
import java.time.*;


/**
 * Clase que representa un artículo individual dentro del carrito de compras.
 * 
 * {@code CartItem} encapsula la combinación de un producto ({@link NewProduct}) con
 * una cantidad específica. Es responsable de:
 * <ul>
 *   <li>Mantener la referencia al producto y su cantidad en el carrito</li>
 *   <li>Calcular el precio total del item (cantidad × precio unitario)</li>
 *   <li>Gestionar cambios de cantidad con validación de stock</li>
 *   <li>Detectar items expirados (que han permanecido demasiado tiempo en el carrito)</li>
 *   <li>Sincronizar el estado del producto con los cambios en el carrito</li>
 * </ul>
 * 
 * @author Lidia Martin
 * @version 1.5
 */
public class CartItem implements java.io.Serializable{
	private NewProduct product;
	private int quantity;
	
	/**
	 * Constructor para crear un nuevo item de carrito. Inicializa el CartItem con un producto 
	 * y una cantidad específica.
	 *
	 * @param product  El producto ({@link NewProduct}) que representa este item.
	 * @param quantity La cantidad inicial de unidades (debe ser > 0).
	 *                 Esta cantidad debe tener stock disponible en el producto.
	 */
	public CartItem(NewProduct product, int quantity) {
	    this.product = product;
	    this.quantity = quantity;
	    this.product.addedToCart();
	}
	
	/**
	 * Calcula el precio total de este CartItem (cantidad × precio unitario).
	 * 
	 * Obtiene el precio actual del producto (sin descuentos globales) y lo multiplica
	 * por la cantidad. Este cálculo se usa para actualizar el precio total del carrito.
	 *
	 * @return El precio total en euros: cantidad × precio unitario.
	 *         Ejemplo: 3 unidades × 15€ = 45€
	 */
	public double fullPrice() {
		return this.quantity * this.product.getPrice();
	}
	
	/**
	 * Establece una nueva cantidad para este CartItem con validación de stock.
	 * 
	 * Verifica que el producto tenga stock suficiente para la nueva cantidad solicitada.
	 * Si hay stock, actualiza internamente la cantidad y notifica al producto sobre
	 * la nueva cantidad pedida mediante {@link NewProduct#orderProduct(int)}.
	 * 
	 * <h3>Validaciones:</h3>
	 * <ul>
	 *   <li>Se verifica que el stock disponible sea >= a la cantidad solicitada</li>
	 *   <li>Si no hay stock suficiente, se lanza excepción sin cambiar nada</li>
	 *   <li>Si hay stock, se actualiza la cantidad y se notifica al producto</li>
	 * </ul>
	 *
	 * @param quantity La nueva cantidad deseada.
	 * 
	 * @throws IllegalArgumentException Si no hay stock suficiente para la cantidad solicitada.
	 */
	public void orderQuantity(int quantity) throws IllegalArgumentException {
		if(this.product.isEffectiveStockHigher(quantity) == false) {
			throw new IllegalArgumentException("Invalid quantity, there's not enough stock");
		}
		this.product.orderProduct(quantity);
		this.quantity = quantity;
	}
	
	/**
	 * Reduce la cantidad de este CartItem, devolviendo unidades al stock disponible.
	 * 
	 * Reduce la cantidad de unidades en el CartItem. Si la cantidad reducida >= cantidad actual,
	 * se establece la cantidad a 0 (el CartItem se marca como vacío). El método notifica
	 * al producto sobre la devolución mediante {@link NewProduct#returnProduct(int, boolean)}.
	 *  
	 * <h3>Ejemplo:</h3>
	 * <pre>
	 * CartItem tiene 5 unidades.
	 * removeQuantity(2) → CartItem tendrá 3 unidades, se devuelven 2 al stock
	 * removeQuantity(5) → CartItem tendrá 0 unidades, se devuelven 3 al stock (todo)
	 * </pre>
	 *
	 * @param quantity La cantidad de unidades a remover/devolver.
	 *                 Si es >= a la cantidad actual, se devuelven todas.
	 * 
	 * @throws IllegalArgumentException Si quantity es negativa.
	 */
	public void removeQuantity(int quantity) throws IllegalArgumentException {
		if(quantity < 0) {
			throw new IllegalArgumentException("Invalid quantity, must be positive");
		}
		
		boolean isAll = false;
		int aux = quantity;
		
		if(aux >= this.quantity) {
			aux = this.quantity;
			this.quantity = 0;
			isAll = true;
		} else {
			this.quantity -= aux;
		}
		this.product.returnProduct(aux, isAll);
	}
	
	/**
	 * Verifica si este CartItem ha expirado (ha permanecido demasiado tiempo en el carrito).
	 * 
	 * @param time La duración máxima permitida (ej: Duration.ofHours(48) para 48 horas).
	 * 
	 * @return {@code true} si el item ha expirado (tiempo transcurrido > duración máxima),
	 *         {@code false} si aún está dentro del período permitido.
	 */
	public boolean isExpired(Duration time) {
		return this.product.isExpired(time);
	}
	
	/**
	 * Comprueba si este CartItem está vacío (no contiene unidades).
	 *
	 * @return {@code true} si quantity == 0, {@code false} en caso contrario.
	 */
	public boolean isEmpty() {
		return this.quantity == 0;
	}
	
	/**
	 * Verifica si este CartItem contiene un producto específico.
	 * 
	 * @param p El producto ({@link NewProduct}) a verificar.
	 * 
	 * @return {@code true} si este CartItem contiene el producto p,
	 *         {@code false} en caso contrario.
	 */
	public boolean isProduct(NewProduct p) {
		return this.product == p;
	}
	
	/**
	 * Obtiene una representación en texto del CartItem para visualización en UI.
	 * 
	 * Retorna una cadena con formato: "X x [nombre del producto con detalles]"
	 * Ejemplo: "3 x One Piece Vol. 1 (7.99€)"
	 * 	 *
	 * @return Una cadena formateada como "cantidad x producto preview".
	 */
	@Override
	public String toString() {
		return this.quantity + " x " + this.product.toShortString();
	}
	
	/**
	 * Obtiene el producto que este CartItem representa.
	 *
	 * @return El objeto {@link NewProduct} de este CartItem.
	 */
	public NewProduct getProduct() {
		return this.product;
	}
	
	/**
	 * Obtiene la cantidad de unidades en este CartItem.
	 *
	 * @return El número de unidades (quantity).
	 *         Puede ser 0 si el CartItem está vacío.
	 */
	public int getQuantity() {
		return this.quantity;
	}
}