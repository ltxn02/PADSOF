package logic;

import java.util.*;

import products.*;
import users.*;
import transactions.*;
import utils.CartItem;

/**
 * Clase de utilidad encargada de la generación de informes y métricas de rendimiento de la tienda.
 * Analiza el comportamiento de los usuarios, el volumen de ventas, la actividad de intercambios
 * y el rendimiento laboral de los empleados mediante el procesamiento de datos globales de la aplicación.
 * @author Taha Ridda
 * @version 2.0
 */
public class StoreStatistics {

    /**
     * Genera un reporte detallado en formato de texto con las estadísticas avanzadas de la tienda.
     * El informe incluye métricas globales de ingresos, rankings de clientes por actividad,
     * productos más vendidos y un control de productividad de los empleados (tasaciones).
     *
     * @return Un {@link String} con el informe formateado listo para ser mostrado por consola o exportado.
     */
    public static String generateStoreReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=======================================================\n");
        sb.append("      ESTADÍSTICAS AVANZADAS DE LA TIENDA RONGERO      \n");
        sb.append("=======================================================\n\n");

        ArrayList<Client> clientes = new ArrayList<>();
        ArrayList<Employee> empleados = new ArrayList<>();

        int totalOrders = 0;
        int totalExchanges = 0;
        double ingresosTotales = 0.0;

        Map<String, Integer> ventasPorProducto = new HashMap<>();
        Map<Client, Integer> comprasPorCliente = new HashMap<>();
        Map<Client, Integer> intercambiosPorCliente = new HashMap<>();
        Map<Employee, Integer> tasacionesPorEmpleado = new HashMap<>();

        for (RegisteredUser u : Application.getUsers()) {
            if (u instanceof Client) {
                Client c = (Client) u;
                clientes.add(c);

                int pedidosCliente = 0;
                if (c.getOrderHistoric() != null && c.getOrderHistoric().getOrders() != null) {
                    for (Order o : c.getOrderHistoric().getOrders()) {
                        pedidosCliente++;
                        totalOrders++;
                        ingresosTotales += o.getPrice();

                        if (o.getItems() != null) {
                            for (CartItem item : o.getItems()) {
                                String nombreProd = item.getProduct().getName();
                                int cantidadComprada = item.getQuantity();
                                ventasPorProducto.put(nombreProd, ventasPorProducto.getOrDefault(nombreProd, 0) + cantidadComprada);
                            }
                        }
                    }
                }
                comprasPorCliente.put(c, pedidosCliente);

                int intercambiosCliente = 0;
                if (c.getExchangeHistoric() != null && c.getExchangeHistoric().getExchanges() != null) {
                    intercambiosCliente = c.getExchangeHistoric().getExchanges().size();
                }
                intercambiosPorCliente.put(c, intercambiosCliente);

            } else if (u instanceof Employee) {
                empleados.add((Employee) u);
            }
        }

        totalExchanges = totalExchanges / 2;

        sb.append("--- MÉTRICAS GLOBALES ---\n");
        sb.append("  Total Usuarios Registrados: ").append(Application.getUsers().size()).append("\n");
        sb.append("  Total Pedidos Realizados: ").append(totalOrders).append("\n");
        sb.append("  Total Intercambios Físicos: ").append(totalExchanges).append("\n");
        sb.append("  Ingresos Estimados: ").append(String.format("%.2f €", ingresosTotales)).append("\n\n");

        sb.append("--- TOP 3 CLIENTES (POR COMPRAS) ---\n");
        List<Map.Entry<Client, Integer>> topClientesCompras = new ArrayList<>(comprasPorCliente.entrySet());
        topClientesCompras.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (int i = 0; i < Math.min(3, topClientesCompras.size()); i++) {
            Map.Entry<Client, Integer> entry = topClientesCompras.get(i);
            if (entry.getValue() > 0) {
                sb.append("  ").append(i + 1).append(".- ").append(entry.getKey().getUsername())
                        .append(" (").append(entry.getValue()).append(" pedidos)\n");
            }
        }

        sb.append("\n--- TOP 3 CLIENTES (POR INTERCAMBIOS) ---\n");
        List<Map.Entry<Client, Integer>> topClientesIntercambios = new ArrayList<>(intercambiosPorCliente.entrySet());
        topClientesIntercambios.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (int i = 0; i < Math.min(3, topClientesIntercambios.size()); i++) {
            Map.Entry<Client, Integer> entry = topClientesIntercambios.get(i);
            if (entry.getValue() > 0) {
                sb.append("  ").append(i + 1).append(".- ").append(entry.getKey().getUsername())
                        .append(" (").append(entry.getValue()).append(" intercambios)\n");
            }
        }
        sb.append("\n");

        sb.append("--- PRODUCTOS MÁS VENDIDOS ---\n");
        if (ventasPorProducto.isEmpty()) {
            sb.append("  Todavía no hay ventas\n");
        } else {
            List<Map.Entry<String, Integer>> topProductos = new ArrayList<>(ventasPorProducto.entrySet());
            topProductos.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            for (int i = 0; i < Math.min(5, topProductos.size()); i++) {
                sb.append("  ").append(i + 1).append(".- ").append(topProductos.get(i).getKey())
                        .append(" (").append(topProductos.get(i).getValue()).append(" uds vendidas)\n");
            }
        }
        sb.append("\n");

        sb.append("--- RENDIMIENTO DE EMPLEADOS (MÁS ACTIVOS) ---\n");

        for (SecondHandProduct shp : Application.getSecondHandProducts()) {
            if (shp.isAppraised()) {
                Employee appraiser = shp.getAppraiser();
                if (appraiser != null) {
                    tasacionesPorEmpleado.put(appraiser, tasacionesPorEmpleado.getOrDefault(appraiser, 0) + 1);
                }
            }
        }

        if (tasacionesPorEmpleado.isEmpty()) {
            sb.append("  Todavía no hay tasaciones\n");
        } else {
            List<Map.Entry<Employee, Integer>> topEmpleados = new ArrayList<>(tasacionesPorEmpleado.entrySet());
            topEmpleados.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            for (int i = 0; i < Math.min(3, topEmpleados.size()); i++) {
                sb.append("  ").append(i + 1).append(".- ").append(topEmpleados.get(i).getKey().getUsername())
                        .append(" (").append(topEmpleados.get(i).getValue()).append(" tasaciones realizadas)\n");
            }
        }

        sb.append("=======================================================\n");
        return sb.toString();
    }
}