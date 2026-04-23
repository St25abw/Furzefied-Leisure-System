package flc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FLCTest {

    private BookingSystem system;
    private Member alice;
    private Member bob;
    private Lesson yogaLesson;
    private Lesson zumbaLesson;

    @BeforeEach
    public void setUp() {
        Timetable tt = Timetable.buildDefaultTimetable();
        system = new BookingSystem(tt);

        alice = new Member("Alice Test");
        bob   = new Member("Bob Test");
        system.registerMember(alice);
        system.registerMember(bob);

        // Use first two lessons from the timetable
        yogaLesson  = tt.getAllLessons().get(0); // W1 Sat Morning Yoga
        zumbaLesson = tt.getAllLessons().get(1); // W1 Sat Afternoon Zumba
    }

    // -----------------------------------------------------------------------
    // Test 1 – Member.hasBookingFor()
    // -----------------------------------------------------------------------

    /**
     * After booking a lesson, hasBookingFor() must return true for that lesson.
     * After cancelling, it should return false (cancelled bookings are excluded).
     */
    
    @Test
    public void testMemberHasBookingFor() {
        assertFalse(alice.hasBookingFor(yogaLesson),
                "Before booking, hasBookingFor should be false");

        Booking b = system.bookLesson(alice, yogaLesson);
        assertNotNull(b, "Booking should succeed");

        assertTrue(alice.hasBookingFor(yogaLesson),
                "After booking, hasBookingFor should be true");

        system.cancelBooking(b);
        assertFalse(alice.hasBookingFor(yogaLesson),
                "After cancellation, hasBookingFor should be false");
    }

    // -----------------------------------------------------------------------
    // Test 2 – Lesson capacity (Lesson.isFull())
    // -----------------------------------------------------------------------

    /**
     * A lesson accepts at most 4 members; the 5th booking should fail.
     */
    @Test
    public void testLessonCapacityEnforcement() {
        // Create 4 extra members and fill the lesson
        for (int i = 0; i < 4; i++) {
            Member m = new Member("TestMember" + i);
            system.registerMember(m);
            Booking b = system.bookLesson(m, yogaLesson);
            assertNotNull(b, "Booking " + (i + 1) + " should succeed (capacity not yet reached)");
        }
        assertTrue(yogaLesson.isFull(), "Lesson should be full after 4 bookings");

        // 5th attempt (alice) must fail
        Booking overflow = system.bookLesson(alice, yogaLesson);
        assertNull(overflow, "5th booking should fail — lesson is at capacity");
    }

    // -----------------------------------------------------------------------
    // Test 3 – BookingSystem.bookLesson() duplicate prevention
    // -----------------------------------------------------------------------

    /**
     * Booking the same lesson twice by the same member must be rejected.
     */
    @Test
    public void testNoDuplicateBooking() {
        Booking first = system.bookLesson(alice, yogaLesson);
        assertNotNull(first, "First booking should succeed");

        Booking second = system.bookLesson(alice, yogaLesson);
        assertNull(second, "Duplicate booking for same lesson should be rejected");

        // Confirm only one booking exists for alice in this lesson
        long count = system.getAllBookings().stream()
                .filter(b -> b.getMember().equals(alice) && b.getLesson().equals(yogaLesson))
                .count();
        assertEquals(1, count, "Exactly one booking should exist for alice in yogaLesson");
    }

    // -----------------------------------------------------------------------
    // Test 4 – BookingSystem.changeBooking()
    // -----------------------------------------------------------------------

    /**
     * Changing a booking to a new lesson should update the lesson reference,
     * set status to "changed", release the old lesson slot, and add to the new.
     */
    @Test
    public void testChangeBooking() {
        Booking booking = system.bookLesson(alice, yogaLesson);
        assertNotNull(booking);

        int oldSize = yogaLesson.getBookedMembers().size();
        boolean success = system.changeBooking(booking, zumbaLesson);

        assertTrue(success, "Change booking should succeed");
        assertEquals("changed", booking.getStatus(), "Status should be 'changed'");
        assertEquals(zumbaLesson, booking.getLesson(), "Booking lesson should now be zumba");
        assertFalse(yogaLesson.getBookedMembers().contains(alice),
                "Alice should be removed from old lesson");
        assertTrue(zumbaLesson.getBookedMembers().contains(alice),
                "Alice should be added to new lesson");
        assertEquals(oldSize - 1, yogaLesson.getBookedMembers().size(),
                "Old lesson member count should decrease by 1");
    }
    

  
    @Test
    public void testAttendLessonAndReview() {
        Booking booking = system.bookLesson(alice, yogaLesson);
        assertNotNull(booking);

        boolean attended = system.attendLesson(booking, "Great session!", 5);
        assertTrue(attended, "attendLesson should return true on first call");
        assertEquals("attended", booking.getStatus(), "Status should be 'attended'");

        List<Review> reviews = booking.getReviews();
        assertEquals(1, reviews.size(), "One review should be attached");
        assertEquals(5, reviews.get(0).getRating(), "Rating should be 5");
        assertEquals("Great session!", reviews.get(0).getReviewText());

        // Attending again should fail
        boolean secondAttend = system.attendLesson(booking, "Trying again", 3);
        assertFalse(secondAttend, "Attending an already-attended booking should fail");
        assertEquals(1, booking.getReviews().size(), "Review count should still be 1");
    }
}
