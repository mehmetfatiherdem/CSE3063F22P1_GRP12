package iteration1.src.input_output;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import iteration1.src.course.*;
import iteration1.src.human.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonParser {

    private static final String advisorsFile = "iteration1/resources/Advisors.json";
    private static final String assistantsFile = "iteration1/resources/Assistants.json";
    private static final String lecturersFile = "iteration1/resources/Lecturers.json";
    private static final String coursesFile = "iteration1/resources/Courses.json";
    private static final String semesterFile = "iteration1/resources/Semester.json";
    private static final String studentsDir = "iteration1/resources/students/";

    private JsonParser(){

    }

    public static Season parseSemester(){

        String semester = (String) readJsonFile(semesterFile);
        return Season.valueOf(semester);
    }

    public static List<Advisor> parseAdvisors(){

        JSONArray arr = (JSONArray) readJsonFile(advisorsFile);

        //parse each advisor

        List<Advisor> advisorList = new ArrayList<>();

        for (Object adv : arr){

            StringBuilder firstName = new StringBuilder();
            StringBuilder middleName = new StringBuilder();
            StringBuilder lastName = new StringBuilder();
            parseHuman((JSONObject)adv,firstName,middleName,lastName);

            String mName = isNull(middleName.toString()) ? null : middleName.toString();

            advisorList.add(new Advisor(firstName.toString(),mName,lastName.toString()));
        }

        return advisorList;
    }

    public static List<Assistant> parseAssistants() {

        JSONArray arr = (JSONArray) readJsonFile(assistantsFile);

        //parse each assistant

        List<Assistant> assistantList = new ArrayList<>();

        for (Object adv : arr) {

            StringBuilder firstName = new StringBuilder();
            StringBuilder middleName = new StringBuilder();
            StringBuilder lastName = new StringBuilder();
            parseHuman((JSONObject) adv, firstName, middleName, lastName);

            String mName = isNull(middleName.toString()) ? null : middleName.toString();

            assistantList.add(new Assistant(firstName.toString(), mName, lastName.toString()));
        }

        return assistantList;
    }

    public static List<Lecturer> parseLecturers(){

        JSONArray arr = (JSONArray) readJsonFile(lecturersFile);

        //parse each lecturer

        List<Lecturer> lecturerList = new ArrayList<>();

        for (Object adv : arr){

            StringBuilder firstName = new StringBuilder();
            StringBuilder middleName = new StringBuilder();
            StringBuilder lastName = new StringBuilder();
            parseHuman((JSONObject)adv,firstName,middleName,lastName);

            String mName = isNull(middleName.toString()) ? null : middleName.toString();

            lecturerList.add(new Lecturer(firstName.toString(),mName,lastName.toString()));
        }

        return lecturerList;
    }

    public static List<Course> parseCourses(){

        JSONArray arr = (JSONArray) readJsonFile(coursesFile);

        List<JSONArray> prerequisiteCodes = new ArrayList<>();
        List<Course> courseList = new ArrayList<>();

        for (Object c : arr){

            JSONObject courseJson = (JSONObject)c;
            String courseCode = (String) courseJson.get("CourseCode");
            String courseName = (String) courseJson.get("CourseName");
            String courseType = (String) courseJson.get("CourseType");

            int credits = (int)(long) courseJson.get("Credits");
            int theoreticalHours = (int)(long) courseJson.get("TheoreticalHours");
            int appliedHours = (int)(long) courseJson.get("AppliedHours");

            JSONArray codes = (JSONArray) courseJson.get("PrerequisiteCodes");
            prerequisiteCodes.add(codes);

            Grade firstGradeToTake = Grade.valueOf((String) courseJson.get("FirstGradeToTake"));
            Season firstSeasonToTake = Season.valueOf((String) courseJson.get("FirstSeasonToTake"));

            // add to list
            Course course = null;

            switch (courseType){
                case "Mandatory":
                    course = new MandatoryCourse(courseCode,courseName,credits,
                            theoreticalHours,appliedHours,firstGradeToTake,firstSeasonToTake);
                    break;
                case "Faculty Technical Elective":
                    course = new FacultyTechnicalElectiveCourse(courseCode,courseName,credits,
                            theoreticalHours,appliedHours,firstGradeToTake,firstSeasonToTake);
                    break;
                case "Technical Elective":
                    course = new TechnicalElectiveCourse(courseCode,courseName,credits,
                            theoreticalHours,appliedHours,firstGradeToTake,firstSeasonToTake);
                    break;
                case "Non-Technical Elective | University Elective":
                    course = new NonTechnicalElectiveCourse(courseCode,courseName,credits,
                            theoreticalHours,appliedHours,firstGradeToTake,firstSeasonToTake);
                    break;
            }

            courseList.add(course);
        }

        assignPrerequisitesToCourses(courseList,prerequisiteCodes);

        return courseList;
    }

    public static List<Student> parseStudents(List<Advisor> advisors, List<Course> courses){

        List<Student> studentList = new ArrayList<>();

        File studentsFolder = new File(studentsDir);
        File[] studentFiles = studentsFolder.listFiles();

        for(File f : studentFiles){
            if(!f.isFile())
                continue;

            JSONObject studentJson = (JSONObject)readJsonFile(f);

            String studentID = (String) studentJson.get("ID");
            Grade grade = Grade.valueOf((String) studentJson.get("Grade"));
            String advisorName = (String) studentJson.get("AdvisorName");
            Advisor advisor = findAdvisorByName(advisors,advisorName);

            StringBuilder firstName = new StringBuilder();
            StringBuilder middleName = new StringBuilder();
            StringBuilder lastName = new StringBuilder();
            parseHuman(studentJson,firstName,middleName,lastName);
            JSONArray courseRecordsJson = (JSONArray)studentJson.get("CourseRecords");
            List<CourseRecord> courseRecords = parseCourseRecords(courseRecordsJson,courses);

            String mName = isNull(middleName.toString()) ? null : middleName.toString();

            studentList.add(new Student(firstName.toString(),mName,lastName.toString(),studentID,grade,advisor,courseRecords));
        }

        return studentList;
    }
    private static void parseHuman(JSONObject obj, StringBuilder firstName, StringBuilder middleName, StringBuilder lastName){

        firstName.append((String)obj.get("FirstName"));
        middleName.append((String) obj.get("MiddleName"));
        lastName.append((String) obj.get("LastName"));
    }
    private static boolean isNull(String s){
        return s.equals("null");
    }

    private static void assignPrerequisitesToCourses(List<Course> courses,List<JSONArray> prerequisiteCodes){
        int len = courses.size();

        for(int i = 0; i < len; i++){
            Course course = courses.get(i);
            JSONArray codes = prerequisiteCodes.get(i);

            for(int j = 0; j < codes.size(); j++){
                String code = (String) codes.get(j);
                Course prerequisite = findCourseWithCode(courses,code);
                course.addPrerequisite(prerequisite);
            }
        }
    }
    private static Course findCourseWithCode(List<Course> courses,String courseCode){
        int len = courses.size();
        Course course = null;

        for(int i = 0; i < len; i++){
            course = courses.get(i);

            if(course.getCode().equals(courseCode))
                break;
        }
        return course;
    }

    private static List<CourseRecord> parseCourseRecords(JSONArray recordsJson,List<Course> courses){

        List<CourseRecord> courseRecords = new ArrayList<>();

        for(Object r : recordsJson){

            JSONObject record = (JSONObject) r;

            String courseCode = (String) record.get("CourseCode");
            Course course = findCourseWithCode(courses,courseCode);
            LetterGrade letterGrade = LetterGrade.valueOf((String) record.get("LetterGrade"));
            Float grade = (float) (double)record.get("Grade");
            Season season = Season.valueOf((String) record.get("Season"));
            Grade recordYear = Grade.valueOf((String) record.get("RecordYear"));
            Boolean passed = (Boolean) record.get("Passed");

            courseRecords.add(new CourseRecord(course,letterGrade,season,recordYear,grade,passed));
        }

        return courseRecords;
    }

    private static Advisor findAdvisorByName(List<Advisor> advisors,String advisorName){
        for(var adv : advisors){
            if(adv.getFullName().equals(advisorName)){
                return adv;
            }
        }

        return null;
    }
    private static Object readJsonFile(String fileName){
        return readJsonFile(new File(fileName));
    }

    private static Object readJsonFile(File file){
        JSONParser parser = new JSONParser();
        Object jsonObj = null;
        // read from a json file to parse
        try (FileReader reader = new FileReader(file))
        {
            //Read JSON file
            jsonObj = parser.parse(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }
}
