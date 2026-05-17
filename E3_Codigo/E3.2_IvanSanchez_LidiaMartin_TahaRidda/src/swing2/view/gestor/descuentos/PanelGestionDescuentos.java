package swing2.view.gestor.descuentos;

import javax.swing.*;

import discounts.IDiscount;

import java.awt.*;
import java.util.HashMap;

import swing2.controller.gestor.GestorDescuentoController;
import swing2.view.VentanaPrincipa;

/**
 * Panel contenedor para gestión de descuentos.
 * Maneja la navegación entre listado, selección de tipo y formulario de creación.
 */
public class PanelGestionDescuentos extends JPanel {
	private VentanaPrincipa ventanaPadre;
	private GestorDescuentoController ctrl;
	
	// LAYOUT INTERNO (CardLayout)
	private CardLayout layoutInterno;
	private JPanel contenedorInterno;
	
	// PANELES
	private PanelListDescuentos panelListado;
	private PanelGestorSeleccionTipoDescuento panelSeleccionTipo;
	private PanelAnadirDescuento panelAnadirNuevo;
	private PanelDetallesDescuento panelDetalles;

	private HashMap<Integer, PanelAnadirDescuento> panelesPorTipo = new HashMap<>();
	
	// COLORES
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	
	public PanelGestionDescuentos(VentanaPrincipa ventanaPadre) {
		this.ventanaPadre = ventanaPadre;
		this.ctrl = new GestorDescuentoController(ventanaPadre, this);

		// USAR CardLayout INTERNO para cambiar entre paneles
		layoutInterno = new CardLayout();
		contenedorInterno = new JPanel(layoutInterno);
		
		// Crear los paneles
		panelListado = new PanelListDescuentos(this, ctrl);
		panelSeleccionTipo = new PanelGestorSeleccionTipoDescuento(this);
		panelDetalles = new PanelDetallesDescuento(this, ctrl);
		
		// Agregar paneles al contenedor interno
		contenedorInterno.add(panelListado, "LISTADO");
		contenedorInterno.add(panelSeleccionTipo, "SELECCION_TIPO");
		contenedorInterno.add(panelDetalles, "DETALLES");
		
		// El panel principal es el contenedor interno
		this.setLayout(new BorderLayout());
		this.add(contenedorInterno, BorderLayout.CENTER);
		
		// Mostrar el listado por defecto
		mostrarListado();
	}
	
	/**
	 * Mostrar el panel del listado de descuentos
	 */
	public void mostrarListado() {
		layoutInterno.show(contenedorInterno, "LISTADO");
		panelListado.cargarDescuentos();
	}
	
	/**
	 * Mostrar el panel de selección de tipo de descuento
	 */
	public void mostrarSeleccionTipo() {
		layoutInterno.show(contenedorInterno, "SELECCION_TIPO");
	}
	
	/**
	 * Mostrar panel de añadir descuento con el tipo ya seleccionado
	 * @param tipoDescuento 0=Rebaja%, 1=Volumen, 2=Regalo, 3=Cantidad
	 */
		public void mostrarAnadirDescuentoConTipo(int tipoDescuento) {
	    // Verificar si ya existe un panel para este tipo
	    if (!panelesPorTipo.containsKey(tipoDescuento)) {
	        // Crear nuevo panel para este tipo
	        panelAnadirNuevo = new PanelAnadirDescuento(this, ctrl, tipoDescuento);
	        panelesPorTipo.put(tipoDescuento, panelAnadirNuevo);
	        
	        // Agregar al contenedor con una clave única
	        String clave = "ANADIR_" + tipoDescuento;
	        contenedorInterno.add(panelAnadirNuevo, clave);
	    }
	    
	    // Mostrar el panel del tipo seleccionado
	    String clave = "ANADIR_" + tipoDescuento;
	    layoutInterno.show(contenedorInterno, clave);
	}
	
	/**
	 * Mostrar detalles de un descuento
	 */
	public void mostrarDetalles(IDiscount descuento) {
		panelDetalles.mostrarDetalles(descuento);
		layoutInterno.show(contenedorInterno, "DETALLES");
	}
}
