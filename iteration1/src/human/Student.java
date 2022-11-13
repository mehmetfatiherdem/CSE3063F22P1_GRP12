package iteration1.src.human;

import iteration1.src.RegistrationData;
import iteration1.src.Transcript;
import iteration1.src.course.*;

import java.util.ArrayList;
import java.util.List;

public class Student extends Human{

    private String studentID;
    private Level level;
    private Advisor advisor;
    private Transcript transcript;
    private List<Section> enrolledCourseSections = new ArrayList<>();

    private final String[] classDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    private final String[] classHours = {"8.30-9.20", "9.30-10.20", "10.30-11.20", "11.30-12.20",
            "13.00-13.50", "14.00-14.50", "15.00-15.50", "16.00-16.50", };

    public Student(String firstName, String middleName, String lastName){
        super(firstName, middleName, lastName);
    }

    public Student(String firstName, String lastName){
        super(firstName, lastName);
    }

    public void enrollCourseSections(List<Section> sections, int year, Season season){

        for(Section s: sections){
            s.getCourse().addToStudentList(this);


            // TODO: uncomment and fix this when the transcript PR is merged
            // this.transcript.takenCourseRecords.add(new CourseRecord(s.course, LetterGrade.NOT_GRADED, null, data.season, data.year, false))
        }

    }

    public void register(RegistrationData data){

        // checks

        for(Section sec: enrolledCourseSections){

            Course c = sec.getCourse();

            // checks
            // if one of the requirement is not met then close the program
             if(!c.canStudentTakeCourse(this)){
                 return;
             }

        }

        // check for collisions between the course sections the student wants to take
        var collisions = Section.checkForCollisions(enrolledCourseSections);

        for(var c : collisions){
            Section sec1 = c.GetKey();
            Section sec2 = c.GetValue();

            var collisionBetween = sec1.getCollisionsWith(sec2);

            for(var d : collisionBetween){
                int collisionDay = d.GetKey();
                int collisionHour = d.GetValue();

                System.out.println("There is a collision between " + sec1.getCourse().getCode()
                        + " and " + sec2.getCourse().getCode() + " on " + classDays[collisionDay] + " at " + classHours[collisionHour]);

                // clear all the enrolled courses in the process
                this.enrolledCourseSections.clear();
                return;
            }
        }

        int year = data.getYear();
        Season season = data.getSeason();

        if(collisions.size() == 0){

            enrollCourseSections(enrolledCourseSections, year, season);

            String schedule = generateWeeklySchedule();
            System.out.println(schedule);
        }

    }

    public String generateWeeklySchedule(){

        String program = this.getFullName() + "'s Weekly Schedule\n";

        if(this.enrolledCourseSections.size() == 0){
            program = "The student " + this.getFullName() + " doesn't have any enrolled courses";
        }

        List<Section[]> schedule = Section.combineSchedules(this.enrolledCourseSections);

        for(int i = 0; i<schedule.size(); i++){

            program += classDays[i] + ": ";

            Section[] day = schedule.get(i);

            for(int j = 0; j<schedule.get(0).length; j++){

                Section sec = day[j];

                if(sec == null)
                    continue;


                program += sec.getCourse().getCode();

                program += "(" + classHours[j%8] + ") ";

            }
            program += "\n";
        }

        return program;
    }

    public String getStudentID() {
        return studentID;
    }

    //TODO: We could have logic checks here
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Advisor getAdvisor() {
        return advisor;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }

    public List<Section> getEnrolledCourses() {
        return enrolledCourseSections;
    }

    public void addToEnrolledCourseSections(Section section){
        this.enrolledCourseSections.add(section);
    }

}
