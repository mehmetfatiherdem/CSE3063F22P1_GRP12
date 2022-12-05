package iteration2.src;

import iteration2.src.course.*;
import iteration2.src.data_structures.Tuple;
import iteration2.src.human.*;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private static Department instance;

    private final String code = "CSE";
    private Season currentSeason;
    private List<Course> courses;
    private List<Student> students;
    private List<Lecturer> lecturers;
    private List<Assistant> assistants;
    private List<Advisor> advisors;
    private boolean initialized;

    private Department() {
        initialized = false;
    }

    public static Department getInstance() {
        if (instance == null)
            instance = new Department();

        return instance;
    }

    public void initialize(Season currentSeason,List<Course> courses,
                           List<Lecturer> lecturers,List<Assistant> assistants,List<Advisor> advisors,List<Student> students){
        if(initialized)
            return;

        if(courses == null || lecturers == null || assistants == null || advisors == null || students == null)
            return;

        this.currentSeason = currentSeason;
        this.courses = courses;
        this.lecturers = lecturers;
        this.assistants = assistants;
        this.advisors = advisors;
        this.students = students;

        initialized = true;

        assignFacultyMembersToCourses();
        generateWeeklyScheduleForAllCourses();
    }

    // Getters
    public String getCode() {
        return code;
    }
    public Season getCurrentSeason(){
        return currentSeason;
    }
    public List<Course> getCourses() {
        return courses;
    }
    public List<Student> getStudents() {
        return students;
    }


    private void assignFacultyMembersToCourses(){
        //TODO: will be added in the future iterations
    }

    private void generateWeeklyScheduleForAllCourses(){
        List<Course> mandatoryCourses = new ArrayList<>();
        List<Course> nonTechnicalElectiveCourses = new ArrayList<>();
        List<Course> technicalElectiveCourses = new ArrayList<>();

        for(Course c : courses){
            if(c instanceof MandatoryCourse)
                mandatoryCourses.add(c);
            else
                nonTechnicalElectiveCourses.add(c);
        }

        generateWeeklyScheduleForMandatoryCourses(mandatoryCourses);
        generateWeeklyScheduleForElectiveCourses(nonTechnicalElectiveCourses, technicalElectiveCourses);
    }

    private void generateWeeklyScheduleForMandatoryCourses(List<Course> mandatoryCourses){
        for(int semester = 0; semester < 8; semester++){
            int grade = semester/2;
            int season = semester % 2;

            List<Course> mandatoryCoursesForSemester = new ArrayList<>();

            for(Course c : mandatoryCourses)
                if(c.getFirstYearToTake().getValue() == grade && c.getFirstSeasonToTake().getValue() == season)
                    mandatoryCoursesForSemester.add(c);

            List<Tuple<Long,Long>> courseSchedules = generateCollisionlessWeeklyScheduleForCourses(mandatoryCoursesForSemester);

            int len = mandatoryCoursesForSemester.size();

            for(int i = 0; i < len; i++){
                Course course = mandatoryCoursesForSemester.get(i);
                var schedules = courseSchedules.get(i);

                if(course.getTheoreticalHours() != 0)
                    course.addCourseSection(schedules.GetKey());

                if(course.getAppliedHours() != 0)
                    course.addLabSection(schedules.GetValue());
            }
        }
    }
    private void generateWeeklyScheduleForElectiveCourses(List<Course> nonTechnicalElectives, List<Course> technicalElectives){
        long availableClassHours;

        for(Course c : nonTechnicalElectives){
            availableClassHours = Section.getScheduleAtPosition(0,48);
            long schedule = getRandomScheduleFrom(availableClassHours,c.getTheoreticalHours());
            c.addCourseSection(schedule);
        }

        List<Tuple<List<Integer>,List<Integer>>> divisions = getDivisions(technicalElectives);

        int len = divisions.size();

        for(int i = 0; i < len ; i++){
            var division = divisions.get(i);
            List<Integer> theoreticalDivision = division.GetKey();

            long schedule = 0L;
            availableClassHours = Section.getScheduleAtPosition(0,40);

            for(int noOfClassHours : theoreticalDivision){
                long divisionSchedule = getRandomScheduleFrom(availableClassHours, noOfClassHours);
                availableClassHours ^= divisionSchedule;
                schedule |= divisionSchedule;
            }

            technicalElectives.get(i).addCourseSection(schedule);
        }
    }
    private List<Tuple<Long,Long>> generateCollisionlessWeeklyScheduleForCourses(List<Course> courses){
        List<Tuple<List<Integer>,List<Integer>>> courseDivisons = getDivisions(courses);

        boolean failedAttempt;
        long availableClassHours;

        List<Tuple<Long,Long>> courseSchedules;

        int len = courseDivisons.size();

        do {
            failedAttempt = false;
            courseSchedules = new ArrayList<>();

            availableClassHours = Section.getScheduleAtPosition(0,40);

            for (int i = 0; i < len; i++){
                var courseDivisions = courseDivisons.get(i);
                List<Integer> theoreticalDivision = courseDivisions.GetKey();
                List<Integer> appliedDivision = courseDivisions.GetValue();

                long theoreticalSchedule = assignScheduleToSection(availableClassHours,theoreticalDivision);

                if(theoreticalSchedule == -1){
                    failedAttempt = true;
                    break;
                }

                availableClassHours ^= theoreticalSchedule;

                long appliedSchedule = assignScheduleToSection(availableClassHours,appliedDivision);

                if(appliedSchedule == -1){
                    failedAttempt = true;
                    break;
                }

                availableClassHours ^= appliedSchedule;

                courseSchedules.add(new Tuple<Long,Long>(theoreticalSchedule,appliedSchedule));
            }
        }while(failedAttempt);

        return courseSchedules;
    }
    private List<Tuple<List<Integer>,List<Integer>>> getDivisions(List<Course> courses){
        List<Tuple<List<Integer>,List<Integer>>> divisions = new ArrayList<>();

        for(Course c : courses){
            int theoreticalHours = c.getTheoreticalHours();
            int appliedHours = c.getAppliedHours();

            List<Integer> theoreticalHoursDivision = getDivision(theoreticalHours);
            List<Integer> appliedHoursDivision = getDivision(appliedHours);
            divisions.add(new Tuple<>(theoreticalHoursDivision,appliedHoursDivision));
        }

        return divisions;
    }
    private List<Integer> getDivision(int noOfHours){
        List<Integer> division = new ArrayList<>();

        float threeConsecutiveHoursCumulativeProbability = 0.05f;
        float twoConsecutiveHoursCumulativeProbability = 0.80f;

        while(!validateDivision(division,noOfHours)){
            division = new ArrayList<>();
            int remainder = noOfHours;

            for(int i = 0; i < 3; i++){
                float random = RandomNumberGenerator.RandomFloat();

                if(random <= threeConsecutiveHoursCumulativeProbability && remainder >= 3){
                    division.add(3);
                    remainder -= 3;
                }
                else if(random <= twoConsecutiveHoursCumulativeProbability && remainder >= 2){
                    division.add(2);
                    remainder -= 2;
                }
                else if(remainder >= 1){
                    division.add(1);
                    remainder--;
                }
            }
        }

        return division;
    }
    private boolean validateDivision(List<Integer> division, int noOfHours){
        int sum = 0;
        int oneHourCounter = 0;

        for (int element : division){
            sum += element;

            if(element == 1)
                oneHourCounter++;
        }

        return sum == noOfHours && oneHourCounter <=1;
    }
    private long assignScheduleToSection(long currentWeeklySchedule,List<Integer> division){
        long sectionSchedule = 0L;
        List<Integer> daysUsed = new ArrayList<>();

        for(int noOfHours : division){
            var availableDays = getAvailableDays(currentWeeklySchedule,noOfHours);

            for (int day : daysUsed)
                if(availableDays.contains(day))
                    availableDays.remove((Object)day);

            if(availableDays.size() == 0){
                sectionSchedule = -1;
                break;
            }

            int randomDay = RandomNumberGenerator.randomIntegerBetween(0,availableDays.size());
            randomDay = availableDays.get(randomDay);
            daysUsed.add(randomDay);

            var availableHours = getAvailablePositionsOnDay(currentWeeklySchedule,randomDay,noOfHours);
            int randomStartingHour = RandomNumberGenerator.randomIntegerBetween(0,availableHours.size());
            randomStartingHour = availableHours.get(randomStartingHour);

            long scheduleForTheseHours = Section.getScheduleAtPosition(randomStartingHour,noOfHours);
            sectionSchedule |= scheduleForTheseHours;
        }

        return sectionSchedule;
    }
    private List<Integer> getAvailableDays(long schedule, int count){
        List<Integer> availableDays = new ArrayList<>();

        int limit = 8 - count + 1;

        for(int i = 0; i < 5; i++){
            int position = i * 8;
            long requestedHours = Section.getScheduleAtPosition(position,count);

            for(int j = 0; j < limit; j++){
                if((schedule & requestedHours) == requestedHours){
                    availableDays.add(i);
                    break;
                }

                requestedHours <<= 1;
            }
        }

        return availableDays;
    }
    private List<Integer> getAvailablePositionsOnDay(long schedule, int dayIndex, int count){
        int position = dayIndex * 8;
        long requestedHours = Section.getScheduleAtPosition(position,count);

        List<Integer> availablePositions = new ArrayList<>();

        int limit = 8 - count + 1;

        for(int i = 0; i < limit; i++){
            if((schedule & requestedHours) == requestedHours)
                availablePositions.add(position + i);

            requestedHours <<= 1;
        }

        return availablePositions;
    }
    private long getRandomScheduleFrom(long availableClassHours, int noOfClassHours){
        List<Integer> availablePositions = new ArrayList<>();

        for(int day = 0; day < 5; day++)
            availablePositions.addAll(getAvailablePositionsOnDay(availableClassHours,day,noOfClassHours));

        int randomPosition = RandomNumberGenerator.randomIntegerBetween(0,availablePositions.size());
        randomPosition = availablePositions.get(randomPosition);

        return Section.getScheduleAtPosition(randomPosition,noOfClassHours);
    }
}
