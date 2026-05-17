package swing2.view.gestor.descuentos;

import swing2.controller.gestor.GestorDescuentoController;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para añadir descuentos con la misma estética que PanelAnadirEmpleado.
 * Soporta los 4 tipos de descuentos: Rebaja %, Volumen, Regalo, Cantidad.
 */
public class PanelAnadirDescuento extends JPanel {
	private PanelGestionDescuentos panelPadre;
	private GestorDescuentoController ctrl;
	
	private int tipoActual = -1;
	
	// Campos comunes
	private JTextField txtDescripcion;
	private JTextField txtFechaInicio, txtFechaFin;
	
	// Campos específicos para cada tipo
	private JComboBox<String> comboCategoria;
	private JTextField txtPorcentaje;			// Rebaja %
	private JTextField txtGastoMinimo;			// Volumen y Regalo
	private JTextField txtDescuentoEuro;		// Volumen
	private JComboBox<String> comboProductoRegalo; // Regalo
	private JTextField txtLleva;				// Cantidad
	private JTextField txtPaga;					// Cantidad
	
	// Panel dinámico que cambia según el tipo
	private JPanel panelCamposDinamicos;
	
	private static final Color COLOR_FONDO = new Color(51, 66, 90);
	
	public PanelAnadirDescuento(PanelGestionDescuentos panelPadre, GestorDescuentoController ctrl, int tipo) {
		this.panelPadre = panelPadre;
		this.ctrl = ctrl;
		this.tipoActual = tipo;
		
		this.setBackground(COLOR_FONDO);
		this.setLayout(new GridBagLayout());
		
		crearFormulario();
	}
	
	private void crearFormulario() {
		this.removeAll();
		
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(8, 8, 8, 8);
		g.fill = GridBagConstraints.HORIZONTAL;
		
		// Título dinámico
		String[] titulos = {
			"CREAR REBAJA PORCENTUAL",
			"CREAR DESCUENTO POR VOLUMEN",
			"CREAR REGALO PROMOCIONAL",
			"CREAR OFERTA POR CANTIDAD"
		};
		
		JLabel titulo = new JLabel(titulos[tipoActual], SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 22));
		titulo.setForeground(Color.WHITE);
		g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = 3;
		this.add(titulo, g);
		
		g.gridwidth = 1;
		int r = 1;
		
		// Campo: Descripción (común a todos)
		txtDescripcion = new JTextField(15);
		agregarFilaFormulario("Descripción:", txtDescripcion, null, g, r++);
		
		// Campo: Fecha inicio
		txtFechaInicio = new JTextField(15);
		agregarFilaFormulario("Fecha inicio (DD/MM/YYYY HH:mm):", txtFechaInicio, null, g, r++);
		
		// Campo: Fecha fin
		txtFechaFin = new JTextField(15);
		agregarFilaFormulario("Fecha fin (DD/MM/YYYY HH:mm):", txtFechaFin, null, g, r++);
		
		// Crear campos dinámicos según el tipo
		crearCamposDinamicos(g, r);
		
		r += 5; // Espacio para los campos dinámicos
		
		// Botones
		JPanel panelBotones = crearPanelBotones();
		g.gridy = r;
		g.gridx = 0;
		g.gridwidth = 3;
		g.insets = new Insets(20, 8, 8, 8);
		this.add(panelBotones, g);
		
		this.revalidate();
		this.repaint();
	}
	
	private void crearCamposDinamicos(GridBagConstraints g, int filaInicio) {
		int r = filaInicio;
		
		switch(tipoActual) {
			case 0: // Rebaja %
				comboCategoria = crearComboCategoria();
				agregarFilaFormulario("Categoría:", comboCategoria, null, g, r++);
				
				txtPorcentaje = new JTextField(15);
				agregarFilaFormulario("Porcentaje (%):", txtPorcentaje, null, g, r++);
				break;
				
			case 1: // Volumen €
				txtGastoMinimo = new JTextField(15);
				agregarFilaFormulario("Gasto mínimo (€):", txtGastoMinimo, null, g, r++);
				
				txtDescuentoEuro = new JTextField(15);
				agregarFilaFormulario("Descuento (€):", txtDescuentoEuro, null, g, r++);
				break;
				
			case 2: // Regalo
				txtGastoMinimo = new JTextField(15);
				agregarFilaFormulario("Gasto mínimo (€):", txtGastoMinimo, null, g, r++);
				
				comboProductoRegalo = crearComboProductos();
				agregarFilaFormulario("Producto regalo:", comboProductoRegalo, null, g, r++);
				break;
				
			case 3: // Cantidad X×Y
				comboCategoria = crearComboCategoria();
				agregarFilaFormulario("Categoría:", comboCategoria, null, g, r++);
				
				txtLleva = new JTextField(15);
				agregarFilaFormulario("Lleva (unidades):", txtLleva, null, g, r++);
				
				txtPaga = new JTextField(15);
				agregarFilaFormulario("Paga (unidades):", txtPaga, null, g, r++);
				break;
		}
	}
	
	private void agregarFilaFormulario(String etiqueta, JComponent campo, JToggleButton btn, GridBagConstraints g, int fila) {
		g.gridy = fila;
		
		// Etiqueta
		g.gridx = 0;
		g.anchor = GridBagConstraints.EAST;
		JLabel lbl = new JLabel(etiqueta);
		lbl.setForeground(Color.WHITE);
		lbl.setFont(new Font("Arial", Font.PLAIN, 11));
		this.add(lbl, g);
		
		// Campo
		g.gridx = 1;
		g.anchor = GridBagConstraints.CENTER;
		if (campo instanceof JTextField) {
			((JTextField)campo).setPreferredSize(new Dimension(200, 28));
		} else if (campo instanceof JComboBox) {
			((JComboBox<?>)campo).setPreferredSize(new Dimension(200, 28));
		}
		this.add(campo, g);
		
		// Botón o espacio
		g.gridx = 2;
		if (btn != null) {
			this.add(btn, g);
		} else {
			this.add(Box.createHorizontalStrut(45), g);
		}
	}
	
	private JComboBox<String> crearComboCategoria() {
		JComboBox<String> combo = new JComboBox<>();
		// Obtener categorías del controlador
		for (String cat : ctrl.obtenerNombresCategorias()) {
			combo.addItem(cat);
		}
		return combo;
	}
	
	private JComboBox<String> crearComboProductos() {
		JComboBox<String> combo = new JComboBox<>();
		// Obtener productos del controlador
		for (String prod : ctrl.obtenerNombresProductos()) {
			combo.addItem(prod);
		}
		return combo;
	}
	
	private JPanel crearPanelBotones() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panel.setOpaque(false);
		
		// Botón guardar
		JButton btnGuardar = new JButton("☑ Crear descuento");
		btnGuardar.setPreferredSize(new Dimension(200, 40));
		btnGuardar.setBackground(new Color(46, 204, 113));
		btnGuardar.setForeground(Color.WHITE);
		btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
		btnGuardar.setBorder(null);
		btnGuardar.setFocusPainted(false);
		btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnGuardar.addActionListener(e -> guardarDescuento());
		
		// Botón cancelar
		JButton btnCancelar = new JButton("☒ Cancelar");
		btnCancelar.setPreferredSize(new Dimension(200, 40));
		btnCancelar.setBackground(new Color(231, 76, 60));
		btnCancelar.setForeground(Color.WHITE);
		btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
		btnCancelar.setBorder(null);
		btnCancelar.setFocusPainted(false);
		btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCancelar.addActionListener(e -> volverAlSeleccion());
		
		panel.add(btnGuardar);
		panel.add(btnCancelar);
		
		return panel;
	}
	
	private void guardarDescuento() {
		try {
			String descripcion = txtDescripcion.getText().trim();
			String fechaInicio = txtFechaInicio.getText().trim();
			String fechaFin = txtFechaFin.getText().trim();
			
			if (descripcion.isEmpty()) {
				JOptionPane.showMessageDialog(this, "La descripción es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Las fechas son obligatorias.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Validar y crear según tipo
			boolean exito = false;
			switch(tipoActual) {
				case 0: // Rebaja %
					exito = crearRebajaPorc(descripcion, fechaInicio, fechaFin);
					break;
				case 1: // Volumen
					exito = crearVolumen(descripcion, fechaInicio, fechaFin);
					break;
				case 2: // Regalo
					exito = crearRegalo(descripcion, fechaInicio, fechaFin);
					break;
				case 3: // Cantidad
					exito = crearCantidad(descripcion, fechaInicio, fechaFin);
					break;
			}
			
			if (exito) {
				limpiarCampos();
				panelPadre.mostrarListado();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private boolean crearRebajaPorc(String desc, String fechaInicio, String fechaFin) {
		try {
			String categoria = (String) comboCategoria.getSelectedItem();
			if (categoria.equals("- Seleccionar categoría -")) {
				JOptionPane.showMessageDialog(this, "Selecciona una categoría.", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			double porcentaje = Double.parseDouble(txtPorcentaje.getText().trim());
			return ctrl.crearDescuentoRebaja(desc, categoria, porcentaje, fechaInicio, fechaFin);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "El porcentaje debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	private boolean crearVolumen(String desc, String fechaInicio, String fechaFin) {
		try {
			double gastoMin = Double.parseDouble(txtGastoMinimo.getText().trim());
			double descuento = Double.parseDouble(txtDescuentoEuro.getText().trim());
			
			return ctrl.crearDescuentoVolumen(desc, gastoMin, descuento, fechaInicio, fechaFin);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Los valores deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	private boolean crearRegalo(String desc, String fechaInicio, String fechaFin) {
		try {
			String producto = (String) comboProductoRegalo.getSelectedItem();
			if (producto.equals("- Seleccionar producto -")) {
				JOptionPane.showMessageDialog(this, "Selecciona un producto.", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			double gastoMin = Double.parseDouble(txtGastoMinimo.getText().trim());
			return ctrl.crearDescuentoRegalo(desc, gastoMin, producto, fechaInicio, fechaFin);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "El gasto debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	private boolean crearCantidad(String desc, String fechaInicio, String fechaFin) {
		try {
			String categoria = (String) comboCategoria.getSelectedItem();
			if (categoria.equals("- Seleccionar categoría -")) {
				JOptionPane.showMessageDialog(this, "Selecciona una categoría.", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			int lleva = Integer.parseInt(txtLleva.getText().trim());
			int paga = Integer.parseInt(txtPaga.getText().trim());
			
			return ctrl.crearDescuentoCantidad(desc, categoria, lleva, paga, fechaInicio, fechaFin);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Las cantidades deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	private void volverAlSeleccion() {
		limpiarCampos();
		panelPadre.mostrarSeleccionTipo();
	}
	
	private void limpiarCampos() {
		txtDescripcion.setText("");
		txtFechaInicio.setText("");
		txtFechaFin.setText("");
		
		if (txtPorcentaje != null) txtPorcentaje.setText("");
		if (comboCategoria != null) comboCategoria.setSelectedIndex(0);
		if (txtGastoMinimo != null) txtGastoMinimo.setText("");
		if (txtDescuentoEuro != null) txtDescuentoEuro.setText("");
		if (comboProductoRegalo != null) comboProductoRegalo.setSelectedIndex(0);
		if (txtLleva != null) txtLleva.setText("");
		if (txtPaga != null) txtPaga.setText("");
	}
}
