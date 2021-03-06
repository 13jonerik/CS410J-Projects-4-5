package edu.pdx.cs410J.jonerik;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.util.Map;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send key/value pairs.
 */
public class PhoneBillRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "phonebill";
    private static final String SERVLET = "calls";

    private final String url;


    /**
     * Creates a client to the Phone Bill REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */

    /**
     * Constructor when the command line is specified to add a call to the server.
     */
    public PhoneBillRestClient( String hostName, int port, String customer)
    {
        this.url = String.format( "http://%s:%d/%s/%s?customer=%s", hostName, port, WEB_APP, SERVLET, customer );
    }

    /**
     * Constructor for executing a search, creates a URL for the search to dump to.
     */
    public PhoneBillRestClient( String hostName, int port, String customer, String startTime, String endTime) {
        this.url = String.format( "http://%s:%d/%s/%s?customer=%s&startTime=%s&endTime=%s",
                hostName, port, WEB_APP, SERVLET, customer, startTime, endTime);
    }

    /*
    public String returnUrl () {
        return this.url;
    }
    */

    /**
     * Returns all keys and values from the server
     */
    public Response getAllKeysAndValues() throws IOException
    {
        return get(this.url);
    }

    /**
     * Returns all values for the given key
     */
    public Response getValues( Map <String, String> map ) throws IOException {
        String customer = map.get("customer");
        return get(this.url, "customer", customer);
    }


    /**
     * Add a call to the url using the helper function post that occurs in the servlet.
     */
    public Response addCall(Map<String, String> map) throws IOException {

        String customer     = map.get("customer");
        //System.out.println(customer);
        String callerNumber = map.get("callerNumber");
        //System.out.println(callerNumber);
        String calleeNumber = map.get("calleeNumber");
        //System.out.println(calleeNumber);
        String startTime    = map.get("startTime");
        //System.out.println(startTime);
        String endTime      = map.get("endTime");
        //System.out.println(endTime);

        return post( this.url, "callerNumber", callerNumber,
                "calleeNumber", calleeNumber, "startTime", startTime,
                "endTime", endTime);
    }


}
