package flc;

import java.util.List;


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

        
        book(system, members[0], lessons.get(0));  
        book(system, members[1], lessons.get(0));  
        book(system, members[2], lessons.get(1));  
        book(system, members[3], lessons.get(2));  
        book(system, members[4], lessons.get(3));  
        book(system, members[5], lessons.get(3));  
        book(system, members[6], lessons.get(4));  
        book(system, members[7], lessons.get(5));  
        book(system, members[8], lessons.get(0));  
        book(system, members[9], lessons.get(1));  

        
        book(system, members[0], lessons.get(6));  
        book(system, members[1], lessons.get(7));  
        book(system, members[2], lessons.get(8));  
        book(system, members[3], lessons.get(9));  
        book(system, members[4], lessons.get(10)); 
        book(system, members[5], lessons.get(11)); 

        
        book(system, members[6], lessons.get(12)); 
        book(system, members[7], lessons.get(13)); 
        book(system, members[8], lessons.get(14)); 
        book(system, members[9], lessons.get(15)); 

        
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

   

    private static Booking book(BookingSystem sys, Member m, Lesson l) {
        Booking b = sys.bookLesson(m, l);
        if (b == null) {
            
        }
        return b;
    }

    private static void attend(BookingSystem sys, Member m, Lesson l, String text, int rating) {
        
        for (Booking b : sys.getAllBookings()) {
            if (b.getMember().equals(m) && b.getLesson().equals(l)) {
                sys.attendLesson(b, text, rating);
                return;
            }
        }
        
        Booking b = sys.bookLesson(m, l);
        if (b != null) sys.attendLesson(b, text, rating);
    }
}
