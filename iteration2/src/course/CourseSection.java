package iteration2.src.course;

import iteration2.src.human.Lecturer;

public class CourseSection extends Section{
    public CourseSection(Course course, String sectionCode, long classHours, Lecturer instructor) {
        super(course, sectionCode ,classHours, instructor);
    }

    public Lecturer getLecturer(){
        return (Lecturer)instructor;
    }

    @Override
    public int getSectionPriority(){
        return super.getSectionPriority() + 1;
    }
}
