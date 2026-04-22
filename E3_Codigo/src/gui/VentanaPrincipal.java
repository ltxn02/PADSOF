package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación Rongero.
 * Utiliza un CardLayout para alternar entre diferentes paneles (Login, Catálogo, etc.)
 * sin necesidad de abrir múltiples ventanas.
 */
public class VentanaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor; // Aquí irán metidas todas las "cartas"

    public VentanaPrincipal() {
        super("Rongero - Tu tienda de segunda mano"); // Título de la ventana

        // Configuración básica de la ventana
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null); // Centrar en la pantalla

        // Inicializamos el CardLayout y el panel que lo usará
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // --- CREACIÓN DE LAS VISTAS (PANELES) ---
        // Aquí iremos instanciando nuestros paneles personalizados
        PanelLogin panelLogin = new PanelLogin(this);

        // Añadimos los paneles al contenedor asignándoles un "nombre" (String) clave
        panelContenedor.add(panelLogin, "LOGIN");

        // Añadimos el contenedor principal a la ventana
        this.getContentPane().add(panelContenedor);
    }

    /**
     * Metodo para cambiar de pantalla desde cualquier controlador.
     * @param nombrePantalla El identificador del panel al que queremos ir.
     */
    public void mostrarPantalla(String nombrePantalla) {
        cardLayout.show(panelContenedor, nombrePantalla);
    }

    public static void main(String[] args) {
        // Cargamos los datos de nuestra aplicación (igual que en tu main de consola)
        logic.Application.cargarDatos("rongero_data.dat");

        // Lanzamos la interfaz gráfica en el hilo especial de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true); // Mostrar ventana
            }
        });
    }
}