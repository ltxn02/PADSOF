package swing2.view.gestor.descuentos;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de selección visual del tipo de descuento a crear.
 * Presenta 4 opciones de descuento con descripción y botones.
 */
public class PanelGestorSeleccionTipoDescuento extends JPanel {
    private PanelGestionDescuentos panelPadre;
    private int tipoSeleccionado = -1;  // -1 = no seleccionado
    
    // Tipos de descuento
    private static final int TIPO_REBAJA = 0;
    private static final int TIPO_VOLUMEN = 1;
    private static final int TIPO_REGALO = 2;
    private static final int TIPO_CANTIDAD = 3;
    
    // COLORES
    private static final Color COLOR_FONDO = new Color(23, 48, 79);
    private static final Color COLOR_CARD = new Color(40, 80, 140);
    private static final Color COLOR_CARD_HOVER = new Color(60, 110, 170);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Color COLOR_DESCRIPCION = new Color(187, 192, 199);
    
    public PanelGestorSeleccionTipoDescuento(PanelGestionDescuentos panelPadre) {
        this.panelPadre = panelPadre;
        
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(COLOR_FONDO);
        this.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // === BARRA SUPERIOR ===
        JPanel barraSuperior = crearBarraSuperior();
        this.add(barraSuperior, BorderLayout.NORTH);
        
        // === CONTENIDO PRINCIPAL: Grid 2x2 con tarjetas ===
        JPanel contenidoPrincipal = crearContenidoPrincipal();
        this.add(contenidoPrincipal, BorderLayout.CENTER);
        
        // === BARRA INFERIOR: Botones ===
        JPanel barraInferior = crearBarraInferior();
        this.add(barraInferior, BorderLayout.SOUTH);
    }
    
    // ========== BARRA SUPERIOR ==========
    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barra.setOpaque(false);
        
        // Botón volver
        JButton btnVolver = new JButton("< Volver");
        btnVolver.setPreferredSize(new Dimension(150, 35));
        btnVolver.setBackground(new Color(52, 73, 94));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 12));
        btnVolver.setBorder(null);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> panelPadre.mostrarListado());
        
        barra.add(btnVolver);
        
        return barra;
    }
    
    // ========== CONTENIDO PRINCIPAL ==========
    private JPanel crearContenidoPrincipal() {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new GridBagLayout());
        contenedor.setOpaque(false);
        
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(15, 15, 15, 15);
        g.fill = GridBagConstraints.BOTH;
        g.weightx = 1.0;
        g.weighty = 1.0;
        
        // Fila 1
        g.gridx = 0; g.gridy = 0;
        contenedor.add(crearTarjetaDescuento(
            "REBAJA PORCENTUAL",
            "Descuento del X% en productos\nde una categoría específica",
            TIPO_REBAJA
        ), g);
        
        g.gridx = 1; g.gridy = 0;
        contenedor.add(crearTarjetaDescuento(
            "DESCUENTO POR VOLUMEN",
            "Descuento en €€ si el carrito\nsupera un gasto mínimo",
            TIPO_VOLUMEN
        ), g);
        
        // Fila 2
        g.gridx = 0; g.gridy = 1;
        contenedor.add(crearTarjetaDescuento(
            "REGALO PROMOCIONAL",
            "Regala un producto si el carrito\nsupera un gasto mínimo",
            TIPO_REGALO
        ), g);
        
        g.gridx = 1; g.gridy = 1;
        contenedor.add(crearTarjetaDescuento(
            "OFERTA POR CANTIDAD",
            "Oferta X por Y: compra X productos\ny paga solo por Y unidades",
            TIPO_CANTIDAD
        ), g);
        
        return contenedor;
    }
    
    // ========== TARJETA DE DESCUENTO ==========
    private JPanel crearTarjetaDescuento(String titulo, String descripcion, int tipo) {
        JPanel tarjeta = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Borde redondeado
                g2d.setColor(tipoSeleccionado == tipo ? new Color(100, 200, 255) : COLOR_CARD);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                // Borde más grueso si está seleccionado
                if (tipoSeleccionado == tipo) {
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(new Color(100, 200, 255));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                }
            }
        };
        
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setOpaque(false);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Título
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(15));
        
        // Descripción
        JLabel lblDesc = new JLabel("<html><center>" + descripcion.replace("\n", "<br>") + "</center></html>");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(COLOR_DESCRIPCION);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblDesc);
        tarjeta.add(Box.createVerticalGlue());
        
        // Evento: Click para seleccionar
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                tipoSeleccionado = tipo;
                PanelGestorSeleccionTipoDescuento.this.repaint();
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                tarjeta.setBackground(COLOR_CARD_HOVER);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                tarjeta.setBackground(COLOR_CARD);
            }
        });
        
        return tarjeta;
    }
    
    // ========== BARRA INFERIOR ==========
    private JPanel crearBarraInferior() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        barra.setBackground(COLOR_FONDO);
        barra.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Botón continuar
        JButton btnContinuar = new JButton("CONTINUAR →");
        btnContinuar.setPreferredSize(new Dimension(200, 40));
        btnContinuar.setBackground(new Color(46, 204, 113));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 14));
        btnContinuar.setBorder(null);
        btnContinuar.setFocusPainted(false);
        btnContinuar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnContinuar.addActionListener(e -> continuarConTipo());
        
        // Botón cancelar
        JButton btnCancelar = new JButton("✕ CANCELAR");
        btnCancelar.setPreferredSize(new Dimension(200, 40));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setBorder(null);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> panelPadre.mostrarListado());
        
        barra.add(btnContinuar);
        barra.add(btnCancelar);
        
        return barra;
    }
    
    // ========== CONTINUAR CON EL TIPO SELECCIONADO ==========
    private void continuarConTipo() {
        if (tipoSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor, selecciona un tipo de descuento.",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Pasar al panel de añadir con el tipo ya seleccionado
        panelPadre.mostrarAnadirDescuentoConTipo(tipoSeleccionado);
    }
}
