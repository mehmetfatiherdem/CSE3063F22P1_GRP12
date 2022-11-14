package iteration1.src;

import java.util.List;
import iteration1.src.course.Course;
import iteration1.src.course.CourseRecord;
import iteration1.src.course.LetterGrade;
import iteration1.src.course.Season;
import iteration1.src.human.Grade;


public class Transcript {
    private LetterGrade lGrade;
    private float GPA;
    private List<CourseRecord> takenCourseRecords;

    //TODO:add constructor and take course records as parameters as they'll be parsed from json data, and call calculateGPA inside the constructor
    public Transcript(List<CourseRecord> takenCourseRecords){
        this.takenCourseRecords = takenCourseRecords;

        calculateGPA();
    }

    //Getters
    public float getGPA() {
        return GPA;
    }

    public void addCourseRecord(Course course, LetterGrade lGrade, Season season, Float score, Grade grade, Boolean isPassed){
        
        CourseRecord courserecord = new CourseRecord(course,lGrade,season,grade, score, isPassed);
        takenCourseRecords.add(courserecord);

    }

    //TODO: Remove the course param
    public Float calculateGPA(){
        float gpa=0;
        int credits=0;
        float temp=0;
        for(CourseRecord r: takenCourseRecords){
            temp+=r.getlGrade().getNumVal()*r.getCourse().getCredits();
            credits+=r.getCourse().getCredits();
        }
        gpa=temp/credits;
        return gpa;
    }

    public int getCompletedCredits(){
        int completedCredits=0;
        
        for (CourseRecord r: takenCourseRecords){
            if(r.getIsPassed()){
                completedCredits += r.getCourse().getCredits();
            }
        }
        return completedCredits;
    }

    public Boolean checkIfPrerequisitesArePassed(Course course){

        for (Course c : course.getPrerequisites()){
            if(!didStudentPass(course)){
                return false;
            }
        }

        return true;
    }

    private Boolean didStudentPass(Course course){

        Boolean passed = true;

        CourseRecord mostRecent = null;
        for (CourseRecord record : takenCourseRecords){
            if(record.getCourse() == course){
                mostRecent = record;
            }
        }

        return mostRecent.getIsPassed();
    }


    public List<CourseRecord> getTakenCourseRecords(){
        return takenCourseRecords;
    }
}
