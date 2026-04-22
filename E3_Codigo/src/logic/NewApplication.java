package logic;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import transactions.Exchange;
import transactions.Order;
import products.catalog.Catalog;
import users.*;

public class Application {
	private static final Guest GUEST_USER = new Guest();
	
	private static User currentUser = Application.GUEST_USER;
	
	private static Map<String, RegisteredUser> users = new HashMap<>();
	private static Map<Integer, Exchange> exchanges = new HashMap<>();
	private static Map<Integer, Order> orders = new HashMap<>();
	private static Catalog newProductsCatalog = new Catalog();
	private static Catalog secondHandCatalog = new Catalog();
	
	// --- BLOQUE DE INICIALIZACIÓN ESTÁTICA ---
	static {
		// 1. INICIALIZACIÓN DE USUARIOS POR DEFECTO
		try {
			Manager lidia = new Manager("lidia", "lidia123", "Lidia Martin", "12345678A", "01/01/2002", "lidia@rongero.es", "600000000", 10000.00);
			Manager taha = new Manager("taha", "taha123", "Taha Ridda", "12345678A", "01/01/2002", "taha@rongero.es", "600000000", 10000.00);
			Manager ivan = new Manager("ivan", "ivan123", "Ivan Sanchez", "12345678A", "01/01/2002", "ivan@rongero.es", "600000000", 10000.00);
		
			Employee defaultEmployee = new Employee("empleado", "empleado123", "Empleado de Prueba", "87654321B", "15/05/1995", "empleado@rongero.es", "600000000", 1200.00, true);
			
			users.put(lidia.getUsername(), lidia);
			users.put(taha.getUsername(), taha);
			users.put(ivan.getUsername(), ivan);
			users.put(defaultEmployee.getUsername(), defaultEmployee);
			
			// System.out.println("[Sistema] Cuentas de gestor y empleado creadas por defecto.");
		} catch (Exception e) {
			System.err.println("[Error] No se pudieron crear usuarios por defecto" + e.getMessage());
		}
		
		// 2. INICIALIZACIÓN DE PRODUCTOS POR DEFECTO
		try {
			
		}
	}
}