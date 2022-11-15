package iteration1.src;

import iteration1.src.course.Course;
import iteration1.src.course.MandatoryCourse;
import iteration1.src.course.Season;
import iteration1.src.course.Section;
import iteration1.src.human.*;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private static Department instance;

    private final String code = "CSE";
    private Season currentSeason;
    private List<Course> courses;
    private List<Student> students;
    private List<Lecturer> lecturers;
    private List<Assistant> assistants;
    private List<Advisor> advisors;
    private boolean initialized;

    private Department() {
        initialized = false;
    }

    public static Department getInstance() {
        if (instance == null)
        {
            instance = new Department();
        }
        return instance;
    }

    public void initialize(Season currentSeason,List<Course> courses,
                           List<Lecturer> lecturers,List<Assistant> assistants,List<Advisor> advisors,List<Student> students){
        if(initialized)
            return;

        if(courses == null || lecturers == null || assistants == null || advisors == null || students == null)
            return;

        this.currentSeason = currentSeason;
        this.courses = courses;
        this.lecturers = lecturers;
        this.assistants = assistants;
        this.advisors = advisors;
        this.students = students;

        initialized = true;

        assignFacultyMembersToCourses();
        generateWeeklyScheduleForAllCourses();
    }

    // Getters
    public String getCode() {
        return code;
    }
    public Season getCurrentSeason(){
        return currentSeason;
    }
    public List<Course> getCourses() {
        return courses;
    }
    public List<Student> getStudents() {
        return students;
    }


    private void assignFacultyMembersToCourses(){
        //TODO:write this
    }

    private void generateWeeklyScheduleForAllCourses(){

        float happyPathChance = 0.72f;

        if(Helper.generateRandomFloat() <= happyPathChance){
            int schedulePos = 0;
            float breakChance = 0.23f;

            for(Course c : courses){
                int theoreticalHours = c.getTheoreticalHours();
                int appliedHours = c.getAppliedHours();

                long schedule = Section.getScheduleAtPosition(schedulePos,theoreticalHours);
                c.addCourseSection(schedule);

                schedulePos += theoreticalHours;
                if(schedulePos > 39)
                    schedulePos = 0;

                if(Helper.generateRandomFloat() <= breakChance){
                    schedulePos++;
                }

                if(appliedHours == 0)
                    continue;

                schedule = Section.getScheduleAtPosition(schedulePos,appliedHours);
                c.addLabSection(schedule);

                schedulePos += appliedHours;
                if(schedulePos > 39)
                    schedulePos = 0;

                if(Helper.generateRandomFloat() <= breakChance){
                    schedulePos++;
                }
            }
        } else {

            for(Course c : courses) {
                int theoreticalHours = c.getTheoreticalHours();
                int appliedHours = c.getAppliedHours();

                int[] randomHours = Helper.generateDistinctClassHours(theoreticalHours);
                long schedule = Section.getScheduleAtRandomPositions(randomHours);
                c.addCourseSection(schedule);

                if(appliedHours == 0)
                    continue;

                randomHours = Helper.generateDistinctClassHours(appliedHours);
                schedule = Section.getScheduleAtRandomPositions(randomHours);
                c.addLabSection(schedule);
            }
        }

        //TODO:Complete better collisionless scheduling algorithm later
        /*
        int semester = 0;

        List<Course> scheduledCourses = new ArrayList<>();

        for(semester = 0; semester < 8; semester++){
            int grade = semester/2;
            int season = semester % 2;

            long currentSchedule = 0;

            List<Course> mandatoryCoursesForSemester = new ArrayList<>();

            for(Course c : courses){
                if(c instanceof MandatoryCourse && c.getFirstYearToTake().getValue() == grade
                        && c.getFirstSeasonToTake().getValue() == season) {
                    mandatoryCoursesForSemester.add(c);
                }
            }

            //theoretical hours
            //divide them into groups of 2

            //First step
            //divide into groups of 2
            //for each group, generate a random value between 0 and 39
            //Dump these values into a list of long

            for(Course c : mandatoryCoursesForSemester){
                long courseSchedule = 0;
                long labSchedule = 0;

                int theoreticalHours = c.getTheoreticalHours();
                int appliedHours = c.getAppliedHours();
                int noOfTheoreticalGroups = theoreticalHours / 2;
                int noOfAppliedGroups = appliedHours / 2;

                for(int i = 0; i < noOfTheoreticalGroups; i++){

                }

                for(int i = 0; i < noOfAppliedGroups; i++){

                }

            }

            //while(collision)
            //check for collisions one by one
            //in the instance of a collision,move one of the colliders back or forth by collision hour

            scheduledCourses.addAll(mandatoryCoursesForSemester);
        }

        */
    }
}
