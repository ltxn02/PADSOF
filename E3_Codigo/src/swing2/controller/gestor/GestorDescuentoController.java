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
 * Controlador encargado de gestionar las promociones y la política de descuentos de la tienda.
 * Implementa el patrón MVC separando la lógica operacional de la persistencia de datos 
 * frente a los componentes visuales de administración de ofertas.
 * 
 * @author Lidia Martín
 */
public class GestorDescuentoController {
    private VentanaPrincipa ventana;
    private PanelGestionDescuentos panel;

    /**
     * Constructor de la clase GestorDescuentoController.
     * Vincula el controlador con el flujo de la ventana principal y el panel administrador de promociones.
     * 
     * @param ventana Ventana principal que coordina el estado global de la interfaz.
     * @param panel   Panel especializado en la gestión visual de los modelos de descuentos.
     */
    public GestorDescuentoController(VentanaPrincipa ventana, PanelGestionDescuentos panel) {
        this.ventana = ventana;
        this.panel = panel;
    }

    /**
     * Recupera del sistema de persistencia todos los descuentos de ámbito global configurados.
     * 
     * @return Un ArrayList con los objetos que implementan la interfaz IDiscount.
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
     * Filtra una lista base de ofertas basándose en la coincidencia parcial de texto.
     * Evalúa la descripción interna del descuento ignorando diferencias entre mayúsculas y minúsculas.
     * 
     * @param descuentosBase Estructura de origen que contiene el histórico total de ofertas.
     * @param termino        Cadena de caracteres o palabra clave de búsqueda.
     * @return Un nuevo ArrayList reducido con las ocurrencias que satisfacen el criterio.
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
     * Identifica el subtipo específico de descuento mapeándolo a una etiqueta de texto legible.
     * 
     * @param desc Instancia genérica del descuento a identificar.
     * @return Cadena informativa correspondiente a la implementación concreta del objeto.
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
     * Extrae el valor porcentual de las promociones basadas en reducciones fraccionales.
     * 
     * @param desc Instancia del descuento evaluado.
     * @return Representación textual formateada del porcentaje (ej. "15.0%") o "N/A" si no aplica.
     */
    public String obtenerPorcentaje(IDiscount desc) {
        if (desc instanceof PercentageDiscount) {
            PercentageDiscount pd = (PercentageDiscount) desc;
            return String.format("%.1f%%", pd.getValue());
        }
        return "N/A";
    }

    /**
     * Determina el alcance o categoría de aplicación asociada a la campaña del descuento provisto.
     * Rastrea de forma inversa el catálogo buscando qué artículos contienen vinculado dicho objeto.
     * 
     * @param desc Instancia del descuento a auditar.
     * @return El nombre del grupo de artículos afectado o el ámbito general del mismo en el carrito.
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
     * Compone una ficha técnica o desglose estructural con las propiedades internas del descuento.
     * Genera especificaciones detalladas dependiendo de las variables implícitas de cada subtipo 
     * (umbrales económicos, multiplicadores de cantidad o productos de obsequio).
     * 
     * @param desc El objeto de descuento del cual estructurar el informe.
     * @return Una cadena multilínea con los atributos detallados del elemento.
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
     * Parsea un valor textual cronológico bajo el formato de máscara "dd/MM/yyyy HH:mm".
     * 
     * @param fechaStr Cadena de entrada que representa el instante temporal.
     * @return El objeto LocalDateTime resultante.
     * @throws DateTimeParseException Si el texto no respeta la sintaxis de fecha u hora obligatoria.
     */
    private LocalDateTime parsearFecha(String fechaStr) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.parse(fechaStr, formatter);
    }

    /**
     * Valida la coherencia secuencial de un intervalo de tiempo.
     * 
     * @param desde Instancia temporal que demarca el inicio.
     * @param hasta Instancia temporal que demarca la finalización.
     * @return true si el límite superior es estrictamente posterior al de inicio; false de lo contrario.
     */
    private boolean validarFechas(LocalDateTime desde, LocalDateTime hasta) {
        return hasta.isAfter(desde);
    }

    /**
     * Da de alta y asocia de forma masiva un descuento de reducción porcentual a una categoría.
     * Valida rangos aritméticos lógicos, consistencia temporal de vigencia y altera las propiedades 
     * de los productos adscritos en el catálogo general guardando el estado final en disco.
     * 
     * @param descripcion Detalle enunciativo de la promoción.
     * @param categoria   Nombre identificativo del sector de productos afectado.
     * @param porcentaje  Tasa de reducción (rango abierto de 0 a 100).
     * @param fechaInicio Fecha de activación del descuento.
     * @param fechaFin    Fecha de expiración o cese del beneficio.
     * @return true si la operación se completó con éxito; false si falló alguna validación previa.
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
     * Da de alta un descuento por volumen de compra global aplicable al total neto facturado.
     * 
     * @param descripcion   Texto descriptivo con las condiciones de la oferta.
     * @param gastoMinimo   Cota monetaria inferior requerida para activar la ventaja.
     * @param descuentoEuro Importe directo en euros a deducir del balance de compra.
     * @param fechaInicio   Momento de inicio de validez legal.
     * @param fechaFin      Momento de término de validez legal.
     * @return true si la operación transaccional se consolida de forma correcta; false si incumple reglas.
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
     * Crea un incentivo promocional basado en obsequiar un artículo si se supera un umbral financiero.
     * 
     * @param descripcion    Etiqueta conceptual del incentivo de regalo.
     * @param gastoMinimo    Monto mínimo a satisfacer para obtener el beneficio.
     * @param productoRegalo Nombre identificativo del artículo del catálogo que se asignará sin coste.
     * @param fechaInicio    Instante inicial de la promoción.
     * @param fechaFin       Instante de caducidad programada.
     * @return true si la entidad se valida y asienta en los registros; false en caso contrario.
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
     * Configura y distribuye un descuento por lote de adquisición lineal ("Lleva X, Paga Y").
     * El beneficio se propaga de forma transversal a todos los productos agrupados en la categoría indicada.
     * 
     * @param descripcion Enunciado representativo de la oferta por lote.
     * @param categoria   Nombre de la familia de artículos receptora de la regla comercial.
     * @param lleva       Cantidad de artículos requeridos para la composición de la oferta.
     * @param paga        Fracción neta de artículos facturables de dicho conjunto.
     * @param fechaInicio Inicio de la vigencia.
     * @param fechaFin    Término de la vigencia.
     * @return true si se registra de forma unívoca y se actualizan los productos; false si falla.
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
     * Da de baja del sistema un descuento activo. 
     * Limpia las referencias directas presentes en los artículos del catálogo vinculados
     * y extrae el objeto del registro global antes de persistir los cambios.
     * 
     * @param descuento Instancia de la oferta a remover del sistema.
     * @return true si la eliminación concluye sin excepciones; false en caso de anomalía.
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
     * Método auxiliar de búsqueda interna de categorías.
     * Evaluado por comparación exacta sin discriminar mayúsculas.
     * 
     * @param nombre Denominación textual de la categoría requerida.
     * @return El objeto Category localizado o null si es inexistente.
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
     * Método auxiliar de búsqueda interna de productos del catálogo por coincidencia de nombre.
     * 
     * @param nombre Identificador textual descriptivo del producto a localizar.
     * @return El objeto NewProduct emparejado o null si no se halla en el catálogo.
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
     * Genera una colección indexada con los nombres de todas las categorías activas.
     * Se inyecta un registro por defecto para servir de cabecera neutral en selectores combobox.
     * 
     * @return ArrayList de String con el listado formateado de nombres.
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
     * Genera una colección indexada con la denominación comercial de todos los productos disponibles.
     * Diseñada para la carga de selectores interactivos en la vista de administración.
     * 
     * @return ArrayList de String con el listado nominal de productos.
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
     * Centraliza y despliega diálogos emergentes de advertencia ante entradas o lógicas erróneas.
     * 
     * @param mensaje Contenido explicativo del error a mostrar en la interfaz gráfica.
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(panel, 
            "❌ " + mensaje, 
            "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Centraliza y despliega diálogos emergentes informativos tras confirmar operaciones exitosas.
     * 
     * @param mensaje Contenido descriptivo del hito alcanzado.
     */
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(panel, 
            "✅ " + mensaje, 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
