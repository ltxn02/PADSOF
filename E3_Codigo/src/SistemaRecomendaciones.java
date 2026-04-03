import java.util.*;

public class SistemaRecomendaciones {

    public static ArrayList<Product> obtenerRecomendaciones(Client cliente, ArrayList<Product> catalogo, ArrayList<Client> todosLosClientes) {
        HashMap<Product, Double> puntosTotales = new HashMap<>();

        HashSet<Product> comprados = obtenerComprados(cliente);
        HashMap<String, Double> perfilInteres = new HashMap<>();
        for (Product p : comprados) {
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
                for (Product p : obtenerComprados(otro)) {
                    if (!comprados.contains(p)) {
                        double puntosExtra = similitud * p.calculateRating();
                        double puntosPrevios = puntosTotales.getOrDefault(p, 0.0);
                        puntosTotales.put(p, puntosPrevios + puntosExtra);
                    }
                }
            }
        }

        for (Product p : catalogo) {
            if (!comprados.contains(p)) {
                double scoreCategoria = 0;
                for (Category cat : p.getCategories()) {
                    scoreCategoria += perfilInteres.getOrDefault(cat.getNameCategory(), 0.0);
                }
                double puntosPrevios = puntosTotales.getOrDefault(p, 0.0);
                puntosTotales.put(p, puntosPrevios + scoreCategoria);
            }
        }

        ArrayList<Product> resultado = new ArrayList<>(puntosTotales.keySet());

        resultado.sort((p1, p2) -> Double.compare(puntosTotales.get(p2), puntosTotales.get(p1)));

        return resultado;
    }


    private static double calcularSimilitudSimple(Client c1, Client c2) {
        HashSet<Product> p1 = obtenerComprados(c1);
        HashSet<Product> p2 = obtenerComprados(c2);

        int comunes = 0;
        for (Product p : p1) {
            if (p2.contains(p)) comunes++;
        }

        if (p1.isEmpty() || p2.isEmpty()) return 0.0;
        return (double) comunes / Math.max(p1.size(), p2.size());
    }


    private static HashSet<Product> obtenerComprados(Client c) {
        HashSet<Product> productos = new HashSet<>();
        if (c.getOrderHistoric() != null) {
            for (Order o : c.getOrderHistoric().getOrders()) {
                for (CartItem item : o.getItems()) {
                    if (item.getProduct() instanceof Product) {
                        productos.add((Product) item.getProduct());
                    }
                }
            }
        }
        return productos;
    }
}