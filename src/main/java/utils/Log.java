package utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Log {

    public enum type{
        INFO,
        DEBUG,
        ERROR,
        VALUE
    }

    public static void send(type t, String source, String message, String shouldbe, String is){
        if(t != type.VALUE){
            send(t, source, is);
        } else {
            System.out.println("--" + t.toString() + "-- " + source + ": " + message
                    + ". Should be: " + shouldbe + " / is: " + is);
        }
    }

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

    private static String stamp(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return formatter.format(date) + ": ";
    }

}
