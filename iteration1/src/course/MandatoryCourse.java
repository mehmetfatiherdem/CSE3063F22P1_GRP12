package iteration1.src.course;

import iteration1.src.human.Student;

public class MandatoryCourse extends Course{

    public boolean checkIfPrerequisitesArePassed(Student student){
        boolean arePrerequisitesPassed = true;
        // var takenCourses = student.getTranscript().getTakenCourseRecords()
        // for(CourseRecord c: takenCourses){
        //      if(!c.isPassed) return c.isPassed;
        //}

        return arePrerequisitesPassed;
    }

    public MandatoryCourse openANewSection(){
        if(this.getQuota()==this.getStudentList().size()){
            return new MandatoryCourse(); //TODO: add the name like CODE.2/3/4/..
        }

        return null;
    }
}
