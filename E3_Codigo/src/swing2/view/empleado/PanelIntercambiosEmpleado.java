package swing2.view.empleado;

import catalog.SecondHandProduct;
import logic.Application;
import swing2.view.VentanaPrincipa;
import users.Employee;
import users.Client;
import users.RegisteredUser;
import utils.Condition;
import utils.ItemType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class PanelIntercambiosEmpleado extends JPanel {
    private VentanaPrincipa ventana;
    private Employee empleadoActual;

    private JButton btnProductos, btnIntercambios, btnPedidos;
    private final Color COLOR_FONDO_NAV = new Color(26, 26, 75, 200);
    private final Color COLOR_ACTIVO = new Color(220, 200, 140); // Dorado

    private CardLayout cardLayoutCentral;
    private JPanel panelContenedorCentral;

    private JPanel contenedorCentral;
    private JScrollPane scrollProductos;

    private String rutaArchivoCSVSeleccionado = null;

    // Variables globales para el REGISTRO MANUAL
    private JTextField txtManualOwner, txtManualNombre, txtManualPrecio;
    private JTextArea txtManualDesc;
    private JComboBox<ItemType> comboManualTipo;
    private JComboBox<Condition> comboManualCondicion;
    private String rutaImagenManualSeleccionada = null;

    public PanelIntercambiosEmpleado(VentanaPrincipa ventana, Employee empleado) {
        this.ventana = ventana;
        this.empleadoActual = empleado;
        this.setLayout(new BorderLayout());
        this.setBackground(COLOR_ACTIVO);

        setupBarraSuperior();

        cardLayoutCentral = new CardLayout();
        panelContenedorCentral = new JPanel(cardLayoutCentral);
        panelContenedorCentral.setOpaque(false);

        // --- CARTA 1: VISTA PRINCIPAL
        JPanel pnlVistaPrincipal = new JPanel(new BorderLayout());
        pnlVistaPrincipal.setOpaque(false);

        pnlVistaPrincipal.add(crearBarraFiltros(), BorderLayout.NORTH);

        contenedorCentral = new JPanel(new GridLayout(0, 3, 25, 25));
        contenedorCentral.setOpaque(false);
        contenedorCentral.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        JPanel wrapperGrid = new JPanel(new BorderLayout());
        wrapperGrid.setOpaque(false);
        wrapperGrid.add(contenedorCentral, BorderLayout.NORTH);

        scrollProductos = new JScrollPane(wrapperGrid);
        scrollProductos.setOpaque(false);
        scrollProductos.getViewport().setOpaque(false);
        scrollProductos.setBorder(null);
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16);

        pnlVistaPrincipal.add(scrollProductos, BorderLayout.CENTER);

        panelContenedorCentral.add(pnlVistaPrincipal, "VISTA_PRINCIPAL");
        panelContenedorCentral.add(crearPanelOpcionesSubida(), "OPCIONES_SUBIDA");
        panelContenedorCentral.add(crearPanelSubirArchivo(), "SUBIR_ARCHIVO");
        panelContenedorCentral.add(crearPanelRegistroManual(), "REGISTRO_MANUAL");
        panelContenedorCentral.add(crearPanelIntercambiosCompletos(), "INTERCAMBIOS_COMPLETOS"); // NUEVA PANTALLA

        this.add(panelContenedorCentral, BorderLayout.CENTER);

        cargarIntercambios();
    }

    // BARRA DE FILTROS SUPERIOR
    private JPanel crearBarraFiltros() {
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        pnlTop.setBorder(new EmptyBorder(15, 40, 15, 40));

        JPanel pnlBotonesCentrales = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        pnlBotonesCentrales.setOpaque(false);

        JButton btnCompletos = crearBotonFiltroBlanco("Intercambios completos");
        JButton btnSubir = crearBotonFiltroBlanco("Subir intercambios");

        btnCompletos.setForeground(new Color(30, 45, 80));
        btnCompletos.setFont(new Font("Arial", Font.BOLD, 16));

        btnCompletos.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "INTERCAMBIOS_COMPLETOS"));
        btnSubir.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "OPCIONES_SUBIDA"));

        pnlBotonesCentrales.add(btnCompletos);
        pnlBotonesCentrales.add(btnSubir);

        pnlTop.add(pnlBotonesCentrales, BorderLayout.CENTER);

        return pnlTop;
    }

    private JButton crearBotonFiltroBlanco(String texto) {
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
        btn.setForeground(new Color(100, 100, 120));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 25, 8, 25));
        return btn;
    }

    // CARTA 5: INTERCAMBIOS COMPLETOS (TABLA)
    private JPanel crearPanelIntercambiosCompletos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Título y filtros superiores
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        pnlTop.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblTitulo = new JLabel("Intercambios completos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 45, 80));
        pnlTop.add(lblTitulo, BorderLayout.NORTH);

        JPanel pnlFiltros = new JPanel(new BorderLayout());
        pnlFiltros.setOpaque(false);
        pnlFiltros.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblFiltrar = new JLabel("Filtrar por ▼");
        lblFiltrar.setFont(new Font("Arial", Font.BOLD, 14));
        lblFiltrar.setForeground(new Color(30, 45, 80));

        JLabel lblOrdenar = new JLabel("Ordenar por ▼");
        lblOrdenar.setFont(new Font("Arial", Font.BOLD, 14));
        lblOrdenar.setForeground(new Color(30, 45, 80));

        pnlFiltros.add(lblFiltrar, BorderLayout.WEST);
        pnlFiltros.add(lblOrdenar, BorderLayout.EAST);
        pnlTop.add(pnlFiltros, BorderLayout.SOUTH);

        panel.add(pnlTop, BorderLayout.NORTH);

        // Zona de la tabla
        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setOpaque(false);

        // Cabecera de la tabla
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

        String[] headers = {"ID Intercambio", "ID usuario 1", "ID usuario 2", "ID objeto 1", "ID objeto 2", "Objeto 1 recogido", "Objeto 2 recogido"};
        for (String h : headers) {
            JLabel lblHeader = new JLabel(h, SwingConstants.CENTER);
            lblHeader.setForeground(Color.WHITE);
            lblHeader.setFont(new Font("Arial", Font.BOLD, 12));
            pnlCabecera.add(lblHeader);
        }
        pnlTabla.add(pnlCabecera, BorderLayout.NORTH);

        // Filas de intercambios
        JPanel pnlFilas = new JPanel();
        pnlFilas.setLayout(new BoxLayout(pnlFilas, BoxLayout.Y_AXIS));
        pnlFilas.setOpaque(false);

        pnlFilas.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlFilas.add(crearFilaIntercambio("76543", "90807", "90654", "78965", "89765", true, true));
        pnlFilas.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlFilas.add(crearFilaIntercambio("54684", "87987", "21868", "84562", "18488", true, false));
        pnlFilas.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlFilas.add(crearFilaIntercambio("69875", "25896", "52369", "36985", "25874", false, false));
        pnlFilas.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlFilas.add(crearFilaIntercambio("12365", "45698", "78965", "45612", "15962", true, true));

        JScrollPane scroll = new JScrollPane(pnlFilas);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        pnlTabla.add(scroll, BorderLayout.CENTER);
        panel.add(pnlTabla, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearFilaIntercambio(String idInt, String idU1, String idU2, String idOb1, String idOb2, boolean rec1, boolean rec2) {
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

        fila.add(crearLabelFilaOscuro(idInt));
        fila.add(crearLabelFilaOscuro(idU1));
        fila.add(crearLabelFilaOscuro(idU2));
        fila.add(crearLabelFilaOscuro(idOb1));
        fila.add(crearLabelFilaOscuro(idOb2));

        // Checkbox objeto 1
        JPanel pnlCheck1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlCheck1.setOpaque(false);
        JCheckBox chk1 = new JCheckBox();
        chk1.setSelected(rec1);
        chk1.setEnabled(false);
        chk1.setOpaque(false);
        pnlCheck1.add(chk1);
        fila.add(pnlCheck1);

        // Checkbox objeto 2
        JPanel pnlCheck2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlCheck2.setOpaque(false);
        JCheckBox chk2 = new JCheckBox();
        chk2.setSelected(rec2);
        chk2.setEnabled(false);
        chk2.setOpaque(false);
        pnlCheck2.add(chk2);
        fila.add(pnlCheck2);

        wrapper.add(fila, BorderLayout.CENTER);
        return wrapper;
    }

    private JLabel crearLabelFilaOscuro(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        lbl.setForeground(new Color(30, 45, 80));
        return lbl;
    }

    // CARTA 2: MENÚ DE OPCIONES DE SUBIDA
    private JPanel crearPanelOpcionesSubida() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel panelAzul = new JPanel();
        panelAzul.setLayout(new BoxLayout(panelAzul, BoxLayout.Y_AXIS));
        panelAzul.setBackground(COLOR_FONDO_NAV);
        panelAzul.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel titulo = new JLabel("Subida de intercambios");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("¿Cómo deseas registrar el nuevo producto de intercambio?");
        subtitulo.setFont(new Font("Arial", Font.BOLD, 20));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        pnlBotones.setOpaque(false);
        pnlBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnManual = crearBotonBlanco("Registro manual");
        JButton btnArchivo = crearBotonBlanco("Subir desde un archivo");

        btnManual.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "REGISTRO_MANUAL"));
        btnArchivo.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "SUBIR_ARCHIVO"));

        pnlBotones.add(btnManual);
        pnlBotones.add(btnArchivo);

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

    // CARTA 4: REGISTRO MANUAL DE INTERCAMBIO
    private JPanel crearPanelRegistroManual() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Registro manual de intercambio");
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
        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Username del cliente (propietario):"), gbc);
        txtManualOwner = new JTextField();
        txtManualOwner.setFont(new Font("Arial", Font.BOLD, 16));
        txtManualOwner.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        gbc.gridy = row++; pnlForm.add(txtManualOwner, gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Nombre del producto:"), gbc);
        txtManualNombre = new JTextField();
        txtManualNombre.setFont(new Font("Arial", Font.BOLD, 16));
        txtManualNombre.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        gbc.gridy = row++; pnlForm.add(txtManualNombre, gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Descripción:"), gbc);
        txtManualDesc = new JTextArea(3, 20);
        txtManualDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        txtManualDesc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtManualDesc.setLineWrap(true);
        txtManualDesc.setWrapStyleWord(true);
        gbc.gridy = row++; pnlForm.add(new JScrollPane(txtManualDesc), gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Tipo de producto:"), gbc);
        comboManualTipo = new JComboBox<>(ItemType.values());
        comboManualTipo.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = row++; pnlForm.add(comboManualTipo, gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Estado (condición):"), gbc);
        comboManualCondicion = new JComboBox<>(Condition.values());
        comboManualCondicion.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = row++; pnlForm.add(comboManualCondicion, gbc);

        gbc.gridy = row++; pnlForm.add(crearLabelBlanco("Precio tasación (€):"), gbc);
        txtManualPrecio = new JTextField();
        txtManualPrecio.setFont(new Font("Arial", Font.BOLD, 16));
        txtManualPrecio.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        gbc.gridy = row++; pnlForm.add(txtManualPrecio, gbc);

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
                rutaImagenManualSeleccionada = "src/imgProductos/" + selectedFile.getName();
                lblRutaImg.setText("Imagen lista: " + selectedFile.getName());
                lblRutaImg.setForeground(new Color(100, 255, 100));
            }
        });

        pnlImage.add(btnUpload);
        pnlImage.add(lblRutaImg);

        pnlCentro.add(pnlForm);
        pnlCentro.add(pnlImage);

        JPanel pnlBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        pnlBoton.setOpaque(false);
        pnlBoton.setBorder(new EmptyBorder(10, 0, 30, 0));

        JButton btnAtras = crearBotonBlanco("Atrás");
        btnAtras.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "OPCIONES_SUBIDA"));

        JButton btnCrear = crearBotonDorado("Crear intercambio");
        btnCrear.addActionListener(e -> procesarRegistroManualIntercambio());

        pnlBoton.add(btnAtras);
        pnlBoton.add(btnCrear);

        wrapper.add(pnlBanner, BorderLayout.NORTH);

        JScrollPane scrollCentro = new JScrollPane(pnlCentro);
        scrollCentro.setOpaque(false);
        scrollCentro.getViewport().setOpaque(false);
        scrollCentro.setBorder(null);
        wrapper.add(scrollCentro, BorderLayout.CENTER);

        wrapper.add(pnlBoton, BorderLayout.SOUTH);

        return wrapper;
    }

    private void procesarRegistroManualIntercambio() {
        try {
            String username = txtManualOwner.getText().trim();
            String nombre = txtManualNombre.getText().trim();
            String desc = txtManualDesc.getText().trim();
            double precio = Double.parseDouble(txtManualPrecio.getText().trim());
            ItemType tipo = (ItemType) comboManualTipo.getSelectedItem();
            Condition condicion = (Condition) comboManualCondicion.getSelectedItem();

            if (username.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del propietario y del producto son obligatorios.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Client owner = null;
            for (RegisteredUser u : Application.getUsers()) {
                if (u instanceof Client && u.getUsername().equalsIgnoreCase(username)) {
                    owner = (Client) u;
                    break;
                }
            }

            if (owner == null) {
                JOptionPane.showMessageDialog(this, "No se encontró ningún cliente con el username '" + username + "'.", "Cliente no encontrado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<String> fotos = new ArrayList<>();
            if (rutaImagenManualSeleccionada != null) {
                fotos.add(rutaImagenManualSeleccionada);
            } else {
                fotos.add("src/imgProductos/foto.png");
            }

            SecondHandProduct nuevoIntercambio = new SecondHandProduct(
                    nombre, desc, fotos, precio, true, tipo, condicion, owner
            );

            Application.addSecondHandProduct(nuevoIntercambio);

            JOptionPane.showMessageDialog(this, "¡Intercambio registrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            txtManualOwner.setText("");
            txtManualNombre.setText("");
            txtManualDesc.setText("");
            txtManualPrecio.setText("");
            comboManualTipo.setSelectedIndex(0);
            comboManualCondicion.setSelectedIndex(0);
            rutaImagenManualSeleccionada = null;

            cargarIntercambios();
            cardLayoutCentral.show(panelContenedorCentral, "VISTA_PRINCIPAL");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio de tasación debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ha ocurrido un error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel crearLabelBlanco(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 18));
        l.setBorder(new EmptyBorder(8, 0, 5, 0));
        return l;
    }


    // CARTA 3: SUBIR DESDE UN ARCHIVO (CSV)
    private JPanel crearPanelSubirArchivo() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Subir intercambios desde un archivo (CSV/TXT)");
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
            fileChooser.setDialogTitle("Selecciona un archivo de intercambios");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto (*.csv, *.txt)", "csv", "txt");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                rutaArchivoCSVSeleccionado = selectedFile.getAbsolutePath();
                lblRutaArchivo.setText("Archivo listo: " + selectedFile.getName());
                lblRutaArchivo.setForeground(new Color(100, 255, 100));
            }
        });

        JPanel pnlBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        pnlBoton.setOpaque(false);
        pnlBoton.setBorder(new EmptyBorder(40, 0, 0, 0));

        JButton btnCancelar = crearBotonBlanco("Atrás");
        btnCancelar.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "OPCIONES_SUBIDA"));

        JButton btnSiguiente = crearBotonDorado("Procesar archivo");
        btnSiguiente.addActionListener(e -> {
            if (rutaArchivoCSVSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un archivo primero.", "Archivo no seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int intercambiosAñadidos = procesarCargaMasivaLocalIntercambios(rutaArchivoCSVSeleccionado);

                if (intercambiosAñadidos > 0) {
                    JOptionPane.showMessageDialog(this,
                            "¡Carga completada con éxito!\nSe han registrado " + intercambiosAñadidos + " nuevos intercambios.",
                            "Carga exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "El archivo fue leído, pero no se encontró ningún intercambio válido o hubo errores en los nombres de usuario.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }

                rutaArchivoCSVSeleccionado = null;
                lblRutaArchivo.setText("Ningún archivo seleccionado");
                lblRutaArchivo.setForeground(Color.LIGHT_GRAY);

                cargarIntercambios();
                cardLayoutCentral.show(panelContenedorCentral, "VISTA_PRINCIPAL");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al intentar cargar el archivo: " + ex.getMessage(), "Fallo en la carga", JOptionPane.ERROR_MESSAGE);
            }
        });

        pnlBoton.add(btnCancelar);
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

    private int procesarCargaMasivaLocalIntercambios(String filePath) throws java.io.IOException {
        int contador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";");

                try {
                    ItemType tipo = ItemType.valueOf(data[0].toUpperCase());
                    String nombre = data[1];
                    String desc = data[2];
                    double precio = Double.parseDouble(data[3]);
                    Condition condicion = Condition.valueOf(data[4].toUpperCase());
                    ArrayList<String> fotos = new ArrayList<>(Arrays.asList(data[5]));
                    String usernamePropietario = data[6];

                    Client owner = null;
                    for (RegisteredUser u : Application.getUsers()) {
                        if (u instanceof Client && u.getUsername().equalsIgnoreCase(usernamePropietario)) {
                            owner = (Client) u;
                            break;
                        }
                    }

                    if (owner == null) continue;

                    SecondHandProduct shp = new SecondHandProduct(nombre, desc, fotos, precio, true, tipo, condicion, owner);
                    Application.addSecondHandProduct(shp);
                    contador++;

                } catch (Exception e) {
                    System.err.println("Error procesando línea CSV de intercambios: " + line + " -> " + e.getMessage());
                }
            }
        }
        return contador;
    }


    // CARGA DE TARJETAS (CUADRÍCULA PRINCIPAL)
    public void cargarIntercambios() {
        contenedorCentral.removeAll();
        java.util.List<SecondHandProduct> todos = Application.getSecondHandProducts();

        if (todos == null || todos.isEmpty()) {
            JLabel mensaje = new JLabel("No hay intercambios disponibles");
            mensaje.setForeground(new Color(30, 45, 80));
            mensaje.setFont(new Font("Arial", Font.BOLD, 20));
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
                g2.setColor(new Color(15, 45, 105));
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

        JButton btnModificar = new JButton("Modificar intercambio");
        btnModificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnModificar.setBackground(new Color(50, 130, 220));
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFocusPainted(false);
        btnModificar.setFont(new Font("Arial", Font.BOLD, 11));
        btnModificar.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        btnModificar.addActionListener(e -> {
            Component[] comps = panelContenedorCentral.getComponents();
            for (Component c : comps) {
                if ("EDITAR_INTERCAMBIO".equals(c.getName())) {
                    panelContenedorCentral.remove(c);
                }
            }
            JPanel panelEdicion = crearPanelEditarIntercambio(p);
            panelEdicion.setName("EDITAR_INTERCAMBIO");
            panelContenedorCentral.add(panelEdicion, "EDITAR_INTERCAMBIO");
            cardLayoutCentral.show(panelContenedorCentral, "EDITAR_INTERCAMBIO");
        });

        pFooter.add(pUser, BorderLayout.WEST);
        pFooter.add(btnModificar, BorderLayout.EAST);

        pBottom.add(pFooter, BorderLayout.SOUTH);
        card.add(pTop, BorderLayout.NORTH);
        card.add(pBottom, BorderLayout.CENTER);

        return card;
    }

    // PANTALLA DE EDICIÓN DE INTERCAMBIO
    private JPanel crearPanelEditarIntercambio(SecondHandProduct p) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Tasación y modificación - " + p.getName());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 45, 80));
        pnlBanner.add(lblTitulo);

        JPanel pnlCentro = new JPanel(new GridBagLayout());
        pnlCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblEstado = new JLabel("Estado (condición):");
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 18));
        pnlCentro.add(lblEstado, gbc);

        gbc.gridx = 1;
        JComboBox<Condition> comboCondition = new JComboBox<>(Condition.values());
        if (p.getCondition() != null) {
            comboCondition.setSelectedItem(p.getCondition());
        }
        comboCondition.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlCentro.add(comboCondition, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPrecio = new JLabel("Precio de tasación (€):");
        lblPrecio.setForeground(Color.WHITE);
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 18));
        pnlCentro.add(lblPrecio, gbc);

        gbc.gridx = 1;
        JTextField txtPrecio = new JTextField(String.valueOf(p.getPrice()));
        txtPrecio.setFont(new Font("Arial", Font.BOLD, 18));
        txtPrecio.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        pnlCentro.add(txtPrecio, gbc);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        pnlBotones.setOpaque(false);
        pnlBotones.setBorder(new EmptyBorder(30, 0, 50, 0));

        JButton btnCancelar = crearBotonBlanco("Cancelar");
        btnCancelar.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "VISTA_PRINCIPAL"));

        JButton btnGuardar = crearBotonDorado("Guardar tasación");
        btnGuardar.addActionListener(e -> {
            try {
                double nuevoPrecio = Double.parseDouble(txtPrecio.getText().trim());
                Condition nuevaCondicion = (Condition) comboCondition.getSelectedItem();

                if (p.getOwner() instanceof Client) {
                    Client propietario = (Client) p.getOwner();
                    empleadoActual.appraiseSecondHandProduct(propietario, p, nuevaCondicion, nuevoPrecio);
                    JOptionPane.showMessageDialog(this, "Intercambio tasado y modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarIntercambios();
                    cardLayoutCentral.show(panelContenedorCentral, "VISTA_PRINCIPAL");
                } else {
                    JOptionPane.showMessageDialog(this, "Error: El propietario del producto no es un cliente válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El precio de tasación debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Asegúrate de que tienes los permisos suficientes.\nError al modificar: " + ex.getMessage(), "Error de Permisos", JOptionPane.ERROR_MESSAGE);
            }
        });

        pnlBotones.add(btnCancelar);
        pnlBotones.add(btnGuardar);

        wrapper.add(pnlBanner, BorderLayout.NORTH);
        wrapper.add(pnlCentro, BorderLayout.CENTER);
        wrapper.add(pnlBotones, BorderLayout.SOUTH);

        return wrapper;
    }

    // MÉTODOS AUXILIARES DE ESTILOS
    private JLabel crearBadge(String texto) {
        JLabel badge = new JLabel(texto, SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(new Color(255, 215, 0));
        badge.setForeground(Color.BLACK);
        badge.setFont(new Font("Arial", Font.BOLD, 11));
        return badge;
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
            Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(scaled));
        } else {
            imgLabel.setText("👤");
            imgLabel.setForeground(Color.WHITE);
        }
    }

    private File encontrarArchivo(String[] rutas) {
        for (String r : rutas) {
            File f = new File(r);
            if (f.exists()) return f;
        }
        return null;
    }

    // BARRA DE NAVEGACIÓN
    private void setupBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COLOR_FONDO_NAV);
        barra.setPreferredSize(new Dimension(1000, 80));

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nav.setOpaque(false);

        btnProductos = crearBotonNav("Productos", false);
        btnIntercambios = crearBotonNav("Intercambios", true);
        btnPedidos = crearBotonNav("Pedidos", false);

        btnProductos.addActionListener(e -> ventana.mostrarPantalla("PRODUCTOS_EMPLEADO"));

        btnIntercambios.addActionListener(e -> {
            cardLayoutCentral.show(panelContenedorCentral, "VISTA_PRINCIPAL");
            ventana.mostrarPantalla("INTERCAMBIOS_EMPLEADO");
        });

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
                "foto/logoPerfilProvisional2.png"
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

        // Si no hay imagen, ponemos texto estándar
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
}