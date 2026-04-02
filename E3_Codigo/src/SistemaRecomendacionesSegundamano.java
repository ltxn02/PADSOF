import java.util.*;

public class SistemaRecomendacionesSegundamano {

    public static ArrayList<SecondHandProduct> obtenerRecomendaciones(Client cliente, ArrayList<SecondHandProduct> catalogo, ArrayList<Client> todosLosClientes) {
        HashMap<SecondHandProduct, Double> puntosTotales = new HashMap<>();

        HashSet<SecondHandProduct> misProductos = obtenerMisProductos(cliente);
        HashMap<String, Double> perfilInteres = generarPerfilPorIntercambios(cliente);

        for (Client otro : todosLosClientes) {
            if (otro.equals(cliente)) continue;

            double similitud = calcularSimilitudIntercambio(cliente, otro);

            if (similitud > 0.3) {
                for (SecondHandProduct p : obtenerMisProductos(otro)) {
                    if (!misProductos.contains(p) && p.estádisponible()) {


                        double puntajeA = similitud * p.calculateRating();
                        puntosTotales.put(p, puntosTotales.getOrDefault(p, 0.0) + puntajeA);
                    }
                }
            }
        }

        for (SecondHandProduct p : catalogo) {
            if (!misProductos.contains(p) && p.estádisponible()) {
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


    private static HashMap<String, Double> generarPerfilPorIntercambios(Client c) {
        HashMap<String, Double> perfil = new HashMap<>();
            for (Exchangeoffer oferta: c.getMisOfertasEnviadas()) {
                SecondHandProduct solicitado= oferta.getRequestedProduct();
                for (Category cat: solicitado.getCategories()) {
                    perfil.put(cat.getNameCategory(), perfil.getOrDefault(cat.getNameCategory(), 0.0) + 2.0);
                }
            }
        return perfil;
    }

    /**
     * Obtiene el conjunto de productos de segunda mano que pertenecen al cliente.
     * Se usa para excluir sus propios productos de las recomendaciones.
     */
    private static HashSet<SecondHandProduct> obtenerMisProductos(Client c) {
        HashSet<SecondHandProduct> productos = new HashSet<>();

        // Verificamos que el cliente tenga una cartera de productos asignada
        if (c.getCarteraSegundaMano() != null) {
            // Recorremos la lista de productos de segunda mano del cliente
            for (SecondHandProduct p : c.getCarteraSegundaMano()) {
                // Añadimos el producto al conjunto
                productos.add(p);
            }
        }

        return productos;
    }
}