package ui;
import java.util.List;
import model.catalog.SecondHandProduct;

public class IntercambiadorCasoDefault {
    public static void mostrar(List<SecondHandProduct> productos, int index ){
        if (index >= 1 && index <= productos.size()) {
            SecondHandProduct select = productos.get(index -1);
            System.out.println("Detalles: \n" );
            System.out.println(select);
        } else {
            System.out.println("Selección no válida.");
        }
    }
}
