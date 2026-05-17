package swing2.view.gestor.clientes;

import javax.swing.*;
import java.awt.*;
import swing2.controller.gestor.GestorClienteController;
import swing2.view.VentanaPrincipa;
import users.Client;

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
	 * Mostrar el panel del listado de empleados
	 */
	public void mostrarListado() {
		layoutInterno.show(contenedorInterno, "LISTADO");
		panelListado.limpiarBusqueda();  // Refrescar datos
	}
	
	/**
	 * Mostrar detalles de un empleado
	 */
	public void mostrarDetalles(Client cliente) {
		panelDetalles.mostrarDetalles(cliente);
		layoutInterno.show(contenedorInterno, "DETALLES");
	}
}