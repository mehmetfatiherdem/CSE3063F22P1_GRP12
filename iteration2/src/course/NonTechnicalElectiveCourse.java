package iteration2.src.course;

import iteration2.src.Department;
import iteration2.src.human.Assistant;
import iteration2.src.human.Grade;
import iteration2.src.human.Lecturer;
import iteration2.src.human.Student;
import iteration2.src.input_output.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NonTechnicalElectiveCourse extends ElectiveCourse{

    public static final Map<Integer, Integer> numberOfCoursesTakeableBySemester = new HashMap<>(){{
        put(0,0);
        put(1, 1);
        put(2,0);
        put(3,0);
        put(4,0);
        put(5,0);
        put(6,1);
        put(7,1);
    }};


    public NonTechnicalElectiveCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                                          Grade firstYearToTake, Season firstSeasonToTake, List<Lecturer> lecturers, List<Assistant> assistants){
            super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake,lecturers,assistants);
        }

    @Override
    public Boolean canStudentTakeCourse(Student student) {
        int semester = student.getStudentSemester();
        int noOfCoursesTillSemester = getTotalNumberOfCoursesUntilSemester(semester);

        boolean canTake = student.getTranscript().getNumberOfNTElectivesPassed() < noOfCoursesTillSemester;
        canTake &= !student.didStudentPass(this);
        return canTake;
    }

    public static int getTotalNumberOfCoursesUntilSemester(int semester){
        int noOfCoursesTillSemester = 0;

        for(int i = 0; i <= semester; i++){
            noOfCoursesTillSemester += numberOfCoursesTakeableBySemester.get(i).intValue();
        }

        return noOfCoursesTillSemester;
    }

    public int getCoursePriority(){
        return 0;
    }
}
