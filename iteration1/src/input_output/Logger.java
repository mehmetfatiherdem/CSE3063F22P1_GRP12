package iteration1.src.input_output;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Logger {
    private static final String logTxt = "iteration1/output/log.txt";

    private Logger(){
    }

    public static void log(String message){
        System.out.println(message);

        var writer = openLogFile();
        writer.append(message + "\n");
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