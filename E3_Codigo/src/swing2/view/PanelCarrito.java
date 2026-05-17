package swing2.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import logic.Application;
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

        
        setLayout(new BorderLayout(20, 20));
        setOpaque(false); 
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        
        JLabel lblVolverGif = new JLabel();
        URL gifUrl = getClass().getResource("/foto/flecha.gif");
        if (gifUrl == null) gifUrl = getClass().getResource("../../foto/flecha.gif");

        if (gifUrl != null) {
            lblVolverGif.setIcon(new ImageIcon(gifUrl));
        } else {
            lblVolverGif.setText("VOLVER");
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
        pIzquierdo.setPreferredSize(new Dimension(220, 80)); 
        pIzquierdo.add(lblVolverGif);

        
        JLabel titulo = new JLabel("MI CARRITO", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);

        
        JPanel panelLogo = PanelInicioo.crearPanelLogo();
        panelLogo.setPreferredSize(new Dimension(220, 80));

        header.add(pIzquierdo, BorderLayout.WEST);
        header.add(titulo, BorderLayout.CENTER);
        header.add(panelLogo, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        
        contenedorProductos = new JPanel();
        contenedorProductos.setLayout(new BoxLayout(contenedorProductos, BoxLayout.Y_AXIS));
        contenedorProductos.setOpaque(false);

        JScrollPane scroll = new JScrollPane(contenedorProductos);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        
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

        if (carritoActual.getCartItems().isEmpty()) {
            contenedorProductos.setLayout(new GridBagLayout());

            JPanel panelVacio = new JPanel();
            panelVacio.setLayout(new BoxLayout(panelVacio, BoxLayout.Y_AXIS));
            panelVacio.setOpaque(false);

            JLabel lblCaraTriste = new JLabel();
            lblCaraTriste.setAlignmentX(Component.CENTER_ALIGNMENT);

            
            URL urlCara = getClass().getResource("/foto/caritatriste.png");
            if (urlCara == null) {
                urlCara = getClass().getResource("../../foto/caritatriste.png");
            }

            if (urlCara != null) {
                ImageIcon icon = new ImageIcon(urlCara);
                
                Image img = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                lblCaraTriste.setIcon(new ImageIcon(img));
            } else {
                
                lblCaraTriste.setText(":(");
                lblCaraTriste.setFont(new Font("Arial", Font.BOLD, 100));
                lblCaraTriste.setForeground(Color.WHITE);
            }

            JLabel lblMensaje = new JLabel("Todavía no tienes ningún producto en el carrito");
            lblMensaje.setFont(new Font("Arial", Font.BOLD, 18));
            lblMensaje.setForeground(Color.WHITE);
            lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblMensaje.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

            panelVacio.add(lblCaraTriste);
            panelVacio.add(lblMensaje);

            contenedorProductos.add(panelVacio);

        } else {
            contenedorProductos.setLayout(new BoxLayout(contenedorProductos, BoxLayout.Y_AXIS));
            for (CartItem ci : carritoActual.getCartItems()) {
                contenedorProductos.add(crearFilaProducto(ci));
                contenedorProductos.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        
        if (lblTotal != null) {
            lblTotal.setText(String.format("%.2f€", carritoActual.getPrice()));
        }

        contenedorProductos.revalidate();
        contenedorProductos.repaint();
    }    
    
    private JPanel crearFilaProducto(CartItem ci) {
        NewProduct p = ci.getProduct();
        JPanel fila = new JPanel(new BorderLayout(15, 0));
        fila.setBackground(new Color(15, 45, 105, 230)); 
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        fila.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        
        JLabel lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(80, 80));
        if (p.getFotos() != null && !p.getFotos().isEmpty()) {
            String nombreArchivo = new File(p.getFotos().get(0)).getName();
            String[] rutas = {
                    "E3_Codigo/src/imgProductos/" + nombreArchivo,
                    "src/imgProductos/" + nombreArchivo,
                    "../src/imgProductos/" + nombreArchivo
            };
            File f = null;
            for (String r : rutas) { if (new File(r).exists()) { f = new File(r); break; } }
        if (f != null) {
            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(50, 76, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(scaled));
        }}
        
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel name = new JLabel(p.getName());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel detalles = new JLabel(ci.getQuantity() + " unidades x " + p.getPrice() + "€");
        detalles.setForeground(new Color(200, 200, 200));
        info.add(name);
        info.add(detalles);

        
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
        lblTotal.setForeground(new Color(0, 178, 255)); 
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
                pagar();
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
    private void pagar() {
        
        if (carritoActual.getCartItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        JDialog dialog = new JDialog();
        dialog.setTitle("Pasarela de pago seguro - RONGERO");
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(new Color(15, 45, 105));
        dialog.setLayout(new BorderLayout(10, 20));

        
        JPanel pInfo = new JPanel(new GridLayout(2, 1, 5, 5));
        pInfo.setOpaque(false);
        pInfo.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));

        JLabel lblTitulo = new JLabel("Resumen de pedido");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblTotal = new JLabel(String.format("Total a cargar: %.2f€", carritoActual.getPrice()));
        lblTotal.setForeground(new Color(180, 160, 255));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setHorizontalAlignment(SwingConstants.CENTER);

        pInfo.add(lblTitulo);
        pInfo.add(lblTotal);

        
        JPanel pTarjeta = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pTarjeta.setOpaque(false);

        JLabel lblTxt = new JLabel("Nº Tarjeta (16 dígitos):");
        lblTxt.setForeground(Color.WHITE);
        lblTxt.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField txtTarjeta = new JTextField(16);
        txtTarjeta.setFont(new Font("Monospaced", Font.BOLD, 16));
        txtTarjeta.setHorizontalAlignment(JTextField.CENTER);

        pTarjeta.add(lblTxt);
        pTarjeta.add(txtTarjeta);

        
        JPanel pBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pBoton.setOpaque(false);
        pBoton.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton btnPagar = new JButton("Pagar ahora");
        btnPagar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPagar.setBackground(new Color(0, 178, 255));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.setFont(new Font("Arial", Font.BOLD, 14));

        btnPagar.addActionListener(e -> {
            String nTarjeta = txtTarjeta.getText().trim();

            if (!nTarjeta.matches("^[0-9]{16}$")) {
                JOptionPane.showMessageDialog(dialog, "La tarjeta debe tener 16 números.", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Client clienteActual = (Client) ventana.getUsuarioLogueado();

                
                for (CartItem ci : carritoActual.getCartItems()) {
                    if (ci.getQuantity() > ci.getProduct().getStock()) {
                        JOptionPane.showMessageDialog(dialog, "Error: El stock de " + ci.getProduct().getName() + " ha cambiado y ya no hay suficiente.", "Error de Stock", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Order nuevoPedido = new Order(
                        clienteActual,
                        new ArrayList<>(carritoActual.getCartItems()),
                        carritoActual.getPrice()
                );

                boolean exito = nuevoPedido.procesarPago(nTarjeta);

                if (exito) {
                    
                    for (CartItem ci : carritoActual.getCartItems()) {
                        NewProduct p = ci.getProduct();
                        p.decreaseStock(ci.getQuantity()); 
                    }

                    
                    
                    

                    dialog.dispose();

                    JOptionPane.showMessageDialog(this,
                            "¡Pago realizado con éxito!\nCódigo de recogida: " + nuevoPedido.getPickupCode(),
                            "Pedido confirmado", JOptionPane.INFORMATION_MESSAGE);

                    carritoActual.clearCart();
                    actualizarVista();

                } else {
                    JOptionPane.showMessageDialog(dialog, "El pago ha sido rechazado por el banco.", "Error de pago", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        pBoton.add(btnPagar);

        dialog.add(pInfo, BorderLayout.NORTH);
        dialog.add(pTarjeta, BorderLayout.CENTER);
        dialog.add(pBoton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}