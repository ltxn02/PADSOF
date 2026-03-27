import java.util.List;
/**
 * Clase para representar los productos de segunda mano
 * @author Taha Ridda
 * @version 1.0
 *
 */
public class SecondHandProduct {
	public String Name;
	public double price;
	public String Foto;
	public String description;
	public Employee empleado;

	public int secondHandId;
	public boolean isAppraised;
	public double Appraised;
	public boolean isOffered;
	public Client owner;
	public ItemType Itemtype;
	public Condition condition;
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
	
	
	
}
