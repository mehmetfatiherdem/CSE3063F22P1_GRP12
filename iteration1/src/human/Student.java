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


    public Student(String firstName, String middleName, String lastName){
        super(firstName, middleName, lastName);
    }

    public Student(String firstName, String lastName){
        super(firstName, lastName);
    }

    public void enrollCourseSections(List<Section> sections, int year, Season season){

        for(Section s: sections){
            s.addToStudentList(this);


            // TODO: uncomment and fix this when the transcript PR is merged
            // this.transcript.takenCourseRecords.add(new CourseRecord(s.course, LetterGrade.NOT_GRADED, null, data.season, data.year, false))
        }

    }

    public void register(RegistrationData data){

        System.out.println("Registration process of " + this.getFullName() + " started");
        System.out.println();

        // checks

        // sections to be removed and added from the enrolledsections due to full quota

        List<Section> secToRemove = new ArrayList<>();
        List<Section> secToAdd = new ArrayList<>();

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

            enrollCourseSections(enrolledCourseSections, year, season);

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
        if(section.isSectionFull()){

            System.out.println("This section of the class is already full");

            //TODO: handle a mandatory course section is full so open a new one

            Course c = section.getCourse();

            if(c instanceof MandatoryCourse){
                    for(Section s: c.getSectionList()){
                        if(!s.isSectionFull()){
                            this.enrolledCourseSections.add(s);
                            return;
                        }
                    }
                    Section newSec = ((MandatoryCourse) c).openANewSection();
                    c.addToSectionList(newSec);

                    this.enrolledCourseSections.add(newSec);

            }

            return;
        }

        this.enrolledCourseSections.add(section);
    }

}
