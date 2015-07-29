package edu.pdx.cs410J.jonerik;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Phone Bill server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    public static void main(String... args) {
        Map<String,String> map = Parser.parseArgs(new ArrayList<>(Arrays.asList(args)));

        if ((Parser.host && !Parser.port) || (!Parser.host && Parser.port)) {
            System.err.println("Host and Port options must both be specified together. Command line" +
                    "contains one without the other. Check arguments and try again.");
            System.exit(1);
        } else if (Parser.host && Parser.port) {
            //execute post
            postCall(map);
        }

        if (Parser.print) {
            //execute print
            PhoneCall call = new PhoneCall(map.get("callerNumber"), map.get("calleeNumber"),
                    map.get("startTime"), map.get("endTime"));
            System.out.println(call.toString());
        }

        /*
        if (Parser.search) {
            // execute search
        }
        */

        System.exit(0);
    }


    private static HttpRequestHelper.Response postCall(Map<String, String> map) {
        HttpRequestHelper.Response response;
        String hostName = map.get("host");
        int port = Integer.parseInt(map.get("port"));
        //System.out.println(port);

        PhoneCall call = new PhoneCall(map.get("callerNumber"), map.get("calleeNumber"),
                map.get("startTime"), map.get("endTime"));

        PhoneBill bill = new PhoneBill(map.get("customer"));
        bill.addPhoneCall(call);

        String customer = map.get("customer");
        PhoneBillRestClient client = new PhoneBillRestClient(hostName, port, customer);
        //System.out.println(client.returnUrl());


        try {
            response = client.addCall(map);
            checkResponseCode(HttpURLConnection.HTTP_OK, response);
            //client.getValues(map);
            //client.getAllKeysAndValues();
            return response;
        } catch (IOException e) {
            System.err.println("** Connection to server could not be established. Please" +
                    " check connection and try running again.");
            return null;
        }

    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("This simple program posts key/value pairs to the server");
        err.println("If no value is specified, then all values are printed");
        err.println("If no key is specified, all key/value pairs are printed");
        err.println();

        System.exit(1);
    }
}