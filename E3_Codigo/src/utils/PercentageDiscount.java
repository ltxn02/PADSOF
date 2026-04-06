package utils;
import java.util.Date;

public class PercentageDiscount extends Discount {
    private double percentage;

    public PercentageDiscount(double percentage, String description, Date from, Date to) {
        // Le pasamos el tipo "PORCENTAJE" a la clase padre
        super("PORCENTAJE", description, from, to);
        if (percentage < 0 || percentage > 1) {
            throw new IllegalArgumentException("Invalid percentage");
        }
        this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        // Devuelve el dinero que te ahorras
        return originalPrice * percentage;
    }

    public double getPercentage() {
        return this.percentage;
    }
}