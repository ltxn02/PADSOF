import java.util.List;
/**
 * Clase para representar los productos de segunda mano
 * @author Taha Ridda
 * @version 1.0
 *
 */
public class SecondHandProduct {
	private String Name;
	private double price;
	private String Foto;
	private String description;
	private Employee empleado;

	private int secondHandId;
	private boolean isAppraised;
	private double Appraised;
	private boolean isOffered;
	private Client owner;
	private ItemType Itemtype;
	private Condition condition;
	/**
	 * @param secondHandId id del producto
	 * @param name nombre del producto
	 * @param description descripcion del producto
	 * @param price precio del producto
	 * @param isAppraised estado de revision del producto
	 * @param Owner dueño del producto
	 * @param ItemType tipo de producto
	 *
	 * */
	public SecondHandProduct(int secondHandId, String name, String description, String Foto, double price, boolean isAppraised, ItemType ItemType, Condition condition, Client Owner) {
		this.Foto = Foto;
		this.Name = name;
		this.price = price;
		this.secondHandId = secondHandId;
		this.isAppraised = isAppraised;
		this.isOffered = false;
		this.Itemtype = ItemType;
		this.condition = condition;
		this.owner = Owner;
		this.isAppraised= false;
		this.description= description;
		this.isAppraised = false;
		this.empleado= null;
	}
	/**
	 * Funcion para valorar un producto por un empleado
	 * @param e empleado que debe valorar el producto
	 * @param p producto que se valora
	 * @param c condicion del producto que se valorará
	 * @param value valor del producto
	 * */
	public boolean AppraisedSecondHand(Employee e,SecondHandProduct p, Condition c, double value){
		if (e.permissions.contains(Permission.EXCH_PRODUCT_APPRAISE)){
			p.condition = c;
			p.price = value;
			p.isAppraised= true;
			p.empleado= e;
			return true;
		}
		return false;

	}

	public void change_offered_status(boolean a){
		this.isOffered= a;
	}


	/**
	 * @author Taha Ridda En Naji
	 * @param new_ownwer nuevo propietario del producto intercambiado
	 *
	 * */
	public boolean change_owners( Client new_ownwer){
		this.owner= new_ownwer;

		return true;
	}
	public String toString(){
		return "Dueño:  " + this.owner;

	}
	public boolean estádisponible(){
		return !this.isOffered;
	}
	
	
}
