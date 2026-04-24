package flc;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Main {

    private static BookingSystem system;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        Timetable timetable = Timetable.buildDefaultTimetable();
        system = new BookingSystem(timetable);
        DataSeeder.seed(system);

        System.out.println("============================================================");
        System.out.println("  Welcome to Furzefield Leisure Centre Booking System");
        System.out.println("============================================================");
        printMembers();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> bookLesson();
                case 2 -> changeOrCancelBooking();
                case 3 -> attendLesson();
                case 4 -> monthlyLessonReport();
                case 5 -> monthlyChampionReport();
                case 0 -> { running = false; System.out.println("\n  Goodbye!"); }
                default -> System.out.println("  [ERROR] Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    

    private static void printMainMenu() {
        System.out.println("\n------------------------------------------------------------");
        System.out.println("  MAIN MENU");
        System.out.println("------------------------------------------------------------");
        System.out.println("  1. Book a group exercise lesson");
        System.out.println("  2. Change / Cancel a booking");
        System.out.println("  3. Attend a lesson");
        System.out.println("  4. Monthly lesson report");
        System.out.println("  5. Monthly champion lesson type report");
        System.out.println("  0. Exit");
        System.out.println("------------------------------------------------------------");
    }

   

    private static void bookLesson() {
        System.out.println("\n--- Book a Lesson ---");
        Member member = selectMember();
        if (member == null) return;

        System.out.println("  View timetable by:");
        System.out.println("  1. Day (Saturday/Sunday)");
        System.out.println("  2. Exercise type");
        int how = readInt("  Choose: ");

        List<Lesson> options;
        if (how == 1) {
            System.out.print("  Enter day (Saturday/Sunday): ");
            String day = scanner.nextLine().trim();
            options = system.getLessonsByDay(day);
            if (options.isEmpty()) { System.out.println("  No lessons found for: " + day); return; }
        } else if (how == 2) {
            ExerciseType type = selectExerciseType();
            if (type == null) return;
            options = system.getLessonsByType(type);
            if (options.isEmpty()) { System.out.println("  No lessons found."); return; }
        } else {
            System.out.println("  Invalid option.");
            return;
        }

        printLessons(options);
        int lessonId = readInt("  Enter Lesson ID to book (0 to cancel): ");
        if (lessonId == 0) return;

        Lesson lesson = system.getTimetable().findById(lessonId);
        if (lesson == null) { System.out.println("  [ERROR] Lesson not found."); return; }

        Booking booking = system.bookLesson(member, lesson);
        if (booking != null) {
            System.out.println("  [SUCCESS] Booking confirmed! Booking ID: " + booking.getBookingId());
        }
    }

    

    private static void changeOrCancelBooking() {
        System.out.println("\n--- Change / Cancel a Booking ---");
        Member member = selectMember();
        if (member == null) return;

        List<Booking> active = member.getActiveBookings();
        if (active.isEmpty()) { System.out.println("  No active bookings for " + member.getName()); return; }

        System.out.println("  Active bookings:");
        for (Booking b : active) System.out.println("    " + b);

        int bookingId = readInt("  Enter Booking ID to change/cancel (0 to go back): ");
        if (bookingId == 0) return;

        Booking booking = system.findBookingById(bookingId);
        if (booking == null || !booking.getMember().equals(member)) {
            System.out.println("  [ERROR] Booking not found or does not belong to this member.");
            return;
        }

        System.out.println("  1. Change to a new lesson");
        System.out.println("  2. Cancel this booking");
        int action = readInt("  Choose: ");

        if (action == 1) {
            System.out.println("  Select new lesson by:");
            System.out.println("  1. Day    2. Exercise type");
            int how = readInt("  Choose: ");
            List<Lesson> options;
            if (how == 1) {
                System.out.print("  Enter day: ");
                String day = scanner.nextLine().trim();
                options = system.getLessonsByDay(day);
            } else {
                ExerciseType type = selectExerciseType();
                if (type == null) return;
                options = system.getLessonsByType(type);
            }
            printLessons(options);
            int newId = readInt("  Enter new Lesson ID (0 to cancel): ");
            if (newId == 0) return;
            Lesson newLesson = system.getTimetable().findById(newId);
            if (newLesson == null) { System.out.println("  [ERROR] Lesson not found."); return; }
            if (system.changeBooking(booking, newLesson)) {
                System.out.println("  [SUCCESS] Booking #" + bookingId + " changed to: " + newLesson);
            }
        } else if (action == 2) {
            if (system.cancelBooking(booking)) {
                System.out.println("  [SUCCESS] Booking #" + bookingId + " cancelled.");
            }
        } else {
            System.out.println("  Invalid option.");
        }
    }

    

    private static void attendLesson() {
        System.out.println("\n--- Attend a Lesson ---");
        Member member = selectMember();
        if (member == null) return;

        List<Booking> active = member.getActiveBookings();
        if (active.isEmpty()) { System.out.println("  No active bookings for " + member.getName()); return; }

        System.out.println("  Active bookings:");
        for (Booking b : active) System.out.println("    " + b);

        int bookingId = readInt("  Enter Booking ID to attend (0 to go back): ");
        if (bookingId == 0) return;

        Booking booking = system.findBookingById(bookingId);
        if (booking == null || !booking.getMember().equals(member)) {
            System.out.println("  [ERROR] Booking not found or does not belong to this member.");
            return;
        }

        System.out.print("  Write your review (a few words/sentences): ");
        String reviewText = scanner.nextLine().trim();
        if (reviewText.isEmpty()) reviewText = "No comment.";

        int rating = 0;
        while (rating < 1 || rating > 5) {
            rating = readInt("  Rate the lesson (1=Very dissatisfied … 5=Very satisfied): ");
            if (rating < 1 || rating > 5) System.out.println("  [ERROR] Rating must be between 1 and 5.");
        }

        if (system.attendLesson(booking, reviewText, rating)) {
            System.out.println("  [SUCCESS] Lesson attended. Thank you for your review!");
        }
    }

    

    private static void monthlyLessonReport() {
        int month = readInt("  Enter month number (e.g., 4 for April, 5 for May): ");
        system.printMonthlyLessonReport(month);
    }

    private static void monthlyChampionReport() {
        int month = readInt("  Enter month number (e.g., 4 for April, 5 for May): ");
        system.printMonthlyChampionReport(month);
    }

   

    private static void printMembers() {
        System.out.println("\n  Pre-registered members:");
        for (Member m : system.getAllMembers()) {
            System.out.printf("    [%d] %s%n", m.getMemberId(), m.getName());
        }
    }

    private static Member selectMember() {
        int id = readInt("  Enter your Member ID (0 to go back): ");
        if (id == 0) return null;
        Member m = system.findMemberById(id);
        if (m == null) System.out.println("  [ERROR] Member not found.");
        return m;
    }

    private static ExerciseType selectExerciseType() {
        System.out.println("  Exercise types:");
        ExerciseType[] types = ExerciseType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("  %d. %s (£%.2f)%n", i + 1, types[i].getDisplayName(), types[i].getPrice());
        }
        int idx = readInt("  Choose type: ");
        if (idx < 1 || idx > types.length) { System.out.println("  Invalid type."); return null; }
        return types[idx - 1];
    }

    private static void printLessons(List<Lesson> lessons) {
        System.out.printf("  %-6s %-8s %-9s %-9s %-12s %-7s %-10s%n",
                "ID", "Wknd", "Day", "Time", "Exercise", "Price", "Spaces");
        System.out.println("  " + "-".repeat(65));
        for (Lesson l : lessons) {
            System.out.printf("  L%03d   %-6d %-9s %-9s %-12s £%-6.2f %d/%d%n",
                    l.getLessonId(), l.getWeekendNumber(), l.getDay(), l.getTimeSlot(),
                    l.getExerciseType().getDisplayName(), l.getPrice(),
                    l.getBookedMembers().size(), Lesson.MAX_CAPACITY);
        }
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            int val = scanner.nextInt();
            scanner.nextLine(); // consume newline
            return val;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
}
