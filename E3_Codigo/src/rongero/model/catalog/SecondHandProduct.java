package model.catalog;
/**
 * Clase que representa los productos de segunda mano dentro del sistema.
 * Gestiona el ciclo de vida de los artículos usados, desde su registro y tasación
 * hasta su intercambio entre clientes.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import model.user.Client;
import util.ItemType;
import util.Condition;



public class SecondHandProduct extends Item {
	private static int lastSecondHandProductId = 0;
	private int secondHandId;
	private boolean isAppraised;
	private double appraisal;
	private boolean isOffered;
	private Client owner;
	private ItemType itemType;
	private Condition condition;
	private LocalDateTime dateadded;


	/**
	 * Constructor completo para un producto de segunda mano.
	 * * @param name         Nombre del producto.
	 * @param description  Descripción del estado y características.
	 * @param picturePath  Ruta a la imagen del artículo.
	 * @param price        Precio inicial (puede ser 0 antes de la tasación).
	 * @param isAppraised  Indica si el producto ya ha sido revisado por un experto.
	 * @param itemType     Categoría técnica del objeto (Comic, Juego, etc.).
	 * @param condition    Estado físico del producto (Nuevo, Usado, etc.).
	 * @param owner        Cliente que posee actualmente el producto.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public SecondHandProduct(String name, String description, String picturePath, double price, boolean isAppraised, ItemType itemType, Condition condition, Client owner) {
		super(name, description, price, picturePath);
		this.isAppraised = isAppraised;
		this.isOffered = false;
		this.itemType = itemType;
		this.condition = condition;
		this.owner = owner;
		this.dateadded = LocalDateTime.now();
		this.secondHandId = SecondHandProduct.lastSecondHandProductId;
		SecondHandProduct.lastSecondHandProductId++;
	}
	/**
	 * Constructor simplificado para productos pendientes de tasación.
	 * * @param name         Nombre del producto.
	 * @param description  Descripción del producto.
	 * @param picturePath  Ruta a la imagen.
	 * @param itemType     Tipo de ítem.
	 * @param owner        Propietario inicial.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public SecondHandProduct(String name, String description, String picturePath, ItemType itemType, Client owner) {
		this(name, description, picturePath, 0, false, itemType, null, owner);
	}

	/**
	 * Permite a un empleado tasar el producto, asignándole una condición física
	 * y un valor de mercado oficial.
	 * * @param c     Condición física evaluada.
	 * @param value Precio asignado tras la revisión.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public void appraiseSecondHand(Condition c, double value) {
		this.condition = c;
		this.setPrice(value);
		this.isAppraised= true;

	}
	/**
	 * Verifica si el producto pertenece a un cliente específico.
	 * * @param owner Cliente a verificar.
	 * @return true si el cliente es el dueño actual, false en caso contrario.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public boolean isOwnedBy(Client owner) {
		return this.owner == owner;
	}
	/**
	 * Obtiene el propietario actual del producto.
	 * @return Objeto Client que posee el artículo.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */

	public Client getOwner() {
		return owner;
	}

	/**
	 * Obtiene la fecha y hora en la que el producto fue registrado en el sistema.
	 * @return LocalDateTime del registro.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public LocalDateTime getDateadded() {
		return dateadded;
	}

	/**
	 * Cambia el estado de disponibilidad del producto (si está en una oferta activa).
	 * @param offered true si el producto está comprometido en una oferta.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public void change_offered_status(boolean offered){
		this.isOffered = offered;
	}


	/**
	 * Transfiere la propiedad del producto a un nuevo cliente (usado en intercambios).
	 * * @param new_owner El cliente que recibe el producto.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public void change_owners(Client new_owner) {
		this.owner = new_owner;
	}
	/**
	 * Genera una representación en cadena con la información detallada del producto.
	 * @return String con los datos del artículo y su estado de tasación.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	@Override
	public String toString(){
		StringBuilder res = super.itemInfo();
		if(isAppraised) {
			res.append("\tValued on: " + this.appraisal + "\n");
			res.append("\t\tCondition: " + this.condition);
		} else {
			res.append("\tPending appraisal\n");
		}
		return res.toString();

	}
	/**
	 * Obtiene las categorías asociadas al producto.
	 * @return Lista de categorías.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public List<Category> getCategories() {
		return super.getCategories();
	}

	/**
	 * Indica si el producto está disponible para ser comprado o intercambiado.
	 * @return true si no está bloqueado por una oferta activa.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public boolean isAvailable(){
		return !this.isOffered;
	}

	/**
	 * Indica si el producto ya ha pasado el proceso de tasación.
	 * @return true si está tasado.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public boolean isAppraised() {
		return isAppraised;
	}

	/**
	 * Obtiene el nombre del producto.
	 * @return Nombre del ítem.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public String getName() {
		return super.getName();
	}

	/**
	 * Obtiene el precio actual del producto.
	 * @return Valor monetario del artículo.
	 * @author Taha Ridda En Naji
	 * @version 3.0
	 */
	public double getPrice() {
		return super.getPrice();
	}
}
