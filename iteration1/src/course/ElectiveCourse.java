package iteration1.src.course;

import iteration1.src.human.Student;

public class ElectiveCourse extends Course {

    public ElectiveCourse(String code){
        super(code);
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
