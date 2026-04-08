package catalog;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import users.Employee;
import utils.*;
import users.Client;

/**
 * Clase que representa un producto de segunda mano (Second Hand) dentro del catálogo.
 * Hereda de {@link Item} y gestiona el ciclo de vida de productos usados, incluyendo
 * el proceso de tasación por empleados, el control de propiedad por clientes y
 * el seguimiento del estado estético del artículo.
 * @author Taha Ridda En Naji, Ivan Sanchez y Lidia Martin
 * @version 2.0
 */
public class SecondHandProduct extends Item implements java.io.Serializable {
	private static int lastSecondHandProductId = 0;
	private int secondHandId;
	private boolean isAppraised;
	private Employee Appraiser;
	private boolean isOffered;
	private Client owner;
	private ItemType itemType;
	private Condition condition;
	private LocalDateTime dateadded;

	/**
	 * Constructor completo para inicializar un producto de segunda mano.
	 * Registra automáticamente el producto en la lista del propietario.
	 *
	 * @param name        Nombre del producto.
	 * @param description Descripción del producto.
	 * @param picturePath Ruta de la imagen del producto.
	 * @param price       Precio asignado.
	 * @param isAppraised Indica si el producto ya ha sido revisado por un profesional.
	 * @param itemType    Tipo de ítem.
	 * @param condition   Estado físico/estético del producto.
	 * @param owner       Cliente que pone el producto a disposición de la tienda.
	 * @throws IllegalArgumentException si el itemType es un PACK.
	 */
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

		if (this.owner != null) {
			this.owner.registerSecondHandProduct(this);
		}
		this.Appraiser = null;
	}

	/**
	 * Constructor simplificado para productos pendientes de tasación inicial.
	 *
	 * @param name        Nombre del producto.
	 * @param description Descripción del producto.
	 * @param picturePath Ruta de la imagen.
	 * @param itemType    Tipo de ítem.
	 * @param owner       Cliente propietario.
	 */
	public SecondHandProduct(String name, String description, String picturePath, ItemType itemType, Client owner) {
		this(name, description, picturePath, 0, false, itemType, null, owner);
	}

	/**
	 * Realiza la tasación formal del producto.
	 * Actualiza el estado, la condición estética y el precio de mercado basándose
	 * en el criterio de un empleado autorizado.
	 * @param e     Empleado que realiza la tasación.
	 * @param c     Condición estética asignada tras la revisión.
	 * @param value Precio de venta recomendado.
	 */
	public void appraiseSecondHand(Employee e, Condition c, double value) {
		if (e.permissions.contains(Permission.EXCH_PRODUCT_APPRAISE)){
			this.condition = c;
			this.setPrice(value);
			this.isAppraised = true;
			this.Appraiser = e;
		}
	}

	/**
	 * Obtiene el empleado que realizó la tasación del producto.
	 * @return El {@link Employee} tasador, o null si no ha sido tasado.
	 */
	public Employee getAppraiser() {
		return Appraiser;
	}

	/**
	 * Comprueba si el producto pertenece a un cliente específico.
	 * @param owner El cliente a comprobar.
	 * @return true si es el dueño, false en caso contrario.
	 */
	public boolean isOwnedBy(Client owner) {
		return this.owner == owner;
	}

	/**
	 * Obtiene el cliente propietario original del producto.
	 * @return El objeto {@link Client} dueño.
	 */
	public Client getOwner() {
		return owner;
	}

	/**
	 * Obtiene la fecha y hora en la que el producto fue registrado en el sistema.
	 * @return {@link LocalDateTime} del registro.
	 */
	public LocalDateTime getDateadded() {
		return dateadded;
	}

	/**
	 * Cambia el estado de oferta del producto (si está bloqueado en un proceso de intercambio).
	 * @param offered true para bloquear, false para liberar.
	 */
	public void change_offered_status(boolean offered){
		this.isOffered = offered;
	}

	/**
	 * Transfiere la propiedad del producto a un nuevo cliente.
	 * Útil para finalizar procesos de intercambio o compra-venta entre particulares.
	 * @param new_owner El nuevo {@link Client} propietario.
	 */
	public void change_owners(Client new_owner) {
		this.owner.removeSecondHandProduct(this);
		this.owner = new_owner;
	}

	/**
	 * Genera una vista previa textual simplificada para listados rápidos.
	 * @return String con información básica y estado de tasación.
	 */
	public String secondHandProductPreview() {
		StringBuilder sb = new StringBuilder("  " + super.itemPreview() + " | ");

		if(this.isAppraised) {
			sb.append(this.condition + " (" + String.format("%.2f €", super.getPrice()));
		} else {
			sb.append("Pending appraisal");
		}

		return sb.toString();
	}

	/**
	 * Asigna manualmente un tasador al producto.
	 * @param e Empleado responsable.
	 */
	public void registrarAppraiser(Employee e){
		this.Appraiser = e;
	}

	/**
	 * Devuelve una representación detallada del producto en formato cadena.
	 * @return Información completa del ítem incluyendo tasación y condición.
	 */
	@Override
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

	/**
	 * Obtiene el estado físico actual del producto.
	 * @return La {@link Condition} del producto.
	 */
	public Condition getCondition() {
		return condition;
	}

	/**
	 * Obtiene las categorías asociadas al ítem.
	 * @return Lista de categorías.
	 */
	@Override
	public ArrayList<Category> getCategories() {
		return super.getCategories();
	}

	/**
	 * Indica si el producto está disponible para ser adquirido o intercambiado.
	 * @return true si no está bloqueado por una oferta activa.
	 */
	public boolean isAvailable(){
		return !this.isOffered;
	}

	/**
	 * Verifica si el producto ya ha pasado por el proceso de tasación.
	 * @return true si está tasado, false si está pendiente.
	 */
	public boolean isAppraised() {
		return this.isAppraised;
	}

	/**
	 * Obtiene el nombre del producto de segunda mano.
	 * @return Nombre del ítem.
	 */
	@Override
	public String getName() {
		return super.getName();
	}

	/**
	 * Obtiene el precio actual del producto.
	 * @return El precio en euros.
	 */
	@Override
	public double getPrice() {
		return super.getPrice();
	}

	/**
	 * Muestra por consola la ficha del producto optimizada para la vista del cliente.
	 * Se centra en la estética y el precio final de venta.
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
	 * Muestra por consola la ficha técnica completa orientada a empleados y gestores.
	 * Incluye identificadores internos, fechas de alta y metadatos de control.
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