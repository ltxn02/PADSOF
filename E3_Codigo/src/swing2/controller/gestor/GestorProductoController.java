package swing2.controller.gestor;

import swing2.view.VentanaPrincipa;
import swing2.view.gestor.productos.PanelGestionProductos;

/**
 * Controlador para la gestión de productos.
 */
public class GestorProductoController {
	private VentanaPrincipa ventanaPadre;
	private PanelGestionProductos panelProductos;

	public GestorProductoController(VentanaPrincipa ventanaPadre, PanelGestionProductos panelProductos) {
		this.ventanaPadre = ventanaPadre;
		this.panelProductos = panelProductos;
	}

	public VentanaPrincipa getVentanaPadre() {
		return ventanaPadre;
	}

	public PanelGestionProductos getPanelProductos() {
		return panelProductos;
	}

	// Métodos para lógica de negocio (completar según sea necesario)
	
	public void agregarProducto(String nombre, double precio, int stock) {
		// TODO: Implementar lógica de agregar producto
	}

	public void actualizarProducto(int id, String nombre, double precio, int stock) {
		// TODO: Implementar lógica de actualizar producto
	}

	public void eliminarProducto(int id) {
		// TODO: Implementar lógica de eliminar producto
	}
}
