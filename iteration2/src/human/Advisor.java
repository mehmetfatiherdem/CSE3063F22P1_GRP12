package iteration2.src.human;

import iteration2.src.course.Section;
import iteration2.src.data_structures.Tuple;
import iteration2.src.input_output.Logger;

import java.util.ArrayList;
import java.util.List;

public class Advisor extends Lecturer {

    public Advisor(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Advisor(String firstName, String middleName, String lastName) {
        super(firstName, middleName, lastName);
    }
    
    /*public void approveRegistration() {
        Logger.log("Advisor approved all courses.");
        
    }*/
    //Kursları mandatory ve elective çakışmasına göre onaylasın ya da onaylamasın.

    public List<Tuple<Section,Section>> examineRegistration(Student student){
        Logger.log("The advisor " + getFullName() + " starts examining the registration of " + student.getFullName());

        List<Section> sections= student.getEnrolledCourses();
        var collisions= Section.checkForCollisions(sections);

        List<Tuple<Section,Section>> unaccepted = new ArrayList<>();

        for(var c: collisions)
            if(!checkTypesOfCollidingSections(c.getKey(),c.getValue()))
                unaccepted.add(c);

        if(unaccepted.size() == 0){
            Logger.log("The advisor " + getFullName() + " approves the registration of " + student.getFullName());
        }
        else{
            Logger.log("The advisor " + getFullName() + " does not approve the registration of " + student.getFullName() + " due to high priority courses having collisions");
        }

        return unaccepted;
    }

    public boolean checkTypesOfCollidingSections(Section section1, Section section2){
        float total = section1.getSectionPriority() + section2.getSectionPriority();
        return total < 5;
    }
}
