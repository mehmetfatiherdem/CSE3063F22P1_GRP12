package iteration1.src.course;

import java.util.HashMap;
import java.util.List;

public abstract class ElectiveCourse extends Course {
    protected List<HashMap<String,Integer>> numberToBeTakenEachSeason;

    public ElectiveCourse(){
        super();
    }

    public List<HashMap<String, Integer>> getNumberToBeTakenEachSeason() {
        return numberToBeTakenEachSeason;
    }

    public void setNumberToBeTakenEachSeason(List<HashMap<String, Integer>> numberToBeTakenEachSeason) {
        this.numberToBeTakenEachSeason = numberToBeTakenEachSeason;
    }
}
// add-DEPARTMENT
