package iteration1.src;

import iteration1.src.course.Course;
import iteration1.src.human.Human;
import iteration1.src.human.Student;

public class Test {
    public static void main(String[] args) {
        departmentTest();
        humanTest();
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
}
