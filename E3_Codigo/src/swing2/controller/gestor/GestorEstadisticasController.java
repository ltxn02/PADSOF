package swing2.controller.gestor;

import swing2.view.gestor.estadisticas.PanelGestionEstadisticas;
import logic.Application;
import users.*;
import transactions.*;
import utils.OrderStatus;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador para estadísticas del gestor.
 * Proporciona datos estadísticos reales recopilados a partir del estado de la aplicación.
 * 
 * @author Lidia Martín
 */
public class GestorEstadisticasController {
    private PanelGestionEstadisticas vista;
    
    /**
     * Constructor del controlador de estadísticas.
     * Asocia el controlador con su vista correspondiente para gestionar la actualización de datos.
     * 
     * @param vista El panel de interfaz gráfica encargado de mostrar las estadísticas.
     */
    public GestorEstadisticasController(PanelGestionEstadisticas vista) {
        this.vista = vista;
    }
    
    /**
     * Calcula los ingresos totales de la aplicación basándose en los pedidos cuyo 
     * estado sea de pago procesado (EN_PREPARACION o ENTREGADO).
     * 
     * @return El importe total acumulado en euros de todas las ventas válidas.
     */
    public double calcularIngresosTotales() {
        return Application.getUsers().stream()
            .filter(u -> u instanceof Client)
            .mapToDouble(u -> {
                Client c = (Client) u;
                return c.getOrders().stream()
                    .filter(o -> o.getOrderStatus() == OrderStatus.EN_PREPARACION || 
                               o.getOrderStatus() == OrderStatus.ENTREGADO)
                    .mapToDouble(Order::getPrice)
                    .sum();
            })
            .sum();
    }
    
    /**
     * Calcula la valoración económica estimada de la totalidad de los productos 
     * de segunda mano introducidos mediante el sistema de tasación.
     * 
     * @return El valor total estimado en euros de los productos de segunda mano.
     */
    public double calcularValoracionEstimado() {
        return Application.getSecondHandProducts().stream()
            .mapToDouble(p -> p.getPrice())
            .sum();
    }
    
    /**
     * Obtiene un ranking (Top 3) con los productos más vendidos de la plataforma, 
     * contabilizando las cantidades agregadas en cada ítem de los pedidos de los clientes.
     * 
     * @return Una lista de cadenas con el formato "Nombre (X ventas)" ordenada descendentemente.
     */
    public List<String> obtenerProductosMasVendidos() {
        Map<String, Integer> ventasPorProducto = new HashMap<>();
        
        Application.getUsers().stream()
            .filter(u -> u instanceof Client)
            .forEach(u -> {
                Client c = (Client) u;
                c.getOrders().forEach(order -> {
                    order.getItems().forEach(item -> {
                        String nombre = item.getProduct().getName();
                        ventasPorProducto.put(nombre, ventasPorProducto.getOrDefault(nombre, 0) + item.getQuantity());
                    });
                });
            });
        
        return ventasPorProducto.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(3)
            .map(e -> e.getKey() + " (" + e.getValue() + " ventas)")
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene un ranking (Top 3) con los usuarios más activos de la aplicación, 
     * clasificados según el volumen bruto de pedidos que han realizado de forma histórica.
     * 
     * @return Una lista de cadenas con el formato "Username (X pedidos)" ordenada de mayor a menor.
     */
    public List<String> obtenerClientesMasActivos() {
        return Application.getUsers().stream()
            .filter(u -> u instanceof Client)
            .map(u -> (Client) u)
            .sorted((a, b) -> Integer.compare(b.getOrders().size(), a.getOrders().size()))
            .limit(3)
            .map(c -> c.getUsername() + " (" + c.getOrders().size() + " pedidos)")
            .collect(Collectors.toList());
    }
    
    /**
     * Solicita la actualización y el redibujado de los componentes de la interfaz visual asociada.
     */
    public void refrescarDatos() {
        vista.refrescar();
    }
}