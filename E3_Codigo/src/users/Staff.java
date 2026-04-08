package users;

/**
 * Clase abstracta que representa al personal (Staff) de la tienda.
 * 
 * {@code Staff} es una clase abstracta que sirve como clase base para todos los empleados
 * con funciones administrativas en el sistema (Gestores y Empleados). Hereda de 
 * {@link RegisteredUser} y añade la gestión de información salarial, que es común
 * a todos los miembros del personal.
 * 
 * Esta clase no puede ser instanciada directamente; debe ser heredada por clases
 * concretas como {@link Manager} o {@link Employee}.
 * 
 * <h3>Jerarquía de herencia:</h3>
 * <pre>
 *     RegisteredUser (clase base)
 *            ↑
 *            |
 *         Staff (clase abstracta)
 *            ↑
 *     ┌──────┴──────┐
 *     |             |
 *  Manager      Employee
 * </pre>
 * 
 * <h3>Características principales:</h3>
 * <ul>
 *   <li>Hereda autenticación y gestión de usuario de {@link RegisteredUser}</li>
 *   <li>Añade información de salario/compensación</li>
 *   <li>Sirve como punto de partida para roles administrativos diferenciados</li>
 *   <li>Implementa {@code java.io.Serializable} para persistencia de datos</li>
 * </ul>
 * 
 * @author Lidia Martin
 * @version 1.5
 */
public abstract class Staff extends RegisteredUser implements java.io.Serializable{
    private double salary;
    
    /**
     * Constructor para crear una nueva cuenta de personal en el sistema.
     * 
     * Este constructor abstracto es llamado por las subclases concretas
     * ({@link Manager} y {@link Employee}) para inicializar los datos
     * de usuario básicos heredados de {@link RegisteredUser} más la información
     * salarial específica del personal.
     *
     * @param username       Nombre de usuario único para el inicio de sesión en el sistema.
     *                       Debe ser único en toda la aplicación (ej: "empleado").
     * @param password       Contraseña de autenticación del personal.
     * @param fullname       Nombre completo y legal del miembro del personal (ej: "Empleado de prueba").
     * @param dni            DNI del personal (ej: "12345678A", "87654321B").
     * @param birthdate      Fecha de nacimiento en formato DD/MM/YYYY (ej: "01/01/2002").
     * @param email          Dirección de correo electrónico (ej: "empleado@rongero.es").
     * @param phoneNumber    Número de teléfono de contacto del personal (ej: "600000000", "666123456").
     * @param salary         Salario bruto mensual del personal en euros (ej: 1200.00 para empleados).
     */
    public Staff(String username, String password, String fullname, String dni, String birthdate, String email, String phoneNumber, double salary) {
        super(username, password, fullname, dni, birthdate, email, phoneNumber);
        this.salary = salary;
    }
}
