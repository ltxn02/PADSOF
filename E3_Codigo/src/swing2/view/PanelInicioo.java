package swing2.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import catalog.*;
import logic.Application;
import swing2.controller.CatalogoController;
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

        // 1. Barra superior
        setupBarraSuperior();

        // 2. Configurar el cuerpo principal
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
        // Mejorar scroll
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16);

        gbcCuerpo.gridy = 0;
        gbcCuerpo.fill = GridBagConstraints.BOTH;
        gbcCuerpo.weighty = 1.0;

        this.add(panelCuerpo, BorderLayout.CENTER);

        // 3. Estado inicial
        marcarActivo(btnInicio);
        cargarMasVendidos();
    }

    private void cambiarLayoutCuerpo(boolean esInicio) {
        panelCuerpo.removeAll();

        // IMPORTANTE: Aseguramos que panelCuerpo use GridBagLayout
        // Si en algún momento cambió a BorderLayout, esto lo arregla.
        panelCuerpo.setLayout(new GridBagLayout());

        if (esInicio) {
            gbcCuerpo.gridx = 0;
            gbcCuerpo.weightx = 0.19;
            gbcCuerpo.fill = GridBagConstraints.BOTH; // Para que ocupe el espacio
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
        // 1. COMPROBACIÓN DE SESIÓN (Seguridad)
        // Cambia 'usuarioActual' por tu variable de sesión o controlador
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this,
                    "¡Atención! Debes iniciar sesión para poder comprar productos.",
                    "Sesión no iniciada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. CREACIÓN DEL DIÁLOGO PERSONALIZADO
        JDialog dialog = new JDialog();
        dialog.setTitle("Seleccionar Cantidad");
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(new Color(15, 45, 105)); // Azul a juego con la tarjeta
        dialog.setLayout(new BorderLayout(10, 10));

        // --- PANEL INFO (Nombre y Precio) ---
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

        // --- PANEL CENTRAL (Selector de Cantidad) ---
        JPanel pCant = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pCant.setOpaque(false);

        JLabel lblTxt = new JLabel("Indica la cantidad:");
        lblTxt.setForeground(Color.WHITE);
        lblTxt.setFont(new Font("Arial", Font.PLAIN, 14));

        // Configuración del Spinner (Valor inicial 1, Min 1, Max 99, Salto 1)
        SpinnerModel model = new SpinnerNumberModel(1, 1, 99, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(60, 30));

        // Estilizar el campo de texto del spinner
        JComponent editor = spinner.getEditor();
        JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
        textField.setColumns(3);

        pCant.add(lblTxt);
        pCant.add(spinner);

        // --- PANEL BOTÓN (Confirmación) ---
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

        // Lógica al pulsar Confirmar
        btnConfirmar.addActionListener(e -> {
            int cantidad = (int) spinner.getValue();

            // --- LLAMADA A TU LÓGICA DE NEGOCIO ---
            // Ejemplo: miControlador.aniadirAlCarrito(p, cantidad, usuarioActual);

            dialog.dispose(); // Cerrar ventana

            // Feedback visual bonito
            JOptionPane.showMessageDialog(this,
                    "Has añadido " + cantidad + " unidad(es) de:\n" + p.getName(),
                    "Producto añadido",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        pBoton.add(btnConfirmar);

        // Ensamblar todo en el diálogo
        dialog.add(pInfo, BorderLayout.NORTH);
        dialog.add(pCant, BorderLayout.CENTER);
        dialog.add(pBoton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this); // Centrar respecto al panel principal
        dialog.setVisible(true);
    }
    private JPanel crearTarjeta(NewProduct p) {
        // 1. Configuración de la Tarjeta
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

        // --- PANEL DE IMAGEN (Contenedor del Badge y la Foto) ---
        JPanel pImg = new JPanel(null);
        pImg.setOpaque(false);

        // --- BADGE ---
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

        // --- LABEL DE IMAGEN ---
        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // --- TU LÓGICA ORIGINAL DE FOTOS (RESTAURADA) ---
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
                // Escalamos la imagen (210x340 es un buen tamaño base)
                Image scaled = icon.getImage().getScaledInstance(210, 340, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(scaled));
            } else {
                imgLabel.setText("No existe");
                imgLabel.setForeground(Color.RED);
            }
        }

        // --- EL LISTENER PARA EL MOVIMIENTO DINÁMICO ---
        pImg.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                // El badge persigue el borde derecho
                badge.setLocation(pImg.getWidth() - bWidth, 0);
                // La imagen ocupa todo el panel disponible
                imgLabel.setBounds(0, 0, pImg.getWidth(), pImg.getHeight());
            }
        });

        pImg.add(badge, 0); // Badge arriba
        pImg.add(imgLabel); // Foto detrás

        // --- INFO INFERIOR ---
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
            abrirSelectorCantidad(p); // Abrimos el diálogo bonito
        });

        priceRow.add(price, BorderLayout.WEST);
        priceRow.add(btnAdd, BorderLayout.EAST);

        info.add(name);
        info.add(priceRow);

        card.add(pImg, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);

        // Eventos de clic
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarDetalleProducto(p);
            }
        });

        return card;
    }

    private JPanel crearPanelPublicitario() {
        // 1. Contenedor principal del lado izquierdo
        JPanel panelPubli = new JPanel(new BorderLayout());
        panelPubli.setOpaque(false);
        panelPubli.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        // 2. Label con recorte redondeado para la imagen
        JLabel labelFoto = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Creamos un área de recorte con esquinas redondeadas (30px)
                Shape clip = new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setClip(clip);

                super.paintComponent(g2);
                g2.dispose();
            }
        };
        labelFoto.setHorizontalAlignment(SwingConstants.CENTER);

        // 3. Cargar la imagen desde src/imgProductos/foto.jpg
        String path = "src/foto/foto2.png";
        File f = new File(path);

        // Si no la encuentra ahí, buscamos en la ruta alternativa por si acaso
        if (!f.exists()) {
            f = new File("E3_Codigo/src/foto/foto2.png");
        }

        if (f.exists()) {
            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
            // Escalamos la imagen (350 de ancho es perfecto para el tercio izquierdo)
            // El 800 de alto lo ajustará Java según el contenedor
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
        // Ajuste de ruta común para evitar nulls
        URL url = getClass().getResource("/foto/FondoCliente.png");
        if (url == null) url = getClass().getResource("../../foto/FondoCliente.png");

        if (url != null) imagenFondo = new ImageIcon(url).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }

    // CAMBIADA VISIBILIDAD DEL MÉTODO DE private A public static
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
        // Ajustamos un poco el margen vertical (de 22 a 15) para que los botones cuadren mejor
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        p.setOpaque(false);

        if (user == null) {
            JButton b = new JButton("Iniciar Sesión");
            b.addActionListener(e -> ventana.mostrarPantalla("LOGIN"));
            p.add(b);
        } else {
            // ==========================================
            // Botón Mi Perfil
            // ==========================================
            JButton btnPerfil = new JButton();
            btnPerfil.setContentAreaFilled(false);
            btnPerfil.setBorderPainted(false);
            btnPerfil.setFocusPainted(false);
            btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Técnicas antibalas para la ruta (igual que en los productos)
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
                btnPerfil.setText("Perfil"); // Si falla, ponemos texto simple en vez de un emoji para evitar el "cuadrado"
                btnPerfil.setFont(new Font("Arial", Font.BOLD, 14));
                btnPerfil.setForeground(Color.WHITE);
            }

            // ==========================================
            // Botón Carrito
            // ==========================================
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
                btnCarrito.setText("Carrito"); // Texto de seguridad
                btnCarrito.setFont(new Font("Arial", Font.BOLD, 14));
                btnCarrito.setForeground(Color.WHITE);
            }

            // Eventos provisionales
            btnPerfil.addActionListener(e -> JOptionPane.showMessageDialog(this, "Próximamente: Perfil de " + user.getUsername()));
            btnCarrito.addActionListener(e -> JOptionPane.showMessageDialog(this, "Próximamente: Tu carrito de compra"));

            // Añadimos los botones al panel
            p.add(btnPerfil);
            p.add(btnCarrito);
        }
        return p;
    }
    public void mostrarDetalleProducto(NewProduct p) {
        // 1. Limpiamos el cuerpo principal
        panelCuerpo.removeAll();
        panelCuerpo.setLayout(new BorderLayout());

        // 2. Contenedor con fondo blanco redondeado
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

        // --- COLUMNA IZQUIERDA: TEXTOS Y SCROLL ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Botón Volver simple (por ahora solo recarga mas vendidos para que no de error)
        // Botón volver
        // --- DENTRO DE mostrarDetalleProducto(NewProduct p) ---

        JButton btnVolver = new JButton("← Volver al Catálogo");
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnVolver.addActionListener(e -> {
            // 1. IMPORTANTE: Forzamos el layout de vuelta a GridBagLayout
            // para que la función cambiarLayoutCuerpo no explote
            panelCuerpo.setLayout(new GridBagLayout());

            // 2. Cargamos la vista que corresponda
            // Si no tienes la variable vistaAnterior, usa una por defecto:
            cargarMasVendidos();

            // Si quieres que sea más dinámico, puedes usar:
            // if (esVistaCatalogo) cargarCatalogoORecomendados(); else cargarMasVendidos();
        });

        JLabel titulo = new JLabel(p.getName());
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel precio = new JLabel(String.format("%.2f €", p.getPrice()));
        precio.setFont(new Font("Arial", Font.BOLD, 28));
        precio.setForeground(new Color(110, 30, 230));
        precio.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Texto de descripción
        JTextArea desc = new JTextArea(p.getDescription());
        desc.setFont(new Font("Arial", Font.PLAIN, 16));
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setForeground(new Color(50, 50, 50));

        // ScrollPane para la descripción
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
            abrirSelectorCantidad(p); // Reutilizamos la misma ventana bonita
        });
        // Añadimos componentes al panel izquierdo
        infoPanel.add(btnVolver);
        infoPanel.add(Box.createVerticalStrut(30));
        infoPanel.add(titulo);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(precio);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(new JLabel("Descripción:"));
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(scrollDesc); // El scroll ya contiene el texto
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(btnAdd);

        // --- COLUMNA DERECHA: IMAGEN ---
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

        // --- ENSAMBLAJE ---
        detalleContenedor.add(infoPanel); // Izquierda
        detalleContenedor.add(lblFoto);   // Derecha

        // Margen exterior
        JPanel margen = new JPanel(new BorderLayout());
        margen.setOpaque(false);
        margen.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        margen.add(detalleContenedor, BorderLayout.CENTER);

        panelCuerpo.add(margen, BorderLayout.CENTER);

        panelCuerpo.revalidate();
        panelCuerpo.repaint();
    }}