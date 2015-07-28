package edu.pdx.cs410J.jonerik;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>PhoneBill</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class PhoneBillServlet extends HttpServlet
{
    private final Map<String, PhoneBill> bills  = new HashMap<>();
    private final Map<String, String> data      = new HashMap<>();

    /**
     * Handles an HTTP GET request from a client by writing the value of the key
     * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
     * parameter is not specified, all of the key/value pairs are written to the
     * HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String customer     = getParameter("customer", request);

        String startTime    = getParameter("startTime", request);
        String endTime      = getParameter("endTime", request);

        if (customer != null && (startTime == null && endTime == null)){
            writeValue(customer, response);
        } else {
            Map <String, String> map = new HashMap<>();
            map.put("customer", customer);
            map.put("startTime", startTime);
            map.put("endTime", endTime);
        }

        // TODO: Check to make sure all params are there
        //writeValue(customer, response);

    }

    /**
     * Handles an HTTP POST request by storing the key/value pair specified by the
     * "key" and "value" request parameters.  It writes the key/value pair to the
     * HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NullPointerException {
        response.setContentType("text/plain");

        Map <String, String> map = setMap(request);
        PhoneCall call = new PhoneCall (map.get("callerNumber"), map.get("calleeNumber"),
                map.get("startTime"), map.get("endTime"));

        String name = map.get("customer");
        PhoneBill bill = bills.get(map.get(name));


        if (bill != null) {
            bill.addPhoneCall(call);
        } else {
            bills.put(name, new PhoneBill(name));
            bills.get(name).addPhoneCall(call);
        }

        PrintWriter pw = response.getWriter();
        pw.println(bill.toString());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);
    }


    protected Map<String, String> setMap (HttpServletRequest request) {
        Map <String, String> map = new HashMap<>();
        String customer = getParameter("customer", request);
        map.put("customer", customer);
        String callerNumber = getParameter("callerNumber", request);
        map.put("callerNumber", callerNumber);
        String calleeNumber = getParameter("calleeNumber", request);
        map.put("calleeNumber", calleeNumber);
        String startTime = getParameter("startTime", request);
        map.put("startTime", startTime);
        String endTime = getParameter("endTime", request);
        map.put("endTime", endTime);
        return map;
    }



    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println( Messages.missingRequiredParameter(parameterName));
        pw.flush();
        
        response.setStatus( HttpServletResponse.SC_PRECONDITION_FAILED );
    }

    /**
     * Writes the value of the given key to the HTTP response.
     *
     * The text of the message is formatted with {@link Messages#getMappingCount(int)}
     * and {@link Messages#formatKeyValuePair(String, PhoneBill)}
     */
    private void writeValue( String customer, HttpServletResponse response ) throws IOException
    {
        PhoneBill bill = this.bills.get(customer);

        PrettyPrinter prettify = new PrettyPrinter(response.getWriter());
        prettify.dump(bill);
        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Writes all of the key/value pairs to the HTTP response.
     *
     * The text of the message is formatted with
     * {@link Messages#formatKeyValuePair(String, PhoneBill)}
     */
    private void writeAllMappings( HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount(bills.size()));

        for (Map.Entry<String, PhoneBill> entry : this.bills.entrySet()) {
            pw.println(Messages.formatKeyValuePair(entry.getKey(), entry.getValue()));
        }

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

}
