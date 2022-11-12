package iteration1.src.course;

public class ElectiveCourse extends Course {

    public boolean isQuotaFull(){
        return this.getQuota() <= this.getStudentList().size();
    }
}
