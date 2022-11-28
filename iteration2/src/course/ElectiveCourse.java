package iteration2.src.course;


import iteration2.src.human.Grade;

public class ElectiveCourse extends Course {

    public ElectiveCourse(String code, String name, int credits, int theoreticalHours, int appliedHours,
                          Grade firstYearToTake, Season firstSeasonToTake){
        super(code,name,credits,theoreticalHours,appliedHours,firstYearToTake,firstSeasonToTake);
    }

    @Override
    public void addPrerequisite(Course prerequisite){
        return;
    }
}
