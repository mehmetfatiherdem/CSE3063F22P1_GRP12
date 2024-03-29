package iteration2.src;

import iteration2.src.course.*;
import iteration2.src.human.*;
import iteration2.src.input_output.HorizontalLineType;
import iteration2.src.input_output.JsonParser;
import iteration2.src.input_output.Logger;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
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

        Logger.clearLogFile();

        return students;
    }

    private static void runSimulation(List<Student> students){
        Logger.logSimulationEntities();

        Logger.newLine();
        Logger.log("THE SIMULATION HAS STARTED!");
        Logger.newLine();

        List<Float> studentGpas = registrationProcess(students);

        Logger.newLine();
        gradingProcess(students, studentGpas);

        JsonParser parser = new JsonParser();
        parser.serializeStudents(students);

        Logger.newLine();
        Logger.log("THE SIMULATION HAS ENDED!");
    }

    private static List<Float> registrationProcess(List<Student> students){
        RegistrationSystem system = RegistrationSystem.getInstance();

        Logger.newLine(HorizontalLineType.Star);
        Logger.newLine();
        Logger.log("THE REGISTRATION PROCESS HAS STARTED!");

        List<Float> studentGPAs = new ArrayList<>();

        for(Student student: students) {
            var openMandatoryCourses = system.getOpenMandatoryCourses(student);
            var openTECourses = system.getOpenTECourses(student);
            var openFTECourses = system.getOpenFTECourses(student);
            var openNTECourses = system.getOpenNTECourses(student);

            int noOfTakeableFTECourses = system.getTheNumberOfFTECoursesStudentCanTake(student);
            int noOfTakeableTECourses = system.getTheNumberOfTECoursesStudentCanTake(student);
            int noOfTakeableNTECourses = system.getTheNumberOfNTECoursesStudentCanTake(student);

            String studentName = student.getFullName();

            Logger.newLine();
            Logger.log("STUDENT INFORMATION :");
            Logger.incrementIndentation();

            float gpa = student.getTranscript().calculateGPA();
            studentGPAs.add(gpa);

            Logger.log("STUDENT NAME : " + studentName);
            Logger.log("STUDENT ID : " + student.getStudentID());
            Logger.log("STUDENT GRADE : " + student.getGrade().toString());
            Logger.log("STUDENT GPA : " + MathHelper.roundFloat(gpa,2));
            Logger.log("ADVISOR : " + student.getAdvisor().getFullName());

            Logger.log("COURSES OPENED FOR THE STUDENT :");
            Logger.incrementIndentation();
            Logger.logCourseCodes("MANDATORY COURSES : ", (List<Course>)(List<?>)openMandatoryCourses);
            Logger.logCourseCodes("TECHNICAL ELECTIVE COURSES : ", (List<Course>)(List<?>)openTECourses);
            Logger.logCourseCodes("FACULTY TECHNICAL ELECTIVE COURSES : ", (List<Course>)(List<?>)openFTECourses);
            Logger.logCourseCodes("NON-TECHNICAL ELECTIVE COURSES : ", (List<Course>)(List<?>)openNTECourses);
            Logger.decrementIndentation();

            Logger.decrementIndentation();
            Logger.newLine();

            Logger.log("THE REGISTRATION PROCESS OF " + studentName + " HAS STARTED :");
            Logger.newLine();

            student.startRegistration(openMandatoryCourses, openTECourses, openFTECourses,
                    openNTECourses, noOfTakeableTECourses, noOfTakeableFTECourses, noOfTakeableNTECourses);
        }

        Logger.newLine();
        Logger.log("THE REGISTRATION PROCESS HAS ENDED");
        Logger.newLine();
        Logger.newLine(HorizontalLineType.Star);
        return studentGPAs;
    }

    private static void gradingProcess(List<Student> students, List<Float> oldGPAs){
        Logger.log("THE GRADING PROCESS HAS STARTED!");

        Season semester = Department.getInstance().getCurrentSeason();

        for(int i = 0; i < students.size(); i++){
            Student student = students.get(i);
            Grade studentOldGrade = student.getGrade();
            Grade studentNewGrade = Grade.SENIOR;

            if(semester == Season.SPRING){
                if (studentOldGrade == Grade.SENIOR){
                    students.remove(i--);
                    continue;
                }

                studentNewGrade = Grade.values()[studentOldGrade.getValue() + 1];
                student.setGrade(studentNewGrade);
            }

            List<CourseRecord> nonGradedCourses = student.getTranscript().getNonGradedCourses();

            for(CourseRecord r:nonGradedCourses) {
                float rand = MathHelper.RandomFloat();

                if (rand <= student.getFailChance()) {
                    float score = MathHelper.randomFloatBetween(0f, 39.99f);
                    r.setScore(score);
                    r.setIsPassed(false);
                    r.setlGrade(Transcript.getLetterGradeOfScore(score));
                } else {
                    float score = MathHelper.randomFloatBetween(40.0f, 100.0f);
                    r.setScore(score);
                    r.setIsPassed(true);
                    r.setlGrade(Transcript.getLetterGradeOfScore(score));
                }
            }

            Logger.newLine();
            Logger.newLine(HorizontalLineType.Dash);
            Logger.newLine();
            Logger.log(student.getFullName() + " (" + student.getStudentID() + ") :");

            Logger.incrementIndentation();
            Logger.log("STUDENT'S NEW GRADE : " + studentNewGrade.toString());
            Logger.log("THE GPA AT THE START OF THIS SEMESTER : " + MathHelper.roundFloat(oldGPAs.get(i),2));
            Logger.log("THE GPA AT THE END OF THIS SEMESTER : " + MathHelper.roundFloat(student.getTranscript().calculateGPA(),2));
            Logger.newLine();
            Logger.log("ALL COURSES' DETAILS OF THIS SEMESTER:");
            Logger.incrementIndentation();

            for(CourseRecord r : nonGradedCourses){
                Logger.newLine();
                Logger.log(r.getCourse().getName() + " (" + r.getCourse().getCode() + ") :");

                Logger.incrementIndentation();
                Logger.log("SCORE : " + MathHelper.roundFloat(r.getScore(),0));
                Logger.log("LETTER GRADE : " + r.getlGrade().toString());
                Logger.log("STATUS : " + (r.getIsPassed() ? "PASSED" : "FAILED"));
                Logger.decrementIndentation();
            }

            Logger.newLine();
            Logger.newLine(HorizontalLineType.Dash);

            Logger.decrementIndentation();
            Logger.decrementIndentation();
        }
    }
}
