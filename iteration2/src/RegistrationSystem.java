package iteration2.src;

import iteration2.src.course.*;
import iteration2.src.data_structures.Tuple;
import iteration2.src.human.Student;
import iteration2.src.input_output.HorizontalLineType;
import iteration2.src.input_output.Logger;

import java.util.ArrayList;
import java.util.List;

public class RegistrationSystem {
    private static RegistrationSystem instance;

    private RegistrationSystem() {}

    public static RegistrationSystem getInstance(){
        if(instance == null)
            instance = new RegistrationSystem();

        return instance;
    }

    public List<MandatoryCourse> getOpenMandatoryCourses(Student student){
        var mandatoryCourses = Department.getInstance().getMandatoryCourses();
        return (List<MandatoryCourse>)(List<?>)getTakeableCourses((List<Course>)(List<?>)mandatoryCourses,student);
    }

    public List<NonTechnicalElectiveCourse> getOpenNTECourses(Student student){
        var nteCourses = Department.getInstance().getNonTechnicalElectiveCourses();
        return (List<NonTechnicalElectiveCourse>)(List<?>)getTakeableCourses((List<Course>)(List<?>)nteCourses,student);
    }

    public List<FacultyTechnicalElectiveCourse> getOpenFTECourses(Student student){
        var fteCourses = Department.getInstance().getFacultyTechnicalElectiveCourses();
        return (List<FacultyTechnicalElectiveCourse>)(List<?>)getTakeableCourses((List<Course>)(List<?>)fteCourses,student);
    }

    public List<TechnicalElectiveCourse> getOpenTECourses(Student student){
        var teCourses = Department.getInstance().getTechnicalElectiveCourses();
        return (List<TechnicalElectiveCourse>)(List<?>)getTakeableCourses((List<Course>)(List<?>)teCourses,student);
    }

    public int getTheNumberOfTECoursesStudentCanTake(Student student){
        if(student.getCompletedCredits() < TechnicalElectiveCourse.REQUIRED_CREDITS)
            return 0;

        int noOfTEPassed = student.getTranscript().getNumberOfTECoursesPassed();
        int noOfTERequired = TechnicalElectiveCourse.getTotalNumberOfCoursesUntilSemester(student.getStudentSemester());
        return noOfTERequired - noOfTEPassed;
    }

    public int getTheNumberOfNTECoursesStudentCanTake(Student student){
        int noOfNTEPassed = student.getTranscript().getNumberOfNTECoursesPassed();
        int noOfNTERequired = NonTechnicalElectiveCourse.getTotalNumberOfCoursesUntilSemester(student.getStudentSemester());
        return noOfNTERequired - noOfNTEPassed;
    }

    public int getTheNumberOfFTECoursesStudentCanTake(Student student){
        int noOfFTEPassed = student.getTranscript().getNumberOfFTECoursesPassed();
        int noOfFTERequired = FacultyTechnicalElectiveCourse.getTotalNumberOfCoursesUntilSemester(student.getStudentSemester());
        return noOfFTERequired - noOfFTEPassed;
    }

    public List<Tuple<Section,Section>> checkEnrolledSections(Student student){
        var collisions = Section.checkForCollisions(student.getEnrolledCourses());

        List<Tuple<Section,Section>> highlyCollidingSections = new ArrayList<>();

        Logger.newLine(HorizontalLineType.Star);
        Logger.newLine();

        for(var c : collisions){
            Section sec1 = c.getKey();
            Section sec2 = c.getValue();

            var collisionsBetween = sec1.getCollisionsWith(sec2);
            int noOfCollisions = collisionsBetween.size();

            if(noOfCollisions >= 2) {
                highlyCollidingSections.add(c);

                Logger.log("THE COURSES " + sec1.toString() + ", AND " + sec2.toString() + " HAVE A COLLISION OF LENGTH " + noOfCollisions + " :");
                Logger.incrementIndentation();

                for(var coll : collisionsBetween){
                    Logger.log("ON " + Section.CLASS_DAYS[coll.getKey()] + " AT " + Section.CLASS_HOURS[coll.getValue()]);
                }

                Logger.decrementIndentation();
                Logger.log("TWO COURSES MUST NOT COLLIDE BY MORE THAN 1 HOUR!");
                continue;
            }

            for(var d : collisionsBetween){
                int collisionDay = d.getKey();
                int collisionHour = d.getValue();

                Logger.log("WARNING : THERE IS A COLLISION BETWEEN " + sec1.toString()
                        + " AND " + sec2.toString() + " ON " + Section.CLASS_DAYS[collisionDay] + " AT " + Section.CLASS_HOURS[collisionHour]);
            }
        }

        Logger.newLine();

        if(highlyCollidingSections.size() == 0){
            Logger.log(student.getFullName() + "'S SELECTED COURSES ARE ADEQUATE TO BE SENT TO ADVISOR APPROVAL!");
        }
        else{
            Logger.log("THERE ARE PREVENTING COLLISIONS AMONG THE COURSES " + student.getFullName() + " HAS SELECTED :");
            Logger.incrementIndentation();

            for (var coll : highlyCollidingSections){
                Logger.log("BETWEEN " + coll.getKey().toString() + " AND " + coll.getValue().toString());
            }

            Logger.decrementIndentation();
        }

        Logger.newLine();
        Logger.newLine(HorizontalLineType.Star);
        Logger.newLine();

        return highlyCollidingSections;
    }

    public List<Tuple<Section,Section>> sendToAdvisorApproval(Student student){
        return student.getAdvisor().examineRegistration(student);
    }

    private List<Course> getTakeableCourses(List<Course> from, Student student){
        List<Course> openCourses = new ArrayList<>();

        for(Course c : from){
            if(c.canStudentTakeCourse(student))
                openCourses.add(c);
        }

        return openCourses;
    }
}
