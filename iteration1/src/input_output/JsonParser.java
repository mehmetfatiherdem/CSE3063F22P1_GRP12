package iteration1.src.input_output;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import iteration1.src.course.*;
import iteration1.src.human.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonParser {

    private final String advisorsFile = "iteration1/resources/Advisors.json";
    private final String assistantsFile = "iteration1/resources/Assistants.json";
    private final String lecturersFile = "iteration1/resources/Lecturers.json";
    private final String coursesFile = "iteration1/resources/Courses.json";
    private final String semesterFile = "iteration1/resources/Semester.json";
    private final String studentsDir = "iteration1/resources/students/";

    public JsonParser(){

    }

    public Season parseSemester(){

        String semester = (String) readJsonFile(semesterFile);
        return Season.valueOf(semester);
    }

    public List<Advisor> parseAdvisors(){

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

    public List<Assistant> parseAssistants() {

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

    public List<Lecturer> parseLecturers(){

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

    public List<Course> parseCourses(){

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

    public List<Student> parseStudents(List<Advisor> advisors, List<Course> courses){

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

    public void serializeStudents(List<Student> students){

        for(Student s : students){
            List<CourseRecord> records = s.getTranscript().getTakenCourseRecords();

            JSONArray recordsJson = new JSONArray();

            for(CourseRecord r : records){
                JSONObject record = new JSONObject();

                Float grade = r.getScore();
                grade = grade == null ? -1 : grade;

                record.put("CourseCode", r.getCourse().getCode());
                record.put("LetterGrade",r.getlGrade().toString());
                record.put("Grade",grade);
                record.put("Season",r.getSeason().toString());
                record.put("RecordYear",r.getGrade().toString());
                record.put("Passed",r.getIsPassed());

                recordsJson.add(record);
            }

            JSONObject studentJson = new JSONObject();
            String mName = s.getMiddleName() == null ? "null" : s.getMiddleName();

            studentJson.put("ID",s.getStudentID());
            studentJson.put("Grade",s.getGrade().toString());
            studentJson.put("AdvisorName",s.getAdvisor().getFullName());
            studentJson.put("CourseRecords",recordsJson);
            studentJson.put("FirstName",s.getFirstName());
            studentJson.put("MiddleName",mName);
            studentJson.put("LastName",s.getLastName());

            String jsonString = studentJson.toJSONString();
            writeToFile(jsonString,studentsDir + s.getStudentID() + ".json");
        }
    }
    private void parseHuman(JSONObject obj, StringBuilder firstName, StringBuilder middleName, StringBuilder lastName){

        firstName.append((String)obj.get("FirstName"));
        middleName.append((String) obj.get("MiddleName"));
        lastName.append((String) obj.get("LastName"));
    }
    private boolean isNull(String s){
        return s.equals("null");
    }

    private void assignPrerequisitesToCourses(List<Course> courses,List<JSONArray> prerequisiteCodes){
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
    private Course findCourseWithCode(List<Course> courses,String courseCode){
        int len = courses.size();
        Course course = null;

        for(int i = 0; i < len; i++){
            course = courses.get(i);

            if(course.getCode().equals(courseCode))
                break;
        }
        return course;
    }

    private List<CourseRecord> parseCourseRecords(JSONArray recordsJson,List<Course> courses){

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

            grade = grade < 0 ? null : grade;

            courseRecords.add(new CourseRecord(course,letterGrade,season,recordYear,grade,passed));
        }

        return courseRecords;
    }

    private Advisor findAdvisorByName(List<Advisor> advisors,String advisorName){
        for(var adv : advisors){
            if(adv.getFullName().equals(advisorName)){
                return adv;
            }
        }

        return null;
    }
    private Object readJsonFile(String fileName){
        return readJsonFile(new File(fileName));
    }

    private Object readJsonFile(File file){
        JSONParser parser = new JSONParser();
        Object jsonObj = null;
        // read from a json file to parse
        try (FileReader reader = new FileReader(file))
        {
            //Read JSON file
            jsonObj = parser.parse(reader);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    private void writeToFile(String str,String fileName){
        try(FileWriter writer = new FileWriter(fileName)){
            writer.write(str);
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
