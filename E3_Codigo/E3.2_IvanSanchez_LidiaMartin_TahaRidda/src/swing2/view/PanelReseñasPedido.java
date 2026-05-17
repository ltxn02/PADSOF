package swing2.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import catalog.NewProduct;
import swing2.controller.ReviewController;
import transactions.Order;
import users.RegisteredUser;
import catalog.Item;


public class PanelReseñasPedido extends JPanel {
    private Map<NewProduct, JComboBox<Integer>> combosRating = new HashMap<>();
    private Map<NewProduct, JTextArea> camposComentario = new HashMap<>();
    private Order pedido;
    private RegisteredUser usuario;
    private VentanaPrincipa ventana;

    public PanelReseñasPedido(VentanaPrincipa ventana, Order pedido, RegisteredUser usuario) {
        this.ventana = ventana;
        this.pedido = pedido;
        this.usuario = usuario;

        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(new Color(15, 45, 105));
        this.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setOpaque(false);

        JButton btnVolver = new JButton("← Volver");
        estilizarBotonSecundario(btnVolver);
        btnVolver.addActionListener(e -> ventana.mostrarPantalla("PERFIL"));

        JLabel titulo = new JLabel("Valorar productos - Pedido #" + pedido.getOrderId(), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);

        panelNorte.add(btnVolver, BorderLayout.WEST);
        panelNorte.add(titulo, BorderLayout.CENTER);
        
        panelNorte.add(Box.createHorizontalStrut(100), BorderLayout.EAST);

        this.add(panelNorte, BorderLayout.NORTH);

        
        JPanel panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setOpaque(false);

        
        System.out.println("Items en pedido: " + pedido.getItems().size());

        for (Object obj : pedido.getItems()) {
            NewProduct p = null;

            if (obj instanceof NewProduct) {
                p = (NewProduct) obj;
            } else {
                /* * ¡ATENCIÓN! Si tus pedidos guardan CartItem o similar,
                 * necesitamos extraer el producto así:
                 */
                try {
                    
                    
                    java.lang.reflect.Method getProd = obj.getClass().getMethod("getProduct");
                    p = (NewProduct) getProd.invoke(obj);
                } catch (Exception e) {
                    
                    System.out.println("No se pudo extraer producto de: " + obj.getClass().getName());
                }
            }

            if (p != null) {
                panelLista.add(crearFilaProducto(p));
                panelLista.add(Box.createVerticalStrut(20));
            }
        }

        
        if (combosRating.isEmpty()) {
            JLabel lblVacio = new JLabel("No se encontraron productos procesables en este pedido.");
            lblVacio.setForeground(Color.YELLOW);
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelLista.add(lblVacio);
        }

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        this.add(scroll, BorderLayout.CENTER);

        
        JButton btnEnviar = new JButton("Enviar todas las reseñas");
        estilizarBotonPrincipal(btnEnviar);
        btnEnviar.addActionListener(e -> procesarEnvio());
        this.add(btnEnviar, BorderLayout.SOUTH);
    }

    private JPanel crearFilaProducto(NewProduct p) {
        JPanel fila = new JPanel(new BorderLayout(15, 10));
        fila.setOpaque(true);
        fila.setBackground(new Color(25, 55, 120));
        fila.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel nombre = new JLabel(p.getName());
        nombre.setForeground(Color.WHITE);
        nombre.setFont(new Font("Arial", Font.BOLD, 14));
        nombre.setPreferredSize(new Dimension(200, 20));
        fila.add(nombre, BorderLayout.WEST);

        JPanel pInputs = new JPanel(new BorderLayout(10, 5));
        pInputs.setOpaque(false);

        Integer[] estrellas = {1, 2, 3, 4, 5};
        JComboBox<Integer> combo = new JComboBox<>(estrellas);
        combo.setSelectedIndex(4);
        combosRating.put(p, combo);

        JTextArea area = new JTextArea(3, 30);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        camposComentario.put(p, area);

        JPanel pRating = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pRating.setOpaque(false);
        JLabel lblPunt = new JLabel("Puntuación: ");
        lblPunt.setForeground(Color.WHITE);
        pRating.add(lblPunt);
        pRating.add(combo);

        pInputs.add(pRating, BorderLayout.NORTH);
        pInputs.add(new JScrollPane(area), BorderLayout.CENTER);

        fila.add(pInputs, BorderLayout.CENTER);
        return fila;
    }

    private void procesarEnvio() {
        if (combosRating.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos para valorar.");
            return;
        }

        for (Map.Entry<NewProduct, JComboBox<Integer>> entry : combosRating.entrySet()) {
            NewProduct p = entry.getKey();
            int rating = (int) entry.getValue().getSelectedItem();
            String comentario = camposComentario.get(p).getText();

            ReviewController.guardarReseña(p, usuario, rating, comentario);
        }

        JOptionPane.showMessageDialog(this, "¡Reseñas enviadas con éxito!", "Gracias", JOptionPane.INFORMATION_MESSAGE);
        ventana.mostrarPantalla("PERFIL");
    }

    private void estilizarBotonPrincipal(JButton b) {
        b.setBackground(new Color(110, 30, 230));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(0, 50));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void estilizarBotonSecundario(JButton b) {
        b.setBackground(new Color(40, 60, 150));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}