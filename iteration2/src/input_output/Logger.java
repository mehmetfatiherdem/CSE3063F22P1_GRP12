package iteration2.src.input_output;

import iteration2.src.course.Section;

import javax.crypto.SecretKey;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class Logger {
    private static final String logTxt = "iteration2/output/log.txt";
    private static boolean enabled = true;

    private Logger(){
    }

    public static void enable(){
        enabled = true;
    }

    public static void disable(){
        enabled = false;
    }
    public static void log(String message){
        if(!enabled)
            return;

        System.out.println(message);

        var writer = openLogFile();
        writer.append(message + "\n");
        closeLogFile(writer);
    }

    public static void log(Object obj){
        if(!enabled)
            return;

        System.out.println(obj);

        var writer = openLogFile();
        writer.append(obj + "\n");
        closeLogFile(writer);
    }

    public static void logStudentSchedule(List<Section> enrolledSections, HorizontalLineType horizontalLineType, char verticalLine){
        var schedule = Section.combineSchedules(enrolledSections);
        StringBuilder scheduleBuilder = new StringBuilder();
        int cellWidth = 14;
        String horizontalLine;

        if(verticalLine == ' ' || verticalLine == '\0' || verticalLine == '\t' || verticalLine == '\n')
            verticalLine = '|';

        if(horizontalLineType == HorizontalLineType.Dash)
            horizontalLine = "\n-------------------------------------------------------------------------------------------------------------------------\n";
        else if(horizontalLineType == HorizontalLineType.Star)
            horizontalLine = "\n*************************************************************************************************************************\n";
        else if(horizontalLineType == HorizontalLineType.Dot)
            horizontalLine = "\n.........................................................................................................................\n";
        else
            horizontalLine = "\n=========================================================================================================================\n";

        scheduleBuilder.append(horizontalLine);
        scheduleBuilder.append(verticalLine + "              " + verticalLine + "    Monday    " + verticalLine + "   Tuesday    " +
                verticalLine + "  Wednesday   " + verticalLine + "   Thursday   " + verticalLine + "    Friday    " + verticalLine +
                "   Saturday   " + verticalLine + "    Sunday    " + verticalLine);

        for (int i = 0; i < 8; i++){
            String classHour = Section.CLASS_HOURS[i];
            scheduleBuilder.append(horizontalLine);
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

        log(scheduleBuilder.toString());
    }

    private static void addCell(String cellText,char verticalLine ,StringBuilder schedule){
        int cellWidth = 14;

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