package iteration2.src.course;

import iteration2.src.Department;
import iteration2.src.human.Grade;
import iteration2.src.human.Student;
import iteration2.src.input_output.Logger;

public class TechnicalElectiveCourse extends ElectiveCourse{

    public static final int REQUIRED_CREDITS = 155;

    public static final int MAX_CHOOSABLE_NUMBER_IN_FALL = 2; // TODO: I took this information from the Google Classroom but couldn't find anything to verify it. We should check this

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


        var dep = Department.getInstance();

        boolean canBeRegistered = !isMaxChoosableNumberExceeded(student, dep.getCurrentSeason());

        if(!canBeRegistered){
            Logger.log("You've already taken " + MAX_CHOOSABLE_NUMBER_IN_FALL + " TE in the Fall Semester which is the max number for that season. " + student.getFullName() + " could not take TE(" + this.getCode() + ")");
        }

        return canBeRegistered;
    }

    public boolean isCreditsRequirementMet(Student student){
        return REQUIRED_CREDITS <= student.getCompletedCredits();
    }

    public boolean isMaxChoosableNumberExceeded(Student student, Season season){

        boolean isExceeded = false;

        //TODO: add an else if when we find out about the Spring requirement
        if(season == Season.FALL){
            isExceeded = student.getChosenCourseTypeCounterInFall().get("TE") >= MAX_CHOOSABLE_NUMBER_IN_FALL;
        }
        return isExceeded;
    }

    @Override
    public boolean isStudentGradeRequirementMet(Student s, Season currentSeason) {
        return (s.getGrade() == Grade.SENIOR && currentSeason == Season.SPRING);
    }


}
