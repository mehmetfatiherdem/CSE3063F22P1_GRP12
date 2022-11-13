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
        // electives have only 1 section
        System.out.println("The quota for the elective course " + this.getCode() + " is already full. So, you cannot take this.");
        return this.getQuota() <= this.getSectionList().get(0).getStudentList().size();
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    @Override
    public void addToSectionList(Section section) {

        if(this.getSectionList().size() < 1){
            this.getSectionList().add(section);
        }else{
            System.out.println("You cannot add more than one section to an elective course..");
        }

    }
}
