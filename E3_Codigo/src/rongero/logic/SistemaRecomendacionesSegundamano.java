package logic;
import java.util.*;

import model.catalog.SecondHandProduct;
import model.catalog.Category;
import model.user.Client;
import model.transactions.ExchangeOffer;


/**
 * Sistema de Recomendacion para productos de segunda mano
 * Este sistema se basa solo en las ofertas hechas, y la similitud entre usuarios
 * @author Taha Ridda En Naji
 * @version 3.0
 *
 * */
public class SistemaRecomendacionesSegundamano {
    /**
     * Genera una lista de productos de segunda mano recomendados para un cliente.
     * Combina el filtrado por similitud entre usuarios y el perfil de interés por categorías
     * derivado de ofertas previas.
     * * @param cliente El cliente para el que se generan las recomendaciones.
     * @param catalogo La lista de productos de segunda mano disponibles en el sistema.
     * @param todosLosClientes Lista de clientes registrados para el cálculo de similitudes.
     * @return ArrayList de SecondHandProduct ordenados de mayor a menor relevancia.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static ArrayList<SecondHandProduct> obtenerRecomendaciones(Client cliente, List<SecondHandProduct> catalogo, ArrayList<Client> todosLosClientes) {
        HashMap<SecondHandProduct, Double> puntosTotales = new HashMap<>();

        HashSet<SecondHandProduct> misProductos = obtenerMisProductos(cliente);
        HashMap<String, Double> perfilInteres = generarPerfilPorIntercambios(cliente);

        for (Client otro : todosLosClientes) {
            if (otro.equals(cliente)) continue;

            double similitud = calcularSimilitudIntercambio(cliente, otro);

            if (similitud > 0.3) {
                for (SecondHandProduct p : obtenerMisProductos(otro)) {
                    if (!misProductos.contains(p) && p.isAvailable()) {

                        puntosTotales.put(p, puntosTotales.getOrDefault(p, 0.0) + similitud);
                    }
                }
            }
        }

        for (SecondHandProduct p : catalogo) {
            if (!misProductos.contains(p) && p.isAvailable()) {
                double scoreCategoria = 0;
                for (Category cat : p.getCategories()) {
                    scoreCategoria += perfilInteres.getOrDefault(cat.getNameCategory(), 0.0);
                }
                puntosTotales.put(p, puntosTotales.getOrDefault(p, 0.0) + scoreCategoria);
            }
        }

        ArrayList<SecondHandProduct> resultado = new ArrayList<>(puntosTotales.keySet());

        resultado.sort((p1, p2) -> Double.compare(puntosTotales.get(p2), puntosTotales.get(p1)));

        return resultado;
    }

    /**
     * Calcula la similitud entre dos clientes basándose en la coincidencia de
     * categorías de interés en sus perfiles de intercambio.
     * @param c1 Primer cliente para la comparación.
     * @param c2 Segundo cliente para la comparación.
     * @return Valor double que representa el grado de similitud (0.0 a 1.0).
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    private static double calcularSimilitudIntercambio(Client c1, Client c2) {
        HashMap<String, Double> p1 = generarPerfilPorIntercambios(c1);
        HashMap<String, Double> p2 = generarPerfilPorIntercambios(c2);

        int coincidencias = 0;
        for (String cat: p1.keySet()) {
            if (p2.containsKey(cat)) coincidencias++;
        }

        if (p1.isEmpty() || p2.isEmpty()) return 0.0;
        return (double) coincidencias / Math.max(p1.size(), p2.size());
    }
    /**
     * Obtiene el conjunto de productos de segunda mano que pertenecen al cliente
     * para poder excluirlos de sus propias recomendaciones.
     * @param c Cliente del que se desea obtener su propiedad actual.
     * @return HashSet con los productos que el cliente ya posee.
     * @author Taha Ridda En Naji
     * @version 3.0
     */

    private static HashMap<String, Double> generarPerfilPorIntercambios(Client c) {
        HashMap<String, Double> perfil = new HashMap<>();
            for (ExchangeOffer oferta: c.obtenerMisOfertasEnviadas()) {
                SecondHandProduct solicitado= oferta.getRequestedProduct();
                for (Category cat: solicitado.getCategories()) {
                    perfil.put(cat.getNameCategory(), perfil.getOrDefault(cat.getNameCategory(), 0.0) + 2.0);
                }
            }
        return perfil;
    }

    /**
     * Obtiene el conjunto de productos de segunda mano que pertenecen al cliente.
     * @author Taha Ridda En Naji
     * @param c El cliente que quiere ver el sistema de recomendaciones asi que recogemos sus productos para excluirlos
     * Se usa para excluir sus propios productos de las recomendaciones.
     *
     */
    private static HashSet<SecondHandProduct> obtenerMisProductos(Client c) {
        HashSet<SecondHandProduct> productos = new HashSet<>();

        // Verificamos que el cliente tenga una cartera de productos asignada
        if (c.getCarteraSegundaMano() != null) {
            // Recorremos la lista de productos de segunda mano del cliente
            for (SecondHandProduct p : c.getCarteraSegundaMano()) {
                productos.add(p);
            }
        }

        return productos;
    }
}