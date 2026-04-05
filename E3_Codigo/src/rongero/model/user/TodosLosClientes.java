package model.user;

import logic.Application;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase de utilidad para la gestión y filtrado de la base de datos de usuarios.
 * Proporciona métodos estáticos para extraer y agrupar a todos los usuarios
 * con el rol de cliente, facilitando operaciones globales como el sistema
 * de recomendaciones.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class TodosLosClientes {

    /**
     * Recupera la lista completa de usuarios registrados en la aplicación y
     * filtra aquellos que son instancias de la clase Client.
     * * @return ArrayList de Client con todos los clientes activos en el sistema.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static ArrayList<Client> todoslosClientes() {
        List<RegisteredUser> usuarios = Application.getInstance().getUsers();
        ArrayList<Client> clientes = new ArrayList<>();

        for (RegisteredUser u : usuarios) {
            if (u instanceof Client) {
                clientes.add((Client) u);
            }
        }

        return clientes;
    }
}