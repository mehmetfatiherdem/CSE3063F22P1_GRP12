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
        this(firstName,null,lastName);
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
}
