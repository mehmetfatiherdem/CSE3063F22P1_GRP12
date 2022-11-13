package iteration1.src.course;

import iteration1.src.human.Lecturer;

public class CourseSection extends Section{

    public CourseSection(Course course, long classHours, Lecturer instructor) {
        super(course, classHours, instructor);
    }

    public Lecturer getLecturer(){
        return (Lecturer)instructor;
    }

    public void setLecturer(Lecturer lecturer){
        instructor = lecturer;
    }
}
