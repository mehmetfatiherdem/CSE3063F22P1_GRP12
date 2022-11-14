package iteration1.src.input_output;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class Logger {

    private static final String logTxt = "iteration1/output/log.txt";

    private Logger(){
    }

    public static void log(String message){
        System.out.println(message);

        var writer = OpenLogFile();
        writer.append(message + "\n");
        CloseLogFile(writer);
    }

    private static PrintWriter OpenLogFile(){
        PrintWriter writer = null;

        try{
            writer = new PrintWriter(new FileOutputStream(logTxt,true));
        }catch(FileNotFoundException exc){
            System.out.println("Output file not found!");
        }

        return writer;
    }

    private static void CloseLogFile(PrintWriter writer){
        writer.flush();
        writer.close();
    }
}