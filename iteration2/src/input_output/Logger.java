package iteration2.src.input_output;

import iteration2.src.course.Section;

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

    public static void log(String message){
        if(!enabled)
            return;

        String log = ignoreIndentation ? "" : getIndentationString();
        log += message + "\n";

        var writer = openLogFile();

        writer.append(log);
        System.out.print(log);

        closeLogFile(writer);
    }

    public static void logStudentSchedule(List<Section> enrolledSections, HorizontalLineType horizontalLineType, char verticalLine){
        var schedule = Section.combineSchedules(enrolledSections);
        StringBuilder scheduleBuilder = new StringBuilder();
        String indentationString = getIndentationString();
        String horizontalLine;

        if(verticalLine == ' ' || verticalLine == '\0' || verticalLine == '\t' || verticalLine == '\n')
            verticalLine = '|';

        if(horizontalLineType == HorizontalLineType.Dash)
            horizontalLine = "\n" + indentationString + "-------------------------------------------------------------------------------------------------------------------------\n";
        else if(horizontalLineType == HorizontalLineType.Star)
            horizontalLine = "\n" + indentationString + "*************************************************************************************************************************\n";
        else if(horizontalLineType == HorizontalLineType.Dot)
            horizontalLine = "\n" + indentationString + ".........................................................................................................................\n";
        else
            horizontalLine = "\n" + indentationString + "=========================================================================================================================\n";

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

    private static PrintWriter openLogFile(){
        PrintWriter writer = null;

        try{
            writer = new PrintWriter(new FileOutputStream(logTxt,true));
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