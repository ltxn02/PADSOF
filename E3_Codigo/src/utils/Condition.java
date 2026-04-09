package utils;

/**
 * Enum para representar la condición de los productos de segunda mano para intercambios
 * @author Taha Ridda
 * @version 1.0
 *
 */
public enum Condition {
	PERFECTO,
	MUY_BUENO,
	USO_LIGERO,
	USO_EVIDENTE,
	DAÑADO;
	
	public String toString() {
		return this.name();
	}
}
