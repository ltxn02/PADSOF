package utils;

public enum ExchangeStatus {
	EN_PROCESO,
	CANCELADO,
	COMPLETADO;
	
	@Override
	public String toString() {
		return this.name();
	}
}
