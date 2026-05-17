package swing2.controller.gestor;

import logic.Application;
import discounts.*;
import catalog.Category;
import catalog.NewProduct;
import catalog.Product;
import swing2.view.VentanaPrincipa;
import swing2.view.gestor.descuentos.PanelGestionDescuentos;

import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Controlador para gestionar descuentos de la tienda.
 * Separa la lógica de negocio de la interfaz gráfica.
 */
public class GestorDescuentoController {
    private VentanaPrincipa ventana;
    private PanelGestionDescuentos panel;

    public GestorDescuentoController(VentanaPrincipa ventana, PanelGestionDescuentos panel) {
        this.ventana = ventana;
        this.panel = panel;
    }

    /**
     * Obtener lista de todos los descuentos globales
     */
    public ArrayList<IDiscount> obtenerDescuentos() {
        ArrayList<IDiscount> descuentos = new ArrayList<>();
        ArrayList<IDiscount> globales = Application.getGlobalDiscounts();
        
        if (globales != null) {
            descuentos.addAll(globales);
        }
        
        return descuentos;
    }

    /**
     * Buscar descuentos por término
     */
    public ArrayList<IDiscount> buscarDescuentos(ArrayList<IDiscount> descuentosBase, String termino) {
        ArrayList<IDiscount> resultados = new ArrayList<>();
        String terminoMinuscula = termino.toLowerCase().trim();

        if (terminoMinuscula.isEmpty()) {
            return new ArrayList<>(descuentosBase);
        }

        for (IDiscount desc : descuentosBase) {
            if (desc.getDescription().toLowerCase().contains(terminoMinuscula)) {
                resultados.add(desc);
            }
        }

        return resultados;
    }

    /**
     * Obtener el tipo de descuento como string
     */
    public String obtenerTipoDescuento(IDiscount desc) {
        if (desc instanceof PercentageDiscount) {
            return "Rebaja %";
        } else if (desc instanceof VolumeDiscount) {
            return "Volumen €";
        } else if (desc instanceof GiftDiscount) {
            return "Regalo";
        } else if (desc instanceof QuantityDiscount) {
            return "Cantidad X×Y";
        }
        return "Desconocido";
    }

    /**
     * Obtener el porcentaje del descuento (si aplica)
     */
    public String obtenerPorcentaje(IDiscount desc) {
        if (desc instanceof PercentageDiscount) {
            PercentageDiscount pd = (PercentageDiscount) desc;
            return String.format("%.1f%%", pd.getValue());
        }
        return "N/A";
    }

    /**
     * Obtener la categoría del producto al que se aplica (si aplica)
     */
    public String obtenerCategoria(IDiscount desc) {
        if (desc instanceof PercentageDiscount) {
            for (NewProduct p : Application.getCatalog()) {
                if (p instanceof Product) {
                    Product prod = (Product) p;
                    if (prod.getDiscount() == desc) {
                        if (prod.getCategories() != null && !prod.getCategories().isEmpty()) {
                            return prod.getCategories().get(0).getNameCategory();
                        }
                    }
                }
            }
        } else if (desc instanceof QuantityDiscount) {
            for (NewProduct p : Application.getCatalog()) {
                if (p instanceof Product) {
                    Product prod = (Product) p;
                    if (prod.getDiscount() == desc) {
                        if (prod.getCategories() != null && !prod.getCategories().isEmpty()) {
                            return prod.getCategories().get(0).getNameCategory();
                        }
                    }
                }
            }
        } else if (desc instanceof GiftDiscount) {
            return "Todos los productos";
        } else if (desc instanceof VolumeDiscount) {
            return "Carrito completo";
        }
        
        return "N/A";
    }

    /**
     * Obtener información adicional del descuento
     */
    public String obtenerDetalles(IDiscount desc) {
        StringBuilder sb = new StringBuilder();
        
        if (desc instanceof PercentageDiscount) {
            PercentageDiscount pd = (PercentageDiscount) desc;
            sb.append("Tipo: Rebaja porcentual\n");
            sb.append("Descuento: ").append(String.format("%.1f%%", pd.getValue())).append("\n");
            
            for (NewProduct p : Application.getCatalog()) {
                if (p instanceof Product) {
                    Product prod = (Product) p;
                    if (prod.getDiscount() == desc) {
                        sb.append("Producto: ").append(prod.getName()).append("\n");
                        if (!prod.getCategories().isEmpty()) {
                            sb.append("Categoría: ").append(prod.getCategories().get(0).getNameCategory()).append("\n");
                        }
                        break;
                    }
                }
            }
            
        } else if (desc instanceof VolumeDiscount) {
            VolumeDiscount vd = (VolumeDiscount) desc;
            sb.append("Tipo: Descuento por volumen\n");
            sb.append("Gasto mínimo: €").append(String.format("%.2f", vd.getThreshold())).append("\n");
            sb.append("Descuento: €").append(String.format("%.2f", vd.getValue())).append("\n");
            sb.append("Aplicable: Carrito completo\n");
            
        } else if (desc instanceof GiftDiscount) {
            GiftDiscount gd = (GiftDiscount) desc;
            sb.append("Tipo: Regalo\n");
            sb.append("Gasto mínimo: €").append(String.format("%.2f", gd.getMinGasto())).append("\n");
            sb.append("Regalo: ").append(gd.getRegalo().getName()).append("\n");
            
        } else if (desc instanceof QuantityDiscount) {
            QuantityDiscount qd = (QuantityDiscount) desc;
            sb.append("Tipo: Descuento por cantidad\n");
            sb.append("Promoción: Lleva ").append(qd.getBuyX()).append(" paga ").append(qd.getPayY()).append("\n");
            
            for (NewProduct p : Application.getCatalog()) {
                if (p instanceof Product) {
                    Product prod = (Product) p;
                    if (prod.getDiscount() == desc) {
                        sb.append("Producto: ").append(prod.getName()).append("\n");
                        if (!prod.getCategories().isEmpty()) {
                            sb.append("Categoría: ").append(prod.getCategories().get(0).getNameCategory()).append("\n");
                        }
                        break;
                    }
                }
            }
        }
        
        sb.append("Descripción: ").append(desc.getDescription()).append("\n");
        
        return sb.toString();
    }

    /**
     * Parsear fecha en formato DD/MM/YYYY HH:mm a LocalDateTime
     */
    private LocalDateTime parsearFecha(String fechaStr) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.parse(fechaStr, formatter);
    }

    /**
     * Validar que la fecha de fin sea posterior a la de inicio
     */
    private boolean validarFechas(LocalDateTime desde, LocalDateTime hasta) {
        return hasta.isAfter(desde);
    }

    /**
     * Crear descuento de rebaja porcentual por categoría
     */
    public boolean crearDescuentoRebaja(String descripcion, String categoria, double porcentaje, 
                                       String fechaInicio, String fechaFin) {
        try {
            if (descripcion == null || descripcion.trim().isEmpty()) {
                mostrarError("La descripción es obligatoria.");
                return false;
            }

            if (categoria == null || categoria.trim().isEmpty() || categoria.equals("- Seleccionar categoría -")) {
                mostrarError("Debes seleccionar una categoría.");
                return false;
            }

            if (porcentaje <= 0 || porcentaje >= 100) {
                mostrarError("El porcentaje debe estar entre 0 y 100.");
                return false;
            }

            LocalDateTime desde, hasta;
            try {
                desde = parsearFecha(fechaInicio);
                hasta = parsearFecha(fechaFin);
            } catch (DateTimeParseException e) {
                mostrarError("Formato de fecha inválido. Usa: DD/MM/YYYY HH:mm\nEj: 01/12/2025 10:30");
                return false;
            }

            if (!validarFechas(desde, hasta)) {
                mostrarError("La fecha de fin debe ser posterior a la de inicio.");
                return false;
            }

            Category cat = buscarCategoriaPorNombre(categoria);
            if (cat == null) {
                mostrarError("Categoría no encontrada: " + categoria);
                return false;
            }

            PercentageDiscount rebaja = new PercentageDiscount(porcentaje, descripcion, desde, hasta);
            
            int productosAfectados = 0;
            for (NewProduct p : Application.getCatalog()) {
                if (p instanceof Product) {
                    Product prod = (Product) p;
                    if (prod.getCategories() != null) {
                        for (Category c : prod.getCategories()) {
                            if (c.getNameCategory().equals(categoria)) {
                                prod.setDiscount(rebaja);
                                productosAfectados++;
                                break;
                            }
                        }
                    }
                }
            }

            Application.addDiscount(rebaja);
            Application.guardarDatos("rongero_data.dat");

            mostrarExito("Descuento de rebaja creado exitosamente.\n" +
                "Categoría: " + categoria + "\n" +
                "Porcentaje: " + String.format("%.1f%%", porcentaje) + "\n" +
                "Productos afectados: " + productosAfectados);
            return true;

        } catch (Exception e) {
            mostrarError("Error al crear rebaja: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Crear descuento por volumen de carrito
     */
    public boolean crearDescuentoVolumen(String descripcion, double gastoMinimo, 
                                        double descuentoEuro, String fechaInicio, String fechaFin) {
        try {
            if (descripcion == null || descripcion.trim().isEmpty()) {
                mostrarError("La descripción es obligatoria.");
                return false;
            }

            if (gastoMinimo <= 0) {
                mostrarError("El gasto mínimo debe ser mayor a 0.");
                return false;
            }

            if (descuentoEuro <= 0) {
                mostrarError("El descuento en euros debe ser mayor a 0.");
                return false;
            }

            if (descuentoEuro >= gastoMinimo) {
                mostrarError("El descuento no puede ser igual o superior al gasto mínimo.");
                return false;
            }

            LocalDateTime desde, hasta;
            try {
                desde = parsearFecha(fechaInicio);
                hasta = parsearFecha(fechaFin);
            } catch (DateTimeParseException e) {
                mostrarError("Formato de fecha inválido. Usa: DD/MM/YYYY HH:mm\nEj: 01/12/2025 10:30");
                return false;
            }

            if (!validarFechas(desde, hasta)) {
                mostrarError("La fecha de fin debe ser posterior a la de inicio.");
                return false;
            }

            VolumeDiscount volumen = new VolumeDiscount(descuentoEuro, gastoMinimo, descripcion, desde, hasta);
            Application.addDiscount(volumen);
            Application.guardarDatos("rongero_data.dat");

            mostrarExito("Descuento por volumen creado exitosamente.\n" +
                "Gasto mínimo: €" + String.format("%.2f", gastoMinimo) + "\n" +
                "Descuento: €" + String.format("%.2f", descuentoEuro));
            return true;

        } catch (Exception e) {
            mostrarError("Error al crear descuento por volumen: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Crear descuento regalo
     */
    public boolean crearDescuentoRegalo(String descripcion, double gastoMinimo, String productoRegalo,
                                       String fechaInicio, String fechaFin) {
        try {
            if (descripcion == null || descripcion.trim().isEmpty()) {
                mostrarError("La descripción es obligatoria.");
                return false;
            }

            if (gastoMinimo <= 0) {
                mostrarError("El gasto mínimo debe ser mayor a 0.");
                return false;
            }

            if (productoRegalo == null || productoRegalo.trim().isEmpty() || productoRegalo.equals("- Seleccionar producto -")) {
                mostrarError("Debes seleccionar un producto para regalar.");
                return false;
            }

            NewProduct pRegalo = buscarProductoEnCatalogo(productoRegalo);
            if (pRegalo == null) {
                mostrarError("Producto no encontrado: " + productoRegalo);
                return false;
            }

            LocalDateTime desde, hasta;
            try {
                desde = parsearFecha(fechaInicio);
                hasta = parsearFecha(fechaFin);
            } catch (DateTimeParseException e) {
                mostrarError("Formato de fecha inválido. Usa: DD/MM/YYYY HH:mm\nEj: 01/12/2025 10:30");
                return false;
            }

            if (!validarFechas(desde, hasta)) {
                mostrarError("La fecha de fin debe ser posterior a la de inicio.");
                return false;
            }

            GiftDiscount regalo = new GiftDiscount(gastoMinimo, pRegalo, descripcion, desde, hasta);
            Application.addDiscount(regalo);
            Application.guardarDatos("rongero_data.dat");

            mostrarExito("Promoción de regalo creada exitosamente.\n" +
                "Gasto mínimo: €" + String.format("%.2f", gastoMinimo) + "\n" +
                "Regalo: " + pRegalo.getName());
            return true;

        } catch (Exception e) {
            mostrarError("Error al crear promoción de regalo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Crear descuento por cantidad (Lleva X, Paga Y)
     */
    public boolean crearDescuentoCantidad(String descripcion, String categoria, int lleva, int paga,
                                         String fechaInicio, String fechaFin) {
        try {
            if (descripcion == null || descripcion.trim().isEmpty()) {
                mostrarError("La descripción es obligatoria.");
                return false;
            }

            if (categoria == null || categoria.trim().isEmpty() || categoria.equals("- Seleccionar categoría -")) {
                mostrarError("Debes seleccionar una categoría.");
                return false;
            }

            if (lleva <= 0 || paga <= 0) {
                mostrarError("Las cantidades deben ser mayores a 0.");
                return false;
            }

            if (paga >= lleva) {
                mostrarError("La cantidad a pagar debe ser menor a la cantidad a llevar.");
                return false;
            }

            LocalDateTime desde, hasta;
            try {
                desde = parsearFecha(fechaInicio);
                hasta = parsearFecha(fechaFin);
            } catch (DateTimeParseException e) {
                mostrarError("Formato de fecha inválido. Usa: DD/MM/YYYY HH:mm\nEj: 01/12/2025 10:30");
                return false;
            }

            if (!validarFechas(desde, hasta)) {
                mostrarError("La fecha de fin debe ser posterior a la de inicio.");
                return false;
            }

            Category cat = buscarCategoriaPorNombre(categoria);
            if (cat == null) {
                mostrarError("Categoría no encontrada: " + categoria);
                return false;
            }

            QuantityDiscount cantidad = new QuantityDiscount(lleva, paga, descripcion, desde, hasta);

            int productosAfectados = 0;
            for (NewProduct p : Application.getCatalog()) {
                if (p instanceof Product) {
                    Product prod = (Product) p;
                    if (prod.getCategories() != null) {
                        for (Category c : prod.getCategories()) {
                            if (c.getNameCategory().equals(categoria)) {
                                prod.setDiscount(cantidad);
                                productosAfectados++;
                                break;
                            }
                        }
                    }
                }
            }

            Application.addDiscount(cantidad);
            Application.guardarDatos("rongero_data.dat");

            mostrarExito("Descuento por cantidad creado exitosamente.\n" +
                "Promoción: Lleva " + lleva + " paga " + paga + "\n" +
                "Categoría: " + categoria + "\n" +
                "Productos afectados: " + productosAfectados);
            return true;

        } catch (Exception e) {
            mostrarError("Error al crear descuento por cantidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Eliminar un descuento
     */
    public boolean eliminarDescuento(IDiscount descuento) {
        try {
            if (descuento instanceof PercentageDiscount || descuento instanceof QuantityDiscount) {
                for (NewProduct p : Application.getCatalog()) {
                    if (p instanceof Product) {
                        Product prod = (Product) p;
                        if (prod.getDiscount() == descuento) {
                            prod.setDiscount(null);
                        }
                    }
                }
            }
            
            ArrayList<IDiscount> globales = Application.getGlobalDiscounts();
            globales.remove(descuento);
            
            mostrarExito("Descuento eliminado correctamente.");
            Application.guardarDatos("rongero_data.dat");
            return true;
            
        } catch (Exception e) {
            mostrarError("Error al eliminar descuento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo auxiliar que busca una categoría por nombre
     */
    private Category buscarCategoriaPorNombre(String nombre) {
        for (Category c : Application.getGlobalCategories()) {
            if (c.getNameCategory().equalsIgnoreCase(nombre)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Metodo auxiliar que busca un producto en el catálogo por su nombre
     */
    private NewProduct buscarProductoEnCatalogo(String nombre) {
        for (NewProduct p : Application.getCatalog()) {
            if (p.getName().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Obtener lista de nombres de categorías para combobox
     */
    public ArrayList<String> obtenerNombresCategorias() {
        ArrayList<String> nombres = new ArrayList<>();
        nombres.add("- Seleccionar categoría -");
        for (Category c : Application.getGlobalCategories()) {
            nombres.add(c.getNameCategory());
        }
        return nombres;
    }

    /**
     * Obtener lista de nombres de productos para combobox
     */
    public ArrayList<String> obtenerNombresProductos() {
        ArrayList<String> nombres = new ArrayList<>();
        nombres.add("- Seleccionar producto -");
        for (NewProduct p : Application.getCatalog()) {
            nombres.add(p.getName());
        }
        return nombres;
    }

    /**
     * Mostrar diálogo de error
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(panel, 
            "❌ " + mensaje, 
            "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Mostrar diálogo de éxito
     */
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(panel, 
            "✅ " + mensaje, 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
