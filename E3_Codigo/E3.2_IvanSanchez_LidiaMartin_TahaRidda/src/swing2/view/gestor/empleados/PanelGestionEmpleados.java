package swing2.view.gestor.empleados;

import javax.swing.*;
import java.awt.*;

import swing2.controller.gestor.GestorEmpleadoController;
import swing2.view.VentanaPrincipa;
import users.Staff;

/**
 * Panel contenedor para la gestión de empleados.
 * Administra de forma centralizada la navegación interna entre las pantallas
 * de listado, formulario de alta y vista de detalles mediante un CardLayout.
 * 
 * @author Lidia Martín
 */
public class PanelGestionEmpleados extends JPanel {
	private VentanaPrincipa ventanaPadre;
	private GestorEmpleadoController ctrl;
	
	// LAYOUT INTERNO (CardLayout)
	private CardLayout layoutInterno;
	private JPanel contenedorInterno;
	
	// PANELES
	private PanelListaEmpleados panelListado;
	private PanelAnadirEmpleado panelAñadir;
	private PanelDetallesEmpleado panelDetalles;
	
	// COLORES
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	
	/**
	 * Constructor de la clase PanelGestionEmpleados.
	 * Inicializa el controlador de empleados, instancia los subpaneles de la sección
	 * y los registra dentro del contenedor principal bajo una distribución de tarjetas.
	 * 
	 * @param ventanaPadre La ventana principal de la aplicación que actúa como marco superior.
	 */
	public PanelGestionEmpleados(VentanaPrincipa ventanaPadre) {
		this.ventanaPadre = ventanaPadre;
		this.ctrl = new GestorEmpleadoController(ventanaPadre, this);

		// USAR CardLayout INTERNO para cambiar entre listado y formulario
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
	 * Alterna la vista activa del contenedor hacia el panel de listado general 
	 * y restablece los filtros o búsquedas previas para refrescar los datos en pantalla.
	 */
	public void mostrarListado() {
		layoutInterno.show(contenedorInterno, "LISTADO");
		panelListado.limpiarBusqueda();  // Refrescar datos
	}
	
	/**
	 * Alterna la vista activa del contenedor hacia el formulario de inserción 
	 * para posibilitar el alta de un nuevo registro de empleado.
	 */
	public void mostrarAnadirEmpleado() {
		layoutInterno.show(contenedorInterno, "AÑADIR");
	}
	
	/**
	 * Carga la información de un miembro del personal específico en el subpanel 
	 * correspondiente y conmuta la interfaz para mostrar su ficha detallada.
	 * 
	 * @param empleado Instancia de Staff que contiene la información del empleado consultado.
	 */
	public void mostrarDetalles(Staff empleado) {
		panelDetalles.mostrarDetalles(empleado);
		layoutInterno.show(contenedorInterno, "DETALLES");
	}
}