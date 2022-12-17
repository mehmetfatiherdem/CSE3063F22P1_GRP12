package iteration2.src.input_output;

import iteration2.src.Department;
import iteration2.src.MathHelper;
import iteration2.src.course.*;
import iteration2.src.human.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class Logger {
    private static final String logTxt = "iteration2/output/log.txt";
    private static final String unitIndentation = "    ";

    private static boolean enabled = true;
    private static int indentation = 0;
    private static boolean ignoreIndentation = false;

    private static int cellWidth = 14;

    private Logger(){
    }

    public static void enable(){
        enabled = true;
    }

    public static void disable(){
        enabled = false;
    }

    public static void clearLogFile(){
        closeLogFile(openLogFile(false));
    }

    public static void decrementIndentation(){
        setIndentation(indentation - 1);
    }

    public static void incrementIndentation(){
        indentation++;
    }

    public static void setIndentation(int indent){
        indent = indent < 0 ? 0 : indent;
        indentation = indent;
    }

    public static int getIndentation(){
        return indentation;
    }

    private static String getIndentationString(){
        StringBuilder indentationBuilder = new StringBuilder("");

        for(int i = 0; i < indentation; i++){
            indentationBuilder.append(unitIndentation);
        }

        return indentationBuilder.toString();
    }

    public static void newLine(){
        ignoreIndentation = true;
        log("");
        ignoreIndentation = false;
    }

    public static void newLine(HorizontalLineType lineType){
        ignoreIndentation = true;

        switch (lineType){
            case Dash:
                log("---------------------------------------------------------------------------------------------------" +
                        "-----------------------------------------------------------------------------------------------" +
                        "-----------------------------------------------------------------------------------------------" +
                        "-----------------------------------------------------------------------------------------------" +
                        "-----------------------------------------------------------------------------------------------" +
                        "-------------------");
                break;
            case Dot:
                log("..................................................................................................." +
                        "..............................................................................................." +
                        "..............................................................................................." +
                        "..............................................................................................." +
                        "..............................................................................................." +
                        "...................");
                break;
            case Star:
                log("***************************************************************************************************" +
                        "***********************************************************************************************" +
                        "***********************************************************************************************" +
                        "***********************************************************************************************" +
                        "***********************************************************************************************" +
                        "*******************");
                break;
            case EqualsSign:
                log("===================================================================================================" +
                        "===============================================================================================" +
                        "===============================================================================================" +
                        "===============================================================================================" +
                        "===============================================================================================" +
                        "===================");
                break;
        }

        ignoreIndentation = false;
    }

    public static void log(String message){
        if(!enabled)
            return;

        String log = ignoreIndentation ? "" : getIndentationString();
        log += message + "\n";

        var writer = openLogFile(true);

        writer.append(log);
        System.out.print(log);

        closeLogFile(writer);
    }

    public static void logSchedule(long schedule){
        StringBuilder scheduleString = new StringBuilder(Long.toBinaryString(schedule));

        for(int n=scheduleString.length(); n < 48; n++)
            scheduleString.insert(0, "0");

        scheduleString.insert(8,"_");
        scheduleString.insert(17,"_");
        scheduleString.insert(26,"_");
        scheduleString.insert(35,"_");
        scheduleString.insert(44,"_");

        log(scheduleString.toString());
    }

    public static void logStudentSchedule(List<Section> enrolledSections, HorizontalLineType horizontalLineType, char verticalLine){
        var schedule = Section.combineSchedules(enrolledSections);
        StringBuilder scheduleBuilder = new StringBuilder();
        String indentationString = getIndentationString();
        String horizontalLine;

        if(verticalLine == ' ' || verticalLine == '\0' || verticalLine == '\t' || verticalLine == '\n')
            verticalLine = '|';

        if(horizontalLineType == HorizontalLineType.Dash)
            horizontalLine = "\n" + indentationString + "---------------------------------------------------------------" +
                    "----------------------------------------------------------\n";
        else if(horizontalLineType == HorizontalLineType.Star)
            horizontalLine = "\n" + indentationString + "***************************************************************" +
                    "**********************************************************\n";
        else if(horizontalLineType == HorizontalLineType.Dot)
            horizontalLine = "\n" + indentationString + "..............................................................." +
                    "..........................................................\n";
        else
            horizontalLine = "\n" + indentationString + "===============================================================" +
                    "==========================================================\n";

        scheduleBuilder.append(horizontalLine);
        scheduleBuilder.append(indentationString + verticalLine + "              " + verticalLine + "    Monday    " + verticalLine + "   Tuesday    " +
                verticalLine + "  Wednesday   " + verticalLine + "   Thursday   " + verticalLine + "    Friday    " + verticalLine +
                "   Saturday   " + verticalLine + "    Sunday    " + verticalLine);

        for (int i = 0; i < 8; i++){
            String classHour = Section.CLASS_HOURS[i];
            scheduleBuilder.append(horizontalLine + indentationString);
            addCell(classHour, verticalLine, scheduleBuilder);

            for (int j = 0; j < 7; j++){
                Section section = schedule.get(j)[i];

                if(section == null){
                    scheduleBuilder.append(verticalLine);
                    appendGap(cellWidth,scheduleBuilder);
                }
                else{
                    addCell(section.toString(), verticalLine, scheduleBuilder);
                }
            }

            scheduleBuilder.append(verticalLine);
        }

        scheduleBuilder.append(horizontalLine);

        ignoreIndentation = true;
        log(scheduleBuilder.toString());
        ignoreIndentation = false;
    }

    public static void logCourseCodes(String frontText,List<Course> courses){
        StringBuilder builder = new StringBuilder(frontText);

        if(courses.size() > 0){
            for(Course c : courses){
                builder.append(c.getCode());
                builder.append(", ");
            }

            int len = builder.length();
            builder.delete(len - 2, len);
        }

        log(builder.toString());
    }

    public static void logSimulationEntities(){
        Department department = Department.getInstance();
        List<Student> students = department.getStudents();
        List<Lecturer> lecturers = department.getLecturers();
        List<Assistant> assistants = department.getAssistants();
        List<Advisor> advisors = department.getAdvisors();
        List<MandatoryCourse> mandatoryCourses = department.getMandatoryCourses();
        List<TechnicalElectiveCourse> technicalElectiveCourses = department.getTechnicalElectiveCourses();
        List<FacultyTechnicalElectiveCourse> facultyTechnicalElectiveCourses = department.getFacultyTechnicalElectiveCourses();
        List<NonTechnicalElectiveCourse> nonTechnicalElectiveCourses = department.getNonTechnicalElectiveCourses();

        newLine(HorizontalLineType.Star);
        newLine();
        log("SEMESTER : " + department.getCurrentSeason().toString());
        newLine();

        log("DEPARTMENT INFORMATION :");
        incrementIndentation();

        log("DEPARTMENT NAME : " + department.getDepartmentName());
        log("DEPARTMENT CODE : " + department.getDepartmentCode());

        log("LECTURERS :");
        logPeopleNames((List<Human>)(List<?>)lecturers);

        log("ASSISTANTS :");
        logPeopleNames((List<Human>)(List<?>)assistants);

        log("ADVISORS :");
        logPeopleNames((List<Human>)(List<?>)advisors);

        log("COURSES :");
        incrementIndentation();
        log("MANDATORY COURSES :");
        logCourses((List<Course>)(List<?>)mandatoryCourses);
        log("TECHNICAL ELECTIVE COURSES :");
        logCourses((List<Course>)(List<?>)technicalElectiveCourses);
        log("FACULTY TECHNICAL ELECTIVE COURSES :");
        logCourses((List<Course>)(List<?>)facultyTechnicalElectiveCourses);
        log("NON-TECHNICAL ELECTIVE COURSES :");
        logCourses((List<Course>)(List<?>)nonTechnicalElectiveCourses);
        decrementIndentation();

        log("STUDENTS : ");
        logStudents(students);

        decrementIndentation();
        newLine(HorizontalLineType.Star);
    }

    private static void addCell(String cellText,char verticalLine ,StringBuilder schedule){
        int textLength = cellText.length();
        int gapLength = cellWidth - textLength;
        int gapWidth = gapLength % 2 == 0 ? gapLength / 2 : (gapLength / 2) + 1;

        schedule.append(verticalLine);
        appendGap(gapWidth,schedule);
        schedule.append(cellText);

        gapWidth = cellWidth - textLength - gapWidth;
        appendGap(gapWidth,schedule);
    }

    private static void appendGap(int width, StringBuilder builder){
        for (int i = 0; i < width; i++)
            builder.append(" ");
    }

    private static void logPeopleNames(List<Human> people){
        incrementIndentation();

        for(Human h : people){
            log(h.getFullName());
        }

        decrementIndentation();
    }

    private static void logCourses(List<Course> courses){
        newLine();
        incrementIndentation();

        for (Course c : courses){
            log("COURSE CODE : " + c.getCode());
            log("COURSE NAME : " + c.getName());
            log("CREDITS : " + c.getCredits());
            log("QUOTA : " + c.getQuota());
            log("THEORETICAL HOURS : " + c.getTheoreticalHours());
            log("APPLIED HOURS : " + c.getAppliedHours());
            logCourseCodes("PREREQUISITES : ", c.getPrerequisites());
            newLine();
        }

        decrementIndentation();
    }

    private static void logStudents(List<Student> students){
        newLine();
        incrementIndentation();

        for (Student s : students){
            log("STUDENT NAME : " + s.getFullName());
            log("STUDENT ID : " + s.getStudentID());
            log("STUDENT GRADE : " + s.getGrade().toString());
            log("ADVISOR : " + s.getAdvisor().getFullName());
            log("COMPLETED CREDITS : " + s.getCompletedCredits());
            log("GPA : " + MathHelper.roundFloat(s.getTranscript().calculateGPA(),2));
            newLine();
        }

        decrementIndentation();
    }

    private static PrintWriter openLogFile(boolean append){
        PrintWriter writer = null;

        try{
            writer = new PrintWriter(new FileOutputStream(logTxt,append));
        }catch(FileNotFoundException exc){
            System.out.println("Output file not found!");
        }

        return writer;
    }

    private static void closeLogFile(PrintWriter writer){
        writer.flush();
        writer.close();
    }
}