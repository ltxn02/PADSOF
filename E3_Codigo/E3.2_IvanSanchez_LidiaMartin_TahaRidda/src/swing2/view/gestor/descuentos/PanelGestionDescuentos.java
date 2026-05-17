package swing2.view.gestor.descuentos;

import javax.swing.*;

import discounts.IDiscount;

import java.awt.*;
import java.util.HashMap;

import swing2.controller.gestor.GestorDescuentoController;
import swing2.view.VentanaPrincipa;

/**
 * Panel contenedor principal para la gestión integral de descuentos en la interfaz del Gestor.
 * Implementa un patrón de navegación interno mediante {@link CardLayout} para alternar
 * fluidamente entre el listado general de descuentos, la pantalla de selección de tipo,
 * los formularios de creación dinámica y la vista de detalles.
 * * @author Lidia Martin
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

	/**
	 * Constructor del panel de gestión de descuentos.
	 * Inicializa el controlador asociado, los paneles secundarios (listado, selección
	 * y detalles) y configura el {@link CardLayout} interno para arrancar mostrando el listado.
	 *
	 * @param ventanaPadre La ventana principal de la aplicación.
	 */
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
	 * Cambia la vista activa del CardLayout para mostrar el panel con el listado
	 * general de descuentos y fuerza la recarga de los datos desde el controlador.
	 */
	public void mostrarListado() {
		layoutInterno.show(contenedorInterno, "LISTADO");
		panelListado.cargarDescuentos();
	}

	/**
	 * Cambia la vista activa del CardLayout para mostrar el menú de selección,
	 * donde el gestor elige qué tipo de descuento desea crear a continuación.
	 */
	public void mostrarSeleccionTipo() {
		layoutInterno.show(contenedorInterno, "SELECCION_TIPO");
	}

	/**
	 * Instancia (o recupera de la caché) y muestra el panel del formulario
	 * de creación correspondiente al tipo de descuento seleccionado.
	 * Utiliza un {@link HashMap} para no recrear paneles que ya han sido instanciados previamente.
	 *
	 * @param tipoDescuento Identificador numérico del tipo de descuento:
	 * 0 = Rebaja porcentual (%), 1 = Volumen, 2 = Regalo, 3 = Cantidad.
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
	 * Cambia la vista activa para mostrar los detalles completos de un descuento específico.
	 * Pasa el objeto al panel de detalles para que se actualice la información mostrada.
	 *
	 * @param descuento El objeto {@link IDiscount} cuyos detalles se desean inspeccionar.
	 */
	public void mostrarDetalles(IDiscount descuento) {
		panelDetalles.mostrarDetalles(descuento);
		layoutInterno.show(contenedorInterno, "DETALLES");
	}
}