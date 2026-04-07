package utils;
import users.Manager;
import java.util.Date;

public abstract class Discount {
    private static int lastDiscountId = 1;
    private int discountId;
    private String type;
    private String description;
    private Date from;
    private Date to;
    private Manager createdBy;

    public Discount(String type, String description, Date from, Date to) {
        this.type = type;
        this.description = description;
        this.from = from;
        this.to = to;
        this.discountId = lastDiscountId++;
    }

    public boolean isValid(Date today) {
        return today.after(this.from) && today.before(this.to);
    }

    // --- Cada hijo decidirá cómo se calcula la rebaja ---
    public abstract double applyDiscount(double originalPrice);

    public String getDescription() {
        return this.description;
    }

    public String getType() {
        return this.type;
    }
}