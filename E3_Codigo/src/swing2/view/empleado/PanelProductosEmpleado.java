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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

public class PanelProductosEmpleado extends JPanel {
    private VentanaPrincipa ventana;
    private Employee empleadoActual;

    private JButton btnProductos, btnIntercambios, btnPedidos;
    private final Color COLOR_FONDO_NAV = new Color(26, 26, 75, 200);
    private final Color COLOR_ACTIVO = new Color(220, 200, 140);

    // Gestor de "cartas" para el panel central
    private CardLayout cardLayoutCentral;
    private JPanel panelContenedorCentral;

    // VARIABLE CLAVE: Guarda el producto que el empleado acaba de buscar
    private NewProduct productoSeleccionadoParaSubida = null;

    public PanelProductosEmpleado(VentanaPrincipa ventana, Employee empleado) {
        this.ventana = ventana;
        this.empleadoActual = empleado;
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(230, 215, 160)); // Fondo dorado principal

        // 1. Barra de Navegación superior
        setupBarraSuperior();

        // 2. Configuración del CardLayout
        cardLayoutCentral = new CardLayout();
        panelContenedorCentral = new JPanel(cardLayoutCentral);
        panelContenedorCentral.setOpaque(false);

        // Añadimos las CUATRO "cartas" (vistas)
        panelContenedorCentral.add(crearPanelTablaProductos(), "TABLA_PRODUCTOS");
        panelContenedorCentral.add(crearPanelOpcionesSubida(), "OPCIONES_SUBIDA");
        panelContenedorCentral.add(crearPanelSubirExistente(), "SUBIR_EXISTENTE");
        panelContenedorCentral.add(crearPanelCantidadSubida(), "CANTIDAD_SUBIDA"); // NUEVA PANTALLA

        this.add(panelContenedorCentral, BorderLayout.CENTER);
    }

    // ==========================================
    // VISTA 1: LA TABLA CON LOS BOTONES
    // ==========================================
    private JPanel crearPanelTablaProductos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlAcciones.setOpaque(false);

        JButton btnSubirManual = crearBotonAzul("Subir Manualmente");
        JButton btnSubirArchivo = crearBotonAzul("Subir desde un archivo");

        btnSubirManual.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "OPCIONES_SUBIDA"));
        btnSubirArchivo.addActionListener(e -> JOptionPane.showMessageDialog(this, "Próximamente: Selector CSV/TXT"));

        pnlAcciones.add(btnSubirManual);
        pnlAcciones.add(btnSubirArchivo);
        panel.add(pnlAcciones, BorderLayout.NORTH);

        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setOpaque(false);
        pnlTabla.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel pnlCabecera = new JPanel(new GridLayout(1, 7, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 45, 80));
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

        JPanel pnlFilas = new JPanel();
        pnlFilas.setLayout(new BoxLayout(pnlFilas, BoxLayout.Y_AXIS));
        pnlFilas.setOpaque(false);

        ArrayList<NewProduct> catalogo = Application.getCatalog();
        if (catalogo != null) {
            for (NewProduct p : catalogo) {
                pnlFilas.add(Box.createRigidArea(new Dimension(0, 10)));

                // Sacamos el ID real
                int idReal = 0;
                if (p instanceof catalog.Product) {
                    idReal = ((catalog.Product) p).getProductId();
                }

                pnlFilas.add(crearFilaProducto(p, idReal));
            }
        }

        JScrollPane scroll = new JScrollPane(pnlFilas);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        pnlTabla.add(scroll, BorderLayout.CENTER);
        panel.add(pnlTabla, BorderLayout.CENTER);

        return panel;
    }

    // ==========================================
    // VISTA 2: EL CUADRO AZUL DE SUBIDA MANUAL
    // ==========================================
    private JPanel crearPanelOpcionesSubida() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel panelAzul = new JPanel();
        panelAzul.setLayout(new BoxLayout(panelAzul, BoxLayout.Y_AXIS));
        panelAzul.setBackground(COLOR_FONDO_NAV);
        panelAzul.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel titulo = new JLabel("Subida Manual");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Desea subir un producto ya existente o uno nuevo?");
        subtitulo.setFont(new Font("Arial", Font.BOLD, 20));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        pnlBotones.setOpaque(false);
        pnlBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnExistente = crearBotonBlanco("Subir un producto existente");
        JButton btnNuevo = crearBotonBlanco("Crear producto nuevo");

        btnExistente.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "SUBIR_EXISTENTE"));
        btnNuevo.addActionListener(e -> JOptionPane.showMessageDialog(this, "Próximamente: Formulario en blanco"));

        pnlBotones.add(btnExistente);
        pnlBotones.add(btnNuevo);

        panelAzul.add(titulo);
        panelAzul.add(Box.createRigidArea(new Dimension(0, 20)));
        panelAzul.add(subtitulo);
        panelAzul.add(Box.createRigidArea(new Dimension(0, 40)));
        panelAzul.add(pnlBotones);

        JPanel alinearArriba = new JPanel(new BorderLayout());
        alinearArriba.setOpaque(false);
        alinearArriba.add(panelAzul, BorderLayout.NORTH);

        wrapper.add(alinearArriba, BorderLayout.CENTER);
        return wrapper;
    }

    // ==========================================
    // VISTA 3: BUSCADOR DE PRODUCTO EXISTENTE
    // ==========================================
    private JPanel crearPanelSubirExistente() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Subida Manual de un producto existente");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 45, 80));
        pnlBanner.add(lblTitulo);

        JPanel pnlCentro = new JPanel();
        pnlCentro.setLayout(new BoxLayout(pnlCentro, BoxLayout.Y_AXIS));
        pnlCentro.setOpaque(false);
        pnlCentro.setBorder(new EmptyBorder(50, 40, 20, 40));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        pnlBusqueda.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar producto por ID:");
        lblBuscar.setFont(new Font("Arial", Font.PLAIN, 24));
        lblBuscar.setForeground(Color.WHITE);

        JTextField txtId = new JTextField(12);
        txtId.setFont(new Font("Arial", Font.BOLD, 20));
        txtId.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlBusqueda.add(lblBuscar);
        pnlBusqueda.add(txtId);

        JPanel pnlResultado = new JPanel(new BorderLayout());
        pnlResultado.setOpaque(false);
        pnlResultado.setMaximumSize(new Dimension(1000, 80));

        JPanel pnlBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBoton.setOpaque(false);
        JButton btnSiguiente = crearBotonDorado("Siguiente");
        btnSiguiente.setVisible(false);

        // AL PULSAR SIGUIENTE PASAMOS A LA PANTALLA DE CANTIDAD
        btnSiguiente.addActionListener(e -> {
            cardLayoutCentral.show(panelContenedorCentral, "CANTIDAD_SUBIDA");
        });

        pnlBoton.add(btnSiguiente);

        txtId.addActionListener(e -> {
            String texto = txtId.getText().trim();
            pnlResultado.removeAll();
            btnSiguiente.setVisible(false);
            productoSeleccionadoParaSubida = null; // Reseteamos por si acaso

            try {
                int idBuscado = Integer.parseInt(texto);
                ArrayList<NewProduct> catalogo = Application.getCatalog();
                NewProduct encontrado = null;

                if (catalogo != null) {
                    for (NewProduct p : catalogo) {
                        if (p instanceof catalog.Product && ((catalog.Product) p).getProductId() == idBuscado) {
                            encontrado = p;
                            break;
                        }
                    }
                }

                if (encontrado != null) {
                    // GUARDAMOS EL PRODUCTO ENCONTRADO EN LA VARIABLE GLOBAL
                    productoSeleccionadoParaSubida = encontrado;

                    pnlResultado.add(crearFilaProducto(encontrado, idBuscado), BorderLayout.CENTER);
                    btnSiguiente.setVisible(true);
                } else {
                    JLabel lblError = new JLabel("No se ha encontrado un producto con el ID " + idBuscado);
                    lblError.setFont(new Font("Arial", Font.BOLD, 16));
                    lblError.setForeground(new Color(255, 100, 100));
                    lblError.setHorizontalAlignment(SwingConstants.CENTER);
                    pnlResultado.add(lblError, BorderLayout.CENTER);
                }
            } catch (NumberFormatException ex) {
                JLabel lblError = new JLabel("Por favor, introduzca un ID numérico válido");
                lblError.setFont(new Font("Arial", Font.BOLD, 16));
                lblError.setForeground(new Color(255, 100, 100));
                lblError.setHorizontalAlignment(SwingConstants.CENTER);
                pnlResultado.add(lblError, BorderLayout.CENTER);
            }

            pnlResultado.revalidate();
            pnlResultado.repaint();
        });

        pnlCentro.add(pnlBusqueda);
        pnlCentro.add(Box.createRigidArea(new Dimension(0, 40)));
        pnlCentro.add(pnlResultado);
        pnlCentro.add(Box.createRigidArea(new Dimension(0, 60)));
        pnlCentro.add(pnlBoton);

        wrapper.add(pnlBanner, BorderLayout.NORTH);
        wrapper.add(pnlCentro, BorderLayout.CENTER);

        return wrapper;
    }

    // ==========================================
    // VISTA 4: PREGUNTAR CANTIDAD Y ACTUALIZAR STOCK (NUEVO)
    // ==========================================
    private JPanel crearPanelCantidadSubida() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        // Cabecera dorada
        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Subida Manual de un producto existente");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 45, 80));
        pnlBanner.add(lblTitulo);

        // Contenedor central
        JPanel pnlCentro = new JPanel();
        pnlCentro.setLayout(new BoxLayout(pnlCentro, BoxLayout.Y_AXIS));
        pnlCentro.setOpaque(false);
        pnlCentro.setBorder(new EmptyBorder(80, 40, 20, 40));

        // Input "Cuantas unidades..."
        JPanel pnlInput = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        pnlInput.setOpaque(false);
        JLabel lblPregunta = new JLabel("Cuantas unidades quieres subir:");
        lblPregunta.setFont(new Font("Arial", Font.PLAIN, 24));
        lblPregunta.setForeground(Color.WHITE);

        JTextField txtCantidad = new JTextField(8);
        txtCantidad.setFont(new Font("Arial", Font.BOLD, 20));
        txtCantidad.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlInput.add(lblPregunta);
        pnlInput.add(txtCantidad);

        // Botón Siguiente (Guardar)
        JPanel pnlBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBoton.setOpaque(false);
        pnlBoton.setBorder(new EmptyBorder(60, 0, 0, 0));
        JButton btnGuardar = crearBotonDorado("Siguiente");

        btnGuardar.addActionListener(e -> {
            try {
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que 0.", "Cantidad inválida", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (productoSeleccionadoParaSubida != null) {
                    // SUMAMOS EL STOCK REAL AL PRODUCTO
                    productoSeleccionadoParaSubida.increaseStock(cantidad);

                    JOptionPane.showMessageDialog(this, "Se han añadido " + cantidad + " unidades correctamente al stock.", "Stock Actualizado", JOptionPane.INFORMATION_MESSAGE);

                    // Limpiamos el cuadro de texto para la próxima vez
                    txtCantidad.setText("");
                    productoSeleccionadoParaSubida = null;

                    // FORZAMOS LA RECARGA DE LA TABLA PARA VER EL NUEVO STOCK
                    panelContenedorCentral.add(crearPanelTablaProductos(), "TABLA_PRODUCTOS");
                    cardLayoutCentral.show(panelContenedorCentral, "TABLA_PRODUCTOS");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, introduzca un número entero válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        pnlBoton.add(btnGuardar);

        pnlCentro.add(pnlInput);
        pnlCentro.add(pnlBoton);

        wrapper.add(pnlBanner, BorderLayout.NORTH);
        wrapper.add(pnlCentro, BorderLayout.CENTER);

        return wrapper;
    }


    // ==========================================
    // MÉTODOS DE DISEÑO DE BOTONES Y FILAS
    // ==========================================
    private JPanel crearFilaProducto(NewProduct p, int id) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel fila = new JPanel(new GridLayout(1, 7, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        fila.setOpaque(false);
        fila.setBorder(new EmptyBorder(5, 20, 5, 20));

        fila.add(crearLabelFila(String.valueOf(id)));
        String nombre = p.getName().length() > 20 ? p.getName().substring(0, 17) + "..." : p.getName();
        fila.add(crearLabelFila(nombre));

        String tipo = "Desconocido", marca = "-";
        if (p instanceof Comic) { tipo = "Cómic"; marca = "Editorial"; }
        else if (p instanceof Figurine) { tipo = "Figura"; marca = "Franquicia"; }
        else if (p instanceof Game) { tipo = "Juego"; marca = "Mecánica"; }

        fila.add(crearLabelFila(tipo));
        fila.add(crearLabelFila(marca));
        fila.add(crearLabelFila(String.valueOf((int)p.getStock())));

        JLabel lblFoto = new JLabel("", SwingConstants.CENTER);
        cargarImagenPequena(p, lblFoto);
        fila.add(lblFoto);
        fila.add(crearLabelFila(String.format("%.2f€", p.getPrice())));

        wrapper.add(fila, BorderLayout.CENTER);
        return wrapper;
    }

    private JLabel crearLabelFila(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(30, 45, 80));
        return lbl;
    }

    private JButton crearBotonBlanco(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
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
        btn.setBorder(new EmptyBorder(12, 25, 12, 25));
        return btn;
    }

    private JButton crearBotonAzul(String texto) {
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

    private JButton crearBotonDorado(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_ACTIVO); // Dorado
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setForeground(new Color(30, 45, 80));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 50, 12, 50));
        return btn;
    }

    // ==========================================
    // CARGA DE IMAGEN MINIATURA Y NAVEGACIÓN
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

    private void setupBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COLOR_FONDO_NAV);
        barra.setPreferredSize(new Dimension(1000, 80));

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nav.setOpaque(false);

        btnProductos = crearBotonNav("Productos", true);
        btnIntercambios = crearBotonNav("Intercambios", false);
        btnPedidos = crearBotonNav("Pedidos", false);

        // EVENTO: Al pulsar "Productos" forzamos recarga y volvemos a la tabla
        btnProductos.addActionListener(e -> {
            panelContenedorCentral.add(crearPanelTablaProductos(), "TABLA_PRODUCTOS");
            cardLayoutCentral.show(panelContenedorCentral, "TABLA_PRODUCTOS");
        });

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