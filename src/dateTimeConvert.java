import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

public class dateTimeConvert {
//    public static String toShortTime(String inputStr){
//        String res="";
////        System.out.println("CONVERTING "+inputStr.substring(11,13));
//        System.out.println(Integer.parseInt(inputStr.substring(11,13)));
//        if(Integer.parseInt(inputStr.substring(11,13))>12){
//            int hour = Integer.parseInt(inputStr.substring(11,13))-12;
//            res=hour+inputStr.substring(13,16)+" PM";
//        }else{
//            int hour = Integer.parseInt(inputStr.substring(11,13));
//            res=hour+inputStr.substring(13,16)+" AM";
//        }
////        res=inputStr.substring(10,16)+" AM";
//        return res;
//    }

    public static String toShortTime(Time inputStr){
        String res="";
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        res= dateFormat.format(inputStr);
        return res;
    }

    public static String toShortDate(Date input){
        String res="";
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        res= dateFormat.format(input);
        return res;
    }

    public static String parseHour(Time inputStr){
        String res="";
        DateFormat dateFormat = new SimpleDateFormat("hh");
        res= dateFormat.format(inputStr);
        return res;
    }
    public static String parseMinute(Time inputStr){
        String res="";
        DateFormat dateFormat = new SimpleDateFormat("mm");
        res= dateFormat.format(inputStr);
        return res;
    }
    public static String parseAM(Time inputStr){
        String res="";
        DateFormat dateFormat = new SimpleDateFormat("a");
        res= dateFormat.format(inputStr);
        return res;
    }

    public static String minutesToHours(int t){
        int hours = t / 60; //since both are ints, you get an int
        int minutes = t % 60;
        return String.format("%dh %02dm", hours, minutes);
    }

}
