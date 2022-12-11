package iteration2.src.human;

import iteration2.src.Department;

public abstract class FacultyMember extends Human {

    protected Department department;

    protected FacultyMember(String firstName, String lastName) {
        this(firstName,null,lastName);
    }

    protected FacultyMember(String firstName,String middleName,String lastName){
        super(firstName,middleName, lastName);
        this.department = Department.getInstance();
    }
}
