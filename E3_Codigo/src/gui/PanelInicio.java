package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class PanelInicio extends JPanel {

    private VentanaPrincipal ventanaPadre;

    public PanelInicio(VentanaPrincipal ventanaPadre) {
        this.ventanaPadre = ventanaPadre;

        // El layout principal será BorderLayout (Norte para la barra, Centro para el contenido)
        this.setLayout(new BorderLayout());

        // =========================================================
        // 1. BARRA DE NAVEGACIÓN SUPERIOR (ZONA NORTE)
        // =========================================================
        JPanel barraNavegacion = new JPanel(new BorderLayout());
        barraNavegacion.setBackground(new Color(51, 66, 90));
        // ¡NUEVO TAMAÑO!: Aumentamos el padding superior e inferior de 10 a 20 para hacer la barra más alta
        barraNavegacion.setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- 1.A PARTE IZQUIERDA (Logo y Menú) ---
        // Aumentamos ligeramente la separación entre elementos (de 20 a 25)
        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        panelIzquierdo.setOpaque(false);

        // Logo Horizontal
        URL imgUrl = getClass().getResource("../foto/logoHorizontal.png");
        if (imgUrl != null) {
            ImageIcon iconOriginal = new ImageIcon(imgUrl);
            // ¡NUEVO TAMAÑO!: Aumentamos el ancho del logo de 120 a 180 píxeles
            int anchoDeseado = 180;
            Image imgEscalada = iconOriginal.getImage().getScaledInstance(anchoDeseado, -1, Image.SCALE_SMOOTH);
            panelIzquierdo.add(new JLabel(new ImageIcon(imgEscalada)));
        } else {
            JLabel logoTexto = new JLabel("RONGERO");
            logoTexto.setFont(new Font("Arial", Font.BOLD, 26)); // ¡NUEVO TAMAÑO! (De 20 a 26)
            logoTexto.setForeground(Color.WHITE);
            panelIzquierdo.add(logoTexto);
        }

        // Botones de navegación (El tamaño se ajusta abajo en el método auxiliar)
        JButton btnInicio = crearBotonMenu("INICIO", true);
        JButton btnProductos = crearBotonMenu("PRODUCTOS", false);
        JButton btnIntercambios = crearBotonMenu("INTERCAMBIOS", false);

        panelIzquierdo.add(btnInicio);
        panelIzquierdo.add(btnProductos);
        panelIzquierdo.add(btnIntercambios);

        // --- 1.B PARTE DERECHA (Búsqueda, Perfil, Carrito) ---
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelDerecho.setOpaque(false);

        // Cuadro de Búsqueda
        JTextField campoBusqueda = new JTextField(15);
        campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 16));
        campoBusqueda.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        panelDerecho.add(campoBusqueda);

        // ==========================================
        // Botón de Búsqueda (NUEVO CON IMAGEN)
        // ==========================================
        JButton btnBuscar = crearBotonMenu("", false); // Sin texto
        URL urlLupa = getClass().getResource("../foto/logoLupaProvisional.png");
        if (urlLupa != null) {
            ImageIcon iconLupa = new ImageIcon(urlLupa);
            // Escalamos a 30x30 (un pelín más pequeña que el perfil para que cuadre bien con el campo de texto)
            Image imgLupa = iconLupa.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            btnBuscar.setIcon(new ImageIcon(imgLupa));
        } else {
            // Sistema de seguridad
            btnBuscar.setText("🔍");
            btnBuscar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        }
        panelDerecho.add(btnBuscar);

        // ==========================================
        // Botón Mi Perfil (NUEVO CON IMAGEN)
        // ==========================================
        JButton btnPerfil = crearBotonMenu("", false); // Lo creamos sin texto
        URL urlPerfil = getClass().getResource("../foto/logoPerfilProvisional.png");
        if (urlPerfil != null) {
            ImageIcon iconPerfil = new ImageIcon(urlPerfil);
            // Escalamos a 35x35 para que quede elegante en la barra
            Image imgPerfil = iconPerfil.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            btnPerfil.setIcon(new ImageIcon(imgPerfil));
        } else {
            // Sistema de seguridad: si no encuentra la imagen, pone el emoji
            btnPerfil.setText("👤");
            btnPerfil.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        }
        panelDerecho.add(btnPerfil);

        // ==========================================
        // Botón Carrito (NUEVO CON IMAGEN)
        // ==========================================
        JButton btnCarrito = crearBotonMenu("", false); // Lo creamos sin texto
        URL urlCarrito = getClass().getResource("../foto/logoCarritoProvisional.png");
        if (urlCarrito != null) {
            ImageIcon iconCarrito = new ImageIcon(urlCarrito);
            // Escalamos a 35x35
            Image imgCarrito = iconCarrito.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            btnCarrito.setIcon(new ImageIcon(imgCarrito));
        } else {
            // Sistema de seguridad
            btnCarrito.setText("🛒");
            btnCarrito.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        }
        panelDerecho.add(btnCarrito);

        // Ensamblamos la barra superior
        barraNavegacion.add(panelIzquierdo, BorderLayout.WEST);
        barraNavegacion.add(panelDerecho, BorderLayout.EAST);

        this.add(barraNavegacion, BorderLayout.NORTH);

        // =========================================================
        // 2. CONTENIDO PRINCIPAL (ZONA CENTRO)
        // =========================================================
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(new Color(240, 242, 245));
        panelCentral.setLayout(new GridBagLayout());

        JLabel mensajeConstruccion = new JLabel("Aquí irán los productos destacados");
        mensajeConstruccion.setFont(new Font("Arial", Font.ITALIC, 24));
        mensajeConstruccion.setForeground(Color.GRAY);
        panelCentral.add(mensajeConstruccion);

        this.add(panelCentral, BorderLayout.CENTER);


        // =========================================================
        // 3. EVENTOS (Navegación básica)
        // =========================================================
        btnPerfil.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Sesión cerrada (Prueba)");
            ventanaPadre.mostrarPantalla("LOGIN");
        });
    }

    /**
     * Metodo auxiliar para crear botones transparentes y con el mismo estilo para el menú superior.
     */
    private JButton crearBotonMenu(String texto, boolean activo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 18));
        boton.setForeground(activo ? Color.WHITE : new Color(180, 190, 200));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}