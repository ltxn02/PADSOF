package users;

/**
 * Clase que representa a un usuario invitado (Guest) del sistema.
 * 
 * Los invitados son usuarios no autenticados que pueden realizar acciones limitadas
 * en la plataforma sin necesidad de registrarse. Hereda de {@link User} y representa
 * el punto de entrada principal para nuevos usuarios que desean explorar la tienda
 * o crear una cuenta.
 * 
 * <h3>Funcionalidades de Guest:</h3>
 * <ul>
 *   <li>Visualización del catálogo de productos públicos (heredado de {@link User})</li>
 *   <li>Búsqueda de productos en el catálogo (heredado de {@link User})</li>
 *   <li>Filtrado de juegos por rango de edad (heredado de {@link User})</li>
 *   <li>Creación de nuevas cuentas de cliente ({@link Client})</li>
 * </ul>
 * 
 * <h3>Jerarquía de herencia:</h3>
 * <pre>
 *     BaseElement (clase base)
 *           ↑
 *           |
 *         User (clase abstracta)
 *           ↑
 *      ┌────┴────┐
 *      |         |
 *    Guest   RegisteredUser (abstracta)
 *               ↑
 *          ┌────┴────┐
 *          |         |
 *        Staff    Client
 *          ↑
 *       ┌──┴──┐
 *       |     |
 *    Manager Employee
 * </pre>
 * 
 * @author Lidia Martin
 * @version 1.2
 */
public class Guest extends User implements java.io.Serializable{
	
	/**
	 * Crea una nueva cuenta de cliente en el sistema a partir de los datos de un invitado.
	 * 
	 * Este método realiza la transición de un usuario invitado a un cliente registrado.
	 * Valida todos los datos según las reglas definidas en el constructor de {@link Client},
	 * que incluyen validación de DNI, teléfono, email y fecha de nacimiento mediante
	 * expresiones regulares.
	 *
	 * @param username       Nombre de usuario único para el inicio de sesión (ej: "empleado").
     * @param password       Contraseña cifrada o en texto plano según la política de seguridad.
     * @param fullname       Nombre completo del empleado (ej: "Empleado de Prueba").
     * @param dni            Documento Nacional de Identidad del empleado (ej: "87654321B").
     * @param birthdate      Fecha de nacimiento en formato DD/MM/YYYY (ej: "15/05/1995").
     * @param email          Correo electrónico corporativo del empleado (ej: "empleado@rongero.es").
     * @param phoneNumber    Número de teléfono de contacto (ej: "600000000").
	 * 
	 * @return Una nueva instancia de {@link Client} completamente inicializada.
	 * 
	 * @throws IllegalArgumentException Si alguno de los parámetros no cumple con las validaciones:
	 *                                  <ul>
	 *                                    <li>DNI con formato inválido</li>
	 *                                    <li>Teléfono que no sea exactamente 9 dígitos</li>
	 *                                    <li>Email sin formato válido</li>
	 *                                    <li>Fecha de nacimiento no en formato DD/MM/YYYY</li>
	 *                                    <li>Usuario o contraseña vacíos</li>
	 *                                  </ul>
	 * 
	 */
	public Client createClientAccount(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber) {
		Client account = new Client(username, password, fullname, dni, birthdate, email, phoneNumber);
		return account;
	}
}