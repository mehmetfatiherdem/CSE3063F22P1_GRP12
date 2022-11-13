package iteration1.src.human;

public class Advisor extends Lecturer {

    public Advisor(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Advisor(String firstName, String middleName, String lastName) {
        super(firstName, middleName, lastName);
    }
    
    public void approveRegistration() {
        System.out.println("Advisor approved all courses.");
        
    }
    
}
