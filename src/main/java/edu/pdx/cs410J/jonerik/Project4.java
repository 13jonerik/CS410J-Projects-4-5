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


        String hostName = map.get("host");
        int portString = Integer.parseInt(map.get("port"));


        /*
        if (Parser.print) {
            //execute print
        }

        if (Parser.search) {
            // execute search
        }
        */

        //String customer = map.get("customer");
        //PhoneBillRestClient client = new PhoneBillRestClient();
        //System.out.println(client);

        //HttpRequestHelper.Response response;

        postCall(map);
        //try {

            //checkResponseCode( HttpURLConnection.HTTP_OK, response);
            /*
            if (customer == null) {
                // Print all key/value pairs
                response = client.getAllKeysAndValues();

            } else if (map.get(customer) == null) {
                // Print all values of key
                response = client.getValues(map);

            } else {
                // Post the key/value pair
                response = client.postCall(map);
            }

        */

            /*
        } catch ( IOException ex ) {
            error("While contacting server: " + ex);
            return;
        }

        if (response != null) {
            System.out.println(response.getContent());
        }
        */

        System.exit(0);
    }


    private static HttpRequestHelper.Response postCall(Map<String, String> map) {
        String hostName = map.get("host");
        int port = Integer.parseInt(map.get("port"));
        //System.out.println(port);

        PhoneCall call = new PhoneCall(map.get("callerNumber"), map.get("calleeNumber"),
                map.get("startTime"), map.get("endTime"));

        PhoneBill bill = new PhoneBill(map.get("customer"));
        bill.addPhoneCall(call);


        String customer = map.get("customer");
        PhoneBillRestClient client = new PhoneBillRestClient(hostName, port, customer);
        HttpRequestHelper.Response response;

        try {
            response = client.postCall(map);
            checkResponseCode(HttpURLConnection.HTTP_OK, response);
            return response;
        } catch (IOException e) {
            System.err.println("Err here");
            e.printStackTrace();
            return null;
        }

        // add the customer, bill to the map response = client.addKeyValuePair(//);




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