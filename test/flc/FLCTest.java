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

        
        yogaLesson  = tt.getAllLessons().get(0); 
        zumbaLesson = tt.getAllLessons().get(1); 
    }

   
    
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

        @Test
    public void testLessonCapacityEnforcement() {
        
        for (int i = 0; i < 4; i++) {
            Member m = new Member("TestMember" + i);
            system.registerMember(m);
            Booking b = system.bookLesson(m, yogaLesson);
            assertNotNull(b, "Booking " + (i + 1) + " should succeed (capacity not yet reached)");
        }
        assertTrue(yogaLesson.isFull(), "Lesson should be full after 4 bookings");

        
        Booking overflow = system.bookLesson(alice, yogaLesson);
        assertNull(overflow, "5th booking should fail — lesson is at capacity");
    }

    
    @Test
    public void testNoDuplicateBooking() {
        Booking first = system.bookLesson(alice, yogaLesson);
        assertNotNull(first, "First booking should succeed");

        Booking second = system.bookLesson(alice, yogaLesson);
        assertNull(second, "Duplicate booking for same lesson should be rejected");

       
        long count = system.getAllBookings().stream()
                .filter(b -> b.getMember().equals(alice) && b.getLesson().equals(yogaLesson))
                .count();
        assertEquals(1, count, "Exactly one booking should exist for alice in yogaLesson");
    }

   
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

        
        boolean secondAttend = system.attendLesson(booking, "Trying again", 3);
        assertFalse(secondAttend, "Attending an already-attended booking should fail");
        assertEquals(1, booking.getReviews().size(), "Review count should still be 1");
    }
}
