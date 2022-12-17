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
import java.util.function.Consumer;
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

        Logger.newLine(HorizontalLineType.EqualsSign);
        Logger.newLine();
        Logger.log(getFullName() + " starts registering to courses");
        Logger.newLine();

        for(var c : openMandatoryCourses){
            if(!studentWantsToTake()){
                Logger.log(getFullName() + " does not want to take " + c.getCode() + " in the current semester");
                continue;
            }

            tryToRegister(c,null);
        }

        registerToElectiveCourses((List<ElectiveCourse>)(List<?>) openTECourses, noOfTakeableTECourses,null);
        registerToElectiveCourses((List<ElectiveCourse>)(List<?>) openFTECourses, noOfTakeableFTECourses,null);
        registerToElectiveCourses((List<ElectiveCourse>)(List<?>) openNTECourses, noOfTakeableNTECourses,null);

        endRegistration(openTECourses,openFTECourses,openNTECourses);
    }

    private ElectiveCourse registerToElectiveCourses(List<ElectiveCourse> openCourses, int noOfTakeableCourses, Section insteadOf) {
        ElectiveCourse lastRemoved = null;

        for(int i = 0; i < noOfTakeableCourses; i++){
            int randomIndex = RandomNumberGenerator.randomIntegerBetween(0, openCourses.size());
            Course course = openCourses.get(randomIndex);

            if(transcript.didStudentFailBefore(course) && !studentWantsToRetake()){
                Logger.log(getFullName() + " does not want to retake the " + course.getCode() + " which they failed earlier");
                continue;
            }
            if(!studentWantsToTake())
                continue;

            tryToRegister(course,insteadOf);
            lastRemoved = openCourses.remove(randomIndex);
        }

        return lastRemoved;
    }

    public void tryToRegister(Course course, Section insteadOf){
        boolean availableSection = false;
        List<CourseSection> courseSections = course.getCourseSections();

        if(courseSections.size() == 0){
            Logger.incrementIndentation();
            Logger.log("(!) THERE ARE NO COURSE SECTIONS AVAILABLE FOR " + course.getCode());
            Logger.decrementIndentation();
        }

        for (CourseSection s : courseSections){
            if(!s.isSectionFull()){
                enrolledSections.add(s);

                if(insteadOf == null)
                    Logger.log(getFullName() + " registers to " + s.toString());
                else
                    Logger.log(getFullName() + " registers to " + s.toString() + " in place of the " + insteadOf.toString() + " which they removed previously");

                availableSection = true;
                break;
            }
            else{
                Logger.log(getFullName() + " tries to register to " + s.toString());
                Logger.incrementIndentation();
                Logger.log("(!) THE QUOTA OF " + s.toString() + " IS FULL");
                Logger.decrementIndentation();
            }
        }

        if(!availableSection){
            course.requestNewCourseSection();
            List<CourseSection> availableSections;

            if((availableSections = course.getAvailableCourseSections()).size() > 0){
                Section s = availableSections.get(0);
                enrolledSections.add(availableSections.get(0));

                if(insteadOf == null)
                    Logger.log(getFullName() + " registers to " + s.toString());
                else
                    Logger.log(getFullName() + " registers to " + s.toString() + " in place of the " + insteadOf.toString() + " which they removed previously");
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
            Logger.log("(!) THERE ARE NO LAB SECTIONS AVAILABLE FOR " + course.getCode());
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

    private void endRegistration(List<TechnicalElectiveCourse> openTECourses, List<FacultyTechnicalElectiveCourse> openFTECourses,
                                 List<NonTechnicalElectiveCourse> openNTECourses){

        Logger.newLine();
        registrationSystemCheck(openTECourses,openFTECourses,openNTECourses,
                getFullName() + " ends registering and checks their schedule to see if they can send their registration to advisor approval :");

        advisorApproval(openTECourses,openFTECourses,openNTECourses);
        Logger.newLine();

        Logger.log("THE STUDENT'S REGISTRATION IS COMPLETED!");
        Logger.log("TRANSCRIPT IS BEING UPDATED WITH THE NEWLY REGISTERED COURSES");

        saveToTranscript();

        Logger.log("STUDENT'S TRANSCRIPT IS UPDATED!");
        Logger.log("STUDENT'S SCHEDULE IS BEING CREATED :");
        Logger.newLine();
        Logger.log("STUDENT NAME : " + getFullName());
        Logger.log("STUDENT ID : " + getStudentID());
        Logger.log("ADVISOR NAME : " +getAdvisor().getFullName());

        Logger.logStudentSchedule(enrolledSections, HorizontalLineType.Dash,'|');
        Logger.newLine(HorizontalLineType.EqualsSign);
    }

    private void registrationSystemCheck(List<TechnicalElectiveCourse> openTECourses, List<FacultyTechnicalElectiveCourse> openFTECourses,
                                         List<NonTechnicalElectiveCourse> openNTECourses,String preCheckLog){
        Supplier<List<Tuple<Section,Section>>> checkCollisionCallback = () -> {
            return RegistrationSystem.getInstance().checkEnrolledSections(this);
        };

        Logger.newLine();
        Logger.log(preCheckLog);
        Logger.newLine();

        checkFixLoop(openTECourses,openFTECourses,openNTECourses,checkCollisionCallback, null ,
                getFullName() + " checks again to see if they can send their registration to advisor approval :");
    }

    private void advisorApproval(List<TechnicalElectiveCourse> openTECourses, List<FacultyTechnicalElectiveCourse> openFTECourses,
                                 List<NonTechnicalElectiveCourse> openNTECourses){

        Supplier<List<Tuple<Section,Section>>> checkCollisionCallback = () -> {
            return RegistrationSystem.getInstance().sendToAdvisorApproval(this);
        };

        Runnable registrationSystemRecheck = () -> {
          registrationSystemCheck(openTECourses,openFTECourses,openNTECourses,
                  getFullName() + " checks their schedule to see if they can send their registration to advisor approval :");
        };

        Logger.newLine();
        Logger.log(getFullName() + " sends an approval request of their registration to their advisor " + advisor.getFullName());
        Logger.newLine();

        checkFixLoop(openTECourses,openFTECourses,openNTECourses,checkCollisionCallback, registrationSystemRecheck,
                getFullName() + " sends another approval request of their registration to their advisor");
    }

    private void checkFixLoop(List<TechnicalElectiveCourse> openTECourses, List<FacultyTechnicalElectiveCourse> openFTECourses,
                              List<NonTechnicalElectiveCourse> openNTECourses,Supplier<List<Tuple<Section,
            Section>>> collisionCheckCallback, Runnable onReplaceCallback, String collisionRecheckLog){

        List<Tuple<Section,Section>> unacceptedCollisions;
        List<Section> alreadyTriedSections = new ArrayList<>();

        while ((unacceptedCollisions = collisionCheckCallback.get()).size() > 0){
            Logger.log(getFullName() + " starts looking into their collision issues");
            Logger.newLine();

            boolean replacement = false;

            replacement = handleUnacceptedCollisions(openTECourses,openFTECourses,openNTECourses,unacceptedCollisions, alreadyTriedSections);

            if(replacement && onReplaceCallback != null)
                onReplaceCallback.run();

            Logger.newLine();
            Logger.log(collisionRecheckLog);
            Logger.newLine();
        }
    }

    private boolean handleUnacceptedCollisions(List<TechnicalElectiveCourse> openTECourses, List<FacultyTechnicalElectiveCourse> openFTECourses,
                                               List<NonTechnicalElectiveCourse> openNTECourses,List<Tuple<Section, Section>> unacceptedCollisions, List<Section> alreadyTriedSections) {

        boolean anyReplacement = false;

        for (var collision : unacceptedCollisions){
            Section s1 = collision.getKey();
            Section s2 = collision.getValue();

            addIfNotAlreadyContained(alreadyTriedSections,s1);
            addIfNotAlreadyContained(alreadyTriedSections,s2);
        }

        for(int i = 0; i < unacceptedCollisions.size(); i++){
            var collision = unacceptedCollisions.get(i);
            Section s1 = collision.getKey();
            Section s2 = collision.getValue();

            Section sectionRemoved;

            if((sectionRemoved = tryToReplace(alreadyTriedSections,new Section[]{s1, s2})) != null){
                eliminateResolvedCollisions(unacceptedCollisions, i, sectionRemoved);
                anyReplacement = true;
                continue;
            }

            sectionRemoved = removeEither(s1, s2);
            eliminateResolvedCollisions(unacceptedCollisions,i,sectionRemoved);

            if(!(sectionRemoved.getCourse() instanceof ElectiveCourse))
                continue;

            anyReplacement |= tryToRegisterToAnotherElective(openTECourses, openFTECourses, openNTECourses,
                    alreadyTriedSections, sectionRemoved);
        }

        return anyReplacement;
    }

    private void eliminateResolvedCollisions(List<Tuple<Section, Section>> unacceptedCollisions, int i, Section sectionRemoved) {
        for(int j = i + 1; j < unacceptedCollisions.size(); j++){
            var collision = unacceptedCollisions.get(j);
            Section s1 = collision.getKey();
            Section s2 = collision.getValue();

            if(sectionRemoved.equals(s1) || sectionRemoved.equals(s2))
                unacceptedCollisions.remove(j--);
        }
    }

    private Section tryToReplace(List<Section> alreadyTriedSections,Section[] sections) {
        Section sectionRemoved = null;

        for(int i = 0; i < 2; i++){
            Section s = sections[i];
            var alternativeSection = pickAlternativeSection(alreadyTriedSections,s);

            if(alternativeSection != null){
                var notRemovedBefore = enrolledSections.remove(s);

                if(notRemovedBefore){
                    sectionRemoved = s;
                    Logger.log(getFullName() + " replaces " + s.toString() + " with " + alternativeSection.toString());
                }
                else{
                    Logger.log(getFullName() + " registers to " + alternativeSection.toString() + " in place of " + s.toString() + " which they removed earlier");
                }

                enrolledSections.add(alternativeSection);
                addIfNotAlreadyContained(alreadyTriedSections,alternativeSection);
                break;
            }
        }

        return sectionRemoved;
    }

    private Section removeEither(Section s1, Section s2) {
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

        if(notRemovedBefore)
            Logger.log(getFullName() + " removes " + sectionToRemove.toString());

        return sectionToRemove;
    }

    private boolean tryToRegisterToAnotherElective(List<TechnicalElectiveCourse> openTECourses, List<FacultyTechnicalElectiveCourse> openFTECourses,
                                                   List<NonTechnicalElectiveCourse> openNTECourses, List<Section> alreadyTriedSections,Section sectionRemoved) {
        List<ElectiveCourse> electives = null;
        Course removedCourse = sectionRemoved.getCourse();

        if(removedCourse instanceof TechnicalElectiveCourse)
            electives = (List<ElectiveCourse>)(List<?>) openTECourses;
        else if(removedCourse instanceof FacultyTechnicalElectiveCourse)
            electives = (List<ElectiveCourse>)(List<?>) openFTECourses;
        else if(removedCourse instanceof NonTechnicalElectiveCourse)
            electives = (List<ElectiveCourse>)(List<?>) openNTECourses;

        if(electives == null || electives.size() == 0)
            return false;

        ElectiveCourse newRegistration = registerToElectiveCourses(electives,1,sectionRemoved);

        if(newRegistration == null)
            return false;

        Section newSection = null;
        for (var s : enrolledSections)
            if(s.getCourse().equals(newRegistration))
                newSection = s;

        if(newSection == null)
            return false;

        addIfNotAlreadyContained(alreadyTriedSections,newSection);
        return true;
    }
    private Section pickAlternativeSection(List<Section> alreadyTriedSections,Section section){
        var alternativeSections = section.getCourse().getAlternativeSections(section);

        for(int i = 0; i < alternativeSections.size(); i++){
            if(i == -1)
                break;

            Section s = alternativeSections.get(i);

            if(alreadyTriedSections.contains(s)){
                alternativeSections.remove(i);
                i--;
            }
        }

        if(alternativeSections.size() == 0)
            return null;

        return alternativeSections.get(RandomNumberGenerator.randomIntegerBetween(0,alternativeSections.size()));
    }

    private void addIfNotAlreadyContained(List<Section> sections, Section addition){
        if(!sections.contains(addition))
            sections.add(addition);
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
