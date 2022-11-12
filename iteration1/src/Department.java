package iteration1.src;

import iteration1.src.course.Course;


import java.util.List;

public class Department {
    private static Department instance;
    private String code;
    private List<Course> courses;
    private List<Student> students = new ArrayList<>();
    private List<FacultyMember> facultyMembers new ArrayList<>();

    // Will be written after these classes finished
    public void assignLecturerToACourse(Lecturer lecturer,Course course){

    }
    public void assignLecturerToACourseSection(Lecturer lecturer,Course course){

    }
    public void assignAssistantToALabCourse(Assistan lecturer,Course course){

    }




    private Department() {
    }

    public static Department getInstance() {
        if (instance == null)
        {
            instance = new Department();
        }
        return instance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if(code.length() < 6){
            System.out.println("Invalid course code.");
        }
        else{
            this.code = code;
        }
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

    public void addStudents(Student student) {
        this.students.add(student); 
    }

    public List<FacultyMember> getFacultyMembers() {
        return facultyMembers;
    }

    public void addFacultyMembers(FacultyMember facultyMember) {
        this.facultyMembers.add(facultyMember);
    }
}
