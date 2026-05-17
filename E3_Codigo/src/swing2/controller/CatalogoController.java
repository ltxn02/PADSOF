package swing2.controller;

import catalog.*;
import logic.Application;
import logic.SistemaRecomendaciones;
import catalog.NewProduct;
import users.Client;
import users.RegisteredUser;
import utils.ItemType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

        
        ArrayList<NewProduct> copiaMezclada = new ArrayList<>(todos);

        
        Collections.shuffle(copiaMezclada);

        
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

        
        if (usuario instanceof Client) {
            
            ArrayList<Client> listaClientes = new ArrayList<>();
            ArrayList<RegisteredUser> todosLosUsuarios = Application.getUsers();

            if (todosLosUsuarios != null) {
                for (RegisteredUser u : todosLosUsuarios) {
                    if (u instanceof Client) {
                        listaClientes.add((Client) u);
                    }
                }
            }

            
            return SistemaRecomendaciones.obtenerRecomendaciones((Client) usuario, catalogoCompleto, listaClientes);
        }

        
        return catalogoCompleto;
    }public static ArrayList<NewProduct> filtrarProductos(ArrayList<NewProduct> listaBase, ItemType tipo, double precioMax) {
        ArrayList<NewProduct> filtrados = new ArrayList<>();

        for (NewProduct p : listaBase) {
            boolean cumpleTipo = (tipo == null);
            if (tipo != null) {
                if (tipo == ItemType.GAME && p instanceof Game) cumpleTipo = true;
                else if (tipo == ItemType.COMIC && p instanceof Comic) cumpleTipo = true;
                else if (tipo == ItemType.FIGURINE && p instanceof Figurine) cumpleTipo = true;
            }

            boolean cumplePrecio = p.getPrice() <= precioMax;

            if (cumpleTipo && cumplePrecio) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    /**
     * Ordena la lista de productos (modifica la lista original).
     */
    public static void ordenarProductos(ArrayList<NewProduct> lista, String criterio) {
        if (lista == null || lista.isEmpty()) return;

        switch (criterio) {
            case "Precio (Menor a Mayor)":
                lista.sort(Comparator.comparingDouble(NewProduct::getPrice));
                break;
            case "Precio (Mayor a Menor)":
                lista.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                break;
            case "Nombre (A-Z)":
                lista.sort(Comparator.comparing(p -> p.getName().toLowerCase()));
                break;
            case "Fecha de adición":
                
                Collections.reverse(lista);
                break;
        }
    }
}