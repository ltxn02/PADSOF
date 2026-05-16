package swing2.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import catalog.*;
import logic.Application;
import swing2.controller.CatalogoController;
import users.Client;
import users.RegisteredUser;
import java.net.URL;

public class PanelInicioo extends JPanel {
    private Image imagenFondo;
    private JPanel contenedorCentral;
    private JPanel panelVacioIzquierdo;
    private JScrollPane scrollProductos;
    private JPanel panelCuerpo;
    private RegisteredUser usuarioActual;
    private VentanaPrincipa ventana;

    private JButton btnInicio, btnProductos, btnIntercambios;
    private GridBagConstraints gbcCuerpo = new GridBagConstraints();

    private final Color COLOR_FONDO_NAV = new Color(26, 26, 75, 200);
    private final Color COLOR_ACTIVO = new Color(0, 178, 255);

    public PanelInicioo(VentanaPrincipa ventana, RegisteredUser usuarioActual) {
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        this.setLayout(new BorderLayout());

        cargarImagenFondo();

        
        setupBarraSuperior();

        
        panelCuerpo = new JPanel(new GridBagLayout());
        panelCuerpo.setOpaque(false);

        panelVacioIzquierdo = new JPanel();
        panelVacioIzquierdo.setOpaque(false);

        contenedorCentral = new JPanel(new GridLayout(0, 3, 20, 20));
        contenedorCentral.setOpaque(false);
        contenedorCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        scrollProductos = new JScrollPane(contenedorCentral);
        scrollProductos.setOpaque(false);
        scrollProductos.getViewport().setOpaque(false);
        scrollProductos.setBorder(null);
        
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16);

        gbcCuerpo.gridy = 0;
        gbcCuerpo.fill = GridBagConstraints.BOTH;
        gbcCuerpo.weighty = 1.0;

        this.add(panelCuerpo, BorderLayout.CENTER);

        
        marcarActivo(btnInicio);
        cargarMasVendidos();
    }

    private void cambiarLayoutCuerpo(boolean esInicio) {
        panelCuerpo.removeAll();

        
        
        panelCuerpo.setLayout(new GridBagLayout());

        if (esInicio) {
            gbcCuerpo.gridx = 0;
            gbcCuerpo.weightx = 0.19;
            gbcCuerpo.fill = GridBagConstraints.BOTH; 
            panelCuerpo.add(panelVacioIzquierdo, gbcCuerpo);
            panelCuerpo.add(crearPanelPublicitario(), gbcCuerpo);
            gbcCuerpo.gridx = 1;
            gbcCuerpo.weightx = 0.81;
            panelCuerpo.add(scrollProductos, gbcCuerpo);
        } else {
            gbcCuerpo.gridx = 0;
            gbcCuerpo.weightx = 1.0;
            gbcCuerpo.fill = GridBagConstraints.BOTH;
            panelCuerpo.add(scrollProductos, gbcCuerpo);
        }

        panelCuerpo.revalidate();
        panelCuerpo.repaint();
    }
    public void cargarMasVendidos() {
        cambiarLayoutCuerpo(true);
        contenedorCentral.removeAll();
        ArrayList<NewProduct> masVendidos = CatalogoController.obtenerProductosMasVendidos();
        actualizarVista(masVendidos, "TENDENCIAS");
    }

    public void cargarCatalogoORecomendados() {
        cambiarLayoutCuerpo(false);
        contenedorCentral.removeAll();
        ArrayList<NewProduct> productos = CatalogoController.obtenerProductosParaInicio(usuarioActual);
        actualizarVista(productos, "CATÁLOGO");
    }

    private void actualizarVista(ArrayList<NewProduct> lista, String titulo) {
        if (lista == null || lista.isEmpty()) {
            JLabel mensaje = new JLabel("No hay productos en " + titulo);
            mensaje.setForeground(Color.WHITE);
            mensaje.setFont(new Font("Arial", Font.ITALIC, 16));
            mensaje.setHorizontalAlignment(SwingConstants.CENTER);
            contenedorCentral.add(mensaje);
        } else {
            for (NewProduct p : lista) {
                contenedorCentral.add(crearTarjeta(p));
            }
        }
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
    }

    private void abrirSelectorCantidad(NewProduct p) {
        
        
        if (ventana.getUsuarioLogueado() == null) {
            JOptionPane.showMessageDialog(this,
                    "¡Atención! Debes iniciar sesión para poder comprar productos.",
                    "Sesión no iniciada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        
        JDialog dialog = new JDialog();
        dialog.setTitle("Seleccionar Cantidad");
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(new Color(15, 45, 105)); 
        dialog.setLayout(new BorderLayout(10, 10));

        
        JPanel pInfo = new JPanel(new GridLayout(2, 1, 5, 5));
        pInfo.setOpaque(false);
        pInfo.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));

        JLabel lblNombre = new JLabel(p.getName());
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblPrecio = new JLabel(String.format("Precio unitario: %.2f€", p.getPrice()));
        lblPrecio.setForeground(new Color(180, 160, 255));
        lblPrecio.setHorizontalAlignment(SwingConstants.CENTER);

        pInfo.add(lblNombre);
        pInfo.add(lblPrecio);

        
        JPanel pCant = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pCant.setOpaque(false);

        JLabel lblTxt = new JLabel("Indica la cantidad:");
        lblTxt.setForeground(Color.WHITE);
        lblTxt.setFont(new Font("Arial", Font.PLAIN, 14));

        
        SpinnerModel model = new SpinnerNumberModel(1, 1, 99, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(60, 30));

        
        JComponent editor = spinner.getEditor();
        JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
        textField.setColumns(3);

        pCant.add(lblTxt);
        pCant.add(spinner);

        
        JPanel pBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pBoton.setOpaque(false);
        pBoton.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton btnConfirmar = new JButton("Confirmar y Añadir");
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.setBackground(new Color(110, 30, 230));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 13));
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        
        btnConfirmar.addActionListener(e -> {
            int cantidad = (int) spinner.getValue();

            
            if (usuarioActual instanceof Client) {
                Client cliente = (Client) usuarioActual;
                
                cliente.getShoppingCart().addCartItem(p, cantidad);

                dialog.dispose();

                JOptionPane.showMessageDialog(this,
                        "¡Añadido! " + cantidad + "x " + p.getName() + " al carrito.",
                        "Producto añadido",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        pBoton.add(btnConfirmar);

        
        dialog.add(pInfo, BorderLayout.NORTH);
        dialog.add(pCant, BorderLayout.CENTER);
        dialog.add(pBoton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this); 
        dialog.setVisible(true);
    }


    private JPanel crearTarjeta(NewProduct p) {
        
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 45, 105));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(240, 420));
        card.setMinimumSize(new Dimension(200, 400));

        int padding = 15;
        card.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        
        JPanel pImg = new JPanel(null);
        pImg.setOpaque(false);

        
        JLabel badge = new JLabel("", SwingConstants.CENTER);
        if (p instanceof Game) badge.setText("GAME");
        else if (p instanceof Comic) badge.setText("COMIC");
        else badge.setText("FIGURINE");

        int bWidth = 75;
        int bHeight = 25;
        badge.setSize(bWidth, bHeight);
        badge.setOpaque(true);
        badge.setBackground(Color.WHITE);
        badge.setForeground(Color.BLACK);
        badge.setFont(new Font("Arial", Font.BOLD, 10));

        
        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        
        if (p.getFotos() != null && !p.getFotos().isEmpty()) {
            String nombreArchivo = new File(p.getFotos().get(0)).getName();
            String[] posiblesRutas = {
                    "E3_Codigo/src/imgProductos/" + nombreArchivo,
                    "src/imgProductos/" + nombreArchivo,
                    "../src/imgProductos/" + nombreArchivo
            };

            File f = null;
            for (String ruta : posiblesRutas) {
                File fPrueba = new File(ruta);
                if (fPrueba.exists()) {
                    f = fPrueba;
                    break;
                }
            }

            if (f != null && f.exists()) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                
                Image scaled = icon.getImage().getScaledInstance(210, 340, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(scaled));
            } else {
                imgLabel.setText("No existe");
                imgLabel.setForeground(Color.RED);
            }
        }

        
        pImg.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                
                badge.setLocation(pImg.getWidth() - bWidth, 0);
                
                imgLabel.setBounds(0, 0, pImg.getWidth(), pImg.getHeight());
            }
        });

        pImg.add(badge, 0); 
        pImg.add(imgLabel); 

        
        JPanel info = new JPanel(new GridLayout(2, 1, 0, 5));
        info.setOpaque(false);

        JLabel name = new JLabel(p.getName());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel priceRow = new JPanel(new BorderLayout());
        priceRow.setOpaque(false);

        JLabel price = new JLabel(String.format("%.2f€", p.getPrice()));
        price.setForeground(new Color(180, 160, 255));
        price.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnAdd = new JButton("Añadir");
        btnAdd.setBackground(new Color(110, 30, 230));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> {
            abrirSelectorCantidad(p); 
        });

        priceRow.add(price, BorderLayout.WEST);
        priceRow.add(btnAdd, BorderLayout.EAST);

        info.add(name);
        info.add(priceRow);

        card.add(pImg, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);

        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarDetalleProducto(p);
            }
        });

        return card;
    }

    private JPanel crearPanelPublicitario() {
        
        JPanel panelPubli = new JPanel(new BorderLayout());
        panelPubli.setOpaque(false);
        panelPubli.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        
        JLabel labelFoto = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                
                Shape clip = new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setClip(clip);

                super.paintComponent(g2);
                g2.dispose();
            }
        };
        labelFoto.setHorizontalAlignment(SwingConstants.CENTER);

        
        String path = "src/foto/foto2.png";
        File f = new File(path);

        
        if (!f.exists()) {
            f = new File("E3_Codigo/src/foto/foto2.png");
        }

        if (f.exists()) {
            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
            
            
            Image imgEscalada = icon.getImage().getScaledInstance(350, 800, Image.SCALE_SMOOTH);
            labelFoto.setIcon(new ImageIcon(imgEscalada));
        } else {
            labelFoto.setText("Falta foto.jpg");
            labelFoto.setForeground(Color.GRAY);
            labelFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }

        panelPubli.add(labelFoto, BorderLayout.CENTER);
        return panelPubli;
    }

    private void configurarPlaceholder(JLabel l, String txt) {
        l.setText(txt);
        l.setForeground(Color.LIGHT_GRAY);
        l.setOpaque(true);
        l.setBackground(new Color(255, 255, 255, 30));
    }

    private void setupBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COLOR_FONDO_NAV);
        barra.setPreferredSize(new Dimension(1000, 80));

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nav.setOpaque(false);

        btnInicio = crearBotonNav("Inicio");
        btnProductos = crearBotonNav("Productos");
        btnIntercambios = crearBotonNav("Intercambios");

        btnInicio.addActionListener(e -> { marcarActivo(btnInicio); cargarMasVendidos(); });
        btnProductos.addActionListener(e -> { marcarActivo(btnProductos); cargarCatalogoORecomendados(); });
        btnIntercambios.addActionListener(e -> ventana.mostrarPantalla("INTERCAMBIOS"));

        nav.add(PanelInicioo.crearPanelLogo());
        nav.add(btnInicio);
        nav.add(btnProductos);
        nav.add(btnIntercambios);

        barra.add(nav, BorderLayout.WEST);
        barra.add(crearPanelUsuario(usuarioActual), BorderLayout.EAST);
        this.add(barra, BorderLayout.NORTH);
    }

    private JButton crearBotonNav(String t) {
        JButton b = new JButton(t);
        b.setPreferredSize(new Dimension(140, 80));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setBackground(COLOR_FONDO_NAV);
        b.setBorder(null);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void marcarActivo(JButton b) {
        btnInicio.setBackground(COLOR_FONDO_NAV);
        btnProductos.setBackground(COLOR_FONDO_NAV);
        btnIntercambios.setBackground(COLOR_FONDO_NAV);
        b.setBackground(COLOR_ACTIVO);
    }

    private void cargarImagenFondo() {
        
        URL url = getClass().getResource("/foto/FondoCliente.png");
        if (url == null) url = getClass().getResource("../../foto/FondoCliente.png");

        if (url != null) imagenFondo = new ImageIcon(url).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }

    
    public static JPanel crearPanelLogo() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL imgUrl = getClass().getResource("/foto/logoHorizontal.png");
                if (imgUrl == null) imgUrl = getClass().getResource("../../foto/logoHorizontal.png");

                if (imgUrl != null) {
                    g.drawImage(new ImageIcon(imgUrl).getImage(), 15, 7, 180, 65, null);
                }
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(220, 80));
        return p;
    }

    private JPanel crearPanelUsuario(RegisteredUser user) {
        
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        p.setOpaque(false);


            
            
            
            JButton btnPerfil = new JButton();
            btnPerfil.setContentAreaFilled(false);
            btnPerfil.setBorderPainted(false);
            btnPerfil.setFocusPainted(false);
            btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));

            
            String[] rutasPerfil = {
                    "E3_Codigo/src/foto/logoPerfil.png",
                    "src/foto/logoPerfil.png",
                    "../src/foto/logoPerfil.png"
            };

            File fPerfil = null;
            for (String ruta : rutasPerfil) {
                File f = new File(ruta);
                if (f.exists()) {
                    fPerfil = f;
                    break;
                }
            }

            if (fPerfil != null && fPerfil.exists()) {
                ImageIcon iconPerfil = new ImageIcon(fPerfil.getAbsolutePath());
                Image imgPerfil = iconPerfil.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                btnPerfil.setIcon(new ImageIcon(imgPerfil));
            } else {
                btnPerfil.setText("Perfil"); 
                btnPerfil.setFont(new Font("Arial", Font.BOLD, 14));
                btnPerfil.setForeground(Color.WHITE);
            }

            
            
            
            JButton btnCarrito = new JButton();
            btnCarrito.setContentAreaFilled(false);
            btnCarrito.setBorderPainted(false);
            btnCarrito.setFocusPainted(false);
            btnCarrito.setCursor(new Cursor(Cursor.HAND_CURSOR));

            String[] rutasCarrito = {
                    "E3_Codigo/src/foto/logoCarritoProvisional.png",
                    "src/foto/logoCarritoProvisional.png",
                    "../src/foto/logoCarritoProvisional.png"
            };

            File fCarrito = null;
            for (String ruta : rutasCarrito) {
                File f = new File(ruta);
                if (f.exists()) {
                    fCarrito = f;
                    break;
                }
            }

            if (fCarrito != null && fCarrito.exists()) {
                ImageIcon iconCarrito = new ImageIcon(fCarrito.getAbsolutePath());
                Image imgCarrito = iconCarrito.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                btnCarrito.setIcon(new ImageIcon(imgCarrito));
            } else {
                btnCarrito.setText("Carrito"); 
                btnCarrito.setFont(new Font("Arial", Font.BOLD, 14));
                btnCarrito.setForeground(Color.WHITE);
            }

            
            btnPerfil.addActionListener(e -> ventana.mostrarPantalla("PERFIL"));
            btnCarrito.addActionListener(e -> ventana.mostrarPantalla("CARRITO"));

            
            p.add(btnPerfil);
            p.add(btnCarrito);

        return p;
    }
    public void mostrarDetalleProducto(NewProduct p) {
        
        panelCuerpo.removeAll();
        panelCuerpo.setLayout(new BorderLayout());

        
        JPanel detalleContenedor = new JPanel(new GridLayout(1, 2, 40, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 210));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        detalleContenedor.setOpaque(false);
        detalleContenedor.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        
        
        

        JButton btnVolver = new JButton("Volver al Catálogo");
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnVolver.addActionListener(e -> {
            
            
            panelCuerpo.setLayout(new GridBagLayout());

            
            
            cargarMasVendidos();

            
            
        });

        JLabel titulo = new JLabel(p.getName());
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel precio = new JLabel(String.format("%.2f €", p.getPrice()));
        precio.setFont(new Font("Arial", Font.BOLD, 28));
        precio.setForeground(new Color(110, 30, 230));
        precio.setAlignmentX(Component.LEFT_ALIGNMENT);

        
        JTextArea desc = new JTextArea(p.getDescription());
        desc.setFont(new Font("Arial", Font.PLAIN, 16));
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setForeground(new Color(50, 50, 50));

        
        JScrollPane scrollDesc = new JScrollPane(desc);
        scrollDesc.setBorder(null);
        scrollDesc.setOpaque(false);
        scrollDesc.getViewport().setOpaque(false);
        scrollDesc.setPreferredSize(new Dimension(400, 200));
        scrollDesc.setMaximumSize(new Dimension(500, 250));
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDesc.getVerticalScrollBar().setUnitIncrement(12);

        JButton btnAdd = new JButton("Añadir a la Cesta");
        btnAdd.setBackground(new Color(110, 30, 230));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 18));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAdd.addActionListener(e -> {
            abrirSelectorCantidad(p); 
        });
        
        infoPanel.add(btnVolver);
        infoPanel.add(Box.createVerticalStrut(30));
        infoPanel.add(titulo);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(precio);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(new JLabel("Descripción:"));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(scrollDesc); 
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(btnAdd);

        
        JLabel lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
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
                Image scaled = icon.getImage().getScaledInstance(400, 500, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(scaled));
            }
        }

        
        detalleContenedor.add(infoPanel); 
        detalleContenedor.add(lblFoto);   

        
        JPanel margen = new JPanel(new BorderLayout());
        margen.setOpaque(false);
        margen.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        margen.add(detalleContenedor, BorderLayout.CENTER);

        panelCuerpo.add(margen, BorderLayout.CENTER);

        panelCuerpo.revalidate();
        panelCuerpo.repaint();
    }}