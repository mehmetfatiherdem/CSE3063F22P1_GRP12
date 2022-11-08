package iteration1.src.course;

import iteration1.src.human.Assistant;
import iteration1.src.human.FacultyMember;

public class LabSection extends Section{

    protected LabSection(Course course, long classHours, Assistant instructor) {
        super(course, classHours, instructor);
    }

    public Assistant getAssistant(){
        return (Assistant)instructor;
    }

    public void setAssistant(Assistant assistant){
        instructor = assistant;
    }
    
}
