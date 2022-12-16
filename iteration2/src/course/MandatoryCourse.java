package iteration2.src.course;

import iteration2.src.Department;
import iteration2.src.human.Assistant;
import iteration2.src.human.Grade;
import iteration2.src.human.Lecturer;
import iteration2.src.human.Student;
import iteration2.src.input_output.Logger;

import java.util.List;

public class MandatoryCourse extends Course{

    public MandatoryCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                           Grade firstYearToTake, Season firstSeasonToTake, List<Lecturer> lecturers, List<Assistant> assistants){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake,lecturers,assistants);
    }

    @Override
    public List<LabSection> getAvailableLabSections(){
        var sections = super.getAvailableLabSections();

        if(sections.size() == 0 && appliedHours > 0){
            Department.getInstance().addNewLabSection(this);
            sections = super.getAvailableLabSections();
        }

        return sections;
    }

    @Override
    public void requestNewCourseSection(){
        Department.getInstance().addNewCourseSection(this);
    }

    public int getCoursePriority(){
        return 3;
    }
}
