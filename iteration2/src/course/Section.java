package iteration2.src.course;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import iteration2.src.data_structures.Tuple;
import iteration2.src.human.FacultyMember;
import iteration2.src.human.Student;

public abstract class Section {

    //Number of class hours in a week
    public static final int NO_OF_WEEKLY_CLASS_HOURS = 56;
    public static final String[] CLASS_DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    public static final String[] CLASS_HOURS = {"8.30-9.20", "9.30-10.20", "10.30-11.20", "11.30-12.20",
            "13.00-13.50", "14.00-14.50", "15.00-15.50", "16.00-16.50", };

    /*
     * Bitmask for the schedule of this section, there are 8 classes per day and 7 days in a week,
     * so each bit represents the class hour that corresponds to that bit's position in the bitmask.
     * For example 17th bit would be the first class hour on Wednesday. 1 means there's a section at that classhour, and 0 means there is not
     */

    protected Course course;
    protected long classSchedule;
    protected FacultyMember instructor;
    protected List<Student> studentList = new ArrayList<>();

    protected Section(Course course, long classSchedule, FacultyMember instructor) {
        this.course = course;
        this.classSchedule = classSchedule;
        this.instructor = instructor;
    }

    //Returns each collision between two sections by their collision days as the key, and their collision hours as the value in the Tuple
    public List<Tuple<Integer, Integer>> getCollisionsWith(Section other) {
        List<Tuple<Integer, Integer>> collisions = new ArrayList<>();

        //Gets the bits that are set in both schedules(collision hours)
        long collisionDetector = classSchedule & other.classSchedule;

        //The callback for calculating the day and hour of a collision and adding it to the list
        Consumer<Integer> collisionCallback = (Integer i) -> collisions.add(new Tuple<Integer, Integer>(i / 8, i % 8));

        //Calls the collisionCallback for each set bit(1) in the collisionDetector with their positions from left to right
        traverseBits(collisionDetector, collisionCallback);

        return collisions;
    }

    //Returns all the colliding sections, the key and value in the Tuple represent two colliding sections, note that a section may collide with more than one section
    public static List<Tuple<Section, Section>> checkForCollisions(List<Section> classes) {

        //A list of all the sections that are combined, this helps with finding which two (or more) sections cause a collision 
        List<Section> combinedSections = new ArrayList<>();
        List<Tuple<Section, Section>> collisions = new ArrayList<>();

        //The bitmask representation of the combined schedule
        long combinedSchedule = 0L;

        for (Section sec : classes) {
            if (checkCollisionBetween(combinedSchedule, sec.classSchedule)) {

                //If there's a collision between a schedule and the combined bitmask, then check all the combined sections to find the collision(s) between sections
                for (Section s : combinedSections) {
                    if (checkCollisionBetween(s.classSchedule, sec.classSchedule)) {
                        collisions.add(new Tuple<Section, Section>(sec, s));
                    }
                }
            }

            //Adds the schedule to the combinedSchedule
            combinedSchedule |= sec.classSchedule;
            combinedSections.add(sec);
        }

        return collisions;
    }

    /*
     * Combines the schedules of the sections given and returns a list of Section arrays, each array represents a day starting from Monday,
     * and each entry in an array represents the corresponding classhour at that day. For example combineSchedules(sections).get(3)[5] would 
     * return the section on Thursday at 6th class hour. Note that a null section means that there's no class at that hour.
     * The arrays are of length 8 because there are 8 class hours in a day. 
     * Warning : The caller should make sure that there's no collision between the sections before calling this method or the returning schedule
     * would miss the first sections of each collision (The latter sections in the array would override the first ones)
     */
    public static List<Section[]> combineSchedules(List<Section> sections) {
        List<Section[]> schedule = new ArrayList<>();

        //Initialize the list and the arrays
        for (int i = 0; i < 7; i++) {
            Section[] day = new Section[8];
            schedule.add(day);
        }

        for (Section sec : sections) {

            //The callback for calculating the position of a class hour in the bitmask and assigning it in the corresponding position in the schedule
            Consumer<Integer> combineCallback = (Integer i) -> schedule.get(i / 8)[i % 8] = sec;

            //Calls the combineCallback for each set bit(1) in the class schedule of each section in the sections list
            traverseBits(sec.classSchedule, combineCallback);
        }

        return schedule;
    }

    // Getters
    public Course getCourse() {
        return course;
    }
    public long getClassSchedule() {
        return classSchedule;
    }
    public List<Student> getStudentList() {
        return studentList;
    }

    //Setters
    public void setCourse(Course course) {
        this.course = course;
    }

    //Checks if there's any collision between two schedules
    private static boolean checkCollisionBetween(long sch1, long sch2) {
        return (sch1 & sch2) != 0L;
    }

    //Finds the positions of set bits(1's) in the given bitmask and calls callback with those bit's positions from left
    private static void traverseBits(long bitmask, Consumer<Integer> setBitCallback) {
        for (int i = 0; i < NO_OF_WEEKLY_CLASS_HOURS; i++) {

            //If the leftmost bit is 1
            if ((bitmask & 1L) == 1L) {
                setBitCallback.accept(i);
            }
            
            //Right shift the bitmask and assign it to bitmask again
            bitmask >>= 1L;

            if (bitmask == 0L)
                return;
        }
    }

    public void addToStudentList(Student student) {
        this.studentList.add(student);
    }

    public boolean isSectionFull(){
        return course.getQuota() <= studentList.size();
    }
}
