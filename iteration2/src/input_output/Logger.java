package iteration2.src.input_output;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

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