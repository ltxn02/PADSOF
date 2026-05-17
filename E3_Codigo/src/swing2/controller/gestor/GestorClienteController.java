package swing2.controller.gestor;

import java.util.ArrayList;
import logic.Application;
import swing2.view.VentanaPrincipa;
import swing2.view.gestor.clientes.PanelGestionClientes;
import users.Client;

/**
 * Controlador encargado de la administración, filtrado y consulta de las cuentas de clientes.
 * Conecta los componentes de la interfaz de gestión con el almacén global de usuarios 
 * de la capa de negocio siguiendo el patrón arquitectónico MVC.
 * 
 * @author Lidia Martín
 */
public class GestorClienteController {
    private VentanaPrincipa ventana;
    private PanelGestionClientes panel;

    /**
     * Constructor de la clase GestorClienteController.
     * Establece la comunicación bidireccional entre la ventana principal y la sección de administración de clientes.
     * 
     * @param ventana Ventana principal que actúa como marco de la interfaz gráfica.
     * @param panel   Panel específico destinado al listado e interacción con los clientes.
     */
    public GestorClienteController(VentanaPrincipa ventana, PanelGestionClientes panel) {
        this.ventana = ventana;
        this.panel = panel;
    }
	
    /**
     * Obtiene y discrimina todos los clientes registrados en el sistema.
     * Recorre la colección general de credenciales y extrae de forma exclusiva 
     * aquellas entidades que correspondan al rol específico de cliente.
     * 
     * @return Un ArrayList con los objetos de tipo Client que están dados de alta en el sistema.
     */
	public ArrayList<Client> obtenerClientes() {
		ArrayList<Client> clientes = new ArrayList<>();
		for (var user : Application.getUsers()) {
			if (user instanceof Client) {
				clientes.add((Client) user);
			}
		}
		return clientes;
	}
	
	/**
     * Busca y filtra un listado de clientes evaluando la coincidencia parcial de texto.
     * Compara un término único contra los atributos clave de cada perfil: alias de usuario, 
     * nombre completo, dirección de correo electrónico o número de contacto telefónico, 
     * normalizando las cadenas a minúsculas para ignorar la distinción de mayúsculas.
     * 
     * @param clientes Lista original sobre la que se va a aplicar la consulta o cribado.
     * @param termino  Cadena de caracteres que representa la palabra clave o patrón de búsqueda.
     * @return Un nuevo ArrayList reducido únicamente con las entidades que cumplen algún criterio.
     */
	public ArrayList<Client> buscarClientes(ArrayList<Client> clientes, String termino) {
		ArrayList<Client> resultado = new ArrayList<>();
		String terminoLower = termino.toLowerCase();
		
		for (Client cliente : clientes) {
			if (cliente.getUsername().toLowerCase().contains(terminoLower) ||
				cliente.getFullname().toLowerCase().contains(terminoLower) ||
				cliente.getEmail().toLowerCase().contains(terminoLower) ||
				cliente.getPhoneNumber().contains(termino)) {
				resultado.add(cliente);
			}
		}
		
		return resultado;
	}
}