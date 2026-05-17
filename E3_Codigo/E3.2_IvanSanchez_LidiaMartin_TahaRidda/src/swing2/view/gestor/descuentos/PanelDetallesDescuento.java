package swing2.view.gestor.descuentos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import swing2.controller.gestor.GestorDescuentoController;
import discounts.*;

import java.awt.*;
import java.time.LocalDateTime;

/**
 * Panel que muestra los detalles detallados de un descuento específico
 * y proporciona opciones interactivas para su gestión, como la eliminación.
 * Forma parte de la interfaz de administración del Gestor.
 * * @author Lidia Martin
 */
public class PanelDetallesDescuento extends JPanel {
	private PanelGestionDescuentos panelPadre;
	private GestorDescuentoController ctrl;
	private IDiscount descuento;

	// COLORES
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_PANEL_INFO = new Color(40, 80, 140);
	private static final Color COLOR_LABEL = new Color(187, 192, 199);

	/**
	 * Constructor del panel de detalles de un descuento.
	 * Inicializa la estructura base, el esquema de colores y los bordes.
	 *
	 * @param panelPadre Referencia al panel principal contenedor para gestionar la navegación (volver o refrescar).
	 * @param ctrl       Controlador principal que gestiona la lógica de negocio de los descuentos.
	 */
	public PanelDetallesDescuento(PanelGestionDescuentos panelPadre, GestorDescuentoController ctrl) {
		this.panelPadre = panelPadre;
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(COLOR_FONDO);
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	/**
	 * Actualiza y reconstruye visualmente el panel para mostrar toda la información
	 * asociada a un descuento específico.
	 *
	 * @param descuento El objeto {@link IDiscount} del cual se extraerán los datos para mostrarlos.
	 */
	public void mostrarDetalles(IDiscount descuento) {
		this.descuento = descuento;

		this.removeAll();

		// BARRA SUPERIOR: Título + botón volver
		JPanel barraSuperior = crearBarraSuperior();
		this.add(barraSuperior, BorderLayout.NORTH);

		// CONTENIDO CENTRAL: Información del descuento
		JPanel contenidoPrincipal = crearContenidoPrincipal();
		JScrollPane scroll = new JScrollPane(contenidoPrincipal);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		this.add(scroll, BorderLayout.CENTER);

		// BARRA INFERIOR: Botones de acción
		JPanel barraInferior = crearBarraInferior();
		this.add(barraInferior, BorderLayout.SOUTH);

		this.revalidate();
		this.repaint();
	}

	/**
	 * Crea la barra superior de la vista, que incluye el título de la pantalla
	 * y el botón para regresar al listado general de descuentos.
	 *
	 * @return Un {@link JPanel} configurado con los elementos de la cabecera.
	 */
	// BARRA SUPERIOR
	private JPanel crearBarraSuperior() {
		JPanel barra = new JPanel(new BorderLayout());
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

		JButton btnVolver = new JButton("< Volver");
		btnVolver.setPreferredSize(new Dimension(150, 35));
		btnVolver.setBackground(new Color(52, 73, 94));
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setFont(new Font("Arial", Font.BOLD, 12));
		btnVolver.setBorder(null);
		btnVolver.setFocusPainted(false);
		btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnVolver.addActionListener(e -> panelPadre.mostrarListado());

		JLabel titulo = new JLabel("DETALLES DEL DESCUENTO");
		titulo.setForeground(Color.WHITE);
		titulo.setFont(new Font("Arial", Font.BOLD, 20));

		barra.add(btnVolver, BorderLayout.WEST);
		barra.add(titulo, BorderLayout.CENTER);

		return barra;
	}

	/**
	 * Ensambla el contenedor central de la vista, dividiéndolo de manera
	 * estructurada en secciones modulares (Información General, Aplicabilidad,
	 * Detalles Específicos y Estado).
	 *
	 * @return Un {@link JPanel} con diseño vertical que engloba todas las secciones informativas.
	 */
	// CONTENIDO PRINCIPAL
	private JPanel crearContenidoPrincipal() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(COLOR_FONDO);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// SECCIÓN 1: INFORMACIÓN GENERAL
		JPanel seccion1 = crearSeccion("INFORMACIÓN GENERAL");
		seccion1.add(crearFilaInfo("Tipo de descuento:", ctrl.obtenerTipoDescuento(descuento)));
		seccion1.add(crearFilaInfo("Descripción:", descuento.getDescription()));
		panel.add(seccion1);
		panel.add(Box.createVerticalStrut(20));

		// SECCIÓN 2: APLICABILIDAD
		JPanel seccion2 = crearSeccion("APLICABILIDAD");
		seccion2.add(crearFilaInfo("Categoría:", ctrl.obtenerCategoria(descuento)));
		panel.add(seccion2);
		panel.add(Box.createVerticalStrut(20));

		// SECCIÓN 3: DETALLES ESPECÍFICOS (depende del tipo)
		JPanel seccion3 = crearSeccionDetalles();
		if (seccion3 != null) {
			panel.add(seccion3);
			panel.add(Box.createVerticalStrut(20));
		}

		// SECCIÓN 4: ESTADO
		JPanel seccion4 = crearSeccion("ESTADO");
		String estado = descuento.isExpired() ? "❌ Expirado/No vigente" : "✅ Vigente";
		seccion4.add(crearFilaInfo("Vigencia:", estado));
		panel.add(seccion4);

		panel.add(Box.createVerticalGlue());

		return panel;
	}

	/**
	 * Construye una sección dinámica que varía dependiendo de la subclase
	 * específica a la que pertenezca el descuento seleccionado (PercentageDiscount,
	 * VolumeDiscount, GiftDiscount o QuantityDiscount).
	 *
	 * @return Un {@link JPanel} formateado con los detalles únicos del tipo de descuento,
	 * o {@code null} si el tipo no es reconocido.
	 */
	// SECCIÓN CON DETALLES ESPECÍFICOS DEL TIPO DE DESCUENTO
	private JPanel crearSeccionDetalles() {
		JPanel seccion = crearSeccion("DETALLES ESPECÍFICOS");

		if (descuento instanceof PercentageDiscount) {
			PercentageDiscount pd = (PercentageDiscount) descuento;
			seccion.add(crearFilaInfo("Tipo:", "Rebaja porcentual"));
			seccion.add(crearFilaInfo("Porcentaje:", String.format("%.1f%%", pd.getValue())));
			return seccion;

		} else if (descuento instanceof VolumeDiscount) {
			VolumeDiscount vd = (VolumeDiscount) descuento;
			seccion.add(crearFilaInfo("Tipo:", "Descuento por volumen"));
			seccion.add(crearFilaInfo("Gasto mínimo:", "€" + String.format("%.2f", vd.getThreshold())));
			seccion.add(crearFilaInfo("Descuento:", "€" + String.format("%.2f", vd.getValue())));
			return seccion;

		} else if (descuento instanceof GiftDiscount) {
			GiftDiscount gd = (GiftDiscount) descuento;
			seccion.add(crearFilaInfo("Tipo:", "Regalo"));
			seccion.add(crearFilaInfo("Gasto mínimo:", "€" + String.format("%.2f", gd.getMinGasto())));
			seccion.add(crearFilaInfo("Producto regalo:", gd.getRegalo().getName()));
			return seccion;

		} else if (descuento instanceof QuantityDiscount) {
			QuantityDiscount qd = (QuantityDiscount) descuento;
			seccion.add(crearFilaInfo("Tipo:", "Descuento por cantidad"));
			seccion.add(crearFilaInfo("Promoción:", "Lleva " + qd.getBuyX() + " paga " + qd.getPayY()));
			return seccion;
		}

		return null;
	}

	/**
	 * Método auxiliar de diseño que genera un contenedor base para cada sección
	 * informativa, unificando los colores, bordes y estilos de los títulos.
	 *
	 * @param titulo El texto que servirá de encabezado para la sección.
	 * @return Un {@link JPanel} base configurado para albergar filas de información.
	 */
	// SECCIÓN CON TÍTULO
	private JPanel crearSeccion(String titulo) {
		JPanel seccion = new JPanel();
		seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
		seccion.setBackground(COLOR_PANEL_INFO);
		seccion.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		seccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
		lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		seccion.add(lblTitulo);
		seccion.add(Box.createVerticalStrut(10));

		return seccion;
	}

	/**
	 * Genera una fila individual compuesta por una etiqueta (clave) y su
	 * respectivo valor. Utilizada para poblar las secciones de información.
	 *
	 * @param etiqueta El título de la propiedad (ej. "Descripción:").
	 * @param valor    El contenido de la propiedad (ej. "Descuento de verano").
	 * @return Un {@link JPanel} horizontal que muestra la dupla de clave-valor.
	 */
	// FILA DE INFORMACIÓN
	private JPanel crearFilaInfo(String etiqueta, String valor) {
		JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		fila.setOpaque(false);
		fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		fila.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblEtiqueta = new JLabel(etiqueta);
		lblEtiqueta.setForeground(COLOR_LABEL);
		lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 11));
		lblEtiqueta.setPreferredSize(new Dimension(180, 25));

		JLabel lblValor = new JLabel(valor);
		lblValor.setForeground(Color.WHITE);
		lblValor.setFont(new Font("Arial", Font.PLAIN, 12));

		fila.add(lblEtiqueta);
		fila.add(lblValor);

		return fila;
	}

	/**
	 * Construye la barra de herramientas inferior, la cual aloja los botones
	 * operativos de retorno y de eliminación del descuento actual, incluyendo
	 * la lógica de confirmación (pop-up) al tratar de borrar.
	 *
	 * @return Un {@link JPanel} con las acciones de pie de página.
	 */
	// BARRA INFERIOR: Botones de acción
	private JPanel crearBarraInferior() {
		JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		// Botón eliminar
		JButton btnEliminar = new JButton("🗑️ ELIMINAR");
		btnEliminar.setPreferredSize(new Dimension(150, 40));
		btnEliminar.setBackground(new Color(231, 76, 60));
		btnEliminar.setForeground(Color.WHITE);
		btnEliminar.setFont(new Font("Arial", Font.BOLD, 12));
		btnEliminar.setBorder(null);
		btnEliminar.setFocusPainted(false);
		btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEliminar.addActionListener(e -> {
			int opcion = JOptionPane.showConfirmDialog(
					this,
					"¿Estás seguro de que deseas eliminar este descuento?",
					"Confirmar eliminación",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE
			);

			if (opcion == JOptionPane.YES_OPTION) {
				if (ctrl.eliminarDescuento(descuento)) {
					panelPadre.mostrarListado();
				}
			}
		});

		// Botón volver
		JButton btnVolver = new JButton("< Volver");
		btnVolver.setPreferredSize(new Dimension(150, 40));
		btnVolver.setBackground(new Color(149, 165, 166));
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setFont(new Font("Arial", Font.BOLD, 12));
		btnVolver.setBorder(null);
		btnVolver.setFocusPainted(false);
		btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnVolver.addActionListener(e -> panelPadre.mostrarListado());

		barra.add(btnEliminar);
		barra.add(btnVolver);

		return barra;
	}
}