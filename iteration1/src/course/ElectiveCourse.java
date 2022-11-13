package iteration1.src.course;

import iteration1.src.human.Student;

public class ElectiveCourse extends Course {

    public ElectiveCourse(String code){
        super(code);
    }

    private int quota;

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

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }
}
