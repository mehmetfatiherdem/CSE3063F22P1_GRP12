package iteration2.src.human;

import iteration2.src.Department;
import iteration2.src.Transcript;
import iteration2.src.course.*;
import iteration2.src.input_output.Logger;

import java.util.ArrayList;
import java.util.List;

public class Student extends Human{

    private String studentID;
    private Grade grade;
    private Advisor advisor;
    private Transcript transcript;
    private List<Section> enrolledSections = new ArrayList<>();


    public Student(String firstName, String middleName, String lastName,String studentID,Grade grade ,Advisor advisor,List<CourseRecord> transcript){
        super(firstName, middleName, lastName);

        this.studentID = studentID;
        this.grade = grade;
        this.advisor = advisor;
        this.transcript = new Transcript(transcript);
    }

    public Student(String firstName, String lastName,String studentID, Grade grade ,Advisor advisor, List<CourseRecord> transcript){
        super(firstName, lastName);

        this.studentID = studentID;
        this.grade = grade;
        this.advisor = advisor;
        this.transcript = new Transcript(transcript);
    }

    public Boolean checkIfPrerequisitesArePassed(Course course){
        return transcript.checkIfPrerequisitesArePassed(course);
    }

    public Boolean didStudentPass(Course course) {
        return transcript.didStudentPass(course);
    }

    public void enrollCourseSections(List<Section> sections, Season season){

        for(Section s: sections){
            s.addToStudentList(this);

            transcript.addCourseRecord(s.getCourse(), LetterGrade.NOT_GRADED, season, null, this.getGrade(), false);
        }
    }

    public void register(){

        Logger.log("");
        Logger.log("Registration process of " + this.getFullName() + " started");
        Logger.log("");

        // checks

        // sections to be removed and added from the enrolledsections due to full quota

        for(Section sec: enrolledSections) {

            Course c = sec.getCourse();

            // checks
            // if one of the requirement is not met then close the program
            if (!c.canStudentTakeCourse(this)) {
                return;
            }

        }

        // check for collisions between the course sections the student wants to take
        var collisions = Section.checkForCollisions(enrolledSections);

        for(var c : collisions){
            Section sec1 = c.GetKey();
            Section sec2 = c.GetValue();

            var collisionBetween = sec1.getCollisionsWith(sec2);

            for(var d : collisionBetween){
                int collisionDay = d.GetKey();
                int collisionHour = d.GetValue();

                Logger.log("There is a collision between " + sec1.getCourse().getCode()
                        + " and " + sec2.getCourse().getCode() + " on " + Section.CLASS_DAYS[collisionDay] + " at " + Section.CLASS_HOURS[collisionHour]);

                // clear all the enrolled courses in the process
                //this.enrolledCourseSections.clear();
                this.enrolledSections.remove(sec1);

                Logger.log("Student is taking " + sec2.getCourse().getCode());

                //Logger.log("Registration process of " + this.getFullName() + " ended");

                Logger.log("");
                Logger.log("");
            }
        }


        collisions.clear();

        if(collisions.size() == 0){

            enrollCourseSections(enrolledSections, Department.getInstance().getCurrentSeason());

            Logger.log("");

            String schedule = generateWeeklySchedule();
            Logger.log(schedule);
        }

        Logger.log("Registration process of " + this.getFullName() + " ended");

        Logger.log("");
        Logger.log("");
    }

    public String generateWeeklySchedule(){

        String program = this.getFullName() + "'s Weekly Schedule\n";

        if(this.enrolledSections.size() == 0){
            program = "The student " + this.getFullName() + " doesn't have any enrolled courses";
        }

        List<Section[]> schedule = Section.combineSchedules(this.enrolledSections);

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

    public void addToRegistrationList(Section section){
        if(section.isSectionFull()){

            Logger.log("This section of " + section.getCourse().getCode() + " is already full");
            return;
        }

        this.enrolledSections.add(section);
    }
    public static int tryToRegister(Student student,Course course, int counter){

        var courseSections = course.getAvailableCourseSections();

        if(courseSections.size() == 0)
            return counter;

        student.addToRegistrationList(courseSections.get(0));

        var labSections = course.getAvailableLabSections();

        if(labSections.size() > 0){
            student.addToRegistrationList(labSections.get(0));
        }

        return counter + 1;
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
        return enrolledSections;
    }

    public int getCompletedCredits(){
        return transcript.getCompletedCredits();
    }

    public Transcript getTranscript(){
        return transcript;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }


}
