import java.util.*;
import java.time.*;

public class ShoppingCart {
	private double fullPrice;
	private Map<NewProduct, Integer> cartItems;
	private static Duration timeOnHold = Duration.ofHours(48);
	
	public ShoppingCart() {
		this.fullPrice = 0;
		this.cartItems = new HashMap<>();
	}
	
	public void addCartItem(NewProduct p, int quantity) throws IllegalArgumentException {
		if(p.isEffectiveStockHigher(quantity) == false) {
			throw new IllegalArgumentException("Invalid quantity, there's not enough stock");
		}
		p.orderProduct(quantity);
		this.cartItems.put(p, quantity);
	}
	
	public void removeCartItem(NewProduct p, int quantity) throws IllegalArgumentException {		
		if(this.cartItems.containsKey(p) == false) {
			throw new IllegalArgumentException("Product not in the shopping cart");
		}
		
		if(quantity < this.cartItems.get(p)) {
			p.returnProduct(quantity, false);
			
		}
		
		p.returnProduct(quantity, isAll);
	 	/*SI CANTIDAD > QUE CANTIDAD EN EL CARRITO NO AFECTA (SE ELIMINA TODA
	 	LA CANTIDAD DE ESE PRODUCTO QUE HAYA EN EL CARRITO Y YA)
	 	
	 	LLAMA A FUNCION PARA BORRAR MOMENTO DE AÑADIR AL CARRITO DEL PRODUCTO
	 	Y RESTABLECER EL STOCK EFECTIVO*/
	 }
	
	public void removeExpiredCartItems() {
		/*Iterator<Map.Entry<NewProduct, Integer>> it = null;
		for(it = this.cartItems.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<NewProduct, Integer> entry = it.next();
			NewProduct p = entry.getKey();
			
			if(this.isExpired(p) == true) {
				it.remove();
			}
		}*/
		/*this.cartItems.entrySet().removeIf(
				entry -> {
					NewProduct p = entry.getKey();
					return this.isExpired(p);
				}
		);*/
	}
}