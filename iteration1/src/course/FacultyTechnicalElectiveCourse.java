package iteration1.src.course;

import iteration1.src.human.Grade;

public class FacultyTechnicalElectiveCourse extends Course{

    public FacultyTechnicalElectiveCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                                          Grade firstYearToTake, Season firstSeasonToTake){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake);
    }

}
