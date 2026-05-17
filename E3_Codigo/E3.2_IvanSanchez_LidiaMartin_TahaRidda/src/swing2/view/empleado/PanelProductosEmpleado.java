package swing2.view.empleado;

import catalog.Category;
import catalog.Comic;
import catalog.Figurine;
import catalog.Game;
import catalog.NewProduct;
import logic.Application;
import swing2.view.VentanaPrincipa;
import users.Employee;
import utils.AgeRange;
import utils.Review;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PanelProductosEmpleado extends JPanel {
    private VentanaPrincipa ventana;
    private Employee empleadoActual;

    private JButton btnProductos, btnIntercambios, btnPedidos;
    private final Color COLOR_FONDO_NAV = new Color(26, 26, 75, 200);
    private final Color COLOR_ACTIVO = new Color(220, 200, 140);

    private CardLayout cardLayoutCentral;
    private JPanel panelContenedorCentral;

    private NewProduct productoSeleccionadoParaSubida = null;

    private JTextField txtNuevoNombre, txtNuevoPrecio, txtNuevoStock;
    private JTextArea txtNuevoDesc;
    private JList<String> listNuevoCategorias;
    private String rutaImagenSeleccionada = null;
    private String rutaArchivoCSVSeleccionado = null;

    private JComboBox<String> comboTipoProducto;
    private CardLayout cardLayoutEspecifico;
    private JPanel panelDinamicoEspecifico;
    private JTextField txtComicPages, txtComicPublisher, txtComicYear, txtComicAuthors;
    private JTextField txtFigHeight, txtFigWidth, txtFigDepth, txtFigMaterial, txtFigFranchise;
    private JTextField txtGamePlayers, txtGameMechanics, txtGameAgeMin, txtGameAgeMax;

    private String rutaImagenEdicion = null;
    private JTextField txtEditNombre, txtEditPrecio, txtEditStock;
    private JTextArea txtEditDesc;

    public PanelProductosEmpleado(VentanaPrincipa ventana, Employee empleado) {
        this.ventana = ventana;
        this.empleadoActual = empleado;
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(230, 215, 160));

        setupBarraSuperior();

        cardLayoutCentral = new CardLayout();
        panelContenedorCentral = new JPanel(cardLayoutCentral);
        panelContenedorCentral.setOpaque(false);

        panelContenedorCentral.add(crearPanelTablaProductos(), "TABLA_PRODUCTOS");
        panelContenedorCentral.add(crearPanelOpcionesSubida(), "OPCIONES_SUBIDA");
        panelContenedorCentral.add(crearPanelSubirExistente(), "SUBIR_EXISTENTE");
        panelContenedorCentral.add(crearPanelCantidadSubida(), "CANTIDAD_SUBIDA");
        panelContenedorCentral.add(crearPanelSubirNuevo(), "CREAR_NUEVO");
        panelContenedorCentral.add(crearPanelSubirNuevoEspecifico(), "CREAR_NUEVO_ESPECIFICO");
        panelContenedorCentral.add(crearPanelSubirArchivo(), "SUBIR_ARCHIVO");

        this.add(panelContenedorCentral, BorderLayout.CENTER);
    }

    // VISTA 1: LA TABLA (CON FILAS CLICKABLES)
    private JPanel crearPanelTablaProductos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlAcciones.setOpaque(false);

        JButton btnSubirManual = crearBotonAzul("Subir manualmente");
        JButton btnSubirArchivo = crearBotonAzul("Subir desde un archivo");

        btnSubirManual.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "OPCIONES_SUBIDA"));
        btnSubirArchivo.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "SUBIR_ARCHIVO"));

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
                int idReal = 0;
                if (p instanceof catalog.Product) {
                    idReal = ((catalog.Product) p).getProductId();
                }
                pnlFilas.add(crearFilaProductoInteractiva(p, idReal));
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

    private JPanel crearFilaProductoInteractiva(NewProduct p, int id) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel fila = new JPanel(new GridLayout(1, 7, 10, 0)) {
            Color bgColor = Color.WHITE;
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        Component[] comps = panelContenedorCentral.getComponents();
                        for (Component c : comps) {
                            if ("EDITAR_PRODUCTO".equals(c.getName())) {
                                panelContenedorCentral.remove(c);
                            }
                        }
                        JPanel panelEdicion = crearPanelEditarProducto(p, id);
                        panelEdicion.setName("EDITAR_PRODUCTO");
                        panelContenedorCentral.add(panelEdicion, "EDITAR_PRODUCTO");
                        cardLayoutCentral.show(panelContenedorCentral, "EDITAR_PRODUCTO");
                    }
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        bgColor = new Color(235, 245, 255);
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                        repaint();
                    }
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        bgColor = Color.WHITE;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
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

    // VISTA 8: EDICIÓN DE PRODUCTO
    private JPanel crearPanelEditarProducto(NewProduct p, int id) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Editar Producto - ID: " + id);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 45, 80));
        pnlBanner.add(lblTitulo);

        // Centro dividido en dos columnas
        JPanel pnlCentro = new JPanel(new GridLayout(1, 2, 40, 0));
        pnlCentro.setOpaque(false);
        pnlCentro.setBorder(new EmptyBorder(40, 60, 0, 60));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.weightx = 1.0;

        int row = 0;
        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Nombre del producto ✏️:"), gbc);
        txtEditNombre = new JTextField(p.getName());
        txtEditNombre.setFont(new Font("Arial", Font.BOLD, 16));
        txtEditNombre.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        gbc.gridy = row++; pnlForm.add(txtEditNombre, gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Descripción ✏️:"), gbc);
        String descActual = "";
        try { descActual = p.getDescription(); } catch (Exception ignored) {}
        txtEditDesc = new JTextArea(descActual, 5, 20);
        txtEditDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEditDesc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtEditDesc.setLineWrap(true);
        txtEditDesc.setWrapStyleWord(true);
        gbc.gridy = row++; pnlForm.add(new JScrollPane(txtEditDesc), gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Precio (€) ✏️:"), gbc);
        txtEditPrecio = new JTextField(String.valueOf(p.getPrice()));
        txtEditPrecio.setFont(new Font("Arial", Font.BOLD, 16));
        txtEditPrecio.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        gbc.gridy = row++; pnlForm.add(txtEditPrecio, gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Stock ✏️:"), gbc);
        txtEditStock = new JTextField(String.valueOf((int)p.getStock()));
        txtEditStock.setFont(new Font("Arial", Font.BOLD, 16));
        txtEditStock.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        gbc.gridy = row++; pnlForm.add(txtEditStock, gbc);

        JPanel pnlDerecha = new JPanel();
        pnlDerecha.setLayout(new BoxLayout(pnlDerecha, BoxLayout.Y_AXIS));
        pnlDerecha.setOpaque(false);
        pnlDerecha.setBorder(new EmptyBorder(30, 0, 0, 0));

        JButton btnUpload = new JButton("Cambiar Imagen");
        btnUpload.setBackground(new Color(0, 110, 255));
        btnUpload.setForeground(Color.WHITE);
        btnUpload.setFocusPainted(false);
        btnUpload.setFont(new Font("Arial", Font.BOLD, 16));
        btnUpload.setBorder(new EmptyBorder(15, 30, 15, 30));
        btnUpload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpload.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblRutaImg = new JLabel("Imagen actual mantenida");
        lblRutaImg.setForeground(Color.LIGHT_GRAY);
        lblRutaImg.setFont(new Font("Arial", Font.ITALIC, 14));
        lblRutaImg.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblRutaImg.setBorder(new EmptyBorder(10,0,20,0));

        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir") + "/src/imgProductos");
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                rutaImagenEdicion = "src/imgProductos/" + fileChooser.getSelectedFile().getName();
                lblRutaImg.setText("Nueva imagen: " + fileChooser.getSelectedFile().getName());
                lblRutaImg.setForeground(new Color(100, 255, 100));
            }
        });

        pnlDerecha.add(btnUpload);
        pnlDerecha.add(lblRutaImg);

        pnlCentro.add(pnlForm);
        pnlCentro.add(pnlDerecha);

        JScrollPane scrollCentro = new JScrollPane(pnlCentro);
        scrollCentro.setOpaque(false);
        scrollCentro.getViewport().setOpaque(false);
        scrollCentro.setBorder(null);

        JPanel pnlBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        pnlBoton.setOpaque(false);
        pnlBoton.setBorder(new EmptyBorder(20, 0, 30, 0));

        JButton btnCancelar = crearBotonBlanco("Cancelar");
        btnCancelar.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "TABLA_PRODUCTOS"));

        JButton btnGuardar = crearBotonDorado("Guardar cambios");
        btnGuardar.addActionListener(e -> procesarEdicionProducto(p));

        pnlBoton.add(btnCancelar);
        pnlBoton.add(btnGuardar);

        wrapper.add(pnlBanner, BorderLayout.NORTH);
        wrapper.add(scrollCentro, BorderLayout.CENTER);
        wrapper.add(pnlBoton, BorderLayout.SOUTH);

        return wrapper;
    }

    private void procesarEdicionProducto(NewProduct p) {
        try {
            String nombre = txtEditNombre.getText().trim();
            String desc = txtEditDesc.getText().trim();
            double precio = Double.parseDouble(txtEditPrecio.getText().trim());
            int stock = Integer.parseInt(txtEditStock.getText().trim());

            String imgFinal = (rutaImagenEdicion != null) ? rutaImagenEdicion : (p.getFotos() != null && !p.getFotos().isEmpty() ? p.getFotos().get(0) : "src/imgProductos/foto.png");

            p.editProductInfo(nombre, desc, precio, imgFinal, stock);

            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
            rutaImagenEdicion = null;

            panelContenedorCentral.add(crearPanelTablaProductos(), "TABLA_PRODUCTOS");
            cardLayoutCentral.show(panelContenedorCentral, "TABLA_PRODUCTOS");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al editar. Revisa los campos numéricos.\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearPanelSubirArchivo() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Subir desde un archivo");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 45, 80));
        pnlBanner.add(lblTitulo);

        JPanel pnlCentro = new JPanel();
        pnlCentro.setLayout(new BoxLayout(pnlCentro, BoxLayout.Y_AXIS));
        pnlCentro.setOpaque(false);
        pnlCentro.setBorder(new EmptyBorder(80, 40, 20, 40));

        JLabel lblIcono = new JLabel("📄", SwingConstants.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        lblIcono.setForeground(Color.WHITE);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRutaArchivo = new JLabel("Ningún archivo seleccionado");
        lblRutaArchivo.setForeground(Color.LIGHT_GRAY);
        lblRutaArchivo.setFont(new Font("Arial", Font.ITALIC, 14));
        lblRutaArchivo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRutaArchivo.setBorder(new EmptyBorder(15, 0, 15, 0));

        JButton btnElegir = crearBotonBlanco("ELEGIR ARCHIVOS  ▼");
        btnElegir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnElegir.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
            fileChooser.setDialogTitle("Selecciona un archivo CSV o TXT");

            javax.swing.filechooser.FileNameExtensionFilter filter =
                    new javax.swing.filechooser.FileNameExtensionFilter("Archivos de texto (*.csv, *.txt)", "csv", "txt");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                rutaArchivoCSVSeleccionado = selectedFile.getAbsolutePath();
                lblRutaArchivo.setText("Archivo listo: " + selectedFile.getName());
                lblRutaArchivo.setForeground(new Color(100, 255, 100));
            }
        });

        JPanel pnlBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBoton.setOpaque(false);
        pnlBoton.setBorder(new EmptyBorder(40, 0, 0, 0));

        JButton btnSiguiente = crearBotonDorado("Siguiente");
        btnSiguiente.addActionListener(e -> {
            if (rutaArchivoCSVSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un archivo primero.", "Archivo no seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int productosAñadidos = procesarCargaMasivaLocal(rutaArchivoCSVSeleccionado);
                if (productosAñadidos > 0) {
                    JOptionPane.showMessageDialog(this, "¡Carga masiva completada con éxito!\nSe han añadido " + productosAñadidos + " productos nuevos al catálogo.", "Carga Exitosa", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "El archivo fue leído, pero no se encontró ningún producto válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
                rutaArchivoCSVSeleccionado = null;
                lblRutaArchivo.setText("Ningún archivo seleccionado");
                lblRutaArchivo.setForeground(Color.LIGHT_GRAY);

                panelContenedorCentral.add(crearPanelTablaProductos(), "TABLA_PRODUCTOS");
                cardLayoutCentral.show(panelContenedorCentral, "TABLA_PRODUCTOS");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al intentar cargar el archivo: " + ex.getMessage(), "Fallo en la carga", JOptionPane.ERROR_MESSAGE);
            }
        });

        pnlBoton.add(btnSiguiente);

        pnlCentro.add(lblIcono);
        pnlCentro.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlCentro.add(btnElegir);
        pnlCentro.add(lblRutaArchivo);
        pnlCentro.add(pnlBoton);

        wrapper.add(pnlBanner, BorderLayout.NORTH);
        wrapper.add(pnlCentro, BorderLayout.CENTER);

        return wrapper;
    }

    private int procesarCargaMasivaLocal(String filePath) throws java.io.IOException {
        int contador = 0;
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";");

                try {
                    String tipo = data[0].toUpperCase();
                    String nombre = data[1];
                    String desc = data[2];
                    double precio = Double.parseDouble(data[3]);
                    int stock = Integer.parseInt(data[4]);
                    ArrayList<String> fotos = new ArrayList<>(Arrays.asList(data[5]));

                    ArrayList<Category> cats = new ArrayList<>();
                    if (!Application.getGlobalCategories().isEmpty()) {
                        cats.add(Application.getGlobalCategories().get(0));
                    }

                    NewProduct nuevo = null;
                    if (tipo.equals("COMIC")) {
                        nuevo = new Comic(nombre, desc, precio, fotos, stock, cats, new ArrayList<Review>(), null,
                                Integer.parseInt(data[6]), data[7], Integer.parseInt(data[8]), new ArrayList<>(Arrays.asList(data[9].split(","))));
                    } else if (tipo.equals("FIGURINE")) {
                        nuevo = new Figurine(nombre, desc, precio, fotos, stock, cats, new ArrayList<Review>(), null,
                                Double.parseDouble(data[6]), Double.parseDouble(data[7]), Double.parseDouble(data[8]), data[9], data[10]);
                    } else if (tipo.equals("GAME")) {
                        nuevo = new Game(nombre, desc, precio, fotos, stock, cats, new ArrayList<Review>(), null,
                                Integer.parseInt(data[6]), new ArrayList<>(Arrays.asList(data[7].split(","))), AgeRange.stringToAgeRange(data[8]));
                    }

                    if (nuevo != null) {
                        Application.getCatalog().add(nuevo);
                        contador++;
                    }
                } catch (Exception e) {
                    System.err.println("Error procesando línea CSV: " + line);
                }
            }
        }
        return contador;
    }

    private JPanel crearPanelOpcionesSubida() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel panelAzul = new JPanel();
        panelAzul.setLayout(new BoxLayout(panelAzul, BoxLayout.Y_AXIS));
        panelAzul.setBackground(COLOR_FONDO_NAV);
        panelAzul.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel titulo = new JLabel("Subida manual");
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
        btnNuevo.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "CREAR_NUEVO"));

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

    private JPanel crearPanelSubirExistente() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Subida manual de un producto existente");
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

        btnSiguiente.addActionListener(e -> {
            cardLayoutCentral.show(panelContenedorCentral, "CANTIDAD_SUBIDA");
        });

        pnlBoton.add(btnSiguiente);

        txtId.addActionListener(e -> {
            String texto = txtId.getText().trim();
            pnlResultado.removeAll();
            btnSiguiente.setVisible(false);
            productoSeleccionadoParaSubida = null;

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

    private JPanel crearPanelCantidadSubida() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Subida manual de un producto existente");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 45, 80));
        pnlBanner.add(lblTitulo);

        JPanel pnlCentro = new JPanel();
        pnlCentro.setLayout(new BoxLayout(pnlCentro, BoxLayout.Y_AXIS));
        pnlCentro.setOpaque(false);
        pnlCentro.setBorder(new EmptyBorder(80, 40, 20, 40));

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

        JPanel pnlBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBoton.setOpaque(false);
        pnlBoton.setBorder(new EmptyBorder(60, 0, 0, 0));
        JButton btnGuardar = crearBotonDorado("Guardar stock");

        btnGuardar.addActionListener(e -> {
            try {
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que 0.", "Cantidad inválida", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (productoSeleccionadoParaSubida != null) {
                    productoSeleccionadoParaSubida.increaseStock(cantidad);
                    JOptionPane.showMessageDialog(this, "Se han añadido " + cantidad + " unidades correctamente al stock.", "Stock actualizado", JOptionPane.INFORMATION_MESSAGE);
                    txtCantidad.setText("");
                    productoSeleccionadoParaSubida = null;

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

    private JPanel crearPanelSubirNuevo() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Subida manual de un producto nuevo (paso 1/2)");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 45, 80));
        pnlBanner.add(lblTitulo);

        JPanel pnlCentro = new JPanel(new GridLayout(1, 2, 40, 0));
        pnlCentro.setOpaque(false);
        pnlCentro.setBorder(new EmptyBorder(20, 60, 0, 60));

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.weightx = 1.0;

        int row = 0;
        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Nombre del producto:"), gbc);
        txtNuevoNombre = new JTextField();
        txtNuevoNombre.setFont(new Font("Arial", Font.BOLD, 16));
        txtNuevoNombre.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        gbc.gridy = row++; pnlForm.add(txtNuevoNombre, gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Descripción:"), gbc);
        txtNuevoDesc = new JTextArea(4, 20);
        txtNuevoDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNuevoDesc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtNuevoDesc.setLineWrap(true);
        txtNuevoDesc.setWrapStyleWord(true);

        JScrollPane scrollDesc = new JScrollPane(txtNuevoDesc);
        gbc.gridy = row++; pnlForm.add(scrollDesc, gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Precio (€):"), gbc);
        txtNuevoPrecio = new JTextField();
        txtNuevoPrecio.setFont(new Font("Arial", Font.BOLD, 16));
        txtNuevoPrecio.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        gbc.gridy = row++; pnlForm.add(txtNuevoPrecio, gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Stock inicial:"), gbc);
        txtNuevoStock = new JTextField();
        txtNuevoStock.setFont(new Font("Arial", Font.BOLD, 16));
        txtNuevoStock.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        gbc.gridy = row++; pnlForm.add(txtNuevoStock, gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Categorías (mantén Ctrl para seleccionar varias):"), gbc);
        ArrayList<catalog.Category> categoriasBD = Application.getGlobalCategories();
        String[] catNombres = new String[categoriasBD.size()];
        for(int i = 0; i < categoriasBD.size(); i++) catNombres[i] = categoriasBD.get(i).getNameCategory();

        listNuevoCategorias = new JList<>(catNombres);
        listNuevoCategorias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listNuevoCategorias.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollCat = new JScrollPane(listNuevoCategorias);
        scrollCat.setPreferredSize(new Dimension(200, 60));
        gbc.gridy = row++; pnlForm.add(scrollCat, gbc);

        JPanel pnlImage = new JPanel();
        pnlImage.setLayout(new BoxLayout(pnlImage, BoxLayout.Y_AXIS));
        pnlImage.setOpaque(false);
        pnlImage.setBorder(new EmptyBorder(80, 0, 0, 0));

        JButton btnUpload = new JButton("<html><center><b style='font-size:20px'>+</b><br>Subir imagen</center></html>");
        btnUpload.setBackground(new Color(0, 110, 255));
        btnUpload.setForeground(Color.WHITE);
        btnUpload.setFocusPainted(false);
        btnUpload.setFont(new Font("Arial", Font.BOLD, 16));
        btnUpload.setBorder(new EmptyBorder(25, 60, 25, 60));
        btnUpload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpload.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRutaImg = new JLabel("Ninguna imagen seleccionada");
        lblRutaImg.setForeground(Color.LIGHT_GRAY);
        lblRutaImg.setFont(new Font("Arial", Font.ITALIC, 14));
        lblRutaImg.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRutaImg.setBorder(new EmptyBorder(15,0,0,0));

        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir") + "/src/imgProductos");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                rutaImagenSeleccionada = "src/imgProductos/" + selectedFile.getName();
                lblRutaImg.setText("Imagen lista: " + selectedFile.getName());
                lblRutaImg.setForeground(new Color(100, 255, 100));
            }
        });

        pnlImage.add(btnUpload);
        pnlImage.add(lblRutaImg);

        pnlCentro.add(pnlForm);
        pnlCentro.add(pnlImage);

        JScrollPane scrollCentro = new JScrollPane(pnlCentro);
        scrollCentro.setOpaque(false);
        scrollCentro.getViewport().setOpaque(false);
        scrollCentro.setBorder(null);

        JPanel pnlBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBoton.setOpaque(false);
        pnlBoton.setBorder(new EmptyBorder(10, 0, 30, 0));

        JButton btnSiguiente = crearBotonDorado("Siguiente paso");
        btnSiguiente.addActionListener(e -> {
            if(txtNuevoNombre.getText().trim().isEmpty() || txtNuevoPrecio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completa los campos principales.");
                return;
            }
            cardLayoutCentral.show(panelContenedorCentral, "CREAR_NUEVO_ESPECIFICO");
        });
        pnlBoton.add(btnSiguiente);

        wrapper.add(pnlBanner, BorderLayout.NORTH);
        wrapper.add(scrollCentro, BorderLayout.CENTER);
        wrapper.add(pnlBoton, BorderLayout.SOUTH);

        return wrapper;
    }

    private JPanel crearPanelSubirNuevoEspecifico() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Especificaciones del producto (Paso 2/2)");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 45, 80));
        pnlBanner.add(lblTitulo);

        JPanel pnlCentro = new JPanel();
        pnlCentro.setLayout(new BoxLayout(pnlCentro, BoxLayout.Y_AXIS));
        pnlCentro.setOpaque(false);
        pnlCentro.setBorder(new EmptyBorder(30, 100, 20, 100));

        JPanel pnlCombo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlCombo.setOpaque(false);
        pnlCombo.add(crearLabelBlanco("Tipo de producto: "));

        comboTipoProducto = new JComboBox<>(new String[]{"CÓMIC", "FIGURA", "JUEGO"});
        comboTipoProducto.setFont(new Font("Arial", Font.BOLD, 16));
        pnlCombo.add(comboTipoProducto);

        pnlCentro.add(pnlCombo);
        pnlCentro.add(Box.createRigidArea(new Dimension(0, 20)));

        cardLayoutEspecifico = new CardLayout();
        panelDinamicoEspecifico = new JPanel(cardLayoutEspecifico);
        panelDinamicoEspecifico.setOpaque(false);

        panelDinamicoEspecifico.add(crearFormComic(), "CÓMIC");
        panelDinamicoEspecifico.add(crearFormFigurine(), "FIGURA");
        panelDinamicoEspecifico.add(crearFormGame(), "JUEGO");

        comboTipoProducto.addActionListener(e -> {
            cardLayoutEspecifico.show(panelDinamicoEspecifico, (String) comboTipoProducto.getSelectedItem());
        });

        pnlCentro.add(panelDinamicoEspecifico);

        JPanel pnlBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        pnlBoton.setOpaque(false);
        pnlBoton.setBorder(new EmptyBorder(20, 0, 30, 0));

        JButton btnAtras = crearBotonBlanco("Atrás");
        btnAtras.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "CREAR_NUEVO"));

        JButton btnFinal = crearBotonDorado("Crear y guardar producto");
        btnFinal.addActionListener(e -> procesarCreacionProducto());

        pnlBoton.add(btnAtras);
        pnlBoton.add(btnFinal);

        wrapper.add(pnlBanner, BorderLayout.NORTH);
        wrapper.add(pnlCentro, BorderLayout.CENTER);
        wrapper.add(pnlBoton, BorderLayout.SOUTH);

        return wrapper;
    }

    private JPanel crearFormComic() {
        JPanel grid = new JPanel(new GridLayout(4, 2, 10, 15));
        grid.setOpaque(false);
        grid.add(crearLabelBlanco("Número de páginas:")); grid.add(txtComicPages = new JTextField());
        grid.add(crearLabelBlanco("Editorial:")); grid.add(txtComicPublisher = new JTextField());
        grid.add(crearLabelBlanco("Año de publicación:")); grid.add(txtComicYear = new JTextField());
        grid.add(crearLabelBlanco("Autores (separados por coma):")); grid.add(txtComicAuthors = new JTextField());

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(grid, BorderLayout.NORTH);
        return wrap;
    }

    private JPanel crearFormFigurine() {
        JPanel grid = new JPanel(new GridLayout(5, 2, 10, 15));
        grid.setOpaque(false);
        grid.add(crearLabelBlanco("Altura (cm):")); grid.add(txtFigHeight = new JTextField());
        grid.add(crearLabelBlanco("Anchura (cm):")); grid.add(txtFigWidth = new JTextField());
        grid.add(crearLabelBlanco("Profundidad (cm):")); grid.add(txtFigDepth = new JTextField());
        grid.add(crearLabelBlanco("Material:")); grid.add(txtFigMaterial = new JTextField());
        grid.add(crearLabelBlanco("Franquicia:")); grid.add(txtFigFranchise = new JTextField());

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(grid, BorderLayout.NORTH);
        return wrap;
    }

    private JPanel crearFormGame() {
        JPanel grid = new JPanel(new GridLayout(4, 2, 10, 15));
        grid.setOpaque(false);
        grid.add(crearLabelBlanco("Número de jugadores:")); grid.add(txtGamePlayers = new JTextField());
        grid.add(crearLabelBlanco("Mecánicas (separadas por coma):")); grid.add(txtGameMechanics = new JTextField());
        grid.add(crearLabelBlanco("Edad mínima recomendada:")); grid.add(txtGameAgeMin = new JTextField());
        grid.add(crearLabelBlanco("Edad máxima recomendada:")); grid.add(txtGameAgeMax = new JTextField());

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(grid, BorderLayout.NORTH);
        return wrap;
    }

    private void procesarCreacionProducto() {
        try {
            String nombre = txtNuevoNombre.getText().trim();
            String desc = txtNuevoDesc.getText().trim();
            double precio = Double.parseDouble(txtNuevoPrecio.getText().trim());
            int stock = Integer.parseInt(txtNuevoStock.getText().trim());

            ArrayList<Category> catGlobales = Application.getGlobalCategories();
            ArrayList<Category> categoriasElegidas = new ArrayList<>();
            for (String catName : listNuevoCategorias.getSelectedValuesList()) {
                for (Category c : catGlobales) {
                    if (c.getNameCategory().equals(catName)) {
                        categoriasElegidas.add(c);
                        break;
                    }
                }
            }

            if (categoriasElegidas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecciona al menos una categoría en el paso 1.");
                return;
            }

            ArrayList<String> fotos = new ArrayList<>();
            if (rutaImagenSeleccionada != null) {
                fotos.add(rutaImagenSeleccionada);
            } else {
                fotos.add("src/imgProductos/foto.png");
            }

            String tipoElegido = (String) comboTipoProducto.getSelectedItem();
            NewProduct nuevoProd = null;

            if (tipoElegido.equals("CÓMIC")) {
                int pages = Integer.parseInt(txtComicPages.getText().trim());
                String pub = txtComicPublisher.getText().trim();
                int year = Integer.parseInt(txtComicYear.getText().trim());
                ArrayList<String> authors = new ArrayList<>(Arrays.asList(txtComicAuthors.getText().split(",")));
                nuevoProd = new Comic(nombre, desc, precio, fotos, stock, categoriasElegidas, new ArrayList<Review>(), null, pages, pub, year, authors);
            }
            else if (tipoElegido.equals("FIGURINE")) {
                double h = Double.parseDouble(txtFigHeight.getText().trim());
                double w = Double.parseDouble(txtFigWidth.getText().trim());
                double d = Double.parseDouble(txtFigDepth.getText().trim());
                String mat = txtFigMaterial.getText().trim();
                String fran = txtFigFranchise.getText().trim();
                nuevoProd = new Figurine(nombre, desc, precio, fotos, stock, categoriasElegidas, new ArrayList<Review>(), null, h, w, d, mat, fran);
            }
            else if (tipoElegido.equals("JUEGO")) {
                int play = Integer.parseInt(txtGamePlayers.getText().trim());
                ArrayList<String> mech = new ArrayList<>(Arrays.asList(txtGameMechanics.getText().split(",")));
                int minAge = Integer.parseInt(txtGameAgeMin.getText().trim());
                int maxAge = Integer.parseInt(txtGameAgeMax.getText().trim());
                AgeRange ageRange = new AgeRange(minAge, maxAge);
                nuevoProd = new Game(nombre, desc, precio, fotos, stock, categoriasElegidas, new ArrayList<Review>(), null, play, mech, ageRange);
            }

            if (nuevoProd != null) {
                Application.getCatalog().add(nuevoProd);
                JOptionPane.showMessageDialog(this, "¡Producto '" + nombre + "' creado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                txtNuevoNombre.setText("");
                txtNuevoDesc.setText("");
                txtNuevoPrecio.setText("");
                txtNuevoStock.setText("");
                rutaImagenSeleccionada = null;

                panelContenedorCentral.add(crearPanelTablaProductos(), "TABLA_PRODUCTOS");
                cardLayoutCentral.show(panelContenedorCentral, "TABLA_PRODUCTOS");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Comprueba los campos numéricos (precio, stock, años, medidas...). Solo deben contener números.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error lógico", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel crearLabelBlanco(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 18));
        l.setBorder(new EmptyBorder(8, 0, 5, 0));
        return l;
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
                g2.setColor(COLOR_ACTIVO);
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

        btnProductos.addActionListener(e -> {
            panelContenedorCentral.add(crearPanelTablaProductos(), "TABLA_PRODUCTOS");
            cardLayoutCentral.show(panelContenedorCentral, "TABLA_PRODUCTOS");
        });

        btnIntercambios.addActionListener(e -> ventana.mostrarPantalla("INTERCAMBIOS_EMPLEADO"));
        btnPedidos.addActionListener(e -> ventana.mostrarPantalla("PEDIDOS_EMPLEADO"));

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
        btnPerfil.setFocusPainted(false);
        btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPerfil.setPreferredSize(new Dimension(50, 50));

        if (user != null) {
            btnPerfil.setToolTipText("Perfil de " + user.getUsername());
        }

        String[] rutasPerfil = {
                "src/foto/logoPerfilProvisional2.png",
                "E3_Codigo/src/foto/logoPerfilProvisional2.png",
                "foto/logoPerfilProvisional2.png" // Por si se ejecuta desde dentro de src
        };

        File fPerfil = encontrarArchivo(rutasPerfil);
        boolean imagenCargada = false;

        if (fPerfil != null) {
            try {
                java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(fPerfil);
                if (img != null) {
                    Image scaled = img.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
                    btnPerfil.setIcon(new ImageIcon(scaled));
                    imagenCargada = true;
                }
            } catch (Exception ex) {
                System.err.println("Aviso: No se pudo cargar la imagen del botón: " + ex.getMessage());
            }
        }

        // Si no hay imagen, ponemos texto estándar (sin emojis que den problemas)
        if (!imagenCargada) {
            btnPerfil.setText("Perfil ▼");
            btnPerfil.setFont(new Font("Arial", Font.BOLD, 16));
            btnPerfil.setForeground(Color.WHITE);
            btnPerfil.setPreferredSize(new Dimension(100, 50));
        }

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesión");
        itemCerrarSesion.setFont(new Font("Arial", Font.BOLD, 14));
        itemCerrarSesion.setForeground(new Color(220, 50, 50));
        itemCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        itemCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que deseas cerrar sesión?",
                    "Cerrar sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                ventana.cambiarSesion(null);
            }
        });

        popupMenu.add(itemCerrarSesion);

        btnPerfil.addActionListener(e -> {
            popupMenu.show(btnPerfil, 0, btnPerfil.getHeight());
        });

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