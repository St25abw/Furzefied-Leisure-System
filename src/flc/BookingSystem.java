package flc;

import java.util.ArrayList;
import java.util.List;

/**
 * Core business-logic facade for Furzefield Leisure Centre.
 * Follows the Facade design pattern: wraps Timetable, Member, Booking, Review.
 */
public class BookingSystem {
    private Timetable timetable;
    private List<Member> members;
    private List<Booking> allBookings;

    public BookingSystem(Timetable timetable) {
        this.timetable   = timetable;
        this.members     = new ArrayList<>();
        this.allBookings = new ArrayList<>();
    }

    // -----------------------------------------------------------------------
    // Member management
    // -----------------------------------------------------------------------

    public void registerMember(Member m) { members.add(m); }

    public Member findMemberById(int id) {
        for (Member m : members) {
            if (m.getMemberId() == id) return m;
        }
        return null;
    }

    public Member findMemberByName(String name) {
        for (Member m : members) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    public List<Member> getAllMembers() { return members; }

    // -----------------------------------------------------------------------
    // Timetable queries
    // -----------------------------------------------------------------------

    public Timetable getTimetable() { return timetable; }

    public List<Lesson> getLessonsByDay(String day) {
        return timetable.getLessonsByDay(day);
    }

    public List<Lesson> getLessonsByType(ExerciseType type) {
        return timetable.getLessonsByType(type);
    }

    // -----------------------------------------------------------------------
    // Booking operations
    // -----------------------------------------------------------------------

    /**
     * Books a member into a lesson.
     * @return the new Booking on success, or null with an error message printed.
     */
    public Booking bookLesson(Member member, Lesson lesson) {
        // Duplicate check
        if (member.hasBookingFor(lesson)) {
            System.out.println("  [ERROR] You already have a booking for this lesson.");
            return null;
        }
        // Capacity check
        if (lesson.isFull()) {
            System.out.println("  [ERROR] This lesson is fully booked (capacity: 4).");
            return null;
        }
        // Time-conflict check
        if (member.hasTimeConflict(lesson)) {
            System.out.println("  [ERROR] You already have a booking at this time slot.");
            return null;
        }

        Booking booking = new Booking(member, lesson);
        member.addBooking(booking);
        lesson.addMember(member);
        allBookings.add(booking);
        return booking;
    }

    /**
     * Changes an existing booking to a new lesson.
     * The booking ID is preserved; status is updated to "changed".
     */
    public boolean changeBooking(Booking booking, Lesson newLesson) {
        if (booking.getStatus().equals("cancelled") || booking.getStatus().equals("attended")) {
            System.out.println("  [ERROR] Cannot change a " + booking.getStatus() + " booking.");
            return false;
        }
        if (newLesson.isFull()) {
            System.out.println("  [ERROR] New lesson is fully booked.");
            return false;
        }
        Member member = booking.getMember();
        if (member.hasBookingFor(newLesson)) {
            System.out.println("  [ERROR] You already have a booking for the new lesson.");
            return false;
        }

        // Release old lesson slot
        booking.getLesson().removeMember(member);

        // Assign new lesson
        booking.setLesson(newLesson);
        booking.setStatus("changed");
        newLesson.addMember(member);
        return true;
    }

    /**
     * Cancels a booking. Releases the slot in the lesson.
     * The booking ID is retired (not reused).
     */
    public boolean cancelBooking(Booking booking) {
        if (booking.getStatus().equals("cancelled")) {
            System.out.println("  [ERROR] Booking is already cancelled.");
            return false;
        }
        if (booking.getStatus().equals("attended")) {
            System.out.println("  [ERROR] Cannot cancel an already-attended booking.");
            return false;
        }
        booking.getLesson().removeMember(booking.getMember());
        booking.setStatus("cancelled");
        return true;
    }

    /**
     * Marks a booking as attended and attaches a review.
     */
    public boolean attendLesson(Booking booking, String reviewText, int rating) {
        if (!booking.getStatus().equals("booked") && !booking.getStatus().equals("changed")) {
            System.out.println("  [ERROR] Booking status is '" + booking.getStatus() + "'. Cannot attend.");
            return false;
        }
        Review review = new Review(booking.getMember(), reviewText, rating);
        booking.addReview(review);
        booking.getLesson().getBookedMembers(); // ensure member still listed
        booking.setStatus("attended");
        return true;
    }

    // -----------------------------------------------------------------------
    // Reporting
    // -----------------------------------------------------------------------

    /**
     * Monthly lesson report: for each lesson in the given month that had at
     * least one attendee, print attendee count and average rating.
     */
    public void printMonthlyLessonReport(int month) {
        List<Lesson> lessons = timetable.getLessonsByMonth(month);
        System.out.println("\n========================================================");
        System.out.printf(" Monthly Lesson Report — Month %02d%n", month);
        System.out.println("========================================================");
        System.out.printf("%-6s %-9s %-9s %-12s %-8s %-8s%n",
                "LssnID", "Weekend", "Day", "Exercise", "Attendees", "Avg Rating");
        System.out.println("--------------------------------------------------------");

        for (Lesson l : lessons) {
            int attendees = countAttendees(l);
            double avgRating = averageRating(l);
            System.out.printf("L%03d   Wknd %-2d  %-9s %-12s %-9d %s%n",
                    l.getLessonId(), l.getWeekendNumber(), l.getDay(),
                    l.getExerciseType().getDisplayName(), attendees,
                    attendees == 0 ? "N/A" : String.format("%.2f", avgRating));
        }
        System.out.println("========================================================\n");
    }

    /**
     * Monthly champion report: totals income per exercise type in the given
     * month based on attended bookings, then prints a ranked list.
     */
    public void printMonthlyChampionReport(int month) {
        double[] incomes = new double[ExerciseType.values().length];

        for (Booking b : allBookings) {
            if (!b.getStatus().equals("attended")) continue;
            if (b.getLesson().getMonth() != month) continue;
            int idx = b.getLesson().getExerciseType().ordinal();
            incomes[idx] += b.getLesson().getPrice();
        }

        System.out.println("\n========================================================");
        System.out.printf(" Monthly Champion Report — Month %02d%n", month);
        System.out.println("========================================================");

        ExerciseType champion = null;
        double maxIncome = -1;
        for (ExerciseType t : ExerciseType.values()) {
            double inc = incomes[t.ordinal()];
            System.out.printf("  %-12s  £%.2f%n", t.getDisplayName(), inc);
            if (inc > maxIncome) { maxIncome = inc; champion = t; }
        }
        System.out.println("--------------------------------------------------------");
        if (champion != null && maxIncome > 0) {
            System.out.printf("  Champion: %s with £%.2f income%n", champion.getDisplayName(), maxIncome);
        } else {
            System.out.println("  No attended lessons in this month.");
        }
        System.out.println("========================================================\n");
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private int countAttendees(Lesson lesson) {
        int count = 0;
        for (Booking b : allBookings) {
            if (b.getLesson().equals(lesson) && b.getStatus().equals("attended")) count++;
        }
        return count;
    }

    private double averageRating(Lesson lesson) {
        int total = 0, count = 0;
        for (Booking b : allBookings) {
            if (b.getLesson().equals(lesson) && b.getStatus().equals("attended")) {
                for (Review r : b.getReviews()) {
                    total += r.getRating();
                    count++;
                }
            }
        }
        return count == 0 ? 0 : (double) total / count;
    }

    public Booking findBookingById(int id) {
        for (Booking b : allBookings) {
            if (b.getBookingId() == id) return b;
        }
        return null;
    }

    public List<Booking> getAllBookings() { return allBookings; }
}
