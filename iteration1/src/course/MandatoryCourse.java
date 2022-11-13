package iteration1.src.course;

import iteration1.src.human.Student;

public class MandatoryCourse extends Course{


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
        //      if(!c.isPassed) return c.isPassed;
        //}
        System.out.println("You don't meet the prerequisite requirement for the " + this.getCode());
        return arePrerequisitesPassed;
    }

    public MandatoryCourse openANewSection(){
        if(this.getQuota()==this.getStudentList().size()){
            return new MandatoryCourse(); //TODO: add the name like CODE.2/3/4/..
        }

        return null;
    }
}
