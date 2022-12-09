package iteration2.src.course;

import iteration2.src.human.Grade;
import iteration2.src.human.Student;

public class FacultyTechnicalElectiveCourse extends ElectiveCourse{

    public FacultyTechnicalElectiveCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                                          Grade firstYearToTake, Season firstSeasonToTake){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake);
    }

    @Override
    public boolean isStudentGradeRequirementMet(Student s, Season currentSeason) {
        return (s.getGrade() == Grade.SENIOR && currentSeason == Season.SPRING);
    }
}
