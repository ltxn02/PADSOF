package rongero.ui;
import logic.Application;
import model.catalog.NewProduct;
import java.util.List;

public class VerCatalogoInvitado {
    public static void mostrar(){
        System.out.println("\n--- CATÁLOGO DE PRODUCTOS ---");

        List<NewProduct> productos = Application.getInstance().getCatalog();

        if (productos.isEmpty()) {
        System.out.println("Actualmente no hay productos en la tienda.");
        return;
    }

        for (NewProduct p : productos) {
        System.out.println("- " + p.getName() + " | Precio: " + p.getPrice() + "€");
    }
}}
