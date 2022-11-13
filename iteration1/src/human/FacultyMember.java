package iteration1.src.human;

import iteration1.src.Department;

public abstract class FacultyMember extends Human {

    protected Department department;

    protected FacultyMember(String firstName, String lastName) {
        super(firstName, lastName);
        department = Department.getInstance();
    }

    protected FacultyMember(String firstName,String middleName,String lastName){
        super(firstName,middleName, lastName);
        this.department = Department.getInstance();
    }
}
