/**
 * Enum para representar el estado del intercambio
 * @author Taha Ridda
 * @version 1.0
 *
 */
public enum ExchangeStatus {
    PENDIENTE,
    ACEPTADA,
    RECHAZADA,
    EXPIRADA,
    CANCELADA;
	/*
	AÑADIMOS UN ESTADO CANCELADO???
	 */

    public String toString() {
        return this.name();
    }

}