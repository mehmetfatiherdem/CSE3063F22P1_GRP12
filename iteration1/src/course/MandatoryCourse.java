package iteration1.src.course;

import iteration1.src.human.Grade;
import iteration1.src.human.Student;

public class MandatoryCourse extends Course{

    public MandatoryCourse(String code, String name, int credits, int theoreticalHours
            , int appliedHours, Grade firstYearToTake, Season firstSeasonToTake){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake);
    }

    @Override
    public Boolean canStudentTakeCourse(Student student) {
        if(!super.canStudentTakeCourse(student))
            return false;

        return student.checkIfPrerequisitesArePassed(this);
    }

    @Override
    public Boolean isAnyCourseSectionAvailable(){
        if(!super.isAnyCourseSectionAvailable()){
            //Section newSection = new CourseSection(this);
        }

        return true;
    }

    @Override
    public Boolean isAnyLabSectionAvailable(){
        if(!super.isAnyLabSectionAvailable()){
            //
        }
        return true;
    }
}
