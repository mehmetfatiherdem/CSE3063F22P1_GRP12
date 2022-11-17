package iteration1.src;
import iteration1.src.course.*;
import iteration1.src.human.*;
import iteration1.src.human.Advisor;
import iteration1.src.human.Grade;
import iteration1.src.course.CourseRecord;
import java.util.List;
import java.util.ArrayList;
import iteration1.src.course.Course;
import iteration1.src.human.Human;
import iteration1.src.human.Student;

public class Test {
    public static void main(String[] args) {

        //departmentTest();
        humanTest();
        CourseTest();
    }
    public static void humanTest(){
        // Tests about student class
        List<CourseRecord> records = new ArrayList<>();
        records.add(new CourseRecord(new MandatoryCourse("CSE 2025","Data Structures",8,3,2,Grade.JUNIOR,Season.FALL),LetterGrade.AA,Season.FALL,Grade.FRESHMAN,90F,Boolean.TRUE));
        Human student = new Student("Ahmet","Şahin ","150115655", Grade.FRESHMAN , new Advisor("Mustafa","Ağaoğlu"),records);

        System.out.println();
        System.out.println("Human test starting...");
        System.out.println();

        System.out.println("Student name: " + student.getFullName());
        System.out.println("Student ID: "+ ((Student) student).getStudentID());
        System.out.println("Student Advisor: "+ ((Student) student).getAdvisor().getFullName());
        System.out.println("Student Grade: " + ((Student) student).getGrade());

        for(var r:records){
            System.out.println();
            System.out.println("Information about the course taken by the student:");
            System.out.println();

            System.out.print("Course Code: ");
            System.out.println(r.getCourse().getCode());

            System.out.print("Course name: ");
            System.out.println(r.getCourse().getName());

            System.out.print("Number of credits of the course: ");
            System.out.println(r.getCourse().getCredits());

            System.out.print("Number of theoretical hours of the course: ");
            System.out.println(r.getCourse().getTheoreticalHours());

            System.out.print("Number of Applied hours of the course: ");
            System.out.println(r.getCourse().getAppliedHours());

            System.out.print("Grade of course: ");
            System.out.println(r.getCourse().getFirstYearToTake());

            System.out.print("Season of course: ");
            System.out.println(r.getCourse().getFirstSeasonToTake());

            System.out.println();

            System.out.println("Information about the student notes: ");
            System.out.println();

            System.out.print("Letter Grade: ");
            System.out.println(r.getlGrade());

            System.out.print("Note Season: ");
            System.out.println(r.getSeason());

            System.out.print("Year: ");
            System.out.println(r.getGrade());

            System.out.print("Score: ");
            System.out.println(r.getScore());

            System.out.print("Passed: ");
            System.out.println(r.getIsPassed());
        }
    }
    public static void CourseTest(){
        System.out.println();
        System.out.println("Course Test Starting...");
        System.out.println("");

        Course course = new MandatoryCourse("cse3063","Object Orianted Software Design",5,3,0,Grade.SENIOR, Season.FALL);
        System.out.print(" Course code:");
        System.out.println(course.getCode());
        System.out.print(" Course Name:");
        System.out.println(course.getName());
        System.out.print(" Course Credits:");
        System.out.println(course.getCredits());
        System.out.print(" Course Theoretical Hours:");
        System.out.println(course.getTheoreticalHours());
        System.out.print(" Course Applied Hours:");
        System.out.println(course.getAppliedHours());
        System.out.print(" Course First Year to take:");
        System.out.println(" "+course.getFirstYearToTake());
        System.out.print(" Course First Season to take:");
        System.out.println(" "+course.getFirstSeasonToTake());
    }
}

