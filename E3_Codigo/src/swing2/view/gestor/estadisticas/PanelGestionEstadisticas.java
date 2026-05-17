package swing2.view.gestor.estadisticas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import swing2.controller.gestor.GestorEstadisticasController;
import swing2.view.VentanaPrincipa;
import java.util.List;

/**
 * Panel de Estadísticas del Gestor - RF-1.6
 * Colores lisos, paneles redondeados, datos 100% reales
 */
public class PanelGestionEstadisticas extends JPanel {
    private VentanaPrincipa ventanaPadre;
    private GestorEstadisticasController ctrl;
    
    // COLORES LISOS
    private static final Color COLOR_FONDO = new Color(23, 48, 79);
    private static final Color COLOR_PANEL_1 = new Color(41, 128, 185);      // Azul
    private static final Color COLOR_PANEL_2 = new Color(39, 174, 96);       // Verde
    private static final Color COLOR_PANEL_3 = new Color(230, 126, 34);      // Naranja
    private static final Color COLOR_TEXT = Color.WHITE;
    private static final Color COLOR_TEXT_DIM = new Color(189, 195, 199);
    
    // FUENTES
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 24);
    private static final Font FUENTE_SUBTITULO = new Font("Arial", Font.BOLD, 16);
    private static final Font FUENTE_NUMERO = new Font("Arial", Font.BOLD, 42);
    private static final Font FUENTE_TEXTO = new Font("Arial", Font.PLAIN, 14);
    
    public PanelGestionEstadisticas(VentanaPrincipa ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
        this.ctrl = new GestorEstadisticasController(this);
        
        this.setLayout(new BorderLayout());
        this.setBackground(COLOR_FONDO);
        this.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Encabezado
        JLabel titulo = new JLabel("ESTADÍSTICAS");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXT);
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new FlowLayout(FlowLayout.LEFT));
        header.add(titulo);
        header.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        this.add(header, BorderLayout.NORTH);
        
        // Contenido principal
        JPanel contenido = crearContenidoPrincipal();
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        this.add(scroll, BorderLayout.CENTER);
    }
    
    private JPanel crearContenidoPrincipal() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        // SECCIÓN 1: Recaudación (Ventas vs Valoración)
        panel.add(crearSeccion(
            "RECAUDACIÓN",
            crearPanelRecaudacion()
        ));
        panel.add(Box.createVerticalStrut(30));
        
        // SECCIÓN 2: Productos más vendidos
        panel.add(crearSeccion(
            "PRODUCTOS MÁS VENDIDOS",
            crearPanelProductosMasVendidos()
        ));
        panel.add(Box.createVerticalStrut(30));
        
        // SECCIÓN 3: Usuarios más activos
        panel.add(crearSeccion(
            "USUARIOS MÁS ACTIVOS",
            crearPanelUsuariosMasActivos()
        ));
        
        return panel;
    }
    
    private JPanel crearSeccion(String titulo, JPanel contenido) {
        JPanel seccion = new JPanel();
        seccion.setOpaque(false);
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        seccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FUENTE_SUBTITULO);
        lblTitulo.setForeground(COLOR_TEXT);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contenido.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        seccion.add(lblTitulo);
        seccion.add(Box.createVerticalStrut(15));
        seccion.add(contenido);
        
        return seccion;
    }
    
 // PANEL 1: RECAUDACIÓN
    private JPanel crearPanelRecaudacion() {
        // Mantenemos las columnas y el espacio de 25 entre ellas
        JPanel panel = new JPanel(new GridLayout(1, 2, 25, 0));
        panel.setOpaque(false);

        panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, 130));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        
        // Ingresos totales
        double ingresos = ctrl.calcularIngresosTotales();
        panel.add(crearTarjetaMetrica(
            "INGRESOS (Ventas)",
            String.format("€%.2f", ingresos),
            "De pedidos completados",
            COLOR_PANEL_1
        ));
        
        // Valor estimado de tasaciones
        double valoracion = ctrl.calcularValoracionEstimado();
        panel.add(crearTarjetaMetrica(
            "VALORACIÓN (Segunda Mano)",
            String.format("€%.2f", valoracion),
            "Valor estimado de productos",
            COLOR_PANEL_2
        ));
        
        return panel;
    }
    
 // PANEL 2: PRODUCTOS MÁS VENDIDOS
    private JPanel crearPanelProductosMasVendidos() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        List<String> productos = ctrl.obtenerProductosMasVendidos();
        
        if (productos.isEmpty()) {
            JLabel lblVacio = new JLabel("Sin datos de ventas aún");
            lblVacio.setFont(FUENTE_TEXTO);
            lblVacio.setForeground(COLOR_TEXT_DIM);
            panel.add(lblVacio);
        } else {
            for (int i = 0; i < productos.size(); i++) {
                panel.add(crearFilaRanking(i + 1, productos.get(i)));
                if (i < productos.size() - 1) {
                    panel.add(Box.createVerticalStrut(12));
                }
            }
            // AJUSTE DE TAMAÑO: Calculamos la altura justa según las filas (50px cada una + 12px de separación)
            int alturaTotal = (productos.size() * 50) + ((productos.size() - 1) * 12);
            panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, alturaTotal));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, alturaTotal));
        }
        
        return panel;
    }
    
 // PANEL 3: USUARIOS MÁS ACTIVOS
    private JPanel crearPanelUsuariosMasActivos() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        List<String> usuarios = ctrl.obtenerClientesMasActivos();
        
        if (usuarios.isEmpty()) {
            JLabel lblVacio = new JLabel("Sin datos de usuarios aún");
            lblVacio.setFont(FUENTE_TEXTO);
            lblVacio.setForeground(COLOR_TEXT_DIM);
            panel.add(lblVacio);
        } else {
            for (int i = 0; i < usuarios.size(); i++) {
                panel.add(crearFilaRanking(i + 1, usuarios.get(i)));
                if (i < usuarios.size() - 1) {
                    panel.add(Box.createVerticalStrut(12));
                }
            }
            // AJUSTE DE TAMAÑO: Calculamos la altura justa según las filas (50px cada una + 12px de separación)
            int alturaTotal = (usuarios.size() * 50) + ((usuarios.size() - 1) * 12);
            panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, alturaTotal));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, alturaTotal));
        }
        
        return panel;
    }
    
// COMPONENTES AUXILIARES
    
    private JPanel crearTarjetaMetrica(String titulo, String valor, String descripcion, Color color) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Antialiasing para suavizar los bordes redondeados
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                // fillRoundRect recibe: x, y, width, height, arcWidth, arcHeight
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); 
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // AJUSTE DE TAMAÑO: Forzamos la altura a 130 para que coincida con el contenedor de Recaudación
        card.setPreferredSize(new Dimension(card.getPreferredSize().width, 130));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FUENTE_TEXTO);
        lblTitulo.setForeground(COLOR_TEXT_DIM);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(FUENTE_NUMERO);
        lblValor.setForeground(COLOR_TEXT);
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(FUENTE_TEXTO);
        lblDesc.setForeground(COLOR_TEXT_DIM);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(8));
        card.add(lblValor);
        card.add(Box.createVerticalStrut(5));
        card.add(lblDesc);
        
        return card;
    }
    
    private JPanel crearFilaRanking(int posicion, String contenido) {
        JPanel fila = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Antialiasing para suavizar los bordes redondeados
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_PANEL_3);
                // Un radio un poco menor (15) para las filas más pequeñas
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        fila.setOpaque(false);
        fila.setLayout(new BorderLayout());
        fila.setBorder(new EmptyBorder(12, 15, 12, 15));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        // Posición
        JLabel lblPos = new JLabel(String.valueOf(posicion));
        lblPos.setFont(new Font("Arial", Font.BOLD, 18));
        lblPos.setForeground(COLOR_TEXT);
        lblPos.setPreferredSize(new Dimension(40, 40));
        lblPos.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Contenido
        JLabel lblContenido = new JLabel(contenido);
        lblContenido.setFont(FUENTE_TEXTO);
        lblContenido.setForeground(COLOR_TEXT);
        lblContenido.setBorder(new EmptyBorder(0, 15, 0, 0));
        
        fila.add(lblPos, BorderLayout.WEST);
        fila.add(lblContenido, BorderLayout.CENTER);
        
        return fila;
    }
    
    public void refrescar() {
        this.revalidate();
        this.repaint();
    }
}