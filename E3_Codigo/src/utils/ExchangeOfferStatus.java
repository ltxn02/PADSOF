package utils;

/**
 * Enum para representar el estado del intercambio
 * @author Taha Ridda
 * @version 1.0
 *
 */
public enum ExchangeOfferStatus {
    PENDIENTE,
    ACEPTADA,
    RECHAZADA,
    EXPIRADA,
    CANCELADA;
	/*
	AÑADIMOS UN ESTADO CANCELADO???
	 */
	
	@Override
    public String toString() {
        return this.name();
    }

}