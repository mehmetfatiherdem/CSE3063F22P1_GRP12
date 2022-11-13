package iteration1.src;

import java.util.List;
import iteration1.src.course.Course;
import iteration1.src.course.CourseRecord;
import iteration1.src.course.LetterGrade;
import iteration1.src.course.Season;


public class Transcript {
    private LetterGrade lGrade;
    private float GPA;
    private List<CourseRecord> takenCourseRecords;

 
    public float getGPA() {
        return GPA;
    }
    public List<CourseRecord> getTakenCourseRecords() {
        return takenCourseRecords;
    }

    
    public void addCourseRecord(Course course,LetterGrade lGrade,Season season,Float grade,int year,Boolean isPassed){
        
        CourseRecord courserecord = new CourseRecord(course,lGrade,season,grade,year,isPassed);
        takenCourseRecords.add(courserecord);

    }

    public Float calculateGPA(Course course){
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
}
