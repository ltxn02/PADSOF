public class Review extends BaseElement{
    private int rating;
    private String comment;
    private Product product;
    /*
    * private Client postedBy;
    * */

    public Review(int rating, String comment, Product product/*, Client postedBy */) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Invalid rating");
        }
        this.rating = rating;
        this.comment = comment;
        this.product = product;
    }

    public int getRating() {
        return rating;
    }
}
