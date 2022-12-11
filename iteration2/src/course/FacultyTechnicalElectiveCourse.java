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

public class FacultyTechnicalElectiveCourse extends ElectiveCourse{

    public static final Map<Integer, Integer> maxNumberThatCanBeTakenInASemester = new HashMap<>(){{
        put(8, 1);
    }};

    public FacultyTechnicalElectiveCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                                          Grade firstYearToTake, Season firstSeasonToTake, List<Lecturer> lecturers, List<Assistant> assistants){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake,lecturers,assistants);
    }

    @Override
    public boolean isStudentGradeRequirementMet(Student s, Season currentSeason) {
        return (s.getGrade() == Grade.SENIOR && currentSeason == Season.SPRING);
    }

    @Override
    public Boolean canStudentTakeCourse(Student student) {

        var dep = Department.getInstance();
        int semester = (student.getGrade().getValue() * 2) + (dep.getCurrentSeason().getValue() + 1);
        int maxNum = maxNumberThatCanBeTakenInASemester.get(semester);
        boolean canBeRegistered = !isMaxChoosableNumberExceeded(student, dep.getCurrentSeason(), "FTE", maxNum);

        if(!canBeRegistered){
            Logger.log("You've already taken " + maxNumberThatCanBeTakenInASemester.get((student.getGrade().getValue() * 2) + (dep.getCurrentSeason().getValue() + 1)) + " FTE in the " + dep.getCurrentSeason() + " Semester which is the max number for that season. " + student.getFullName() + " could not take FTE(" + this.getCode() + ")");
        }

        return canBeRegistered;
    }

    @Override
    public boolean isMaxChoosableNumberExceeded(Student student, Season season, String courseTypeCode, int maxNumberThatCanBeTakenInASemester){

        return super.isMaxChoosableNumberExceeded(student, season, courseTypeCode, maxNumberThatCanBeTakenInASemester);

    }
}
