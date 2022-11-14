package iteration1.src;
import iteration1.src.course.Course;
import iteration1.src.course.MandatoryCourse;
import iteration1.src.human.Assistant;
import iteration1.src.human.Lecturer;
import iteration1.src.human.Student;
import iteration1.src.human.Human;


import iteration1.src.course.Course;
import iteration1.src.human.Human;
import iteration1.src.human.Student;

public class Test {
    public static void main(String[] args) {

        departmentTest();
        humanTest();
        CourseTest();
    }


    public static void departmentTest(){
        System.out.println("Trying to set department code as abc");
        Department.getInstance().setCode("Department code is set to " + "abc");
        System.out.println();

        System.out.println("Trying to set department code as absdsadsa");
        Department.getInstance().setCode("absdsadsa");
        System.out.println("Department code is set to " + Department.getInstance().getCode());
        System.out.println();

        System.out.println("Trying to set department code as ' '");
        Department.getInstance().setCode(" ");
        System.out.println();

    }
    public static void humanTest(){
        // Tests about student class
        System.out.println("Calling the constructor of student class without middle name");
        Human student = new Student("Ahmet","Şahin");
        System.out.println("Name of the student is " + student.getFullName());
        System.out.println();

        System.out.println("Calling the constructor of student class with full name");
        Human student2 = new Student("Mehmet","Kemal","Demir");
        System.out.println("Name of the student is " + student2.getFullName());
        System.out.println();

        System.out.println("Trying to set the first name to blank");
        student2.setFirstName("");
        System.out.println();

        System.out.println("Trying to set the last name to blank");
        student2.setLastName("");
        System.out.println();

        System.out.println("Trying to call the constructor of student class without name");
        Human student3 = new Student("","");
        System.out.println("Name of the student is " + student3.getFullName());
        System.out.println();

    }
    public static void CourseTest(){
        System.out.println("Trying to set course code as abc");
        System.out.println("");
        Course course = new MandatoryCourse("cse3034",70);
        System.out.println("Trying to call a course code");
        System.out.println(course.getQuota());




    }




}

