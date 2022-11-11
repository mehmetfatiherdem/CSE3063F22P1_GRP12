package iteration1.src;

import java.util.ArrayList;
import java.util.List;

import iteration1.src.course.Course;
import iteration1.src.course.CourseRecord;

public class Transcript {
    private float GPA;
    private List<CourseRecord> takenCourseRecords;
    public enum Status{
        FAILED,PASSED
    }

    //getters
    public float getGPA() {
        return GPA;
    }
    public List<CourseRecord> getTakenCourseRecords() {
        return takenCourseRecords;
    }

    //setters
    public void setGPA(float GPA) {
        if(GPA < 0 || GPA > 4){
        // print an error here to the console
        return;
        }
        this.GPA = GPA;
    }
    

    public String setCourseStatus(Course course,Status status){
        
        return "Not implemented";
    }
    public int getCompletedCredits(){
        // Not implemented yet.
        return 0;
    }
}
