package iteration2.src.course;

import iteration2.src.human.Assistant;
import iteration2.src.human.Grade;
import iteration2.src.human.Lecturer;
import iteration2.src.human.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacultyTechnicalElectiveCourse extends ElectiveCourse{

    public static final Map<Integer, Integer> numberOfCoursesTakeableBySemester = new HashMap<>(){{
        put(0,0);
        put(1,0);
        put(2,0);
        put(3,0);
        put(4,0);
        put(5,0);
        put(6,0);
        put(7,1);
    }};

    public FacultyTechnicalElectiveCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                                          Grade firstYearToTake, Season firstSeasonToTake, List<Lecturer> lecturers, List<Assistant> assistants){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake,lecturers,assistants);
    }

    @Override
    public Boolean canStudentTakeCourse(Student student) {
        int semester = student.getStudentSemester();
        int noOfCoursesTillSemester = getTotalNumberOfCoursesUntilSemester(semester);

        return student.getTranscript().getNumberOfFTECoursesPassed() < noOfCoursesTillSemester
        && !student.didStudentPass(this);
    }

    public static int getTotalNumberOfCoursesUntilSemester(int semester){
        int noOfCoursesTillSemester = 0;

        for(int i = 0; i <= semester; i++){
            noOfCoursesTillSemester += numberOfCoursesTakeableBySemester.get(i).intValue();
        }

        return noOfCoursesTillSemester;
    }

    public int getCoursePriority(){
        return 1;
    }
}
