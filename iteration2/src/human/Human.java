package iteration2.src.human;

import iteration2.src.input_output.Logger;

public abstract class Human {

    protected String firstName;
    protected String middleName;
    protected String lastName;

    protected Human(String firstName, String middleName, String lastName){
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    protected Human(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getFullName(){
        return firstName + ((middleName == null || middleName.length() <= 0) ? "" : " " + middleName) + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        if(firstName == null || firstName.trim().length() <= 0){
            Logger.log("Human: Human first name must exist and have a length greater than 0");
            return;
        }
        this.firstName = firstName.trim();
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName.trim();
    }

    public void setLastName(String lastName) {
        if(lastName == null || lastName.trim().length() <= 0){
            Logger.log("Human: Human last name must exist and have a length greater than 0");
            return;
        }
        this.lastName = lastName.trim();
    }
}
