package swing2.controller.gestor;

import swing2.view.VentanaPrincipa;
import swing2.view.gestor.productos.PanelGestionProductos;

/**
 * Controlador para la gestión de productos.
 * 
 * @author Lidia Martín
 */
public class GestorProductoController {
	private VentanaPrincipa ventanaPadre;
	private PanelGestionProductos panelProductos;

	/**
	 * Constructor de la clase GestorProductoController.
	 * Asigna la ventana principal y el panel de gestión de productos para coordinar
	 * las acciones entre la vista y el modelo.
	 * 
	 * @param ventanaPadre   La ventana principal de la aplicación.
	 * @param panelProductos El panel encargado de la vista de gestión de productos.
	 */
	public GestorProductoController(VentanaPrincipa ventanaPadre, PanelGestionProductos panelProductos) {
		this.ventanaPadre = ventanaPadre;
		this.panelProductos = panelProductos;
	}

	/**
	 * Obtiene la referencia a la ventana principal de la aplicación.
	 * 
	 * @return El objeto VentanaPrincipa que actúa como contenedor principal.
	 */
	public VentanaPrincipa getVentanaPadre() {
		return ventanaPadre;
	}

	/**
	 * Obtiene la referencia al panel de gestión de productos controlado por esta clase.
	 * 
	 * @return El objeto PanelGestionProductos asociado.
	 */
	public PanelGestionProductos getPanelProductos() {
		return panelProductos;
	}
}
