package swing2.view.gestor.estadisticas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import logic.Application;
import swing2.controller.gestor.GestorEstadisticasController;
import swing2.view.VentanaPrincipa;
import users.*;

/**
 * Panel de Estadísticas del Gestor - SIMPLIFICADO
 */
public class PanelGestionEstadisticas extends JPanel {
    private VentanaPrincipa ventanaPadre;
    private GestorEstadisticasController ctrl;
    
    // === COLORES ===
    private static final Color COLOR_FONDO = new Color(23, 48, 79);
    private static final Color COLOR_CARD_1 = new Color(30, 70, 120);
    private static final Color COLOR_CARD_2 = new Color(50, 100, 150);
    private static final Color COLOR_ACCENT_BLUE = new Color(0, 180, 255);
    private static final Color COLOR_ACCENT_GREEN = new Color(100, 220, 100);
    private static final Color COLOR_ACCENT_ORANGE = new Color(255, 150, 50);
    private static final Color COLOR_TEXT_DIM = new Color(140, 150, 190);
    
    // === FUENTES ===
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 20);
    private static final Font FUENTE_SUBTITULO = new Font("Arial", Font.BOLD, 14);
    private static final Font FUENTE_NUMERO = new Font("Arial", Font.BOLD, 28);
    private static final Font FUENTE_ETIQUETA = new Font("Arial", Font.PLAIN, 12);
    
    public PanelGestionEstadisticas(VentanaPrincipa ventanaPadre) {
        this.ventanaPadre = ventanaPadre;
        this.ctrl = new GestorEstadisticasController(this);
        
        this.setLayout(new BorderLayout(0, 0));
        this.setBackground(COLOR_FONDO);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Encabezado
        JPanel panelHeader = crearPanelEncabezado();
        this.add(panelHeader, BorderLayout.NORTH);
        
        // Contenido scrolleable
        JPanel panelPrincipal = crearPanelPrincipal();
        JScrollPane scroll = new JScrollPane(panelPrincipal);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        this.add(scroll, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelEncabezado() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel titulo = new JLabel("PANEL DE ESTADÍSTICAS");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_ACCENT_BLUE);
        
        JLabel subtitulo = new JLabel("Resumen del negocio");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setForeground(COLOR_TEXT_DIM);
        
        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);
        
        panel.add(textos, BorderLayout.WEST);
        return panel;
    }
    
    private JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // KPIs principales
        panel.add(crearFilaKPIs());
        panel.add(Box.createVerticalStrut(40));
        
        // Usuarios
        panel.add(crearTituloBloqueSeccion("👥 USUARIOS"));
        panel.add(Box.createVerticalStrut(15));
        panel.add(crearFilaUsuarios());
        panel.add(Box.createVerticalStrut(40));
        
        // Productos
        panel.add(crearTituloBloqueSeccion("📦 PRODUCTOS"));
        panel.add(Box.createVerticalStrut(15));
        panel.add(crearFilaProductos());
        
        return panel;
    }
    
    private JLabel crearTituloBloqueSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_SUBTITULO);
        lbl.setForeground(COLOR_ACCENT_BLUE);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
    
    // ========== KPIs PRINCIPALES ==========
    private JPanel crearFilaKPIs() {
        JPanel fila = new JPanel(new GridLayout(1, 4, 20, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        
        int totalUsuarios = Application.getUsers().size();
        int totalClientes = (int) Application.getUsers().stream()
            .filter(u -> u instanceof Client).count();
        int totalEmpleados = (int) Application.getUsers().stream()
            .filter(u -> u instanceof Employee).count();
        int totalGestores = (int) Application.getUsers().stream()
            .filter(u -> u instanceof Manager).count();
        
        fila.add(crearTarjetaKPI("USUARIOS", String.valueOf(totalUsuarios), COLOR_ACCENT_BLUE));
        fila.add(crearTarjetaKPI("CLIENTES", String.valueOf(totalClientes), COLOR_ACCENT_GREEN));
        fila.add(crearTarjetaKPI("EMPLEADOS", String.valueOf(totalEmpleados), COLOR_ACCENT_ORANGE));
        fila.add(crearTarjetaKPI("GESTORES", String.valueOf(totalGestores), new Color(200, 100, 255)));
        
        return fila;
    }
    
    private JPanel crearTarjetaKPI(String titulo, String valor, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gp = new GradientPaint(0, 0, COLOR_CARD_1, getWidth(), getHeight(), COLOR_CARD_2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(accentColor);
                g2.setStroke(new BasicStroke(4));
                g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FUENTE_ETIQUETA);
        lblTitulo.setForeground(COLOR_TEXT_DIM);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(FUENTE_NUMERO);
        lblValor.setForeground(accentColor);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(10));
        card.add(lblValor);
        
        return card;
    }
    
    // ========== SECCIÓN USUARIOS ==========
    private JPanel crearFilaUsuarios() {
        JPanel fila = new JPanel(new GridLayout(1, 2, 20, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Clientes con pedidos
        int clientesConPedidos = (int) Application.getUsers().stream()
            .filter(u -> u instanceof Client)
            .filter(u -> !((Client) u).getOrders().isEmpty())
            .count();
        
        fila.add(crearTarjetaInfo("Clientes Activos", String.valueOf(clientesConPedidos), COLOR_ACCENT_GREEN));
        
        // Total de pedidos
        int totalPedidos = (int) Application.getUsers().stream()
            .filter(u -> u instanceof Client)
            .mapToInt(u -> ((Client) u).getOrders().size())
            .sum();
        
        fila.add(crearTarjetaInfo("Pedidos Totales", String.valueOf(totalPedidos), COLOR_ACCENT_BLUE));
        
        return fila;
    }
    
    // ========== SECCIÓN PRODUCTOS ==========
    private JPanel crearFilaProductos() {
        JPanel fila = new JPanel(new GridLayout(1, 3, 20, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        int productosNuevos = Application.getCatalog().size();
        fila.add(crearTarjetaInfo("Productos", String.valueOf(productosNuevos), COLOR_ACCENT_BLUE));
        
        int productosSegundaMano = Application.getSecondHandProducts().size();
        fila.add(crearTarjetaInfo("Segunda Mano", String.valueOf(productosSegundaMano), COLOR_ACCENT_ORANGE));
        
        int categorias = Application.getGlobalCategories().size();
        fila.add(crearTarjetaInfo("Categorías", String.valueOf(categorias), COLOR_ACCENT_GREEN));
        
        return fila;
    }
    
    private JPanel crearTarjetaInfo(String titulo, String valor, Color color) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(COLOR_CARD_1);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                
                g2.setColor(color);
                g2.fillRect(0, 0, getWidth(), 3);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FUENTE_ETIQUETA);
        lblTitulo.setForeground(COLOR_TEXT_DIM);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 26));
        lblValor.setForeground(color);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(10));
        card.add(lblValor);
        
        return card;
    }
    
    public void refrescar() {
        this.revalidate();
        this.repaint();
    }
}