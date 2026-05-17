package users;
import utils.*;
import catalog.*;
import java.util.*;

/**
 * Clase abstracta que representa un usuario base del sistema.
 * 
 * {@code User} es la clase raíz de la jerarquía de usuarios en la aplicación.
 * Define funcionalidades comunes de visualización y búsqueda en el catálogo que
 * son accesibles a todos los usuarios del sistema, independientemente de su rol
 * (cliente, empleado o gestor). Hereda de {@link BaseElement} para incorporar
 * funcionalidades de auditoría y control de estado.
 * 
 * <h3>Funcionalidades principales:</h3>
 * <ul>
 *   <li>Visualización del catálogo con visibilidad controlada según rol del usuario</li>
 *   <li>Búsqueda de productos en el catálogo por nombre o descripción</li>
 *   <li>Filtrado de juegos por rango de edad recomendado</li>
 * </ul>
 * 
 * <h3>Jerarquía de herencia:</h3>
 * <pre>
 *     BaseElement (clase base)
 *           ↑
 *           |
 *         User (clase abstracta)
 *           ↑
 *           |
 *    RegisteredUser (clase abstracta)
 *           ↑
 *      ┌────┴────┐
 *      |         |
 *    Staff    Client
 *      ↑
 *   ┌──┴──┐
 *   |     |
 * Manager Employee
 * </pre>
 * 
 * <h3>Control de acceso al catálogo:</h3>
 * El método {@link #view_catalog(User, Catalog)} implementa lógica de control de acceso:
 * <ul>
 *   <li><strong>Empleados y Gestores:</strong> Ven solo productos visibles (no ocultos)</li>
 *   <li><strong>Otros usuarios (Clientes):</strong> Ven todos los productos disponibles</li>
 * </ul>
 * 
 * @author Lidia Martin
 * @version 1.3
 */
public abstract class User extends BaseElement implements java.io.Serializable{

	/**
	 * Obtiene una vista del catálogo con visibilidad controlada según el rol del usuario.
	 * 
	 * Este método implementa un sistema de filtrado basado en roles:
	 * <ul>
	 *   <li><strong>Para Empleados y Gestores:</strong> Retorna todos los productos.</li>
	 *   <li><strong>Para otros usuarios (Clientes):</strong> Retorna solo los productos
	 *       visibles (no ocultos).</li>
	 *
	 * @param currentUser El usuario ({@link User}) que solicita ver el catálogo.
	 * @param c           El catálogo ({@link Catalog}) del cual obtener los productos.
	 * 
	 * @return Una lista de productos ({@link NewProduct}) según la visibilidad permitida:
	 *         <ul>
	 *           <li>Si es {@link Employee} o {@link Manager}: todos los productos</li>
	 *           <li>Si es otro tipo de usuario: solo los productos visibles del catálogo</li>
	 *         </ul>
	 *         La lista puede estar vacía si no hay productos que coincidan con los criterios.
	 */
	public List<NewProduct> view_catalog(User currentUser, Catalog c) {
		if(currentUser instanceof Employee || currentUser instanceof Manager) {
			return c.allProducts();
		}
		return c.visibleProducts();
	}
	
	/**
	 * Busca productos en el catálogo por nombre.
	 * 
	 * Realiza una búsqueda textual en el catálogo que retorna todos los productos
	 * cuyo nombre contengan el término de búsqueda (la búsqueda no es sensible a mayúsculas/minúsculas).
	 * 
	 * @param s El término de búsqueda (cadena de texto). Ejemplo: "one piece", "catan".
	 * @param c El catálogo ({@link Catalog}) en el cual buscar.
	 * 
	 * @return Una lista de productos ({@link NewProduct}) que coinciden con el término de búsqueda.
	 *         Si no hay coincidencias, retorna una lista vacía.
	 */
	public List<NewProduct> search_catalog(String s, Catalog c) {
		return c.searchProducts(s);
	}

	/**
	 * Filtra los juegos del catálogo por rango de edad recomendado.
	 * 
	 * Retorna todos los juegos cuyo rango de edad recomendado se encuentra completamente
	 * dentro del rango especificado.
	 * 
	 * @param min La edad mínima del rango de filtrado (inclusive).
	 *            Ejemplo: 4 (para incluir juegos a partir de 4 años).
	 * @param max La edad máxima del rango de filtrado (inclusive).
	 *            Ejemplo: 10 (para incluir juegos hasta 10 años).
	 * @param c   El catálogo ({@link Catalog}) del cual obtener los juegos.
	 * 
	 * @return Una lista de juegos ({@link Game}) cuyo rango de edad recomendado
	 *         se encuentra dentro del rango especificado [min, max].
	 *         Si no hay juegos que coincidan, retorna una lista vacía.
	 */	
	public List<Game> filter_games_by_age(int min, int max, Catalog c) {
		return c.filterByAge(min, max);
	}
} 