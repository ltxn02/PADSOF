package swing2.view.gestor.descuentos;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import logic.Application;
import catalog.Category;
import discounts.*;

/**
 * Panel para crear un descuento específico del tipo ya seleccionado.
 * Adapta el formulario según el tipo: Rebaja, Volumen, Regalo o Cantidad.
 */
public class PanelAnadirDescuento extends JPanel {
    private PanelGestionDescuentos panelPadre;
    private int tipoDescuento;  // 0=Rebaja, 1=Volumen, 2=Regalo, 3=Cantidad
    
    // Componentes comunes
    private JTextField txtDescripcion;
    private JComboBox<String> comboCategoria;
    private JSpinner spinFechaDesde, spinFechaHasta;
    
    // Componentes específicos Rebaja
    private JSpinner spinPorcentaje;
    
    // Componentes específicos Volumen
    private JSpinner spinGastoMinimo;
    private JSpinner spinDescuentoEuro;
    
    // Componentes específicos Regalo
    private JComboBox<String> comboProductoRegalo;
    
    // Componentes específicos Cantidad
    private JSpinner spinLleva;
    private JSpinner spinPaga;
    
    // Panel dinámico para componentes específicos
    private JPanel panelEspecifico;
    
    // COLORES
    private static final Color COLOR_FONDO = new Color(23, 48, 79);
    private static final Color COLOR_PANEL_FORM = new Color(40, 80, 140);
    private static final Color COLOR_LABEL = new Color(187, 192, 199);
    
    public PanelAnadirDescuento(PanelGestionDescuentos panelPadre, int tipoDescuento) {
        this.panelPadre = panelPadre;
        this.tipoDescuento = tipoDescuento;
        
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(COLOR_FONDO);
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // === BARRA SUPERIOR ===
        JPanel barraSuperior = crearBarraSuperior();
        this.add(barraSuperior, BorderLayout.NORTH);
        
        // === CONTENIDO PRINCIPAL (SCROLL) ===
        JPanel contenidoPrincipal = crearContenidoPrincipal();
        JScrollPane scroll = new JScrollPane(contenidoPrincipal);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scroll, BorderLayout.CENTER);
        
        // === BARRA INFERIOR ===
        JPanel barraInferior = crearBarraInferior();
        this.add(barraInferior, BorderLayout.SOUTH);
    }
    
    // ========== BARRA SUPERIOR ==========
    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COLOR_FONDO);
        barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JButton btnVolver = new JButton("< Volver");
        btnVolver.setPreferredSize(new Dimension(150, 35));
        btnVolver.setBackground(new Color(52, 73, 94));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 12));
        btnVolver.setBorder(null);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> panelPadre.mostrarSeleccionTipo());
        
        JLabel titulo = new JLabel("CREAR NUEVO DESCUENTO: " + obtenerNombreTipo());
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        
        barra.add(btnVolver, BorderLayout.WEST);
        barra.add(titulo, BorderLayout.CENTER);
        
        return barra;
    }
    
    // ========== CONTENIDO PRINCIPAL ==========
    private JPanel crearContenidoPrincipal() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // SECCIÓN 1: Información General
        JPanel seccion1 = crearSeccion("INFORMACIÓN GENERAL");
        txtDescripcion = crearCampoTexto("Descripción de la oferta:");
        seccion1.add(crearFilaConCampo("Descripción:", txtDescripcion));
        seccion1.add(Box.createVerticalStrut(10));
        
        // Categoría (solo si no es Regalo)
        if (tipoDescuento != 2) {  // 2 = Regalo
            comboCategoria = crearComboCategoria();
            seccion1.add(crearFilaConCombo("Categoría aplicable:", comboCategoria));
        }
        
        panel.add(seccion1);
        panel.add(Box.createVerticalStrut(20));
        
        // SECCIÓN 2: Parámetros Específicos
        panelEspecifico = crearSeccion("PARÁMETROS ESPECÍFICOS");
        cargarComponentesEspecificos();
        panel.add(panelEspecifico);
        panel.add(Box.createVerticalStrut(20));
        
        // SECCIÓN 3: Vigencia
        JPanel seccion3 = crearSeccion("VIGENCIA");
        spinFechaDesde = new JSpinner(new SpinnerDateModel());
        spinFechaHasta = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorDesde = new JSpinner.DateEditor(spinFechaDesde, "dd/MM/yyyy HH:mm");
        JSpinner.DateEditor editorHasta = new JSpinner.DateEditor(spinFechaHasta, "dd/MM/yyyy HH:mm");
        spinFechaDesde.setEditor(editorDesde);
        spinFechaHasta.setEditor(editorHasta);
        
        seccion3.add(crearFilaConSpinner("Válido desde:", spinFechaDesde));
        seccion3.add(Box.createVerticalStrut(10));
        seccion3.add(crearFilaConSpinner("Válido hasta:", spinFechaHasta));
        
        panel.add(seccion3);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    // ========== CARGAR COMPONENTES ESPECÍFICOS SEGÚN TIPO ==========
    private void cargarComponentesEspecificos() {
        panelEspecifico.removeAll();
        
        JLabel titulo = new JLabel(obtenerNombreTipo());
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEspecifico.add(titulo);
        panelEspecifico.add(Box.createVerticalStrut(10));
        
        switch (tipoDescuento) {
            case 0:  // Rebaja %
                spinPorcentaje = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 100.0, 0.5));
                panelEspecifico.add(crearFilaConSpinner("Porcentaje descuento (%):", spinPorcentaje));
                break;
                
            case 1:  // Volumen €
                spinGastoMinimo = new JSpinner(new SpinnerNumberModel(50.0, 0.0, 10000.0, 5.0));
                spinDescuentoEuro = new JSpinner(new SpinnerNumberModel(10.0, 0.0, 10000.0, 1.0));
                panelEspecifico.add(crearFilaConSpinner("Gasto mínimo (€):", spinGastoMinimo));
                panelEspecifico.add(Box.createVerticalStrut(10));
                panelEspecifico.add(crearFilaConSpinner("Descuento (€):", spinDescuentoEuro));
                break;
                
            case 2:  // Regalo
                comboProductoRegalo = crearComboProductos();
                panelEspecifico.add(crearFilaConCombo("Producto regalo:", comboProductoRegalo));
                panelEspecifico.add(Box.createVerticalStrut(10));
                spinGastoMinimo = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 10000.0, 5.0));
                panelEspecifico.add(crearFilaConSpinner("Gasto mínimo para regalo (€):", spinGastoMinimo));
                break;
                
            case 3:  // Cantidad X×Y
                spinLleva = new JSpinner(new SpinnerNumberModel(3, 1, 100, 1));
                spinPaga = new JSpinner(new SpinnerNumberModel(2, 1, 100, 1));
                panelEspecifico.add(crearFilaConSpinner("Lleva (X):", spinLleva));
                panelEspecifico.add(Box.createVerticalStrut(10));
                panelEspecifico.add(crearFilaConSpinner("Paga (Y):", spinPaga));
                break;
        }
        
        panelEspecifico.revalidate();
        panelEspecifico.repaint();
    }
    
    // ========== COMPONENTES AUXILIARES ==========
    private JPanel crearSeccion(String titulo) {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setBackground(COLOR_PANEL_FORM);
        seccion.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        seccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        seccion.add(lblTitulo);
        seccion.add(Box.createVerticalStrut(10));
        
        return seccion;
    }
    
    private JTextField crearCampoTexto(String placeholder) {
        JTextField txt = new JTextField(placeholder);
        txt.setPreferredSize(new Dimension(300, 28));
        txt.setFont(new Font("Arial", Font.PLAIN, 12));
        return txt;
    }
    
    private JPanel crearFilaConCampo(String etiqueta, JTextField campo) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(COLOR_LABEL);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setPreferredSize(new Dimension(180, 25));
        
        fila.add(lbl);
        fila.add(campo);
        
        return fila;
    }
    
    private JPanel crearFilaConCombo(String etiqueta, JComboBox<?> combo) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(COLOR_LABEL);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setPreferredSize(new Dimension(180, 25));
        
        combo.setPreferredSize(new Dimension(300, 28));
        
        fila.add(lbl);
        fila.add(combo);
        
        return fila;
    }
    
    private JPanel crearFilaConSpinner(String etiqueta, JSpinner spinner) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(COLOR_LABEL);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setPreferredSize(new Dimension(180, 25));
        
        spinner.setPreferredSize(new Dimension(120, 28));
        
        fila.add(lbl);
        fila.add(spinner);
        
        return fila;
    }
    
    private JComboBox<String> crearComboCategoria() {
        ArrayList<Category> categorias = Application.getGlobalCategories();
        ArrayList<String> nombres = new ArrayList<>();
        nombres.add("- Seleccionar categoría -");
        for (Category c : categorias) {
            nombres.add(c.getNameCategory());
        }
        return new JComboBox<>(nombres.toArray(new String[0]));
    }
    
    private JComboBox<String> crearComboProductos() {
        ArrayList<String> productos = new ArrayList<>();
        productos.add("- Seleccionar producto regalo -");
        // Aquí puedes obtener productos del Application si es necesario
        return new JComboBox<>(productos.toArray(new String[0]));
    }
    
    // ========== BARRA INFERIOR ==========
    private JPanel crearBarraInferior() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        barra.setBackground(COLOR_FONDO);
        
        JButton btnCrear = new JButton("✓ CREAR DESCUENTO");
        btnCrear.setPreferredSize(new Dimension(200, 40));
        btnCrear.setBackground(new Color(46, 204, 113));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFont(new Font("Arial", Font.BOLD, 14));
        btnCrear.setBorder(null);
        btnCrear.setFocusPainted(false);
        btnCrear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCrear.addActionListener(e -> crearDescuento());
        
        JButton btnCancelar = new JButton("✕ CANCELAR");
        btnCancelar.setPreferredSize(new Dimension(200, 40));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setBorder(null);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> panelPadre.mostrarListado());
        
        barra.add(btnCrear);
        barra.add(btnCancelar);
        
        return barra;
    }
    
    // ========== CREAR DESCUENTO ==========
    private void crearDescuento() {
        String descripcion = txtDescripcion.getText().trim();
        
        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            LocalDateTime fechaDesde = new java.sql.Timestamp(((java.util.Date) spinFechaDesde.getValue()).getTime()).toLocalDateTime();
            LocalDateTime fechaHasta = new java.sql.Timestamp(((java.util.Date) spinFechaHasta.getValue()).getTime()).toLocalDateTime();
            
            IDiscountFactory factory = new StandardDiscountFactory();
            Discount nuevoDescuento = null;
            
            switch (tipoDescuento) {
                case 0:  // Rebaja %
                    if (comboCategoria.getSelectedIndex() <= 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una categoría.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    double porcentaje = (Double) spinPorcentaje.getValue();
                    IRebaja rebaja = factory.createPercentageDiscount(porcentaje, descripcion);
                    Application.addDiscount((Discount) rebaja);
                    nuevoDescuento = (Discount) rebaja;
                    break;
                    
                case 1:  // Volumen €
                    if (comboCategoria.getSelectedIndex() <= 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una categoría.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    double gastoMin = (Double) spinGastoMinimo.getValue();
                    double descuentoEuro = (Double) spinDescuentoEuro.getValue();
                    IVolumen volumen = factory.createVolumeDiscount(gastoMin, descuentoEuro, descripcion);
                    Application.addDiscount((Discount) volumen);
                    nuevoDescuento = (Discount) volumen;
                    break;
                    
                case 2:  // Regalo
                    if (comboProductoRegalo.getSelectedIndex() <= 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona un producto regalo.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    double gastoMinRegalo = (Double) spinGastoMinimo.getValue();
                    // Aquí necesitarías obtener el producto seleccionado
                    JOptionPane.showMessageDialog(this, "Tipo Regalo: próximamente en desarrollo", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                    
                case 3:  // Cantidad X×Y
                    if (comboCategoria.getSelectedIndex() <= 0) {
                        JOptionPane.showMessageDialog(this, "Selecciona una categoría.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int llevax = (Integer) spinLleva.getValue();
                    int pagay = (Integer) spinPaga.getValue();
                    if (pagay >= llevax) {
                        JOptionPane.showMessageDialog(this, "Debes pagar menos de lo que llevas.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ICantidad cantidad = factory.createQuantityDiscount(llevax, pagay, descripcion);
                    Application.addDiscount((Discount) cantidad);
                    nuevoDescuento = (Discount) cantidad;
                    break;
            }
            
            if (nuevoDescuento != null) {
                JOptionPane.showMessageDialog(this, "✓ Descuento creado exitosamente: " + descripcion, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                panelPadre.mostrarListado();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear descuento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // ========== UTILIDADES ==========
    private String obtenerNombreTipo() {
        switch (tipoDescuento) {
            case 0: return "REBAJA PORCENTUAL";
            case 1: return "DESCUENTO POR VOLUMEN";
            case 2: return "REGALO PROMOCIONAL";
            case 3: return "OFERTA POR CANTIDAD";
            default: return "DESCUENTO";
        }
    }
}
