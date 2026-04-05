package util;
import model.user.Manager;
import java.util.Date;

public class Discount {
    private static int lastDiscountId = 1;
    private int discountId;
    private double percentage;
    private String type;
    private String description;
    private Date from;
    private Date to;
    private Manager createdBy;

    public Discount(double percentage, String type, String description, Date from, Date to) {
        if (percentage < 0 || percentage > 1) {
            throw new IllegalArgumentException("Invalid percentage");
        }
        this.percentage = percentage;
        this.type = type;
        this.description = description;
        this.from = from;
        this.to = to;
        this.discountId = lastDiscountId;
        Discount.lastDiscountId++;
    }

    public boolean isValid(Date today) {
        if (today.after(this.from) && today.before(this.to)) {
            return true;
        }
        return false;
    }

    public double applyDiscount(double originalPrice){
        return originalPrice * percentage;
    }
}
