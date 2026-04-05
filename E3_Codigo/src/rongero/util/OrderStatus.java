package util;

/**
 * Enum para representar el estado del pedido
 * @author Taha Ridda
 * @version 1.0
 *
 */
public enum OrderStatus {
	SIN_PAGAR,
	EN_PREPARACION,
	ENTREGADO,
	CANCELADO;

	
	public String toString() {
		return this.name();
	}
	
}
