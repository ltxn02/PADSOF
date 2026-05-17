package swing2.view.gestor.clientes;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import swing2.controller.gestor.GestorClienteController;

import java.awt.*;
import java.util.ArrayList;
import users.Client;

/**
 * Panel que muestra el listado de clientes con tabla, búsqueda y paginación.
 */
public class PanelListaClientes extends JPanel {
	private PanelGestionClientes panelPadre;  // Para volver atrás
	private GestorClienteController ctrl;
	
	// === TABLA ===
	private JTable tabla;
	private DefaultTableModel modeloTabla;
	private JScrollPane scrollPane;
	
	// === BÚSQUEDA Y FILTRADO ===
	private JTextField campoBusqueda;
	private ArrayList<Client> clientesFiltrados;
	private ArrayList<Client> clientesActuales;
	
	// === PAGINACIÓN ===
	private int paginaActual = 0;
	private int clientesPorPagina = 10;
	private int totalPaginas = 1;
	private JLabel labelPaginacion;
	private JButton btnAnterior, btnSiguiente;
	
	// === COLORES ===
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_TABLE = new Color(40, 80, 140);
	private static final Color COLOR_HEADER = new Color(20, 50, 100);
	
	public PanelListaClientes(PanelGestionClientes panelPadre, GestorClienteController ctrl) {
		this.panelPadre = panelPadre;
		this.ctrl = ctrl;
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(COLOR_FONDO);
		this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		// 1.- Barra superior (SOLO búsqueda)
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
		cargarClientes();
	}
	
	/**
	 * ========================================================
	 * 1.- BARRA SUPERIOR (SOLO búsqueda, ancho completo)
	 * ========================================================
	 */
	private JPanel crearBarraSuperior() {
	    // Contenedor invisible para centrar
	    JPanel contenedor = new JPanel(new GridBagLayout());
	    contenedor.setOpaque(false);

	    // Barra real con mismo ancho que la tabla
	    JPanel barra = new JPanel(new BorderLayout(10, 0));
	    barra.setBackground(COLOR_FONDO);
	    barra.setPreferredSize(new Dimension(900, 40)); // MISMO ANCHO QUE LA TABLA

	    // --- Búsqueda (ANCHO COMPLETO) ---
	    campoBusqueda = new JTextField();
	    aplicarPlaceholder();
	    campoBusqueda.setPreferredSize(new Dimension(900, 35));
	    campoBusqueda.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

	    campoBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
	        @Override
	        public void focusGained(java.awt.event.FocusEvent e) {
	            if (campoBusqueda.getText().equals("Buscar clientes")) {
	                campoBusqueda.setText("");
	                campoBusqueda.setForeground(Color.BLACK);
	                campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 12));
	            }
	        }

	        @Override
	        public void focusLost(java.awt.event.FocusEvent e) {
	            if (campoBusqueda.getText().trim().isEmpty()) {
	                campoBusqueda.setText("Buscar clientes");
	                campoBusqueda.setForeground(Color.GRAY);
	                campoBusqueda.setFont(new Font("Arial", Font.ITALIC, 12));
	            }
	        }
	    });

	    campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
	        @Override
	        public void keyReleased(java.awt.event.KeyEvent evt) {
	            String texto = campoBusqueda.getText();
	            if (texto.equals("Buscar clientes")) return;
	            buscarClientes(texto);
	            paginaActual = 0;
	            actualizarTabla();
	            actualizarPaginacion();
	        }
	    });

	    barra.add(campoBusqueda, BorderLayout.CENTER);

	    contenedor.add(barra);
	    return contenedor;
	}
	
	private void aplicarPlaceholder() {
	    campoBusqueda.setText("Buscar clientes");
	    campoBusqueda.setForeground(Color.GRAY);
	    campoBusqueda.setFont(new Font("Arial", Font.BOLD, 14));
	}
	
	/**
	 * ========================================================
	 * 2.- CREAR LA TABLA
	 * ========================================================
	 */
	private void crearTabla() {
		String[] columnas = {"ID", "Usuario", "Nombre Completo", "Email", "Teléfono", "Detalles"};
		
		modeloTabla = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tabla = new JTable(modeloTabla);
		
		tabla.setBackground(Color.WHITE);
		tabla.setForeground(Color.BLACK);
		tabla.setFont(new Font("Arial", Font.PLAIN, 12));
		tabla.setSelectionBackground(new Color(100, 150, 255));
		
		tabla.setRowHeight(37);
		tabla.setIntercellSpacing(new Dimension(0, 1));
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
		        setHorizontalAlignment(SwingConstants.CENTER);
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
		tabla.getColumnModel().getColumn(0).setPreferredWidth(80);	// ID Usuario
		tabla.getColumnModel().getColumn(1).setPreferredWidth(130);	// Usuario
		tabla.getColumnModel().getColumn(2).setPreferredWidth(225);	// Nombre Completo
		tabla.getColumnModel().getColumn(3).setPreferredWidth(175);	// Email
		tabla.getColumnModel().getColumn(4).setPreferredWidth(100);	// Teléfono
		tabla.getColumnModel().getColumn(5).setPreferredWidth(100); // Ver más
		
		// EVENTO: Click en una fila para ver detalles
		tabla.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int fila = tabla.rowAtPoint(evt.getPoint());
				int columna = tabla.columnAtPoint(evt.getPoint());
				if (fila >= 0 && columna == 5) {
					verDetallesCliente(fila);
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
	
	public void cargarClientes() {
		clientesFiltrados = ctrl.obtenerClientes();
		clientesActuales = new ArrayList<>(clientesFiltrados);
		calcularPaginas();
		actualizarTabla();
		actualizarPaginacion();
	}
	
	private void buscarClientes(String termino) {
		clientesActuales = ctrl.buscarClientes(clientesFiltrados, termino);
		calcularPaginas();
	}
	
	private void actualizarTabla() {
		modeloTabla.setRowCount(0);
		
		int inicio = paginaActual * clientesPorPagina;
		int fin = Math.min(inicio + clientesPorPagina, clientesActuales.size());
		
		for (int i = inicio; i < fin; i++) {
			Client cliente = clientesActuales.get(i);
			
			Object[] fila = {
					cliente.getUserId(),
					cliente.getUsername(),
					cliente.getFullname(),
					cliente.getEmail(),
					cliente.getPhoneNumber(),
					"Ver más"
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
		totalPaginas = (int) Math.ceil((double) clientesActuales.size() / clientesPorPagina);
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
	 * VER DETALLES DEL CLIENTE
	 * ========================================================
	 */
	
	private void verDetallesCliente(int fila) {
		int inicio = paginaActual * clientesPorPagina;
		Client cliente = clientesActuales.get(inicio + fila);
		
		// Mostrar el panel de detalles
		panelPadre.mostrarDetalles(cliente);
	}
	
	/**
	 * ========================================================
	 * LIMPIAR Y REFRESCAR
	 * ========================================================
	 */
	
	public void limpiarBusqueda() {
		aplicarPlaceholder();
		paginaActual = 0;
		cargarClientes();
	}
}