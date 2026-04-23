package flc;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a registered member of Furzefield Leisure Centre.
 */
public class Member {
    private static int idCounter = 1;

    private int memberId;
    private String name;
    private List<Booking> bookings;

    public Member(String name) {
        this.memberId = idCounter++;
        this.name = name;
        this.bookings = new ArrayList<>();
    }

    public int getMemberId() { return memberId; }
    public String getName()   { return name; }
    public List<Booking> getBookings() { return bookings; }

    public void addBooking(Booking b) { bookings.add(b); }

    /**
     * Returns active (non-cancelled) bookings that haven't been attended yet.
     */
    public List<Booking> getActiveBookings() {
        List<Booking> active = new ArrayList<>();
        for (Booking b : bookings) {
            String s = b.getStatus();
            if (s.equals("booked") || s.equals("changed")) {
                active.add(b);
            }
        }
        return active;
    }

    /**
     * Checks whether this member already has a booking for the given lesson
     * (excluding cancelled bookings).
     */
    public boolean hasBookingFor(Lesson lesson) {
        for (Booking b : bookings) {
            if (!b.getStatus().equals("cancelled") && b.getLesson().equals(lesson)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for a time conflict: returns true if the member already has a
     * non-cancelled booking on the same day and time slot.
     */
    public boolean hasTimeConflict(Lesson candidate) {
        for (Booking b : bookings) {
            if (b.getStatus().equals("cancelled")) continue;
            Lesson booked = b.getLesson();
            if (booked.getDay().equals(candidate.getDay())
                    && booked.getTimeSlot().equals(candidate.getTimeSlot())
                    && booked.getWeekendNumber() == candidate.getWeekendNumber()
                    && !booked.equals(candidate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Member[" + memberId + "] " + name;
    }
}
