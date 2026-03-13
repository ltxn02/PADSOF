import java.util.ArrayList;

public class Comic extends Product{
    private int nPages;
    private String publisher;
    private int publicacionYear;
    private ArrayList<String> writtenBy;

    public Comic(int nPages, String publisher, int publicacionYear, ArrayList<String> writtenBy) {
        if (nPages <= 0) {
            this.nPages = 1;
        } else {
            this.nPages = nPages;
        }

        this.publisher = publisher;
        this.publicacionYear = publicacionYear;
        this.writtenBy = writtenBy;
    }
}
