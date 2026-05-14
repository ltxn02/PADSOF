package swing2.controller.gestor;

import logic.Application;
import discounts.*;
import catalog.Category;
import catalog.NewProduct;
import catalog.Product;
import users.Manager;
import swing2.view.VentanaPrincipa;
import swing2.view.gestor.descuentos.PanelGestionDescuentos;

import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        // Los descuentos globales (Volumen, Regalo) no aplican a categorías específicas
        // Los descuentos de producto (Rebaja %, Cantidad X×Y) se aplican a productos específicos
        
        if (desc instanceof PercentageDiscount) {
            // Buscar el producto que tiene este descuento
            for (NewProduct p : Application.getCatalog()) {
                if (p instanceof Product) {
                    Product prod = (Product) p;
                    if (prod.getDiscount() == desc) {
                        // Retornar la primera categoría del producto
                        if (prod.getCategories() != null && !prod.getCategories().isEmpty()) {
                            return prod.getCategories().get(0).getNameCategory();
                        }
                    }
                }
            }
        } else if (desc instanceof QuantityDiscount) {
            // Buscar el producto que tiene este descuento
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
            sb.append("Tipo: Rebaja Porcentual\n");
            sb.append("Descuento: ").append(String.format("%.1f%%", pd.getValue())).append("\n");
            
            // Encontrar el producto
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
            sb.append("Tipo: Descuento por Volumen\n");
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
            sb.append("Tipo: Descuento por Cantidad\n");
            sb.append("Promoción: Lleva ").append(qd.getBuyX()).append(" paga ").append(qd.getPayY()).append("\n");
            
            // Encontrar el producto
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
     * Crear descuento de rebaja porcentual
     */
    public boolean crearDescuentoRebaja(String nombreProducto, double porcentaje, String descripcion,
                                       LocalDateTime desde, LocalDateTime hasta) {
        try {
            // Buscar el producto
            NewProduct base = buscarProductoEnCatalogo(nombreProducto);
            
            if (!(base instanceof Product)) {
                JOptionPane.showMessageDialog(panel, 
                    "❌ Producto no encontrado o es un Pack.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            Product prod = (Product) base;
            
            if (porcentaje <= 0 || porcentaje >= 100) {
                JOptionPane.showMessageDialog(panel, 
                    "❌ El porcentaje debe estar entre 0 y 100.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            IRebaja rebaja = new PercentageDiscount(porcentaje, descripcion, desde, hasta);
            prod.setDiscount(rebaja);
            Application.addDiscount((Discount) rebaja);
            
            JOptionPane.showMessageDialog(panel, 
                "✅ Rebaja del " + String.format("%.1f%%", porcentaje) + " aplicada a '" + nombreProducto + "'.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, 
                "❌ Error al crear rebaja: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Crear descuento por volumen
     */
    public boolean crearDescuentoVolumen(double gastoMinimo, double descuentoEuro, String descripcion,
                                        LocalDateTime desde, LocalDateTime hasta) {
        try {
            if (gastoMinimo <= 0) {
                JOptionPane.showMessageDialog(panel, 
                    "❌ El gasto mínimo debe ser mayor a 0.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (descuentoEuro <= 0) {
                JOptionPane.showMessageDialog(panel, 
                    "❌ El descuento en euros debe ser mayor a 0.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            IVolumen vol = new VolumeDiscount(descuentoEuro, gastoMinimo, descripcion, desde, hasta);
            Application.addDiscount((Discount) vol);
            
            JOptionPane.showMessageDialog(panel, 
                "✅ Descuento por volumen creado:\n" +
                "Gasto mínimo: €" + String.format("%.2f", gastoMinimo) + "\n" +
                "Descuento: €" + String.format("%.2f", descuentoEuro), 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, 
                "❌ Error al crear descuento por volumen: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Crear descuento de regalo
     */
    public boolean crearDescuentoRegalo(double gastoMinimo, String nombreProductoRegalo, 
                                       String descripcion, LocalDateTime desde, LocalDateTime hasta) {
        try {
            NewProduct pRegalo = buscarProductoEnCatalogo(nombreProductoRegalo);
            
            if (pRegalo == null) {
                JOptionPane.showMessageDialog(panel, 
                    "❌ El producto para regalo no existe.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (gastoMinimo <= 0) {
                JOptionPane.showMessageDialog(panel, 
                    "❌ El gasto mínimo debe ser mayor a 0.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            IRegalo gift = new GiftDiscount(gastoMinimo, pRegalo, descripcion, desde, hasta);
            Application.addDiscount((Discount) gift);
            
            JOptionPane.showMessageDialog(panel, 
                "✅ Promoción de regalo creada:\n" +
                "Gasto mínimo: €" + String.format("%.2f", gastoMinimo) + "\n" +
                "Regalo: " + pRegalo.getName(), 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, 
                "❌ Error al crear promoción de regalo: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Crear descuento por cantidad
     */
    public boolean crearDescuentoCantidad(String nombreProducto, int lleva, int paga, 
                                         String descripcion, LocalDateTime desde, LocalDateTime hasta) {
        try {
            NewProduct pCant = buscarProductoEnCatalogo(nombreProducto);
            
            if (!(pCant instanceof Product)) {
                JOptionPane.showMessageDialog(panel, 
                    "❌ Producto no encontrado o es un Pack.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (lleva <= 0 || paga <= 0) {
                JOptionPane.showMessageDialog(panel, 
                    "❌ Las cantidades deben ser mayores a 0.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (paga >= lleva) {
                JOptionPane.showMessageDialog(panel, 
                    "❌ La cantidad a pagar debe ser menor a la cantidad a llevar.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            ICantidad promo = new QuantityDiscount(lleva, paga, descripcion, desde, hasta);
            ((Product) pCant).setDiscount(promo);
            Application.addDiscount((Discount) promo);
            
            JOptionPane.showMessageDialog(panel, 
                "✅ Promoción de cantidad creada:\n" +
                "Lleva " + lleva + " paga " + paga, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, 
                "❌ Error al crear promoción de cantidad: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Eliminar un descuento
     */
    public boolean eliminarDescuento(IDiscount descuento) {
        try {
            // Eliminar de productos si aplica
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
            
            // Eliminar de la lista global
            ArrayList<IDiscount> globales = Application.getGlobalDiscounts();
            globales.remove(descuento);
            
            JOptionPane.showMessageDialog(panel, 
                "✅ Descuento eliminado correctamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            Application.guardarDatos("rongero_data.dat");
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, 
                "❌ Error al eliminar descuento: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Método auxiliar que busca un producto en el catálogo por su nombre
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
     * Obtener lista de nombres de productos para combobox
     */
    public ArrayList<String> obtenerNombresProductos() {
        ArrayList<String> nombres = new ArrayList<>();
        for (NewProduct p : Application.getCatalog()) {
            nombres.add(p.getName());
        }
        return nombres;
    }

    /**
     * Obtener lista de nombres de productos individuales (no packs)
     */
    public ArrayList<String> obtenerNombresProductosIndividuales() {
        ArrayList<String> nombres = new ArrayList<>();
        for (NewProduct p : Application.getCatalog()) {
            if (p instanceof Product) {
                nombres.add(p.getName());
            }
        }
        return nombres;
    }
}
