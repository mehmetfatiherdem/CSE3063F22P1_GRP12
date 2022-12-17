package iteration2.src;

import iteration2.src.course.*;
import iteration2.src.data_structures.Tuple;
import iteration2.src.human.*;
import iteration2.src.input_output.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {
     private final String code = "CSE";
     private Season currentSeason;
     List<CourseRecord> records = new ArrayList<>();
     List<Assistant> assistants = new ArrayList<>();
     List<Lecturer> lecturers = new ArrayList<>();
     List<MandatoryCourse> mandatoryCourses = new ArrayList<>();
     List<TechnicalElectiveCourse> technicalElectiveCourses= new ArrayList<>();
     List<FacultyTechnicalElectiveCourse> facultyTechnicalElectiveCourses= new ArrayList<>();
     List<NonTechnicalElectiveCourse> nonTechnicalElectiveCourses= new ArrayList<>();
     List<Student> students= new ArrayList<>();
     List<Advisor> advisors = new ArrayList<>();
     List<Course> courses = new ArrayList<>();
     boolean initialized;

    @BeforeEach
    void setup(){
        lecturers.add(new Lecturer("Borahan","TÜMER"));
        lecturers.add(new Lecturer("Sanem","ARSLAN"));
        lecturers.add(new Lecturer("Ömer","KORÇAK"));
        lecturers.add(new Lecturer("Murat","Can","GANİZ"));
        lecturers.add(new Lecturer("Fatma","Corut","ERGİN"));

        assistants.add(new Assistant("Birol","GENÇYILMAZ"));
        assistants.add(new Assistant("Lokman",null,"ALTIN"));
        assistants.add(new Assistant("Serap",null,"KORKMAZ"));
        assistants.add(new Assistant("Zuhal",null,"ALTUNTAŞ"));
        assistants.add(new Assistant("Lokman",null,"ALTIN"));


        advisors.add(new Advisor("Ali","Haydar","ÖZER"));
        advisors.add(new Advisor("Sanem","ARSLAN"));
        advisors.add(new Advisor("Ömer","KORÇAK"));
        advisors.add(new Advisor("Murat","Can","GANİZ"));
        advisors.add(new Advisor("Fatma","Corut","ERGİN"));

        records.add(new CourseRecord(new MandatoryCourse("CSE 2025","Data Structures",8,3,2,Grade.JUNIOR,Season.FALL,lecturers,assistants),LetterGrade.AA,Season.FALL,Grade.FRESHMAN,90F,Boolean.TRUE));
        records.add(new CourseRecord(new MandatoryCourse("MATH2055", "Differential Equations", 4, 3, 0,Grade.JUNIOR,Season.FALL,lecturers,assistants),LetterGrade.AA,Season.FALL,Grade.FRESHMAN,90F,Boolean.TRUE));
        records.add(new CourseRecord(new MandatoryCourse("CSE3215",  "Digital Logic Design", 6, 3, 2,Grade.JUNIOR, Season.FALL,lecturers,assistants),LetterGrade.AA,Season.FALL,Grade.JUNIOR,90F,Boolean.TRUE));
        records.add(new CourseRecord(new MandatoryCourse("STAT2253",  "Introduction to Probability and Statistics", 5, 3, 0,Grade.SOPHOMORE, Season.SPRING,lecturers,assistants),LetterGrade.AA,Season.FALL,Grade.FRESHMAN,90F,Boolean.TRUE));

        students.add(new Student("Ahmet","Şahin ","150115656", Grade.FRESHMAN , new Advisor("Fatma","Corut","ERGİN"),records));
        students.add(new Student("Mehmet","Kerim ","150118857", Grade.JUNIOR , new Advisor("Murat","Can","GANİZ"),records));
        students.add(new Student("Sıla","Demir ","150119075", Grade.SENIOR , new Advisor("Ali","Haydar","ÖZER"),records));
        students.add(new Student("Ayşe","Bal ","150120777", Grade.SOPHOMORE , new Advisor("Murat","Can","GANİZ"),records));
        students.add(new Student("Halil","Yalmaç ","150121682", Grade.SOPHOMORE , new Advisor("Fatma","Corut","ERGİN"),records));

        lecturers.addAll(advisors);
        var season = Department.getInstance().getCurrentSeason();

        Department department = Department.getInstance();
        department.initialize(season,courses,lecturers,assistants,advisors,students);
        courses = Department.getInstance().getAllCourses();
        initialized = false;
        System.out.println();
        System.out.println("Department test starting...");
        System.out.println();
    }

    @Test
    void initialize() {
        if(initialized)
            return;

        if(courses == null || lecturers == null || assistants == null || advisors == null || students == null)
            return;

        currentSeason = Department.getInstance().getCurrentSeason() ;

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
    void generateWeeklyScheduleForAllCourses(){
         generateWeeklyScheduleForMandatoryCourses();
         generateWeeklyScheduleForElectiveCourses();
    }
    void generateWeeklyScheduleForMandatoryCourses(){
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

                course.addCourseSection(schedules.GetKey());
                course.addLabSection(schedules.GetValue());
            }
        }
    }
    void generateWeeklyScheduleForElectiveCourses(){
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
            List<Integer> theoreticalDivision = division.GetKey();
            availableClassHours = getScheduleAtPosition(0,40);
            long schedule = getRandomDividedScheduleFrom(availableClassHours,theoreticalDivision);
            technicalElectives.get(i).addCourseSection(schedule);
        }
    }
    List<Tuple<Long,Long>> generateCollisionlessWeeklyScheduleForCourses(List<Course> courses){
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
    List<Tuple<List<Integer>,List<Integer>>> getDivisions(List<Course> courses){
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
    public void addNewCourseSection(MandatoryCourse course){
        long schedule = getNewSectionSchedule(course,course.getTheoreticalHours());
        course.addCourseSection(schedule);
    }
    public void addNewLabSection(MandatoryCourse course){
        long schedule = getNewSectionSchedule(course,course.getAppliedHours());
        course.addLabSection(schedule);
    }
    @Test
    void getCode() {
        assertEquals("CSE",Department.getInstance().getCode());
    }
    @Test
    void getCurrentSeason() {
        assertEquals(Department.getInstance().getCurrentSeason(),currentSeason);
    }

}