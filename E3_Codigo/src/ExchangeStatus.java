/**
 * Enum para representar el estado del intercambio
 * @author Taha Ridda
 * @version 1.0
 *
 */
public enum ExchangeOffer {
    PENDIENTE,
    ACEPTADA,
    RECHAZADA,
    EXPIRADA;
	/*
	AÑADIMOS UN ESTADO CANCELADO???
	 */

    public String toString() {
        return this.name();
    }

}