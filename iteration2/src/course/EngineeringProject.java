package iteration2.src.course;

import iteration2.src.human.Assistant;
import iteration2.src.human.Grade;
import iteration2.src.human.Lecturer;
import iteration2.src.human.Student;
import iteration2.src.input_output.Logger;

import java.util.List;

public class EngineeringProject extends Course{

    public static final int REQUIRED_CREDITS = 165;

    public EngineeringProject(String code, String name, int credits, int theoreticalHours
            , int appliedHours, Grade firstYearToTake, Season firstSeasonToTake, List<Lecturer> lecturers, List<Assistant> assistants){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake,lecturers,assistants);
    }

    @Override
    public Boolean canStudentTakeCourse(Student student) {
        if(!super.canStudentTakeCourse(student)){
            return false;
        }

        if(!isCreditsRequirementMet(student)){
            Logger.log("You must complete " + REQUIRED_CREDITS + " to take Engineering Project(" + this.getCode() + ")");
            return false;
        }

        return true;
    }

    public boolean isCreditsRequirementMet(Student student){
        return REQUIRED_CREDITS <= student.getCompletedCredits();
    }

    public int getCoursePriority(){
        return 3;
    }
}
