package swing2.view.gestor.descuentos;

import javax.swing.*;

import discounts.IDiscount;

import java.awt.*;

import swing2.controller.gestor.GestorDescuentoController;
import swing2.view.VentanaPrincipa;
import users.Staff;

/**
 * Panel contenedor para gestión de descuentos.
 * Maneja la navegación entre listado, selección de tipo y formulario de creación.
 */
public class PanelGestionDescuentos extends JPanel {
	private VentanaPrincipa ventanaPadre;
	private GestorDescuentoController ctrl;
	
	// === LAYOUT INTERNO (CardLayout) ===
	private CardLayout layoutInterno;
	private JPanel contenedorInterno;
	
	// === PANELES ===
	private PanelListDescuentos panelListado;
	private PanelGestorSeleccionTipoDescuento panelSeleccionTipo;
	private PanelAnadirDescuento panelAnadirNuevo;
	private PanelDetallesDescuento panelDetalles;
	
	// === COLORES ===
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	
	public PanelGestionDescuentos(VentanaPrincipa ventanaPadre) {
		this.ventanaPadre = ventanaPadre;
		this.ctrl = new GestorDescuentoController(ventanaPadre, null);
		
		// ============================================================
		// USAR CardLayout INTERNO para cambiar entre paneles
		// ============================================================
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
		// El panel de añadir se agregará dinámicamente cuando se seleccione un tipo
		
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
		panelListado.limpiarBusqueda();
	}
	
	/**
	 * Mostrar el panel de selección de tipo de descuento
	 */
	public void mostrarSeleccionTipo() {
		layoutInterno.show(contenedorInterno, "SELECCION_TIPO");
	}
	
	/**
	 * Mostrar panel de añadir descuento con el tipo ya seleccionado
	 */
	public void mostrarAnadirDescuentoConTipo(int tipoDescuento) {
		// Crear un nuevo panel con el tipo seleccionado
		panelAnadirNuevo = new PanelAnadirDescuento(this, tipoDescuento);
		
		// Remover el anterior si existe
		contenedorInterno.remove(panelListado);
		
		// Agregar el nuevo
		contenedorInterno.add(panelAnadirNuevo, "ANADIR_NUEVO");
		
		// Mostrar
		layoutInterno.show(contenedorInterno, "ANADIR_NUEVO");
	}
	
	/**
	 * Mostrar detalles de un descuento
	 */
	public void mostrarDetalles(Object descuento) {
		IDiscount discount = (IDiscount)descuento;
		panelDetalles.mostrarDetalles(discount);
		layoutInterno.show(contenedorInterno, "DETALLES");
	}
}
