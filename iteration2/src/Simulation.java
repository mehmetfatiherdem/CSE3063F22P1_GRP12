package iteration2.src;

import iteration2.src.course.*;
import iteration2.src.human.*;
import iteration2.src.input_output.JsonParser;
import iteration2.src.input_output.Logger;

import java.util.List;

public class Simulation {

    //TODO:Revise these implementations ,of which some are unwise, and move them into their related classes in the next iteration
    //TODO:Do something about brute forcing of trying to take electives and hence the long long lines of unnecessary logs
    public static void main(String[] args) {
        runSimulation(init());
    }

    public static List<Student> init(){

        JsonParser parser = new JsonParser();
        var lecturers = parser.parseLecturers();
        var assistants = parser.parseAssistants();
        var advisors = parser.parseAdvisors();
        var courses = parser.parseCourses();
        var students = parser.parseStudents(advisors,courses);
        var season = parser.parseSemester();

        lecturers.addAll(advisors);

        Department department = Department.getInstance();
        department.initialize(season,courses,lecturers,assistants,advisors,students);

        return students;
    }

    public static void runSimulation(List<Student> students){

        Department department = Department.getInstance();
        Season currentSeason = department.getCurrentSeason();
        List<Course> courses = department.getCourses();

        for(Student s: students){

            int nteCounter = 0;
            int teCounter = 0;
            int fteCounter = 0;

            for(Course c : courses){

                if(c.canStudentTakeCourse(s)){
                    if(c instanceof MandatoryCourse){
                        tryToRegister(s,c,0);
                    } else if(c instanceof NonTechnicalElectiveCourse
                            && c.isStudentGradeRequirementMet(s, currentSeason) && nteCounter == 0){
                            nteCounter = tryToRegister(s,c,nteCounter);
                    }else if(c instanceof TechnicalElectiveCourse
                            && c.isStudentGradeRequirementMet(s, currentSeason) && teCounter == 0) {
                        teCounter = tryToRegister(s,c,teCounter);
                    }else if(c instanceof FacultyTechnicalElectiveCourse
                            && c.isStudentGradeRequirementMet(s, currentSeason) && fteCounter == 0){
                        fteCounter = tryToRegister(s,c,fteCounter);
                    }else if(c instanceof TechnicalElectiveCourse
                            && c.isStudentGradeRequirementMet(s, currentSeason) && teCounter < 2){
                        teCounter = tryToRegister(s,c,teCounter);
                    }
                }
            }

            s.register();
        }

        JsonParser parser = new JsonParser();
        parser.serializeStudents(students);

        Logger.log("");
        Logger.log("Registration has ended");
    }

    private static int tryToRegister(Student student,Course course, int counter){

        var courseSections = course.getAvailableCourseSections();

        if(courseSections.size() == 0)
            return counter;

        student.addToRegistrationList(courseSections.get(0));

        var labSections = course.getAvailableLabSections();

        if(labSections.size() > 0){
            student.addToRegistrationList(labSections.get(0));
        }

        return counter + 1;
    }
}
