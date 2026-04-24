package flc;

import java.util.ArrayList;
import java.util.List;


public class Lesson {
    public static final int MAX_CAPACITY = 4;

    private static int lessonCounter = 1;

    private int lessonId;
    private ExerciseType exerciseType;
    private String day;       
    private String timeSlot;  
    private int weekendNumber; 
    private int month;        

   
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
