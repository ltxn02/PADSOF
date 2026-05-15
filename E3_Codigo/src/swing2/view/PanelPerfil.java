package swing2.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import users.Client;
import users.RegisteredUser;

public class PanelPerfil extends JPanel {
    private RegisteredUser usuario;
    private VentanaPrincipa ventana;
    private Image imagenFondo;

    public PanelPerfil(RegisteredUser usuario, VentanaPrincipa ventana) {
        this.usuario = usuario;
        this.ventana = ventana;

        cargarImagenFondo();
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // --- ENCABEZADO ---
        setupHeader();

        // --- CONTENIDO PRINCIPAL ---
        JPanel contenedorCentral = new JPanel(new GridBagLayout());
        contenedorCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // 1. Datos del Usuario
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4; gbc.weighty = 0.5;
        contenedorCentral.add(crearPanelDatos(), gbc);

        // 2. Foto y Opciones de Edición
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.6;
        contenedorCentral.add(crearPanelEdicion(), gbc);

        // 3. Historial de Pedidos (Abajo ocupando todo el ancho)
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weighty = 0.5;
        contenedorCentral.add(crearPanelPedidos(), gbc);

        add(contenedorCentral, BorderLayout.CENTER);
    }

    private void setupHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        // Reutilizamos el GIF de flecha que ya tienes
        JLabel lblVolver = new JLabel(new ImageIcon(getClass().getResource("/foto/flecha.gif")));
        lblVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ventana.mostrarPantalla("INICIO");
            }
        });

        JLabel titulo = new JLabel("MI PERFIL", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);

        header.add(lblVolver, BorderLayout.WEST);
        header.add(titulo, BorderLayout.CENTER);
        header.add(PanelInicioo.crearPanelLogo(), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private JPanel crearPanelDatos() {
        JPanel p = crearPanelEstilizado();
        p.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel lblUser = new JLabel("Usuario: " + usuario.getUsername());
        JLabel lblEmail = new JLabel("Email: " + (usuario instanceof Client ? ((Client)usuario).getEmail() : "N/A"));
        JLabel lblTipo = new JLabel("Rango: " + usuario.getClass().getSimpleName());

        styleLabel(lblUser, 18);
        styleLabel(lblEmail, 16);
        styleLabel(lblTipo, 16);

        p.add(new JLabel("INFORMACIÓN PERSONAL")).setForeground(new Color(0, 178, 255));
        p.add(lblUser);
        p.add(lblEmail);
        p.add(lblTipo);
        return p;
    }

    private JPanel crearPanelEdicion() {
        JPanel p = crearPanelEstilizado();
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        // Placeholder para la foto de perfil
        JLabel fotoPerfil = new JLabel();
        fotoPerfil.setPreferredSize(new Dimension(120, 120));
        fotoPerfil.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        fotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        fotoPerfil.setText("FOTO");
        fotoPerfil.setForeground(Color.WHITE);

        // Botones de acción (ahora con 3 filas)
        JPanel acciones = new JPanel(new GridLayout(3, 1, 10, 10));
        acciones.setOpaque(false);

        JButton btnPass = crearBoton("Cambiar Contraseña");
        JButton btnFoto = crearBoton("Subir Nueva Foto");
        JButton btnCerrarSesion = crearBoton("Cerrar Sesión");

        // Ponemos el botón de cerrar sesión en rojo para que destaque
        btnCerrarSesion.setBackground(new Color(220, 50, 50));

        btnPass.addActionListener(e -> JOptionPane.showInputDialog("Introduce tu nueva contraseña:"));
        btnFoto.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abriendo selector de archivos..."));

        // Lógica para cerrar sesión
        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que deseas cerrar sesión?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                ventana.cambiarSesion(null); // Esto redirige al LOGIN automáticamente
            }
        });

        acciones.add(btnPass);
        acciones.add(btnFoto);
        acciones.add(btnCerrarSesion);

        p.add(fotoPerfil);
        p.add(acciones);
        return p;
    }

    private JPanel crearPanelPedidos() {
        JPanel p = crearPanelEstilizado();
        p.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("MIS PEDIDOS RECIENTES");
        styleLabel(titulo, 18);
        titulo.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        // Lista de ejemplo (Aquí conectarías con tu lógica de facturas/pedidos)
        String[] columnas = {"ID Pedido", "Fecha", "Total", "Estado"};
        Object[][] datos = {
                {"#001", "12/05/2026", "45.99€", "Entregado"},
                {"#002", "13/05/2026", "120.00€", "En camino"}
        };

        JTable tabla = new JTable(datos, columnas);
        tabla.setBackground(new Color(30, 30, 60));
        tabla.setForeground(Color.WHITE);
        tabla.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(new Color(26, 26, 75));

        p.add(titulo, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    // --- MÉTODOS AUXILIARES DE ESTILO ---
    private JPanel crearPanelEstilizado() {
        JPanel p = new JPanel();
        p.setBackground(new Color(15, 45, 105, 200));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return p;
    }

    private void styleLabel(JLabel l, int size) {
        l.setFont(new Font("Arial", Font.BOLD, size));
        l.setForeground(Color.WHITE);
    }

    private JButton crearBoton(String t) {
        JButton b = new JButton(t);
        b.setBackground(new Color(110, 30, 230));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void cargarImagenFondo() {
        URL url = getClass().getResource("/foto/FondoCliente.png");
        if (url != null) imagenFondo = new ImageIcon(url).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (imagenFondo != null) g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        super.paintComponent(g);
    }
}