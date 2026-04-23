package flc;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a booking made by a member for a lesson.
 * Status lifecycle: booked → attended | changed | cancelled
 */
public class Booking {
    private static int bookingCounter = 1;

    private int bookingId;
    private Member member;
    private Lesson lesson;
    private String status; // "booked", "attended", "changed", "cancelled"
    private List<Review> reviews;

    public Booking(Member member, Lesson lesson) {
        this.bookingId = bookingCounter++;
        this.member    = member;
        this.lesson    = lesson;
        this.status    = "booked";
        this.reviews   = new ArrayList<>();
    }

    // --- Getters ---
    public int    getBookingId() { return bookingId; }
    public Member getMember()    { return member; }
    public Lesson getLesson()    { return lesson; }
    public String getStatus()    { return status; }
    public List<Review> getReviews() { return reviews; }

    // --- Setters ---
    public void setLesson(Lesson lesson) { this.lesson = lesson; }
    public void setStatus(String status) { this.status = status; }

    public void addReview(Review r) { reviews.add(r); }

    @Override
    public String toString() {
        return String.format("Booking #%d | %s | %s | Status: %s",
                bookingId, member.getName(), lesson.toString(), status);
    }
}
