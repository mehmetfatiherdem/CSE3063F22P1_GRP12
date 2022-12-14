package iteration2.src.human;

import iteration2.src.Department;
import iteration2.src.RandomNumberGenerator;
import iteration2.src.RegistrationSystem;
import iteration2.src.Transcript;
import iteration2.src.course.*;
import iteration2.src.data_structures.Tuple;
import iteration2.src.input_output.HorizontalLineType;
import iteration2.src.input_output.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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

    public void saveToTranscript(){
        for(Section s: enrolledSections){
            s.addToStudentList(this);
            transcript.addCourseRecord(s.getCourse(), LetterGrade.NOT_GRADED,
                    Department.getInstance().getCurrentSeason(), null, this.getGrade(), false);
        }
    }

    public void startRegistration(List<MandatoryCourse> openMandatoryCourses, List<TechnicalElectiveCourse> openTECourses,
                                  List<FacultyTechnicalElectiveCourse> openFTECourses, List<NonTechnicalElectiveCourse> openNTECourses,
                                  int noOfTakeableTECourses, int noOfTakeableFTECourses,int noOfTakeableNTECourses){

        Logger.log("");
        Logger.log("Registration process of " + this.getFullName() + " started");
        Logger.log("");

        for(var c : openMandatoryCourses){
            if(!studentWantsToTake())
                continue;

            tryToRegister(c);
        }

        startRegistrationOnElectiveCourses((List<ElectiveCourse>)(List<?>) openTECourses, noOfTakeableTECourses);
        startRegistrationOnElectiveCourses((List<ElectiveCourse>)(List<?>) openFTECourses, noOfTakeableFTECourses);
        startRegistrationOnElectiveCourses((List<ElectiveCourse>)(List<?>) openNTECourses, noOfTakeableNTECourses);

        endRegistration();
    }

    private void startRegistrationOnElectiveCourses(List<ElectiveCourse> openCourses, int noOfTakeableCourses) {
        for(int i = 0; i < noOfTakeableCourses; i++){
            int randomIndex = RandomNumberGenerator.randomIntegerBetween(0, openCourses.size());
            Course course;
            try {
                course = openCourses.get(randomIndex);
            }catch (Exception e){
                course = null;
            }

            if(!transcript.didStudentFailBefore(course) || !studentWantsToRetake())
                continue;
            if(!studentWantsToTake())
                continue;

            tryToRegister(course);
            openCourses.remove(randomIndex);
        }
    }

    public void tryToRegister(Course course){
        var courseSections = course.getAvailableCourseSections();

        if(courseSections.size() == 0)
            return;

        enrolledSections.add(courseSections.get(0));

        var labSections = course.getAvailableLabSections();

        if(labSections.size() > 0)
            enrolledSections.add(courseSections.get(0));
    }

    private boolean studentWantsToTake(){
        float rand = RandomNumberGenerator.RandomFloat();
        return rand > notTakeChance;
    }

    private boolean studentWantsToRetake(){
        float rand = RandomNumberGenerator.RandomFloat();
        return rand <= retakeChance;
    }

    private Section pickAlternativeSection(Section section){
        var alternativeSections = section.getCourse().getAlternativeSections(section);

        if(alternativeSections.size() == 0)
            return null;

        return alternativeSections.get(RandomNumberGenerator.randomIntegerBetween(0,alternativeSections.size()));
    }

    private void endRegistration(){
        Supplier<List<Tuple<Section,Section>>> checkCollisionCallback = () -> {
            return RegistrationSystem.getInstance().checkEnrolledSections(this);
        };

        //Validated by the registration system
        handleUnacceptedCollisions(checkCollisionCallback);

        checkCollisionCallback = () -> {
          return RegistrationSystem.getInstance().sendToAdvisorApproval(this);
        };

        //Validated by the advisor
        handleUnacceptedCollisions(checkCollisionCallback);


        saveToTranscript();

        Logger.log("");
        Logger.log("Student Name : " + getFullName());
        Logger.log("Student ID : " + studentID);
        Logger.log("Advisor Name : "  + advisor.getFullName());
        Logger.log("");

        Logger.logStudentSchedule(enrolledSections, HorizontalLineType.EqualsSign,'|');
    }

    private void handleUnacceptedCollisions(Supplier<List<Tuple<Section,Section>>> collisionCheckCallback){
        List<Tuple<Section,Section>> unacceptedCollisions;

        while ((unacceptedCollisions = collisionCheckCallback.get()).size() > 0){
            for(var collision : unacceptedCollisions){
                Section s1 = collision.getKey();
                Section s2 = collision.getValue();
                Section[] temp = new Section[] {s1, s2};

                boolean resolved = false;

                for(int i = 0; i < 2; i++){
                    Section s = temp[i];
                    var alternativeSection = pickAlternativeSection(s);

                    if(alternativeSection != null){
                        Logger.log("");
                        Logger.log("Student replaced section " + s.toString() + " with section " + alternativeSection.toString());
                        Logger.log("");
                        enrolledSections.remove(s);
                        enrolledSections.add(alternativeSection);
                        resolved = true;
                        break;
                    }
                }

                if(resolved)
                    continue;

                int s1Priority = s1.getSectionPriority();
                int s2Priority = s2.getSectionPriority();

                if(s1Priority == s2Priority){
                    Section sectionToRemove = temp[RandomNumberGenerator.randomIntegerBetween(0,2)];
                    Logger.log("");
                    Logger.log("Student removed the section " + sectionToRemove.toString());
                    Logger.log("");
                    enrolledSections.remove(sectionToRemove);
                }
                else{
                    Logger.log("");
                    Logger.log("Student removed the section " + (s1Priority > s2Priority ? s2 : s1) + "due to high priority of other section");
                    Logger.log("");
                    enrolledSections.remove(s1Priority > s2Priority ? s2 : s1);
                }
            }
        }
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

    public int getStudentSemester() {
        return 2 * getGrade().getValue() + Department.getInstance().getCurrentSeason().getValue();
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

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
