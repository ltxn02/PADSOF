package swing2.view.gestor;

import javax.swing.*;
import java.awt.*;

import swing2.controller.gestor.GestorDescuentoController;
import swing2.view.VentanaPrincipa;
import users.Staff;

/**
 * Panel contenedor para gestión de empleados.
 * Maneja la navegación entre listado y formulario.
 */
public class PanelGestionDescuentos extends JPanel {
	private VentanaPrincipa ventanaPadre;
	private GestorDescuentoController ctrl;
	
	// === LAYOUT INTERNO (CardLayout) ===
	private CardLayout layoutInterno;
	private JPanel contenedorInterno;
	
	// === PANELES ===
	private PanelListaEmpleados panelListado;
	private PanelAnadirEmpleado panelAñadir;
	private PanelDetallesEmpleado panelDetalles;
	
	// === COLORES ===
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	
	public PanelGestionDescuentos(VentanaPrincipa ventanaPadre) {
		this.ventanaPadre = ventanaPadre;
		this.ctrl = new GestorDescuentoController(ventanaPadre, this);
		
		// ============================================================
		// USAR CardLayout INTERNO para cambiar entre listado y formulario
		// ============================================================
		layoutInterno = new CardLayout();
		contenedorInterno = new JPanel(layoutInterno);
		
		// Crear los paneles
		panelListado = new PanelListaEmpleados(this, ctrl);
		panelAñadir = new PanelAnadirEmpleado(ventanaPadre, this);
		panelDetalles = new PanelDetallesEmpleado(this, ctrl);
		
		// Agregar ambos paneles al contenedor interno
		contenedorInterno.add(panelListado, "LISTADO");
		contenedorInterno.add(panelAñadir, "AÑADIR");
		contenedorInterno.add(panelDetalles, "DETALLES");
		
		// El panel principal es el contenedor interno
		this.setLayout(new BorderLayout());
		this.add(contenedorInterno, BorderLayout.CENTER);
		
		// Mostrar el listado por defecto
		mostrarListado();
	}
	
	/**
	 * Mostrar el panel del listado de empleados
	 */
	public void mostrarListado() {
		layoutInterno.show(contenedorInterno, "LISTADO");
		panelListado.limpiarBusqueda();  // Refrescar datos
	}
	
	/**
	 * Mostrar el panel para añadir empleado
	 */
	public void mostrarAnadirEmpleado() {
		layoutInterno.show(contenedorInterno, "AÑADIR");
	}
	
	/**
	 * Mostrar detalles de un empleado
	 */
	public void mostrarDetalles(Staff empleado) {
		panelDetalles.mostrarDetalles(empleado);
		layoutInterno.show(contenedorInterno, "DETALLES");
	}
}