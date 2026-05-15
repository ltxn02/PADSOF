package swing2.view;

import catalog.SecondHandProduct;
import logic.Application;
import users.RegisteredUser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class PanelIntercambios extends JPanel {
    private Image imagenFondo;
    private JPanel contenedorCentral;
    private JScrollPane scrollProductos;
    private RegisteredUser usuarioActual;
    private VentanaPrincipa ventana;

    private JButton btnInicio, btnProductos, btnIntercambios;
    private final Color COLOR_FONDO_NAV = new Color(26, 26, 75, 200);
    private final Color COLOR_ACTIVO = new Color(0, 178, 255);

    public PanelIntercambios(VentanaPrincipa ventana, RegisteredUser usuarioActual) {
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        this.setLayout(new BorderLayout());

        cargarImagenFondo();
        setupBarraSuperior();

        
        JPanel panelCuerpo = new JPanel(new BorderLayout());
        panelCuerpo.setOpaque(false);

        
        contenedorCentral = new JPanel(new GridLayout(0, 3, 25, 25)); 
        contenedorCentral.setOpaque(false);
        contenedorCentral.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        
        JPanel wrapperGrid = new JPanel(new BorderLayout());
        wrapperGrid.setOpaque(false);
        wrapperGrid.add(contenedorCentral, BorderLayout.NORTH);

        scrollProductos = new JScrollPane(wrapperGrid);
        scrollProductos.setOpaque(false);
        scrollProductos.getViewport().setOpaque(false);
        scrollProductos.setBorder(null);
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16);

        panelCuerpo.add(scrollProductos, BorderLayout.CENTER);
        this.add(panelCuerpo, BorderLayout.CENTER);

        
        marcarActivo(btnIntercambios);
        cargarIntercambios();
    }

    public void cargarIntercambios() {
        contenedorCentral.removeAll();
        java.util.List<SecondHandProduct> todos = Application.getSecondHandProducts();

        if (todos == null || todos.isEmpty()) {
            JLabel mensaje = new JLabel("No hay intercambios disponibles");
            mensaje.setForeground(Color.WHITE);
            mensaje.setFont(new Font("Arial", Font.ITALIC, 20));
            mensaje.setHorizontalAlignment(SwingConstants.CENTER);
            contenedorCentral.add(mensaje);
        } else {
            for (SecondHandProduct p : todos) {
                if (p.isAvailable()) {
                    contenedorCentral.add(crearTarjeta(p));
                }
            }
        }
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
    }

    private JPanel crearTarjeta(SecondHandProduct p) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 45, 105, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        
        card.setPreferredSize(new Dimension(280, 440));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        
        JPanel pTop = new JPanel(null);
        pTop.setOpaque(false);
        pTop.setPreferredSize(new Dimension(250, 310));

        
        String condicionTxt = (p.getCondition() != null) ? p.getCondition().toString().replace("_", " ") : "SIN TASAR";
        JLabel lblCondition = crearBadge(condicionTxt);
        lblCondition.setBounds(0, 0, 100, 25);
        pTop.add(lblCondition);

        
        JLabel lblPrice = crearBadge(String.format("%.0f€", p.getPrice()));
        lblPrice.setBounds(180, 0, 70, 25);
        pTop.add(lblPrice);

        
        JLabel imgLabel = new JLabel("", SwingConstants.CENTER);
        
        imgLabel.setBounds(10, 35, 230, 270);
        cargarImagenProducto(p, imgLabel);
        pTop.add(imgLabel);

        
        JPanel pBottom = new JPanel(new BorderLayout());
        pBottom.setOpaque(false);

        
        JLabel name = new JLabel(p.getName(), SwingConstants.CENTER);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 14));
        name.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        pBottom.add(name, BorderLayout.NORTH);

        
        JPanel pFooter = new JPanel(new BorderLayout());
        pFooter.setOpaque(false);

        
        JPanel pUser = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pUser.setOpaque(false);

        JLabel lblPic = new JLabel();
        cargarImagenPerfil(lblPic);

        JLabel lblOwner = new JLabel(p.getOwner() != null ? p.getOwner().getUsername() : "User");
        lblOwner.setForeground(Color.WHITE);
        lblOwner.setFont(new Font("Arial", Font.PLAIN, 12));

        pUser.add(lblPic);
        pUser.add(lblOwner);

        
        JButton btnExchange = new JButton("Solicitar Intercambio");
        btnExchange.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExchange.setBackground(new Color(50, 130, 220));
        btnExchange.setForeground(Color.WHITE);
        btnExchange.setFocusPainted(false);
        btnExchange.setFont(new Font("Arial", Font.BOLD, 11));
        btnExchange.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        pFooter.add(pUser, BorderLayout.WEST);
        pFooter.add(btnExchange, BorderLayout.EAST);

        pBottom.add(pFooter, BorderLayout.SOUTH);

        card.add(pTop, BorderLayout.NORTH);
        card.add(pBottom, BorderLayout.CENTER);

        return card;
    }

    
    private JLabel crearBadge(String texto) {
        JLabel badge = new JLabel(texto, SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(new Color(255, 215, 0));
        badge.setForeground(Color.BLACK);
        badge.setFont(new Font("Arial", Font.BOLD, 11));
        return badge;
    }

    
    private void cargarImagenProducto(SecondHandProduct p, JLabel imgLabel) {
        if (p.getFotos() != null && !p.getFotos().isEmpty()) {
            String nombreArchivo = new File(p.getFotos().get(0)).getName();
            String[] rutas = {
                    "E3_Codigo/src/imgProductos/" + nombreArchivo,
                    "src/imgProductos/" + nombreArchivo,
                    "../src/imgProductos/" + nombreArchivo
            };
            File f = encontrarArchivo(rutas);
            if (f != null) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                
                Image scaled = icon.getImage().getScaledInstance(230, 270, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(scaled));
            } else {
                imgLabel.setText("No foto");
                imgLabel.setForeground(Color.GRAY);
            }
        }
    }

    
    private void cargarImagenPerfil(JLabel imgLabel) {
        String[] rutas = {
                "E3_Codigo/src/foto/logoPerfil.png",
                "src/foto/logoPerfil.png",
                "../src/foto/logoPerfil.png"
        };
        File f = encontrarArchivo(rutas);
        if (f != null) {
            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(scaled));
        } else {
            imgLabel.setText("👤");
            imgLabel.setForeground(Color.WHITE);
            imgLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        }
    }

    private File encontrarArchivo(String[] rutas) {
        for (String r : rutas) {
            File f = new File(r);
            if (f.exists()) return f;
        }
        return null;
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

        
        btnInicio.addActionListener(e -> ventana.mostrarPantalla("INICIO"));
        btnProductos.addActionListener(e -> ventana.mostrarPantalla("INICIO"));
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
        String[] rutas = {
                "E3_Codigo/src/foto/FondoCliente.png",
                "src/foto/FondoCliente.png",
                "../src/foto/FondoCliente.png"
        };
        File f = encontrarArchivo(rutas);
        if (f != null) imagenFondo = new ImageIcon(f.getAbsolutePath()).getImage();
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
                String[] rutas = {
                        "E3_Codigo/src/foto/logoHorizontal.png",
                        "src/foto/logoHorizontal.png",
                        "../src/foto/logoHorizontal.png"
                };
                File f = encontrarArchivo(rutas);
                if (f != null) g.drawImage(new ImageIcon(f.getAbsolutePath()).getImage(), 15, 7, 180, 65, null);
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(220, 80));
        return p;
    }

    private JPanel crearPanelUsuario(RegisteredUser user) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        p.setOpaque(false);

        if (user == null) {
            JButton b = new JButton("Iniciar Sesión");
            b.addActionListener(e -> ventana.mostrarPantalla("LOGIN"));
            p.add(b);
        } else {
            JButton btnPerfil = new JButton();
            btnPerfil.setContentAreaFilled(false);
            btnPerfil.setBorderPainted(false);
            btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));

            String[] rutasPerfil = {
                    "E3_Codigo/src/foto/logoPerfilProvisional.png",
                    "src/foto/logoPerfilProvisional.png",
                    "../src/foto/logoPerfilProvisional.png"
            };
            File fPerfil = encontrarArchivo(rutasPerfil);
            if (fPerfil != null) {
                Image img = new ImageIcon(fPerfil.getAbsolutePath()).getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                btnPerfil.setIcon(new ImageIcon(img));
            } else {
                btnPerfil.setText("Perfil");
                btnPerfil.setForeground(Color.WHITE);
            }

            JButton btnCarrito = new JButton();
            btnCarrito.setContentAreaFilled(false);
            btnCarrito.setBorderPainted(false);
            btnCarrito.setCursor(new Cursor(Cursor.HAND_CURSOR));

            String[] rutasCarrito = {
                    "E3_Codigo/src/foto/logoCarritoProvisional.png",
                    "src/foto/logoCarritoProvisional.png",
                    "../src/foto/logoCarritoProvisional.png"
            };
            File fCarrito = encontrarArchivo(rutasCarrito);
            if (fCarrito != null) {
                Image img = new ImageIcon(fCarrito.getAbsolutePath()).getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                btnCarrito.setIcon(new ImageIcon(img));
            } else {
                btnCarrito.setText("Carrito");
                btnCarrito.setForeground(Color.WHITE);
            }

            p.add(btnPerfil);
            p.add(btnCarrito);
        }
        return p;
    }
}