package iteration2.src.human;

import iteration2.src.input_output.Logger;

public class Advisor extends Lecturer {

    public Advisor(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Advisor(String firstName, String middleName, String lastName) {
        super(firstName, middleName, lastName);
    }
    
    public void approveRegistration() {
        Logger.log("Advisor approved all courses.");
        
    }
    
}
