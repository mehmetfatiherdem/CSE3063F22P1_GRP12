package iteration1.src.course;

import iteration1.src.human.Student;

public class ElectiveCourse extends Course {

    @Override
    public Boolean canStudentTakeCourse(Student student) {

        boolean isStudentAbleToTake = true;
        if(isQuotaFull()){
            isStudentAbleToTake = false;
        }

        return isStudentAbleToTake;
    }

    public boolean isQuotaFull(){
        return this.getQuota() <= this.getStudentList().size();
    }
}
