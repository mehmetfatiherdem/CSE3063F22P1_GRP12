package iteration1.src.course;
import iteration1.src.human.Assistant;
import iteration1.src.human.Lecturer;
import iteration1.src.human.Student;
import java.util.List;

public abstract class Course {

    protected String code;
    protected int credits;
    protected int ects;
    protected int quota;
    protected int theoreticalHours;
    protected int appliedHours;
    protected int firstYearToTake;
    protected Season firstSeasonToTake;
    protected List<Lecturer> lecturers;
    protected List<Assistant> assistants;
    protected List<Season> educationSeason;
    protected List<Section> studentList;

    //Getters
    public int getCredits() {
        return credits;
    }
    public String getCode() {
        return code;
    }
    public int getQuota() {
        return quota;
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
    public List<Section> getStudentList() {
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
    public void setQuota(int quota) {
        //mandatory course and elective course have different rule about quota
        this.quota = quota;
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
    public void addToLecturer(List<Lecturer> lecturers) {
            this.lecturers = lecturers;
    }
    public void addToAssistants(List<Assistant> assistants) {
        this.assistants = assistants;
    }
    public void addToEducationSeason(List<Season> educationSeason) {
        this.educationSeason = educationSeason;
    }

    public void addToStudentList(List<Section> studentList) {
        this.studentList = studentList;
    }

    public  Boolean canStudentTakeCourse(Student student){
        return null;
    }
}
