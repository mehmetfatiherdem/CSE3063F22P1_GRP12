package iteration1.src;

import iteration1.src.course.Course;


import java.util.List;

public class Department {
    private static Department instance;
    private String code;
    private List<Course> courses;
    private List<Student> students;
    private List<FacultyMember> facultyMembers;

    // Will be written after these classes finished
    public void assignLecturerToACourse(Lecturer lecturer,Course course){

    }
    public void assignLecturerToACourseSection(Lecturer lecturer,Course course){

    }
    public void assignAssistantToALabCourse(Assistan lecturer,Course course){

    }




    private Department Department() {
        return this;
    }

    public static void setInstance(Department instance) {
        Department.instance = instance;
    }

    public static Department getInstance() {
        return instance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Lecturer> getLecturers() {
        return lecturers;
    }

    public void setLecturers(List<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }
}
