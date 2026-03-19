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
	
	/*public boolean addCartItem(NewProduct p, int quantity) {

		COMPRUEBA QUE HAY STOCK EFECTIVO SUFICIENTE (return false)

		AÑADE PRODUCTO AL CARRITO

		LLAMA A FUNCION PARA REGISTRAR MOMENTO EXACTO Y ESTABLECER EL
		STOCK EFECTIVO

		 - Item debería tener atributo de tipo Instant para registrar
		   el momento en el que un NewProduct o un SecondHandProduct se
		   reserva para carrito o intercambio.
		   
		   > Haz el método para registrar el momento exacto de tipo protected
		     (accesible solo por las subclases).

		 - NewProduct debería tener atributo effectiveStock para registrar
		   el stock que hay liberado (sin reservar en carritos) mientras
		   se mantiene correcto el stock real.
		   
		 - Se llamaría a la función de NewProduct de reservarProductos o algo
		   así que actualizaría el stock efectivo en función de la cantidad de
		   productos que va a añadir al carrito el cliente.
		
		return true;
	}*/
	
	/*public void removeCartItem(NewProduct p, int quantity) {
	 	SI CANTIDAD > QUE CANTIDAD EN EL CARRITO NO AFECTA (SE ELIMINA TODA
	 	LA CANTIDAD DE ESE PRODUCTO QUE HAYA EN EL CARRITO Y YA)
	 	
	 	LLAMA A FUNCION PARA BORRAR MOMENTO DE AÑADIR AL CARRITO DEL PRODUCTO
	 	Y RESTABLECER EL STOCK EFECTIVO
	 }*/
	
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