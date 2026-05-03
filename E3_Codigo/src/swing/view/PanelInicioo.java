package swing.view;

import javax.swing.*;
import java.awt.*;
import catalog.NewProduct;
import swing.controller.CatalogoController;
import users.RegisteredUser;
import java.net.URL;
import java.util.ArrayList;

public class PanelInicioo extends JPanel {
    private Image imagenFondo;
    private JPanel contenedorCentral;
    private RegisteredUser usuarioActual;
    private VentanaPrincipa ventana;

    private JButton btnInicio, btnProductos, btnIntercambios;
    private final Color COLOR_FONDO_NAV = new Color(26, 26, 75, 200);
    private final Color COLOR_ACTIVO = new Color(46, 204, 113);

    public PanelInicioo(VentanaPrincipa ventana, RegisteredUser usuarioActual) {
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout());

        URL imgUrl1 = getClass().getResource("../../foto/FondoCliente.png");
        if (imgUrl1 != null) {
            this.imagenFondo = new ImageIcon(imgUrl1).getImage();
        }

        // --- BARRA SUPERIOR ---
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(COLOR_FONDO_NAV);
        barraSuperior.setPreferredSize(new Dimension(1000, 80));

        // 1. IZQUIERDA: LOGO
        JPanel panelLogo = crearPanelLogo();

        // 2. CENTRO: NAVEGACIÓN
        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelNavegacion.setOpaque(false);

        btnInicio = crearBotonNav("Inicio");
        btnProductos = crearBotonNav("Productos");
        btnIntercambios = crearBotonNav("Intercambios");

        btnInicio.addActionListener(e -> { marcarActivo(btnInicio); cargarMasVendidos(); });
        btnProductos.addActionListener(e -> { marcarActivo(btnProductos); cargarCatalogoORecomendados(); });

        panelNavegacion.add(btnInicio);
        panelNavegacion.add(btnProductos);
        panelNavegacion.add(btnIntercambios);

        JPanel contenedorIzquierdo = new JPanel(new BorderLayout());
        contenedorIzquierdo.setOpaque(false);
        contenedorIzquierdo.add(panelLogo, BorderLayout.WEST);
        contenedorIzquierdo.add(panelNavegacion, BorderLayout.CENTER);

        barraSuperior.add(contenedorIzquierdo, BorderLayout.WEST);

        // 3. DERECHA: LOGIN / USER
        barraSuperior.add(crearPanelUsuario(usuarioActual), BorderLayout.EAST);

        add(barraSuperior, BorderLayout.NORTH);

        // --- CUERPO ---
        contenedorCentral = new JPanel(new GridLayout(0, 3, 15, 15));
        contenedorCentral.setOpaque(false);
        contenedorCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(contenedorCentral);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        marcarActivo(btnInicio);
        cargarMasVendidos();
    }

    private JButton crearBotonNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(140, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(COLOR_FONDO_NAV);
        btn.setContentAreaFilled(true);
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void marcarActivo(JButton botonSeleccionado) {
        btnInicio.setBackground(COLOR_FONDO_NAV);
        btnProductos.setBackground(COLOR_FONDO_NAV);
        btnIntercambios.setBackground(COLOR_FONDO_NAV);
        botonSeleccionado.setBackground(COLOR_ACTIVO);
    }

    private void cargarMasVendidos() {
        contenedorCentral.removeAll();
        ArrayList<NewProduct> masVendidos = CatalogoController.obtenerProductosMasVendidos();
        actualizarVista(masVendidos, "TENDENCIAS");
    }

    private void cargarCatalogoORecomendados() {
        contenedorCentral.removeAll();
        ArrayList<NewProduct> productos = CatalogoController.obtenerProductosParaInicio(usuarioActual);
        actualizarVista(productos, "CATÁLOGO");
    }

    private void actualizarVista(ArrayList<NewProduct> lista, String titulo) {
        if (lista == null || lista.isEmpty()) {
            JLabel mensaje = new JLabel("No hay productos en " + titulo);
            mensaje.setForeground(Color.WHITE);
            contenedorCentral.add(mensaje);
        } else {
            for (NewProduct p : lista) {
                contenedorCentral.add(crearTarjeta(p));
            }
        }
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
    }

    private JPanel crearPanelLogo() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL imgUrl = getClass().getResource("../../foto/logoHorizontal.png");
                if (imgUrl != null) {
                    ImageIcon icono = new ImageIcon(imgUrl);
                    g.drawImage(icono.getImage(), 15, 7, 180, 65, null);
                }
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(220, 80));
        return p;
    }

    private JPanel crearPanelUsuario(RegisteredUser user) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 22));
        p.setOpaque(false);

        if (user == null) {
            JButton btnLogin = new JButton("Iniciar Sesión");
            btnLogin.setPreferredSize(new Dimension(130, 35));
            btnLogin.setFont(new Font("Arial", Font.BOLD, 12));
            btnLogin.setBackground(Color.WHITE);
            btnLogin.setForeground(new Color(26, 26, 75));
            btnLogin.setFocusPainted(false);
            btnLogin.addActionListener(e -> ventana.mostrarPantalla("LOGIN"));
            p.add(btnLogin);
        } else {
            JLabel lbl = new JLabel("Hola, " + user.getUsername());
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));

            JButton btnOut = new JButton("Salir");
            btnOut.addActionListener(e -> ventana.cambiarSesion(null));

            p.add(lbl);
            p.add(btnOut);
        }
        return p;
    }

    public void limpiarCampos() {}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JPanel crearTarjeta(NewProduct p) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(new Color(255, 255, 255, 240));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nombre = new JLabel(p.getName(), SwingConstants.CENTER);
        nombre.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel precio = new JLabel(String.format("%.2f €", p.getPrice()), SwingConstants.CENTER);
        precio.setForeground(new Color(46, 139, 87));
        precio.setFont(new Font("Arial", Font.BOLD, 16));

        card.add(nombre, BorderLayout.CENTER);
        card.add(precio, BorderLayout.SOUTH);
        return card;
    }
}