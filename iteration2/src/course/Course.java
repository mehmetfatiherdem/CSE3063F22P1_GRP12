package iteration2.src.course;
import iteration2.src.Department;
import iteration2.src.RandomNumberGenerator;
import iteration2.src.human.Assistant;
import iteration2.src.human.Grade;
import iteration2.src.human.Lecturer;
import iteration2.src.human.Student;
import iteration2.src.input_output.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class Course {
    private static final int minQuota = 40;
    private static final int maxQuota = 80;

    protected String code;
    protected String name;
    protected int credits;
    protected int ects;
    protected int theoreticalHours;
    protected int appliedHours;
    protected Grade firstYearToTake;
    protected Season firstSeasonToTake;
    protected int quota;

    protected List<Course> prerequisites = new ArrayList<>();
    protected List<Lecturer> lecturers = new ArrayList<>();
    protected List<Assistant> assistants = new ArrayList<>();
    protected List<CourseSection> courseSections = new ArrayList<>();
    protected List<LabSection> labSections = new ArrayList<>();

    public Course(String code, String name, int credits, int theoreticalHours, int appliedHours,
                  Grade firstYearToTake,Season firstSeasonToTake, List<Lecturer> lecturers, List<Assistant> assistants){
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.ects = credits;
        this.theoreticalHours = theoreticalHours;
        this.appliedHours = appliedHours;
        this.firstYearToTake = firstYearToTake;
        this.firstSeasonToTake = firstSeasonToTake;

        if(theoreticalHours > 0)
            this.lecturers = lecturers;

        if(appliedHours > 0)
            this.assistants = assistants;

        this.quota = RandomNumberGenerator.randomIntegerBetween(minQuota,maxQuota + 1);

        //Each and every semester, at least one section of all mandatory courses should be registrable without any collision
    }

    public Boolean isAnyCourseSectionAvailable(){
        for(CourseSection s: courseSections){
            if(!s.isSectionFull())
                return  true;
        }

        return false;
    }
    public  Boolean isAnyLabSectionAvailable(){
        for(LabSection s:labSections){
            if(!s.isSectionFull())
                return true;
        }

        return false;
    }
    public void addPrerequisite(Course prerequisite){
        prerequisites.add(prerequisite);
    }
    public void addCourseSection(long schedule){
        if(theoreticalHours == 0)
            return;

        int randomIndex = RandomNumberGenerator.randomIntegerBetween(0, lecturers.size());
        Lecturer lecturer = lecturers.get(randomIndex);
        String sectionCode = String.valueOf(courseSections.size() + 1);

        courseSections.add(new CourseSection(this,sectionCode,schedule,lecturer));
    }

    public void addLabSection(long schedule){
        if(appliedHours == 0)
            return;

        int randomIndex = RandomNumberGenerator.randomIntegerBetween(0, assistants.size());
        Assistant assistant = assistants.get(randomIndex);

        StringBuilder sectionCode = new StringBuilder();
        sectionCode.append(courseSections.size());
        sectionCode.append('.');
        sectionCode.append(labSections.size() + 1);

        labSections.add(new LabSection(this,sectionCode.toString(),schedule,assistant));
    }

    public boolean isStudentGradeRequirementMet(Student s, Season currentSeason){
        return true;
    }

    public Boolean canStudentTakeCourse(Student student){
        if((student.getGrade().getValue() < firstYearToTake.getValue()
                || (student.getGrade().getValue() == firstYearToTake.getValue()
                && Department.getInstance().getCurrentSeason().getValue() < firstSeasonToTake.getValue()))
                || student.didStudentPass(this) || !student.checkIfPrerequisitesArePassed(this)){
            Logger.log("In order to take " + code + " a student grade should be greater than or equal to " + firstYearToTake
                    + ", the current season must be " + firstSeasonToTake + " and the student shouldn't pass the this course in the past semesters");
            return false;
        }

        return true;
    }


    public List<CourseSection> getAvailableCourseSections(){
        List<CourseSection> sections = new ArrayList<>();

        for (CourseSection s: courseSections){
            if(!s.isSectionFull())
                sections.add(s);
        }

        return sections;
    }
    public List<LabSection> getAvailableLabSections(){
        List<LabSection> sections = new ArrayList<>();

        for(LabSection s: labSections){
            if(!s.isSectionFull())
                sections.add(s);
        }

        return sections;
    }

    //Getters
    public int getCredits() {
        return credits;
    }
    public String getCode() {
        return code;
    }
    public String getName(){
        return name;
    }
    public List<Course> getPrerequisites() {
        return prerequisites;
    }
    public int getEcts() {
        return ects;
    }
    public int getTheoreticalHours() {
        return theoreticalHours;
    }
    public int getAppliedHours() {
        return appliedHours;
    }
    public Grade getFirstYearToTake() {
        return firstYearToTake;
    }
    public List<Lecturer> getLecturers() {
        return lecturers;
    }
    public Season getFirstSeasonToTake() {
        return firstSeasonToTake;
    }
    public List<Assistant> getAssistants() {
        return assistants;
    }
    public List<Section> getAllSections() {
        List<Section> allSections = new ArrayList<>(courseSections);
        allSections.addAll(labSections);
        return allSections;
    }

    public List<CourseSection> getCourseSections(){
        return courseSections;
    }

    public List<LabSection> getLabSections(){
        return labSections;
    }
    public int getQuota() {
        return quota;
    }
}
