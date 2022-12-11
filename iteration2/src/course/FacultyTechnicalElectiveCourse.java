package iteration2.src.course;

import iteration2.src.human.Assistant;
import iteration2.src.human.Grade;
import iteration2.src.human.Lecturer;
import iteration2.src.human.Student;

import java.util.List;

public class FacultyTechnicalElectiveCourse extends ElectiveCourse{

    public FacultyTechnicalElectiveCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                                          Grade firstYearToTake, Season firstSeasonToTake, List<Lecturer> lecturers, List<Assistant> assistants){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake,lecturers,assistants);
    }

    @Override
    public boolean isStudentGradeRequirementMet(Student s, Season currentSeason) {
        return (s.getGrade() == Grade.SENIOR && currentSeason == Season.SPRING);
    }
}
