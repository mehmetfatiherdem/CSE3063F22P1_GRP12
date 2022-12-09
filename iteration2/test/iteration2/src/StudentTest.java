package iteration2.src;

import iteration2.src.course.*;
import iteration2.src.human.*;
import iteration2.src.input_output.Logger;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    List<CourseRecord> records = new ArrayList<>();
    Human student = new Student("Ahmet","Şahin ","150115655", Grade.FRESHMAN , new Advisor("Mustafa","Ağaoğlu"),records);
    Transcript transcript = new Transcript(records);
    List<Section> courseSection = new ArrayList<>();

    @BeforeEach
    void setup(){
        records.add(new CourseRecord(new MandatoryCourse("CSE 2025","Data Structures",8,3,2,Grade.JUNIOR,Season.FALL),LetterGrade.AA,Season.FALL,Grade.FRESHMAN,90F,Boolean.TRUE));
        records.add(new CourseRecord(new MandatoryCourse("MATH2055", "Differential Equations", 4, 3, 0,Grade.JUNIOR,Season.FALL),LetterGrade.AA,Season.FALL,Grade.FRESHMAN,90F,Boolean.TRUE));
        courseSection.add(new CourseSection(new MandatoryCourse("MATH2055", "Differential Equations", 4, 3, 0,Grade.JUNIOR,Season.FALL),3,new Lecturer("Borahan","Tümer")));
        ((Student)student).getEnrolledCourses().add(courseSection.get(0));

        System.out.println();
        System.out.println("Student test starting...");
        System.out.println();
    }

    @Test
    void checkIfPrerequisitesArePassed() {
        assertEquals(4, Calculator.add(2, 2));

    }

    @Test
    void didStudentPass() {
        for (var record:records) {
            assertEquals(true,record.getIsPassed());
        }
    }

    @Test
    void enrollCourseSections() {
        for(Section s: courseSection){
            s.addToStudentList((Student)student);

            transcript.addCourseRecord(s.getCourse(), LetterGrade.NOT_GRADED, Season.FALL, null, ((Student)student).getGrade(), false);
        }
        assertEquals(student, courseSection.get(0).getStudentList().get(0));
    }
    @Test
    void addToRegistrationList() {
        if(courseSection.get(0).isSectionFull()){

            Logger.log("This section of " + (courseSection).get(0).getCourse().getCode() + " is already full");
            return;
        }
        ((Student)student).getEnrolledCourses().add(courseSection.get(0));
        assertEquals(courseSection.get(0), ((Student)student).getEnrolledCourses().get(1));

    }

    @Test
    void register() {
        // will be added after refactoring
    }

    @Test
    void generateWeeklySchedule() {
        String program = ((Student)student).getFullName() + "'s Weekly Schedule\n";

        if(((Student)student).getEnrolledCourses().size() == 0){
            program = "The student " + student.getFullName() + " doesn't have any enrolled courses";
        }

        List<Section[]> schedule = Section.combineSchedules(((Student)student).getEnrolledCourses());

        for(int i = 0; i<schedule.size(); i++){

            program += Section.CLASS_DAYS[i] + ": ";

            Section[] day = schedule.get(i);

            for(int j = 0; j<schedule.get(0).length; j++){

                Section sec = day[j];

                if(sec == null)
                    continue;


                program += sec.getCourse().getCode();

                program += "(" + Section.CLASS_HOURS[j%8] + ") ";

            }
            program += "\n";
        }
        assertEquals(program,"Ahmet Şahin 's Weekly Schedule\nMonday: MATH2055(8.30-9.20) MATH2055(9.30-10.20) \nTuesday: \nWednesday: \nThursday: \nFriday: \nSaturday: \nSunday: \n");
    }


    @Test
    void getStudentID() {
        assertEquals("150115655", ((Student) student).getStudentID());

    }

    @Test
    void getGrade() {
        assertEquals(Grade.FRESHMAN,((Student) student).getGrade());
    }

    @Test
    void getAdvisor() {
        assertEquals(("Mustafa Ağaoğlu"), ((Student) student).getAdvisor().getFullName());
    }
    @Test
    void getCourseCode(){
        for (var record:records) {
            assertEquals("CSE 2025",record.getCourse().getCode());
        }
    }
    @Test
    void getCourseName(){
        for (var record:records) {
            assertEquals("Data Structures",record.getCourse().getName());
        }
    }
    @Test
    void getCourseCredit(){
        for (var record:records) {
            assertEquals(8,record.getCourse().getCredits());
        }
    }
    @Test
    void getTheoreticalHours(){
        for (var record:records) {
            assertEquals(3,record.getCourse().getTheoreticalHours());
        }
    }
    @Test
    void getAppliedHours(){
        for (var record:records) {
            assertEquals(2,record.getCourse().getAppliedHours());
        }
    }
    @Test
    void getFirstYearToTake(){
        for (var record:records) {
            assertEquals(Grade.JUNIOR,record.getCourse().getFirstYearToTake());
        }
    }
    @Test
    void getFirstSeasonToTake(){
        for (var record:records) {
            assertEquals(Season.FALL,record.getCourse().getFirstSeasonToTake());
        }
    }
    @Test
    void getlGrade(){
        for (var record:records) {
            assertEquals(LetterGrade.AA,record.getlGrade());
        }
    }
    @Test
    void getSeason(){
        for (var record:records) {
            assertEquals(Season.FALL,record.getSeason());
        }
    }
    @Test
    void getScore(){
        for (var record:records) {
            assertEquals(90F,record.getScore());
        }
    }
    @Test
    void getIsPassed(){
        for (var record:records) {
            assertEquals(true,record.getIsPassed());
        }
    }

    @Test
    void getCompletedCredits() {
        int completedCredits=0;
        for (CourseRecord r: records){
            if(r.getIsPassed()){
                completedCredits += r.getCourse().getCredits();
            }
        }
        assertEquals(12,completedCredits);

    }

    @Test
    void getTranscript() {
        //Since I created a new transcript object, I compared the inside of transcripts
        assertSame(transcript.getTakenCourseRecords().get(0),((Student)student).getTranscript().getTakenCourseRecords().get(0));
    }

}