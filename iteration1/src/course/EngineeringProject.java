package iteration1.src.course;

import iteration1.src.human.Student;

public class EngineeringProject extends Course{

    public EngineeringProject(String code) {
        super(code);
    }

    @Override
    public Boolean canStudentTakeCourse(Student student) {

        boolean isStudentAbleToTake = true;
        if(!isCreditsRequirementMet(student)){
            isStudentAbleToTake = false;
        }

        return isStudentAbleToTake;
    }


    public boolean isCreditsRequirementMet(Student student){
        // return REQUIRED_CREDITS <= student.getTranscript().getCompletedCredits()
        return true;
    }
}
