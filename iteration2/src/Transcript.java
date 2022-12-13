package iteration2.src;

import java.util.ArrayList;
import java.util.List;

import iteration2.src.course.*;
import iteration2.src.human.Grade;


public class Transcript {
    private List<CourseRecord> takenCourseRecords;
    //GPA FALAN BASTIRILICAK
    //TODO:add constructor and take course records as parameters as they'll be parsed from json data, and call calculateGPA inside the constructor
    public Transcript(List<CourseRecord> takenCourseRecords){
        this.takenCourseRecords = takenCourseRecords;

        calculateGPA();
    }

    public void addCourseRecord(Course course, LetterGrade lGrade, Season season, Float score, Grade grade, Boolean isPassed){
        
        CourseRecord courserecord = new CourseRecord(course,lGrade,season,grade, score, isPassed);
        takenCourseRecords.add(courserecord);

    }


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

    public Boolean didStudentPass(Course course){

        Boolean passed = true;

        CourseRecord mostRecent = null;
        for (CourseRecord record : takenCourseRecords){
            if(record.getCourse() == course){
                mostRecent = record;
            }
        }

        if(mostRecent == null)
            return false;

        return mostRecent.getIsPassed();
    }

    public List<CourseRecord> getTakenCourseRecords(){
        return takenCourseRecords;
    }

    public int getNumberOfNTElectivesPassed(){
        int i=0;
        for(CourseRecord r:takenCourseRecords){
            Course course = r.getCourse();
            if(course instanceof NonTechnicalElectiveCourse && didStudentPass(course)){
                i++;
            }
        }

        return i;
    }
    public List<CourseRecord> getNonGradedCourses(){
        List<CourseRecord> courseRecordList = new ArrayList<>();
        for(CourseRecord r:takenCourseRecords){
            if(r.getlGrade() == LetterGrade.NOT_GRADED){
             courseRecordList.add(r);
            }
        }
        return courseRecordList;
    }

    public static LetterGrade getLetterGradeOfScore(float score) {
        if(score >= 0.0f && score < 35.0f){return LetterGrade.FF;}
        else if(score>=35.0f && score < 40.0f ){return  LetterGrade.FD;}
        else if(score>=40.0f && score < 50.0f){return  LetterGrade.DD;}
        else if(score>=50.0f && score < 65.0f){return  LetterGrade.DC;}
        else if(score>=65.0f && score < 75.0f){return  LetterGrade.CC;}
        else if(score>=75.0f && score < 80.0f){return  LetterGrade.CB;}
        else if(score>=80.0f && score < 85.0f){return  LetterGrade.BB;}
        else if(score>=85.0f && score < 90.0f){return  LetterGrade.BA;}
        else{return  LetterGrade.AA;}
    }
    public int getNumberOfTElectivesPassed(){
        int i=0;
        for(CourseRecord r:takenCourseRecords){
            Course course = r.getCourse();
            if(course instanceof TechnicalElectiveCourse && didStudentPass(course)){
                i++;
            }
        }
        return i;
    }
    public int getNumberOfFTElectivesPassed(){
        int i=0;
        for(CourseRecord r:takenCourseRecords){
            Course course = r.getCourse();
            if(course instanceof FacultyTechnicalElectiveCourse && didStudentPass(course)){
                i++;
            }
        }
        return i;
    }
    public boolean didStudentFailBefore(Course course){
        boolean didFail=false;

        for(CourseRecord r:takenCourseRecords) {
            if(r.getCourse()==course)
                didFail=!r.getIsPassed();
        }
        return didFail;
    }
}
