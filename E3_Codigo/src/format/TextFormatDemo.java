// TextFormatDemo.java
package format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Programa de demostración para ver todas las funciones de TextFormat
 * y cómo se visualizan en la terminal.
 */
public class TextFormatDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║         DEMOSTRACIÓN DE TEXTFORMAT - SALIDAS               ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // ====================================================================
        // 1. ITEMS CORTOS
        // ====================================================================
        printSection("1. ITEM SHORT (Nombre + Precio)");
        System.out.println(TextFormat.itemShort("One Piece Vol. 1", 7.99));
        System.out.println(TextFormat.itemShort("Manga sin valorar", 0.0));

        // ====================================================================
        // 2. ITEMS BREVES (para listas)
        // ====================================================================
        printSection("2. ITEM BRIEF (para listas)");
        System.out.println(TextFormat.itemBrief("One Piece Vol. 1", 
            "El inicio de la aventura de Luffy", 7.99));
        System.out.println(TextFormat.itemBrief("Catan", 
            "Juego de estrategia y negociación", 45.00));

        // ====================================================================
        // 3. ITEMS DETALLADOS (base para productos específicos)
        // ====================================================================
        printSection("3. ITEM DETAILED (Base de productos)");
        List<String> comicCategories = Arrays.asList("Cómics", "Manga", "Aventura");
        System.out.println(TextFormat.itemDetailed("One Piece Vol. 1",
            "El inicio de la aventura de Luffy en su viaje para convertirse\n" +
            "en el Rey de los Piratas. Una historia épica de aventura.",
            7.99, comicCategories));

        // ====================================================================
        // 4. COMIC DETALLADO
        // ====================================================================
        printSection("4. COMIC DETALLADO");
        String comicBase = TextFormat.itemDetailed("One Piece Vol. 1",
            "El inicio de la aventura de Luffy",
            7.99, comicCategories);
        List<String> authors = Arrays.asList("Eiichiro Oda");
        System.out.println(TextFormat.comicDetailed(comicBase, "208", "Planeta Cómic", 
            "1997", authors));

        // ====================================================================
        // 5. GAME DETALLADO
        // ====================================================================
        printSection("5. GAME DETALLADO");
        List<String> gameCategories = Arrays.asList("Juegos de mesa");
        String gameBase = TextFormat.itemDetailed("Catan",
            "Juego de estrategia donde compites por construir asentamientos",
            45.00, gameCategories);
        List<String> mechanics = Arrays.asList("Gestión de recursos", "Tirar dados");
        System.out.println(TextFormat.gameDetailed(gameBase, "2-4", mechanics, "10-99 años"));

        // ====================================================================
        // 6. FIGURINE DETALLADA
        // ====================================================================
        printSection("6. FIGURINE DETALLADA");
        List<String> figurineCategories = Arrays.asList("Anime", "Figuras");
        String figurineBase = TextFormat.itemDetailed("Figura Goku",
            "Figura de colección de 15cm de altura",
            35.50, figurineCategories);
        System.out.println(TextFormat.figurineDetailed(figurineBase, "15.0x5.0x5.0 cm", 
            "PVC", "Dragon Ball"));

        // ====================================================================
        // 7. SEGUNDA MANO - BROWSER
        // ====================================================================
        printSection("7. SEGUNDA MANO - Vista Comprador");
        String secondHandShort = TextFormat.itemShort("Manga antiguo", 12.50);
        System.out.println(TextFormat.secondHandDetailedBrowser(secondHandShort, "COMIC", "MUY_BUENO"));

        // ====================================================================
        // 8. SEGUNDA MANO - PROPIETARIO
        // ====================================================================
        printSection("8. SEGUNDA MANO - Vista Propietario");
        System.out.println(TextFormat.secondHandDetailedOwner(secondHandShort, "COMIC", 
            "MUY_BUENO", "Sí", "No"));

        // ====================================================================
        // 9. MENSAJES
        // ====================================================================
        printSection("9. MENSAJES DE ERROR, ÉXITO E INFO");
        System.out.println(TextFormat.genericErrorMessage());
        System.out.println(TextFormat.genericErrorMessage("Stock insuficiente"));
        System.out.println(TextFormat.closeAppMessage());

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    FIN DE LA DEMOSTRACIÓN                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    /**
     * Utilidad para imprimir separadores de sección.
     */
    private static void printSection(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60));
    }
}
