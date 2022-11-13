package iteration1.src.course;

import iteration1.src.human.Student;

import java.util.ArrayList;
import java.util.List;

public class MandatoryCourse extends Course{

    private List<MandatoryCourse> prerequisites = new ArrayList<>();

    public MandatoryCourse(String code){
        super(code);
    }

    @Override
    public Boolean canStudentTakeCourse(Student student) {

        boolean isStudentAbleToTake = true;
        if(!checkIfPrerequisitesArePassed(student)){
            isStudentAbleToTake = false;
        }

        return isStudentAbleToTake;
    }

    public boolean checkIfPrerequisitesArePassed(Student student){
        boolean arePrerequisitesPassed = true;
        // var takenCourses = student.getTranscript().getTakenCourseRecords()
        // for(CourseRecord c: takenCourses){
        //      if(!c.isPassed){
        //      System.out.println("You don't meet the prerequisite requirement for the " + this.getCode());
        //      return c.isPassed;
        //  }
        // }
        return arePrerequisitesPassed;
    }

    public Section openANewSection(Section section){
         /*
        if(getQuota()==this.getStudentList().size()){

            if(){

            }
            return new Cou; //TODO: add the name like CODE.2/3/4/..

        }

          */
        return null;
    }

    public List<MandatoryCourse> getPrerequisites() {
        return prerequisites;
    }

    public void addToPrerequisites(MandatoryCourse course){
        this.prerequisites.add(course);
    }
}
