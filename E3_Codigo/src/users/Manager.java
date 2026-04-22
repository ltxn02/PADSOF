package users;
import utils.*;

import java.util.ArrayList;

import products.*;
import products.catalog.Category;

/**
 * Clase que representa a un Gestor (Manager) del sistema de la tienda.
 * 
 * Los gestores son usuarios con permisos administrativos de nivel superior.
 * Heredan de la clase {@link Staff} y tienen la responsabilidad de:
 * <ul>
 *   <li>Crear y gestionar cuentas de empleados</li>
 *   <li>Activar/desactivar empleados</li>
 *   <li>Asignar y revocar permisos a los empleados</li>
 *   <li>Crear y gestionar categorías del catálogo</li>
 *   <li>Crear y editar packs promocionales</li>
 * </ul>
 * 
 * Los gestores tienen acceso a funcionalidades de gestión de personal y configuración
 * del catálogo que los empleados normales no poseen.
 * 
 * @author Lidia Martin
 * @version 2.0
 */
public class Manager extends Staff implements java.io.Serializable {
	
	/**
     * Constructor para crear una nueva cuenta de gestor en el sistema.
     * Inicializa todos los atributos del gestor heredados de {@link Staff}.
     *
     * @param username       Nombre de usuario único para el inicio de sesión (ej: "lidia").
     * @param password       Contraseña cifrada o en texto plano según la política de seguridad.
     * @param fullname       Nombre completo del gestor (ej: "Lidia Martin Teres").
     * @param dni            Documento Nacional de Identidad del gestor (ej: "12345678A").
     * @param birthdate      Fecha de nacimiento en formato DD/MM/YYYY (ej: "01/01/2002").
     * @param email          Correo electrónico de contacto (ej: "lidia@rongero.es").
     * @param phoneNumber    Número de teléfono de contacto (ej: "600000000").
     * @param salary         Salario bruto anual del gestor en euros (ej: 10000.00).
     */
    public Manager (String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary){
        super(username, password, fullname, dni, birthdate, email, phoneNumber, salary);
    }
    
    /**
     * Crea una nueva cuenta de empleado en el sistema.
     *
     * @param username       Nombre de usuario único para el inicio de sesión del nuevo empleado.
     * @param password       Contraseña inicial del empleado.
     * @param fullname       Nombre completo del empleado a registrar.
     * @param dni            Documento Nacional de Identidad del empleado.
     * @param birthdate      Fecha de nacimiento en formato DD/MM/YYYY.
     * @param email          Dirección de correo electrónico corporativa del empleado.
     * @param phoneNumber    Número de teléfono del empleado.
     * @param salary         Salario bruto anual del empleado en euros.
     * @param enabled        {@code true} si la cuenta debe estar activa al crear, {@code false} si debe estar desactivada.
     * 
     * @return Una nueva instancia de {@link Employee} con los parámetros especificados.
     */
    public Employee createEmployeeAccount(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary, boolean enabled) {
    	Employee account = new Employee(username, password, fullname, dni, birthdate, email, phoneNumber, salary, enabled);
    	return account;
    }
    
    /**
     * Cambia el estado de activación (activo/inactivo) de un empleado.
     * 
     * Un empleado desactivado no podrá realizar operaciones en el sistema,
     * pero sus datos se mantienen almacenados en la base de datos.
     *
     * @param employee El empleado cuyo estado se desea cambiar.
     * @param enable   {@code true} para activar el empleado, {@code false} para desactivarlo.
     * 
     * @throws IllegalArgumentException Si el parámetro {@code employee} es nulo.
     */
    public void changeEmployeeStatus(Employee employee, boolean enable) throws IllegalArgumentException {
    	if(employee == null) {
    		throw new IllegalArgumentException("Must receive an employee");
    	}
    	
    	if(enable) {
    		employee.activateEmployee();
    	} else {
    		employee.desactivateEmployee();
    	}
    }
    
    /**
     * Asigna un nuevo permiso a un empleado.
     * 
     * El gestor puede otorgar diferentes permisos que habilitarán al empleado
     * para realizar operaciones específicas en el sistema (ej: valorar productos,
     * cargar productos masivamente, etc.).
     *
     * @param employee El empleado al que se le quiere asignar el permiso.
     * @param p        El permiso ({@link Permission}) a añadir al empleado.
     * 
     * @throws IllegalArgumentException Si el parámetro {@code employee} es nulo.
     */
    public void addPermissionTo(Employee employee, Permission p) throws IllegalArgumentException {
    	if(employee == null) {
    		throw new IllegalArgumentException("Must receive an employee");
    	}
    	employee.add_permisions(p);
    }
    
    /**
     * Revoca un permiso previamente asignado a un empleado.
     * 
     * Una vez revocado, el empleado no podrá ejecutar las operaciones
     * asociadas a ese permiso hasta que vuelva a ser asignado.
     *
     * @param employee El empleado al que se le quiere revocar el permiso.
     * @param p        El permiso ({@link Permission}) a eliminar del empleado.
     * 
     * @throws IllegalArgumentException Si el parámetro {@code employee} es nulo.
     */
    public void removePermissionTo(Employee employee, Permission p) throws IllegalArgumentException {
    	if(employee == null) {
    		throw new IllegalArgumentException("Must receive an employee");
    	}
    	employee.delete_permisions(p);
    }
    
    /**
     * Crea una nueva categoría en el sistema.
     * 
     * Las categorías se utilizan para clasificar y organizar los productos
     * en el catálogo, mejorando la navegación y presentación de los artículos.
     *
     * @param name El nombre descriptivo de la nueva categoría (ej: "Cómics y manga").
     * 
     * @return Una nueva instancia de {@link Category} con el nombre especificado.
     * @throws IllegalArgumentException Si el parámetro {@code name} es nulo o está vacío.
     */
    public Category createCategory(String name) throws IllegalArgumentException {
    	if(name == null || name.isEmpty()) {
    		throw new IllegalArgumentException("Must receive a valid string");
    	}
    	Category category = new Category(name);
    	return category;
    }
    
    /**
     * Cambia el nombre de una categoría existente.
     * 
     * Este método permite renombrar categorías ya creadas sin afectar
     * a los productos que contiene.
     *
     * @param category La categoría a ser renombrada.
     * @param name     El nuevo nombre que se desea asignar a la categoría.
     * 
     * @throws IllegalArgumentException Si {@code name} es nulo o está vacío,
     *                                  o si {@code category} es nulo.
     */
    public void renameCategory(Category category, String name) throws IllegalArgumentException {
    	if(name == null || name.isEmpty()) {
    		throw new IllegalArgumentException("Must receive a valid string");
    	}
    	
    	if(category == null) {
    		throw new IllegalArgumentException("Must receive a valid category");
    	}
    	category.rename(name);
    }
    
    /**
     * Añade un artículo (producto) a una categoría específica.
     * 
     * Este método permite clasificar productos dentro de categorías,
     * facilitando la organización del catálogo.
     *
     * @param category La categoría a la que se añadirá el artículo.
     * @param item     El artículo ({@link Item}) a clasificar.
     * 
     * @throws IllegalArgumentException Si el artículo ya existe en la categoría
     *                                  (se captura y registra internamente).
     */
    public void addItemToCategory(Category category, Item item) {
    	try {
    		category.addItem(item);
    	} catch (Exception e) {
    		System.err.println("Error adding item: " + e.getMessage());
    	}
    }
    
    /**
     * Cambia la visibilidad de una categoría en el catálogo.
     * 
     * Una categoría invisible no se mostrará a los clientes en la navegación
     * del catálogo, aunque sus productos siguen estando almacenados en el sistema.
     *
     * @param category La categoría cuya visibilidad desea cambiar.
     * @param visible  {@code true} para hacer visible la categoría,
     *                 {@code false} para ocultarla.
     * 
     * @throws IllegalArgumentException Si el parámetro {@code category} es nulo.
     */
    public void changeVisibilityCategory(Category category, boolean visible) throws IllegalArgumentException {
    	if(category == null) {
    		throw new IllegalArgumentException("Must receive a valid category");
    	}
    	category.markAs(visible);
    }
    
    /**
     * Crea un nuevo pack (lote promocional) en el catálogo.
     * 
     * Los packs permiten agrupar múltiples productos y ofrecerlos como una unidad
     * con un precio especial, promoviendo la venta cruzada y ofreciendo valor
     * adicional a los clientes.
     *
     * @param name             Nombre del pack promocional (ej: "Pack Manga Premium").
     * @param description      Descripción detallada del contenido y beneficios del pack.
     * @param price            Precio total conjunto del pack en euros.
     * @param image            Ruta relativa de la imagen representativa del pack.
     * @param stock            Cantidad inicial de packs disponibles en inventario.
     * @param categories       Lista de categorías a las que pertenece el pack.
     * @param reviews          Lista de reseñas y valoraciones iniciales del pack.
     * @param initialProducts  Lista inicial de productos que conforman el pack (mínimo 2).
     * 
     * @return Una nueva instancia de {@link Pack} con los parámetros especificados.
     * @throws IllegalArgumentException Si {@code initialProducts} contiene menos de 2 productos
     *                                  o alguno de los parámetros requeridos es nulo.
     */
    public Pack createPack(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, ArrayList<NewProduct> initialProducts) {
    	Pack pack = new Pack(name, description, price, image, stock, categories, reviews, initialProducts);
    	return pack;
    }
}
