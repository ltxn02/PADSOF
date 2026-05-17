package utils;

/**
 * Enum para representar el tipo de producto de segunda mano para intercambios
 * @author Taha Ridda
 * @version 1.0
 *
 */
public enum ItemType {
	COMIC,
	GAME,
	FIGURINE,
	PACK;
	
	public String toString() {
		return this.name();		
	}
}
