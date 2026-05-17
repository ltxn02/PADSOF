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
import utils.ItemType;

import java.net.URL;

public class PanelInicioo extends JPanel {
    private Image imagenFondo;
    private JPanel contenedorCentral;
    private JPanel panelVacioIzquierdo;
    private JScrollPane scrollProductos;
    private JPanel panelCuerpo;
    private RegisteredUser usuarioActual;
    private VentanaPrincipa ventana;
    private ItemType tipoFiltrado = null;
    private double precioMaximo = Double.MAX_VALUE;
    private String criterioOrden = "Fecha";
    private ItemType filtroTipo = null;
    private String filtroCategoria = "Todas";
    private int filtroPrecioMax = 8000;
    private JButton btnInicio, btnProductos, btnIntercambios;
    private GridBagConstraints gbcCuerpo = new GridBagConstraints();
    private ArrayList<NewProduct> productosMostradosActualmente;
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
        this.productosMostradosActualmente = lista;
        contenedorCentral.removeAll();
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

        
        if (p.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "Lo sentimos, este producto está agotado.", "Sin stock", JOptionPane.ERROR_MESSAGE);
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

        JLabel lblPrecio = new JLabel(String.format("Precio unitario: %.2f€ | Stock: %d", p.getPrice(), (int)p.getStock()));
        lblPrecio.setForeground(new Color(180, 160, 255));
        lblPrecio.setHorizontalAlignment(SwingConstants.CENTER);

        pInfo.add(lblNombre);
        pInfo.add(lblPrecio);

        
        JPanel pCant = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pCant.setOpaque(false);

        JLabel lblTxt = new JLabel("Indica la cantidad:");
        lblTxt.setForeground(Color.WHITE);
        lblTxt.setFont(new Font("Arial", Font.PLAIN, 14));

        
        SpinnerModel model = new SpinnerNumberModel(1, 1, (int)p.getStock(), 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(60, 30));

        pCant.add(lblTxt);
        pCant.add(spinner);

        
        JPanel pBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pBoton.setOpaque(false);
        pBoton.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton btnConfirmar = new JButton("Confirmar y Añadir");
        btnConfirmar.setBackground(new Color(110, 30, 230));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 13));

        btnConfirmar.addActionListener(e -> {
            int cantidad = (int) spinner.getValue();

            
            if (usuarioActual instanceof Client) {
                Client cliente = (Client) usuarioActual;

                
                boolean sePudoAñadir = cliente.getShoppingCart().addCartItem(p, cantidad);

                if (sePudoAñadir) {
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this,
                            "¡Añadido! " + cantidad + "x " + p.getName() + " al carrito.",
                            "Producto añadido",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    
                    JOptionPane.showMessageDialog(dialog,
                            "No puedes añadir esa cantidad.\nSupera el stock disponible considerando lo que ya tienes en el carrito.",
                            "Error de Stock",
                            JOptionPane.ERROR_MESSAGE);
                }
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

        
        JLabel badgeRating = new JLabel("", SwingConstants.CENTER);
        double promedio = p.calculateRating();
        if (promedio > 0) {
            badgeRating.setText(String.format("%.1f", promedio)); 
            badgeRating.setSize(35, 25);
            badgeRating.setOpaque(true);
            badgeRating.setBackground(new Color(255, 193, 7));
            badgeRating.setForeground(Color.BLACK);
            badgeRating.setFont(new Font("Arial", Font.BOLD, 11));
        }

        
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
            String[] posiblesRutas = {"E3_Codigo/src/imgProductos/" + nombreArchivo, "src/imgProductos/" + nombreArchivo, "../src/imgProductos/" + nombreArchivo};
            File f = null;
            for (String ruta : posiblesRutas) { File fPrueba = new File(ruta); if (fPrueba.exists()) { f = fPrueba; break; } }
            if (f != null && f.exists()) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                Image scaled = icon.getImage().getScaledInstance(210, 340, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(scaled));
            }
        }

        pImg.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                badge.setLocation(pImg.getWidth() - bWidth, 0);
                badgeRating.setLocation(0, 0);
                imgLabel.setBounds(0, 0, pImg.getWidth(), pImg.getHeight());
            }
        });

        if (promedio > 0) pImg.add(badgeRating, 0);
        pImg.add(badge);
        pImg.add(imgLabel);

        
        JPanel info = new JPanel(new GridLayout(3, 1, 0, 2));
        info.setOpaque(false);

        JLabel name = new JLabel(p.getName());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 14));

        
        JPanel pEstrellasTarjeta = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        pEstrellasTarjeta.setOpaque(false);

        if (promedio > 0) {
            ImageIcon iconEstrella = crearIconoReescalado("src/foto/estrella.png", 14, 14); 
            int estrellasEnteras = (int) Math.round(promedio);
            for (int i = 0; i < estrellasEnteras; i++) {
                if (iconEstrella != null) pEstrellasTarjeta.add(new JLabel(iconEstrella));
                else pEstrellasTarjeta.add(new JLabel("⭐"));
            }
        }

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
        btnAdd.addActionListener(e -> abrirSelectorCantidad(p));

        priceRow.add(price, BorderLayout.WEST);
        priceRow.add(btnAdd, BorderLayout.EAST);

        info.add(name);
        info.add(pEstrellasTarjeta); 
        info.add(priceRow);

        card.add(pImg, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) { mostrarDetalleProducto(p); }
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

        JPanel contenedorNorte = new JPanel(new BorderLayout());
        contenedorNorte.setOpaque(false);


        JPanel barraNav = new JPanel(new BorderLayout());
        barraNav.setBackground(COLOR_FONDO_NAV);
        barraNav.setPreferredSize(new Dimension(1000, 80));

        JPanel navIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navIzquierda.setOpaque(false);

        btnInicio = crearBotonNav("Inicio");
        btnProductos = crearBotonNav("Productos");
        btnIntercambios = crearBotonNav("Intercambios");

        btnInicio.addActionListener(e -> { marcarActivo(btnInicio); cargarMasVendidos(); });
        btnProductos.addActionListener(e -> { marcarActivo(btnProductos); cargarCatalogoORecomendados(); });
        btnIntercambios.addActionListener(e -> ventana.mostrarPantalla("INTERCAMBIOS"));

        navIzquierda.add(PanelInicioo.crearPanelLogo());
        navIzquierda.add(btnInicio);
        navIzquierda.add(btnProductos);
        navIzquierda.add(btnIntercambios);

        barraNav.add(navIzquierda, BorderLayout.WEST);
        barraNav.add(crearPanelUsuario(usuarioActual), BorderLayout.EAST);


        JPanel barraFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        barraFiltros.setOpaque(false);

        JButton btnFiltrar = new JButton("Filtrar ▼");
        JButton btnOrdenar = new JButton("Ordenar por ▼");


        btnFiltrar.addActionListener(e -> abrirDialogoFiltros());
        btnOrdenar.addActionListener(e -> abrirDialogoOrden());

        barraFiltros.add(btnFiltrar);
        barraFiltros.add(btnOrdenar);


        contenedorNorte.add(barraNav, BorderLayout.NORTH);
        contenedorNorte.add(barraFiltros, BorderLayout.SOUTH);

        this.add(contenedorNorte, BorderLayout.NORTH);
    }


    private void abrirDialogoFiltros() {
        JDialog dialog = new JDialog(ventana, "Filtrar Catálogo", true);
        dialog.getContentPane().setBackground(new Color(15, 45, 105));
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel pCentral = new JPanel();
        pCentral.setLayout(new BoxLayout(pCentral, BoxLayout.Y_AXIS));
        pCentral.setOpaque(false);
        pCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font f = new Font("Arial", Font.BOLD, 13);


        JLabel l1 = new JLabel("Tipo de Producto:");
        l1.setForeground(Color.WHITE); l1.setFont(f);
        JComboBox<ItemType> comboTipo = new JComboBox<>(ItemType.values());
        comboTipo.insertItemAt(null, 0);
        comboTipo.setSelectedIndex(0);


        JLabel lCat = new JLabel("Categoría:");
        lCat.setForeground(Color.WHITE); lCat.setFont(f);


        ArrayList<String> nombresCat = new ArrayList<>();
        nombresCat.add("Todas");
        for(catalog.Category c : Application.getGlobalCategories()) {
            nombresCat.add(c.getNameCategory());
        }
        JComboBox<String> comboCat = new JComboBox<>(nombresCat.toArray(new String[0]));


        JLabel l2 = new JLabel("Precio Máximo:");
        l2.setForeground(Color.WHITE); l2.setFont(f);
        JSlider slider = new JSlider(0, 1000, 500);
        slider.setOpaque(false);
        JLabel lblVal = new JLabel(slider.getValue() + " €");
        lblVal.setForeground(new Color(180, 160, 255));
        slider.addChangeListener(e -> lblVal.setText(slider.getValue() + " €"));


        pCentral.add(l1); pCentral.add(comboTipo);
        pCentral.add(Box.createVerticalStrut(10));
        pCentral.add(lCat); pCentral.add(comboCat);
        pCentral.add(Box.createVerticalStrut(10));
        pCentral.add(l2); pCentral.add(lblVal); pCentral.add(slider);

        JButton btnAplicar = new JButton("Aplicar Filtros");
        btnAplicar.addActionListener(e -> {
            this.tipoFiltrado = (ItemType) comboTipo.getSelectedItem();
            this.precioMaximo = slider.getValue();
            String catSeleccionada = (String) comboCat.getSelectedItem();


            ArrayList<NewProduct> base = CatalogoController.obtenerProductosParaInicio(usuarioActual);


            ArrayList<NewProduct> filtrados = CatalogoController.filtrarProductos(base, tipoFiltrado, precioMaximo);


            if (!catSeleccionada.equals("Todas")) {
                ArrayList<NewProduct> aux = new ArrayList<>();
                for (NewProduct p : filtrados) {
                    for (catalog.Category c : p.getCategories()) {
                        if (c.getNameCategory().equals(catSeleccionada)) {
                            aux.add(p);
                            break;
                        }
                    }
                }
                filtrados = aux;
            }

            actualizarVista(filtrados, "FILTRADO");
            dialog.dispose();
        });

        dialog.add(pCentral, BorderLayout.CENTER);
        dialog.add(btnAplicar, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private void abrirDialogoOrden() {
        String[] opciones = {"Precio (Menor a Mayor)", "Precio (Mayor a Menor)", "Nombre (A-Z)", "Fecha de adición"};
        String seleccion = (String) JOptionPane.showInputDialog(this, "Selecciona orden:", "Ordenar",
                JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        if (seleccion != null && productosMostradosActualmente != null) {
            CatalogoController.ordenarProductos(productosMostradosActualmente, seleccion);
            actualizarVista(new ArrayList<>(productosMostradosActualmente), "ORDENADO");
        }
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

        
        JPanel contenedorVistaCompleta = new JPanel();
        contenedorVistaCompleta.setLayout(new BoxLayout(contenedorVistaCompleta, BoxLayout.Y_AXIS));
        contenedorVistaCompleta.setOpaque(false);

        
        
        
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

        
        double promedio = p.calculateRating();
        JLabel lblValoracionMedia = new JLabel();
        if (promedio > 0) {
            lblValoracionMedia.setText(String.format("%.1f (%d valoraciones)", promedio, p.getReviews().size()));
            lblValoracionMedia.setForeground(new Color(200, 140, 0)); 
            lblValoracionMedia.setFont(new Font("Arial", Font.BOLD, 16));
        } else {
            lblValoracionMedia.setText("Sin valoraciones");
            lblValoracionMedia.setForeground(Color.GRAY);
            lblValoracionMedia.setFont(new Font("Arial", Font.ITALIC, 14));
        }
        lblValoracionMedia.setAlignmentX(Component.LEFT_ALIGNMENT);
        

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
        infoPanel.add(lblValoracionMedia); 
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

        
        contenedorVistaCompleta.add(margen);
        

        
        JPanel seccionResenas = new JPanel();
        seccionResenas.setLayout(new BoxLayout(seccionResenas, BoxLayout.Y_AXIS));
        seccionResenas.setOpaque(false);
        seccionResenas.setBorder(BorderFactory.createEmptyBorder(10, 100, 50, 100));

        JLabel tituloResenas = new JLabel("Opiniones de los clientes");
        tituloResenas.setFont(new Font("Arial", Font.BOLD, 24));
        tituloResenas.setForeground(Color.WHITE);
        tituloResenas.setAlignmentX(Component.LEFT_ALIGNMENT);
        seccionResenas.add(tituloResenas);
        seccionResenas.add(Box.createVerticalStrut(20));

        if (p.getReviews() == null || p.getReviews().isEmpty()) {
            JLabel sinResenas = new JLabel("Aún no hay reseñas para este producto. ¡Sé el primero en valorarlo al recibir tu pedido!");
            sinResenas.setForeground(Color.LIGHT_GRAY);
            sinResenas.setFont(new Font("Arial", Font.ITALIC, 16));
            sinResenas.setAlignmentX(Component.LEFT_ALIGNMENT);
            seccionResenas.add(sinResenas);
        } else {
            for (utils.Review r : p.getReviews()) {
                seccionResenas.add(crearCajaComentario(r));
                seccionResenas.add(Box.createVerticalStrut(15));
            }
        }

        contenedorVistaCompleta.add(seccionResenas);

        
        JScrollPane scrollMaestro = new JScrollPane(contenedorVistaCompleta);
        scrollMaestro.setOpaque(false);
        scrollMaestro.getViewport().setOpaque(false);
        scrollMaestro.setBorder(null);
        scrollMaestro.getVerticalScrollBar().setUnitIncrement(20);

        panelCuerpo.add(scrollMaestro, BorderLayout.CENTER);

        panelCuerpo.revalidate();
        panelCuerpo.repaint();
    }

    


    private ImageIcon crearIconoReescalado(String path, int ancho, int alto) {
        if (path == null || path.isEmpty()) return null;

        File f = new File(path);
        if (f.exists()) {
            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }


    private JPanel crearCajaComentario(utils.Review r) {
        JPanel panel = new JPanel(new BorderLayout(15, 5));
        panel.setOpaque(true);
        panel.setBackground(new Color(25, 45, 100, 210));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setMaximumSize(new Dimension(1000, 115));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        
        
        
        JLabel lblFotoUser = new JLabel();
        lblFotoUser.setPreferredSize(new Dimension(55, 55));

        Client autor = r.getPostedBy();
        String pathFoto = (autor != null) ? autor.getFoto() : null;
        String fotoPorDefecto = "src/foto/logoPerfil.png"; 

        
        ImageIcon iconPerfil = crearIconoReescalado(pathFoto, 55, 55);

        
        if (iconPerfil == null) {
            iconPerfil = crearIconoReescalado(fotoPorDefecto, 55, 55);
        }

        if (iconPerfil != null) {
            lblFotoUser.setIcon(iconPerfil);
        } else {
            
            lblFotoUser.setText("👤");
            lblFotoUser.setForeground(Color.WHITE);
        }
        panel.add(lblFotoUser, BorderLayout.WEST);

        
        
        
        JPanel pContenido = new JPanel();
        pContenido.setLayout(new BoxLayout(pContenido, BoxLayout.Y_AXIS));
        pContenido.setOpaque(false);

        
        String nombreUser = (autor != null) ? autor.getUsername() : "Anónimo";
        JLabel lblNombre = new JLabel(nombreUser);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 15));
        lblNombre.setForeground(new Color(0, 178, 255));

        
        JPanel pEstrellas = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        pEstrellas.setOpaque(false);

        String rutaEstrellaPng = "src/foto/estrella.png";
        ImageIcon iconEstrella = crearIconoReescalado(rutaEstrellaPng, 18, 18);

        if (iconEstrella != null) {
            for (int i = 0; i < r.getRating(); i++) {
                pEstrellas.add(new JLabel(iconEstrella));
            }
        } else {
            pEstrellas.add(new JLabel(" ".repeat(r.getRating())));
        }

        
        JTextArea txtComentario = new JTextArea(r.getComment());
        txtComentario.setEditable(false);
        txtComentario.setOpaque(false);
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.setForeground(Color.WHITE);
        txtComentario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtComentario.setRows(2);

        pContenido.add(lblNombre);
        pContenido.add(Box.createVerticalStrut(3));
        pContenido.add(pEstrellas);
        pContenido.add(Box.createVerticalStrut(5));
        pContenido.add(txtComentario);

        panel.add(pContenido, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel crearFilaReview(utils.Review r) {
        JPanel panel = new JPanel(new BorderLayout(15, 5));
        panel.setOpaque(true);
        panel.setBackground(new Color(255, 255, 255, 30)); 
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setMaximumSize(new Dimension(900, 120));

        
        JLabel fotoUser = new JLabel();
        String pathFoto = r.getPostedBy().getFoto(); 
        if (pathFoto != null && !pathFoto.isEmpty()) {
            ImageIcon icon = new ImageIcon(pathFoto);
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            fotoUser.setIcon(new ImageIcon(img));
        } else {
            fotoUser.setText("👤");
            fotoUser.setFont(new Font("Arial", Font.PLAIN, 30));
        }
        panel.add(fotoUser, BorderLayout.WEST);

        
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);

        JLabel nombre = new JLabel(r.getPostedBy().getUsername() + "   ".repeat(r.getRating()));
        nombre.setFont(new Font("Arial", Font.BOLD, 14));
        nombre.setForeground(new Color(0, 178, 255));

        JTextArea comentario = new JTextArea(r.getComment());
        comentario.setEditable(false);
        comentario.setOpaque(false);
        comentario.setLineWrap(true);
        comentario.setForeground(Color.WHITE);
        comentario.setFont(new Font("Arial", Font.ITALIC, 13));

        contenido.add(nombre);
        contenido.add(Box.createVerticalStrut(5));
        contenido.add(comentario);

        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }}