package iteration1.src.course;
import iteration1.src.Department;
import iteration1.src.human.Assistant;
import iteration1.src.human.Grade;
import iteration1.src.human.Lecturer;
import iteration1.src.human.Student;
import iteration1.src.input_output.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    //TODO:Remove education season
    protected List<Season> educationSeason;
    private List<Section> sectionList = new ArrayList<>();

    public Course(String code, String name, int credits, int theoreticalHours
            , int appliedHours, Grade firstYearToTake,Season firstSeasonToTake){
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.theoreticalHours = theoreticalHours;
        this.appliedHours = appliedHours;
        this.firstYearToTake = firstYearToTake;
        this.firstSeasonToTake = firstSeasonToTake;

        Random rng = new Random();
        this.quota = rng.nextInt(maxQuota - minQuota + 1) + minQuota - 1;

        //Each and every semester, at least one section of all mandatory courses should be registerable without any collision
    }

    public Boolean isAnyCourseSectionAvailable(){
        for(Section s: sectionList){
            if(s instanceof CourseSection && !s.isSectionFull()){
                return  true;
            }
        }

        return false;
    }
    public  Boolean isAnyLabSectionAvailable(){
        for(Section s:sectionList){
            if(s instanceof LabSection && !s.isSectionFull())
                return true;
        }

        return false;
    }
    public void addPrerequisite(Course prerequisite){
        prerequisites.add(prerequisite);
    }
    public void addToLecturer(Lecturer lecturer) {
        this.lecturers.add(lecturer);
    }
    public void addToAssistants(Assistant assistant) {
        this.assistants.add(assistant);
    }
    public void addToEducationSeason(Season educationSeason) {
        this.educationSeason.add(educationSeason);
    }
    public void addToSectionList(LabSection section){
        this.sectionList.add(section);
    }
    public void addToSectionList(CourseSection section) { this.sectionList.add(section); }
    public Boolean canStudentTakeCourse(Student student){
        if(student.getGrade().getValue() < firstYearToTake.getValue()
                || (student.getGrade().getValue() == firstYearToTake.getValue()
                && Department.getInstance().getCurrentSemester().getValue() < firstSeasonToTake.getValue())){
            //TODO: add a log
            return false;
        }

        return true;
    }


    //Getters
    public List<CourseSection> getAvailableCourseSections(){
        List<CourseSection> sections = new ArrayList<>();

        for (Section s: sectionList){
            if(s instanceof CourseSection && !s.isSectionFull()){
                sections.add((CourseSection) s);
            }
        }

        return sections;
    }
    public List<LabSection> getAvailableLabSections(){
        List<LabSection> sections = new ArrayList<>();

        for(Section s: sectionList){
            if(s instanceof LabSection && !s.isSectionFull()){
                sections.add((LabSection) s);
            }
        }

        return sections;
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
    public List<Season> getEducationSeason() {
        return educationSeason;
    }
    public List<Section> getSectionList() {
        return sectionList;
    }
    public int getQuota() {
        return quota;
    }
}
