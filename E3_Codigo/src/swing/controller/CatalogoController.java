package swing.controller;

import logic.Application;
import logic.SistemaRecomendaciones;
import catalog.NewProduct;
import users.Client;
import users.RegisteredUser;

import java.util.ArrayList;
import java.util.Collections;

public class CatalogoController {

    /**
     * Devuelve una sublista del catálogo para la pantalla de "Inicio".
     * En este caso, simulamos tendencias mezclando el catálogo y tomando los primeros 6.
     */
    public static ArrayList<NewProduct> obtenerProductosMasVendidos() {
        ArrayList<NewProduct> todos = Application.getCatalog();

        if (todos == null || todos.isEmpty()) {
            return new ArrayList<>();
        }

        // Creamos una copia para no alterar el orden original del catálogo en la App
        ArrayList<NewProduct> copiaMezclada = new ArrayList<>(todos);

        // Mezclamos la lista para que la sección de "Inicio" parezca dinámica
        Collections.shuffle(copiaMezclada);

        // Devolvemos un máximo de 6 productos para no saturar la vista inicial
        int limite = Math.min(copiaMezclada.size(), 6);
        return new ArrayList<>(copiaMezclada.subList(0, limite));
    }

    /**
     * Decide qué productos mostrar en la sección de "Productos" basándose en el rol del usuario.
     * Si es un cliente registrado, aplica el Sistema de Recomendaciones.
     */
    public static ArrayList<NewProduct> obtenerProductosParaInicio(RegisteredUser usuario) {
        ArrayList<NewProduct> catalogoCompleto = Application.getCatalog();

        if (catalogoCompleto == null) {
            return new ArrayList<>();
        }

        // REGLA: Solo si es CLIENTE se activa el recomendador
        if (usuario instanceof Client) {
            // Filtramos la lista global de usuarios para obtener solo los Clientes
            ArrayList<Client> listaClientes = new ArrayList<>();
            ArrayList<RegisteredUser> todosLosUsuarios = Application.getUsers();

            if (todosLosUsuarios != null) {
                for (RegisteredUser u : todosLosUsuarios) {
                    if (u instanceof Client) {
                        listaClientes.add((Client) u);
                    }
                }
            }

            // Retorna la lista personalizada mediante el algoritmo de recomendaciones
            return SistemaRecomendaciones.obtenerRecomendaciones((Client) usuario, catalogoCompleto, listaClientes);
        }

        // Si no está logueado o es empleado/gestor, mostramos el catálogo completo
        return catalogoCompleto;
    }
}