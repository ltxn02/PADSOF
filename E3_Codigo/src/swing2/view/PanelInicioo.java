package swing2.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import catalog.*;
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
        if (esInicio) {
            gbcCuerpo.gridx = 0;
            gbcCuerpo.weightx = 0.33;
            panelCuerpo.add(panelVacioIzquierdo, gbcCuerpo);
            gbcCuerpo.gridx = 1;
            gbcCuerpo.weightx = 0.67;
            panelCuerpo.add(scrollProductos, gbcCuerpo);
        } else {
            gbcCuerpo.gridx = 0;
            gbcCuerpo.weightx = 1.0;
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

        nav.add(crearPanelLogo());
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

    private JPanel crearPanelLogo() {
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
}