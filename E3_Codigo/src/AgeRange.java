import java.util.ArrayList;

public class AgeRange {
    private int minAge;
    private int maxAge;
    private ArrayList<Game>  games;

    public AgeRange(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < 0 || minAge > maxAge) {
            throw new IllegalArgumentException();
        }
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.games = new ArrayList<>();
    }

    public AgeRange(int minAge, int maxAge, ArrayList<Game> games) {
        this(minAge, maxAge);
        this.games = games;
    }
}
