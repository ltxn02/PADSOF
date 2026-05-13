package swing2.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import transactions.*;
import catalog.NewProduct;
import users.Client;
import utils.CartItem;

public class PanelCarrito extends JPanel {
    private JPanel contenedorProductos;
    private JLabel lblTotal;
    private ShoppingCart carritoActual;
    private VentanaPrincipa ventana;
    private Image imagenFondo;

    public PanelCarrito(ShoppingCart carrito, VentanaPrincipa ventana) {
        this.carritoActual = carrito;
        this.ventana = ventana;

        cargarImagenFondo();

        // Configuración básica del panel
        setLayout(new BorderLayout(20, 20));
        setOpaque(false); // Para que se vea la imagen de fondo
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- 1. ENCABEZADO (GIF Volver + Título + Logo) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        // Lado Izquierdo: GIF de flecha
        JLabel lblVolverGif = new JLabel();
        URL gifUrl = getClass().getResource("/foto/flecha.gif");
        if (gifUrl == null) gifUrl = getClass().getResource("../../foto/flecha.gif");

        if (gifUrl != null) {
            lblVolverGif.setIcon(new ImageIcon(gifUrl));
        } else {
            lblVolverGif.setText("← VOLVER");
            lblVolverGif.setForeground(Color.WHITE);
        }
        lblVolverGif.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblVolverGif.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ventana.mostrarPantalla("INICIO");
            }
        });

        JPanel pIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pIzquierdo.setOpaque(false);
        pIzquierdo.setPreferredSize(new Dimension(220, 80)); // Balanceado con el logo
        pIzquierdo.add(lblVolverGif);

        // Centro: Título
        JLabel titulo = new JLabel("MI CARRITO", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);

        // Lado Derecho: Logo
        JPanel panelLogo = PanelInicioo.crearPanelLogo();
        panelLogo.setPreferredSize(new Dimension(220, 80));

        header.add(pIzquierdo, BorderLayout.WEST);
        header.add(titulo, BorderLayout.CENTER);
        header.add(panelLogo, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- 2. CUERPO (Lista de productos con Scroll) ---
        contenedorProductos = new JPanel();
        contenedorProductos.setLayout(new BoxLayout(contenedorProductos, BoxLayout.Y_AXIS));
        contenedorProductos.setOpaque(false);

        JScrollPane scroll = new JScrollPane(contenedorProductos);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // --- 3. LATERAL (Resumen de pago) ---
        add(crearPanelResumen(), BorderLayout.EAST);

        actualizarVista();
    }

    private void cargarImagenFondo() {
        URL url = getClass().getResource("/foto/FondoCliente.png");
        if (url == null) url = getClass().getResource("../../foto/FondoCliente.png");
        if (url != null) imagenFondo = new ImageIcon(url).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }

    public void actualizarVista() {
        contenedorProductos.removeAll();
        for (CartItem ci : carritoActual.getCartItems()) {
            contenedorProductos.add(crearFilaProducto(ci));
            contenedorProductos.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        // getPrice() calcula totales y descuentos automáticamente
        lblTotal.setText(String.format("%.2f€", carritoActual.getPrice()));

        contenedorProductos.revalidate();
        contenedorProductos.repaint();
    }

    private JPanel crearFilaProducto(CartItem ci) {
        NewProduct p = ci.getProduct();
        JPanel fila = new JPanel(new BorderLayout(15, 0));
        fila.setBackground(new Color(15, 45, 105, 230)); // Azul translúcido
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        fila.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Imagen del producto
        JLabel lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(80, 80));
        if (p.getFotos() != null && !p.getFotos().isEmpty()) {
            String nombre = new File(p.getFotos().get(0)).getName();
            String path = "src/imgProductos/" + nombre;
            if (new File(path).exists()) {
                ImageIcon icon = new ImageIcon(path);
                lblFoto.setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
            }
        }

        // Info central
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel name = new JLabel(p.getName());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel detalles = new JLabel(ci.getQuantity() + " unidades x " + p.getPrice() + "€");
        detalles.setForeground(new Color(200, 200, 200));
        info.add(name);
        info.add(detalles);

        // Botón eliminar
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(180, 50, 50));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> {
            carritoActual.removeCartItem(p, 1);
            actualizarVista();
        });

        fila.add(lblFoto, BorderLayout.WEST);
        fila.add(info, BorderLayout.CENTER);
        fila.add(btnEliminar, BorderLayout.EAST);
        return fila;
    }

    private JPanel crearPanelResumen() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(280, 0));
        p.setBackground(new Color(20, 25, 45, 240));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));

        JLabel txt = new JLabel("TOTAL A PAGAR");
        txt.setForeground(Color.GRAY);
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTotal = new JLabel("0.00€");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 35));
        lblTotal.setForeground(new Color(0, 178, 255)); // Azul neón
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnPagar = new JButton("FINALIZAR COMPRA");
        btnPagar.setMaximumSize(new Dimension(220, 50));
        btnPagar.setBackground(new Color(110, 30, 230));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.setFont(new Font("Arial", Font.BOLD, 14));
        btnPagar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPagar.addActionListener(e -> {
            if(carritoActual.getCartItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El carrito está vacío.");
            } else {
                JOptionPane.showMessageDialog(this, "¡Gracias por tu compra en RONGERO!");
                carritoActual.clearCart();
                actualizarVista();
            }
        });

        p.add(txt);
        p.add(Box.createVerticalStrut(10));
        p.add(lblTotal);
        p.add(Box.createVerticalGlue());
        p.add(btnPagar);
        return p;
    }
}