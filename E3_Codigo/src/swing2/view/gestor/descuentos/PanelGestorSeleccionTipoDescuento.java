package swing2.view.gestor.descuentos;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para seleccionar el tipo de descuento a crear.
 * Muestra 4 opciones en una columna centrada.
 */
public class PanelGestorSeleccionTipoDescuento extends JPanel {
	private PanelGestionDescuentos panelPadre;
	
	private static final Color COLOR_FONDO = new Color(23, 48, 79);
	private static final Color COLOR_BOTON = new Color(52, 152, 219);
	private static final Color COLOR_BOTON_HOVER = new Color(41, 128, 185);
	
	public PanelGestorSeleccionTipoDescuento(PanelGestionDescuentos panelPadre) {
		this.panelPadre = panelPadre;
		
		this.setBackground(COLOR_FONDO);
		this.setLayout(new GridBagLayout());
		
		// Panel central con los botones
		JPanel panelCentral = crearPanelBotones();
		
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		g.weightx = 1.0;
		g.weighty = 1.0;
		g.anchor = GridBagConstraints.CENTER;
		this.add(panelCentral, g);
	}
	
	private JPanel crearPanelBotones() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel titulo = new JLabel("SELECCIONA TIPO DE DESCUENTO");
		titulo.setFont(new Font("Arial", Font.BOLD, 24));
		titulo.setForeground(Color.WHITE);
		titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(titulo);
		panel.add(Box.createVerticalStrut(40));
		
		// Botón 1: Rebaja %
		JButton btn1 = crearBotonTipo(
			0,
			"📊 REBAJA PORCENTUAL",
			"Descuento del X% en productos de una categoría específica"
		);
		panel.add(btn1);
		panel.add(Box.createVerticalStrut(20));
		
		// Botón 2: Volumen €
		JButton btn2 = crearBotonTipo(
			1,
			"💰 DESCUENTO POR VOLUMEN",
			"Descuento en €€ si el carrito supera un gasto mínimo"
		);
		panel.add(btn2);
		panel.add(Box.createVerticalStrut(20));
		
		// Botón 3: Regalo
		JButton btn3 = crearBotonTipo(
			2,
			"🎁 REGALO PROMOCIONAL",
			"Regala un producto si el carrito supera un gasto mínimo"
		);
		panel.add(btn3);
		panel.add(Box.createVerticalStrut(20));
		
		// Botón 4: Cantidad X×Y
		JButton btn4 = crearBotonTipo(
			3,
			"📦 OFERTA POR CANTIDAD",
			"Compra X productos y paga solo por Y unidades"
		);
		panel.add(btn4);
		panel.add(Box.createVerticalStrut(40));
		
		// Botón cancelar
		JButton btnCancelar = new JButton("☒ Cancelar");
		btnCancelar.setPreferredSize(new Dimension(350, 40));
		btnCancelar.setMaximumSize(new Dimension(350, 40));
		btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnCancelar.setBackground(new Color(230, 126, 34));
		btnCancelar.setForeground(Color.WHITE);
		btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
		btnCancelar.setBorder(null);
		btnCancelar.setFocusPainted(false);
		btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCancelar.addActionListener(e -> panelPadre.mostrarListado());
		
		panel.add(btnCancelar);
		
		return panel;
	}
	
	private JButton crearBotonTipo(int tipo, String titulo, String descripcion) {
		JButton btn = new JButton();
		btn.setPreferredSize(new Dimension(450, 80));
		btn.setMaximumSize(new Dimension(450, 80));
		btn.setMinimumSize(new Dimension(450, 80));
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.setBackground(COLOR_BOTON);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Arial", Font.BOLD, 13));
		btn.setBorder(null);
		btn.setFocusPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setContentAreaFilled(true);
		
		// Layout del botón con dos líneas de texto
		btn.setLayout(new BoxLayout(btn, BoxLayout.Y_AXIS));
		
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 13));
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel lblDesc = new JLabel(descripcion);
		lblDesc.setFont(new Font("Arial", Font.PLAIN, 11));
		lblDesc.setForeground(new Color(220, 220, 220));
		lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		btn.add(Box.createVerticalGlue());
		btn.add(lblTitulo);
		btn.add(Box.createVerticalStrut(5));
		btn.add(lblDesc);
		btn.add(Box.createVerticalGlue());
		
		// Efecto hover
		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				btn.setBackground(COLOR_BOTON_HOVER);
			}
			
			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				btn.setBackground(COLOR_BOTON);
			}
		});
		
		// Acción
		btn.addActionListener(e -> panelPadre.mostrarAnadirDescuentoConTipo(tipo));
		
		return btn;
	}
}
