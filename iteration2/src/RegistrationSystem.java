package iteration2.src;

import iteration2.src.course.*;
import iteration2.src.data_structures.Tuple;
import iteration2.src.human.Student;
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

        List<MandatoryCourse> openMandatoryCourses = new ArrayList<>();
        for(MandatoryCourse c : mandatoryCourses){
            if(c.canStudentTakeCourse(student))
                openMandatoryCourses.add(c);
        }

        return openMandatoryCourses;
    }

    public List<NonTechnicalElectiveCourse> getOpenNonTechnicalElectiveCourses(Student student){
        var nteCourses = Department.getInstance().getNonTechnicalElectiveCourses();

        List<NonTechnicalElectiveCourse> openNTECourses = new ArrayList<>();
        for(NonTechnicalElectiveCourse c : nteCourses){
            if(c.canStudentTakeCourse(student))
                openNTECourses.add(c);
        }

        return openNTECourses;
    }

    public List<FacultyTechnicalElectiveCourse> getOpenFacultyTechnicalElectiveCourses(Student student){
        var fteCourses = Department.getInstance().getFacultyTechnicalElectiveCourses();

        List<FacultyTechnicalElectiveCourse> openFTECourses = new ArrayList<>();
        for(FacultyTechnicalElectiveCourse c : fteCourses){
            if(c.canStudentTakeCourse(student))
                openFTECourses.add(c);
        }

        return openFTECourses;
    }

    public List<TechnicalElectiveCourse> getOpenTechnicalElectiveCourses(Student student){
        var teCourses = Department.getInstance().getTechnicalElectiveCourses();

        List<TechnicalElectiveCourse> openTECourses = new ArrayList<>();
        for(TechnicalElectiveCourse c : teCourses){
            if(c.canStudentTakeCourse(student))
                openTECourses.add(c);
        }

        return openTECourses;
    }

    public int getTheNumberOfTechnicalElectiveCoursesStudentCanTake(Student student){
        int noOfTEPassed = student.getTranscript().getNumberOfTElectivesPassed();
        int noOfTERequired = TechnicalElectiveCourse.getTotalNumberOfCoursesUntilSemester(student.getStudentSemester());
        return noOfTERequired - noOfTEPassed;
    }

    public int getTheNumberOfNonTechnicalElectiveCoursesStudentCanTake(Student student){
        int noOfNTEPassed = student.getTranscript().getNumberOfNTElectivesPassed();
        int noOfNTERequired = TechnicalElectiveCourse.getTotalNumberOfCoursesUntilSemester(student.getStudentSemester());
        return noOfNTERequired - noOfNTEPassed;
    }

    public int getTheNumberOfFacultyTechnicalElectiveCoursesStudentCanTake(Student student){
        int noOfFTEPassed = student.getTranscript().getNumberOfFTElectivesPassed();
        int noOfFTERequired = TechnicalElectiveCourse.getTotalNumberOfCoursesUntilSemester(student.getStudentSemester());
        return noOfFTERequired - noOfFTEPassed;
    }

    public List<Tuple<Section,Section>> checkEnrolledSections(Student student){

        var collisions = Section.checkForCollisions(student.getEnrolledCourses());

        List<Tuple<Section,Section>> highlyCollidingSections = new ArrayList<>();

        for(var c : collisions){
            Section sec1 = c.getKey();
            Section sec2 = c.getValue();

            var collisionsBetween = sec1.getCollisionsWith(sec2);
            int noOfCollisions = collisionsBetween.size();

            if(noOfCollisions >= 2) {
                highlyCollidingSections.add(c);

                Logger.log("The courses " + sec1.toString() + " ,and " + sec2.toString() + " have a collision of length " + noOfCollisions);
                Logger.log("Two courses can not collide by more than 2 hours!");
                continue;
            }

            for(var d : collisionsBetween){
                int collisionDay = d.getKey();
                int collisionHour = d.getValue();

                Logger.log("Warning : There is a collision between " + sec1.toString()
                        + " and " + sec2.toString() + " on " + Section.CLASS_DAYS[collisionDay] + " at " + Section.CLASS_HOURS[collisionHour]);
            }
        }

        Logger.log("");

        if(highlyCollidingSections.size() == 0)
            Logger.log(student.getFullName() + "'s selected courses are adequate to send to advisor approval!");
        else
            Logger.log("There are preventing collisions among the courses " + student.getFullName() + " has selected!");

        return highlyCollidingSections;
    }

    public List<Tuple<Section,Section>> sendToAdvisorApproval(Student student){
        return student.getAdvisor().examineRegistration(student);
    }
}
