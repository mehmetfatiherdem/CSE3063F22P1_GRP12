package iteration2.src.course;

import iteration2.src.human.Lecturer;
import iteration2.src.input_output.Logger;

public class CourseSection extends Section{
    public CourseSection(Course course, String sectionCode, long classHours, Lecturer instructor) {
        super(course, sectionCode ,classHours, instructor);
        Logger.incrementIndentation();
        Logger.log("ADDING NEW COURSE SECTION FOR THE COURSE " + course.getCode() + " WITH THE CODE " + toString());
        Logger.decrementIndentation();
    }

    public Lecturer getLecturer(){
        return (Lecturer)instructor;
    }

    @Override
    public int getSectionPriority(){
        return super.getSectionPriority() + 1;
    }
}
