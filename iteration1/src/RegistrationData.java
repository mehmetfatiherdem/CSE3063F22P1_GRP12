package iteration1.src;

import iteration1.src.course.Section;

import java.util.ArrayList;
import java.util.List;

public class RegistrationData {

    public enum Season {
        FALL,
        SPRING,
        SUMMER
    }

    private int year;
    private Season season;
    private List<Section> openSections = new ArrayList<>();

    public RegistrationData( int year, Season season, List<Section> openSections){
        this.year = year;
        this.season = season;
        this.openSections = openSections;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public List<Section> getOpenSections() {
        return openSections;
    }

    public void addOpenSection(Section section){
        this.addOpenSection(section);
    }

}
