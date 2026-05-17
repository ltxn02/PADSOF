package logic;

import java.util.*;
import utils.*;
import users.*;
import catalog.*;
import transactions.*;

/**
 * Clase de utilidad que gestiona el motor de recomendaciones para artículos de segunda mano.
 * A diferencia del sistema estándar, este motor se basa en el histórico de ofertas de intercambio
 * realizadas y la disponibilidad actual de los productos en el mercado de usados.
 * @author Taha Ridda En Naji
 * @version 3.0
 */
public class SistemaRecomendacionesSegundamano {

    /**
     * Genera una lista de recomendaciones de productos de segunda mano para un cliente.
     * El algoritmo prioriza productos disponibles que coincidan con las categorías de ofertas
     * previas del usuario y productos de dueños con gustos de intercambio similares.
     *
     * @param cliente           El {@link Client} que recibirá las recomendaciones.
     * @param catalogo          Lista global de {@link SecondHandProduct} en el sistema.
     * @param todosLosClientes  Lista de clientes registrados para el cálculo de similitud.
     * @return Una {@link ArrayList} de productos de segunda mano ordenados por relevancia.
     */
    public static ArrayList<SecondHandProduct> obtenerRecomendaciones(Client cliente, ArrayList<SecondHandProduct> catalogo, ArrayList<Client> todosLosClientes) {
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
     * Calcula el índice de similitud entre dos clientes basándose en la coincidencia
     * de categorías por las que ambos han mostrado interés en sus ofertas de intercambio.
     *
     * @param c1 Cliente principal.
     * @param c2 Cliente con el que comparar.
     * @return Valor decimal que representa el grado de afinidad en intereses de intercambio.
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
     * Construye un perfil de preferencias analizando las categorías
     * de los productos solicitados en las ofertas de intercambio hechas por el cliente.
     * Cada coincidencia de categoría incrementa el peso del interés en el perfil.
     *
     * @param c El {@link Client} cuyo perfil se desea construir.
     * @return Un {@link HashMap} con los nombres de categorías y su peso acumulado.
     */
    private static HashMap<String, Double> generarPerfilPorIntercambios(Client c) {
        HashMap<String, Double> perfil = new HashMap<>();
        for (ExchangeOffer oferta: c.getOffersMade()) {
            SecondHandProduct solicitado = oferta.getRequestedProduct();
            for (Category cat: solicitado.getCategories()) {
                perfil.put(cat.getNameCategory(), perfil.getOrDefault(cat.getNameCategory(), 0.0) + 2.0);
            }
        }
        return perfil;
    }

    /**
     * Obtiene de forma eficiente el conjunto de productos de segunda mano que
     * el cliente tiene actualmente en propiedad.
     * Se utiliza principalmente para filtrar y excluir estos ítems de las recomendaciones.
     *
     * @param c El {@link Client} propietario.
     * @return Un {@link HashSet} con los {@link SecondHandProduct} del cliente.
     */
    private static HashSet<SecondHandProduct> obtenerMisProductos(Client c) {
        HashSet<SecondHandProduct> productos = new HashSet<>();

        if (c.getCarteraSegundaMano() != null) {
            for (SecondHandProduct p : c.getCarteraSegundaMano()) {
                productos.add(p);
            }
        }

        return productos;
    }
}