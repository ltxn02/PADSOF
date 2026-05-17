package swing2.view.gestor.productos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import swing2.controller.gestor.GestorProductoController;
import swing2.view.VentanaPrincipa;

/**
 * Panel para añadir un nuevo producto.
 * Placeholder que sigue la estructura de PanelAnadirEmpleado.
 * 
 * @author Lidia Martin
 */
public class PanelAnadirProducto extends JPanel {
	private VentanaPrincipa ventanaPadre;
	private PanelGestionProductos panelPadre;

	// Colores
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_PANEL_FORMA = new Color(30, 45, 80);
	private static final Color COLOR_BOTON = new Color(52, 152, 219);
	private static final Color COLOR_BOTON_CANCELAR = new Color(200, 50, 50);

	/**
	 * Constructor de la clase PanelAnadirProducto.
	 * Configura el layout básico y el color de fondo para el panel de inserción.
	 * 
	 * @param ventanaPadre La ventana principal de la aplicación.
	 * @param panelPadre   El panel de gestión de productos que actúa como contenedor de navegación.
	 */
	public PanelAnadirProducto(VentanaPrincipa ventanaPadre, PanelGestionProductos panelPadre) {
		this.ventanaPadre = ventanaPadre;
		this.panelPadre = panelPadre;

		this.setLayout(new BorderLayout());
		this.setBackground(COLOR_FONDO);
		
		crearInterfaz();
	}

	/**
	 * Inicializa, distribuye y ensambla los componentes principales de la interfaz,
	 * como el área central del formulario y el panel inferior de botones.
	 */
	private void crearInterfaz() {
		// Panel principal centrado
		JPanel pnlCentral = new JPanel(new BorderLayout());
		pnlCentral.setOpaque(false);
		pnlCentral.setBorder(new EmptyBorder(40, 100, 40, 100));

		// Panel del formulario
		JPanel pnlFormulario = crearFormulario();
		pnlCentral.add(pnlFormulario, BorderLayout.CENTER);

		// Panel de botones de acción
		JPanel pnlBotones = crearPanelBotones();
		pnlCentral.add(pnlBotones, BorderLayout.SOUTH);

		this.add(pnlCentral, BorderLayout.CENTER);
	}

	/**
	 * Diseña y dibuja el contenedor del formulario con bordes redondeados y 
	 * posicionamiento GridBagLayout, incluyendo leyendas provisionales de desarrollo.
	 * 
	 * @return Un JPanel personalizado que representa la estructura física del formulario.
	 */
	private JPanel crearFormulario() {
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(COLOR_PANEL_FORMA);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
				g2.dispose();
			}
		};
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new EmptyBorder(30, 30, 30, 30));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;

		// Título
		JLabel lblTitulo = new JLabel("Añadir Nuevo Producto");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitulo.setForeground(Color.WHITE);
		gbc.gridwidth = 2;
		gbc.gridy = 0;
		panel.add(lblTitulo, gbc);

		// Espacio
		gbc.gridy = 1;
		panel.add(Box.createVerticalStrut(20), gbc);

		// Mensaje placeholder
		JLabel lblPlaceholder = new JLabel("Formulario de añadir producto - En desarrollo");
		lblPlaceholder.setFont(new Font("Arial", Font.ITALIC, 14));
		lblPlaceholder.setForeground(new Color(180, 180, 180));
		gbc.gridy = 2;
		panel.add(lblPlaceholder, gbc);

		gbc.gridy = 3;
		panel.add(Box.createVerticalStrut(30), gbc);

		return panel;
	}

	/**
	 * Crea y agrupa los botones encargados de confirmar la acción o de cancelar
	 * la operación regresando al listado principal.
	 * 
	 * @return Un JPanel que actúa como barra de herramientas inferior con los botones de acción.
	 */
	private JPanel crearPanelBotones() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		panel.setOpaque(false);

		JButton btnAceptar = crearBoton("✔ Guardar", COLOR_BOTON);
		JButton btnCancelar = crearBoton("✕ Cancelar", COLOR_BOTON_CANCELAR);

		btnAceptar.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, "Funcionalidad en desarrollo");
		});

		btnCancelar.addActionListener(e -> {
			panelPadre.mostrarListado();
		});

		panel.add(btnAceptar);
		panel.add(btnCancelar);

		return panel;
	}

	/**
	 * Factoría interna para construir botones con estética estilizada, 
	 * esquinas redondeadas, efectos de cursor y colores específicos de la aplicación.
	 * 
	 * @param texto Etiqueta textual visible que portará el botón.
	 * @param color Color de fondo que se aplicará al diseño del componente gráfico.
	 * @return Un JButton personalizado según las especificaciones provistas.
	 */
	private JButton crearBoton(String texto, Color color) {
		JButton btn = new JButton(texto) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(color);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
				g2.dispose();
				super.paintComponent(g);
			}
		};
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setFont(new Font("Arial", Font.BOLD, 14));
		btn.setForeground(Color.WHITE);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setPreferredSize(new Dimension(150, 45));
		return btn;
	}
}
