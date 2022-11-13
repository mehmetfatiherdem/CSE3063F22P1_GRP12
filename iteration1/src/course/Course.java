package iteration1.src.course;
import iteration1.src.human.Assistant;
import iteration1.src.human.Lecturer;
import iteration1.src.human.Student;

import java.util.ArrayList;
import java.util.List;

public abstract class Course {

    protected String code;
    protected int credits;
    protected int ects;
    protected int theoreticalHours;
    protected int appliedHours;
    protected int firstYearToTake;
    protected Season firstSeasonToTake;
    protected List<Lecturer> lecturers = new ArrayList<>();
    protected List<Assistant> assistants = new ArrayList<>();
    protected List<Season> educationSeason;
    protected List<Student> studentList = new ArrayList<>();
    private List<Section> sectionList = new ArrayList<>();

    public Course(String code){
        this.code = code;
    }

    //Getters
    public int getCredits() {
        return credits;
    }
    public String getCode() {
        return code;
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
    public int getFirstYearToTake() {
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
    public List<Student> getStudentList() {
        return studentList;
    }

    //Setters
    public void setCode(String code) {
        //The course code has at least 1 letter, 3 digit, 1 dot and 1 more digit.(MB302.6)
        if(code.length() < 6){
            System.out.println("Invalid course code.");
        }
        else{
            this.code = code;
        }
    }
    public void setCredits(int credits) {
        //Course's credit can not be negative. YDI0001 has no credit.
        if(credits < 0){
            System.out.println("Invalid course credits value");
        }
        else{
            this.credits = credits;
        }
    }

    public void setEcts(int ects) {
        if(ects<0){
            System.out.println("Invalid course ECTS value");
        }
        else{
            this.ects = ects;
        }
    }
    public void setTheoreticalHours(int theoreticalHours) {
        if(theoreticalHours < 0){
            System.out.println("Invalid course theoretical hours value");
        }
        else{
            this.theoreticalHours = theoreticalHours;
        }
    }
    public void setAppliedHours(int appliedHours) {
        if(appliedHours < 0){
            System.out.println("Invalid course applied hours value");
        }
        else{
            this.appliedHours = appliedHours;
        }
    }
    public void setFirstYearToTake(int firstYearToTake) {
        //A student must complete entire university courses in 7 years. Students have right to defer enrolment for 4 season.
        if(firstYearToTake<2013){
            System.out.println("Invalid course firstYearToTake value.");
        }
        else{
            this.firstYearToTake = firstYearToTake;
        }
    }
    public void setFirstSeasonToTake(Season firstSeasonToTake) {
        this.firstSeasonToTake = firstSeasonToTake;
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

    public void addToStudentList(Student student) {
        this.studentList.add(student);
    }

    public  Boolean canStudentTakeCourse(Student student){
        return null;
    }


    public void addToSectionList(Section section){
        this.sectionList.add(section);
    }
}
