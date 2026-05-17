package swing2.view.gestor.productos;

import javax.swing.*;
import java.awt.*;

import swing2.controller.gestor.GestorProductoController;
import swing2.view.VentanaPrincipa;

/**
 * Panel contenedor para gestión de productos.
 * Maneja la navegación entre listado y formulario.
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
	 * Mostrar el panel del listado de productos
	 */
	public void mostrarListado() {
		layoutInterno.show(contenedorInterno, "LISTADO");
		panelListado.refrescar();
	}
	
	/**
	 * Mostrar el panel para añadir producto
	 */
	public void mostrarAnadirProducto() {
		layoutInterno.show(contenedorInterno, "AÑADIR");
	}
	
	public GestorProductoController getController() {
		return ctrl;
	}
}
