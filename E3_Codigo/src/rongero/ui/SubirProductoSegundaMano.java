package ui;

import logic.Application;
import model.catalog.SecondHandProduct;
import model.user.Client;
import util.Condition;
import util.ItemType;
import java.util.Scanner;

/**
 * Clase de interfaz de usuario para el registro de artículos de segunda mano.
 * Permite a los clientes introducir los metadatos de sus productos (nombre, descripción,
 * tipo y estado de conservación) para integrarlos en el flujo de tasación de la plataforma.
 * * @author Taha Ridda En Naji
 * @version 3.0
 */
public class SubirProductoSegundaMano {

    /**
     * Inicia el proceso interactivo de subida de un producto para intercambio.
     * El producto se crea con un valor económico de 0.0 y un estado de tasación
     * falso (isAppraised = false), quedando a la espera de la revisión de un empleado.
     * * @param cliente El cliente propietario que realiza la subida del artículo.
     * @param scanner Instancia de Scanner para capturar los datos desde la consola.
     * @author Taha Ridda En Naji
     * @version 3.0
     */
    public static void SubirProductoSegundaMano(Client cliente, Scanner scanner) {
        System.out.println("\n--- FORMULARIO DE SUBIDA DE PRODUCTO ---");

        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción breve: ");
        String descripcion = scanner.nextLine();

        System.out.print("Ruta de la imagen (ej. resources/img/item.jpg): ");
        String foto = scanner.nextLine();

        /**
         * Selección del Tipo de Producto.
         * Mapea la entrada numérica del usuario con los valores del enumerado ItemType.
         */
        System.out.println("Selecciona el Tipo de producto:");
        System.out.println("1. CÓMIC / MANGA");
        System.out.println("2. VIDEOJUEGO");
        System.out.println("3. FIGURA / MERCHANDISING");
        System.out.print("Opción: ");

        String tipoInput = scanner.nextLine();
        ItemType tipo = ItemType.COMIC;
        if (tipoInput.equals("2")) tipo = ItemType.GAME;
        if (tipoInput.equals("3")) tipo = ItemType.FIGURINE;

        /**
         * Selección del Estado de Conservación.
         * Mapea la entrada numérica con los valores del enumerado Condition para
         * facilitar la futura tasación por parte del empleado.
         */
        System.out.println("\nEstado de conservación:");
        System.out.println("1. PERFECTO (Como nuevo)");
        System.out.println("2. MUY BUENO (Signos mínimos)");
        System.out.println("3. USO LIGERO (Desgaste normal)");
        System.out.println("4. USO EVIDENTE (Marcas visibles)");
        System.out.println("5. DAÑADO (Afecta estética/uso)");
        System.out.print("Opción: ");

        String condInput = scanner.nextLine();
        Condition condicion = Condition.PERFECTO;
        switch (condInput) {
            case "2": condicion = Condition.MUY_BUENO; break;
            case "3": condicion = Condition.USO_LIGERO; break;
            case "4": condicion = Condition.USO_EVIDENTE; break;
            case "5": condicion = Condition.DAÑADO; break;
        }

        /**
         * Instanciación del Producto de Segunda Mano.
         * Se delega la lógica de construcción al modelo SecondHandProduct.
         * Nótese que el precio se inicializa a 0.0 y el flag de tasación a 'false'.
         */
        SecondHandProduct nuevoProducto = new SecondHandProduct(
                nombre, descripcion, foto, 0.0, false, tipo, condicion, cliente
        );

        /**
         * Registro en el sistema.
         * Sincroniza la adición tanto en el inventario personal del cliente como
         * en el repositorio global de la aplicación para que sea visible por los tasadores.
         */
        cliente.addMyProduct(nuevoProducto);
        Application.getInstance().addSecondHandProduct(nuevoProducto);

        System.out.println("\n[✔] Producto '" + nombre + "' registrado correctamente.");
        System.out.println("[i] El artículo aparecerá en la tienda una vez sea valorado por nuestro equipo.");
    }
}