package edu.pdx.cs410J.jonerik;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.PhoneBillDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * The Pretty Printer is used to format a phone bill
 * too look like a bill you would see in real life,
 * thus making the phone bill information look nice.
 */
public class PrettyPrinter implements PhoneBillDumper {

    //private DataOutputStream call;
    private PrintWriter call;

    /**
     * Constructor for the PrettyPrinter
     * @param call takes in a stream from
     * Project3 and uses the stream in dump
     */
    //public PrettyPrinter(DataOutputStream call) {
    //    this.call = call;
    //}

    /**
     * Created new constructor for Print Writer to integrate
     * with Servlet
     * @param pretty takes in a PrintWriter pw
     */
    public PrettyPrinter(PrintWriter pretty) {
        this.call = pretty;
    }

    /**
     * This dump function takes in a phone bill and dumps it to a file
     * much like the text dumper, but this time in a pretty format like
     * one you would see in the real world. Dumps to a file that is
     * passed in with the constructor of PrettyPrinter
     */
    public void dump(AbstractPhoneBill bill) throws IOException {

        PhoneBill bill1 = (PhoneBill) bill;
        String formatString = "%77s";

        //call.write("testing\n");

        //String customer = bill1.getCustomer();
        //call.write(customer);

        call.write(String.format(formatString, String.valueOf("*** " + bill1.getCustomer()) + "'s Phone Bill ***"));
        call.write("\n\n");

        List<PhoneCall> temp = (List<PhoneCall>) bill.getPhoneCalls();
        //int length = temp.size();
        //temp = removeDuplicates(temp, length);

        Collections.sort(temp);

        int length = temp.size();
        temp = removeDuplicates(temp, length);

        formatString = "%-15s %-5s %-10s %-5s %-16s %-6s %-15s %-18s %s%n";

        call.write(String.format(formatString, "    Caller", "|", "Callee", "|", "Start Time", "|", "End Time", "|", "Duration"));
        call.write("-------------------------------------------------------"
                + "------------------------------------------------------------------\n");


        formatString = "%-15s %-2s %-13s %-2s %-18s %-2s %-16s %-2s %s%n";

        for (AbstractPhoneCall each : temp) {

            Long duration = Math.abs(each.getStartTime().getTime() - each.getEndTime().getTime());
            Long durationHours      = TimeUnit.MILLISECONDS.toHours(duration);
            Long durationMins       = TimeUnit.MILLISECONDS.toMinutes(duration);
            durationMins = durationMins % 60;


            call.write(String.format(formatString, "  " + each.getCaller(), "|", each.getCallee(), "|",
                    each.getStartTimeString(), "|", each.getEndTimeString(), "|",
                    " -> Duration: " + durationHours + " Hours and " + durationMins + " minutes!"));
                    }


            call.write("---------------------------------------------------------------------------" +
                    "----------------------------------------------\n");

        call.flush();
        call.close();
    }

    /**
     * This version of the dumper formats the bill the same way as above,
     * but this time outputs to the console.
     */
    public void dumpToConsole(AbstractPhoneBill bill) {

        String formatString = "%77s";

        PhoneBill bill1 = (PhoneBill) bill;

        Print(String.format(formatString, String.valueOf("*** " + bill1.getCustomer()) + "'s Phone Bill ***"));
        Print("\n\n");

        List<PhoneCall> temp = (List<PhoneCall>) bill1.getPhoneCalls();

        Collections.sort(temp);
        int length = temp.size();
        temp = removeDuplicates(temp, length);


        formatString = "%-15s %-5s %-10s %-5s %-16s %-6s %-15s %-18s %s%n";
        Print(String.format(formatString, "    Caller", "|", "Callee", "|", "Start Time", "|", "End Time", "|", "Duration"));
        Print("----------------------------------------------------------------"
                + "---------------------------------------------------------\n");
        formatString = "%-15s %-2s %-13s %-2s %-18s %-2s %-16s %-2s %s%n";

        for (AbstractPhoneCall each : temp) {


            Long duration = Math.abs(each.getStartTime().getTime() - each.getEndTime().getTime());

            Long durationHours      = TimeUnit.MILLISECONDS.toHours(duration);
            Long durationMins       = TimeUnit.MILLISECONDS.toMinutes(duration);
            durationMins = durationMins % 60;


            Print(String.format(formatString, "  " + each.getCaller(), "|", each.getCallee(), "|",
                    each.getStartTimeString(), "|", each.getEndTimeString(), "|",
                    " -> Duration: " + durationHours + " Hours and " + durationMins + " minutes!"));
            Print("--------------------------------------------------------------"
                    + "-----------------------------------------------------------\n");
        }

    }

    /**
     * This helper function takes in the list of phone calls and returns
     * a new list, but without duplicates. A phone call is considered a
     * duplicate if it has the same start time and the same caller
     * number.
     */
    public List<PhoneCall> removeDuplicates(List<PhoneCall> temp, int length) {
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                for (int j = i + 1; j < length; j++) {
                    if (temp.get(i).compareTo(temp.get(j)) == 0) {
                        temp.remove(temp.get(i));
                        --length;
                        removeDuplicates(temp, length);
                        return temp;
                    }
                }
            }
            return temp;

        } else {
            return temp;
        }
    }


    /**
     * Helper print function
     */
    public static void Print(String str){
        System.out.println(str);
    }


}