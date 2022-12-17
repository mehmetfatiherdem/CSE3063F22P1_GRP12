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

    public void addPrerequisite(Course prerequisite){
        prerequisites.add(prerequisite);
    }
    public CourseSection addCourseSection(long schedule){
        if(theoreticalHours == 0)
            return null;

        int randomIndex = RandomNumberGenerator.randomIntegerBetween(0, lecturers.size());
        Lecturer lecturer = lecturers.get(randomIndex);
        String sectionCode = String.valueOf(courseSections.size() + 1);

        CourseSection newSection = new CourseSection(this,sectionCode,schedule,lecturer);
        courseSections.add(newSection);

        return newSection;
    }

    public LabSection addLabSection(long schedule){
        if(appliedHours == 0)
            return null;

        int randomIndex = RandomNumberGenerator.randomIntegerBetween(0, assistants.size());
        Assistant assistant = assistants.get(randomIndex);

        StringBuilder sectionCode = new StringBuilder();
        sectionCode.append(courseSections.size());
        sectionCode.append('.');
        sectionCode.append(labSections.size() + 1);

        LabSection newSection = new LabSection(this,sectionCode.toString(),schedule,assistant);
        labSections.add(newSection);

        return newSection;
    }

    public boolean isStudentGradeRequirementMet(Student student){
        return getCourseSemester() <= student.getStudentSemester();
    }

    public Boolean canStudentTakeCourse(Student student){
        return isStudentGradeRequirementMet(student) && !student.didStudentPass(this)
                && student.checkIfPrerequisitesArePassed(this);
    }


    public List<CourseSection> getAvailableCourseSections(){
        List<CourseSection> sections = new ArrayList<>();

        for (CourseSection s: courseSections){
            if(!s.isSectionFull())
                sections.add(s);
        }

        return sections;
    }

    public List<Section> getAlternativeSections(Section section){
        List<Section> alternatives = new ArrayList<>();

        if(section instanceof CourseSection) {
            for (Section s : courseSections)
                if (!section.equals(s))
                    alternatives.add(s);
        }
        else{
            for (Section s : labSections)
                if(!section.equals(s))
                    alternatives.add(s);
        }
        return alternatives;
    }

    public List<LabSection> getAvailableLabSections(){
        List<LabSection> sections = new ArrayList<>();

        for(LabSection s: labSections){
            if(!s.isSectionFull())
                sections.add(s);
        }

        return sections;
    }

    public void requestNewCourseSection(){ }

    //Getters
    public abstract int getCoursePriority();
    public int getCourseSemester(){
        return 2 * firstYearToTake.getValue() + firstSeasonToTake.getValue();
    }
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

    public List<CourseSection> getCourseSections(){
        return courseSections;
    }
    public List<Section> getAllSections() {
        List<Section> allSections = new ArrayList<>(courseSections);
        allSections.addAll(labSections);
        return allSections;
    }
    public int getQuota() {
        return quota;
    }
}
