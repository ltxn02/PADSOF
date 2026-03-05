import Ids;

public abstract class Producto {
    private int idProducto;
    private String nombre;
    private double precio;
    private String marca;
    private int stock;

    public Producto(String nombre, String marca, int stock){
        /*Se haria una comprobación de errores en los inputs*/
        this.idProducto = newProductId(); // Importamos una clase de identificadores (user, productos, etc.) con metodos que devuelvan el ultimo id y se incremente automaticamente
        this.nombre = nombre;
        this.marca = marca;
        this.stock = stock;
        this.precio = 0; /*Valor por defecto*/
    }

    public long getProductId(Producto product){
        return this.idProducto;
    }

    public String getProductName(Producto product){
        return this.nombre;
    }

    public double getProductPrice(Producto product){
        return this.precio;
    }

    public String getProductBrand(Producto product){
        return this.marca;
    }

    public int getProductStock(Producto product){
        return this.stock;
    }

    public Status setProductName(Producto product, String name){
        if (!product || !name) {
            return ERROR;
        }

        this.nombre = name;
        return OK;
    }

    public Status setProductPrice(Producto product, double price){
        if (!product || price<=0) {
            return ERROR;
        }

        this.precio = price;

        return OK;
    }

    public Status setProductBrand(Producto product || String brand){
        if (!product || !brand) {
            return ERROR;
        }

        this.marca = brand;

        return OK;
    }

    public Status setProductStock(Producto product, int stock){
        if (!product || stock<0) {
            return ERROR;
        }

        this.stock = stock;

        return OK;
    }
}