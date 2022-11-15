package iteration1.src.course;

import iteration1.src.human.Grade;
import iteration1.src.human.Student;
import iteration1.src.input_output.Logger;

public class TechnicalElectiveCourse extends ElectiveCourse{

    public static final int REQUIRED_CREDITS = 155;

    public TechnicalElectiveCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                                   Grade firstYearToTake, Season firstSeasonToTake){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake);
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

}
