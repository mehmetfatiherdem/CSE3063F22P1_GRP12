package iteration1.src.course;
import iteration1.src.human.Lecturer;
import iteration1.src.human.Student;
import java.util.List;

abstract class Course {
    enum Season{
        FALL,
        SPRING,
        SUMMER
    }
    protected String code;
    protected int credits;
    protected int ects;
    //protected int quote;
    protected int theoreticalHours;
    protected int appliedHours;
    protected int firstYearToTake;
    protected Season firstSeasonToTake;
    protected List<Lecturer> lecturer;
    protected List<Lecturer> assistants;
    protected List<Season> educationSeason;
    protected List<Student> studentList;
    //protected List<Section> studentList;




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
    public List<Lecturer> getLecturer() {
        return lecturer;
    }
    public Season getFirstSeasonToTake() {
        return firstSeasonToTake;
    }
    public List<Lecturer> getAssistants() {
        return assistants;
    }
    public List<Season> getEducationSeason() {
        return educationSeason;
    }
    public List<Student> getStudentList() {
        return studentList;
    }
    /* public int getQuote() {
        return quote;
    }*/

    //Setters
    public void setCode(String code) {
    }
    public void setCredits(int credits) {
    }
    public void setEcts(int ects) {
    }
    public void setTheoreticalHours(int theoreticalHours) {
    }
    public void setAppliedHours(int appliedHours) {
    }
    public void setFirstYearToTake(int firstYearToTake) {
    }
    public void setFirstSeasonToTake(Season firstSeasonToTake) {
        this.firstSeasonToTake = firstSeasonToTake;
    }
    public void setLecturer(List<Lecturer> lecturer) {
    }
    public void setAssistants(List<Lecturer> assistants) {
        this.assistants = assistants;
    }
    public void setEducationSeason(List<Season> educationSeason) {
        this.educationSeason = educationSeason;
    }
    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
    /*
    public void setQuote(int quote) {
        this.quote = quote;
    }
    */
    //canStudentTakeCourse method should be implemented in subclasses
    public abstract Boolean canStudentTakeCourse(Student student);
}
