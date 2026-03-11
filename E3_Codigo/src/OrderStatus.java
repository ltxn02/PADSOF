/**
 * Enum para representar el estado del pedido
 * @author Taha Ridda
 * @version 1.0
 *
 */
public enum OrderStatus {
	SIN_PAGAR,
	EN_PREPARACIÓN,
	ENTREGADO;
	
	public String toString() {
		return this.name();
	}
	
}
