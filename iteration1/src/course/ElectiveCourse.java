package iteration1.src.course;


public class ElectiveCourse extends Course {

    public ElectiveCourse(String code, int quota){
        super(code, quota);
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
