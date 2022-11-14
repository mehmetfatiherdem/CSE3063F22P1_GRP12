package iteration1.src.course;

import iteration1.src.human.Student;

public class EngineeringProject extends Course{

    public static final int REQUIRED_CREDITS = 165;

    public EngineeringProject(String code, int quota){
        super(code, quota);
    }

    @Override
    public Boolean canStudentTakeCourse(Student student) {

        boolean isStudentAbleToTake = true;
        if(!isCreditsRequirementMet(student)){
            System.out.println("You must complete " + REQUIRED_CREDITS + " to take Engineering Project(" + this.getCode() + ")");
            isStudentAbleToTake = false;
        }

        return isStudentAbleToTake;
    }


    public boolean isCreditsRequirementMet(Student student){
        return REQUIRED_CREDITS <= student.getTranscript().getCompletedCredits();
    }
}
