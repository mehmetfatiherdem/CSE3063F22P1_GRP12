package iteration2.src.course;

import iteration2.src.human.Assistant;

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
