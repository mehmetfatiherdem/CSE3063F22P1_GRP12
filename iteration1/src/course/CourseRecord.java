package iteration1.src.course;


public class CourseRecord {
    //DataFields
    private Course course;
    private LetterGrade lGrade;
    private Season season;
    private Float grade;
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
       if(grade < 0){
       // print error messages
       return;
       }
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

    public CourseRecord(Course course,LetterGrade lGrade,Season season,Float grade,int year,Boolean isPassed){
        this.course=course;
        this.lGrade=lGrade;
        this.season=season;
        this.grade=grade;
        this.year=year;
        this.isPassed=isPassed;

    }
    //Get season details Method
    public void getFullSeasonDetails(){

    }
    

}
