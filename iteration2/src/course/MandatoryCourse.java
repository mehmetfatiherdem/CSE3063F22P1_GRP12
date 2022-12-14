package iteration2.src.course;

import iteration2.src.Department;
import iteration2.src.human.Assistant;
import iteration2.src.human.Grade;
import iteration2.src.human.Lecturer;
import iteration2.src.human.Student;

import java.util.List;

public class MandatoryCourse extends Course{

    public MandatoryCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                           Grade firstYearToTake, Season firstSeasonToTake, List<Lecturer> lecturers, List<Assistant> assistants){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake,lecturers,assistants);
    }

    @Override
    public Boolean isAnyCourseSectionAvailable(){
        if(theoreticalHours == 0)
            return false;
        if(!super.isAnyCourseSectionAvailable())
            Department.getInstance().addNewCourseSection(this);

        return true;
    }

    @Override
    public Boolean isAnyLabSectionAvailable(){
        if(appliedHours == 0)
            return false;
        if(!super.isAnyLabSectionAvailable())
            Department.getInstance().addNewLabSection(this);

        return true;
    }

    @Override
    public List<CourseSection> getAvailableCourseSections(){
        var sections = super.getAvailableCourseSections();

        if(sections.size() == 0 && theoreticalHours > 0){
            Department.getInstance().addNewCourseSection(this);
            sections = super.getAvailableCourseSections();
        }

        return sections;
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

    public int getCoursePriority(){
        return 3;
    }
}
