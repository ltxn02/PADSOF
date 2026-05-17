package swing2.controller.gestor;

import swing2.view.gestor.estadisticas.PanelGestionEstadisticas;
import logic.Application;
import users.*;
import transactions.*;
import utils.OrderStatus;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador para estadísticas del gestor
 * Datos 100% reales del sistema
 */
public class GestorEstadisticasController {
    private PanelGestionEstadisticas vista;
    
    public GestorEstadisticasController(PanelGestionEstadisticas vista) {
        this.vista = vista;
    }
    
    /**
     * Calcula ingresos totales de pedidos pagos (EN_PREPARACION, ENTREGADO)
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
     * Calcula valor estimado de productos por tasación
     */
    public double calcularValoracionEstimado() {
        return Application.getSecondHandProducts().stream()
            .mapToDouble(p -> p.getPrice())
            .sum();
    }
    
    /**
     * Obtiene top 3 productos más vendidos
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
     * Obtiene top 3 clientes más activos (por número de pedidos)
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
    
    public void refrescarDatos() {
        vista.refrescar();
    }
}