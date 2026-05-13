package swing2.view.empleado;

import catalog.NewProduct;
import catalog.Comic;
import catalog.Figurine;
import catalog.Game;
import logic.Application;
import swing2.view.VentanaPrincipa;
import users.Employee;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class PanelProductosEmpleado extends JPanel {
    private VentanaPrincipa ventana;
    private Employee empleadoActual;

    private JButton btnProductos, btnIntercambios, btnPedidos;
    private final Color COLOR_FONDO_NAV = new Color(26, 26, 75, 200);
    private final Color COLOR_ACTIVO = new Color(220, 200, 140); // El color dorado/crema de la captura

    public PanelProductosEmpleado(VentanaPrincipa ventana, Employee empleado) {
        this.ventana = ventana;
        this.empleadoActual = empleado;
        this.setLayout(new BorderLayout());

        // El fondo general será el color dorado/crema (puedes ajustarlo o usar un degradado luego)
        this.setBackground(new Color(230, 215, 160));

        // 1. Barra de Navegación
        setupBarraSuperior();

        // 2. Panel Central (Controles + Tabla)
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(20, 40, 20, 40));

        // 2.1 Botones de Acción (Subir Manualmente, etc.)
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlAcciones.setOpaque(false);

        JButton btnSubirManual = crearBotonAccion("Subir Manualmente");
        JButton btnSubirArchivo = crearBotonAccion("Subir desde un archivo");

        // Eventos provisionales
        btnSubirManual.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abre formulario de nuevo producto"));
        btnSubirArchivo.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abre selector de archivos CSV/TXT"));

        pnlAcciones.add(btnSubirManual);
        pnlAcciones.add(btnSubirArchivo);

        panelCentral.add(pnlAcciones, BorderLayout.NORTH);

        // 2.2 Zona de la Tabla
        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setOpaque(false);
        pnlTabla.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Cabecera de la tabla (Azul oscuro)
        JPanel pnlCabecera = new JPanel(new GridLayout(1, 7, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 45, 80)); // Azul oscuro
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        pnlCabecera.setOpaque(false);
        pnlCabecera.setPreferredSize(new Dimension(0, 50));
        pnlCabecera.setBorder(new EmptyBorder(0, 20, 0, 20));

        String[] headers = {"ID", "Nombre", "Tipo", "Marca", "Stock", "Foto", "Precio"};
        for (String h : headers) {
            JLabel lblHeader = new JLabel(h, SwingConstants.CENTER);
            lblHeader.setForeground(Color.WHITE);
            lblHeader.setFont(new Font("Arial", Font.BOLD, 14));
            pnlCabecera.add(lblHeader);
        }
        pnlTabla.add(pnlCabecera, BorderLayout.NORTH);

        // Filas de productos
        JPanel pnlFilas = new JPanel();
        pnlFilas.setLayout(new BoxLayout(pnlFilas, BoxLayout.Y_AXIS));
        pnlFilas.setOpaque(false);

        // Cargar productos desde Application
        ArrayList<NewProduct> catalogo = Application.getCatalog();
        if (catalogo != null) {
            // Un contador simple para simular el ID visible
            int fakeId = 90800;
            for (NewProduct p : catalogo) {
                pnlFilas.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre filas
                pnlFilas.add(crearFilaProducto(p, fakeId++));
            }
        }

        JScrollPane scroll = new JScrollPane(pnlFilas);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        pnlTabla.add(scroll, BorderLayout.CENTER);
        panelCentral.add(pnlTabla, BorderLayout.CENTER);

        this.add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearFilaProducto(NewProduct p, int id) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel fila = new JPanel(new GridLayout(1, 7, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE); // Fondo blanco
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        fila.setOpaque(false);
        fila.setBorder(new EmptyBorder(5, 20, 5, 20));

        // 1. ID
        fila.add(crearLabelFila(String.valueOf(id)));

        // 2. Nombre (Recortado si es muy largo)
        String nombre = p.getName().length() > 20 ? p.getName().substring(0, 17) + "..." : p.getName();
        fila.add(crearLabelFila(nombre));

        // 3. Tipo y 4. Marca
        String tipo = "Desconocido";
        String marca = "-";
        if (p instanceof Comic) { tipo = "Cómic"; marca = "Editorial"; }
        else if (p instanceof Figurine) { tipo = "Figura"; marca = "Franquicia"; }
        else if (p instanceof Game) { tipo = "Juego"; marca = "Mecánica"; }

        fila.add(crearLabelFila(tipo));
        fila.add(crearLabelFila(marca));

        // 5. Stock
        fila.add(crearLabelFila(String.valueOf((int)p.getStock())));

        // 6. Foto (Usando la lógica a prueba de balas)
        JLabel lblFoto = new JLabel("", SwingConstants.CENTER);
        cargarImagenPequena(p, lblFoto);
        fila.add(lblFoto);

        // 7. Precio
        fila.add(crearLabelFila(String.format("%.2f€", p.getPrice())));

        wrapper.add(fila, BorderLayout.CENTER);
        return wrapper;
    }

    private JLabel crearLabelFila(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(30, 45, 80)); // Letra oscura
        return lbl;
    }

    private JButton crearBotonAccion(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(new Color(30, 45, 80));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        return btn;
    }

    // ==========================================
    // CARGA DE IMAGEN MINIATURA
    // ==========================================
    private void cargarImagenPequena(NewProduct p, JLabel imgLabel) {
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
                Image scaled = icon.getImage().getScaledInstance(35, 45, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(scaled));
            } else {
                imgLabel.setText("No img");
            }
        }
    }

    // ==========================================
    // BARRA DE NAVEGACIÓN (Estilo Empleado)
    // ==========================================
    private void setupBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COLOR_FONDO_NAV);
        barra.setPreferredSize(new Dimension(1000, 80));

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nav.setOpaque(false);

        btnProductos = crearBotonNav("Productos", true); // Activo por defecto
        btnIntercambios = crearBotonNav("Intercambios", false);
        btnPedidos = crearBotonNav("Pedidos", false);

        nav.add(crearPanelLogo());
        nav.add(btnProductos);
        nav.add(btnIntercambios);
        nav.add(btnPedidos);

        barra.add(nav, BorderLayout.WEST);
        barra.add(crearPanelUsuario(empleadoActual), BorderLayout.EAST);
        this.add(barra, BorderLayout.NORTH);
    }

    private JButton crearBotonNav(String t, boolean activo) {
        JButton b = new JButton(t);
        b.setPreferredSize(new Dimension(140, 80));
        b.setFont(new Font("Arial", Font.BOLD, 15));
        b.setBorder(null);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (activo) {
            b.setBackground(COLOR_ACTIVO);
            b.setForeground(Color.BLACK);
        } else {
            b.setBackground(COLOR_FONDO_NAV);
            b.setForeground(Color.WHITE);
        }
        return b;
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

    private JPanel crearPanelUsuario(Employee user) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        p.setOpaque(false);

        JButton btnPerfil = new JButton();
        btnPerfil.setContentAreaFilled(false);
        btnPerfil.setBorderPainted(false);
        btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));

        String[] rutasPerfil = {
                "E3_Codigo/src/foto/logoPerfilProvisional2.png",
                "src/foto/logoPerfilProvisional2.png",
                "../src/foto/logoPerfilProvisional2.png"
        };
        File fPerfil = encontrarArchivo(rutasPerfil);
        if (fPerfil != null) {
            Image img = new ImageIcon(fPerfil.getAbsolutePath()).getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            btnPerfil.setIcon(new ImageIcon(img));
        } else {
            btnPerfil.setText("👤 " + (user != null ? user.getUsername() : ""));
            btnPerfil.setForeground(Color.WHITE);
        }

        // Al hacer click en el perfil, que pueda cerrar sesión
        btnPerfil.addActionListener(e -> ventana.cambiarSesion(null));
        p.add(btnPerfil);

        return p;
    }

    private File encontrarArchivo(String[] rutas) {
        for (String r : rutas) {
            File f = new File(r);
            if (f.exists()) return f;
        }
        return null;
    }
}