package catalog;

import java.util.ArrayList;
import utils.*;


public class Game extends Product implements java.io.Serializable{
    private int nPlayers;
    private ArrayList<String> mechanics;
    private AgeRange ageRange;

    public Game(String name, String description, double price, String image, int stock, ArrayList<Category> categories, ArrayList<Review> reviews, Discount discount, int nPlayers, ArrayList<String> mechanics, AgeRange ageRange) {
        super(name, description, price, image, stock, categories, reviews, discount);
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
