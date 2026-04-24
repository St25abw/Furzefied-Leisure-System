package flc;


public class Review {
    private Member author;
    private String reviewText;
    private int rating; // 1-5

    public Review(Member author, String reviewText, int rating) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be 1-5.");
        this.author     = author;
        this.reviewText = reviewText;
        this.rating     = rating;
    }

    public Member getAuthor()   { return author; }
    public String getReviewText() { return reviewText; }
    public int getRating()      { return rating; }

    @Override
    public String toString() {
        return String.format("  %s rated %d/5: \"%s\"", author.getName(), rating, reviewText);
    }
}
