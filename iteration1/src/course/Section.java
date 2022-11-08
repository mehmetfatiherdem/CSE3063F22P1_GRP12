package iteration1.src.course;

import java.util.ArrayList;
import java.util.List;
import iteration1.src.data_structures.Tuple;
import iteration1.src.human.FacultyMember;

public abstract class Section {

    protected Course course;
    protected long classHours;
    protected FacultyMember instructor;

    protected Section(Course course,long classHours,FacultyMember instructor){
        this.course = course;
        this.classHours = classHours;
        this.instructor = instructor;
    }

    public static ArrayList<Tuple<Section,Section>> checkForCollisions(List<Section> classes){
        ArrayList<Section> combinedSections = new ArrayList<>();
        ArrayList<Tuple<Section,Section>> collisions = new ArrayList<>();

        long combinedSchedule = 0;

        for(Section sec : classes){
            if(checkCollision(combinedSchedule, sec.classHours)){
                for(Section s : combinedSections){
                    if(checkCollision(s.classHours, sec.classHours)){
                        collisions.add(new Tuple<Section,Section>(sec,s));
                    }
                }
                
                continue;
            }

            combinedSchedule = combineSchedules(combinedSchedule, sec.classHours);
            combinedSections.add(sec);
        }

        return collisions;
    }

    public static long getCombinedClassHours(List<Section> sections){
        long combinedSchedule = 0;

        for(Section sec : sections){
            combinedSchedule = combineSchedules(combinedSchedule, sec.classHours);
        }

        return combinedSchedule;
    }

    public Course getCourse(){
        return course;
    }

    public void setCourse(Course course){
        this.course = course;
    }

    public long getClassHours(){
        return classHours;
    }

    public void setClassHours(long classHours){
        this.classHours = classHours;
    }

    private static long combineSchedules(long sch1,long sch2){
        return sch1 | sch2;
    }

    private static Boolean checkCollision(long sch1,long sch2){
        return (sch1 & sch2) != 0;
    }
}
