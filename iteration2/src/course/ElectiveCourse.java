package iteration2.src.course;


import iteration2.src.human.Assistant;
import iteration2.src.human.Grade;
import iteration2.src.human.Lecturer;
import iteration2.src.human.Student;

import java.util.List;

public class ElectiveCourse extends Course {

    public ElectiveCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                          Grade firstYearToTake, Season firstSeasonToTake, List<Lecturer> lecturers, List<Assistant> assistants){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake,lecturers,assistants);
    }

    @Override
    public void addPrerequisite(Course prerequisite){
        return;
    }

    public boolean isMaxChoosableNumberExceeded(Student student, Season season){

       return false;

    }
}
