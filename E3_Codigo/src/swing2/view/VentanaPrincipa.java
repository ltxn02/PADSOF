package swing2.view;

import java.awt.CardLayout;
import java.awt.Component;
import java.net.URL;

import javax.swing.*;

import logic.Application;
import swing2.view.empleado.PanelProductosEmpleado;
import swing2.view.empleado.PanelIntercambiosEmpleado;
import swing2.view.empleado.PanelPedidosEmpleado;
import swing2.view.gestor.PanelDashboard;
import users.*;

public class VentanaPrincipa extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel contenedor = new JPanel(cardLayout);


    private RegisteredUser usuarioLogueado = null;


    private PanelInicioo panelCliente = null;
    private PanelDashboard panelGestor = null;
    private PanelCarrito panelCarrito = null;

    public VentanaPrincipa() {

        Application.cargarDatos("rongero_data.dat");

        setTitle("RONGERO - Tienda Frikis");
        setSize(1400, 750);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.out.println("[Sistema] Guardando todos los datos en rongero_data.dat antes de salir...");
                Application.guardarDatos("rongero_data.dat");
                System.out.println("[Sistema] ¡Datos guardados con éxito! Cerrando aplicación.");
                System.exit(0);
            }
        });
        setLocationRelativeTo(null);

        try {
            URL iconURL = getClass().getResource("/foto/logoi.png"); 
            if (iconURL == null) {
                iconURL = getClass().getResource("../../foto/logoi.png");
            }

            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                this.setIconImage(icon.getImage());
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono de la aplicación");
        }

        ReproductorMusica musica = new ReproductorMusica();
        musica.reproducirMusica("/foto/musica_fondo.wav");
        panelCliente = new PanelInicioo(this, usuarioLogueado);
        panelGestor = new PanelDashboard(this, null);



        contenedor.add(panelCliente, "INICIO");
        contenedor.add(new PanelIntercambios(this, usuarioLogueado), "INTERCAMBIOS");
        contenedor.add(new PanelLoginn(this), "LOGIN");
        contenedor.add(new PanelRegistro(this), "REGISTRO");
        contenedor.add(panelGestor, "GESTOR");

        add(contenedor);
        setVisible(true);
    }

    /**
     * Cambia el usuario actual y refresca el Panel de Inicio.
     * Esto permite que el sistema de recomendaciones actúe si el usuario es un Client.
     * ~ Lidia, 20:10, 05/05/2026
     */
    public void cambiarSesion(RegisteredUser nuevoUsuario) {
        this.usuarioLogueado = nuevoUsuario;

        if (nuevoUsuario == null) {
            mostrarPantalla("INICIO");
        } else if (nuevoUsuario instanceof Manager) {
            Component[] componentes = contenedor.getComponents();
            for (Component c : componentes) {
                if (c instanceof PanelDashboard) {
                    contenedor.remove(c);
                    break;
                }
            }


            panelGestor = new PanelDashboard(this, (Manager)nuevoUsuario);
            contenedor.add(panelGestor, "GESTOR");

            contenedor.revalidate();
            contenedor.repaint();

            mostrarPantalla("GESTOR");

        } else if (nuevoUsuario instanceof Employee) {



            Component[] componentes = contenedor.getComponents();
            for (Component c : componentes) {

                if (c instanceof PanelProductosEmpleado ||
                        c instanceof PanelIntercambiosEmpleado ||
                        c instanceof PanelPedidosEmpleado) {
                    contenedor.remove(c);
                }
            }


            Employee emp = (Employee) nuevoUsuario;
            contenedor.add(new PanelProductosEmpleado(this, emp), "PRODUCTOS_EMPLEADO");
            contenedor.add(new PanelIntercambiosEmpleado(this, emp), "INTERCAMBIOS_EMPLEADO");
            contenedor.add(new PanelPedidosEmpleado(this, emp), "PEDIDOS_EMPLEADO");

            contenedor.revalidate();
            contenedor.repaint();

            mostrarPantalla("PRODUCTOS_EMPLEADO");

        } else if (nuevoUsuario instanceof Client) {


            Component[] componentes = contenedor.getComponents();
            for (Component c : componentes) {
                if (c instanceof PanelInicioo || c instanceof PanelIntercambios) {
                    contenedor.remove(c);
                }
            }


            panelCliente = new PanelInicioo(this, nuevoUsuario);
            contenedor.add(panelCliente, "INICIO");
            contenedor.add(new PanelIntercambios(this, nuevoUsuario), "INTERCAMBIOS");


            contenedor.revalidate();
            contenedor.repaint();


            mostrarPantalla("INICIO");
        }
    }

    /**
     * Cambia la visibilidad entre los paneles del CardLayout.
     */
    public void mostrarPantalla(String nombre) {
        if (nombre.equals("CARRITO")) {

            Component[] componentes = contenedor.getComponents();
            for (Component c : componentes) {
                if (c instanceof PanelCarrito) {
                    contenedor.remove(c);
                    break;
                }
            }


            if (usuarioLogueado instanceof Client) {
                Client cliente = (Client) usuarioLogueado;

                panelCarrito = new PanelCarrito(cliente.getShoppingCart(), this);
                contenedor.add(panelCarrito, "CARRITO");
            } else {
                JOptionPane.showMessageDialog(this,
                        "¡Atención! Debes iniciar sesión para poder comprar productos.",
                        "Sesión no iniciada",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }
        } else if (nombre.equals("PERFIL")) {
            if (usuarioLogueado == null) {

                Object[] opciones = {"Iniciar Sesión", "Registrarse", "Cancelar"};


                int seleccion = JOptionPane.showOptionDialog(
                        this,
                        "¡Te damos la bienvenida! \nPara continuar, necesitas una cuenta.",
                        "Sesión no iniciada",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                );


                switch (seleccion) {
                    case 0:
                        mostrarPantalla("LOGIN");
                        break;
                    case 1:
                        mostrarPantalla("REGISTRO");
                        break;
                    default:

                        break;
                }
                
                return;
            }

            Component[] componentes = contenedor.getComponents();
            for (Component c : componentes) {
                if (c instanceof PanelPerfil) {
                    contenedor.remove(c);
                    break;
                }
            }

            PanelPerfil pPerfil = new PanelPerfil(usuarioLogueado, this);
            contenedor.add(pPerfil, "PERFIL");
        }


        contenedor.revalidate();
        contenedor.repaint();
        cardLayout.show(contenedor, nombre);
    }

    public RegisteredUser getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");

        SwingUtilities.invokeLater(() -> new VentanaPrincipa());
    }
    public void cambiarPanelDinamico(JPanel nuevoPanel) {
        
        String idDinamico = "PANEL_DINAMICO_" + nuevoPanel.hashCode();
        contenedor.add(nuevoPanel, idDinamico);

        
        contenedor.revalidate();
        contenedor.repaint();

        
        cardLayout.show(contenedor, idDinamico);
    }
}