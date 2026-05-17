package swing2.view.empleado;

import logic.Application;
import swing2.view.VentanaPrincipa;
import transactions.Order;
import users.Client;
import users.Employee;
import users.RegisteredUser;
import utils.OrderStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PanelPedidosEmpleado extends JPanel {
    private VentanaPrincipa ventana;
    private Employee empleadoActual;

    private JButton btnProductos, btnIntercambios, btnPedidos;
    private final Color COLOR_FONDO_NAV = new Color(26, 26, 75, 200);
    private final Color COLOR_ACTIVO = new Color(220, 200, 140); // Dorado

    private CardLayout cardLayoutCentral;
    private JPanel panelContenedorCentral;
    private JPanel contenedorTabla;

    public PanelPedidosEmpleado(VentanaPrincipa ventana, Employee empleado) {
        this.ventana = ventana;
        this.empleadoActual = empleado;
        this.setLayout(new BorderLayout());
        this.setBackground(COLOR_ACTIVO);

        // 1. Barra de navegación superior
        setupBarraSuperior();

        // 2. Configurar el cardlayout
        cardLayoutCentral = new CardLayout();
        panelContenedorCentral = new JPanel(cardLayoutCentral);
        panelContenedorCentral.setOpaque(false);

        // Añadimos las cartas
        panelContenedorCentral.add(crearPanelTablaPedidos(), "TABLA_PEDIDOS");

        this.add(panelContenedorCentral, BorderLayout.CENTER);
    }

    // VISTA 1: LA TABLA DE PEDIDOS
    private JPanel crearPanelTablaPedidos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Título y filtros
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        pnlTop.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblTitulo = new JLabel("Pedidos");
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

        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setOpaque(false);

        // Cabecera de la tabla
        JPanel pnlCabecera = new JPanel(new GridLayout(1, 4, 10, 0)) {
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

        String[] headers = {"ID pedido", "ID comprador", "Fecha", "Estado de pedido"};
        for (String h : headers) {
            JLabel lblHeader = new JLabel(h, SwingConstants.CENTER);
            lblHeader.setForeground(Color.WHITE);
            lblHeader.setFont(new Font("Arial", Font.BOLD, 14));
            pnlCabecera.add(lblHeader);
        }
        pnlTabla.add(pnlCabecera, BorderLayout.NORTH);

        // Filas de pedidos
        contenedorTabla = new JPanel();
        contenedorTabla.setLayout(new BoxLayout(contenedorTabla, BoxLayout.Y_AXIS));
        contenedorTabla.setOpaque(false);

        cargarFilasPedidos(contenedorTabla);

        JScrollPane scroll = new JScrollPane(contenedorTabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        pnlTabla.add(scroll, BorderLayout.CENTER);
        panel.add(pnlTabla, BorderLayout.CENTER);

        return panel;
    }

    private void cargarFilasPedidos(JPanel pnlFilas) {
        pnlFilas.removeAll();
        List<Order> todosLosPedidos = obtenerTodosLosPedidos();

        if (todosLosPedidos.isEmpty()) {
            JLabel vacio = new JLabel("No hay pedidos registrados en el sistema.");
            vacio.setFont(new Font("Arial", Font.ITALIC, 16));
            vacio.setForeground(new Color(30, 45, 80));
            vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlFilas.add(Box.createRigidArea(new Dimension(0, 20)));
            pnlFilas.add(vacio);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());

            for (Order o : todosLosPedidos) {
                pnlFilas.add(Box.createRigidArea(new Dimension(0, 10)));

                String idPed = String.valueOf(o.getOrderId());
                String idComp = (o.getClient() != null) ? o.getClient().getUsername() : "Desconocido";

                // Si getOrderedAt es null, miramos si tiene getPaidAt. Si no, ponemos N/A
                String fecha = "N/A";
                if (o.getOrderedAt() != null) {
                    fecha = formatter.format(o.getOrderedAt());
                } else if (o.getPaidAt() != null) {
                    fecha = formatter.format(o.getPaidAt());
                }

                String estado = (o.getOrderStatus() != null) ? o.getOrderStatus().toString().replace("_", " ") : "DESCONOCIDO";

                pnlFilas.add(crearFilaPedidoInteractiva(o, idPed, idComp, fecha, estado));
            }
        }
        pnlFilas.revalidate();
        pnlFilas.repaint();
    }

    /**
     * Extrae los pedidos de todos los clientes.
     */
    private List<Order> obtenerTodosLosPedidos() {
        List<Order> listaGlobal = new ArrayList<>();
        for (RegisteredUser u : Application.getUsers()) {
            if (u instanceof Client) {
                Client c = (Client) u;
                try {
                    if (c.getOrders() != null) {
                        listaGlobal.addAll(c.getOrders());
                    }
                } catch (Exception e) {
                    // Ignoramos si falla al intentar leer de un cliente
                }
            }
        }
        return listaGlobal;
    }

    // Fila interactiva que al hacer clic abre la edición
    private JPanel crearFilaPedidoInteractiva(Order order, String idPed, String idComp, String fecha, String estado) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel fila = new JPanel(new GridLayout(1, 4, 10, 0)) {
            Color bgColor = Color.WHITE;

            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        // Limpiamos vistas de edición anteriores
                        Component[] comps = panelContenedorCentral.getComponents();
                        for (Component c : comps) {
                            if ("EDITAR_PEDIDO".equals(c.getName())) {
                                panelContenedorCentral.remove(c);
                            }
                        }

                        JPanel pnlEdit = crearPanelEditarPedido(order);
                        pnlEdit.setName("EDITAR_PEDIDO");
                        panelContenedorCentral.add(pnlEdit, "EDITAR_PEDIDO");
                        cardLayoutCentral.show(panelContenedorCentral, "EDITAR_PEDIDO");
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

        fila.add(crearLabelFilaOscuro(idPed));
        fila.add(crearLabelFilaOscuro(idComp));
        fila.add(crearLabelFilaOscuro(fecha));
        fila.add(crearLabelFilaOscuro(estado));

        wrapper.add(fila, BorderLayout.CENTER);
        return wrapper;
    }

    private JLabel crearLabelFilaOscuro(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 15));
        lbl.setForeground(new Color(30, 45, 80));
        return lbl;
    }


    // VISTA 2: EDICIÓN DE ESTADO DEL PEDIDO
    private JPanel crearPanelEditarPedido(Order order) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(COLOR_FONDO_NAV);

        // Cabecera dorada
        JPanel pnlBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        pnlBanner.setBackground(COLOR_ACTIVO);
        JLabel lblTitulo = new JLabel("Modificar estado - Pedido #" + order.getOrderId());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 45, 80));
        pnlBanner.add(lblTitulo);

        // Centro con el formulario
        JPanel pnlCentro = new JPanel(new GridBagLayout());
        pnlCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Cliente
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblCliente = new JLabel("Cliente (comprador):");
        lblCliente.setForeground(Color.WHITE);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 18));
        pnlCentro.add(lblCliente, gbc);

        gbc.gridx = 1;
        String clientName = (order.getClient() != null) ? order.getClient().getUsername() : "Desconocido";
        JLabel lblClienteVal = new JLabel(clientName);
        lblClienteVal.setForeground(Color.LIGHT_GRAY);
        lblClienteVal.setFont(new Font("Arial", Font.PLAIN, 18));
        pnlCentro.add(lblClienteVal, gbc);

        // Fila 2: Total
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblTotal = new JLabel("Total del pedido:");
        lblTotal.setForeground(Color.WHITE);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        pnlCentro.add(lblTotal, gbc);

        gbc.gridx = 1;
        JLabel lblTotalVal = new JLabel(String.format("%.2f €", order.getPrice()));
        lblTotalVal.setForeground(Color.LIGHT_GRAY);
        lblTotalVal.setFont(new Font("Arial", Font.PLAIN, 18));
        pnlCentro.add(lblTotalVal, gbc);

        // Fila 3: Combo box de estado
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblEstado = new JLabel("Estado actual:");
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 18));
        pnlCentro.add(lblEstado, gbc);

        gbc.gridx = 1;
        JComboBox<OrderStatus> comboStatus = new JComboBox<>(OrderStatus.values());
        if (order.getOrderStatus() != null) {
            comboStatus.setSelectedItem(order.getOrderStatus());
        }
        comboStatus.setFont(new Font("Arial", Font.BOLD, 16));
        pnlCentro.add(comboStatus, gbc);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        pnlBotones.setOpaque(false);
        pnlBotones.setBorder(new EmptyBorder(40, 0, 50, 0));

        JButton btnCancelar = crearBotonBlanco("Cancelar");
        btnCancelar.addActionListener(e -> cardLayoutCentral.show(panelContenedorCentral, "TABLA_PEDIDOS"));

        JButton btnGuardar = crearBotonDorado("Guardar estado");
        btnGuardar.addActionListener(e -> {
            OrderStatus nuevoEstado = (OrderStatus) comboStatus.getSelectedItem();

            try {
                // Aquí aplicamos el metodo que comprueba si el empleado tiene el PERMISO real
                empleadoActual.updateOrderStatus(order, nuevoEstado);

                JOptionPane.showMessageDialog(this, "El estado del pedido #" + order.getOrderId() + " ha sido actualizado a " + nuevoEstado + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Recargamos la tabla para ver el cambio
                cargarFilasPedidos(contenedorTabla);
                cardLayoutCentral.show(panelContenedorCentral, "TABLA_PEDIDOS");

            } catch (SecurityException ex) {
                // Si el empleado no tiene el permiso Permission.ORDER_STATUS_UPDATE, salta esto
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Acceso denegado", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error inesperado al cambiar el estado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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


    // BARRA DE NAVEGACIÓN
    private void setupBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COLOR_FONDO_NAV);
        barra.setPreferredSize(new Dimension(1000, 80));

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nav.setOpaque(false);

        btnProductos = crearBotonNav("Productos", false);
        btnIntercambios = crearBotonNav("Intercambios", false);
        btnPedidos = crearBotonNav("Pedidos", true); // Activo

        btnProductos.addActionListener(e -> ventana.mostrarPantalla("PRODUCTOS_EMPLEADO"));
        btnIntercambios.addActionListener(e -> ventana.mostrarPantalla("INTERCAMBIOS_EMPLEADO"));

        btnPedidos.addActionListener(e -> {
            cargarFilasPedidos(contenedorTabla); // Refrescar siempre al darle
            cardLayoutCentral.show(panelContenedorCentral, "TABLA_PEDIDOS");
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
        btnPerfil.setFocusPainted(false);
        btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Forzamos un tamaño mínimo para que nunca se vuelva invisible
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

        // Si no hay imagen, ponemos texto estándar (sin emojis que den problemas)
        if (!imagenCargada) {
            btnPerfil.setText("Perfil ▼");
            btnPerfil.setFont(new Font("Arial", Font.BOLD, 16));
            btnPerfil.setForeground(Color.WHITE);
            btnPerfil.setPreferredSize(new Dimension(100, 50)); // Lo hacemos más ancho para el texto
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