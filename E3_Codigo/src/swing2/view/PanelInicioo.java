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
            gbcCuerpo.weightx = 0.33;
            gbcCuerpo.fill = GridBagConstraints.BOTH; // Para que ocupe el espacio
            panelCuerpo.add(panelVacioIzquierdo, gbcCuerpo);

            gbcCuerpo.gridx = 1;
            gbcCuerpo.weightx = 0.67;
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
        card.setPreferredSize(new Dimension(200, 420));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pImg = new JPanel(null);
        pImg.setOpaque(false);
        pImg.setPreferredSize(new Dimension(180, 190));

        // --- GESTIÓN DE IMAGEN CON RUTA ABSOLUTA ---
        JLabel imgLabel = new JLabel();
        imgLabel.setBounds(23, 5, 210, 350);
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        if (p.getFotos() != null && !p.getFotos().isEmpty()) {
            String pathOriginal = p.getFotos().get(0);
            String nombreArchivo = new File(pathOriginal).getName();

            // Le damos a Java las 3 rutas donde es posible que estés ejecutando el programa
            String[] posiblesRutas = {
                    "E3_Codigo/src/imgProductos/" + nombreArchivo, // Si el IDE se lanza desde PADSOF (Tu caso actual)
                    "src/imgProductos/" + nombreArchivo,           // Si se lanza desde la terminal en E3_Codigo
                    "../src/imgProductos/" + nombreArchivo         // Si se lanza desde la carpeta bin
            };

            File f = null;
            // Buscamos cuál de las 3 rutas es la correcta
            for (String ruta : posiblesRutas) {
                File fPrueba = new File(ruta);
                if (fPrueba.exists()) {
                    f = fPrueba; // ¡Lo encontramos!
                    break;
                }
            }

            if (f != null && f.exists()) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE || icon.getIconWidth() > 0) {
                    Image scaled = icon.getImage().getScaledInstance(210, 340, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(scaled));
                    imgLabel.setText(""); // Ocultamos el texto
                } else {
                    imgLabel.setText("Error lectura");
                    imgLabel.setForeground(Color.ORANGE);
                }
            } else {
                imgLabel.setText("No existe");
                imgLabel.setForeground(Color.RED);
            }
        } else {
            imgLabel.setText("Sin foto");
            imgLabel.setForeground(Color.GRAY);
        }

        // Badge (Etiqueta de tipo)
        JLabel badge = new JLabel("", SwingConstants.CENTER);
        if(p instanceof Game) badge.setText("GAME");
        else if(p instanceof Comic) badge.setText("COMIC");
        else badge.setText("FIGURINE");

        badge.setBounds(100, 10, 75, 25);
        badge.setOpaque(true);
        badge.setBackground(Color.WHITE);
        badge.setForeground(Color.BLACK);
        badge.setFont(new Font("Arial", Font.BOLD, 10));

        pImg.add(badge);
        pImg.add(imgLabel);

        // Info inferior (Nombre, Precio y Botón)
        JPanel info = new JPanel(new GridLayout(2, 1, 0, 5));
        info.setOpaque(false);

        JLabel name = new JLabel(p.getName());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 13));

        JPanel priceRow = new JPanel(new BorderLayout());
        priceRow.setOpaque(false);

        JLabel price = new JLabel(String.format("%.2f€", p.getPrice()));
        price.setForeground(Color.WHITE);
        price.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnAdd = new JButton("Añadir");
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setBackground(new Color(110, 30, 230));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

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
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        return card;
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
            if (usuarioActual == null) {
                JOptionPane.showMessageDialog(this,
                        "Debes iniciar sesión para añadir productos al carrito",
                        "Inicio de sesión necesario",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                // Suponiendo que tu RegisteredUser o Client tiene un método addCarrito
                // Si no lo tiene, aquí llamarías a tu lógica: Application.aniadirAlCarrito(p);

                // Ejemplo de feedback visual:
                btnAdd.setText("¡Añadido! ✓");
                btnAdd.setBackground(new Color(40, 167, 69)); // Cambia a verde
                btnAdd.setEnabled(false); // Evita que lo pulse mil veces seguidas

                JOptionPane.showMessageDialog(this,
                        p.getName() + " se ha añadido correctamente a tu carrito.");

                // Volver al catálogo automáticamente después de añadir (Opcional)
                // cargarMasVendidos();
            }
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