package iteration2.src.course;

import iteration2.src.human.Assistant;
import iteration2.src.human.Grade;
import iteration2.src.human.Lecturer;
import iteration2.src.human.Student;
import iteration2.src.input_output.Logger;

import java.util.List;

public class TechnicalElectiveCourse extends ElectiveCourse{

    public static final int REQUIRED_CREDITS = 155;

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

        boolean isStudentAbleToTake = true;
        if(!isCreditsRequirementMet(student) || isMaxChoosableNumberExceeded(student)){
            Logger.log("You must complete " + REQUIRED_CREDITS + " to take TE(" + this.getCode() + ")");
            isStudentAbleToTake = false;
        }

        return isStudentAbleToTake;
    }

    public boolean isCreditsRequirementMet(Student student){
        return REQUIRED_CREDITS <= student.getCompletedCredits();
    }

    public boolean isMaxChoosableNumberExceeded(Student student){
        //TODO: will be added after some fixes in the student class
        return false;
    }

    @Override
    public boolean isStudentGradeRequirementMet(Student s, Season currentSeason) {
        return (s.getGrade() == Grade.SENIOR && currentSeason == Season.SPRING);
    }


}
