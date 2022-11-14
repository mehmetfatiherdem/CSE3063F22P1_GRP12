package iteration1.src;
import iteration1.src.course.Course;
import iteration1.src.course.MandatoryCourse;
import iteration1.src.human.Assistant;
import iteration1.src.human.Lecturer;
import iteration1.src.human.Student;
import iteration1.src.human.Human;


public class Test {
    public static void main(String[] args) {
        System.out.println("This is testing");
        humanTest();
    }
    public static void humanTest(){
        System.out.println("Trying to call the constructor without middle name");
        System.out.println("");
        Human student = new Student("Ahmet","Şahin");
        System.out.println("Trying to call full name");
        System.out.println(student.getFullName());

    }
    public static void CourseTest(){
        System.out.println("Trying to set course code as abc");
        System.out.println("");
        Course course = new MandatoryCourse("cse3034",70);
        System.out.println("Trying to call a course code");
        System.out.println(course.getCode());




    }




}

