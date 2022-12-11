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

public class TechnicalElectiveCourse extends ElectiveCourse{

    public static final int REQUIRED_CREDITS = 155;

    public static final Map<Integer, Integer> maxNumberThatCanBeTakenInASemester = new HashMap<>(){{
        put(7, 1);
        put(8, 3);
    }};

    public TechnicalElectiveCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                                   Grade firstYearToTake, Season firstSeasonToTake, List<Lecturer> lecturers, List<Assistant> assistants){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake,lecturers,assistants);
    }

    @Override
    public void addPrerequisite(Course prerequisite){
        prerequisites.add(prerequisite);
    }

    @Override
    public Boolean canStudentTakeCourse(Student student) {

        var dep = Department.getInstance();

        boolean canBeRegistered = !isMaxChoosableNumberExceeded(student, dep.getCurrentSeason());

        if(!canBeRegistered){
            Logger.log("You've already taken " + maxNumberThatCanBeTakenInASemester.get((student.getGrade().getValue() * 2) + (dep.getCurrentSeason().getValue() + 1)) + " TE in the " + dep.getCurrentSeason() + " Semester which is the max number for that season. " + student.getFullName() + " could not take TE(" + this.getCode() + ")");
        }

        return canBeRegistered;
    }

    public boolean isCreditsRequirementMet(Student student){
        return REQUIRED_CREDITS <= student.getCompletedCredits();
    }

    @Override
    public boolean isMaxChoosableNumberExceeded(Student student, Season season){

        int semester = (student.getGrade().getValue() * 2) + (season.getValue() + 1);

        return student.getChosenCourseTypeCounterInRegistration().get("TE") > maxNumberThatCanBeTakenInASemester.get(semester);

    }

    @Override
    public boolean isStudentGradeRequirementMet(Student s, Season currentSeason) {
        return (s.getGrade() == Grade.SENIOR && (currentSeason == Season.SPRING || currentSeason == Season.FALL));
    }


}
