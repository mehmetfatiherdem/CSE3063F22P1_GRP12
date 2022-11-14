package iteration1.src.course;


import iteration1.src.human.Grade;

public class CourseRecord {
    //DataFields
    private Course course;
    private LetterGrade lGrade;
    private Season season;
    private Float score;
    private Grade grade;
    private Boolean isPassed;

    //Getters
    public Course getCourse() {
        return course;
    }
    public float getScore() {
        return score;
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
    public Grade getGrade() {
        return grade;
    }
    

    //Setters
    public void setCourse(Course course) {
        this.course = course;
    }
    public void setScore(float score) {
       if(score < 0){
       // print error messages
       return;
       }
        this.score = score;
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


    public CourseRecord(Course course, LetterGrade lGrade, Season season, Grade grade, Float score, Boolean isPassed){
        this.course=course;
        this.lGrade=lGrade;
        this.season=season;
        this.grade = grade;
        this.score = score;
        this.isPassed=isPassed;

    }
    //Get season details Method
    public void getFullSeasonDetails(){

    }
    

}
