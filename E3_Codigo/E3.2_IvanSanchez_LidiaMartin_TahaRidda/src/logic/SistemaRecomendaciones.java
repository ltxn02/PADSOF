package logic;

import java.util.*;
import utils.*;
import users.*;
import catalog.*;
import transactions.*;

/**
 * Clase de utilidad encargada de gestionar la lógica del motor de recomendaciones.
 * Implementa un sistema híbrido que combina el filtrado basado en contenido
 * (preferencias de categorías del usuario) y filtrado colaborativo simple
 * (similitud entre perfiles de clientes).
 * @author Taha Ridda
 * @version 2.0
 */
public class SistemaRecomendaciones {

    /**
     * Genera una lista personalizada de productos recomendados para un cliente específico.
     * El algoritmo evalúa el histórico de compras, las categorías de interés y la
     * similitud con otros usuarios para puntuar productos del catálogo aún no adquiridos.
     *
     * @param cliente           El {@link Client} para quien se generan las recomendaciones.
     * @param catalogo          Lista completa de {@link NewProduct} disponibles en el sistema.
     * @param todosLosClientes  Lista de todos los clientes registrados para el análisis de similitud.
     * @return Una {@link ArrayList} de productos ordenados de mayor a menor relevancia.
     */
    public static ArrayList<NewProduct> obtenerRecomendaciones(Client cliente, ArrayList<NewProduct> catalogo, ArrayList<Client> todosLosClientes) {
        HashMap<NewProduct, Double> puntosTotales = new HashMap<>();

        HashSet<NewProduct> comprados = obtenerComprados(cliente);

        HashMap<String, Double> perfilInteres = new HashMap<>();
        for (NewProduct p : comprados) {
            double rating = p.calculateRating();
            if (rating >= 3) {
                double peso = (rating >= 5) ? 2.0 : (rating >= 4) ? 1.5 : 1.0;
                for (Category cat : p.getCategories()) {
                    String nombre = cat.getNameCategory();
                    double puntosActuales = perfilInteres.getOrDefault(nombre, 0.0);
                    perfilInteres.put(nombre, puntosActuales + peso);
                }
            }
        }

        for (Client otro : todosLosClientes) {
            if (otro.equals(cliente)) continue;

            double similitud = calcularSimilitudSimple(cliente, otro);

            if (similitud > 0.5) {
                for (NewProduct p : obtenerComprados(otro)) {
                    if (!comprados.contains(p)) {
                        double puntosExtra = similitud * p.calculateRating();
                        double puntosPrevios = puntosTotales.getOrDefault(p, 0.0);
                        puntosTotales.put(p, puntosPrevios + puntosExtra);
                    }
                }
            }
        }

        for (NewProduct p : catalogo) {
            if (!comprados.contains(p)) {
                double scoreCategoria = 0;
                for (Category cat : p.getCategories()) {
                    scoreCategoria += perfilInteres.getOrDefault(cat.getNameCategory(), 0.0);
                }
                double puntosPrevios = puntosTotales.getOrDefault(p, 0.0);
                puntosTotales.put(p, puntosPrevios + scoreCategoria);
            }
        }

        ArrayList<NewProduct> resultado = new ArrayList<>(puntosTotales.keySet());
        resultado.sort((p1, p2) -> Double.compare(puntosTotales.get(p2), puntosTotales.get(p1)));
        return resultado;
    }

    /**
     * Calcula el índice de similitud entre dos clientes basándose en la intersección
     * de sus catálogos de productos comprados.
     *
     * @param c1 Primer cliente.
     * @param c2 Segundo cliente.
     * @return Valor decimal entre 0.0 y 1.0 que representa la afinidad entre ambos.
     */
    private static double calcularSimilitudSimple(Client c1, Client c2) {
        HashSet<NewProduct> p1 = obtenerComprados(c1);
        HashSet<NewProduct> p2 = obtenerComprados(c2);

        int comunes = 0;
        for (NewProduct p : p1) {
            if (p2.contains(p)) comunes++;
        }

        if (p1.isEmpty() || p2.isEmpty()) return 0.0;
        return (double) comunes / Math.max(p1.size(), p2.size());
    }

    /**
     * Extrae de forma única todos los productos comprados por un cliente a través
     * de su histórico de pedidos.
     *
     * @param c El {@link Client} a analizar.
     * @return Un {@link HashSet} con los productos únicos adquiridos.
     */
    private static HashSet<NewProduct> obtenerComprados(Client c) {
        HashSet<NewProduct> productos = new HashSet<>();
        if (c.getOrderHistoric() != null) {
            for (Order o : c.getOrderHistoric().getOrders()) {
                if (o.getItems() != null) {
                    for (CartItem item : o.getItems()) {
                        productos.add(item.getProduct());
                    }
                }
            }
        }
        return productos;
    }
}