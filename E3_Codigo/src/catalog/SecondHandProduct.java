package catalog;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import utils.*;
import users.Client;
/**
 * Clase para representar los productos de segunda mano
 * @author Taha Ridda En Naji
 * @version 2.0
 *
 */
public class SecondHandProduct extends Item {
	private static int lastSecondHandProductId = 0;
	private int secondHandId;
	private boolean isAppraised;
	private boolean isOffered;
	private Client owner;
	private ItemType itemType;
	private Condition condition;
	private LocalDateTime dateadded;

	/**
	 * @param name nombre del producto
	 * @param description descripcion del producto
	 * @param price precio del producto
	 * @param isAppraised estado de revision del producto
	 * @param owner dueño del producto
	 * @param itemType tipo de producto
	 *
	 * */
	public SecondHandProduct(String name, String description, String picturePath, double price, boolean isAppraised, ItemType itemType, Condition condition, Client owner) {
		super(name, description, price, picturePath);
		if(itemType == ItemType.PACK) {
			throw new IllegalArgumentException("Invalid item type, a second hand product cannot be a pack");
		}
		this.isAppraised = isAppraised;
		this.isOffered = false;
		this.itemType = itemType;
		this.condition = condition;
		this.owner = owner;
		this.dateadded = LocalDateTime.now();
		this.secondHandId = SecondHandProduct.lastSecondHandProductId;
		SecondHandProduct.lastSecondHandProductId++;
	}
	
	public SecondHandProduct(String name, String description, String picturePath, ItemType itemType, Client owner) {
		this(name, description, picturePath, 0, false, itemType, null, owner);
	}

	/**
	 * Funcion para valorar un producto por un empleado
	 * @param c condicion del producto que se valorará
	 * @param value valor del producto
	 * */
	public void appraiseSecondHand(Condition c, double value) {
		this.condition = c;
		this.setPrice(value);
		this.isAppraised= true;
	}
	
	public boolean isOwnedBy(Client owner) {
		return this.owner == owner;
	}
	
	public Client getOwner() {
		return owner;
	}

	public LocalDateTime getDateadded() {
		return dateadded;
	}
	
	public void change_offered_status(boolean offered){
		this.isOffered = offered;
	}

	/**
	 * @author Taha Ridda En Naji
	 * @param new_owner nuevo propietario del producto intercambiado
	 *
	 * */
	public void change_owners(Client new_owner) {
		this.owner.removeSecondHandProduct(this);
		this.owner = new_owner;
	}
	
	public String secondHandProductPreview() {
		StringBuilder sb = new StringBuilder("  " + super.itemPreview() + " | ");
		
		if(this.isAppraised) {
			sb.append(this.condition + " (" + String.format(".2f €", super.getPrice()));
		} else {
			sb.append("Pending appraisal");
		}
		
		return sb.toString();
	}
	
	public String toString(){
		StringBuilder res = new StringBuilder(super.itemInfo());
		if(isAppraised) {
			res.append("\tValued on: " + super.getPrice() + "\n");
			res.append("\t\tCondition: " + this.condition);
		} else {
			res.append("\tPending appraisal\n");
		}
		return res.toString();

	}

	public Condition getCondition() {
		return condition;
	}

	public ArrayList<Category> getCategories() {
		return super.getCategories();
	}

	public boolean isAvailable(){
		return !this.isOffered;
	}

	public boolean isAppraised() {
		return this.isAppraised;
	}

	public String getName() {
		return super.getName();
	}

	public double getPrice() {
		return super.getPrice();
	}

	/**
	 * Muestra la ficha del producto desde la perspectiva de un comprador o del dueño.
	 * Se centra en el nombre, estado estético y precio.
	 */
	public void imprimirCliente() {
		String linea = "****************************************************************";
		System.out.println("\n" + linea);
		System.out.println("                VISTA DE PRODUCTO: " + this.getName().toUpperCase());
		System.out.println(linea);

		System.out.println("  ESTADO ESTÉTICO: " + (this.condition != null ? this.condition : "Pendiente de revisión"));

		if (isAppraised) {
			System.out.printf("  PRECIO DE VENTA: %.2f €\n", this.getPrice());
		} else {
			System.out.println("  PRECIO DE VENTA: [ En proceso de tasación ]");
		}

		System.out.println("\n  DESCRIPCIÓN:");
		System.out.println("  \"" + this.getDescription()+ "\"");

		System.out.println(linea + "\n");
	}

	/**
	 * Muestra la ficha técnica completa para empleados y gestores.
	 * Incluye IDs de base de datos, fechas de registro y control de disponibilidad.
	 */
	public void imprimirSuperior() {
		String decoracion = "################################################################";
		System.out.println("\n" + decoracion);
		System.out.println("  PANEL DE CONTROL TÉCNICO - ID INTERNO: SH-" + this.secondHandId);
		System.out.println(decoracion);

		System.out.println("  DATOS DE PROPIEDAD:");
		System.out.println("    > Propietario: " + (owner != null ? owner.getUsername() : "SISTEMA"));
		System.out.println("    > Fecha Alta:  " + this.dateadded);

		System.out.println("\n  CONTROL DE ESTADO:");
		System.out.println("    > Tipo Item:   " + this.itemType);
		System.out.println("    > Tasado:      " + (isAppraised ? "SÍ" : "NO"));
		System.out.println("    > En Oferta:   " + (isOffered ? "SÍ (Bloqueado)" : "NO (Disponible)"));

		System.out.println("\n  VALORES FINANCIEROS:");
		System.out.printf("    > Precio Base: %.2f €\n", this.getPrice());
		System.out.println("    > Condición:   " + (this.condition != null ? this.condition : "POR DEFINIR"));

		System.out.println(decoracion + "\n");
	}

}
