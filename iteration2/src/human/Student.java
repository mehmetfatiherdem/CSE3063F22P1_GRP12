package iteration2.src.human;

import iteration2.src.Department;
import iteration2.src.RandomNumberGenerator;
import iteration2.src.Transcript;
import iteration2.src.course.*;
import iteration2.src.input_output.HorizontalLineType;
import iteration2.src.input_output.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends Human{
    public static float minFailChance;
    public static float maxFailChance;
    public static float minRetakeChance;
    public static float maxRetakeChance;
    public static float minNotTakeChance;
    public static float maxNotTakeChance;

    private String studentID;
    private Grade grade;
    private Advisor advisor;

    private float failChance;
    private float retakeChance;
    private float notTakeChance;
    private Transcript transcript;
    private List<Section> enrolledSections = new ArrayList<>();
    private Map<String, Integer> chosenCourseTypeCounterInFall = new HashMap<>(){{
        put("Mandatory", 0);
        put("NTE", 0);
        put("TE", 0);
        put("FTE", 0);
    }};

    public Student(String firstName, String middleName, String lastName,String studentID,Grade grade ,Advisor advisor,List<CourseRecord> transcript){
        super(firstName, middleName, lastName);

        this.studentID = studentID;
        this.grade = grade;
        this.advisor = advisor;
        this.transcript = new Transcript(transcript);

        failChance = RandomNumberGenerator.RandomFloatBetween(minFailChance, maxFailChance);
        retakeChance = RandomNumberGenerator.RandomFloatBetween(minRetakeChance, maxRetakeChance);
        notTakeChance = RandomNumberGenerator.RandomFloatBetween(minNotTakeChance, maxNotTakeChance);
    }

    public Student(String firstName, String lastName,String studentID, Grade grade ,Advisor advisor, List<CourseRecord> transcript){
        this(firstName,null,lastName,studentID,grade,advisor,transcript);
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
            }
        }

        collisions.clear();

        if(collisions.size() == 0){
            enrollCourseSections(enrolledSections, Department.getInstance().getCurrentSeason());

            Logger.log("Student schedule is : ");
            Logger.log("");
            Logger.logStudentSchedule(enrolledSections, HorizontalLineType.EqualsSign, '|');
            Logger.log("");

        }

        Logger.log("Registration process of " + this.getFullName() + " ended");
        Logger.log("");
    }

    public void addToRegistrationList(Section section){
        if(section.isSectionFull()){

            Logger.log("This section of " + section.getCourse().getCode() + " is already full");
            return;
        }

        this.enrolledSections.add(section);
    }
    public void tryToRegister(Student student, Course course){

        String courseTypeOfTheCounter = "";

        if(course instanceof NonTechnicalElectiveCourse){
            courseTypeOfTheCounter = "NTE";
        } else if(course instanceof TechnicalElectiveCourse){
            courseTypeOfTheCounter = "TE";
        }else if(course instanceof FacultyTechnicalElectiveCourse){
            courseTypeOfTheCounter = "FTE";
        }else if(course instanceof MandatoryCourse){
            courseTypeOfTheCounter = "Mandatory";
        }

        int counter = student.getChosenCourseTypeCounterInFall().get(courseTypeOfTheCounter);

        var courseSections = course.getAvailableCourseSections();

        if(courseSections.size() == 0)
            return;

        student.addToRegistrationList(courseSections.get(0));

        var labSections = course.getAvailableLabSections();

        if(labSections.size() > 0){
            student.addToRegistrationList(labSections.get(0));
        }

        student.getChosenCourseTypeCounterInFall().put(courseTypeOfTheCounter, counter + 1);
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
    public float getFailChance(){
        return failChance;
    }

    public float getRetakeChance(){
        return retakeChance;
    }

    public float getNotTakeChance(){
        return notTakeChance;
    }

    public Map<String, Integer> getChosenCourseTypeCounterInFall() {
        return chosenCourseTypeCounterInFall;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
