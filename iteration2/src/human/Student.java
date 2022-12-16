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
import java.util.concurrent.Callable;
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

            if(s instanceof LabSection && s.getCourse().getTheoreticalHours() != 0)
                continue;

            transcript.addCourseRecord(s.getCourse(), LetterGrade.NOT_GRADED,
                    Department.getInstance().getCurrentSeason(), null, this.getGrade(), false);
        }
    }

    public void startRegistration(List<MandatoryCourse> openMandatoryCourses, List<TechnicalElectiveCourse> openTECourses,
                                  List<FacultyTechnicalElectiveCourse> openFTECourses, List<NonTechnicalElectiveCourse> openNTECourses,
                                  int noOfTakeableTECourses, int noOfTakeableFTECourses,int noOfTakeableNTECourses){

        Logger.newLine();
        Logger.log(getFullName() + " starts registering to courses");
        Logger.newLine();

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
            Course course = openCourses.get(randomIndex);

            if(transcript.didStudentFailBefore(course) && !studentWantsToRetake())
                continue;
            if(!studentWantsToTake())
                continue;

            tryToRegister(course);
            openCourses.remove(randomIndex);
        }
    }

    public void tryToRegister(Course course){
        boolean availableSection = false;
        List<CourseSection> courseSections = course.getCourseSections();

        if(courseSections.size() == 0){
            Logger.incrementIndentation();
            Logger.log("THERE ARE NO COURSE SECTIONS AVAILABLE FOR " + course.getCode());
            Logger.decrementIndentation();
        }

        for (CourseSection s : courseSections){
            if(!s.isSectionFull()){
                enrolledSections.add(s);
                Logger.log(getFullName() + " registers to " + s.toString());
                availableSection = true;
                break;
            }
            else{
                Logger.incrementIndentation();
                Logger.log("THE QUOTA OF " + s.toString() + " IS FULL");
                Logger.decrementIndentation();
            }
        }

        if(!availableSection){
            course.requestNewCourseSection();
            List<CourseSection> availableSections;

            if((availableSections = course.getAvailableCourseSections()).size() > 0){
                Section s = availableSections.get(0);
                enrolledSections.add(availableSections.get(0));
                Logger.log(getFullName() + " registers to " + s.toString());
            }
        }

        List<LabSection> availableSections;

        if((availableSections = course.getAvailableLabSections()).size() > 0){
            Section s = availableSections.get(0);
            enrolledSections.add(s);
            Logger.log(getFullName() + " registers to " + s.toString());
        }
        else{
            Logger.incrementIndentation();
            Logger.log("THERE ARE NO LAB SECTIONS AVAILABLE FOR " + course.getCode());
            Logger.decrementIndentation();
        }
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
        registrationSystemCheck(getFullName() + " ends registering and checks their schedule to see if they can send their registration to advisor approval :");
        advisorApproval();

        saveToTranscript();

        Logger.logStudentSchedule(enrolledSections, HorizontalLineType.EqualsSign,'|');
    }

    private void registrationSystemCheck(String preCheckLog){
        Supplier<List<Tuple<Section,Section>>> checkCollisionCallback = () -> {
            return RegistrationSystem.getInstance().checkEnrolledSections(this);
        };

        Logger.newLine();
        Logger.log(preCheckLog);
        Logger.newLine();

        handleUnacceptedCollisions(checkCollisionCallback, null ,
                getFullName() + " checks again to see if they can send their registration to advisor approval :");
    }

    private void advisorApproval(){
        Supplier<List<Tuple<Section,Section>>> checkCollisionCallback = () -> {
            return RegistrationSystem.getInstance().sendToAdvisorApproval(this);
        };

        Runnable registrationSystemRecheck = () -> {
          registrationSystemCheck(getFullName() + " checks their schedule to see if they can send their registration to advisor approval :");
        };

        Logger.newLine();
        Logger.log(getFullName() + " sends an approval request of their registration to their advisor " + advisor.getFullName());
        Logger.newLine();

        handleUnacceptedCollisions(checkCollisionCallback, registrationSystemRecheck,
                getFullName() + " sends another approval request of their registration to their advisor");
    }

    private void handleUnacceptedCollisions(Supplier<List<Tuple<Section,Section>>> collisionCheckCallback,Runnable onReplacCallback,String collisionRecheckLog){
        List<Tuple<Section,Section>> unacceptedCollisions;
        int replaceCounter = 0;

        while ((unacceptedCollisions = collisionCheckCallback.get()).size() > 0){
            Logger.log(getFullName() + " starts looking into their collision issues");
            Logger.newLine();

            boolean replace = false;

            for(var collision : unacceptedCollisions){
                replaceCounter++;
                Section s1 = collision.getKey();
                Section s2 = collision.getValue();
                Section[] temp = new Section[] {s1, s2};

                if(replaceCounter <= 3 && tryToReplace(new Section[]{s1, s2})){
                    replace = true;
                    continue;
                }

                removeEither(s1, s2);
            }

            if(replace && onReplacCallback != null)
                onReplacCallback.run();

            Logger.log(collisionRecheckLog);
        }
    }

    private void removeEither(Section s1, Section s2) {
        int s1Priority = s1.getSectionPriority();
        int s2Priority = s2.getSectionPriority();

        Section sectionToRemove;

        if(s1Priority == s2Priority){
            if(RandomNumberGenerator.randomIntegerBetween(0,2) == 0)
                sectionToRemove = s1;
            else
                sectionToRemove = s2;
        }
        else{
            sectionToRemove = s1Priority > s2Priority ? s2 : s1;
        }

        boolean notRemovedBefore = enrolledSections.remove(sectionToRemove);

        if(notRemovedBefore){
            Logger.log(getFullName() + " removes " + sectionToRemove.toString());
            Logger.newLine();
        }
    }

    private boolean tryToReplace(Section[] sections) {
        boolean resolved = false;
        
        for(int i = 0; i < 2; i++){
            Section s = sections[i];
            var alternativeSection = pickAlternativeSection(s);

            if(alternativeSection != null){

                var notRemovedBefore = enrolledSections.remove(s);

                if(notRemovedBefore)
                    Logger.log(getFullName() + " replaces " + s.toString() + " with " + alternativeSection.toString());
                else
                    Logger.log(getFullName() + " registers to " + alternativeSection.toString() + " in place of " + s.toString() + " which they removed earlier");
                enrolledSections.add(alternativeSection);
                resolved = true;
                break;
            }
        }
        
        return resolved;
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

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
