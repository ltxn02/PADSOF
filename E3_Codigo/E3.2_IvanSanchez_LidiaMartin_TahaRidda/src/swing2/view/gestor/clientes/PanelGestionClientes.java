package swing2.view.gestor.clientes;

import javax.swing.*;
import java.awt.*;
import swing2.controller.gestor.GestorClienteController;
import swing2.view.VentanaPrincipa;
import users.Client;

/**
 * Panel contenedor principal para la gestión de clientes en la interfaz del Gestor.
 * Utiliza un patrón de navegación interno mediante {@link CardLayout} para alternar
 * de manera fluida entre la vista del listado general de clientes y la vista
 * específica con los detalles de un cliente seleccionado.
 * * @author Lidia Martin
 */
public class PanelGestionClientes extends JPanel {
	private VentanaPrincipa ventanaPadre;
	private GestorClienteController ctrl;

	// LAYOUT INTERNO (CardLayout)
	private CardLayout layoutInterno;
	private JPanel contenedorInterno;

	// PANELES
	private PanelListaClientes panelListado;
	private PanelDetallesCliente panelDetalles;

	// COLORES
	private static final Color COLOR_FONDO = new Color(23, 48, 79);

	/**
	 * Constructor del panel de gestión de clientes.
	 * Inicializa el controlador asociado, instancia los subpaneles (listado y detalles)
	 * y configura el contenedor interno para mostrar por defecto el listado general.
	 *
	 * @param ventanaPadre La ventana principal de la aplicación.
	 */
	public PanelGestionClientes(VentanaPrincipa ventanaPadre) {
		this.ventanaPadre = ventanaPadre;
		this.ctrl = new GestorClienteController(ventanaPadre, this);

		// USAR CardLayout INTERNO para cambiar entre paneles
		layoutInterno = new CardLayout();
		contenedorInterno = new JPanel(layoutInterno);

		// Crear los paneles
		panelListado = new PanelListaClientes(this, ctrl);
		panelDetalles = new PanelDetallesCliente(this, ctrl);

		// Agregar paneles al contenedor interno
		contenedorInterno.add(panelListado, "LISTADO");
		contenedorInterno.add(panelDetalles, "DETALLES");

		// El panel principal es el contenedor interno
		this.setLayout(new BorderLayout());
		this.add(contenedorInterno, BorderLayout.CENTER);

		// Mostrar el listado por defecto
		mostrarListado();
	}

	/**
	 * Alterna la vista activa del CardLayout interno para mostrar el panel
	 * con el listado de clientes, forzando además una limpieza de los filtros
	 * de búsqueda y un refresco visual de los datos en la tabla.
	 */
	public void mostrarListado() {
		layoutInterno.show(contenedorInterno, "LISTADO");
		panelListado.limpiarBusqueda();  // Refrescar datos
	}

	/**
	 * Alterna la vista activa del CardLayout interno para mostrar los detalles
	 * completos de un cliente específico.
	 *
	 * @param cliente El objeto {@link Client} cuyos detalles se desean inspeccionar.
	 */
	public void mostrarDetalles(Client cliente) {
		panelDetalles.mostrarDetalles(cliente);
		layoutInterno.show(contenedorInterno, "DETALLES");
	}
}