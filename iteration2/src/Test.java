package iteration2.src;
import iteration2.src.course.*;
import iteration2.src.human.*;
import iteration2.src.human.Advisor;
import iteration2.src.human.Grade;
import iteration2.src.course.CourseRecord;
import java.util.List;
import java.util.ArrayList;
import iteration2.src.course.Course;
import iteration2.src.human.Human;
import iteration2.src.human.Student;

public class Test {
    public static void main(String[] args) {

        departmentTest();
        humanTest();
        CourseTest();
    }
    public static void departmentTest(){
        // Tests about department class
        System.out.println();
        System.out.println("Starting department test");
        System.out.println();


        List<CourseRecord> records = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<Lecturer> lecturers = new ArrayList<>();
        List<Assistant> assistants = new ArrayList<>();
        List<Advisor> advisors = new ArrayList<>();
        List<Student> students = new ArrayList<>();

        Department department = Department.getInstance();
        department.initialize(Season.FALL,courses,lecturers,assistants,advisors,students);

        records.add(new CourseRecord(new MandatoryCourse("CSE 4074","Computer Networks",5,3,0,Grade.SENIOR,Season.FALL),LetterGrade.AA,Season.FALL,Grade.FRESHMAN,90F,Boolean.TRUE));
        courses.add(new MandatoryCourse("CSE 4074","Computer Networks",5,3,0,Grade.SENIOR,Season.FALL));
        lecturers.add(new Lecturer("Ömer","Korçak"));
        assistants.add(new Assistant("No","Assistant"));
        advisors.add(new Advisor("Mesut","Yılmaz"));
        students.add(new Student("Mehmet","Erdem ","150115655", Grade.FRESHMAN , new Advisor("Mustafa","Ağaoğlu"),records));

        for(var r:courses){

            System.out.println("Information about Department");

            System.out.print("Course Code: ");
            System.out.println(r.getCode());

            System.out.print("Course Name: ");
            System.out.println(r.getName());

            System.out.print("Course Credits: ");
            System.out.println(r.getCredits());

            System.out.print("Number of theoretical hours of the course: ");
            System.out.println(r.getTheoreticalHours());

            System.out.print("Number of Applied hours of the course: ");
            System.out.println(r.getAppliedHours());

            System.out.print("Grade of course: ");
            System.out.println(r.getFirstYearToTake());

            System.out.print("Season of course: ");
            System.out.println(r.getFirstSeasonToTake());
        }
        for(var r:lecturers){
            System.out.println();
            System.out.println("Information About Lecturers");
            System.out.println();

            System.out.print("Lecturer Name and Surname: ");
            System.out.println(r.getFullName());

        }
        for(var r:assistants){
            System.out.println();
            System.out.println("Information About Assistant");
            System.out.println();

            System.out.print("Assistant Name and Surname: ");
            System.out.println(r.getFullName());
        }
        for(var r:advisors){
            System.out.println();
            System.out.println("Information About Advisor");
            System.out.println();

            System.out.print("Advisor Name and Surname: ");
            System.out.println(r.getFullName());
        }
        for(var r:students){
            System.out.println();
            System.out.println("Information About Students");
            System.out.println();

            System.out.print("Student Name and Surname: ");
            System.out.println(r.getFullName());

            System.out.print("Student ID: ");
            System.out.println(r.getStudentID());

            System.out.print("Student Grade: ");
            System.out.println(r.getGrade());

            System.out.print("Student Name and Surname: ");
            System.out.println(r.getFullName());

            System.out.print("Student Advisor: ");
            System.out.println(r.getAdvisor().getFullName());
        }
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
        System.out.println();

        Course course = new MandatoryCourse("cse3063","Object Oriented Software Design",5,3,0,Grade.SENIOR, Season.FALL);
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

