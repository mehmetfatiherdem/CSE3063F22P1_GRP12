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
        var advisors = parser.parseAdvisors();
        lecturers.addAll(advisors);
        var assistants = parser.parseAssistants();
        var courses = parser.parseCourses(lecturers,assistants);
        var students = parser.parseStudents(advisors,courses);
        var season = parser.parseSemester();

        Department department = Department.getInstance();
        department.initialize(season,courses,lecturers,assistants,advisors,students);

        return students;
    }

    private static void runSimulation(List<Student> students){
        Logger.log("The simulation has started!");

        registrationProcess(students);

        Logger.log("");
        Logger.log("Registration process has ended");

        Logger.log("");
        Logger.log("The grading process has started!");

        gradingProcess(students);

        Logger.log("");
        Logger.log("The grading process has ended");

        JsonParser parser = new JsonParser();
        parser.serializeStudents(students);

        Logger.log("");
        Logger.log("The simulation has ended!");
    }

    private static void registrationProcess(List<Student> students){
        RegistrationSystem system = RegistrationSystem.getInstance();

        for(Student s: students) {
            var openMandatoryCourses = system.getOpenMandatoryCourses(s);
            var openTECourses = system.getOpenTECourses(s);
            var openFTECourses = system.getOpenFTECourses(s);
            var openNTECourses = system.getOpenNTECourses(s);

            int noOfTakeableFTECourses = system.getTheNumberOfFTECoursesStudentCanTake(s);
            int noOfTakeableTECourses = system.getTheNumberOfTECoursesStudentCanTake(s);
            int noOfTakeableNTECourses = system.getTheNumberOfNTECoursesStudentCanTake(s);

            s.startRegistration(openMandatoryCourses, openTECourses, openFTECourses, openNTECourses, noOfTakeableTECourses, noOfTakeableFTECourses, noOfTakeableNTECourses);
        }
    }

    private static void gradingProcess(List<Student> students){
        for(Student s : students){
            List<CourseRecord> nonGradedCourses = s.getTranscript().getNonGradedCourses();

            Logger.log("");
            Logger.log(s.getFullName() + "'s grades : ");
            Logger.log("");

            for(CourseRecord r:nonGradedCourses){
                float rand = RandomNumberGenerator.RandomFloat();

                if(rand <= s.getFailChance()){
                    r.setScore(RandomNumberGenerator.RandomFloatBetween(0.0f, LetterGrade.DD.getNumVal()-0.01f));
                    r.setIsPassed(false);
                    r.setlGrade(Transcript.getLetterGradeOfScore(r.getScore()));
                }else{
                    r.setScore(RandomNumberGenerator.RandomFloatBetween(LetterGrade.DD.getNumVal(),100.0f));
                    r.setIsPassed(true);
                    r.setlGrade(Transcript.getLetterGradeOfScore(r.getScore()));
                }

                Logger.log("    " + r.getCourse().getName() + " (" + r.getCourse().getCode() + ") :");
                Logger.log("        Score : " + r.getScore());
                Logger.log("        Letter Grade : " + r.getlGrade().toString());
                Logger.log("        Status : " + (r.getIsPassed() ? "Passed" : "Failed"));
            }
        }
    }
}
