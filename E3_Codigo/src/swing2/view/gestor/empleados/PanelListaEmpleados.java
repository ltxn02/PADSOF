package swing2.view.gestor.empleados;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import swing2.controller.gestor.GestorEmpleadoController;

import java.awt.*;
import java.util.ArrayList;
import users.Staff;
import users.Employee;

/**
 * Panel que muestra el listado de empleados con tabla, búsqueda y paginación.
 * 
 * @author Lidia Martín
 */
public class PanelListaEmpleados extends JPanel {
	private PanelGestionEmpleados panelPadre;  // Para volver atrás
	private GestorEmpleadoController ctrl;
	
	// TABLA
	private JTable tabla;
	private DefaultTableModel modeloTabla;
	private JScrollPane scrollPane;
	
	// BÚSQUEDA Y FILTRADO
	private JTextField campoBusqueda;
	private ArrayList<Staff> empleadosFiltrados;
	private ArrayList<Staff> empleadosActuales;
	
	// PAGINACIÓN
	private int paginaActual = 0;
	private int empleadosPorPagina = 10;
	private int totalPaginas = 1;
	private JLabel labelPaginacion;
	private JButton btnAnterior, btnSiguiente;
	
	// COLORES
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_TABLE = new Color(40, 80, 140);
	private static final Color COLOR_HEADER = new Color(20, 50, 100);
	
	/**
	 * Constructor de la clase PanelListaEmpleados.
	 * Configura el layout, inicializa los componentes de la interfaz (barra superior,
	 * tabla con scroll, barra de paginación) y carga los datos iniciales de los empleados.
	 * 
	 * @param panelPadre Panel de gestión de empleados que actúa como contenedor de navegación.
	 * @param ctrl       Controlador encargado de la lógica de negocio de los empleados.
	 */
	public PanelListaEmpleados(PanelGestionEmpleados panelPadre, GestorEmpleadoController ctrl) {
		this.panelPadre = panelPadre;
		this.ctrl = ctrl;
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(COLOR_FONDO);
		this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		// 1.- Barra superior (búsqueda + botón "Añadir empleado")
		JPanel barraSuperior = crearBarraSuperior();
		this.add(barraSuperior, BorderLayout.NORTH);
		
		// 2.- Tabla con scroll (más pequeña y centrada)
		crearTabla();
		scrollPane = new JScrollPane(tabla);
		scrollPane.setBackground(COLOR_FONDO);
		scrollPane.getViewport().setBackground(COLOR_FONDO);
		
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());

		scrollPane.setPreferredSize(new Dimension(900, 420));

		JPanel panelTablaCentrada = new JPanel(new GridBagLayout());
		panelTablaCentrada.setOpaque(false);
		panelTablaCentrada.add(scrollPane);

		this.add(panelTablaCentrada, BorderLayout.CENTER);
		
		// 3.- Barra de paginación (abajo)
		JPanel barraPaginacion = crearBarraPaginacion();
		this.add(barraPaginacion, BorderLayout.SOUTH);
		
		// 4.- Cargar datos iniciales
		cargarEmpleados();
	}
	
	/**
	 * Construye la barra superior que contiene el campo de texto para la búsqueda
	 * en tiempo real y el botón destinado a añadir un nuevo empleado.
	 * 
	 * @return Un JPanel alineado y configurado para la zona norte de la interfaz.
	 */
	private JPanel crearBarraSuperior() {
	    // Contenedor invisible para centrar
	    JPanel contenedor = new JPanel(new GridBagLayout());
	    contenedor.setOpaque(false);

	    // Barra real con mismo ancho que la tabla
	    JPanel barra = new JPanel(new BorderLayout(10, 0));
	    barra.setBackground(COLOR_FONDO);
	    barra.setPreferredSize(new Dimension(900, 40)); // MISMO ANCHO QUE LA TABLA

	    // Búsqueda
	    JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    panelBusqueda.setOpaque(false);

	    campoBusqueda = new JTextField();
	    aplicarPlaceholder();
	    campoBusqueda.setPreferredSize(new Dimension(500, 35));
	    campoBusqueda.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

	    campoBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
	        @Override
	        public void focusGained(java.awt.event.FocusEvent e) {
	            if (campoBusqueda.getText().equals("Buscar empleados")) {
	                campoBusqueda.setText("");
	                campoBusqueda.setForeground(Color.BLACK);
	                campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 12));
	            }
	        }

	        @Override
	        public void focusLost(java.awt.event.FocusEvent e) {
	            if (campoBusqueda.getText().trim().isEmpty()) {
	                campoBusqueda.setText("Buscar empleados");
	                campoBusqueda.setForeground(Color.GRAY);
	                campoBusqueda.setFont(new Font("Arial", Font.ITALIC, 12));
	            }
	        }
	    });

	    campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
	        @Override
	        public void keyReleased(java.awt.event.KeyEvent evt) {
	            String texto = campoBusqueda.getText();
	            if (texto.equals("Buscar empleados")) return;
	            buscarEmpleados(texto);
	            paginaActual = 0;
	            actualizarTabla();
	            actualizarPaginacion();
	        }
	    });

	    panelBusqueda.add(campoBusqueda);

	    // Botón añadir
	    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
	    panelBotones.setOpaque(false);

	    JButton btnAnadir = new JButton("+ Añadir empleado");
	    btnAnadir.setPreferredSize(new Dimension(180, 35));
	    btnAnadir.setBackground(Color.WHITE);
	    btnAnadir.setForeground(COLOR_FONDO);
	    btnAnadir.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
	    btnAnadir.setBorder(null);
	    btnAnadir.setFocusPainted(false);
	    btnAnadir.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    btnAnadir.addActionListener(e -> panelPadre.mostrarAnadirEmpleado());

	    panelBotones.add(btnAnadir);

	    barra.add(panelBusqueda, BorderLayout.WEST);
	    barra.add(panelBotones, BorderLayout.EAST);

	    contenedor.add(barra);
	    return contenedor;
	}
	
	/**
	 * Aplica el texto por defecto y la tipografía en cursiva como marcador de posición 
	 * (placeholder) en el cuadro de búsqueda de empleados.
	 */
	private void aplicarPlaceholder() {
	    campoBusqueda.setText("Buscar empleados");
	    campoBusqueda.setForeground(Color.GRAY);
	    campoBusqueda.setFont(new Font("Arial", Font.BOLD, 14));
	}
	
	/**
	 * Inicializa y define el JTable, configurando las columnas, deshabilitando la edición 
	 * directa de celdas, personalizando renderizadores (filas alternas y texto centrado), 
	 * estilizando la cabecera y enlazando el evento de selección para detalles.
	 */
	private void crearTabla() {
		String[] columnas = {"Nombre", "Usuario", "Correo", "Teléfono", "Rol", "Estado", "Detalles"};
		
		modeloTabla = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tabla = new JTable(modeloTabla);  // Primero crear la tabla
		
		tabla.setBackground(Color.WHITE);
		tabla.setForeground(Color.BLACK);
		tabla.setFont(new Font("Arial", Font.PLAIN, 12));
		tabla.setSelectionBackground(new Color(100, 150, 255));
		
		tabla.setRowHeight(37);	 // Altura
		tabla.setIntercellSpacing(new Dimension(0, 1));  // Espaciado
		tabla.setShowGrid(false);
		tabla.setBorder(null);

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value,
		                                                   boolean isSelected, boolean hasFocus,
		                                                   int row, int column) {
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		        if (!isSelected) {
		            setBackground(row % 2 == 0 ? new Color(219, 219, 219) : Color.WHITE);
		        }
		        setForeground(Color.BLACK);
		        setHorizontalAlignment(SwingConstants.CENTER); // todo centrado
		        return this;
		    }
		};

		for (int i = 0; i < tabla.getColumnCount(); i++) {
		    tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
		
		// Personalizar header
		JTableHeader header = tabla.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		header.setBackground(Color.WHITE);
		header.setForeground(new Color(105, 103, 99));
		header.setFont(new Font("Arial", Font.BOLD, 16));
		header.setReorderingAllowed(false);

		header.setDefaultRenderer(new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value,
		                                                   boolean isSelected, boolean hasFocus,
		                                                   int row, int column) {
		        JLabel lbl = (JLabel) super.getTableCellRendererComponent(
		                table, value, isSelected, hasFocus, row, column);
		        lbl.setHorizontalAlignment(SwingConstants.CENTER);
		        lbl.setOpaque(true);
		        lbl.setBackground(Color.WHITE);
		        lbl.setForeground(new Color(105, 103, 99));
		        lbl.setFont(new Font("Arial", Font.BOLD, 16));
		        lbl.setBorder(null);
		        return lbl;
		    }
		});
		
		// Ajustar ancho de columnas
		tabla.getColumnModel().getColumn(0).setPreferredWidth(200);	// Nombre
		tabla.getColumnModel().getColumn(1).setPreferredWidth(100);	// Usuario
		tabla.getColumnModel().getColumn(2).setPreferredWidth(150);	// Correo
		tabla.getColumnModel().getColumn(3).setPreferredWidth(100);	// Teléfono
		tabla.getColumnModel().getColumn(4).setPreferredWidth(100);	// Rol
		tabla.getColumnModel().getColumn(5).setPreferredWidth(100);	// Estado
		tabla.getColumnModel().getColumn(6).setPreferredWidth(100); // Detalles
		
		// EVENTO: Click en una fila para ver detalles
		tabla.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int fila = tabla.rowAtPoint(evt.getPoint());
				int columna = tabla.columnAtPoint(evt.getPoint());
				if (fila >= 0 && columna == 6) {	// Columna "Detalles"
					verDetallesEmpleado(fila);
				}
			}
		});
	}
	
	/**
	 * Configura la barra inferior de paginación que agrupa los botones de navegación 
	 * interactivos (Anterior/Siguiente) junto con la leyenda explicativa de roles.
	 * 
	 * @return Un JPanel listo para posicionarse en la zona sur de la interfaz.
	 */
	private JPanel crearBarraPaginacion() {
		JPanel barra = new JPanel(new BorderLayout());
		barra.setBackground(COLOR_FONDO);
		barra.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		// Panel paginación
		JPanel panelPaginacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panelPaginacion.setOpaque(false);
		
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
		
		panelPaginacion.add(btnAnterior);
		panelPaginacion.add(labelPaginacion);
		panelPaginacion.add(btnSiguiente);
		
		// Panel leyenda
		JPanel leyendaRoles = crearLeyendaRoles();
		
		barra.add(panelPaginacion, BorderLayout.NORTH);
		barra.add(leyendaRoles, BorderLayout.SOUTH);
		
		return barra;
	}
	
	/**
	 * Crea un subpanel de texto informativo para indicar el significado 
	 * de las siglas de los roles asignados a los empleados en la tabla.
	 * 
	 * @return Un JPanel con la leyenda alineada.
	 */
	private JPanel crearLeyendaRoles() {
		JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		leyenda.setOpaque(false);
		leyenda.setPreferredSize(new Dimension(900, 25));
		leyenda.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		
		JLabel lblTitulo = new JLabel("Roles: ");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 11));
		leyenda.add(lblTitulo);
		
		JLabel lblOE = new JLabel("OE = Operario de pedidos");
		lblOE.setForeground(new Color(173, 216, 230));
		lblOE.setFont(new Font("Arial", Font.PLAIN, 10));
		leyenda.add(lblOE);
		
		JLabel lblEI = new JLabel("EI = Empleado de intercambios");
		lblEI.setForeground(new Color(144, 238, 144));
		lblEI.setFont(new Font("Arial", Font.PLAIN, 10));
		leyenda.add(lblEI);
		
		JLabel lblEP = new JLabel("EP = Empleado de productos");
		lblEP.setForeground(new Color(255, 218, 185));
		lblEP.setFont(new Font("Arial", Font.PLAIN, 10));
		leyenda.add(lblEP);
		
		return leyenda;
	}
	
	/**
	 * Consulta los empleados disponibles mediante el controlador, inicializa las estructuras
	 * de filtrado, calcula el rango de páginas y repinta la información.
	 */
	public void cargarEmpleados() {
		empleadosFiltrados = ctrl.obtenerEmpleados();
		empleadosActuales = new ArrayList<>(empleadosFiltrados);
		calcularPaginas();
		actualizarTabla();
		actualizarPaginacion();
	}
	
	/**
	 * Filtra la colección de empleados actual apoyándose en el controlador utilizando
	 * un criterio de búsqueda textual y recalcula las páginas resultantes.
	 * 
	 * @param termino Cadena de texto que representa el filtro introducido.
	 */
	private void buscarEmpleados(String termino) {
		empleadosActuales = ctrl.buscarEmpleados(empleadosFiltrados, termino);
		calcularPaginas();
	}
	
	/**
	 * Limpia las filas actuales de la tabla y escribe los datos de los empleados 
	 * correspondientes a la página activa calculando los límites de índice pertinentes.
	 */
	private void actualizarTabla() {
		modeloTabla.setRowCount(0);
		
		int inicio = paginaActual * empleadosPorPagina;
		int fin = Math.min(inicio + empleadosPorPagina, empleadosActuales.size());
		
		for (int i = inicio; i < fin; i++) {
			Staff empleado = empleadosActuales.get(i);
			String estado = null;
			String rolesAbreviados = "";
			
			if (empleado instanceof Employee) {
				Employee emp = (Employee) empleado;
				estado = emp.isEnabled() ? "Activo" : "Inactivo";
				rolesAbreviados = obtenerRolesAbreviados(emp);
			} else {
				estado = "Activo";
				rolesAbreviados = "N/A";
			}
			
			Object[] fila = {
					empleado.getFullname(),
					empleado.getUsername(),
					empleado.getEmail(),
					empleado.getPhoneNumber(),
					rolesAbreviados,
					estado,
					"Ver más"
			};
			
			modeloTabla.addRow(fila);
		}
	}
	
	/**
	 * Convierte la lista interna de roles enumerados de un empleado en una 
	 * representación condensada por siglas separadas por comas.
	 * 
	 * @param emp Instancia del empleado a evaluar.
	 * @return Cadena formateada con las abreviaturas de los roles (OE, EI, EP) o "N/A".
	 */
	private String obtenerRolesAbreviados(Employee emp) {
		StringBuilder roles = new StringBuilder();
		
		if (emp.Rol != null && !emp.Rol.isEmpty()) {
			for (int i = 0; i < emp.Rol.size(); i++) {
				switch (emp.Rol.get(i).toString()) {
					case "ORDERS_EMPLOYEE":
						roles.append("OE");
						break;
					case "EXCHANGES_EMPLOYEE":
						roles.append("EI");
						break;
					case "PRODUCTS_EMPLOYEE":
						roles.append("EP");
						break;
				}
				
				// Añadir coma si no es el último
				if (i < emp.Rol.size() - 1) {
					roles.append(", ");
				}
			}
		}
		
		return roles.toString().isEmpty() ? "N/A" : roles.toString();
	}
	
	/**
	 * Realiza la operación matemática de techo para determinar el número máximo
	 * de páginas requeridas según la dimensión de la lista de empleados.
	 */
	private void calcularPaginas() {
		totalPaginas = (int) Math.ceil((double) empleadosActuales.size() / empleadosPorPagina);
		if (totalPaginas == 0) totalPaginas = 1;
	}
	
	/**
	 * Actualiza el texto informativo del contador de páginas y habilita o deshabilita 
	 * el estado funcional y los textos de los botones Anterior y Siguiente.
	 */
	private void actualizarPaginacion() {
		labelPaginacion.setText(String.format("Página %d de %d", paginaActual + 1, totalPaginas));
		btnAnterior.setEnabled(paginaActual > 0);
		btnSiguiente.setEnabled(paginaActual < totalPaginas - 1);
		btnAnterior.setText(paginaActual == 0 ? "" : "< Anterior");
		btnSiguiente.setText(paginaActual == totalPaginas - 1 ? "" : "Siguiente >");
	}
	
	/**
	 * Disminuye el índice de página actual si no se encuentra en el origen, 
	 * refrescando las celdas de la tabla y sus mandos.
	 */
	private void irPaginaAnterior() {
		if (paginaActual > 0) {
			paginaActual--;
			actualizarTabla();
			actualizarPaginacion();
		}
	}
	
	/**
	 * Incrementa el índice de página actual si quedan elementos posteriores, 
	 * refrescando las celdas de la tabla y sus mandos.
	 */
	private void irPaginaSiguiente() {
		if (paginaActual < totalPaginas - 1) {
			paginaActual++;
			actualizarTabla();
			actualizarPaginacion();
		}
	}
	
	/**
	 * Identifica el objeto Staff exacto correspondiente a la fila seleccionada 
	 * combinando el desplazamiento de paginación e instruye al panel principal para mostrarlo.
	 * 
	 * @param fila Índice relativo de la fila pulsada dentro de la vista actual del JTable.
	 */
	private void verDetallesEmpleado(int fila) {
		int inicio = paginaActual * empleadosPorPagina;
		Staff empleado = empleadosActuales.get(inicio + fila);
		
		// Mostrar el panel de detalles
		panelPadre.mostrarDetalles(empleado);
	}
	
	/**
	 * Restablece el control visual del cuadro de búsqueda al marcador original, 
	 * posiciona la navegación en la primera página y recarga el listado completo.
	 */
	public void limpiarBusqueda() {
		aplicarPlaceholder();
		paginaActual = 0;
		cargarEmpleados();
	}
}