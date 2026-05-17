package swing2.view.gestor.productos;

import javax.swing.*;
import java.awt.*;

import swing2.controller.gestor.GestorProductoController;
import swing2.view.VentanaPrincipa;

/**
 * Panel contenedor para gestión de productos.
 * Maneja la navegación entre listado y formulario.
 * 
 * @author Lidia Martin
 */
public class PanelGestionProductos extends JPanel {
	private VentanaPrincipa ventanaPadre;
	private GestorProductoController ctrl;
	
	// === LAYOUT INTERNO (CardLayout) ===
	private CardLayout layoutInterno;
	private JPanel contenedorInterno;
	
	// === PANELES ===
	private PanelListaProductos panelListado;
	private PanelAnadirProducto panelAñadir;
	
	// === COLORES ===
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	
	/**
	 * Constructor de la clase PanelGestionProductos.
	 * Inicializa el contenedor interno utilizando un CardLayout para gestionar 
	 * el intercambio entre la vista de listado y la vista de creación de productos.
	 * 
	 * @param ventanaPadre La ventana principal de la aplicación que actúa como marco contenedor.
	 */
	public PanelGestionProductos(VentanaPrincipa ventanaPadre) {
		this.ventanaPadre = ventanaPadre;
		this.ctrl = new GestorProductoController(ventanaPadre, this);
		
		// ============================================================
		// USAR CardLayout INTERNO para cambiar entre listado y formulario
		// ============================================================
		layoutInterno = new CardLayout();
		contenedorInterno = new JPanel(layoutInterno);
		contenedorInterno.setBackground(COLOR_FONDO);
		
		// Crear los paneles
		panelListado = new PanelListaProductos(this, ctrl);
		panelAñadir = new PanelAnadirProducto(ventanaPadre, this);
		
		// Agregar ambos paneles al contenedor interno
		contenedorInterno.add(panelListado, "LISTADO");
		contenedorInterno.add(panelAñadir, "AÑADIR");
		
		// El panel principal es el contenedor interno
		this.setLayout(new BorderLayout());
		this.setBackground(COLOR_FONDO);
		this.add(contenedorInterno, BorderLayout.CENTER);
		
		// Mostrar el listado por defecto
		mostrarListado();
	}
	
	/**
	 * Muestra el panel correspondiente al listado de productos
	 * y actualiza/refresca sus datos visuales en pantalla.
	 */
	public void mostrarListado() {
		layoutInterno.show(contenedorInterno, "LISTADO");
		panelListado.refrescar();
	}
	
	/**
	 * Muestra el panel correspondiente al formulario para añadir 
	 * un nuevo producto al sistema.
	 */
	public void mostrarAnadirProducto() {
		layoutInterno.show(contenedorInterno, "AÑADIR");
	}
	
	/**
	 * Recupera el controlador asignado a la gestión de los productos.
	 * 
	 * @return Objeto GestorProductoController que maneja la lógica de negocio de este panel.
	 */
	public GestorProductoController getController() {
		return ctrl;
	}
}
