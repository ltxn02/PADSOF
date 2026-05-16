package swing2.view;

import catalog.SecondHandProduct;
import logic.Application;
import transactions.Exchange;
import transactions.ExchangeOffer;
import utils.ExchangeOfferStatus;
import users.Client;
import users.RegisteredUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import utils.ItemType;

public class PanelIntercambios extends JPanel {
    private Image imagenFondo;
    private JPanel panelCuerpo;
    private JPanel contenedorCentral;
    private JScrollPane scrollProductos;
    private RegisteredUser usuarioActual;
    private VentanaPrincipa ventana;

    public static final Color TEXT_DIM = new Color(140, 150, 190);
    public static final Color ACCENT_CYAN = new Color(0, 220, 255);
    public static final Color BG_CARD = new Color(16, 20, 42, 240);
    public static final Color BG_CARD_ALT = new Color(20, 25, 52, 240);
    public static final Color TEXT_PRIMARY = new Color(230, 235, 255);

    private JButton btnInicio, btnProductos, btnIntercambios;
    private final Color COLOR_FONDO_NAV = new Color(26, 26, 75, 200);
    private final Color COLOR_ACTIVO = new Color(0, 178, 255);

    
    
    
    public PanelIntercambios(VentanaPrincipa ventana, RegisteredUser usuarioActual) {
        this.ventana = ventana;
        this.usuarioActual = usuarioActual;
        setLayout(new BorderLayout());
        cargarImagenFondo();
        setupBarraSuperior();

        panelCuerpo = new JPanel(new BorderLayout());
        panelCuerpo.setOpaque(false);

        contenedorCentral = new JPanel(new GridLayout(0, 3, 25, 25));
        contenedorCentral.setOpaque(false);
        contenedorCentral.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel wrapperGrid = new JPanel(new BorderLayout());
        wrapperGrid.setOpaque(false);
        wrapperGrid.add(contenedorCentral, BorderLayout.NORTH);

        scrollProductos = new JScrollPane(wrapperGrid);
        scrollProductos.setOpaque(false);
        scrollProductos.getViewport().setOpaque(false);
        scrollProductos.setBorder(null);
        scrollProductos.getVerticalScrollBar().setUnitIncrement(16);

        add(panelCuerpo, BorderLayout.CENTER);
        marcarActivo(btnIntercambios);
        cargarIntercambios();
    }

    
    
    
    private File encontrarArchivo(String nombre, String subcarpeta) {
        String[] rutas = {
                "E3_Codigo/src/" + subcarpeta + "/" + nombre,
                "src/" + subcarpeta + "/" + nombre,
                "../src/" + subcarpeta + "/" + nombre,
                nombre
        };
        for (String r : rutas) {
            File f = new File(r);
            if (f.exists()) return f;
        }
        return null;
    }

    
    
    
    public void cargarIntercambios() {
        actualizarVistaPrincipal("EXPLORAR");
        contenedorCentral.removeAll();
        List<SecondHandProduct> todos = Application.getSecondHandProducts();
        if (todos != null) {
            for (SecondHandProduct p : todos) {
                if (p.isAppraised() && p.isAvailable()
                        && (usuarioActual == null || !p.getOwner().equals(usuarioActual))) {
                    contenedorCentral.add(crearTarjeta(p, true));
                }
            }
        }
        finalizarCarga();
    }

    private void cargarMisProductos() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes iniciar sesión para ver tus productos.",
                    "Acceso denegado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        actualizarVistaPrincipal("MIS PRODUCTOS");
        contenedorCentral.removeAll();
        contenedorCentral.add(crearTarjetaAnadir());
        List<SecondHandProduct> todos = Application.getSecondHandProducts();
        if (todos != null) {
            for (SecondHandProduct p : todos) {
                if (p.getOwner().equals(usuarioActual)) {
                    contenedorCentral.add(crearTarjeta(p, true));
                }
            }
        }
        finalizarCarga();
    }

    private void cargarOfertasRecibidas() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes iniciar sesión para ver tus solicitudes.",
                    "Acceso denegado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!(usuarioActual instanceof Client)) return;

        actualizarVistaPrincipal("SOLICITUDES");
        contenedorCentral.removeAll();

        Client cliente = (Client) usuarioActual;
        List<ExchangeOffer> recibidas = cliente.obtenerMisOfertasRecibidos();

        if (recibidas != null && !recibidas.isEmpty()) {
            for (ExchangeOffer offer : new ArrayList<>(recibidas)) {
                if (offer.getEstado() == ExchangeOfferStatus.PENDIENTE) {
                    contenedorCentral.add(crearTarjetaOferta(offer, true));
                }
            }
        } else {
            mostrarMensajeVacio("No tienes solicitudes pendientes.");
        }
        finalizarCarga();
    }

    private void cargarOfertasEnviadas() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes iniciar sesión para ver tus ofertas.",
                    "Acceso denegado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!(usuarioActual instanceof Client)) return;

        actualizarVistaPrincipal("OFERTAS");
        contenedorCentral.removeAll();

        Client cliente = (Client) usuarioActual;
        List<ExchangeOffer> enviadas = cliente.getOffersMade();

        if (enviadas != null && !enviadas.isEmpty()) {
            for (ExchangeOffer offer : new ArrayList<>(enviadas)) {
                if (offer.getEstado() == ExchangeOfferStatus.PENDIENTE) {
                    contenedorCentral.add(crearTarjetaOferta(offer, false));
                }
            }
        } else {
            mostrarMensajeVacio("No has enviado ninguna oferta aún.");
        }
        finalizarCarga();
    }

    
    
    
    private void actualizarVistaPrincipal(String titulo) {
        panelCuerpo.removeAll();
        panelCuerpo.add(buildSubMenu(titulo), BorderLayout.NORTH);
        panelCuerpo.add(scrollProductos, BorderLayout.CENTER);
    }

    private void mostrarMensajeVacio(String msg) {
        JLabel lbl = new JLabel(msg);
        lbl.setForeground(TEXT_DIM);
        lbl.setFont(new Font("Arial", Font.ITALIC, 16));
        contenedorCentral.add(lbl);
    }

    private void finalizarCarga() {
        contenedorCentral.revalidate();
        contenedorCentral.repaint();
        panelCuerpo.revalidate();
        panelCuerpo.repaint();
    }

    
    
    
    private JPanel buildSubMenu(String activo) {
        JPanel menu = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        menu.setOpaque(false);

        String[] nombres = {"EXPLORAR", "MIS PRODUCTOS", "SOLICITUDES", "OFERTAS"};
        for (String n : nombres) {
            JButton b = new JButton(n);
            b.setForeground(n.equals(activo) ? ACCENT_CYAN : Color.WHITE);
            b.setFont(new Font("Arial", Font.BOLD, 12));
            b.setContentAreaFilled(false);
            b.setBorder(n.equals(activo) ? BorderFactory.createMatteBorder(0,0,2,0, ACCENT_CYAN) : null);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (n.equals("EXPLORAR"))       b.addActionListener(e -> cargarIntercambios());
            if (n.equals("MIS PRODUCTOS"))  b.addActionListener(e -> cargarMisProductos());
            if (n.equals("SOLICITUDES"))    b.addActionListener(e -> cargarOfertasRecibidas());
            if (n.equals("OFERTAS"))        b.addActionListener(e -> cargarOfertasEnviadas());

            menu.add(b);
        }
        return menu;
    }

    
    
    
    private JPanel crearTarjeta(SecondHandProduct p, boolean clicable) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BG_CARD, 0, getHeight(), BG_CARD_ALT));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(280, 440));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        if (clicable) {
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    mostrarDetalleIntercambio(p);
                }
            });
        }

        
        JLabel imgLabel = new JLabel("", SwingConstants.CENTER);
        imgLabel.setPreferredSize(new Dimension(230, 250));
        if (p.getFotos() != null && !p.getFotos().isEmpty()) {
            File f = encontrarArchivo(new File(p.getFotos().get(0)).getName(), "imgProductos");
            if (f != null) {
                Image img = new ImageIcon(f.getAbsolutePath()).getImage()
                        .getScaledInstance(230, 250, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
            }
        }

        
        JPanel footer = new JPanel(new GridLayout(2, 1));
        footer.setOpaque(false);
        JLabel name = new JLabel(p.getName());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel owner = new JLabel("por " + p.getOwner().getUsername());
        owner.setForeground(ACCENT_CYAN);

        footer.add(name);
        footer.add(owner);

        card.add(imgLabel, BorderLayout.CENTER);
        card.add(footer, BorderLayout.SOUTH);
        return card;
    }

    private JPanel crearTarjetaAnadir() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(280, 440));
        JLabel lbl = new JLabel("+ Subir Producto");
        lbl.setForeground(Color.WHITE);
        card.add(lbl);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarFormularioSubirProducto();
            }
        });
        return card;
    }

    private JPanel crearTarjetaOferta(ExchangeOffer of, boolean recibida) {
        JPanel card = new JPanel(new BorderLayout(0, 12)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                GradientPaint gp = recibida
                        ? new GradientPaint(0, 0, new Color(0, 220, 255, 100), getWidth(), 0, new Color(100, 230, 100, 100))
                        : new GradientPaint(0, 0, new Color(255, 70, 160, 100), getWidth(), 0, new Color(0, 220, 255, 100));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), 6, 20, 20);

                g2.setColor(new Color(255, 255, 255, 15));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(300, 180));
        card.setBorder(new EmptyBorder(20, 18, 15, 18));

        String dir = recibida ? "↙ RECIBIDA" : "↗ ENVIADA";
        Color col = recibida ? new Color(150, 255, 100) : new Color(255, 100, 180);
        JLabel dirLabel = new JLabel(dir);
        dirLabel.setFont(new Font("Arial", Font.BOLD, 10));
        dirLabel.setForeground(col);

        String prodsOfrecidos = of.getOfferedProducts().get(0).getName();
        if (of.getOfferedProducts().size() > 1)
            prodsOfrecidos += " (+" + (of.getOfferedProducts().size()-1) + ")";

        JLabel titleLabel = new JLabel(prodsOfrecidos + "  ⇄  " + of.getRequestedProduct().getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel fromLabel = new JLabel("De: " + of.getOfferor().getUsername());
        fromLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        fromLabel.setForeground(TEXT_DIM);

        JPanel info = new JPanel(new GridLayout(3, 1, 0, 4));
        info.setOpaque(false);
        info.add(dirLabel);
        info.add(titleLabel);
        info.add(fromLabel);
        card.add(info, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);

        if (recibida) {
            JButton btnAceptar = new JButton("✓ Aceptar");
            JButton btnRechazar = new JButton("✕ Rechazar");

            btnAceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnRechazar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnAceptar.addActionListener(e -> {
                of.aceptaroferta();
                cargarOfertasRecibidas();
            });
            btnRechazar.addActionListener(e -> {
                of.reject_offer();
                cargarOfertasRecibidas();
            });

            btnRow.add(btnRechazar);
            btnRow.add(btnAceptar);
        } else {
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCancelar.addActionListener(e -> {
                of.cancelOffer();
                cargarOfertasEnviadas();
            });
            btnRow.add(btnCancelar);
        }

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetalleOferta(of);
            }
        });

        card.add(btnRow, BorderLayout.SOUTH);
        return card;
    }

    
    
    
    public void mostrarDetalleIntercambio(SecondHandProduct p) {
        panelCuerpo.removeAll();

        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.setBorder(new EmptyBorder(30, 80, 30, 80));

        JPanel detailCard = new JPanel(new GridLayout(1, 2, 50, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BG_CARD, 0, getHeight(), BG_CARD_ALT));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
                g2.dispose();
            }
        };
        detailCard.setOpaque(false);
        detailCard.setBorder(new EmptyBorder(40, 50, 40, 50));

        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JButton btnVolver = new JButton("← Volver al catálogo");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 12));
        btnVolver.setForeground(TEXT_PRIMARY);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorder(null);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVolver.addActionListener(e -> cargarIntercambios());

        JLabel lblTitulo = new JLabel(p.getName());
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDuenio = new JLabel("Propietario: " + p.getOwner().getUsername());
        lblDuenio.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDuenio.setForeground(ACCENT_CYAN);
        lblDuenio.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea txtDesc = new JTextArea(p.getDescription());
        txtDesc.setFont(new Font("Arial", Font.PLAIN, 15));
        txtDesc.setForeground(new Color(200, 200, 220));
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);

        JScrollPane scrollDesc = new JScrollPane(txtDesc);
        scrollDesc.setBorder(null);
        scrollDesc.setOpaque(false);
        scrollDesc.getViewport().setOpaque(false);
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDesc.setPreferredSize(new Dimension(300, 150));

        JButton btnAccion = new JButton("OFERTAR INTERCAMBIO");
        btnAccion.setBackground(ACCENT_CYAN);
        btnAccion.setForeground(Color.BLACK);
        btnAccion.setFont(new Font("Arial", Font.BOLD, 14));
        btnAccion.setFocusPainted(false);
        btnAccion.setPreferredSize(new Dimension(200, 45));
        btnAccion.setMaximumSize(new Dimension(300, 45));
        btnAccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAccion.addActionListener(e -> gestionarSolicitudIntercambio(p));

        infoPanel.add(btnVolver);
        infoPanel.add(Box.createVerticalStrut(25));
        infoPanel.add(lblTitulo);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(lblDuenio);
        infoPanel.add(Box.createVerticalStrut(25));
        infoPanel.add(new JLabel("DESCRIPCIÓN") {{ setForeground(Color.GRAY); setFont(new Font("Arial", Font.BOLD, 10)); }});
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(scrollDesc);
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(btnAccion);

        
        JLabel lblFoto = new JLabel("", SwingConstants.CENTER);
        if (p.getFotos() != null && !p.getFotos().isEmpty()) {
            File f = encontrarArchivo(new File(p.getFotos().get(0)).getName(), "imgProductos");
            if (f != null) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(350, 400, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
            } else {
                lblFoto.setText("Imagen no encontrada");
                lblFoto.setForeground(Color.GRAY);
            }
        }

        detailCard.add(infoPanel);
        detailCard.add(lblFoto);
        outer.add(detailCard, BorderLayout.CENTER);
        panelCuerpo.add(outer, BorderLayout.CENTER);
        finalizarCarga();
    }

    public void mostrarDetalleOferta(ExchangeOffer offer) {
        panelCuerpo.removeAll();

        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.setBorder(new EmptyBorder(30, 60, 30, 60));

        JPanel detailCard = new JPanel(new BorderLayout(0, 20)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BG_CARD, 0, getHeight(), BG_CARD_ALT));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        detailCard.setOpaque(false);
        detailCard.setBorder(new EmptyBorder(30, 40, 30, 40));

        
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);

        JLabel lblTitulo = new JLabel("Resumen de la Propuesta #" + (offer.hashCode() % 1000));
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 26));
        lblTitulo.setForeground(ACCENT_CYAN);

        JLabel lblUsuarios = new JLabel("Solicitante:"
                + offer.getOfferor().getUsername() + "| Receptor: "
                + offer.getReceptor().getUsername() + " ");
        lblUsuarios.setForeground(TEXT_DIM);

        header.add(lblTitulo);
        header.add(lblUsuarios);
        detailCard.add(header, BorderLayout.NORTH);

        
        JPanel body = new JPanel(new GridLayout(1, 2, 40, 0));
        body.setOpaque(false);

        JPanel pnlOfrecido = new JPanel();
        pnlOfrecido.setLayout(new BoxLayout(pnlOfrecido, BoxLayout.Y_AXIS));
        pnlOfrecido.setOpaque(false);
        pnlOfrecido.add(new JLabel("PRODUCTOS OFRECIDOS") {{ setForeground(Color.GRAY); }});
        pnlOfrecido.add(Box.createVerticalStrut(10));
        for (SecondHandProduct prod : offer.getOfferedProducts()) {
            pnlOfrecido.add(crearMiniFilaProducto(prod));
            pnlOfrecido.add(Box.createVerticalStrut(5));
        }

        JPanel pnlPedido = new JPanel();
        pnlPedido.setLayout(new BoxLayout(pnlPedido, BoxLayout.Y_AXIS));
        pnlPedido.setOpaque(false);
        pnlPedido.add(new JLabel("PRODUCTO SOLICITADO") {{ setForeground(Color.GRAY); }});
        pnlPedido.add(Box.createVerticalStrut(10));
        pnlPedido.add(crearMiniFilaProducto(offer.getRequestedProduct()));

        body.add(pnlOfrecido);
        body.add(pnlPedido);
        detailCard.add(body, BorderLayout.CENTER);

        
        JButton btnVolver = new JButton(" Volver a la lista");
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            if (offer.getOfferor().equals(usuarioActual)) cargarOfertasEnviadas();
            else cargarOfertasRecibidas();
        });

        detailCard.add(btnVolver, BorderLayout.SOUTH);
        outer.add(detailCard, BorderLayout.CENTER);
        panelCuerpo.add(outer, BorderLayout.CENTER);
        finalizarCarga();
    }

    private JPanel crearMiniFilaProducto(SecondHandProduct p) {
        JPanel mini = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        mini.setOpaque(true);
        mini.setBackground(new Color(255, 255, 255, 10));

        JLabel img = new JLabel();
        if (p.getFotos() != null && !p.getFotos().isEmpty()) {
            File f = encontrarArchivo(new File(p.getFotos().get(0)).getName(), "imgProductos");
            if (f != null) {
                Image icono = new ImageIcon(f.getAbsolutePath()).getImage()
                        .getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                img.setIcon(new ImageIcon(icono));
            }
        }

        JLabel name = new JLabel(p.getName() + " (" + p.getPrice() + "€)");
        name.setForeground(Color.WHITE);

        mini.add(img);
        mini.add(name);
        return mini;
    }

    
    
    
    public void mostrarFormularioSubirProducto() {
        panelCuerpo.removeAll();
        panelCuerpo.add(buildSubMenu("MIS PRODUCTOS"), BorderLayout.NORTH);

        JPanel formCard = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BG_CARD, 0, getHeight(), BG_CARD_ALT));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        formCard.setOpaque(false);
        formCard.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("NUEVO PRODUCTO DE SEGUNDA MANO");
        lblTitulo.setFont(new Font("Georgia", Font.BOLD, 20));
        lblTitulo.setForeground(ACCENT_CYAN);

        JTextField txtNombre = new JTextField(20);
        JTextArea txtDesc = new JTextArea(4, 20);
        txtDesc.setLineWrap(true);

        JComboBox<ItemType> comboTipo = new JComboBox<>(ItemType.values());

        JButton btnImagen = new JButton("Seleccionar Imagen");
        JLabel lblRutaImagen = new JLabel("No seleccionada");
        lblRutaImagen.setForeground(TEXT_DIM);
        ArrayList<String> listaFotos = new ArrayList<>();

        btnImagen.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                listaFotos.clear();
                listaFotos.add(f.getAbsolutePath());
                lblRutaImagen.setText(f.getName());
            }
        });

        JButton btnPublicar = new JButton("PUBLICAR PARA VALORACIÓN");
        btnPublicar.setBackground(ACCENT_CYAN);
        btnPublicar.setFont(new Font("Arial", Font.BOLD, 13));

        btnPublicar.addActionListener(e -> {
            if (txtNombre.getText().isEmpty() || listaFotos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre e imagen obligatorios.");
                return;
            }

            SecondHandProduct nuevo = new SecondHandProduct(
                    txtNombre.getText(),
                    txtDesc.getText(),
                    listaFotos,
                    (ItemType) comboTipo.getSelectedItem(),
                    (Client) usuarioActual
            );

            Application.addSecondHandProduct(nuevo);
            JOptionPane.showMessageDialog(this, "¡Producto enviado! Pendiente de tasación.");
            cargarMisProductos();
        });

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(lblTitulo, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        formCard.add(new JLabel("Nombre:") {{ setForeground(Color.WHITE); }}, gbc);
        gbc.gridx = 1; formCard.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formCard.add(new JLabel("Categoría:") {{ setForeground(Color.WHITE); }}, gbc);
        gbc.gridx = 1; formCard.add(comboTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formCard.add(new JLabel("Descripción:") {{ setForeground(Color.WHITE); }}, gbc);
        gbc.gridx = 1; formCard.add(new JScrollPane(txtDesc), gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formCard.add(btnImagen, gbc);
        gbc.gridx = 1; formCard.add(lblRutaImagen, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 0, 10);
        formCard.add(btnPublicar, gbc);

        JPanel centrado = new JPanel(new GridBagLayout());
        centrado.setOpaque(false);
        centrado.add(formCard);

        panelCuerpo.add(centrado, BorderLayout.CENTER);
        finalizarCarga();
    }

    
    
    
    private void gestionarSolicitudIntercambio(SecondHandProduct deseado) {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes iniciar sesión para proponer un intercambio.",
                    "Acceso denegado", JOptionPane.WARNING_MESSAGE);
            ventana.mostrarPantalla("LOGIN");
            return;
        }

        List<SecondHandProduct> misProductosValidos = new ArrayList<>();
        for (SecondHandProduct miProd : Application.getSecondHandProducts()) {
            if (miProd.getOwner().equals(usuarioActual) &&
                    miProd.isAppraised() &&
                    miProd.isAvailable()) {
                misProductosValidos.add(miProd);
            }
        }

        if (misProductosValidos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No tienes productos valorados disponibles para ofrecer.",
                    "Sin stock", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DefaultListModel<SecondHandProduct> modeloLista = new DefaultListModel<>();
        for (SecondHandProduct p : misProductosValidos) modeloLista.addElement(p);

        JList<SecondHandProduct> listaSeleccion = new JList<>(modeloLista);
        listaSeleccion.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaSeleccion.setVisibleRowCount(6);

        JScrollPane scrollPane = new JScrollPane(listaSeleccion);
        scrollPane.setPreferredSize(new Dimension(300, 150));

        Object[] mensaje = {
                "Vas a solicitar: " + deseado.getName(),
                "Dueño: " + deseado.getOwner().getUsername(),
                "\nSelecciona los productos que quieres ofrecer a cambio:",
                "(Mantén pulsado Ctrl o Cmd para seleccionar varios)",
                scrollPane
        };

        int opcion = JOptionPane.showConfirmDialog(
                this, mensaje, "Propuesta de Intercambio Múltiple",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            List<SecondHandProduct> seleccionados = listaSeleccion.getSelectedValuesList();

            if (seleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar al menos un producto.");
                return;
            }

            ExchangeOffer exito = new ExchangeOffer(deseado,
                    (ArrayList<SecondHandProduct>) seleccionados, (Client) usuarioActual);

            if (exito != null) {
                String nombres = "";
                for (SecondHandProduct s : seleccionados) nombres += "- " + s.getName() + "\n";

                JOptionPane.showMessageDialog(this,
                        "¡Solicitud enviada con éxito!\nHas ofrecido:\n" + nombres +
                                "\nPor el producto: " + deseado.getName());

                cargarIntercambios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al procesar la solicitud.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
        btnProductos.addActionListener(e -> ventana.mostrarPantalla("CATALOGO"));
        btnIntercambios.addActionListener(e -> ventana.mostrarPantalla("INTERCAMBIOS"));

        nav.add(crearPanelLogo());
        nav.add(btnInicio);
        nav.add(btnProductos);
        nav.add(btnIntercambios);
        barra.add(nav, BorderLayout.WEST);
        add(barra, BorderLayout.NORTH);
    }

    private JButton crearBotonNav(String t) {
        JButton b = new JButton(t);
        b.setPreferredSize(new Dimension(140, 80));
        b.setForeground(Color.WHITE);
        b.setBackground(COLOR_FONDO_NAV);
        b.setBorder(null);
        b.setFocusPainted(false);
        return b;
    }

    private void cargarImagenFondo() {
        File f = encontrarArchivo("FondoCliente.png", "foto");
        if (f != null) imagenFondo = new ImageIcon(f.getAbsolutePath()).getImage();
    }

    private JPanel crearPanelLogo() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                File f = encontrarArchivo("logoHorizontal.png", "foto");
                if (f != null) g.drawImage(new ImageIcon(f.getAbsolutePath()).getImage(),
                        15, 7, 180, 65, null);
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(220, 80));
        return p;
    }

    private void marcarActivo(JButton b) {
        btnInicio.setBackground(COLOR_FONDO_NAV);
        btnProductos.setBackground(COLOR_FONDO_NAV);
        btnIntercambios.setBackground(COLOR_FONDO_NAV);
        b.setBackground(COLOR_ACTIVO);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }
}