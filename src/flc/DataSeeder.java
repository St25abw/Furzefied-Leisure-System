package flc;

import java.util.List;

/**
 * Seeds the system with 10 pre-registered members and enough bookings/reviews
 * to satisfy the coursework requirement (10 members, 20+ reviews).
 *
 * Design pattern: used as a static utility (similar to a Builder helper).
 */
public class DataSeeder {

    public static void seed(BookingSystem system) {
        Timetable tt = system.getTimetable();
        List<Lesson> lessons = tt.getAllLessons();

        // --- 10 pre-registered members ---
        String[] names = {
            "Alice Johnson", "Bob Smith", "Carol White", "David Brown",
            "Emma Davis", "Frank Miller", "Grace Wilson", "Harry Moore",
            "Isla Taylor", "Jack Anderson"
        };
        Member[] members = new Member[names.length];
        for (int i = 0; i < names.length; i++) {
            members[i] = new Member(names[i]);
            system.registerMember(members[i]);
        }

        // Helper: book member into lesson by index in the all-lessons list
        // We pick specific lessons from the 48-lesson timetable.
        // lessons index: 0-5 = weekend1, 6-11 = weekend2, etc.

        // --- Weekend 1 (April) ---
        book(system, members[0], lessons.get(0));  // Alice → W1 Sat Morning Yoga
        book(system, members[1], lessons.get(0));  // Bob   → W1 Sat Morning Yoga
        book(system, members[2], lessons.get(1));  // Carol → W1 Sat Afternoon Zumba
        book(system, members[3], lessons.get(2));  // David → W1 Sat Evening Box Fit
        book(system, members[4], lessons.get(3));  // Emma  → W1 Sun Morning Aquacise
        book(system, members[5], lessons.get(3));  // Frank → W1 Sun Morning Aquacise
        book(system, members[6], lessons.get(4));  // Grace → W1 Sun Afternoon Body Blitz
        book(system, members[7], lessons.get(5));  // Harry → W1 Sun Evening Yoga
        book(system, members[8], lessons.get(0));  // Isla  → W1 Sat Morning Yoga
        book(system, members[9], lessons.get(1));  // Jack  → W1 Sat Afternoon Zumba

        // --- Weekend 2 (April) ---
        book(system, members[0], lessons.get(6));  // Alice → W2 Sat Morning Zumba
        book(system, members[1], lessons.get(7));  // Bob   → W2 Sat Afternoon Box Fit
        book(system, members[2], lessons.get(8));  // Carol → W2 Sat Evening Aquacise
        book(system, members[3], lessons.get(9));  // David → W2 Sun Morning Body Blitz
        book(system, members[4], lessons.get(10)); // Emma  → W2 Sun Afternoon Yoga
        book(system, members[5], lessons.get(11)); // Frank → W2 Sun Evening Zumba

        // --- Weekend 3 (April) ---
        book(system, members[6], lessons.get(12)); // Grace → W3 Sat Morning Aquacise
        book(system, members[7], lessons.get(13)); // Harry → W3 Sat Afternoon Body Blitz
        book(system, members[8], lessons.get(14)); // Isla  → W3 Sat Evening Yoga
        book(system, members[9], lessons.get(15)); // Jack  → W3 Sun Morning Zumba

        // --- Attend lessons and write reviews (20+ reviews) ---
        attend(system, members[0], lessons.get(0),  "Wonderful session, very calming.",      5);
        attend(system, members[1], lessons.get(0),  "Enjoyed the instructor's guidance.",     4);
        attend(system, members[8], lessons.get(0),  "Could be a bit longer.",                 3);
        attend(system, members[2], lessons.get(1),  "High energy and fun!",                   5);
        attend(system, members[9], lessons.get(1),  "Good music and pace.",                   4);
        attend(system, members[3], lessons.get(2),  "Tough workout, loved it.",               5);
        attend(system, members[4], lessons.get(3),  "Relaxing and refreshing.",               4);
        attend(system, members[5], lessons.get(3),  "Great pool environment.",                 3);
        attend(system, members[6], lessons.get(4),  "Intense but rewarding.",                  4);
        attend(system, members[7], lessons.get(5),  "Peaceful end to the weekend.",            5);
        attend(system, members[0], lessons.get(6),  "Loved the Zumba choreography.",           5);
        attend(system, members[1], lessons.get(7),  "Box Fit really challenges you.",          4);
        attend(system, members[2], lessons.get(8),  "Aquacise is underrated!",                 4);
        attend(system, members[3], lessons.get(9),  "Body Blitz was tough but great.",         5);
        attend(system, members[4], lessons.get(10), "Yoga on Sunday morning is perfect.",      5);
        attend(system, members[5], lessons.get(11), "Energetic Zumba session.",                4);
        attend(system, members[6], lessons.get(12), "Aquacise: therapeutic and fun.",          4);
        attend(system, members[7], lessons.get(13), "Body Blitz is my new favourite!",         5);
        attend(system, members[8], lessons.get(14), "Serene yoga with great instructor.",      5);
        attend(system, members[9], lessons.get(15), "Zumba gets better every week.",           4);
        // Extra reviews (bonus) - members with no time conflict for these slots
        attend(system, members[1], lessons.get(3),  "Aquacise felt refreshing.",               4);
        attend(system, members[0], lessons.get(2),  "Great way to end the weekend.",           4);
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    private static Booking book(BookingSystem sys, Member m, Lesson l) {
        Booking b = sys.bookLesson(m, l);
        if (b == null) {
            // silently skip seed conflicts (e.g. capacity already filled)
        }
        return b;
    }

    private static void attend(BookingSystem sys, Member m, Lesson l, String text, int rating) {
        // Find the booking for this member + lesson
        for (Booking b : sys.getAllBookings()) {
            if (b.getMember().equals(m) && b.getLesson().equals(l)) {
                sys.attendLesson(b, text, rating);
                return;
            }
        }
        // If booking doesn't exist, create and immediately attend
        Booking b = sys.bookLesson(m, l);
        if (b != null) sys.attendLesson(b, text, rating);
    }
}
