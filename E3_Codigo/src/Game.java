import java.util.ArrayList;

public class Game extends Product{
    private int nPlayers;
    private ArrayList<String> mechanics;
    private AgeRange ageRange;

    public Game(int nPlayers, ArrayList<String> mechanics, AgeRange ageRange) {
        if (nPlayers < 1) {
            throw new IllegalArgumentException("Argumentos invalidos");
        }
        this.nPlayers = nPlayers;
        this.mechanics = mechanics;
        this.ageRange = ageRange;
    }
}
