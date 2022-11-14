package iteration1.src.human;

import iteration1.src.RegistrationData;
import iteration1.src.Transcript;
import iteration1.src.course.*;
import iteration1.src.input_output.Logger;

import java.util.ArrayList;
import java.util.List;

public class Student extends Human{

    private String studentID;
    private Grade grade;
    private Advisor advisor;
    private Transcript transcript;
    private List<Section> enrolledCourseSections = new ArrayList<>();


    public Student(String firstName, String middleName, String lastName,String studentID,Grade grade ,Advisor advisor,List<CourseRecord> transcript){
        super(firstName, middleName, lastName);

        this.studentID = studentID;
        this.grade = grade;
        this.advisor = advisor;
        this.transcript = new Transcript(transcript);
    }

    public Student(String firstName, String lastName,String studentID, Grade grade ,Advisor advisor, List<CourseRecord> transcript){
        super(firstName, lastName);

        this.grade = grade;
        this.advisor = advisor;
        this.transcript = new Transcript(transcript);
    }

    public Boolean checkIfPrerequisitesArePassed(Course course){
        return transcript.checkIfPrerequisitesArePassed(course);
    }

    public void enrollCourseSections(List<Section> sections, Season season){

        for(Section s: sections){
            s.addToStudentList(this);

            transcript.addCourseRecord(s.getCourse(), LetterGrade.NOT_GRADED, season, null, this.getGrade(), false);
        }
    }

    public void register(RegistrationData data){

        System.out.println();
        System.out.println("Registration process of " + this.getFullName() + " started");
        System.out.println();

        // checks

        // sections to be removed and added from the enrolledsections due to full quota

        for(Section sec: enrolledCourseSections) {

            Course c = sec.getCourse();

            // checks
            // if one of the requirement is not met then close the program
            if (!c.canStudentTakeCourse(this)) {
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
                        + " and " + sec2.getCourse().getCode() + " on " + Section.CLASS_DAYS[collisionDay] + " at " + Section.CLASS_HOURS[collisionHour]);

                // clear all the enrolled courses in the process
                this.enrolledCourseSections.clear();

                System.out.println();

                System.out.println("Registration process of " + this.getFullName() + " ended");

                System.out.println();
                System.out.println();
                return;
            }
        }

        int year = data.getYear();
        Season season = data.getSeason();


        if(collisions.size() == 0){

            enrollCourseSections(enrolledCourseSections, season);

            System.out.println();

            String schedule = generateWeeklySchedule();
            System.out.println(schedule);
        }

        System.out.println("Registration process of " + this.getFullName() + " ended");

        System.out.println();
        System.out.println();
    }

    public String generateWeeklySchedule(){

        String program = this.getFullName() + "'s Weekly Schedule\n";

        if(this.enrolledCourseSections.size() == 0){
            program = "The student " + this.getFullName() + " doesn't have any enrolled courses";
        }

        List<Section[]> schedule = Section.combineSchedules(this.enrolledCourseSections);

        for(int i = 0; i<schedule.size(); i++){

            program += Section.CLASS_DAYS[i] + ": ";

            Section[] day = schedule.get(i);

            for(int j = 0; j<schedule.get(0).length; j++){

                Section sec = day[j];

                if(sec == null)
                    continue;


                program += sec.getCourse().getCode();

                program += "(" + Section.CLASS_HOURS[j%8] + ") ";

            }
            program += "\n";
        }

        return program;
    }

    public String getStudentID() {
        return studentID;
    }

    public Grade getGrade() {
        return grade;
    }

    public Advisor getAdvisor() {
        return advisor;
    }

    public List<Section> getEnrolledCourses() {
        return enrolledCourseSections;
    }

    public int getCompletedCredits(){
        return transcript.getCompletedCredits();
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }

    public void addToRegistrationList(Section section){
        if(section.isSectionFull()){

            Logger.log("This section of " + section.getCourse().getCode() + " is already full");
            return;
        }

        this.enrolledCourseSections.add(section);
    }

    public Transcript getTranscript(){
        return transcript;
    }

}
