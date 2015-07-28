package edu.pdx.cs410J.jonerik;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is created to parse the command line
 * and validate the incoming arguments. The goal
 * of this class is to move functionality out of
 * the main class into its own class.
 */

public class Parser {

    public static String hostOption     = "-host";
    public static String portOption     = "-port";
    public static String printOption    = "-print";
    public static boolean print         = false;
    public static String readMeOption   = "-README";
    public static String searchOption   = "-search";
    public static boolean search        = false;


    public static Map<String, String> parseArgs (ArrayList <String> argList) {
        Map <String, String> argMap = new HashMap<>();
        checkForReadMe(argList);
        setHost(argMap, argList);
        setPort(argMap, argList);
        checkForPrint(argMap, argList);
        checkForSearch(argMap, argList);

        if(validateCall(argList)) {
            addCallArgsToMap(argMap, argList);
        }

        return argMap;

    }


    public static void checkForReadMe(ArrayList<String> list) {
        if (list.contains(readMeOption)){
            readMe();
            System.exit(0);
        }
    }


    public static ArrayList setHost(Map<String, String> map, ArrayList<String> list) {
        if (list.contains(hostOption)) {
            int index = list.indexOf(hostOption) + 1;
            map.put("host", list.get(index));
            list.remove(index);
            list.remove(index - 1);
        }

        else {
            Messages.missingRequiredParameter("Host");
            //System.err.println("** Must provide a valid host name argument");
            System.exit(1);
        }

        return list;

    }

    public static ArrayList setPort(Map<String, String> map, ArrayList<String> list) {
        if (list.contains(portOption)) {
            int index = list.indexOf(portOption) + 1;
            map.put("port", list.get(index));
            list.remove(index);
            list.remove(index - 1);
        }

        else {
            Messages.missingRequiredParameter("Port");
            //System.err.println("** Must provide a valid port name argument");
            System.exit(1);
        }

        return list;

    }

    public static ArrayList checkForPrint(Map<String, String> map, ArrayList<String> list) {
        if (list.contains(printOption)) {
            map.put("print", "-print");
            list.remove(list.indexOf("-print"));
            print = true;
        }

        return list;

    }

    public static ArrayList checkForSearch(Map<String, String> map, ArrayList<String> list) {
        if (list.contains(searchOption)){
            map.put("search", "-search");
            list.remove(list.indexOf("-search"));
            search = true;
        }

        return list;

    }

    public static void addCallArgsToMap(Map<String, String> map, ArrayList<String> list) {


        int i = 0;
        String customer     = list.get(i++);
        String callerNumber = list.get(i++);
        String calleeNumber = list.get(i++);
        String startTime    = list.get(i++) + " " + list.get(i++) + " " + list.get(i++);
        String endTime      = list.get(i++) + " " + list.get(i++) + " " + list.get(i);

        map.put("customer", customer);
        map.put("callerNumber", callerNumber);
        map.put("calleeNumber", calleeNumber);
        map.put("startTime", startTime);
        map.put("endTime", endTime);

    }

    private static boolean validateCall(ArrayList callInfo) {

        int i = 0;

        if (callInfo.size() < 9) {
            System.err.println("** Missing command line arguments in call info. Check call args and try again. ");
            System.exit(1);
        } else if (callInfo.size() > 9) {
            System.err.println("** Extraneous command line arguments in call info. Check call args and try again. ");
            System.exit(1);
        }

        String customer = (String) callInfo.get(i++);
        String callerNumber = (String) callInfo.get(i++);
        String calleeNumber = (String) callInfo.get(i++);
        String startTime = callInfo.get(i++) + " " + callInfo.get(i++) + " " + callInfo.get(i++);
        String endTime = callInfo.get(i++) + " " + callInfo.get(i++) + " " + callInfo.get(i);

        if (!customer.matches("[a-zA-Z\\s*' - - ! @ # $ % ^ & * ? 1 2 3 4 5 6 7 8 9 0 , .]+")) {
            System.err.println("Customer Name Invalid");
            System.exit(1);
        } else if (!callerNumber.matches("[0-9]{3}[-][0-9]{3}[-][0-9]{4}")) {
            System.err.println("Caller Number Invalid");
            System.exit(1);
        } else if (!calleeNumber.matches("[0-9]{3}[-][0-9]{3}[-][0-9]{4}")) {
            System.err.println("Callee Number Invalid");
            System.exit(1);
        } else if (!startTime.matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/[0-9]{2}([0-9]{2})?[\\s*][0-9][0-2]{0,1}[:][0-5][0-9][\\s*]((A|a|P|p))(M|m)")) {
            System.err.println("Start Time Invalid");
            System.exit(1);
        } else if (!endTime.matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/[0-9]{2}([0-9]{2})?[\\s*][0-9][0-2]{0,1}[:][0-5][0-9][\\s*]((A|a|P|p))(M|m)")) {
            System.err.println("End Time Invalid");
            System.exit(1);
        }

        return true;
    }


    private static void readMe() {

        DateFormat currDate = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        System.out.println("\nREADME:\n\n" +
                        "Jon-Erik Svenson\n" +
                        "Phone Bill Application for Project 1 version 1.0. " + currDate.format(date) + "\n\n" +
                        "This application is designed to record a call to a customers phone bill using\n" +
                        "command line arguments to pass necessary information. The main functionality\n" +
                        "in this version is parsing and validating each of the arguments to ensure a valid\n" +
                        "phone call. The order of the arguments and the corresponding types are as follows:\n\n" +
                        "   customer:           Person whose phone bill we’re modeling\n" +
                        "   callerNumber:       Phone number of caller\n" +
                        "   calleeNumber:       Phone number of person who was called\n" +
                        "   startTime:          Date and time call began (24-hour time)\n" +
                        "   endTime:            Date and time call ended (24-hour time)\n" +
                        "   options are (options may appear in any order):\n" +
                        "   -textFile file      where to read/write the phone bill\n" +
                        "   -print:             Prints a description of the new phone call\n" +
                        "   -README:            Prints a README for this project and exits\n" +
                        "   Date and time should be in the format: mm/dd/yyyy hh:mm"
        );

    }

}
