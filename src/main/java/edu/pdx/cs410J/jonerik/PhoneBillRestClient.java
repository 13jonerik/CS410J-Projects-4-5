package edu.pdx.cs410J.jonerik;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.text.MessageFormat;
import java.net.URLEncoder;

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


    public PhoneBillRestClient( String hostName, int port, String customer)
    {
        this.url = String.format( "http://%s:%d/%s/%s?customer=%s", hostName, port, WEB_APP, SERVLET, customer );
    }



    public static String urlBuilder(Map<String, String> map) {

        String host     = map.get("host");
        String port     = map.get("port");
        String customer = map.get("customer");


        try {
            customer = URLEncoder.encode(customer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Bad URL");
        }
        System.out.println(MessageFormat.format("http://{0}:{1}/{2}/{3}?customer={4}", host, port, WEB_APP, SERVLET, customer));
        return MessageFormat.format("http://{0}:{1}/{2}/{3}?customer={4}", host, port, WEB_APP, SERVLET, customer);
    }

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


    /*
    public Response addKeyValuePair( Map <String, String> map ) throws IOException {
        String customer =
        return post(this.url, "key", key, "value", value);
    }
    */

    public Response postCall(Map<String, String> map) throws IOException {

        String customer     = map.get("customer");
        System.out.println(customer);
        String callerNumber = map.get("callerNumber");
        System.out.println(callerNumber);
        String calleeNumber = map.get("calleeNumber");
        System.out.println(calleeNumber);
        String startTime    = map.get("startTime");
        System.out.println(startTime);
        String endTime      = map.get("endTime");
        System.out.println(endTime);

        return post( urlBuilder(map), "callerNumber", map.get(callerNumber),
                "calleeNumber", map.get(calleeNumber), "startTime", map.get(startTime),
                "endTime", map.get(endTime));
    }


}
