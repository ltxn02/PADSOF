package swing2.controller.gestor;

import java.util.ArrayList;
import logic.Application;
import swing2.view.VentanaPrincipa;
import swing2.view.gestor.clientes.PanelGestionClientes;
import users.Client;

public class GestorClienteController {
    private VentanaPrincipa ventana;
    private PanelGestionClientes panel;

    public GestorClienteController(VentanaPrincipa ventana, PanelGestionClientes panel) {
        this.ventana = ventana;
        this.panel = panel;
    }
	
	/**
	 * Obtiene todos los clientes del sistema.
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
	 * Busca clientes filtrando por usuario, nombre, email o teléfono.
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