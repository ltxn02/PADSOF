package utils;
import java.util.Date;

public class FixedDiscount extends Discount {
    private double amount;

    public FixedDiscount(double amount, String description, Date from, Date to) {
        // Le pasamos el tipo "FIJO" a la clase padre
        super("FIJO", description, from, to);
        this.amount = amount;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        // Te ahorras la cantidad fija (usamos Math.min para no devolver dinero si el descuento es mayor que el precio)
        return Math.min(this.amount, originalPrice);
    }

    public double getAmount() {
        return this.amount;
    }
}