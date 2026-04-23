package flc;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single timetabled group exercise lesson.
 * Each lesson has a day (Saturday/Sunday), a time slot (Morning/Afternoon/Evening),
 * a weekend number (1-8), and an exercise type.
 * Capacity is fixed at 4 members.
 */
public class Lesson {
    public static final int MAX_CAPACITY = 4;

    private static int lessonCounter = 1;

    private int lessonId;
    private ExerciseType exerciseType;
    private String day;       // "Saturday" or "Sunday"
    private String timeSlot;  // "Morning", "Afternoon", "Evening"
    private int weekendNumber; // 1-8
    private int month;        // e.g., 4 for April, 5 for May

    // Members who have been booked (non-cancelled) into this lesson
    private List<Member> bookedMembers;

    public Lesson(ExerciseType exerciseType, String day, String timeSlot, int weekendNumber, int month) {
        this.lessonId      = lessonCounter++;
        this.exerciseType  = exerciseType;
        this.day           = day;
        this.timeSlot      = timeSlot;
        this.weekendNumber = weekendNumber;
        this.month         = month;
        this.bookedMembers = new ArrayList<>();
    }

    // --- Getters ---
    public int getLessonId()           { return lessonId; }
    public ExerciseType getExerciseType() { return exerciseType; }
    public String getDay()             { return day; }
    public String getTimeSlot()        { return timeSlot; }
    public int getWeekendNumber()      { return weekendNumber; }
    public int getMonth()              { return month; }
    public List<Member> getBookedMembers() { return bookedMembers; }

    public double getPrice() { return exerciseType.getPrice(); }

    public boolean isFull() { return bookedMembers.size() >= MAX_CAPACITY; }
    public int availableSpaces() { return MAX_CAPACITY - bookedMembers.size(); }

    public void addMember(Member m)    { if (!bookedMembers.contains(m)) bookedMembers.add(m); }
    public void removeMember(Member m) { bookedMembers.remove(m); }

    @Override
    public String toString() {
        return String.format("[L%03d] Weekend %d | %-9s | %-9s | %-10s | £%.2f | Spaces: %d/%d",
                lessonId, weekendNumber, day, timeSlot,
                exerciseType.getDisplayName(), getPrice(),
                bookedMembers.size(), MAX_CAPACITY);
    }
}
