package swing2.controller.gestor;

import swing2.view.gestor.estadisticas.PanelGestionEstadisticas;
import logic.Application;
import users.*;
import catalog.*;

/**
 * Controlador para estadísticas del gestor
 */
public class GestorEstadisticasController {
    private PanelGestionEstadisticas vista;
    
    // Cache
    private long cacheTime = 0;
    private static final long CACHE_VALIDITY = 5000; // 5 segundos
    
    public GestorEstadisticasController(PanelGestionEstadisticas vista) {
        this.vista = vista;
    }
    
    /**
     * Calcula el revenue basado en precios del catálogo
     */
    public double calcularRevenue() {
        return Application.getCatalog().stream()
            .mapToDouble(NewProduct::getPrice)
            .sum();
    }
    
    public void refrescarDatos() {
        vista.refrescar();
    }
}