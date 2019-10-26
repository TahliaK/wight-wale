package utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Log {

    /**
     * This specifies the output format for the logs
     */
    public enum type{
        INFO,
        DEBUG,
        ERROR,
        VALUE
    }

    /**
     * Logs a VALUE with comparison (as Strings)
     * @param t Log.type.____ enum. If it does not equal VALUE, this will default to the other send.
     * @param source    Source string (TAG)
     * @param message   Message string
     * @param shouldbe  Value of the expected output
     * @param is        Value of the actual output
     */
    public static void send(type t, String source, String message, String shouldbe, String is){
        if(t != type.VALUE){
            send(t, source, is);
        } else {
            System.out.println("--" + t.toString() + "-- " + source + ": " + message
                    + ". Should be: " + shouldbe + " / is: " + is);
        }
    }

    /**
     * Logs a message to Println with formatting.
     * @param t     log type (INFO, ERROR, DEBUG or VALUE)
     * @param source    source of message (TAG)
     * @param message   message for output
     */
    public static void send(type t, String source, String message){
        switch(t) {
            case INFO:
                System.out.println(stamp() + "::" + t.toString() + ":: " + source + ": " + message);
                break;
            case DEBUG:
                System.out.println(t.toString() +"("+ source + ") "+ message);
                break;
            case ERROR:
                System.out.println("[[" + t.toString() + "]] " + source + ": " + message);
                break;
            case VALUE:
                System.out.println("--" + t.toString() + "-- " + source + ": " + message);
                break;
        }
    } //end send

    //this adds a timestamp to log outputs
    private static String stamp(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return formatter.format(date) + ": ";
    }

}
