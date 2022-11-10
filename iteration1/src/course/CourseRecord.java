package iteration1.src.course;

public class CourseRecord {
    //DataFields
    private enum LetterGrade{
        AA,BA,BB,CB,CC,DC,DD,FD,FF,ZZ
    }
    private enum Season{
        FALL,SPRING,SUMMER
    }
    private Course course;
    private LetterGrade lGrade;
    private Season season;
    private float grade;
    private int year;
    private Boolean isPassed;

    //Getters
    public Course getCourse() {
        return course;
    }
    public float getGrade() {
        return grade;
    }
    public int getYear() {
        return year;
    }
    public Boolean getIsPassed() {
        return isPassed;
    }
    public Season getSeason() {
        return season;
    }
    public LetterGrade getlGrade() {
        return lGrade;
    }
    

    //Setters
    public void setCourse(Course course) {
        this.course = course;
    }
    public void setGrade(float grade) {
        this.grade = grade;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setIsPassed(Boolean isPassed) {
        this.isPassed = isPassed;
    }
    public void setlGrade(LetterGrade lGrade) {
        this.lGrade = lGrade;
    }
    public void setSeason(Season season) {
        this.season = season;
    }

    //Get season details Method
    public void getFullSeasonDetails(){

    }
    

}
