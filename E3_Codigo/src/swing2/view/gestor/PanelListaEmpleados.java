package swing2.view.gestor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import swing2.controller.gestor.GestorEmpleadoController;

import java.awt.*;
import java.util.ArrayList;
import users.Staff;
import users.Employee;

/**
 * Panel que muestra el listado de empleados con tabla, búsqueda y paginación.
 */
public class PanelListaEmpleados extends JPanel {
	private PanelGestionEmpleados panelPadre;  // Para volver atrás
	private GestorEmpleadoController ctrl;
	
	// === TABLA ===
	private JTable tabla;
	private DefaultTableModel modeloTabla;
	private JScrollPane scrollPane;
	
	// === BÚSQUEDA Y FILTRADO ===
	private JTextField campoBusqueda;
	private ArrayList<Staff> empleadosFiltrados;
	private ArrayList<Staff> empleadosActuales;
	
	// === PAGINACIÓN ===
	private int paginaActual = 0;
	private int empleadosPorPagina = 10;
	private int totalPaginas = 1;
	private JLabel labelPaginacion;
	private JButton btnAnterior, btnSiguiente;
	
	// === COLORES ===
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_TABLE = new Color(40, 80, 140);
	private static final Color COLOR_HEADER = new Color(20, 50, 100);
	
	public PanelListaEmpleados(PanelGestionEmpleados panelPadre, GestorEmpleadoController ctrl) {
		this.panelPadre = panelPadre;
		this.ctrl = ctrl;
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(COLOR_FONDO);
		this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		// 1.- Barra superior (búsqueda + botón "Añadir empleado")
		JPanel barraSuperior = crearBarraSuperior();
		this.add(barraSuperior, BorderLayout.NORTH);
		
		// 2.- Tabla con scroll
		crearTabla();
		scrollPane = new JScrollPane(tabla);
		scrollPane.setBackground(COLOR_FONDO);
		scrollPane.getViewport().setBackground(COLOR_FONDO);
		this.add(scrollPane, BorderLayout.CENTER);
		
		// 3.- Barra de paginación (abajo)
		JPanel barraPaginacion = crearBarraPaginacion();
		this.add(barraPaginacion, BorderLayout.SOUTH);
		
		// 4.- Cargar datos iniciales
		cargarEmpleados();
	}
	
	/**
	 * ========================================================
	 * 1.- BARRA SUPERIOR (búsqueda + botón "Añadir empleado")
	 * ========================================================
	 */
	private JPanel crearBarraSuperior() {
		JPanel barra = new JPanel(new BorderLayout(10, 0));
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		
		// --- LADO IZQUIERDO: Búsqueda ---
		JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		panelBusqueda.setOpaque(false);
		
		JLabel labelBusqueda = new JLabel("🔍︎ Buscar empleados");
		labelBusqueda.setBackground(Color.WHITE);
		labelBusqueda.setForeground(new Color(187, 192, 199));
		labelBusqueda.setFont(new Font("Arial", Font.BOLD, 12));
		
		campoBusqueda = new JTextField(25);
		campoBusqueda.setPreferredSize(new Dimension(250, 30));
		campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 12));
		
		// Evento búsqueda en tiempo real
		campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				buscarEmpleados(campoBusqueda.getText());
				paginaActual = 0;
				actualizarTabla();
				actualizarPaginacion();
			}
		});
		
		panelBusqueda.add(labelBusqueda);
		panelBusqueda.add(campoBusqueda);
		
		// --- LADO DERECHO: Botón "Añadir empleado" ---
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		panelBotones.setOpaque(false);
		
		JButton btnAnadir = new JButton("+ Añadir empleado");
		btnAnadir.setPreferredSize(new Dimension(180, 35));
		btnAnadir.setBackground(Color.WHITE);
		btnAnadir.setForeground(COLOR_FONDO);
		btnAnadir.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 12));
		btnAnadir.setBorder(null);
		btnAnadir.setFocusPainted(false);
		btnAnadir.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAnadir.addActionListener(e -> panelPadre.mostrarAnadirEmpleado());
		
		panelBotones.add(btnAnadir);
		
		barra.add(panelBusqueda, BorderLayout.WEST);
		barra.add(panelBotones, BorderLayout.EAST);
		
		return barra;
	}
	
	/**
	 * ========================================================
	 * 2.- CREAR LA TABLA
	 * ========================================================
	 */
	private void crearTabla() {
		String[] columnas = {"Nombre", "Usuario", "Correo", "Teléfono", "Estado", "Ver detalles"};
		
		modeloTabla = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tabla = new JTable(modeloTabla);
		tabla.setBackground(Color.WHITE);
		tabla.setForeground(Color.BLACK);
		tabla.setFont(new Font("Arial", Font.PLAIN, 11));
		tabla.setRowHeight(30);
		tabla.setSelectionBackground(new Color(100, 150, 255));
		tabla.setGridColor(new Color(200, 200, 200));
		
		// Personalizar header
		JTableHeader header = tabla.getTableHeader();
		header.setBackground(COLOR_HEADER);
		header.setForeground(Color.WHITE);
		header.setFont(new Font("Arial", Font.BOLD, 12));
		header.setReorderingAllowed(false);
		
		// Ajustar ancho de columnas
		tabla.getColumnModel().getColumn(0).setPreferredWidth(150);	// Nombre
		tabla.getColumnModel().getColumn(1).setPreferredWidth(100);	// Usuario
		tabla.getColumnModel().getColumn(2).setPreferredWidth(150);	// Correo
		tabla.getColumnModel().getColumn(3).setPreferredWidth(100);	// Teléfono
		tabla.getColumnModel().getColumn(4).setPreferredWidth(80);	// Estado
		tabla.getColumnModel().getColumn(5).setPreferredWidth(80); 	// Más
		
		// EVENTO: Click en una fila para ver detalles
		tabla.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int fila = tabla.rowAtPoint(evt.getPoint());
				int columna = tabla.columnAtPoint(evt.getPoint());
				if (fila >= 0 && columna == 5) {	// Columna "Usuario"
					verDetallesEmpleado(fila);
				}
			}
		});
	}
	
	/**
	 * ========================================================
	 * 3.- CREAR BARRA DE PAGINACIÓN
	 * ========================================================
	 */
	private JPanel crearBarraPaginacion() {
		JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		// Botón anterior
		btnAnterior = new JButton("< Anterior");
		btnAnterior.setPreferredSize(new Dimension(120, 30));
		btnAnterior.setBackground(COLOR_FONDO);
		btnAnterior.setForeground(Color.WHITE);
		btnAnterior.setBorder(null);
		btnAnterior.setFocusPainted(false);
		btnAnterior.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAnterior.addActionListener(e -> irPaginaAnterior());
		
		// Etiqueta de paginación
		labelPaginacion = new JLabel("Página 1 de 1");
		labelPaginacion.setForeground(Color.WHITE);
		labelPaginacion.setFont(new Font("Arial", Font.BOLD, 12));
		
		// Botón siguiente
		btnSiguiente = new JButton("Siguiente >");
		btnSiguiente.setPreferredSize(new Dimension(120, 30));
		btnSiguiente.setBackground(COLOR_FONDO);
		btnSiguiente.setForeground(Color.WHITE);
		btnSiguiente.setBorder(null);
		btnSiguiente.setFocusPainted(false);
		btnSiguiente.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSiguiente.addActionListener(e -> irPaginaSiguiente());
		
		barra.add(btnAnterior);
		barra.add(labelPaginacion);
		barra.add(btnSiguiente);
		
		return barra;
	}
	
	/**
	 * ========================================================
	 * LÓGICA: CARGAR, BUSCAR, ACTUALIZAR
	 * ========================================================
	 */
	
	public void cargarEmpleados() {
		empleadosFiltrados = ctrl.obtenerEmpleados();
		empleadosActuales = new ArrayList<>(empleadosFiltrados);
		calcularPaginas();
		actualizarTabla();
		actualizarPaginacion();
	}
	
	private void buscarEmpleados(String termino) {
		empleadosActuales = ctrl.buscarEmpleados(empleadosFiltrados, termino);
		calcularPaginas();
	}
	
	private void actualizarTabla() {
		modeloTabla.setRowCount(0);
		
		int inicio = paginaActual * empleadosPorPagina;
		int fin = Math.min(inicio + empleadosPorPagina, empleadosActuales.size());
		
		for (int i = inicio; i < fin; i++) {
			Staff empleado = empleadosActuales.get(i);
			String estado = null;
			if (empleado instanceof Employee) {
				estado = ((Employee)empleado).isEnabled() ? "Activo" : "Inactivo";
			} else {
				estado = "Activo";
			}
			
			Object[] fila = {
					empleado.getFullname(),
					empleado.getUsername(),
					empleado.getEmail(),
					empleado.getPhoneNumber(),
					estado,
					">"
			};
			
			modeloTabla.addRow(fila);
		}
	}
	
	/**
	 * ========================================================
	 * PAGINACIÓN
	 * ========================================================
	 */
	
	private void calcularPaginas() {
		totalPaginas = (int) Math.ceil((double) empleadosActuales.size() / empleadosPorPagina);
		if (totalPaginas == 0) totalPaginas = 1;
	}
	
	private void actualizarPaginacion() {
		labelPaginacion.setText(String.format("Página %d de %d", paginaActual + 1, totalPaginas));
		btnAnterior.setEnabled(paginaActual > 0);
		btnSiguiente.setEnabled(paginaActual < totalPaginas - 1);
		btnAnterior.setText(paginaActual == 0 ? "" : "< Anterior");
		btnSiguiente.setText(paginaActual == totalPaginas - 1 ? "" : "Siguiente >");
	}
	
	private void irPaginaAnterior() {
		if (paginaActual > 0) {
			paginaActual--;
			actualizarTabla();
			actualizarPaginacion();
		}
	}
	
	private void irPaginaSiguiente() {
		if (paginaActual < totalPaginas - 1) {
			paginaActual++;
			actualizarTabla();
			actualizarPaginacion();
		}
	}
	
	/**
	 * ========================================================
	 * VER DETALLES DEL EMPLEADO
	 * ========================================================
	 */
	
	private void verDetallesEmpleado(int fila) {
		int inicio = paginaActual * empleadosPorPagina;
		Staff empleado = empleadosActuales.get(inicio + fila);
		
		// Mostrar el panel de detalles
		panelPadre.mostrarDetalles(empleado);
	}
	
	/**
	 * ========================================================
	 * LIMPIAR Y REFRESCAR
	 * ========================================================
	 */
	
	public void limpiarBusqueda() {
		campoBusqueda.setText("");
		paginaActual = 0;
		cargarEmpleados();
	}
}