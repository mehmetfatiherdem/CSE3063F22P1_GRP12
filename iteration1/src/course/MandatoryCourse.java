package iteration1.src.course;

import iteration1.src.Helper;
import iteration1.src.human.Grade;
import iteration1.src.human.Student;

import java.util.List;

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

    @Override
    public List<CourseSection> getAvailableCourseSections(){
        var sections = super.getAvailableCourseSections();

        if(sections.size() == 0){
            int[] classes = Helper.generateDistinctClassHours(theoreticalHours);
            long schedule = Section.getScheduleAtRandomPositions(classes);
            CourseSection newSection = new CourseSection(this,schedule,null);
            sectionList.add(newSection);
            sections.add(newSection);
        }

        return sections;
    }

    @Override
    public List<LabSection> getAvailableLabSections(){
        var sections = super.getAvailableLabSections();

        if(sections.size() == 0){
            int[] classes = Helper.generateDistinctClassHours(appliedHours);
            long schedule = Section.getScheduleAtRandomPositions(classes);
            LabSection newSection = new LabSection(this,schedule,null);
            sectionList.add(newSection);
            sections.add(newSection);
        }

        return sections;
    }
}
