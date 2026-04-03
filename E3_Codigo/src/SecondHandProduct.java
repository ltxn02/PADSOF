import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
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
	private ArrayList<Review> reviews;
	private static int secondHandId = 0;
	private boolean isAppraised;
	private double Appraised;
	private boolean isOffered;
	private Client owner;
	private ItemType Itemtype;
	private Condition condition;
	private ArrayList<Category> categories;
	private LocalDateTime dateadded;

	/**
	 * @param name nombre del producto
	 * @param description descripcion del producto
	 * @param price precio del producto
	 * @param isAppraised estado de revision del producto
	 * @param Owner dueño del producto
	 * @param ItemType tipo de producto
	 *
	 * */
	public SecondHandProduct(String name, String description, String Foto, double price, boolean isAppraised, ItemType ItemType, Condition condition, Client Owner) {
		this.Foto = Foto;
		this.Name = name;
		this.price = price;
		this.secondHandId++;
		this.isAppraised = isAppraised;
		this.isOffered = false;
		this.Itemtype = ItemType;
		this.condition = condition;
		this.owner = Owner;
		this.isAppraised= false;
		this.description= description;
		this.isAppraised = false;
		this.empleado= null;
		this.categories= new ArrayList<>();
		this.dateadded = LocalDateTime.now();
	}

	public void add_categories(Category e){
		this.categories.add(e);
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

	public Client getOwner() {
		return owner;
	}

	public LocalDateTime getDateadded() {
		return dateadded;
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
		return "\nNombre: " + this.Name
				+ "\nFecha de Añadido:  " + this.dateadded
				+	"\nCategorias: " + this.categories
					+ "\nDescripcion:  " + this.description
				+	"\nValorado en: " + this.price
						+ "\nCondicion:  " + this.condition
				+ "\n Empleado que valoró:" + this.empleado;

	}

	public ArrayList<Category> getCategories() {
		return categories;
	}

	public boolean estádisponible(){
		return !this.isOffered;
	}

	public boolean isAppraised() {
		return isAppraised;
	}

	public int calculateRating(){
		int rating = 0;
		for (Review review : reviews){
			rating += review.getRating();
		}

		rating /= reviews.size();
		return rating;
	}

	public String getName() {
		return Name;
	}

	public double getPrice() {
		return price;
	}
}
