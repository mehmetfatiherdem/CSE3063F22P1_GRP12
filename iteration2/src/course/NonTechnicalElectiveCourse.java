package iteration2.src.course;

import iteration2.src.human.Grade;
import iteration2.src.human.Student;

public class NonTechnicalElectiveCourse extends ElectiveCourse{
        public NonTechnicalElectiveCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                                          Grade firstYearToTake, Season firstSeasonToTake){
            super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake);
        }

    @Override
    public boolean isStudentGradeRequirementMet(Student s, Season currentSeason) {

            return ((s.getGrade() == Grade.FRESHMAN) && (currentSeason == Season.SPRING) || (s.getGrade() == Grade.SENIOR && currentSeason == Season.FALL)
                    || (s.getGrade() == Grade.SENIOR && currentSeason == Season.SPRING));
    }
}
