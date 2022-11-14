package iteration1.src;

import iteration1.src.course.Course;
import iteration1.src.course.Season;
import iteration1.src.human.Assistant;
import iteration1.src.human.FacultyMember;
import iteration1.src.human.Lecturer;
import iteration1.src.human.Student;
import java.util.ArrayList;
import java.util.List;

public class Department {
    private static Department instance;

    private String code;
    private Season  currentSemester;
    private List<Course> courses;
    private List<Student> students = new ArrayList<>();
    private List<FacultyMember> facultyMembers = new ArrayList<>();

    // Will be written after these classes finished
    public void assignLecturerToACourse(Lecturer lecturer,Course course){

    }
    public void assignLecturerToACourseSection(Lecturer lecturer,Course course){

    }
    public void assignAssistantToALabCourse(Assistant lecturer,Course course){

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

    // Getters
    public String getCode() {
        return code;
    }
    public Season getCurrentSemester(){
        return currentSemester;
    }
    public List<Course> getCourses() {
        return courses;
    }
    public List<Student> getStudents() {
        return students;
    }
    public List<FacultyMember> getFacultyMembers() {
        return facultyMembers;
    }

    //Setters
    public void setCode(String code) {
        if (code.length() < 6) {
            System.out.println("Invalid course code.");
        } else {
            this.code = code;
        }
    }
    public void setCurrentSemester(Season semester) {
        this.currentSemester = semester;
    }
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }


    public void addStudents(Student student) {
        this.students.add(student); 
    }



    public void addFacultyMembers(FacultyMember facultyMember) {
        this.facultyMembers.add(facultyMember);
    }

}
