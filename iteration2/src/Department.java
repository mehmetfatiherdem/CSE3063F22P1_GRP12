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
    private List<MandatoryCourse> mandatoryCourses;
    private List<TechnicalElectiveCourse> technicalElectiveCourses;
    private List<FacultyTechnicalElectiveCourse> facultyTechnicalElectiveCourses;
    private List<NonTechnicalElectiveCourse> nonTechnicalElectiveCourses;
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
        this.lecturers = lecturers;
        this.assistants = assistants;
        this.advisors = advisors;
        this.students = students;

        mandatoryCourses = new ArrayList<>();
        technicalElectiveCourses = new ArrayList<>();
        facultyTechnicalElectiveCourses = new ArrayList<>();
        nonTechnicalElectiveCourses = new ArrayList<>();

        for (Course c : courses){
            if(c instanceof MandatoryCourse)
                mandatoryCourses.add((MandatoryCourse) c);
            else if(c instanceof TechnicalElectiveCourse)
                technicalElectiveCourses.add((TechnicalElectiveCourse) c);
            else if(c instanceof FacultyTechnicalElectiveCourse)
                facultyTechnicalElectiveCourses.add((FacultyTechnicalElectiveCourse) c);
            else if(c instanceof NonTechnicalElectiveCourse)
                nonTechnicalElectiveCourses.add((NonTechnicalElectiveCourse) c);
        }

        initialized = true;

        generateWeeklyScheduleForAllCourses();
    }

    public void addNewCourseSection(MandatoryCourse course){
        long schedule = getNewSectionSchedule(course,course.getTheoreticalHours());
        course.addCourseSection(schedule);
    }

    public void addNewLabSection(MandatoryCourse course){
        long schedule = getNewSectionSchedule(course,course.getAppliedHours());
        course.addLabSection(schedule);
    }

    // Getters
    public String getCode() {
        return code;
    }
    public Season getCurrentSeason(){
        return currentSeason;
    }
    public List<Student> getStudents() {
        return students;
    }
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>(mandatoryCourses);
        courses.addAll(technicalElectiveCourses);
        courses.addAll(facultyTechnicalElectiveCourses);
        courses.addAll(nonTechnicalElectiveCourses);

        return courses;
    }

    public List<MandatoryCourse> getMandatoryCourses(){
        return mandatoryCourses;
    }

    public List<TechnicalElectiveCourse> getTechnicalElectiveCourses(){
        return technicalElectiveCourses;
    }

    public List<FacultyTechnicalElectiveCourse> getFacultyTechnicalElectiveCourses(){
        return facultyTechnicalElectiveCourses;
    }

    public List<NonTechnicalElectiveCourse> getNonTechnicalElectiveCourses(){
        return nonTechnicalElectiveCourses;
    }

    private long getNewSectionSchedule(MandatoryCourse course, int classHours){
        List<MandatoryCourse> semesterCourses = new ArrayList<>();

        for(MandatoryCourse c : mandatoryCourses)
            if(course.getFirstYearToTake() == c.getFirstYearToTake() && course.getFirstSeasonToTake() == c.getFirstSeasonToTake())
                semesterCourses.add(c);

        long availableClassHours = getScheduleAtPosition(0,40);

        for(MandatoryCourse c : semesterCourses)
            for(Section s : c.getAllSections())
                availableClassHours ^= s.getClassSchedule();

        List<Integer> division = getDivision(classHours);
        long schedule = getRandomDividedScheduleFrom(availableClassHours,division);

        int checker = 0;

        for(int i = 0; i < 40; i++)
            if((schedule & (1 << i)) > 0)
                checker++;

        if(checker < classHours){
            availableClassHours = getScheduleAtPosition(0,40);
            schedule = getRandomDividedScheduleFrom(availableClassHours,division);
        }

        return schedule;
    }
    private void generateWeeklyScheduleForAllCourses(){
        generateWeeklyScheduleForMandatoryCourses();
        generateWeeklyScheduleForElectiveCourses();
    }

    private void generateWeeklyScheduleForMandatoryCourses(){
        for(int semester = 0; semester < 8; semester++){
            List<Course> mandatoryCoursesForSemester = new ArrayList<>();

            for(Course c : mandatoryCourses)
                if(semester == c.getCourseSemester())
                    mandatoryCoursesForSemester.add(c);

            List<Tuple<Long,Long>> courseSchedules = generateCollisionlessWeeklyScheduleForCourses(mandatoryCoursesForSemester);

            int len = mandatoryCoursesForSemester.size();

            for(int i = 0; i < len; i++){
                Course course = mandatoryCoursesForSemester.get(i);
                var schedules = courseSchedules.get(i);

                course.addCourseSection(schedules.getKey());
                course.addLabSection(schedules.getValue());
            }
        }
    }
    private void generateWeeklyScheduleForElectiveCourses(){
        List<Course> technicalElectives = new ArrayList<>(technicalElectiveCourses);
        technicalElectives.addAll(facultyTechnicalElectiveCourses);

        long availableClassHours;

        for(Course c : nonTechnicalElectiveCourses){
            availableClassHours = getScheduleAtPosition(0,48);
            long schedule = getRandomScheduleFrom(availableClassHours,c.getTheoreticalHours());
            c.addCourseSection(schedule);
        }

        List<Tuple<List<Integer>,List<Integer>>> divisions = getDivisions(technicalElectives);

        int len = divisions.size();

        for(int i = 0; i < len ; i++){
            var division = divisions.get(i);
            List<Integer> theoreticalDivision = division.getKey();
            availableClassHours = getScheduleAtPosition(0,40);
            long schedule = getRandomDividedScheduleFrom(availableClassHours,theoreticalDivision);
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

            availableClassHours = getScheduleAtPosition(0,40);

            for (int i = 0; i < len; i++){
                var courseDivisions = courseDivisons.get(i);
                List<Integer> theoreticalDivision = courseDivisions.getKey();
                List<Integer> appliedDivision = courseDivisions.getValue();

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

            long scheduleForTheseHours = getScheduleAtPosition(randomStartingHour,noOfHours);
            sectionSchedule |= scheduleForTheseHours;
        }

        return sectionSchedule;
    }
    private List<Integer> getAvailableDays(long schedule, int count){
        List<Integer> availableDays = new ArrayList<>();

        int limit = 8 - count + 1;

        for(int i = 0; i < 5; i++){
            int position = i * 8;
            long requestedHours = getScheduleAtPosition(position,count);

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
        long requestedHours = getScheduleAtPosition(position,count);

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

        try {
            randomPosition = availablePositions.get(randomPosition);
        }catch (Exception e){
            availableClassHours = getScheduleAtPosition(0,40);
            return getRandomScheduleFrom(availableClassHours,noOfClassHours);
        }

        return getScheduleAtPosition(randomPosition,noOfClassHours);
    }
    private long getRandomDividedScheduleFrom(long availableClassHours, List<Integer> division){
        long schedule = 0L;

        for(int noOfClassHours : division){
            long divisionSchedule = getRandomScheduleFrom(availableClassHours, noOfClassHours);
            availableClassHours ^= divisionSchedule;
            schedule |= divisionSchedule;
        }

        return schedule;
    }
    private long getScheduleAtPosition(int position, int count){
        long schedule = -1L;
        schedule >>>= (64 - count);
        schedule <<= position;
        return schedule;
    }
}
