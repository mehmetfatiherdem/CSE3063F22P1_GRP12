package iteration1.src.course;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import iteration1.src.data_structures.Tuple;
import iteration1.src.human.FacultyMember;

public abstract class Section {

    public static final int NO_OF_WEEKLY_CLASS_HOURS = 56;
    protected Course course;
    protected long classSchedule;
    protected FacultyMember instructor;

    protected Section(Course course, long classSchedule, FacultyMember instructor) {
        this.course = course;
        this.classSchedule = classSchedule;
        this.instructor = instructor;
    }

    public List<Tuple<Integer,Integer>> getCollisionsWith(Section other){
        List<Tuple<Integer,Integer>> collisions = new ArrayList<>();

        long collisionDetector = classSchedule & other.classSchedule;
        Consumer<Integer> collisionCallback = (Integer i) -> collisions.add(new Tuple<Integer,Integer>(i / 8, i % 8));

        TraverseBits(collisionDetector, collisionCallback);

        return collisions;
    }

    public static List<Tuple<Section, Section>> checkForCollisions(List<Section> classes) {
        List<Section> combinedSections = new ArrayList<>();
        List<Tuple<Section, Section>> collisions = new ArrayList<>();

        long combinedSchedule = 0L;

        for (Section sec : classes) {
            if (checkCollisionBetween(combinedSchedule,sec.classSchedule)){
                for (Section s : combinedSections){
                    if (checkCollisionBetween(s.classSchedule,sec.classSchedule)){
                        collisions.add(new Tuple<Section, Section>(sec, s));
                    }
                }
            }

            combinedSchedule |= sec.classSchedule;
            combinedSections.add(sec);
        }

        return collisions;
    }

    public static List<Section[]> combineSchedules(List<Section> sections) {
        if (checkForCollisions(sections).size() > 0)
            return null;

        List<Section[]> schedule = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Section[] day = new Section[8];
            schedule.add(day);
        }

        for (Section sec : sections) {
            long schTemp = sec.classSchedule;
            Consumer<Integer> combineCallback = (Integer i) -> schedule.get(i / 8)[i % 8] = sec;
            TraverseBits(schTemp,combineCallback);
        }

        return schedule;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public long getClassSchedule() {
        return classSchedule;
    }

    public void setClassSchedule(long classHours) {
        this.classSchedule = classHours;
    }

    private static boolean checkCollisionBetween(long sch1, long sch2){
        return (sch1 & sch2) != 0L;
    }

    private static void TraverseBits(long bitmask, Consumer<Integer> setBitCallback){
        for (int i = 0; i < NO_OF_WEEKLY_CLASS_HOURS; i++) {
            if ((bitmask & 1L) == 1L) {
                setBitCallback.accept(i);
            }

            bitmask >>= 1L;

            if (bitmask == 0L)
                return;
        }
    }
}
