package iteration1.src.course;

import iteration1.src.human.Student;

public class TechnicalElectiveCourse extends ElectiveCourse{

    public static final int REQUIRED_CREDITS = 155;

    public TechnicalElectiveCourse(String code){
        super(code);
    }

    @Override
    public Boolean canStudentTakeCourse(Student student) {

        boolean isStudentAbleToTake = true;
        if(!isCreditsRequirementMet(student) || isMaxChoosableNumberExceeded(student)){
            isStudentAbleToTake = false;
        }

        return isStudentAbleToTake;
    }

    public boolean isCreditsRequirementMet(Student student){
       // return REQUIRED_CREDITS <= student.getTranscript().getCompletedCredits()
        return true;
    }

    public boolean isMaxChoosableNumberExceeded(Student student){
        //TODO: will be added after some fixes in the student class
        return false;
    }

}
