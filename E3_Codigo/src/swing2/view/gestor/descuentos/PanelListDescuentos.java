package swing2.view.gestor.descuentos;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import swing2.controller.gestor.GestorDescuentoController;
import discounts.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * Panel que muestra el listado de descuentos con tabla, búsqueda y paginación.
 */
public class PanelListDescuentos extends JPanel {
	private PanelGestionDescuentos panelPadre;
	private GestorDescuentoController ctrl;
	
	// === TABLA ===
	private JTable tabla;
	private DefaultTableModel modeloTabla;
	private JScrollPane scrollPane;
	
	// === BÚSQUEDA Y FILTRADO ===
	private JTextField campoBusqueda;
	private ArrayList<IDiscount> descuentosFiltrados;
	private ArrayList<IDiscount> descuentosActuales;
	
	// === PAGINACIÓN ===
	private int paginaActual = 0;
	private int descuentosPorPagina = 10;
	private int totalPaginas = 1;
	private JLabel labelPaginacion;
	private JButton btnAnterior, btnSiguiente;
	
	// === COLORES ===
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_TABLE = new Color(40, 80, 140);
	private static final Color COLOR_HEADER = new Color(20, 50, 100);
	private static final Color COLOR_NA = new Color(150, 150, 150);
	
	public PanelListDescuentos(PanelGestionDescuentos panelPadre, GestorDescuentoController ctrl) {
		this.panelPadre = panelPadre;
		this.ctrl = ctrl;
		
		this.setLayout(new BorderLayout(10, 10));
		this.setBackground(COLOR_FONDO);
		this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		// 1.- Barra superior (búsqueda + botón "Añadir descuento")
		JPanel barraSuperior = crearBarraSuperior();
		this.add(barraSuperior, BorderLayout.NORTH);
		
		// 2.- Tabla con scroll
		crearTabla();
		scrollPane = new JScrollPane(tabla);
		scrollPane.setBackground(COLOR_FONDO);
		scrollPane.getViewport().setBackground(COLOR_FONDO);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
		scrollPane.setPreferredSize(new Dimension(1000, 420));

		JPanel panelTablaCentrada = new JPanel(new GridBagLayout());
		panelTablaCentrada.setOpaque(false);
		panelTablaCentrada.add(scrollPane);

		this.add(panelTablaCentrada, BorderLayout.CENTER);
		
		// 3.- Barra de paginación (abajo)
		JPanel barraPaginacion = crearBarraPaginacion();
		this.add(barraPaginacion, BorderLayout.SOUTH);
		
		// 4.- Cargar datos iniciales
		cargarDescuentos();
	}
	
	/**
	 * ========================================================
	 * 1.- BARRA SUPERIOR (búsqueda + botón "Añadir descuento")
	 * ========================================================
	 */
	private JPanel crearBarraSuperior() {
	    JPanel contenedor = new JPanel(new GridBagLayout());
	    contenedor.setOpaque(false);

	    JPanel barra = new JPanel(new BorderLayout(10, 0));
	    barra.setBackground(COLOR_FONDO);
	    barra.setPreferredSize(new Dimension(1000, 40));

	    // --- Búsqueda ---
	    JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    panelBusqueda.setOpaque(false);

	    campoBusqueda = new JTextField();
	    aplicarPlaceholder();
	    campoBusqueda.setPreferredSize(new Dimension(500, 35));
	    campoBusqueda.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

	    campoBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
	        @Override
	        public void focusGained(java.awt.event.FocusEvent e) {
	            if (campoBusqueda.getText().equals("Buscar descuentos")) {
	                campoBusqueda.setText("");
	                campoBusqueda.setForeground(Color.BLACK);
	                campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 12));
	            }
	        }

	        @Override
	        public void focusLost(java.awt.event.FocusEvent e) {
	            if (campoBusqueda.getText().trim().isEmpty()) {
	                campoBusqueda.setText("Buscar descuentos");
	                campoBusqueda.setForeground(Color.GRAY);
	                campoBusqueda.setFont(new Font("Arial", Font.ITALIC, 12));
	            }
	        }
	    });

	    campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
	        @Override
	        public void keyReleased(java.awt.event.KeyEvent evt) {
	            String texto = campoBusqueda.getText();
	            if (texto.equals("Buscar descuentos")) return;
	            buscarDescuentos(texto);
	            paginaActual = 0;
	            actualizarTabla();
	            actualizarPaginacion();
	        }
	    });

	    panelBusqueda.add(campoBusqueda);

	    // --- Botón añadir ---
	    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
	    panelBotones.setOpaque(false);

	    JButton btnAnadir = new JButton("+ Crear descuento");
	    btnAnadir.setPreferredSize(new Dimension(200, 35));
	    btnAnadir.setBackground(Color.WHITE);
	    btnAnadir.setForeground(COLOR_FONDO);
	    btnAnadir.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
	    btnAnadir.setBorder(null);
	    btnAnadir.setFocusPainted(false);
	    btnAnadir.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    btnAnadir.addActionListener(e -> panelPadre.mostrarSeleccionTipo());

	    panelBotones.add(btnAnadir);

	    barra.add(panelBusqueda, BorderLayout.WEST);
	    barra.add(panelBotones, BorderLayout.EAST);

	    contenedor.add(barra);
	    return contenedor;
	}
	
	private void aplicarPlaceholder() {
	    campoBusqueda.setText("Buscar descuentos");
	    campoBusqueda.setForeground(Color.GRAY);
	    campoBusqueda.setFont(new Font("Arial", Font.BOLD, 14));
	}
	
	/**
	 * ========================================================
	 * 2.- CREAR LA TABLA
	 * ========================================================
	 */
	private void crearTabla() {
		String[] columnas = {"ID", "Categoría", "Tipo", "Porcentaje", "Detalles", "Eliminar"};
		
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
		        
		        // Si el valor es "N/A", usar color más suave
		        if (value != null && value.toString().equals("N/A")) {
		            setForeground(COLOR_NA);
		        } else {
		            setForeground(Color.BLACK);
		        }
		        
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
		tabla.getColumnModel().getColumn(0).setPreferredWidth(80);	// ID
		tabla.getColumnModel().getColumn(1).setPreferredWidth(200);	// Categoría
		tabla.getColumnModel().getColumn(2).setPreferredWidth(150);	// Tipo
		tabla.getColumnModel().getColumn(3).setPreferredWidth(120);	// Porcentaje
		tabla.getColumnModel().getColumn(4).setPreferredWidth(150); // Detalles
		tabla.getColumnModel().getColumn(5).setPreferredWidth(100); // Eliminar
		
		// EVENTO: Click en una fila
		tabla.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int fila = tabla.rowAtPoint(evt.getPoint());
				int columna = tabla.columnAtPoint(evt.getPoint());
				
				if (fila >= 0) {
					int inicio = paginaActual * descuentosPorPagina;
					IDiscount descuento = descuentosActuales.get(inicio + fila);
					
					if (columna == 4) {	// Columna "Detalles"
						verDetallesDescuento(descuento);
					} else if (columna == 5) {	// Columna "Eliminar"
						eliminarDescuento(descuento, fila, inicio);
					}
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
		
		btnAnterior = new JButton("< Anterior");
		btnAnterior.setPreferredSize(new Dimension(120, 30));
		btnAnterior.setBackground(COLOR_FONDO);
		btnAnterior.setForeground(Color.WHITE);
		btnAnterior.setBorder(null);
		btnAnterior.setFocusPainted(false);
		btnAnterior.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAnterior.addActionListener(e -> irPaginaAnterior());
		
		labelPaginacion = new JLabel("Página 1 de 1");
		labelPaginacion.setForeground(Color.WHITE);
		labelPaginacion.setFont(new Font("Arial", Font.BOLD, 12));
		
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
	
	public void cargarDescuentos() {
		descuentosFiltrados = ctrl.obtenerDescuentos();
		descuentosActuales = new ArrayList<>(descuentosFiltrados);
		calcularPaginas();
		actualizarTabla();
		actualizarPaginacion();
	}
	
	private void buscarDescuentos(String termino) {
		descuentosActuales = ctrl.buscarDescuentos(descuentosFiltrados, termino);
		calcularPaginas();
	}
	
	private void actualizarTabla() {
		modeloTabla.setRowCount(0);
		
		int inicio = paginaActual * descuentosPorPagina;
		int fin = Math.min(inicio + descuentosPorPagina, descuentosActuales.size());
		
		for (int i = inicio; i < fin; i++) {
			IDiscount desc = descuentosActuales.get(i);
			
			Object[] fila = {
					String.valueOf(desc.hashCode() % 10000), // ID simplificado
					ctrl.obtenerCategoria(desc),
					ctrl.obtenerTipoDescuento(desc),
					ctrl.obtenerPorcentaje(desc),
					"Ver más",
					"Eliminar"
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
		totalPaginas = (int) Math.ceil((double) descuentosActuales.size() / descuentosPorPagina);
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
	 * VER DETALLES DEL DESCUENTO
	 * ========================================================
	 */
	
	private void verDetallesDescuento(IDiscount descuento) {
		panelPadre.mostrarDetalles(descuento);
	}
	
	/**
	 * ========================================================
	 * ELIMINAR DESCUENTO
	 * ========================================================
	 */
	
	private void eliminarDescuento(IDiscount descuento, int fila, int inicio) {
		int opcion = JOptionPane.showConfirmDialog(
			this,
			"¿Estás seguro de que deseas eliminar este descuento?\n\n" + descuento.getDescription(),
			"Confirmar eliminación",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE
		);
		
		if (opcion == JOptionPane.YES_OPTION) {
			if (ctrl.eliminarDescuento(descuento)) {
				cargarDescuentos();
			}
		}
	}
	
	/**
	 * ========================================================
	 * LIMPIAR Y REFRESCAR
	 * ========================================================
	 */
	
	public void limpiarBusqueda() {
		aplicarPlaceholder();
		paginaActual = 0;
		cargarDescuentos();
	}
}
