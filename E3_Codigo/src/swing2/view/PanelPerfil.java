package swing2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import users.Client;
import users.RegisteredUser;
import transactions.Order;
import utils.Notification;

public class PanelPerfil extends JPanel {
    private RegisteredUser usuario;
    private VentanaPrincipa ventana;
    private Image imagenFondo;
    private JLabel lblFotoPerfil;

    // Formateador para las fechas de las tablas
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")
            .withZone(ZoneId.systemDefault());

    public PanelPerfil(RegisteredUser usuario, VentanaPrincipa ventana) {
        this.usuario = usuario;
        this.ventana = ventana;

        cargarImagenFondo();
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        setupHeader();

        JPanel contenedorCentral = new JPanel(new BorderLayout(0, 25));
        contenedorCentral.setOpaque(false);

        // Superior: Datos y Botones
        JPanel filaSuperior = new JPanel(new GridLayout(1, 2, 25, 0));
        filaSuperior.setOpaque(false);
        filaSuperior.add(crearPanelDatos());
        filaSuperior.add(crearPanelAcciones());

        // Inferior: Historial y Notificaciones
        contenedorCentral.add(filaSuperior, BorderLayout.NORTH);
        contenedorCentral.add(crearPanelTablas(), BorderLayout.CENTER);

        add(contenedorCentral, BorderLayout.CENTER);
    }

    private void setupHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        URL gifUrl = getClass().getResource("/foto/flecha.gif");
        JLabel lblVolver = new JLabel(gifUrl != null ? new ImageIcon(gifUrl) : null);
        if (lblVolver.getIcon() == null) lblVolver.setText("← Volver");

        lblVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { ventana.mostrarPantalla("INICIO"); }
        });

        JLabel titulo = new JLabel("MI PERFIL", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 35));
        titulo.setForeground(Color.WHITE);

        header.add(lblVolver, BorderLayout.WEST);
        header.add(titulo, BorderLayout.CENTER);
        header.add(PanelInicioo.crearPanelLogo(), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private JPanel crearPanelDatos() {
        JPanel p = crearPanelEstilizado();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel t = new JLabel("INFORMACIÓN PERSONAL");
        t.setForeground(new Color(0, 178, 255));
        t.setFont(new Font("Arial", Font.BOLD, 20));

        p.add(t); p.add(Box.createVerticalStrut(15));
        p.add(crearDatoLbl("Usuario: ", usuario.getUsername()));
        p.add(crearDatoLbl("Email: ", (usuario instanceof Client ? ((Client)usuario).getEmail() : "N/A")));
        p.add(crearDatoLbl("Nombre: ", usuario.getFullname()));
        p.add(crearDatoLbl("DNI: ", usuario.MaskedDni()));

        return p;
    }

    private JPanel crearPanelAcciones() {
        JPanel p = crearPanelEstilizado();
        p.setLayout(new BorderLayout(20, 0));

        lblFotoPerfil = new JLabel();
        lblFotoPerfil.setPreferredSize(new Dimension(150, 150));
        lblFotoPerfil.setBorder(BorderFactory.createLineBorder(new Color(0, 178, 255), 2));
        actualizarFotoUI();

        JPanel botones = new JPanel(new GridLayout(3, 1, 0, 10));
        botones.setOpaque(false);

        JButton btnPass = crearBoton("Cambiar contraseña");
        JButton btnFoto = crearBoton("Actualizar foto");
        JButton btnLogout = crearBoton("Cerrar Sesión");
        btnLogout.setBackground(new Color(180, 40, 40));

        btnPass.addActionListener(e -> gestionarCambioPassword());
        btnFoto.addActionListener(e -> gestionarCambioFoto());
        btnLogout.addActionListener(e -> ventana.cambiarSesion(null));

        botones.add(btnPass); botones.add(btnFoto); botones.add(btnLogout);
        p.add(lblFotoPerfil, BorderLayout.WEST);
        p.add(botones, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearPanelTablas() {
        JPanel p = new JPanel(new GridLayout(1, 2, 25, 0));
        p.setOpaque(false);

        // TABLA PEDIDOS (Izquierda)
        p.add(crearTablaPersonalizada("MIS PEDIDOS", obtenerMatrizPedidos(),
                new String[]{"ID", "Fecha", "Total", "Estado"}));

        // TABLA NOTIFICACIONES (Derecha)
        p.add(crearTablaPersonalizada("NOTIFICACIONES", obtenerMatrizNotificaciones(),
                new String[]{"Fecha", "Mensaje"}));



        return p;
    }

    // --- MÉTODOS DE EXTRACCIÓN DE DATOS (Arreglan el problema de impresión) ---

    private Object[][] obtenerMatrizPedidos() {
        if (!(usuario instanceof Client)) return new Object[0][0];
        List<Order> pedidos = ((Client)usuario).getOrders();
        if (pedidos == null || pedidos.isEmpty()) return new Object[0][0];

        Object[][] data = new Object[pedidos.size()][4];
        for (int i = 0; i < pedidos.size(); i++) {
            Order o = pedidos.get(i);
            data[i][0] = "#" + o.getOrderId();
            data[i][1] = (o.getPaidAt() != null) ? fmt.format(o.getPaidAt()) : "---";
            data[i][2] = String.format("%.2f€", o.getPrice());
            data[i][3] = o.getOrderStatus();
        }
        return data;
    }

    private Object[][] obtenerMatrizNotificaciones() {
        List<Notification> todas = usuario.getMyNotifications();
        if (todas == null) return new Object[0][0];

        // FILTRO: Solo las que NO han sido leídas
        List<Notification> noLeidas = todas.stream()
                .filter(n -> !n.isRead())
                .toList();

        if (noLeidas.isEmpty()) return new Object[0][0];

        Object[][] data = new Object[noLeidas.size()][2];
        for (int i = 0; i < noLeidas.size(); i++) {
            Notification n = noLeidas.get(i);
            data[i][0] = fmt.format(java.time.Instant.now()); // O n.receivedAt si tienes el getter
            // Limpiamos el texto para que solo salga el mensaje en la tabla
            String texto = n.toString();
            data[i][1] = texto.contains(":") ? texto.substring(texto.indexOf(":") + 1).trim() : texto;
        }
        return data;
    }

    private void actualizarTablasUI() {
        // Eliminamos el panel inferior actual y lo volvemos a crear
        // Nota: Para que esto funcione de forma sencilla, puedes hacer que crearPanelTablas
        // se guarde en una variable de clase o simplemente refrescar la pantalla entera:
        ventana.mostrarPantalla("PERFIL");
    }

    // --- LÓGICA DE GESTIÓN ---
    private void gestionarLecturaNotificacion(int index) {
        // 1. Obtener las notificaciones NO LEÍDAS (ya que la tabla filtra por ellas)
        List<Notification> todas = usuario.getMyNotifications();
        List<Notification> pendientes = todas.stream()
                .filter(n -> !n.isRead())
                .toList();

        if (index >= 0 && index < pendientes.size()) {
            Notification n = pendientes.get(index);

            // 2. Mostrar el mensaje
            String mensajeCompleto = n.toString(); // Tu toString ya formatea bonito
            int opcion = JOptionPane.showConfirmDialog(this,
                    mensajeCompleto,
                    "Detalle de Notificación",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);

            // 3. Al darle a Aceptar (u OK), marcar como leída y refrescar
            if (opcion == JOptionPane.OK_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                n.markAsRead(true);

                // 4. Refrescar la interfaz para que desaparezca de la lista
                actualizarTablasUI();
            }
        }
    }

    private void gestionarCambioPassword() {
        JPasswordField pf = new JPasswordField();
        int ok = JOptionPane.showConfirmDialog(this, pf, "Nueva contraseña (8+ carac. y 1 Mayus):", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            String pass = new String(pf.getPassword());
            if (pass.length() >= 8 && pass.matches(".*[A-Z].*")) {
                usuario.Password(pass);
                JOptionPane.showMessageDialog(this, "Contraseña actualizada.");
            } else {
                JOptionPane.showMessageDialog(this, "Debe tener 8 caracteres y una mayúscula.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void gestionarCambioFoto() {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            usuario.Foto(jfc.getSelectedFile().getAbsolutePath());
            actualizarFotoUI();
        }
    }

    private void actualizarFotoUI() {
        if (usuario.getFoto() != null && !usuario.getFoto().isEmpty()) {
            ImageIcon icon = new ImageIcon(usuario.getFoto());
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblFotoPerfil.setIcon(new ImageIcon(img));
            lblFotoPerfil.setText("");
        } else {
            lblFotoPerfil.setText("SIN FOTO");
            lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
            lblFotoPerfil.setForeground(Color.GRAY);
        }
    }

    // --- COMPONENTES ESTILIZADOS ---

    private JPanel crearTablaPersonalizada(String titulo, Object[][] datos, String[] cabecera) {
        JPanel p = crearPanelEstilizado();
        p.setLayout(new BorderLayout(0, 10));

        JLabel lbl = new JLabel(titulo);
        lbl.setForeground(new Color(0, 178, 255));
        lbl.setFont(new Font("Arial", Font.BOLD, 16));

        DefaultTableModel model = new DefaultTableModel(datos, cabecera) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable t = new JTable(model);
        t.setBackground(new Color(20, 20, 50));
        t.setForeground(Color.WHITE);
        t.setRowHeight(30);
        t.getTableHeader().setBackground(new Color(40, 40, 80));
        t.getTableHeader().setForeground(Color.WHITE);

        if (titulo.equals("NOTIFICACIONES")) {
            t.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int fila = t.getSelectedRow();
                    if (fila != -1) {
                        gestionarLecturaNotificacion(fila);
                    }
                }
            });
        }

        JScrollPane sp = new JScrollPane(t);
        sp.getViewport().setBackground(new Color(20, 20, 50));
        sp.setBorder(null);

        p.add(lbl, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearPanelEstilizado() {
        JPanel p = new JPanel();
        p.setBackground(new Color(10, 30, 80, 230));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 150), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return p;
    }

    private JLabel crearDatoLbl(String t1, String t2) {
        JLabel l = new JLabel("<html><font color='#00B2FF'><b>" + t1 + "</b></font> <font color='white'>" + (t2 != null ? t2 : "---") + "</font></html>");
        l.setFont(new Font("Arial", Font.PLAIN, 16));
        l.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return l;
    }

    private JButton crearBoton(String t) {
        JButton b = new JButton(t);
        b.setBackground(new Color(80, 40, 200));
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