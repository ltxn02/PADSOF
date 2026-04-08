package users;
import utils.*;
import java.util.*;

/**
 * Clase abstracta que representa a un usuario registrado en el sistema.
 * 
 * {@code RegisteredUser} es la clase base para todos los usuarios autenticados en la plataforma.
 * Define los atributos comunes de identidad, autenticación y notificaciones que comparten
 * clientes y personal administrativo (gestores y empleados). Hereda de {@link User} e implementa
 * funcionalidades esenciales como:
 * <ul>
 *   <li>Autenticación mediante usuario y contraseña</li>
 *   <li>Gestión de datos personales con validación mediante expresiones regulares</li>
 *   <li>Sistema de notificaciones personalizado</li>
 *   <li>Generación de perfiles y previsualizaciones de usuario</li>
 *   <li>Enmascaramiento de información sensible para privacidad</li>
 * </ul>
 * 
 * <h3>Jerarquía de herencia:</h3>
 * <pre>
 *        User (clase base)
 *          ↑
 *          |
 *    RegisteredUser (clase abstracta)
 *          ↑
 *     ┌────┴────┐
 *     |         |
 *   Staff    Client
 *     ↑
 *  ┌──┴──���
 *  |     |
 * Manager Employee
 * </pre>
 * 
 * @author da, Lidia Martin
 * @version 2.2
 */
public abstract class RegisteredUser extends User implements java.io.Serializable{
	private static int lastUserId = 0;
	private int userId;
	private String username;
	private String password;
	private String fullname;
	private String dni;
	private String birthdate;
	private String email;
	private String phoneNumber;
	private List<Notification> myNotifications;
	
	/**
	 * Constructor para crear un nuevo usuario registrado en el sistema.
	 * 
	 * Realiza validaciones exhaustivas de todos los parámetros mediante expresiones regulares
	 * antes de asignarlos. Si alguna validación falla, lanza una {@code IllegalArgumentException}
	 * con un mensaje descriptivo del problema.
	 * 
	 * @param username       Nombre de usuario único para el inicio de sesión (ej: "empleado").
     * @param password       Contraseña cifrada o en texto plano según la política de seguridad.
     * @param fullname       Nombre completo del empleado (ej: "Empleado de Prueba").
     * @param dni            Documento Nacional de Identidad del empleado (ej: "87654321B").
     * @param birthdate      Fecha de nacimiento en formato DD/MM/YYYY (ej: "15/05/1995").
     * @param email          Correo electrónico corporativo del empleado (ej: "empleado@rongero.es").
     * @param phoneNumber    Número de teléfono de contacto (ej: "600000000").
	 * 
	 * @throws IllegalArgumentException Si el DNI no tiene el formato correcto.
	 * @throws IllegalArgumentException Si el teléfono no tiene exactamente 9 dígitos.
	 * @throws IllegalArgumentException Si el email no es válido.
	 * @throws IllegalArgumentException Si la fecha de nacimiento no está en formato DD/MM/YYYY.
	 * @throws IllegalArgumentException Si el usuario o contraseña están vacíos.
	 * @throws IllegalArgumentException Si algún parámetro requerido es nulo.
	 */
	public RegisteredUser(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber) {
		// --- VALIDACIONES DE SEGURIDAD MEDIANTE REGEX ---
		// DNI: Exactamente 8 números seguidos de una letra (mayúscula o minúscula)
		if (dni == null || !dni.matches("^[0-9]{8}[A-Za-z]$")) {
			throw new IllegalArgumentException("DNI inválido. Debe contener 8 dígitos y una letra.");
		}

		// Teléfono: Exactamente 9 números
		if (phoneNumber == null || !phoneNumber.matches("^[0-9]{9}$")) {
			throw new IllegalArgumentException("Teléfono inválido. Debe contener exactamente 9 dígitos numéricos.");
		}

		// Email: Texto + @ + Texto + . + Texto
		if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
			throw new IllegalArgumentException("Email inválido. Revisa el formato (ejemplo@dominio.com).");
		}

		// Fecha de nacimiento: Formato DD/MM/YYYY
		if (birthdate == null || !birthdate.matches("^([0-2][0-9]|3[0-1])/(0[1-9]|1[0-2])/[0-9]{4}$")) {
			throw new IllegalArgumentException("Fecha de nacimiento inválida. Usa el formato DD/MM/YYYY.");
		}

		// Username y password: Al menos no estar vacíos ni ser espacios en blanco
		if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("El usuario y la contraseña no pueden estar vacíos.");
		}

		// --- FIN DE VALIDACIONES ---

		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.dni = dni.toUpperCase(); // Guardamos la letra del DNI en mayúscula por convención
		this.birthdate = birthdate;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.myNotifications = new ArrayList<>();
		this.userId = RegisteredUser.lastUserId;
		RegisteredUser.lastUserId++;
	}
	
	/**
	 * Autentica al usuario verificando sus credenciales (usuario y contraseña).
	 * 
	 * Este método compara el nombre de usuario y contraseña proporcionados contra
	 * los almacenados en el sistema. Se ejecuta una validación sensible a mayúsculas/minúsculas.
	 *
	 * @param username El nombre de usuario a verificar.
	 * @param password La contraseña a verificar.
	 * 
	 * @return {@code true} si ambas credenciales coinciden exactamente, {@code false} en caso contrario.
	 *         Si una o ambas credenciales son incorrectas, devuelve false sin especificar cuál falló.
	 */
	public boolean login(String username, String password) {
		return username.equals(this.username) && password.equals(this.password);
	}
	
	/**
	 * Obtiene el nombre de usuario del usuario registrado.
	 *
	 * @return El nombre de usuario único utilizado para autenticación.
	 *         Ejemplo: "juanperez", "lidia", "empleado".
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Obtiene una vista de las notificaciones en la bandeja de entrada del usuario.
	 * 
	 * Retorna un resumen de todas las notificaciones, incluyendo el número total
	 * de notificaciones nuevas (no leídas) y una previsualización de cada una.
	 * Las notificaciones nuevas se destacan en el contador inicial.
	 *
	 * @return Una cadena formateada con:
	 *         <ul>
	 *           <li>Número de notificaciones nuevas (no leídas)</li>
	 *           <li>Lista de todas las notificaciones con sus previsualizaciones</li>
	 *           <li>Tabulación para mejor legibilidad</li>
	 *         </ul>
	 */
	public String view_notifications() {
		String res = "My inbox (" + countNewNotifications() + " new):\n";
		for(Notification n: this.myNotifications) {
			res += "  " + n.notificationPreview() + "\n";
		}
		return res;
	}
	
	/**
	 * Lee una notificación específica y la marca como leída.
	 * 
	 * Cuando el usuario visualiza una notificación en detalle, este método
	 * retorna el contenido completo de la notificación y la marca automáticamente
	 * como leída en el sistema.
	 *
	 * @param notification La notificación ({@link Notification}) a leer.
	 * 
	 * @return El contenido completo de la notificación en formato de cadena.
	 */
	public String read_notification(Notification notification) {
		String res = "" + notification;
		notification.markAsRead(true);
		return res;
	}
	
	/**
	 * Cambia la visibilidad (archivado/no archivado) de una notificación.
	 * 
	 * Una notificación invisible se considera archivada pero no se elimina del sistema.
	 * El usuario puede recuperarla más tarde si es necesario.
	 *
	 * @param notification La notificación ({@link Notification}) a modificar.
	 * @param visible      {@code true} para que sea visible en la bandeja,
	 *                     {@code false} para archivarla.
	 */
	public void change_visibility(Notification notification, boolean visible) {
		notification.markAs(visible);
	}
	
	/**
	 * Obtiene una representación breve del usuario para mostrar en listas.
	 * Genera una vista previa compacta del usuario con su nombre completo y nombre de usuario.
	 *
	 * @return Una cadena corta con el formato "NombreCompleto (@nombreusuario)".
	 *         Ejemplo: "Juan Pérez García (@juanperez)".
	 */
	public String userPreview() {
		return this.fullname + " (@" + this.username + ")";
	}
	
	/**
	 * Obtiene el perfil completo del usuario con información privada enmascarada.
	 * 
	 * Genera una visualización detallada de todos los datos del usuario. Por seguridad
	 * y privacidad, algunos campos sensibles son enmascarados:
	 * <ul>
	   <li><strong>Email:</strong> Solo muestra la primera letra y el dominio (ej: j***@gmail.com)</li>
	 *   <li><strong>DNI:</strong> Solo muestra los últimos 4 caracteres (ej: ****78A)</li>
	 * </ul>
	 *
	 * @return Una cadena formateada con:
	 *         <ul>
	 *           <li>Nombre completo</li>
	 *           <li>Nombre de usuario</li>
	 *           <li>Email (enmascarado)</li>
	 *           <li>Teléfono</li>
	 *           <li>Fecha de nacimiento</li>
	 *           <li>DNI (enmascarado)</li>
	 *         </ul>
	 *         Cada línea está separada por salto de línea.
	 */
	public String userProfile() {
		StringBuilder res = new StringBuilder();

		res.append("Nombre: " + this.fullname + "\n");
		res.append("Usuario: " + this.username + "\n");
		res.append("Email: " + maskEmail(this.email) + "\n");
		res.append("Teléfono: " + this.phoneNumber + "\n");
		res.append("Fecha de nacimiento: " + this.birthdate + "\n");
		res.append("DNI: " + maskDni(this.dni) + "\n");

		return res.toString();
	}
	
	/**
	 * Obtiene la lista de todas las notificaciones del usuario (visibles y no visibles).
	 *
	 * @return Una lista de {@link Notification} con todas las notificaciones del usuario.
	 *         La lista puede estar vacía si el usuario no tiene notificaciones.
	 */
	public List<Notification> getMyNotifications() {
		return this.myNotifications;
	}

	/**
	 * Añade una nueva notificación a la bandeja de entrada del usuario.
	 *
	 * @param n La notificación ({@link Notification}) a añadir.
	 */
	public void addNotification(Notification n) {
		this.myNotifications.add(n);
	}

    
    
    
	// ═══════════════════════════════════════════════════════════
    // HELPERS / MÉTODOS AUXILIARES PRIVADOS
    // ═══════════════════════════════════════════════════════════

	
	/**
	 * Cuenta el número de notificaciones no leídas del usuario.
	 * 
	 * @return El número de notificaciones con estado "no leída" (isRead() == false).
	 */
	private int countNewNotifications() {
		int i = 0;
		for(Notification n: this.myNotifications) {
			if(n.isRead() == false) {
				i++;
			}
		}
		return i;
	}
	
	/**
	 * Enmascara un email por privacidad para visualización.
	 * 
	 * Reemplaza todos los caracteres entre la primera letra y el símbolo @ con asteriscos.
	 * Esto permite que el usuario identifique su email sin revelar información completa
	 * a terceros que vean su perfil.
	 * 
	 * Ejemplo: "juan.perez@gmail.com" → "j***@gmail.com"
	 *
	 * @param email El email a enmascarar.
	 * 
	 * @return El email enmascarado, o el email original si no tiene formato válido.
	 *         Si el email es nulo o muy corto, se devuelve sin cambios.
	 */
	private String maskEmail(String email) {
		if(email == null || !email.contains("@") || email.indexOf("@") <= 1) {
			return email;
		}

		int i, atIndex = email.indexOf("@");
		String aux = "";

		for(i = 1; i < atIndex; i++) {
			aux += "*";
		}

		return email.charAt(0) + aux + email.substring(atIndex);
	}
	
	/**
	 * Enmascara un DNI por privacidad para visualización.
	 * 
	 * Reemplaza los primeros 4 caracteres del DNI con asteriscos, mostrando solo los últimos 4
	 * (los dígitos más diferenciadores y la letra final). Esto permite al usuario identificar
	 * su DNI sin exponerlo completamente a terceros.
	 * 
	 * Ejemplo: "12345678A" → "****5678A"
	 *
	 * @param dni El DNI a enmascarar.
	 * 
	 * @return El DNI enmascarado, o el DNI original si es muy corto.
	 *         Si el DNI es nulo, se devuelve sin cambios.
	 */
	private String maskDni(String dni) {
	    if(dni == null || dni.length() < 5) {
	        return dni;
	    }

	    return "****" + dni.substring(dni.length() - 5);  // ✅ Toma últimos 5 (5678A)
	}
}