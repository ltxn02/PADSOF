package format;

import java.util.List;

public class MenuOptions {
	static final List<String> GUEST = List.of(
			"Catálogo de productos",
			"Login",
			"Registrarse",
			"Salir de la aplicación"
	);
	
	static final List<String> CLIENT = List.of(
			"Catálogo de productos y comprar",
			"Mi carrito de la compra",
			"Productos de segunda mano",
			"Mi cartera de segunda mano",
			"Recomendaciones",
			"Notificaciones",
			"Perfil",
			"Cerrar sesión"
	);
	
	static final List<String> EMPLOYEE = List.of(
			"Gestión de productos (subida manual/masiva)",
			"Gestión de pedidos (actualizar estado)",
			"Valorar productos de segunda mano",
			"Confirmar intercambios",
			"Cerrar sesión"
	);
	
	static final List<String> MANAGER = List.of(
			"Empleados (dar alta/baja/permisos)",
			"Catálogo (categorías y packs)",
			"Descuentos",
			"Ver estadísticas",
			"Configuración",
			"Cerrar sesión"
	);
}
