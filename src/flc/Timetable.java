package flc;

import java.util.ArrayList;
import java.util.List;


public class Timetable {
    private List<Lesson> lessons;

    public Timetable() {
        lessons = new ArrayList<>();
    }

    public void addLesson(Lesson l) { lessons.add(l); }

    public List<Lesson> getAllLessons() { return lessons; }

   
    public List<Lesson> getLessonsByDay(String day) {
        List<Lesson> result = new ArrayList<>();
        for (Lesson l : lessons) {
            if (l.getDay().equalsIgnoreCase(day)) result.add(l);
        }
        return result;
    }

   
    public List<Lesson> getLessonsByType(ExerciseType type) {
        List<Lesson> result = new ArrayList<>();
        for (Lesson l : lessons) {
            if (l.getExerciseType() == type) result.add(l);
        }
        return result;
    }

    
    public List<Lesson> getLessonsByMonth(int month) {
        List<Lesson> result = new ArrayList<>();
        for (Lesson l : lessons) {
            if (l.getMonth() == month) result.add(l);
        }
        return result;
    }

    
    public Lesson findById(int id) {
        for (Lesson l : lessons) {
            if (l.getLessonId() == id) return l;
        }
        return null;
    }

    
    public static Timetable buildDefaultTimetable() {
        Timetable tt = new Timetable();

      

        Object[][] schedule = {
          
            {1, 4, ExerciseType.YOGA,      ExerciseType.ZUMBA,      ExerciseType.BOX_FIT,    ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ, ExerciseType.YOGA},
            {2, 4, ExerciseType.ZUMBA,     ExerciseType.BOX_FIT,    ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ, ExerciseType.YOGA,       ExerciseType.ZUMBA},
            {3, 4, ExerciseType.AQUACISE,  ExerciseType.BODY_BLITZ, ExerciseType.YOGA,       ExerciseType.ZUMBA,      ExerciseType.BOX_FIT,    ExerciseType.AQUACISE},
            {4, 4, ExerciseType.BOX_FIT,   ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ, ExerciseType.YOGA,       ExerciseType.ZUMBA,      ExerciseType.BOX_FIT},
            {5, 5, ExerciseType.BODY_BLITZ,ExerciseType.YOGA,       ExerciseType.ZUMBA,      ExerciseType.BOX_FIT,    ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ},
            {6, 5, ExerciseType.YOGA,      ExerciseType.BOX_FIT,    ExerciseType.AQUACISE,   ExerciseType.ZUMBA,      ExerciseType.BODY_BLITZ, ExerciseType.YOGA},
            {7, 5, ExerciseType.ZUMBA,     ExerciseType.AQUACISE,   ExerciseType.BODY_BLITZ, ExerciseType.YOGA,       ExerciseType.BOX_FIT,    ExerciseType.ZUMBA},
            {8, 5, ExerciseType.BOX_FIT,   ExerciseType.BODY_BLITZ, ExerciseType.YOGA,       ExerciseType.AQUACISE,   ExerciseType.ZUMBA,      ExerciseType.AQUACISE},
        };

        String[] days  = {"Saturday","Saturday","Saturday","Sunday","Sunday","Sunday"};
        String[] slots = {"Morning","Afternoon","Evening","Morning","Afternoon","Evening"};

        for (Object[] row : schedule) {
            int weekend = (int) row[0];
            int month   = (int) row[1];
            for (int i = 0; i < 6; i++) {
                ExerciseType type = (ExerciseType) row[2 + i];
                tt.addLesson(new Lesson(type, days[i], slots[i], weekend, month));
            }
        }
        return tt;
    }
}
