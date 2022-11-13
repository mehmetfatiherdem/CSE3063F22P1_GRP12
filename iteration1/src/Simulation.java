package iteration1.src;

import iteration1.src.course.*;
import iteration1.src.human.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    //TODO:Delete test methods when u done with them
    public static void main(String[] args) {

        runSimulation(init());
    }

    public static void runSimulation(List<Student> students){

        // 0000000000000000000000000000001000000110
        Course c1 = new MandatoryCourse("CSE3063", 1);
        // 0000000000000000000000100000110011000000
        Course c2 = new MandatoryCourse("CSE3015", 2);
        // 0000110000000000000000000000000000111000
        Course c3 = new MandatoryCourse("CSE3033", 2);
        // 0000000000001000000011001100000000000000
        Course c4 = new MandatoryCourse("CSE3055", 2);
        // 0000001000000110000000000000000000000000
        Course c5 = new MandatoryCourse("IE3081", 2);
        // 0000000000000000001000000000000000000000
        Course c6 = new ElectiveCourse("GER2022", 2);

        Section sec1 = new CourseSection(c1,
                518l, new Lecturer("n1", "l1"));
        Section sec2 = new CourseSection(c2,
                134336l, new Lecturer("n2", "l2"));
        Section sec3 = new CourseSection(c3,
                51539607608l, new Lecturer("n3", "l3"));
        Section sec4 = new CourseSection(c4,
                135053312l, new Lecturer("n4", "l4"));
        Section sec5 = new CourseSection(c5,
                8690597888l, new Lecturer("n5", "l5"));
        Section sec6= new CourseSection(c6,
                2097152l, new Lecturer("n6", "l6"));




        sec1.setCourse(c1);
        sec2.setCourse(c2);
        sec3.setCourse(c3);
        sec4.setCourse(c4);
        sec5.setCourse(c5);
        sec6.setCourse(c6);

        c1.addToSectionList(sec1);
        c2.addToSectionList(sec2);
        c3.addToSectionList(sec3);
        c4.addToSectionList(sec4);
        c5.addToSectionList(sec5);
        c6.addToSectionList(sec6);

        List<Section> sectionList = new ArrayList<>();

        //her course un ctor ında ilk sectionlar oluşturulup assign edilmeli/sectionlar hazır verilmeli ctor a
        sectionList.add(sec1);
        sectionList.add(sec2);
        sectionList.add(sec3);
        sectionList.add(sec4);
        sectionList.add(sec5);
        sectionList.add(sec6);

        RegistrationData data = new RegistrationData(1, Season.FALL, sectionList); //this could be changed (logic moved to department)

        Student st1 = students.get(0);
        Student st2 = students.get(1);

        st1.addToEnrolledCourseSections(sec1);
        st1.addToEnrolledCourseSections(sec2);
        st1.addToEnrolledCourseSections(sec3);
        st1.addToEnrolledCourseSections(sec4);
        st1.addToEnrolledCourseSections(sec5);
        st1.addToEnrolledCourseSections(sec6);

        st1.register(data);

        st2.addToEnrolledCourseSections(sec1);
        st2.addToEnrolledCourseSections(sec2);
        st2.addToEnrolledCourseSections(sec3);
        st2.addToEnrolledCourseSections(sec4);
        st2.addToEnrolledCourseSections(sec5);
        st2.addToEnrolledCourseSections(sec6);

        st2.register(data);

    }

    public static List<Student> init(){

        List<Student> l = new ArrayList<>();

        Student s1 = new Student("John", "Dogan");

        Student s2 = new Student("Tunahan", "Bas");

        l.add(s1);
        l.add(s2);

        return l;
    }
}
