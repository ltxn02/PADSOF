package swing2.view.gestor.clientes;

import javax.swing.*;

import swing2.controller.gestor.GestorClienteController;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import users.Client;

/**
 * Panel de la interfaz gráfica del Gestor destinado a mostrar de forma detallada
 * y organizada toda la información asociada a un cliente específico del sistema.
 * <p>
 * Este panel presenta la información dividida en secciones claras (Personal, Contacto
 * e Información de Cuenta) y proporciona opciones de navegación para volver al listado principal.
 * </p>
 * * @author Lidia Martin
 */
public class PanelDetallesCliente extends JPanel {
	private PanelGestionClientes panelPadre;
	private GestorClienteController ctrl;
	private Client cliente;

	// COLORES
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_PANEL_INFO = new Color(40, 80, 140);
	private static final Color COLOR_LABEL = new Color(187, 192, 199);

	/**
	 * Constructor del panel de detalles del cliente.
	 * Inicializa la estructura del contenedor, el esquema de colores institucional
	 * y los márgenes de diseño.
	 *
	 * @param panelPadre Referencia al panel principal contenedor que gestiona la navegación de clientes.
	 * @param ctrl       Controlador responsable de la lógica de negocio relacionada con los clientes.
	 */
	public PanelDetallesCliente(PanelGestionClientes panelPadre, GestorClienteController ctrl) {
		this.panelPadre = panelPadre;
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(COLOR_FONDO);
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	/**
	 * Actualiza y repinta visualmente el panel completo para mostrar
	 * toda la información extraída del objeto {@link Client} proporcionado.
	 *
	 * @param cliente El cliente cuyos datos se desean inspeccionar.
	 */
	// Cargar y mostrar los detalles de un cliente
	public void mostrarDetalles(Client cliente) {
		this.cliente = cliente;

		// Limpiar panel anterior
		this.removeAll();

		// BARRA SUPERIOR: Título + botón volver
		JPanel barraSuperior = crearBarraSuperior();
		this.add(barraSuperior, BorderLayout.NORTH);

		// CONTENIDO CENTRAL: Información del cliente
		JPanel contenidoPrincipal = crearContenidoPrincipal();
		JScrollPane scroll = new JScrollPane(contenidoPrincipal);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		this.add(scroll, BorderLayout.CENTER);

		// BARRA INFERIOR: Botón volver
		JPanel barraInferior = crearBarraInferior();
		this.add(barraInferior, BorderLayout.SOUTH);

		// Actualizar la vista
		this.revalidate();
		this.repaint();
	}

	/**
	 * Crea la barra superior que contiene el título de la vista y un botón
	 * de retroceso para volver al listado general de clientes.
	 *
	 * @return Un {@link JPanel} formateado con los controles superiores.
	 */
	// BARRA SUPERIOR
	private JPanel crearBarraSuperior() {
		JPanel barra = new JPanel(new BorderLayout());
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

		// Botón volver
		JButton btnVolver = new JButton("< Volver");
		btnVolver.setPreferredSize(new Dimension(150, 35));
		btnVolver.setBackground(new Color(52, 73, 94));
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setFont(new Font("Arial", Font.BOLD, 12));
		btnVolver.setBorder(null);
		btnVolver.setFocusPainted(false);
		btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnVolver.addActionListener(e -> panelPadre.mostrarListado());

		// Título
		JLabel titulo = new JLabel("DETALLES DEL CLIENTE");
		titulo.setForeground(Color.WHITE);
		titulo.setFont(new Font("Arial", Font.BOLD, 20));

		barra.add(btnVolver, BorderLayout.WEST);
		barra.add(titulo, BorderLayout.CENTER);

		return barra;
	}

	/**
	 * Ensambla el contenedor central de la vista, dividiéndolo estructuralmente
	 * en tres grandes módulos de información: Personal, Contacto y Cuenta de Cliente.
	 *
	 * @return Un {@link JPanel} con disposición vertical que agrupa todos los datos.
	 */
	// Información del cliente
	private JPanel crearContenidoPrincipal() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(COLOR_FONDO);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// 1.- INFORMACIÓN PERSONAL
		JPanel seccion1 = crearSeccion("INFORMACIÓN PERSONAL");
		seccion1.add(crearFilaInfo("Nombre completo:", cliente.getFullname()));
		seccion1.add(crearFilaInfo("Nombre de usuario:", cliente.getUsername()));
		seccion1.add(crearFilaInfo("DNI:", cliente.getMaskedDni()));
		seccion1.add(crearFilaInfo("Fecha de nacimiento:", cliente.getBirthdate()));

		panel.add(seccion1);
		panel.add(Box.createVerticalStrut(20));

		// 2.- CONTACTO
		JPanel seccion2 = crearSeccion("CONTACTO");
		seccion2.add(crearFilaInfo("Correo electrónico:", cliente.getEmail()));
		seccion2.add(crearFilaInfo("Teléfono:", cliente.getPhoneNumber()));

		panel.add(seccion2);
		panel.add(Box.createVerticalStrut(20));

		// 3.- INFORMACIÓN DE CLIENTE
		JPanel seccion3 = crearSeccion("INFORMACIÓN DE CLIENTE");
		seccion3.add(crearFilaInfo("Fecha de incorporación:", formatearFecha(cliente.getJoiningDate())));
		seccion3.add(crearFilaInfo("ID de cliente:", String.valueOf(cliente.getUserId())));

		panel.add(seccion3);
		panel.add(Box.createVerticalGlue());

		return panel;
	}

	/**
	 * Construye la estructura base para los diferentes módulos informativos
	 * dentro del panel, proporcionándoles un fondo distintivo, márgenes y un título de sección.
	 *
	 * @param titulo El nombre o título descriptivo de la sección.
	 * @return Un {@link JPanel} contenedor listo para recibir filas de datos.
	 */
	// SECCIÓN CON TÍTULO
	private JPanel crearSeccion(String titulo) {
		JPanel seccion = new JPanel();
		seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
		seccion.setBackground(COLOR_PANEL_INFO);
		seccion.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		seccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

		// Título de sección
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
		lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		seccion.add(lblTitulo);
		seccion.add(Box.createVerticalStrut(10));

		return seccion;
	}

	/**
	 * Genera una fila individual que exhibe una pareja de clave-valor.
	 * Es la unidad mínima de información mostrada dentro de las secciones del cliente.
	 *
	 * @param etiqueta El título descriptivo del dato (ej. "Nombre completo:").
	 * @param valor    El contenido real del dato a mostrar (ej. "Juan Pérez").
	 * @return Un {@link JPanel} horizontal con la información formateada.
	 */
	// FILA DE INFORMACIÓN
	private JPanel crearFilaInfo(String etiqueta, String valor) {
		JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		fila.setOpaque(false);
		fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		fila.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Etiqueta
		JLabel lblEtiqueta = new JLabel(etiqueta);
		lblEtiqueta.setForeground(COLOR_LABEL);
		lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 11));
		lblEtiqueta.setPreferredSize(new Dimension(180, 25));

		// Valor
		JLabel lblValor = new JLabel(valor);
		lblValor.setForeground(Color.WHITE);
		lblValor.setFont(new Font("Arial", Font.PLAIN, 12));

		fila.add(lblEtiqueta);
		fila.add(lblValor);

		return fila;
	}

	/**
	 * Crea la barra de herramientas inferior, la cual aloja un botón de retorno
	 * adicional para facilitar la navegación ergonómica en monitores grandes.
	 *
	 * @return Un {@link JPanel} centrado con el botón inferior de cierre/retorno.
	 */
	// BARRA INFERIOR: Botón volver
	private JPanel crearBarraInferior() {
		JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

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

		barra.add(btnVolver);

		return barra;
	}

	/**
	 * Convierte un objeto de tipo {@link LocalDateTime} a una cadena de texto
	 * estructurada de forma legible (formato DD/MM/YYYY).
	 *
	 * @param fecha La fecha y hora a procesar.
	 * @return El string resultante, o "N/A" en caso de que el objeto inicial sea nulo.
	 */
	// Formatear fecha LocalDateTime a String
	private String formatearFecha(LocalDateTime fecha) {
		if (fecha == null) return "N/A";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return fecha.format(formatter);
	}
}