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

/**
 * Controlador encargado de gestionar las operaciones asociadas al catálogo de productos.
 * Actúa como el nexo del patrón MVC para coordinar el filtrado, la ordenación, la simulación
 * de tendencias de mercado y el procesamiento del sistema inteligente de recomendaciones.
 * 
 * @author Taha Ridda
 */
public class CatalogoController {

	/**
     * Devuelve una sublista del catálogo para la pantalla de "Inicio".
     * En este caso, simulamos tendencias mezclando el catálogo y tomando los primeros 6.
     * 
     * @return Un ArrayList con hasta un máximo de 6 productos seleccionados de forma aleatoria.
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
     * 
     * @param usuario La instancia del usuario registrado que realiza la consulta en la interfaz.
     * @return Un ArrayList con el catálogo ordenado de forma personalizada o el catálogo base completo.
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
    }
    
    /**
     * Filtra una lista base de productos evaluando de forma combinada su tipo específico 
     * (Videojuegos, Cómics, Figuras) junto con un umbral de coste económico máximo.
     * 
     * @param listaBase Lista original de productos sobre la cual aplicar los criterios.
     * @param tipo      Enumeración que define la categoría del producto (si es null, ignora el tipo).
     * @param precioMax Límite superior de precio para la inclusión del artículo.
     * @return Un nuevo ArrayList con los productos que cumplen simultáneamente con ambos filtros.
     */
    public static ArrayList<NewProduct> filtrarProductos(ArrayList<NewProduct> listaBase, ItemType tipo, double precioMax) {
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
     * Reestructura de forma interna la disposición física de los elementos usando criterios 
     * ascendentes o descendentes de precio, orden alfabético o antigüedad.
     * 
     * @param lista    La lista original de productos que será reordenada.
     * @param criterio Cadena de texto informativa que determina la propiedad de comparación.
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