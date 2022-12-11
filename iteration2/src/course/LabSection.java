package iteration2.src.course;

import iteration2.src.human.Assistant;

public class LabSection extends Section{
    protected LabSection(Course course, String sectionCode ,long classHours, Assistant instructor) {
        super(course, sectionCode ,classHours, instructor);
    }

    public Assistant getAssistant(){
        return (Assistant)instructor;
    }
}
