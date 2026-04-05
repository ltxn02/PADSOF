package rongero.ui;

import logic.Application;
import model.catalog.Category;
import java.util.List;
import java.util.ArrayList;

public class VerCategoria {
    public static void VerCategorias() {
        System.out.println("\n--- CATEGORÍAS ACTUALES ---");
        List<Category> categorias = Application.getInstance().getGlobalCategories();

        if (categorias.isEmpty()) {
            System.out.println("No hay categorías registradas en el sistema.");
            return;
        }

        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ".- " + categorias.get(i).getNameCategory());
        }
    }

}
