import java.util.ArrayList;

public class Game extends Product{
    private int nPlayers;
    private ArrayList<String> mechanics;
    private AgeRange ageRange;

    public Game(String name, String description, double price, String image, ArrayList<Category> categories, int stock, ArrayList<Review> reviews, Discount discount, int nPlayers, ArrayList<String> mechanics, AgeRange ageRange) {
        super(name, description, price, image, categories, stock, reviews, discount);
        if (nPlayers < 1) {
            throw new IllegalArgumentException("Argumentos inválidos");
        }
        this.nPlayers = nPlayers;
        this.mechanics = mechanics;
        this.ageRange = ageRange;
    }
    
    public AgeRange getAgeRange() {
    	return this.ageRange;
    }
}
